// dryadsynth/scripts/java_capnp.rs

use std::path::Path;
use std::fs;
use std::io::Write;
use std::io::Cursor;
use reqwest;

pub fn ensure(schema_dir: &Path) {
    let target = schema_dir.join("java.capnp");
    if target.exists() {
        println!("java.capnp already vendored, skipping");
        return;
    }

    let url = "https://raw.githubusercontent.com/capnproto/capnproto-java/master/compiler/src/main/schema/capnp/java.capnp";
    println!("Downloading java.capnp from {url}");
    let resp = reqwest::blocking::get(url)
        .expect("Failed to request java.capnp")
        .bytes()
        .expect("Failed to read java.capnp response");

    if let Some(p) = target.parent() {
        fs::create_dir_all(p).expect("Failed to create schema dir");
    }

    let mut file = fs::File::create(&target).expect("Failed to create java.capnp");
    file.write_all(&resp).expect("Failed to write java.capnp");
    println!("java.capnp vendored to {}", target.display());
}
