use std::{env, fs};
use std::ffi::CString;
use std::fs::{File, set_permissions, metadata};
use std::io::Write;
use std::os::unix::fs::PermissionsExt;
use std::path::PathBuf;
use std::path::Path;

fn create_dir_all<P: AsRef<std::path::Path>>(path: P) -> Result<(), Box<dyn std::error::Error>> {
    if let Err(e) = fs::create_dir_all(path) {
        if e.kind() != std::io::ErrorKind::AlreadyExists {
            panic!("Failed to create build directory: {}", e);
        }
    }
    Ok(())
}

fn binary_file<P: AsRef<std::path::Path>>(temp_path: P, binary_data: &[u8]) -> Result<(), Box<dyn std::error::Error>> {
    if !temp_path.as_ref().exists() {
        let mut file = File::create(&temp_path)?;
        file.write_all(binary_data)?;
        drop(file);

        let mut perms = metadata(&temp_path)?.permissions();
        perms.set_mode(0o777);
        set_permissions(&temp_path, perms)?;
    }
    Ok(())
}

#[cfg(target_os = "linux")]
fn load_z3() -> Result<PathBuf, Box<dyn std::error::Error>> {
    let libz3java_so = include_bytes!(concat!(env!("OUT_DIR"), "/z3/build/libz3java.so"));
    let libz3_so = include_bytes!(concat!(env!("OUT_DIR"), "/z3/build/libz3.so"));
    let mut temp_path = env::temp_dir();
    temp_path.push("z3-4.8.5");
    temp_path.push("lib");

    if !temp_path.exists() {
        create_dir_all(&temp_path)?;
        
        binary_file(temp_path.join("libz3java.so"), libz3java_so)?;
        binary_file(temp_path.join("libz3.so.4.8"), libz3_so)?;
    }
    
    Ok(temp_path)
}

#[cfg(target_os = "macos")]
fn load_z3() -> Result<PathBuf, Box<dyn std::error::Error>> {
    let libz3java_so = include_bytes!(concat!(env!("OUT_DIR"), "/z3/build/libz3java.dylib"));
    let libz3_so = include_bytes!(concat!(env!("OUT_DIR"), "/z3/build/libz3.dylib"));
    let mut temp_path = env::temp_dir();
    temp_path.push("z3-4.8.5");
    temp_path.push("lib");

    if !temp_path.exists() {
        create_dir_all(&temp_path)?;
        
        binary_file(temp_path.join("libz3java.dylib"), libz3java_so)?;
        binary_file(temp_path.join("libz3.4.8.5.0.dylib"), libz3_so)?;
    }
    
    Ok(temp_path)
}

fn main() -> Result<(), Box<dyn std::error::Error>> {
    {
        // Set the PATH environment variable to include the directory of the current executable
        if let Some(arg0) = env::args().next() {
            if let Some(dir) = Path::new(&arg0).parent() {
                let dir_str = dir.to_str().unwrap_or_default();
                let current_path = env::var("PATH").unwrap_or_default();
                let new_path = format!("{}:{}", dir_str, current_path);
                env::set_var("PATH", new_path);
            }
        }
    }

    // The build.rs script should generate or copy the binary to the OUT_DIR.
    // Here we include that binary at compile time.
    // Make sure your build.rs puts the binary in $OUT_DIR as "embedded_binary"
    let binary_data = include_bytes!(concat!(env!("OUT_DIR"), "/dryadsynth-graalvm"));

    let dryadsynth_filename = concat!("dryadsynth-", include_str!(concat!(env!("OUT_DIR"), "/dryadsynth-graalvm.md5sum")));

    let mut temp_path = env::temp_dir();
    temp_path.push(dryadsynth_filename);

    binary_file(&temp_path, binary_data)?;

    unsafe {
        #[cfg(target_os = "linux")]
        std::env::set_var("LD_LIBRARY_PATH", load_z3().unwrap().to_str().unwrap());
        #[cfg(target_os = "macos")]
        std::env::set_var("DYLD_LIBRARY_PATH", load_z3().unwrap().to_str().unwrap());
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