use std::path::PathBuf;

fn main() {
    let dir: PathBuf = ["tree-sitter", "tree-sitter-python", "src"].iter().collect();

    cc::Build::new()
        .include(&dir)
        .file(dir.join("parser.c"))
        .compile("tree-sitter-python");
    cc::Build::new()
        .cpp(true)
        .include(&dir)
        .file(dir.join("scanner.cc"))
        .compile("tree-sitter-python-scanner");

    let dir2: PathBuf = ["tree-sitter", "tree-sitter-markdown", "src"].iter().collect();

    cc::Build::new()
        .include(&dir2)
        .file(dir2.join("parser.c"))
        .file(dir2.join("scanner.cc"))
        .compile("tree-sitter-markdown");
}