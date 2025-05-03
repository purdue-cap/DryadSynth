use std::path::Path;
use std::fs;
use std::io::Cursor;
use std::time::Duration;
use reqwest::blocking::Client;
use flate2::bufread::GzDecoder;
use reqwest::blocking::get;
use tar::Archive;
use zip_extract::extract;

pub fn ensure(output_dir: &Path) {
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

    // Timeout
    let client = Client::builder().timeout(Duration::from_secs(600)).build().expect("Failed to build reqwest client");

    // Downloading
    let response = client.get(&download_url).send().expect("Failed to send request");
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
