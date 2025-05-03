use anyhow::Result;

fn main() -> Result<()> {
    let in_path = std::env::args().nth(1)
        .expect("usage: dryadsynth <file.sl>");
    let bytes   = dryadsynth_rs::encode_file(&in_path)?;
    let solved  = dryadsynth_rs::solve(&bytes)?;
    std::fs::write("solution.bin", solved)?;
    println!("wrote solution.bin");
    Ok(())
}
