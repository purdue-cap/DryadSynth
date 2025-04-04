use std::time::SystemTime;
use std::{env, fs};

use std::path::Path;
use std::process::Command;
extern crate flate2;
extern crate reqwest;
extern crate tar;
extern crate zip_extract;
extern crate glob;

use glob::glob;
use std::fs::File;
use std::io::{Read, Write};

struct Timestamps(Box<Path>, std::collections::HashMap<String, u64>);
impl Timestamps {
    pub fn new(output_dir: &Path) -> Self {
        let timestamps_path = output_dir.join("timestamps.json");
        if timestamps_path.exists() {
            let timestamps = fs::read_to_string(timestamps_path.as_path()).expect("Failed to read timestamps file");
            let parsed = serde_json::from_str(&timestamps).expect("Failed to parse timestamps JSON");
            Timestamps(timestamps_path.into_boxed_path(), parsed)
        } else {
            Timestamps(timestamps_path.into_boxed_path(), Default::default())
        }
    }   
    pub fn try_insert(&mut self, key: String) -> bool {
        let value = fs::metadata(&key).unwrap().modified().unwrap().duration_since(SystemTime::UNIX_EPOCH).unwrap().as_millis() as u64;
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

mod graalvm;
mod z3;

fn main() {
    println!("cargo::rerun-if-changed=src");
    let output_dir_str = env::var("OUT_DIR").unwrap();
    let output_dir = Path::new(&output_dir_str);
    graalvm::ensure(&output_dir);
    #[cfg(target_os = "linux")]
    let graalvm_home = output_dir.join("graalvm").to_str().unwrap().to_owned();
    #[cfg(target_os = "macos")]
    let graalvm_home = output_dir.join("graalvm").join("Contents").join("Home").to_str().unwrap().to_owned();
    
    
    unsafe {
        // Set the GRAALVM_HOME environment variable;
        env::set_var("GRAALVM_HOME", &graalvm_home);

        // Set the JAVA_HOME environment variable;
        env::set_var("JAVA_HOME", &graalvm_home);

        // Set the PATH environment variable;
        env::set_var("PATH",
            format!("{}/bin:{}", &graalvm_home, env::var("PATH").unwrap())
        );

        // Set the LD_LIBRARY_PATH environment variable;
        env::set_var("LD_LIBRARY_PATH",
            format!("{}/lib:{}", &graalvm_home, env::var("LD_LIBRARY_PATH").unwrap_or_default())
        );
    }

    z3::ensure(&output_dir);
    let z3_dir = output_dir.join("z3");
    {
        #[cfg(target_os = "linux")]
        let z3_lib_path = z3_dir.join("build").join("libz3java.so");
        #[cfg(target_os = "macos")]
        let z3_lib_path = z3_dir.join("build").join("libz3java.dylib");
        if !z3_lib_path.exists() {
            panic!("Required file {:?} does not exist", z3_lib_path);
        }
    }

    let mut timestamps = Timestamps::new(output_dir);
    println!("PATH: {}", env::var("PATH").unwrap_or_default());

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
        if !timestamps.try_insert(g4.to_str().unwrap().into()) {
            continue;
        }
        println!("Compiling g4: {}", g4.to_str().unwrap());
        
        let status = Command::new("java")
            .args(&["-cp", "lib/antlr.jar", "org.antlr.v4.Tool", "-o", build_dir.to_str().unwrap(), "-visitor", g4.to_str().unwrap()])
            .status()
            .expect("Failed to execute command for Sygus.g4");
        
        timestamps.save();
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
        .chain([z3_dir.join("build").join("com.microsoft.z3.jar").to_str().unwrap().to_owned()])
        .collect::<Vec<_>>().join(":");

    println!("source_pathes: {}", source_pathes);
    println!("class_pathes: {}", class_pathes);

    for java in glob("src/*/*.java").expect("Failed to read glob pattern") {
        let java = java.unwrap();
        if !timestamps.try_insert(java.to_str().unwrap().into()) {
            continue;
        }
        println!("Compiling java: {} {}", java.to_str().unwrap(), 
            fs::metadata(&java).unwrap().modified().unwrap().elapsed().unwrap().as_millis() as u64);
        let status = Command::new("javac")
            .args(&[
                "-classpath", class_pathes.as_str(),
                "-sourcepath", source_pathes.as_str(),
                "-d", classes_dir.to_str().unwrap(),
                java.to_str().unwrap()])
            .status()
            .expect("Failed to execute javac command");
        
        timestamps.save();
        if !status.success() {
            panic!("javac failed: {}", java.to_str().unwrap());
        }
    }
    
    {
        let dest_dir = classes_dir.join("META-INF/native-image");
        let _ = fs::create_dir_all(&dest_dir);
        fs::copy("reachability-metadata.json", dest_dir.join("reachability-metadata.json"))
            .expect("Failed to copy reachability-metadata.json");
    }
    let status = Command::new("native-image")
        .args(&["--static-nolibc", "--color=always",
            "--no-fallback", "--link-at-build-time", "--exact-reachability-metadata", "--enable-native-access=ALL-UNNAMED",
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

    {
        
        // Open the generated binary file
        let binary_path = output_dir.join("dryadsynth-graalvm");
        let mut file = File::open(&binary_path).expect("Failed to open dryadsynth-graalvm");
        let mut buffer = Vec::new();
        file.read_to_end(&mut buffer).expect("Failed to read dryadsynth-graalvm");

        // Compute MD5 hash using the md5 crate
        let digest = md5::compute(&buffer);

        // Write the hexadecimal md5sum to dryadsynth-graalvm.md5sum
        let md5sum_path = output_dir.join("dryadsynth-graalvm.md5sum");
        let mut out_file = File::create(&md5sum_path).expect("Failed to create md5sum file");
        write!(out_file, "{:x}", digest).expect("Failed to write md5sum");
    }
}
