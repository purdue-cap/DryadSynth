# DryadSynth

Dryad Synthesizer for SyGuS competition.

For a complete description on methods, please see `docs/pldi2020.pdf` for CLIA and `docs/popl2024.ppsx` for bitvector.

# Publications

- [Reconciling enumerative and deductive program synthesis.](https://dl.acm.org/doi/abs/10.1145/3385412.3386027) Kangjing Huang, Xiaokang Qiu, Peiyuan Shen, and Yanjun Wang. In Proceedings of the 41st ACM SIGPLAN Conference on Programming Language Design and Implementation, pp. 1159-1174. 2020. [author version](https://github.com/purdue-cap/DryadSynth/blob/master/docs/pldi2020.pdf).
- [Enhanced Enumeration of Techniques for Syntax-Guided Synthesis of Bit-Vector Manipulations](https://dl.acm.org/doi/10.1145/3632913) Yuantian Ding, Xiaokang Qiu, In Proc. 51st ACM SIGPLAN Symposium on Principles of Programming Languages (POPL '24), 2024. [author version](https://github.com/purdue-cap/DryadSynth/blob/master/docs/popl2024.pdf).
- [A Concurrent Approach to String Transformation Synthesis.](https://yuantianding.github.io/uploads/PLDI_2025.pdf) Yuantian Ding, Xiaokang Qiu, Conditional Accepted by PLDI 2025. [author version](https://yuantianding.github.io/uploads/PLDI_2025.pdf).

# Installation and Running

## Install the Synthesizer

To install DryadSynth, you need:

- Basic Linux Utilities: `gcc`, `g++`, `glibc-dev`, `zlib-dev`, `zlib-static`, `libstdc++-static` installed (Used for [GraalVM](https://www.graalvm.org/latest/getting-started/linux/) to compile java scripts).
- Rust toolchain: `rustup`, `cargo`, `rustc`, `rust-std` installed.
- For general solver, make sure `z3` (recommended version `4.14.1`) with java bindings `libz3java.so` installed in `LD_LIBRARY_PATH` or `DYLB_LIBRARY_PATH`.
- For bit-vector solver, make sure `bitwuzla` command installed in `PATH`.

And then simply install DryadSynth by `cargo install dryadsynth dryadsynth-bv synthphonia-rs`. 

## Supported Platform

| Platform       | Test  | Notes                                                                                                                                        |
| :------:       | :--:  | :----:                                                                                                                                       |
| linux-x64-glibc| Passed| Tested on Fedora 32, March 2025                                                                                                              |
| macos-x64      | Passed| Homebrew `z3` package `libz3java.dylib` missing, install from [Github](https://github.com/Z3Prover/z3/releases/tag/z3-4.14.1) |

## Run the Synthesizer

Once DryadSynth in installed in your system. The following command will be available in your environment:

* `dryadsynth` the main entry of the general solver, only general solving strategy options for SyGuS-IF (`.sl`) format supported, will call the sub-solvers in settings restricted in `sygus-if2` format.
* `dryadsynth-bv` the bit-vector sub-solver, used to specify bit-vector specific options in our [POPL'24 paper](https://github.com/purdue-cap/DryadSynth/blob/master/docs/popl2024.pdf).
* `synthphonia` the string sub-solver, used to specify the string-related grammar and options, offering richer grammar in the default settings.

Note: Some problems run in multithread by default. If you don't specify the number of threads, the CPU core count on your system would be used. **This may cause unexpected behavior when the size of the CPU pool is large**.

