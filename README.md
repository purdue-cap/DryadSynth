# DryadSynth

Dryad Synthesizer for SyGuS competition.

For a complete description on methods, please see `docs/pldi2020.pdf` for CLIA and `docs/popl2024.ppsx` for bitvector.

# Publications

- [Reconciling enumerative and deductive program synthesis.](https://dl.acm.org/doi/abs/10.1145/3385412.3386027) Kangjing Huang, Xiaokang Qiu, Peiyuan Shen, and Yanjun Wang. In Proceedings of the 41st ACM SIGPLAN Conference on Programming Language Design and Implementation, pp. 1159-1174. 2020. [author version](https://github.com/purdue-cap/DryadSynth/blob/master/docs/pldi2020.pdf).
- [Enhanced Enumeration of Techniques for Syntax-Guided Synthesis of Bit-Vector Manipulations](https://dl.acm.org/doi/10.1145/3632913) Yuantian Ding, Xiaokang Qiu, In Proc. 51st ACM SIGPLAN Symposium on Principles of Programming Languages (POPL '24), 2024. [author version](https://github.com/purdue-cap/DryadSynth/blob/master/docs/popl2024.pdf).

# Installation and Running

Currently the synthesizer is not packaged, but you could compile and run it from the project directory.

This program is developed, tested and ran on Linux platform, it is recommended to run it in the same environment. However, testings on Mac OS have also passed.

It is theoretically possible to run the program on Windows platform given a working JVM and a properly installed Z3, however this is not encouraged.

## Dependencies

- JDK with version >= 17
- Z3 with java bindings enabled
- Nightly version rust toolchain

## Steps to compile

1. Install JDK on your system, you should refer to your system/distribution manuals to figure out how to complete this. Make sure that JDK version is no lower than 9.
2. Configure and install Z3 with java bindings enabled
    1. First download Z3, older versions like 4.8.9 are recommended, as the newest version may not be supported by DryadSynth.
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
    4. Install Bitwuzla (If using bitvector PBE search).
       1. Download [bitwuzla](https://github.com/bitwuzla/bitwuzla/releases).
       2. Install Bitwuzla and make sure `bitwuzla` command is available in `PATH`.
3. Install rust from [rustup](https://www.rust-lang.org/tools/install), and set the current directory to use nightly rust by `rustup override set nightly`.
4. Complile the program
    - `cd` into the source code directory of the program and run `make` would do the work.

## Running the program

1. Ensure `libz3java.so` is in `java.library.path`:
    - On Linux platforms, you can simply set environment variable `LD_LIBRARY_PATH` to include the path, it would be automatically added.
2. If you use ChatGPT for `bit-vector`, please make sure to set the `OPENAI_API_KEY` environment variable.
3. Simply run `$ ./exec.sh <path/to/sygus/file>`
4. Looking into `$ ./exec.sh --help` for further information.


Note: Some problems run in multithread by default. If you don't specify the number of threads, the CPU core count on your system would be used. **This may cause unexpected behavior when the size of the CPU pool is large**.

