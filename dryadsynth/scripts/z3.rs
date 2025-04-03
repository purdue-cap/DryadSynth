use std::io::Cursor;
use std::path::Path;
use std::fs;

pub fn ensure(output_dir: &Path) {

    let z3_dir = output_dir.join("z3");
    if z3_dir.exists() {
        println!("Z3 directory already exists, skipping build.");
        return;
    }

    let url = "https://github.com/Z3Prover/z3/archive/refs/tags/Z3-4.8.5.zip";

    let z3_src_dir = output_dir.join("z3src");
    if z3_src_dir.exists() {
        println!("Z3 directory already exists, skipping download and unpack.");
    } else {
        println!("Downloading Z3 4.8.5.");
        let response = reqwest::blocking::get(url).expect("Failed to send request");
        if !response.status().is_success() {
            panic!("Failed to download file: {}", response.status());
        }
        let bytes = response.bytes().expect("Failed to read response body");
        println!("Download complete.");
        
        println!("Unpacking Z3...");
        let cursor = Cursor::new(bytes);
        zip_extract::extract(cursor, &z3_src_dir, true).expect("Failed to extract ZIP file");
        println!("Unpack complete.");
    }

    println!("Building Z3...");
    let _ = cmake::Config::new(z3_src_dir.clone())
        .define("BUILD_JAVA_BINDINGS", "TRUE")
        .define("BUILD_LIBZ3_SHARED", "TRUE")
        .out_dir(&output_dir.join("z3"))
        // .generator("Unix Makefiles")
        .build();
    println!("Build complete.");

    fs::remove_dir_all(z3_src_dir).expect("Failed to remove Z3 source directory.");
}