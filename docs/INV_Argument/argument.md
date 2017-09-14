---
papersize: letter
geometry: margin=1in
...
\newcommand{\pref}{\mathrm{Pref}}
\newcommand{\postf}{\mathrm{Postf}}
\newcommand{\transf}{\mathrm{Transf}}
\newcommand{\invf}{\mathrm{Invf}}

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

And atoms \(T_j^{(i)}\) has the form such that
\[\begin{split}T_1^{(i)} &\land T_2^{(i)} \land \dots \Rightarrow \\
 &(x_1 + c_1' \le x_1' \le x_1 + c_1) \land (x_2 + c_2' \le x_2' \le x_2 + c_2) \land\dots\end{split} \]

We would like to prove the argument that given this assumption as a constraint, if there exists an invariant, there must exist another invariant that have the form
\[\invf \Leftrightarrow C_1 \land C_2 \dots \]
with \(C_i\) being in the same form as \(C_j^{(i)}\)

# Terminology

We shall use certain terminology for the rest of our discussion. We will define them here.

Region
:   A region is a set of constraints on variables.\[f_i(x_1, x_2, \dots, x_n) \ge 0\] It represents a geometric region in \(N^n\) space. For CLIA problems, all region involved in the problem shall have linear boundaries.

Regular Region
:   A regular region is a region with the form: \[ C_1 \land C_2 \land \dots\] And with atomic constraints in the form being: \[ C_i \Leftrightarrow
\begin{cases}x_k \ge c   \\ x_k \le c \\ x_l - x_k \ge c \end{cases}\]

Transformation
:   A transformation is a set of constraints set on \((x_1, x_2, \dots, x_n)\) and \((x_1', x_2', \dots, x_n') \) So that in one interation of the loop, given values of all variables in last interation, constraints on the new values of all variables could be determined

Linearly Bounded Transformation
:   If a transformation \(T\) satisfies the following condition:
\[\begin{split}&T \Rightarrow x_k' \le x_k + c \\
\lor &T \Rightarrow x_k +c \le x_k'\\
\lor &T \Rightarrow x_k +c_1 \le x_k' \le x_k + c_2
\end{split} \]
Then we call it a linearly bounded transformation

Envelope
:   Given a region and a transformation. A set of constraints on \((x_1', x_2', \dots, x_n')\) could be obtained from the constraints denoted by the region. These constraints actually form a region for the primed version of the variables. Denote this new region as the envelope of the original region under one iteration of the transformation. And denote the transformation from the region to its envelope as one **expanding** of the original region.

Ranges and Subranges of a transformation
:   If a transformation has certain conditions on its input variables (the unprimed version of the varaibles) that is a region:
\[ T \Leftrightarrow  R(\{x_k\})\land T'(\{x_k, x_k'\})\]
denote that region as the **Range** of the transformation. If a transformation is in the form of a disjunction, with each part being a ranged transformation without any overlaying parts:
\[\begin{split} T  \Leftrightarrow  &(R_1(\{x_k\})\land T_1'(\{x_k, x_k'\}))\\
&\lor(R_2(\{x_k\})\land T_2'(\{x_k, x_k'\}))\\
&\dots
\end{split} \]
\[R_i \land R_j = \emptyset, \text{for any } i \neq j\]
Then denote this transformation as a **multi-ranged** transformation, denote each of these regions as **subranges** of the transformation and denote the transformations in subranges as **subtransformations**.

Extended Transformation
:   If a multi-ranged transformation's subranges do not cover the whole \(N^n\) space, then for the uncovered part of the space, or for the **undefined range** of the transformation, constraints on primed version of variables does not exist, so principally they could be any value. An **extended transformation** could be introduced, for which in the undfined range of the original multi-ranged transformation, \(x_k' = x_k\) is enforced for all variable pairs. We still call that range as undefined range for convenience. Observe that this enforces the subtransformation to be linearly bounded for the undefined ranges.

Contain
:   Given a region and a transformation. If the envelope of the region under one iteration of that transformation is still a subset of the region, we call that this transformation is **contained** with in such region. In other words, if a transformation is contained by certain region, expanding that region would not result in a region that's larger than the original region.

# Observation of problem and assumptions

Using the terminology defined above, certain observations could be obtained for the problem and the assumptions
