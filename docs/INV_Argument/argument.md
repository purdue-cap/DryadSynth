---
papersize: letter
geometry: margin=1in
...
\newcommand{\pref}{\mathrm{F_{PRE}}}
\newcommand{\postf}{\mathrm{F_{POST}}}
\newcommand{\transf}{\mathrm{F_{TRAN}}}
\newcommand{\invf}{\mathrm{F_{INV}}}

# Problem Description

Given a set of variables \(x_1, \dots, x_n\) and their primed versions \(x_1', \dots, x_n'\), it is possbile to describe a loop which uses these variables by 3 boolean functions:
\[\pref(x_1, \dots, x_n)\]
\[\transf(x_1,\dots,x_n,x_1',\dots,x_n')\]
\[\postf(x_1,\dots,x_n)\]

With:

\(\pref(x_1, \dots, x_n) = 1\)
:   denoting that the set of variables are a valid set of input variables to the loop;

\(\transf(x_1, \dots, x_n, x_1', \dots, x_n') = 1\)
:   denoting that the unprimed set of input variables become its corresponding primed versions after one interation of loop

\(\postf(x_1, \dots, x_n) = 1\)
:   denoting that the set of input variables are a valid set of ending state of the loop

The problem is to find the invariant of the loop, a boolean function
\[\invf(x_1,\dots,x_n)\]
such that
\[\begin{split}\pref(x_1,\dots,x_n) & \Rightarrow \invf(x_1, \dots, x_n) \\
\invf(x_1,\dots,x_n) \land \transf(x_1,\dots,x_n,x_1',\dots,x_n') & \Rightarrow \invf(x_1',\dots,x_n') \\
\invf(x_1,\dots,x_n) &\Rightarrow\postf(x_1,\dots,x_n)\end{split}\]

# Assumption and argument

Assume that the form of \(\pref\) is
\[\pref \Leftrightarrow C_1^{(0)}\land C_2^{(0)}\land\dots\]

And the form of \(\transf\) is:
\[\begin{split}
\transf \Leftrightarrow ( &C_1^{(1)} \land C_2^{(1)} \land \dots
\\ &T^{(1)}_1 \land T^{(1)}_2 \land \dots \land T^{(1)}_n) \lor \\
( &C_1^{(2)} \land C_2^{(2)} \land \dots
\\ &T^{(2)}_1 \land T^{(2)}_2 \land \dots \land T^{(2)}_n) \lor \\
& \dots
\end{split}\]
with elements, or atoms \(C_j^{(i)}\) having the form
\[C_j^{(i)} \Leftrightarrow  \begin{cases}x_k \ge c   \\ x_k \le c \\ x_l - x_k \ge c \end{cases}\]

And atoms \(T_j^{(i)}\) have the form
\[T_j^{(i)} \Leftrightarrow x_j' = x_j + c_j \]

We would like to prove the argument that given this assumption as a constraint, if there exists an invariant, there must exist another invariant that have the form
\[\invf \Leftrightarrow C_1 \land C_2 \dots \]
with \(C_i\) being in the same form as \(C_j^{(i)}\)

# Terminology

We shall use certain terminology for the rest of our discussion. We will define them here.

Region
:   A region is a set of constraints on variables.\[f_i(x_1, x_2, \dots, x_n) \ge 0\] It represents a geometric region in \(N^n\) space. For CLIA problems, all region involved in the problem shall have linear boundaries. We could use a boolean function of these variables to denote a region, as a boolean function is effectively a set of constraints on it parameters. If the set of constraints of a region are all of linear forms, in other words, if \(f_i\)'s are all linear functions, then we denote such a region as a **linear region**.

Subregion
:   Given two Regions
\[R_1({x_i}) \ge 0, R_2({x_i}) \ge 0\]
if
\[R_1({x_i}) \ge 0 \Rightarrow R_2({x_i}) \ge 0\]
We denote that \(R_1\) is a **subregion** of \(R_2\)

Regular Region
:   A regular region is a region with the form: \[ C_1 \land C_2 \land \dots\] And with atomic constraints in the form being: \[ C_i \Leftrightarrow
\begin{cases}x_k \ge c   \\ x_k \le c \\ x_l - x_k \ge c \end{cases}\]

Transformation
:   A transformation is a set of constraints set on \((x_1, x_2, \dots, x_n)\) (input variables) and \((x_1', x_2', \dots, x_n') \) (output variables) So that in one interation of the loop, given values of all variables in last interation as input variables, constraints on the new values of all variables could be determined as output variables

Linear Deterministic Transformation
:   If a transformation \(T\) satisfies the following condition:
\[ T \Leftrightarrow (x_1' = x_1 + c_1) \land (x_2' = x_2 + c_2) \land
\dots \land (x_n' = x_n + c_n) \]
with \(n\) input variables and \(n\) output variables on \(N^n\) space, then we call it a **Linear Deterministic Transformation**. We also denote the n-dimensional vector
\[ (c_1, c_2, \dots c_n) \]
as the **Transform Vector** of the transformation.

Envelope
:   Given an input region and a transformation. A set of constraints on \((x_1', x_2', \dots, x_n')\) could be obtained from the constraints denoted by the region. These constraints actually form a region for the  output variables. Denote this new region, unioned with the input region, as the envelope of the original region under one iteration of the transformation. And denote the transformation from the input region to its envelope as one **expanding** of the original region. An input region is always a subregion of its envelope.

Domains and Subdomains of a transformation
:   If a transformation has certain conditions on its input variables that is a region:
\[ T \Leftrightarrow  R(\{x_k\})\land T'(\{x_k, x_k'\})\]
denote that region as the **Domain** of the transformation. If a transformation is in the form of a disjunction, with each part being a domain transformation without any overlaying parts:
\[\begin{split} T  \Leftrightarrow  &(R_1(\{x_k\})\land T_1'(\{x_k, x_k'\}))\\
&\lor(R_2(\{x_k\})\land T_2'(\{x_k, x_k'\}))\\
&\dots
\end{split} \]
\[R_i \land R_j = \emptyset, \text{for any } i \neq j\]
Then denote this transformation as a **multi-domain** transformation, denote each of these regions as **subdomains** of the transformation and denote the transformations in subdomains as **subtransformations**.

Regular Multi-domain Transformation
:   If the domains of a multi-domain transformation are regular regions, we denote that transformation as a regular multi-domain transformation.

Linear Deterministic Regular Multi-domain Transformation
:   If for each domains of a regular multi-domain transformation, the subtransformations are linear Deterministic transformations, we denote the whole transformation as a linear deterministic regular multi-domain transformation.

Extended Transformation
:   If a multi-domain transformation's subdomains do not cover the whole \(N^n\) space, then for the uncovered part of the space, or for the **undefined domain** of the transformation, constraints on output variables does not exist, so principally they could be any value. An **extended transformation** could be introduced, for which in the undfined domain of the original multi-domain transformation, \(x_k' = x_k\) is enforced for all variable pairs. We still call that domain as undefined domain for convenience. Observe that this enforces the subtransformation to be linearly bounded for the undefined domains.

Contain
:   Given a region and a transformation. If the envelope of the region under one iteration of that transformation is still a subregion of the region, we call that this transformation is **contained** with in such region. In other words, if a transformation is contained by certain region, expanding that region would not result in a region that's larger than the original region.

# Equivalent expression for the problem

Using the terminology defined above, certain observations could be obtained for the problem:

- \(\pref, \postf, \invf\) denotes three regions in \(N^n\) space
- \(\pref\) should be a subregion of \(\invf\)
- \(\invf\) should be a subregion of \(\postf\)
- Transform denoted by \(\transf\) should be contained in \(\invf\)

These observations could serve as a set of equivalent expressions to the original invariant synthesis problem.

# Lemmas

Certain lemmas could be obtained to help the proof of the argument as well.

Lemma 1
:   The envelope of a regular region under a linear deterministic regular multi-domain transformation is still a regular region.

# Linear Deterministic Regular Multi-domain Transformation Conditions

Theorem 1
:   For an invariant synthesis problem with regular \(\pref\), regular \(\postf\), and a linear deterministic regular multi-domain transformation as its \(\invf\), if :

    - there exisits a linear invariant \(\invf'\) for this problem, and,
    - the region of \(\invf'\) has at least one complete subdomain of \(\transf\) as its subregion

    Then:

    - Taking the combined region of all subdomains of \(\transf\) that are subregions of \(\invf'\), denoted as \(R_0\)
    - The envelope of \(R_0\), denoted as \(\invf\), is a regular invariant of the synthesis problem

Proof:

Use proof by contradiction.

- Assume that \(\invf\) is not an invariant
- Denote the subdomains of \(\transf\) that are not subregions of \(\invf'\) as **boundary domains** of \(\transf\) for \(\invf'\)
- For input points from \(R_0\), after one transformation, the output points have 2 possibilities:
    - They're still in \(R_0\)
        - These points are contained by \(R_0\), thus certainly contained by \(\invf\)
    - They're in one of the boundary domains
- Thus, given that \(\invf\) is not an invariant, there must exist some point in \(\invf\) and also in the boundary domains of \(\transf\) for \(\invf'\), that transfers out of \(\invf\) after one transformation
- ***KEY***: prove that this leads to points near the boundaries of \(\invf'\) would transfer out of \(\invf'\) as well, which would cause a contradiction
     - The directions of the boundaries may be important in providing this proof
     - Easy case: \(\invf'\) boundaries are regular.
