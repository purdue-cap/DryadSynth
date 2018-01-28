# To-dos

- Currently the SinInv code is not a strict implementation of the SSI theory, it
  only could handle DNF constraint formats with equation relations in each clause.
    - It would give incorrect anwsers on benchmarks without equation relations
- Refine single-invocation method codes to fit the design pattern of dispatcher.
    - Also need to correct poor naming and poor codes in SININV
- Bug in SinInv method when handling contraints with only one disjunction.
    - As it is in new "Common" case benchmarks in CLIA track
- Performance issue in SinInv codes causing result size and processing time to
  exponentially explode.
    - duplicate terms in constraint could and should be moved out as a
    prerequisite of the whole SinInv generation
    - Currently they're only treated together as a whole term
    - Could help reduce the base of the exponential explode, but could not elim it.
- Performance issue in SinInv codes related to implementation
    - We're reinventing the wheel a lot in the the processing, many work like DNF
    conversion could be handled by Z3 with its more sufficient algorithms.
    - Z3 is doing bad on serializing the AST of expression to a String
    representation, suffering from the single-thread method it adapts. We may
    do this part ourselves in parallel to make it faster
- SinInv codes may still have problem on arithmetic ITE terms
    - like `(>= (ite (>= x y) x y) 0)`
    - Need "Pop up" algorithm when traversing the AST tree

# Fixed problems

- Bug in SinInv method when handling fg_mpg_example5.sl
    - Fixed, this is the result of SinInv code wrongly handling ITE terms.
    - ITE terms with arithmetic terms could still cause problem, see to-dos
- Single invocation methods are generating insanly large results, need
  optimization
    - On result size. Large result size may bring in problem for post-processing
    - On reformatting. Currently we're doing reformatting on smaller sizes of
      solutions only, and doing regex replace on larger sizes
        - Maybe it's a better idea to use regex globally
    - Fixed, this is the result of SinInv code wrongly uses 0 to represent
    default case, so when concating results, it's creating unneccesary codes
    - Still some size-related and performance-related problems, see to-dos

# Custom patched Z3

During the development of this solver, multiple bugs in Z3 related to our use
cases emerged. We fixed these bugs and submitted the fixing patches to the Z3
project, yet it takes time for the patches to be accepted and pushed down to
master, then get released in a verison. Thus using our [patched
version](https://github.com/chaserhkj/z3/tree/api-fix-4.5.0) of Z3 is
recommended.
