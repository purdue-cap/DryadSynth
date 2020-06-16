# DryadSynth

Dryad Synthesizer for SyGuS competition.

For a complete description on methods, please see `doc/DryadSynth_published.pdf`.

# Publications

- [Reconciling enumerative and deductive program synthesis.](https://dl.acm.org/doi/abs/10.1145/3385412.3386027) Kangjing Huang, Xiaokang Qiu, Peiyuan Shen, and Yanjun Wang. In Proceedings of the 41st ACM SIGPLAN Conference on Programming Language Design and Implementation, pp. 1159-1174. 2020.

# Installation and Running

Currently the synthesizer is not packaged, but you could compile and run it from the project directory.

This program is developed, tested and ran on Linux platform, it is recommended to run it in the same environment. However, testings on Mac OS have also passed.

It is theoretically possible to run the program on Windows platform given a working JVM and a properly installed Z3, however this is not encouraged.

## Dependencies

- JDK with version >= 9
- Z3 with java bindings enabled

## Steps to compile

1. Install JDK on your system, you should refer to your system/distribution manuals to figure out how to complete this. Make sure that JDK version is no lower than 9.
2. Configure and install Z3 with java bindings enabled
    1. First download Z3, git version is recommended, as some of the bug fixes that are required for this program to run may not have been released to a released version yet.
        - Here is the [link](https://github.com/Z3Prover/z3/) to the project site of Z3.
        - For using the git version, run `git clone https://github.com/Z3Prover/z3/`
    2. Configure the z3 source codes, preparing for build
        - Note that if multiple versions of java have been installed on the system, before configuration, you'll need to set environment variables `JAVA_HOME`, `JDK_HOME` to the home path of the version that you would like to use, also you need to set `JNI_HOME` to `${JDK_HOME}/include`
        - Change the argument `--prefix` according to your needs
    ```bash
    $ cd z3
    $ ./configure --prefix=/usr/local --java
    ```

    3. Build and install Z3
        - If the install prefix is not system-wide, omit the `sudo` for `make install`.
    ```bash
    $ cd build; make
    $ sudo make install
    ```
3. Complile the program
    - `cd` into the source code directory of the program and run `make` would do the work.

## Running the program

The compiled class files would be put in `classes` directory, and related library files are in `lib` diretory, entrance classes are `Run` and `Synth`, so for runing the program:

- java classpath need to include these pathes:
    - `classes/`
    - `lib/antlr.jar`
    - `lib/com.microsoft.z3.jar`
    - `lib/jopt-simple.jar`
    - See java man pages on how.
- java.library.path needs to include the path that `libz3java.so` lies (eg. `/usr/local/lib` in the metioned configuration)
    - On Linux platforms, you can simply set environment variable `LD_LIBRARY_PATH` to include the path, it would be automatically added.
    - Or you can fallback to `-D` arguments to java, see java man page for details
- Entrance class `Synth` is for single threaded algorithm, with arguments:
    
    ```
    java <java arguments> Synth <path to benchmark file>
    ```
    
    Output would be written to stdout

- Entrance class `Run` is for multithread algorithm, with arguments:
  
    ``` 
    java <java arguments> Run <path to benchmark file> [Numbers of threads to use]
    ```
  
    Or
  
    ```
    java <java arguments> Run <path to benchmark file> [Timeout for last finite boundary search] [Time out for infinite boundary search]
    ```
  
    Or
  
    ```
    java <java arguments> Run <path to benchmark file> [Numbers of threads to use] [Timeout for last finite boundary search] [Time out for infinite boundary search]
    ```

    If omitted numbers of threads, the CPU core count on your system would be used. **Note that this may cause unexpected behavior when the size of the CPU pool is large**. If omitted timeouts, default timeouts would be used.

Finally, there's a script `exec.sh` in the directory for easier execution, simply setup proper `LD_LIBRARY_PATH` and run it like
```./exec.sh <arguments that are after Run>```
and it would execute `Run` class properly.
