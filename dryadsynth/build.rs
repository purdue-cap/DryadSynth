use core::time;
use std::env;
use std::fs::{self, File};
use std::io::{self, Cursor};
use std::os::linux::raw::stat;
use std::path::Path;
use std::process::Command;
extern crate flate2;
extern crate reqwest;
extern crate tar;
extern crate zip_extract;
extern crate glob;

use glob::glob;
use flate2::read::GzDecoder;
use reqwest::blocking::get;
use tar::Archive;
use zip_extract::extract;

struct Timestamps(Box<Path>, std::collections::HashMap<String, u64>);
impl Timestamps {
    pub fn new(output_dir: &Path) -> Self {
        let timestamps_path = output_dir.join("timestamps.json");
        if timestamps_path.exists() {
            let timestamps = fs::read_to_string(timestamps_path.as_path()).expect("Failed to read timestamps file");
            Timestamps(timestamps_path.into_boxed_path(), serde_json::from_str(&timestamps).expect("Failed to parse timestamps JSON"))
        } else {
            Timestamps(timestamps_path.into_boxed_path(), Default::default())
        }
    }   
    pub fn try_insert(&mut self, key: String) -> bool {
        let value = fs::metadata(&key).unwrap().modified().unwrap().elapsed().unwrap().as_millis() as u64;
        if let Some(old_value) = self.1.get(&key) {
            if *old_value == value {
                return false;
            }
        }
        self.1.insert(key, value);
        true
    }
    pub fn save(&mut self) {
        fs::write(&self.0, serde_json::to_string(&self.1).expect("Failed to convert timestamps to JSON"))
            .expect("Failed to write timestamps file");
    }
}



fn ensure_graalvm(output_dir: &Path) {
    // Check if the "graalvm" directory already exists
    if output_dir.join("graalvm").exists() {
        println!("Directory 'graalvm' exists. Exiting.");
        return;
    }

    let graalvm_variant = "graalvm-jdk-24";
    let base_url = "https://download.oracle.com/graalvm/24/latest";

    // OS and Architecture Detection
    let os = if cfg!(target_os = "windows") {
        "windows"
    } else if cfg!(target_os = "macos") {
        "macos"
    } else if cfg!(target_os = "linux") {
        "linux"
    } else {
        panic!("Unrecognized OS");
    };

    #[cfg(target_arch = "x86_64")]
    let arch = "x64";

    #[cfg(any(target_arch = "aarch64", target_arch = "arm"))]
    let arch = "aarch64";

    #[cfg(not(any(target_arch = "x86_64", target_arch = "aarch64", target_arch = "arm")))]
    compile_error!("Unsupported architecture");

    // Determine File Extension
    let ext = if os == "windows" { "zip" } else { "tar.gz" };

    // Construct the Download URL
    let download_url = format!("{}/{}_{}-{}_bin.{}", base_url, graalvm_variant, os, arch, ext);
    println!("Downloading GraalVM from: {}", download_url);

    // Downloading
    let response = get(&download_url).expect("Failed to send request");
    if !response.status().is_success() {
        panic!("Failed to download file: {}", response.status());
    }
    let bytes = response.bytes().expect("Failed to read response body");
    println!("Download complete.");

    // Unpacking
    println!("Unpacking GraalVM...");
    if ext == "zip" {
        // On Windows, extract ZIP files
        let cursor = Cursor::new(bytes);
        extract(cursor, &Path::new(output_dir), true).expect("Failed to extract ZIP file");
    } else {
        // On Unix-like systems, extract tar.gz files
        let tar = GzDecoder::new(Cursor::new(bytes));
        let mut archive = Archive::new(tar);
        archive.unpack(output_dir).expect("Failed to unpack archive");
    }
    println!("Unpack complete.");

    // Rename the extracted directory to "graalvm"
    fs::read_dir(output_dir).unwrap().find(|entry| 
        entry.as_ref().unwrap().file_name().to_string_lossy().starts_with(&graalvm_variant)
    ).map(|entry| 
        entry.unwrap().path()
    ).map(|path|
        fs::rename(path, output_dir.join("graalvm")).expect("Failed to rename directory")
    ).expect("Failed to find extracted directory");
}

fn main() {
    println!("cargo::rerun-if-changed=src");
    let output_dir_str = env::var("OUT_DIR").unwrap();
    let output_dir = Path::new(&output_dir_str);
    ensure_graalvm(&output_dir);
    
    assert!(output_dir.join("graalvm").join("bin").exists(), "GraalVM not retrieved successfully");
    unsafe{
        // Set the GRAALVM_HOME environment variable;
        env::set_var("GRAALVM_HOME",
            output_dir.join("graalvm").to_str().unwrap()
        );

        // Set the JAVA_HOME environment variable;
        env::set_var("JAVA_HOME",
            output_dir.join("graalvm").to_str().unwrap()
        );

        // Set the PATH environment variable;
        env::set_var("PATH",
            format!("{}/bin:{}", env::var("GRAALVM_HOME").unwrap(), env::var("PATH").unwrap())
        );

        // Set the LD_LIBRARY_PATH environment variable;
        env::set_var("LD_LIBRARY_PATH",
            format!("{}/lib:{}", env::var("GRAALVM_HOME").unwrap(), env::var("LD_LIBRARY_PATH").unwrap_or_default())
        );
    }

    let mut timestamps = Timestamps::new(output_dir);

    // Build the Sygus.g4 file
    let build_dir = output_dir.join("build");
    let classes_dir = output_dir.join("classes");
    for dir in [&build_dir, &classes_dir].iter() {
        if let Err(e) = fs::create_dir_all(dir) {
            if e.kind() != std::io::ErrorKind::AlreadyExists {
                panic!("Failed to create build directory: {}", e);
            }
        }
    }


    // Compile the Sygus.g4 file
    for g4 in glob("*.g4").expect("Failed to read glob pattern") {
        let g4 = g4.unwrap();
        let status = Command::new("java")
            .args(&["-cp", "lib/antlr.jar", "org.antlr.v4.Tool", "-o", build_dir.to_str().unwrap(), "-visitor", g4.to_str().unwrap()])
            .status()
            .expect("Failed to execute command for Sygus.g4");
        if !status.success() {
            panic!("Sygus.g4 generation failed");
        }
    }
    
    // Compile the all Java files
    let source_pathes = glob("src/*").unwrap()
        .map(|x| x.unwrap().to_str().unwrap().to_owned())
        .filter(|x| !x.ends_with(".rs"))
        .chain([build_dir.to_str().unwrap().to_owned()])
        .collect::<Vec<_>>().join(":");

    let class_pathes = glob("lib/*").unwrap()
        .map(|x| x.unwrap().to_str().unwrap().to_owned())
        .chain([classes_dir.to_str().unwrap().to_owned()])
        .collect::<Vec<_>>().join(":");

    println!("source_pathes: {}", source_pathes);
    println!("class_pathes: {}", class_pathes);

    for java in glob("src/*/*.java").expect("Failed to read glob pattern") {
        let java = java.unwrap();
        if !timestamps.try_insert(java.to_str().unwrap().into()) {
            continue;
        }
        let status = Command::new("javac")
            .args(&[
                "-classpath", class_pathes.as_str(),
                "-sourcepath", source_pathes.as_str(),
                "-d", classes_dir.to_str().unwrap(),
                java.to_str().unwrap()])
            .status()
            .expect("Failed to execute command for Sygus.java");
        
        timestamps.save();
        if !status.success() {
            panic!("javac failed: {}", java.to_str().unwrap());
        }
    }
    
    println!("PATH: {}", env::var("PATH").unwrap_or_default());
    {
        let dest_dir = classes_dir.join("META-INF/native-image");
        let _ = fs::create_dir_all(&dest_dir);
        fs::copy("reachability-metadata.json", dest_dir.join("reachability-metadata.json"))
            .expect("Failed to copy reachability-metadata.json");
    }
    let status = Command::new("native-image")
        .args(&["--static-nolibc", "--color=always",
            "--no-fallback", "--link-at-build-time", "--exact-reachability-metadata",
            "--initialize-at-build-time", "--initialize-at-run-time=com.microsoft.z3.Native",
            "-cp", class_pathes.as_str(), "Run", "-o", output_dir.join("dryadsynth-graalvm").to_str().unwrap()])
        .status()
        .expect("Failed to execute native-image command");
        
    if !status.success() {
        panic!("native-image generation failed: {}", status);
    }
    let status = Command::new(output_dir.join("dryadsynth-graalvm").to_str().unwrap())
        .status()
        .expect("Failed to execute dryadsynth-graalvm");
    assert!(status.success(), "Failed to execute dryadsynth-graalvm");
}
