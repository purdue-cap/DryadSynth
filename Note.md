# To-dos

- Refine single-invocation method codes to fit the design pattern of dispatcher.
    - Also need to correct poor naming and poor codes in SININV
- Single invocation methods are generating insanly large results, need
  optimization
    - On result size. Large result size may bring in problem for post-processing
    - On reformatting. Currently we're doing reformatting on smaller sizes of
      solutions only, and doing regex replace on larger sizes
        - Maybe it's a better idea to use regex globally
- Bug in SinInv method when handling contraints with only one disjunction.
    - As it is in new "Common" case benchmarks in CLIA track
- Bug in SinInv method when handling fg_mpg_example5.sl

# Custom patched Z3

During the development of this solver, multiple bugs in Z3 related to our use
cases emerged. We fixed these bugs and submitted the fixing patches to the Z3
project, yet it takes time for the patches to be accepted and pushed down to
master, then get released in a verison. Thus using our [patched
version](https://github.com/chaserhkj/z3/tree/api-fix-4.5.0) of Z3 is
recommended.
