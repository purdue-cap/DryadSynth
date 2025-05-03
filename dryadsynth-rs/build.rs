fn main() {
    println!("cargo:rerun-if-changed=../schema/sygus.capnp");
    let out_dir = std::env::var("OUT_DIR").unwrap();

    capnpc::CompilerCommand::new()
        .src_prefix("../schema")
        .file("../schema/sygus.capnp")
        .output_path(&out_dir)
        .run()
        .expect("capnpc compile failed");
}
