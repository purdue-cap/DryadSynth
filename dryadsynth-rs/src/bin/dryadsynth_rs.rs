use anyhow::Result;

fn main() -> Result<()> {
    // read .sl file
    let path = std::env::args().nth(1)
        .expect("usage: dryadsynth <problem.sl>");
    let src = std::fs::read_to_string(&path)?;

    // invoke dryadsynth_rs
    let solution = dryadsynth_rs::synthesize(&src)?;

    // output
    std::fs::write("solution.bin", solution)?;
    println!("wrote solution.bin");
    Ok(())
}
