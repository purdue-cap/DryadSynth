
# DryadSynth-BV

The current DryadSynth is already embedded all the techniques mentioned in the paper [Enhanced Enumeration of Techniques for Syntax-Guided Synthesis of Bit-Vector Manipulations](https://dl.acm.org/doi/10.1145/3632913). To further configure the arguments mentioned in the paper, A TOML file can be passed into DryadSynth using the following command:

```bash
DryadSynth [-B <config-file>] <sygus-if-file>
```

In `src/meet-middle/config`, there are a lot of commonly used configuration files. 
Here is the default configuration file for PBE problems with explanation of each parameter:

```toml
random_example = 10 # How many additional random examples are generated from reference implementation 
limited_ite = false # Limit ITE condition search size to 10000 
ite_tree_limit = 30 # Limit the size of decition tree
chatgpt = true # Disable/Enable ChatGPT
smt_solver = "bitwuzla" # Using command bitwuzla as SMT-Solver. Require `bitwuzla` command installed

[expr_search]
sample = 64 # The number of sampling
dag_size = true # Enable Graph-Based Enumeration
filter = {
    deductive_combine = true, # Enable/Disable deduction for AND and OR
    deductive_reverse = true  # Enable/Disable deduction for XOR and ADD
} 
```

