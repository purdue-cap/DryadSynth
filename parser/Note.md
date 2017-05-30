# Problems remain to be solved

- Benchmarks that use non-primitive types as input or output of the functions,
such as `(BitVec )`, `(Enum )`, `(Array )`

# Custom patched Z3 java API library

The codes in Z3 java API have a bug in `Expr.update()`, which causes the
method call to return super class rather than subclass, resulting in we can't
cast the type to `BoolExpr`. `DefinedFunc.rewrite()` uses this method, so this
must be worked around otherwise we could not put the rewritten expression into
SMT solver.

> My pull request to fix this bug has already be
> [merged](https://github.com/Z3Prover/z3/commit/8092dd5aa352f253536064156ad1a35f78409fa1)
> by the Z3 team.
>
> -- Kangjing

It would take time for the bugfix to be appeared in a release. Thus using the
master git version of Z3 repository is advised to work around this.

To make it simple, a patched version of Z3 java API is included in
`lib/com.microsoft.z3.jar`, which is Z3 v4.5.0 java API patched with the bugfix.
It should be working directly with Z3 v4.5.0 tools and libraries.

