use std::env;
use std::fs::{File, set_permissions, metadata};
use std::io::Write;
use std::os::unix::fs::PermissionsExt;
use std::process::Command;
use std::ffi::CString;

fn main() -> Result<(), Box<dyn std::error::Error>> {
    // The build.rs script should generate or copy the binary to the OUT_DIR.
    // Here we include that binary at compile time.
    // Make sure your build.rs puts the binary in $OUT_DIR as "embedded_binary"
    #[cfg(windows)]
    let binary_data = include_bytes!(concat!(env!("OUT_DIR"), "\\dryadsynth-graalvm"));
    #[cfg(not(windows))]
    let binary_data = include_bytes!(concat!(env!("OUT_DIR"), "/dryadsynth-graalvm"));

    // Determine a temp path and write the binary there.

    let mut temp_path = env::temp_dir();
    temp_path.push("dryadsynth-graalvm");

    if !temp_path.exists() {
        let mut file = File::create(&temp_path)?;
        file.write_all(binary_data)?;
        drop(file);

        let mut perms = metadata(&temp_path)?.permissions();
        perms.set_mode(0o755);
        set_permissions(&temp_path, perms)?;
    }

    #[cfg(unix)]
    {
        let c_path = CString::new(temp_path.to_str().unwrap()).unwrap();
        let mut argv: Vec<CString> = Vec::new();
        for arg in std::env::args() {
            argv.push(CString::new(arg).unwrap());
        }
        let argv_ptrs: Vec<*const i8> = argv.iter().map(|a| a.as_ptr()).chain(std::iter::once(std::ptr::null())).collect();

        unsafe {
            libc::execv(c_path.as_ptr(), argv_ptrs.as_ptr());
        }

        panic!("execve failed");
    }

    #[cfg(not(unix))]
    {
        let status = Command::new(&temp_path)
            .status()?;
        Ok(status.code().unwrap())
    }

}