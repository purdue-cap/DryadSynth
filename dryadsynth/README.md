
# DryadSynth General Solver

Ported from java code, using [GraalVM](https://www.graalvm.org/) to reduce start-up time. We retrieved GraalVM using `scripts/graalvm.rs` script.

To build the solver, simply run `cargo build --release`. With JDK (>= 17) installed, the source can also be built using `make`, and run using:

```sh
java -cp classes:lib/antlr.jar:lib/com.microsoft.z3.jar:lib/jopt-simple.jar Run "$@"
```

Where `$@` represent the input arguments.

# Publications

- [Reconciling enumerative and deductive program synthesis.](https://dl.acm.org/doi/abs/10.1145/3385412.3386027) Kangjing Huang, Xiaokang Qiu, Peiyuan Shen, and Yanjun Wang. In Proceedings of the 41st ACM SIGPLAN Conference on Programming Language Design and Implementation, pp. 1159-1174. 2020. [author version](https://github.com/purdue-cap/DryadSynth/blob/master/docs/pldi2020.pdf).