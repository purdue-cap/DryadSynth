# Problems remain to be solved

- Benchmarks that use non-primitive types as input or output of the functions,
such as `(BitVec )`, `(Enum )`, `(Array )`

# Custom patched Z3

During the development of this solver, multiple bugs in Z3 related to our use
cases emerged. We fixed these bugs and submitted the fixing patches to the Z3
project, yet it takes time for the patches to be accepted and pushed down to
master, then get released in a verison. Thus using our [patched
version](https://github.com/chaserhkj/z3/tree/api-fix-4.5.0) of Z3 is
recommended.
