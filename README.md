# DryadSynth

Dryad Synthesizer for SyGuS competition.

For a complete description on methods, please see `docs/pldi2020.pdf` or `docs/pldi2020.ppsx`.

# Publications

- [Reconciling enumerative and deductive program synthesis.](https://dl.acm.org/doi/abs/10.1145/3385412.3386027) Kangjing Huang, Xiaokang Qiu, Peiyuan Shen, and Yanjun Wang. In Proceedings of the 41st ACM SIGPLAN Conference on Programming Language Design and Implementation, pp. 1159-1174. 2020.
- Or you could access the [author version](https://github.com/purdue-cap/DryadSynth/blob/master/docs/pldi2020.pdf).

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
3. Install rust from [rustup](https://www.rust-lang.org/tools/install), and set the current directory to use nightly rust by `rustup override set nightly`.
4. Complile the program
    - `cd` into the source code directory of the program and run `make` would do the work.

## Running the program

1. Ensure `libz3java.so` is in `java.library.path`:
    - On Linux platforms, you can simply set environment variable `LD_LIBRARY_PATH` to include the path, it would be automatically added.
2. Simply run `$ ./exec.sh <path/to/sygus/file>`
3. Looking into `$ ./exec.sh --help` for further information.
4. Make sure `z3` and `bitwuzla` (used only for bit-vector) commands are available in the `PATH`.
5. If you use ChatGPT for `bit-vector`, please make sure to set the `OPENAI_API_KEY` environment variable.

Note: Some problems run in multithread by default. If you don't specify the number of threads, the CPU core count on your system would be used. **This may cause unexpected behavior when the size of the CPU pool is large**.

