---
papersize: letter
geometry: margin=1in
...
\newcommand{\pref}{\mathrm{F_{PRE}}}
\newcommand{\postf}{\mathrm{F_{POST}}}
\newcommand{\transf}{\mathrm{F_{TRAN}}}
\newcommand{\invf}{\mathrm{F_{INV}}}
\newcommand{\env}{\mathrm{Env}}

# Problem Description

Given a set of integer variables \(x_1, \dots, x_n\) and their primed versions \(x_1', \dots, x_n'\), it is possbile to describe a loop which uses these variables by 3 boolean functions:
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

# General Terminology

Set
:   A set, referred to in our context, is a set of points of \(Z^n\) space. It could be expressed as a set of expressions on the \(n\) variables.\[f_i(x_1, x_2, \dots, x_n) \ge 0\] It represents a geometric region in \(Z^n\) space. For CLIA problems, all set involved in the problem shall have linear boundaries. If the set of constraints of a set  are all of linear forms, in other words, if \(f_i\)'s are all linear functions, then we denote such a set as a **linearly bounded set**.

Finite Set
:   If a set contains finite numbers of points from \(Z^n\) space, then we call it a finite set. In other words, if
\[\exists P\in Z^n, P \in R \]
has finite solutions, then \(R\) is a finite set.

Transformation
:   A transformation is a set of constraints set on \((x_1, x_2, \dots, x_n)\) (input variables) and \((x_1', x_2', \dots, x_n') \) (output variables) So that in one interation of the loop, given values of all variables in last interation as input variables, constraints on the new values of all variables could be determined as output variables

Deterministic Transformation
:   A deterministic transformation is a transformation which could determine a set of values of the output variables with the values of input variables given. In other words, for a deterministic transformation \(T\),
\[\exists \{x_i'\}, T(\{x_i\}, \{x_i'\}) \] has and only has one set of \(\{x_i'\}\) as its solution. When \(T\) is a deterministic transformation, we denote that
\[T(\{x_i\}) = \{x_i'\}\]
or
\[T(P) = P' \]
Given that
\[P = \{x_i\}, P' = \{x_i'\}\]

Linear Deterministic Transformation
:   If a transformation \(T\) satisfies the following condition:
\[ T \Leftrightarrow (x_1' = x_1 + c_1) \land (x_2' = x_2 + c_2) \land
\dots \land (x_n' = x_n + c_n) \]
with \(n\) input variables and \(n\) output variables on \(Z^n\) space, then we call it a **Linear Deterministic Transformation**. We also denote the n-dimensional vector
\[ \vec{c} =  (c_1, c_2, \dots c_n) \]
as the **Transform Vector** of the transformation.

    Using the notation of deterministic transformation above, this transformation could be expressed as
    \[T(\{x_1, x_2, \dots, x_n\}) = \{x_1 + c_1, x_2 + c_2, \dots, x_n + c_n\} \]

Envelope
:   Given an input set \(R\) and a deterministic transformation \(T\). Define the envelope of \(R\) under \(T\), \(\env(R, T)\) as:
\[\env(R,T) = R \lor \{P'| P' = T(P), P \in R\} \]
We denote the transformation from a set to its envelope under certain deterministic transformation as one expansion of that set under such transformation, and we could keep doing that expansion on the expanded envelope.

Regions of a transformation
:   If a transformation has certain conditions on its input variables that is a set:
\[ T \Leftrightarrow  R(\{x_k\})\land T'(\{x_k, x_k'\})\]
denote that set as the **Region** of the transformation. If a transformation is in the form of a disjunction, with each part being a region transformation without any overlaying parts:
\[\begin{split} T  \Leftrightarrow  &(R_1(\{x_k\})\land T_1'(\{x_k, x_k'\}))\\
&\lor(R_2(\{x_k\})\land T_2'(\{x_k, x_k'\}))\\
&\dots
\end{split} \]
\[R_i \land R_j = \emptyset, \text{for any } i \neq j\]
Then denote this transformation as a **multi-region** transformation, still denote each of these regions as **regions** of the transformation and denote the transformations in these regions as **subtransformations**.

Extended Transformation
:   If a multi-region transformation's regions do not cover the whole \(Z^n\) space, then for the uncovered part of the space, or for the **undefined regions** of the transformation, constraints on output variables does not exist, so principally they could be any value. An **extended transformation** could be introduced, for which in the undfined regions of the original multi-region transformation, \(x_k' = x_k\) is enforced for all variable pairs. We still call that domain as undefined regions for convenience. Observe that this enforces the subtransformation to be linear deterministic for the undefined regions.

Contain
:   Given a set \(R\) and a transformation \(T\). If
\[\forall P \in R, T(P) \in R \]
Then we note that \(T\) is **contained** by \(R\), this could also be expressed as:
\[T \land R \Rightarrow R(\{x_i'\} \to \{x_i\}) \]

# Equivalent expression for the problem

Using the terminology defined above, certain observations could be obtained for the problem:

- \(\pref, \postf, \invf\) denotes three sets in \(Z^n\) space
- \(\pref\) should be a subset of \(\invf\)
- \(\invf\) should be a subset of \(\postf\)
- Transform denoted by \(\transf\) should be contained in \(\invf\)

These observations could serve as a set of equivalent expressions to the original invariant synthesis problem.

# Proof on finite bound problems

## Assumption and argument

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

We would like to prove the argument that given this assumption as a constraint, if there exists an invariant that is a finite set,
there must exist another invariant that have the form
\[\invf \Leftrightarrow C_1 \land C_2 \dots \]
with \(C_i\) being in the same form as \(C_j^{(i)}\),
and such invariant could be obtained by expanding \(\pref\) for finite times.

## Terminology

Octagon
:   An Octagon is a set with the form: \[ C_1 \land C_2 \land \dots\] And with atomic constraints in the form being: \[ C_i \Leftrightarrow
\begin{cases}x_k \ge c   \\ x_k \le c \\ x_l - x_k \ge c \end{cases}\]

Combination of Octagons
:   A combination of octagons is a set with the form: \[R_1 \lor R_2 \lor \dots \] And with each of these \(R_i\) being an octagon.

Octagonal Multi-region Transformation
:   If the regions of a multi-region transformation are octagons, we denote that transformation as a octagonal multi-region transformation.

Linear Deterministic Octagonal Multi-region Transformation
:   If for each regions of a octagonal multi-region transformation, the subtransformations are linear Deterministic transformations, we denote the whole transformation as a linear deterministic octagonal multi-region transformation.
## Lemmas

Certain lemmas could be obtained to help the proof of the argument as well.

Lemma 1
:   A set is a combination of octagons iff. the equations of the boundaries of this set is of form \(f_i\), with \(f_i\) being:
\[ f_i \Leftrightarrow \begin{cases} x_i = c_i \\ x_i - x_j = c_{ij}\end{cases}\]

Proof.

If set \(R\) is a combination of octagons, then the atomic expression of its logical form is \(C_j^{(i)}\), so the boundary equation forms of \(R\) shall be the equation version of the inequalities in \(C_j^{(i)}\), which is of form \(f_i\)

If set \(R\) has boundary equations in form of \(f_i\), the logical form of \(R\) shall consist of inequality version forms of these equations as its atomic expression, which is \(C_j^{(i)}\), and we could always convert this form into a disjunction normal form (DNF), which is the same notation as it is in the definition of combinations of octagons, thus \(R\) is a combination of octagons.

Lemma 2
:   Transformation \(T\) is contained by set \(R\) iff. the envelope of \(R\) under \(T\) should be \(R\) iff. \(\{P'| T(P) = P', P \in R\} \subseteq R\)

Proof.

\[\forall P\in R, T(P) \in R \Leftrightarrow  \{P'| T(P) = P', P \in R\}\subseteq R \Leftrightarrow  R \lor \{P'| T(P) = P', P \in R\} = R\]

Lemma 3
:   Conjunctions and disjunctions of combinations of octagons are still combinations of octagons.

Proof. Given combination of octagons:
\[R_1 = r_{11} \lor r_{12} \dots r_{1n}\]
\[R_2 = r_{21} \lor r_{22} \dots r_{2n}\]
\(R_1 \lor R_2\) is in the same form of combinations of octagons definition, so it is a combination of octagons as well
\(R_1 \land R_2\) could be converted into disjunction normal form, which is in the same form of combinations of octagons definition as well, so it is a combination of octagons too.

Lemma 4
:   The envelope of a combination of octagons \(R\) under a linear deterministic octagonal multi-region transformation \(T\), \(\env(R,T)\), is still a combination of octagons

Proof.

Consider different portions in \(R\) that contributes to \[R' = \{P'| P' = T(P), P \in R\}\]
As the whole \(Z^n\) space is divided by \(T\)'s regions without any overlaying parts, it should be possible to split R into subsets without overlaying parts with each subset being in a different region of \(T\).

Formally, denote the regions of \(T\) as \(D_1, D_2, \dots D_m\),
\(R\) could be written as
\[R = R_1 \lor R_2 \lor \dots R_m \]
with \[ R_i \Rightarrow D_i\]
and \[ R_i \land R_j = \emptyset, i \neq j \]

Then, for each these subsets, points in it have a contribution in \(R'\), denote them as
\[R_i' = \{P'| P' = T(P), P \in R_i\}\]

\(R_i\) is \(R\) separated by region boundaries of \(T\), thus its boundaries shall be either a boundary from \(R\)'s boundaries or a boundary from \(T\)'s region boundaries. These boundaries all have the forms of \(f_i\), thus according to **Lemma 1**, \(R_i\) is a combination of octagons.

As \(R_i \Rightarrow D_i\), denote the transform vector in \(D_i\) as \(\vec{t_i}\), all points in \(R_i\) are moved by vector \(\vec{t_i}\) to form \(R_i'\), thus the boundaries are just moved by \(\vec{t_i}\) as well, they're still in forms of \(f_i\), thus \(R_i'\)s are combinations of octagons as well.

Then using **Lemma 3** \(R' = R_1' \lor R_2' \lor \dots \lor R_m'\) is a combination of octagons as well.

Also then \(\env(R, T) = R \lor R'\) is a combination of octagons.

Lemma 5
:   If transformation \(T\) is contained in \(R\), and \(R_s \subseteq R\), then \(\env(R_s, T) \subseteq R\)

Proof.

\(T\) is contained in \(R\), so \(\{P'|P' = T(P), P\in R\} \subseteq R\) (**Lemma 2**)

And \[R_s \subseteq R\]
thus \[\{P'|P' = T(P), P \in R_s\} \subseteq \{P'|P' = T(P), P \in R\} \subseteq R \]
thus \[ \env(R_s, T) = R_s \lor
\{P'|P' = T(P), P \in R_s\} \subseteq R\]

## Proof

Theorem 1
:   For an invariant synthesis problem with \(\pref\) in the form of a combination of octagons, an arbitrary \(\postf\) that makes sense, and a linear deterministic octagonal multi-region transformation as its \(\transf\), if :

    - there exisits an invariant \(\invf'\) for this problem, and,
    - the set of \(\invf'\) is a finite set

    Then, there should exist another \(\invf\) for the this problem, and \(\invf\) shall be a combination of octagons,
    and such \(\invf\) could be obtained by expanding \(\pref\) for finite times.

Proof.

First, we take the pre-condition \(\pref\) and expand it to its envelope \(\env(\pref, T) \), it is obivous that \(\pref \subseteq \env(\pref, T)\) according to the definition of envelopes.

We denote the number of points in set \(R\) as \(|R|\), then we have \(|R| \le |\env(R, T)|\) according to the definition of envelopes.

Denote that \[\env^n(R, T) = \env(\env^{n-1}(R, T), T)\]
and \[\env^0(R, T) = R\]

Assume that \[\forall n \in Z, \env^{n-1}(\pref, T) \neq \env^{n}(\pref, T) \].

This leads to \[|\env^{n-1}(\pref, T)| \neq |\env^{n}(\pref, T)| \]

We also have \[|\env^{n-1}(\pref, T)| \le |\env^{n}(\pref, T)|\]

Thus \[  |\env^{n-1}(\pref, T)| < |\env^{n}(\pref, T)| \]

We are on \(Z^n\) space, thus all these should be integer numbers, thus
 \[  |\env^{n-1}(\pref, T)| + 1 \le |\env^{n}(\pref, T)| \]

Expanding the set to its envelope shall at least increase the set size by 1.

On the other hand, according to the original problem, \(T\) shall be contained in \(\invf'\), thus for any subset of \(T\), its envelop under \(T\) shall still be a subset of \(T\) (**Lemma 5**).

So \[\forall n \in Z, \env^n(\pref, T) \subseteq \invf' \]
so \[|\env^n(\pref, T)| \le |\invf'|\]

But as \(\invf'\) is finite, \(|\invf'|\) is finite as well, and the set size increases at least one after expansion.
\[\exists m \in Z, \env^m(\pref, T) > |\invf'|\]

That raise a contradiction, so the assumption shall not hold, thus
\[\exists n \in Z, \env^{n-1}(\pref, T) = \env^n(\pref, T) \]

We denote \(\invf = \env^{n-1}(\pref, T) \),
thus \[\invf = \env(\invf, T) \]
According to **Lemma 2**, this means \(T\) is contained by \(\invf\) and thus \(\invf\) is a invariant solution for the original problem.

According to **Lemma 4**, \(\invf\) is generated by expanding \(\pref\) for finite times, as \(\pref\) is a combination of octagons, \(\invf\) is a combination of octagons as well.

Q.E.D.

# Intermediate ideas: Stable Regions and Safe Zones

- A region is stable if
    - A set expanding into such a region would either:
        - Be expanding infinitely in such region and covering all points in a direction.
        - Reach a fixed point
    - And either way, it would never exit such region again.
    - That property could be captured syntactically using constraints on the formations of the transformation regions and transformation vectors
- With stable regions in a transformation identified, we could define a safe zone for the transformation
    - A safe zone of a transforamtion is a set in the space such that it contains all points that, after a finite number of transforamtion steps, would step into a stable region
    - This could be obtained by "reverse expanding" the stable regions.
- We can defined the compliment of a safe zone as the unsafe zone of the corresponding transformation
- If the unsafe zone is finite, the strongest invariant shall be obtained by keep expanding from the pre condition set.
    - Expanding that way, a set would either
        - Expand into a stable region and yield a part of invariant in that region.
        - Reach a fixed point after some expansions
            - This could be proved in the same manner as the finite invariant proof.

# General ideas: Any system that will not create a loop should work

## Terminology

Transformation Graph
:   For a deterministic multi-region transformation, define two sets:
\[V = \{R_i | R_i \text{ is a region of transformation }T\}\]
\[E = \{ (R_i, R_j) | \exists P \in R_i, T(P) \in R_j \} \]
Then \((V,E)\) effectively defined a directed graph. We note this graph as the
**transformation graph** of transformation \(T\)

Loop-free transformation
:   For a linear deterministic multi-region transformation, if its transformation
graph is acyclic, then we denote this transformation as a **loop-free transformation**

Strong Invariant
:   For a linear deterministic multi-region transformation \(T\) with \(\pref\),
\(\postf\) and one of its invariant \(\invf\), if in addition to the definition
of invariants, this invariant satisfies:
\[\forall P \in \invf, \exists P' \in \pref, \exists n \in Z^{+}, T^{(n)}(P') = P\]


Continuation
:   For a deterministic multi-region transformation \(T\), and a set \(S\),
if there exists another set \(S'\), so that 
\[\forall P' \in S', \exists P \in S, \exists n \ge 0, T^{(n)}(P) = P'\]
and
\[\forall n \ge 0, \forall P \in S, \exists P' \in S', T^{(n)}(P) = P'\]
then we say that \(S'\) is a **continuation** of \(S\) under \(T\), denoted as
\[\mathrm{Cont}(S, T) = S'\]

    Note: as when \( n = 0\), we have \( T^{(0)}(P) = P\), it is obvious that  a
    continuation of a set shall contain all the points in the original set,
    in other words, \( S \subseteq \mathrm{Cont}(S, T)\)

Modular Set
:   For a set \(S\), we call the set
\[S' = S \land (x_i = r_i \mod m_i)\]
as the **modular set** of \(S\) on \(x_i\) with \(r_i \mod m_i\) , denoted as 
\[S' = \mathrm{Modu}(S, x_i, r_i, m_i)\]

Modular Partitions
:   For a set S, we call the series of set
\[\mathrm{Modu}(S, x_i, 1, m_i), \mathrm{Modu}(S, x_i, 2, m_i), \dots, \mathrm{Modu}(S, x_i, m_i - 1, m_i)\]
as a series of **modular partition** of \(S\) on \(x_i\) with \(m_i\)

Modular Octagon
:   We call the modular set of an octagon as a **modular octagon**

Modular Partition of a Set
:   For a linear deterministic multi-region transformation \(T\) with a set \(S\),
we call the following procedure of partitioning set \(S'\) as determining
the modular partition of \(S\) under \(T\)

    First take partitions of \(S\) by the regions of \(T\):
    \[ S_i = S \land R_i \]
    Then, for each \(S_i\), assume that the transformation vector in \(R_i\) is
    \[(\delta x_1^{(i)}, \dots, \delta x_n^{(i)}) \]
    we partition \(S_i\) into \(\delta x_1^{(i)} \times \dots \times \delta x_n^{(i)} \)
    modular partitions:
    \[S_i^{(m_1, \dots, m_n)} = \mathrm{Modu}(\dots \mathrm{Modu}(\mathrm{Modu}( S, x_1, m_1, \delta x_1^{(i)}), x_2, m_2, \delta x_2^{(i)} ) \dots , x_n, m_n, \delta x_n^{(i)} ) \]
    Expressed equivalently:
    \[S_i^{(m_1, \dots, m_n)} = S \land (x_1 = m_1 \mod \delta x_1^{(i)}) \land \dots (x_n = m_n \mod \delta x_n^{(i)}) \]
    And we denote this partition as:
    \[S_i^{(m_1, \dots, m_n)} = \mathrm{MPart}(S, T, R_i, \vec{m}), \vec{m} = (m_1, \dots, m_n)\]

Modular Expansion of a Octagon
:   For a linear deterministic multi-region transformation \(T\) with a set \(S\),
we call the following procedure of determining set \(S'\) as determining
the modular expansion of \(S\) under \(T\)

    First, take the modular partitions of \(S\) under \(T\), for a partition
    \[S_i^{(m_1, \dots, m_n)} \]
    Take its envelope,
    \[\env(S_i^{(m_1, \dots, m_n)}) \]
    and taking the union of all these envelopes as the output
    \[S' = \bigvee_{i, m_1, \dots m_n} \env(S_i^{(m_1, \dots, m_n)}) \]
    Denoted \[S' = \mathrm{MExp}(S, T)\]

Modular Extension of a Octagon
:   For a linear deterministic octagonal multi-region transformation \(T\) with a set \(S\),
we call the following procedure of determining set \(S'\) as determining
the modular extension of \(S\) under \(T\)
    
    First, take the modular partitions of \(S\) under \(T\), for a partition
    \[S_i^{(m_1, \dots, m_n)} \]
    It is trival to note that this set shall be a modular octagon, so it would be
    in the form of
    \[C_1 \land C_2 \land \dots \land (x_1 = m_1 \mod \delta x_1^{(i)}) \land \dots (x_n = m_n \mod \delta x_n^{(i)})\]
    Also note that \(R_i\) shall be an octagon, in form of
    \[C_1'\land C_2' \land \dots \]
    We than do the following for this set:
    
    **This definition of extension is currently broken, the operations above could not guarantee that
    all extensible partitions are captured and are extented in a correct way**

    For each variable \(x_l\) in the problem, it has a corresponding \(\delta x_l\) in
    region \(R_i\) of \(T\), for these values, if they all satisfy the following properties:
        - \(\delta x_l = 0\), or
        - \(\delta x_l > 0\), and \(R_i\) don't have forms of boundries like
            \[ x_l \le c, x_l \pm x_m \le c\], or
        - \(\delta x_l < 0\), and \(R_i\) don't have forms of boundries like
            \[ x_l \ge c, x_l \pm x_m \ge c\]

    Then this partition would be locally extensible, we obtain the extension by:
        - for any \(\delta x_l > 0\), remove all boundries in \(S_i^{(m_1, \dots, m_n)} \) that are in the form
            \[ x_l \le c, x_l \pm x_m \le c\], and
        - for any \(\delta x_l < 0\), remove all boundries in \(S_i^{(m_1, \dots, m_n)} \) that are in the form
            \[ x_l \ge c, x_l \pm x_m \ge c\], and
    
    We denote the set that is obtained after doing these procedures above for each
    variable as the local extension of \(S_i^{(m_1, \dots, m_n)} \)
    \[\mathrm{LExt}(S_i^{(m_1, \dots, m_n)}, T, R_i)  \]
    The final output of modular extension is obtained by taking the union of the
    local extensions of all these partitions:
    \[S' = \bigvee_{i, m_1, \dots m_n} \mathrm{LExt}(S_i^{(m_1, \dots, m_n)}, T, R_i)\]
    Denoted \[S' = \mathrm{MExt}(S, T)\]

## Assumption and Argument

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

The argument we would like to prove is 2-fold. Firstly, we would like to prove that,
if the transformation \(T\) is a loop-free, and we denote the operator \(O\) as
\[O(S, T) = \mathrm{MExp}(\mathrm{MExt}(S, T), T) \]
then there exists \(n \in Z^{+}\), such that
\[O^{n+1}(S, T) = O^{n}(S, T)\]
Secondly, if this \(O^n(S, T)\) satisfies \(O^n(S, T)\subseteq \postf\),
then this \(O^n(S, T)\) is a strong invariant of this problem. 

## Lemmas

Lemma 6
:    For a deterministic multi-region transformation \(T\), and a set \(S\),
if \[ S = S_1 \lor S_2 \lor \dots \lor S_k \],
and \[S_i' = \mathrm{Cont}(S_i, T), i = 1, \dots, k\]
then \[S' = S_1' \lor S_2' \lor \dots \lor S_k' = \mathrm{Cont}(S, T) \]

Proof.

\(\forall P \in S'\), as \(S' = S_1' \lor \dots \lor S_k'\),
\[\exists S_i', P \in S_i'\]

And \(S_i' = \mathrm{Cont}(S_i, T)\), so
\[\exists P' \in S_i, \exists n \ge 0, T^{(n)}(P') = P\]

As \(S_i \subseteq S\), this proves 
\[\exists P' \in S, \exists n \ge 0, T^{(n)}(P') = P\]
and thus justify the first part of continuation definition.

\(\forall P \in S, \forall n \ge 0\), as \(S = S_1 \lor \dots \lor S_k\)
\[\exists S_i, P \in S_i\]

And \(S_i' = \mathrm{Cont}(S_i, T)\), so
\[\exists P' \in S_i', T^{(n)}(P) = P'\]

As \(S_i' \subseteq S'\), this proves 
\[\exists P' \in S', T^{(n)}(P) = P'\]
and thus justify the second part of continuation definition.

So that,
\[S' = \mathrm{Cont}(S, T)\]

Q.E.D.

Lemma 7
:    For a deterministic multi-region transformation \(T\) with \(\pref, \postf\) to form a problem,
and a set \(\invf\).
\[\invf\text{ is a strong invariant of the problem} \Leftrightarrow (\invf = \mathrm{Cont}(\pref, T)) \land (\invf \subseteq \postf)\]

Proof.

The forward direction:

If \(\invf\) is a strong invariant of the problem, then by definition we have \(\invf \subseteq \postf\)

And
\[\forall P \in \invf, \exists P' \in \pref, \exists n \in Z^{+}, T^{(n)}(P') = P\]
This proves the first part of continuation definition.

For the second part, consider \(\forall P \in \pref\), assume \(\exists n \ge 0\)
\[T^{(n)}(P) \notin \invf\]
As \(P\in \pref \subseteq \invf\) there must exist a \(n\ge m\ge 0\), so that
\[T^{(m)}(P) \in \invf, T^{(m + 1)}(P) \notin \invf\]
This contradicts with the \(\invf\) property of
\[\forall Q \in \invf, T(Q) \in \invf\]
So the assumption should not hold, thus
\[\forall n \ge 0, T^{(n)}(P) \in \invf\]
This proves the second part of continuation definition.

So that the forward direction is proven.

Now the backward direction:

If \(S = \mathrm{Cont}(\pref, T)\), according to the first property of continuation, \(\forall P \in \invf\)
\[\exists n\ge 0, \exists Q \in \pref, P = T^{(n)}(Q)\]
then according to the second property of continuation, we have
\[T(P) = T^{(n+1)}(Q) \in \invf\]
This effectively proves
\[\forall P \in \invf, T(P) \in \invf\]
Together with \(\invf \subseteq \postf\) this proves \(\invf\) is a invariant.

We already have the first property of continuation in the same form as the strong
invariant additional property, thus this proves \(\invf\) is a strong invariant.

So that the backward direction is proven.

Q.E.D.


Lemma 8
:   **This lemma is where I found the flaw, this lemma should be used to prove that,
a set is extensible ( S is extensible iff. all points in S would not leave the 
region S is in after arbitrary number of transformations ) iff. the boundary
conditions in the "Modular Extension" definition is met.**

Lemma 9
:   For a linear deterministic octagonal multi-region transformation \(T\) with a octagon \(S\),
if \(S\) is the subset of a region \(R_i\) of \(T\), \(S \subseteq R_i\), and \(S\)
satisfies:
\[\forall n \ge 0, \forall P \in S, T^{(n)}(P)\in R\]
then we take the modular extension of \(S\),
\[S' = \mathrm{MExt}(S, T)\]
\(S'\) shall be a continuation of \(S\) under \(T\):
\[S' = \mathrm{Cont}(S, T)\]

** As the flaw causes the definition of modular extension may need to be modified,
the proof of this lemma needs to be rewritten**

Lemma 10
:   For a linear deterministic multi-region transformation \(T\) with a set \(S\),
\(S\) could be partitioned by regions of \(T\):
\[S_i = S \land R_i, i = 1, \dots, k\]
\[S = S_1 \lor \dots \lor S_k\]
then modular expansion could be performed separately on these partions then unioned:
\[\mathrm{MExp}(S,T) = \mathrm{MExp}(S_1, T) \lor \dots \lor \mathrm{MExp}(S_k, T) \]

Proof.

As the first step of modular expansion is to partition the input set, this argument
is trival to prove


Lemma 11
:   For a linear deterministic octagonal multi-region transformation \(T\) with a octagon \(S\),
\(S\) could be partitioned by regions of \(T\):
\[S_i = S \land R_i, i = 1, \dots, k\]
\[S = S_1 \lor \dots \lor S_k\]
then modular extension could be performed separately on these partions then unioned:
\[\mathrm{MExt}(S,T) = \mathrm{MExt}(S_1, T) \lor \dots \lor \mathrm{MExt}(S_k, T) \]

Proof.

As the first step of modular extension is to partition the input set, this argument
is trival to prove

**the expression this lemma needs to be modified as modular extension definition is to be modified,
but the proof should remain trival even after modification**



## Proof

For an octagonal \(\pref\) that is an octagon in a linear deterministic octagonal
multi-region loop-free transformation \(T\). As the
transformation graph for \(T\) is loop-free, performing transformation continuously
on any point in \(\pref\) shall end up in a region, in which any further
transformation would not move the point out of the region.

Thus, for any point in \(\pref\) we could associate a finite path of regions with it
\[(R_{p_1}, R_{p_2}, \dots R_{p_j}), p_i \neq p_j \text{ when } i\neq j \]
We could partition \(\pref\) based on the different paths associated with the points

**We need an additional lemma here to prove that the modular extension operation
is applicable to these partitions. I thought before that being an octagon should
guarantee that, and the extension operation shall always preserve the octagon
property so it is a trival argument, but now the flaw is that the extension
operation may not preserve that property, so we need to modify both the extension
operation's applicability conditions and the operation itself, then provide a
concrete proof on this lemma**

When the modular expansion and modular extension operation is performed on the 
whole set, take one partition with path \((R_{p_1}, R_{p_2}, \dots, R_{p_j})\)
to consider. Denote this partition as \(S\)

According to the path definition, we should have \(S \subseteq R_{p_1}\), and
\[T^{(i)}(S) \subseteq R_{p_{i+1}}, i < j\]
\[T^{(i)}(S) \subseteq R_{p_j}, i \ge j\]

As \(p_i \neq p_j\) when \( i \neq j\), this means that
\[\forall i < j - 1, \forall P \in T^{(i)}(S), \exists l \ge 0, T^{(l)}(P) \in R_{p_j} \neq R_{p_{i + 1}}\]
So that according to lemma 8, \(T^{(i)}(S), i < j - 1\) would not satisfy the boundary
conditions of modular extension.

This means \(O^{(i)}(S), i < j - 1\) would not satisfy the boundary conditions of
modular extension, and when \(i < j - 1\) \(O^{(i)}(S) = S \lor T(S) \lor \dots T^{(i)}(S) \)

Proof for this argument could be done by induction:

for \(i = 0\), \(O^{(i)}(S) = S = T^{(0)}(S) \)
as \(0 < j - 1\), S would not satisfy the conditions.

Assume \(O^{(k)}(S), k + 1 < j - 1\) does not satisfy the conditions, and \(O^{(k)}(S) = S \lor T(S) \lor \dots T^{(k)}(S)\)
then \(O^{(k+1)}(S) = O(O^{(k)}(S)) = \mathrm{MExp}(\mathrm{MExt}(O^{(k)}(S)))\)
as \(O^{(k)}(S)\) does not satisfy the conditions, 
\[\mathrm{MExt}(O^{(k)}(S)) = O^{(k)}(S) \]
so \[O^{(k+1)}(S) = \mathrm{MExp}(O^{(k)}(S)) = T(O^{(k)}(S)) \lor O^{(k)}(S)\]
put \(O^{(k)}(S)\) expression in, and we've got
\[O^{(k+1)}(S) = S \lor T(S) \lor \dots \lor T^{(k+1)}(S)\]
And this form shall not satisfy the conditions, either.

Thus by mathematical induction the original argument is proven

This means \(O\) operations do not apply \(\mathrm{MExt}\) on \(O^{(i)}(S)\) when
\(i < j - 1\)

When \(i = j\), \[O^{(i)}(S) = O^{(j)}(S) = O(O^{(j - 1)}(S))\],
And \[O^{(j-1)}(S) = O(O^{(j-2)}(S)) = \mathrm{MExp}(T\lor T(S) \lor \dots \lor T^{(j-2)}(S)) = O^{(j-2)}(S)\lor T^{(j-1)}(S)\]
According to the definition of paths, \(\forall P \in T^{(j-1)}(S), \forall n \ge 0\)
\[T^{(n)}(P) \in R_{p_j}\]
So according to lemma 9
\[\mathrm{MExt}(T^{(j-1)}(S)) = \mathrm{Cont}(T^{(j-1)}(S)) \]
As \[\mathrm{MExt}(O^{(j-2)}(S)) = O^{(j-2)}(S)\]
and \[T^{(j-1)}(S)\subseteq R_{q_j}, O^{(j-2)}(S) \land R_{q_j}  = \emptyset\]
if we partition \(O^{(j-2)}(S)\) by the regions to \(P_1, P_2, \dots, P_t\)
then \(P_1, P_2, \dots, P_t, T^{(j-1)}(S) \) shall be a valid partition by regions
of \(O^{(j-1)}(S)\)

Then according to lemma 11, \(\mathrm{MExt}(O^{(j-1)}(S)) = \mathrm{Cont}(T^{(j-1)}(S))\lor O^{(j-2)}(S)\)

As \[O^{(j-2)}(S) = T\lor T(S) \lor \dots \lor T^{(j-2)}(S)\]
\(\mathrm{MExt}(O^{(j-1)}(S)) \) shall satisfy the two properties of continuation
of \(S\) as well
So \[\mathrm{MExt}(O^{(j-1)}(S)) = \mathrm{Cont}(S)\]
Then \[ O^{(j)}(S) = \mathrm{MExp}(\mathrm{Cont}(S))\]

According to the definition of continuation, it is trival that
\[\mathrm{MExp}(\mathrm{Cont}(S)) = \mathrm{Cont}(S)\]

So \[O^{(j)}(S) = \mathrm{Cont}(S)\]

Also we have that \[O^{(i + 1)}(S) = O^{(i)}(S), i \ge j\]

For this particular partition, after \(j\)th \(O\) operation, we obtain the continuation of the partition

Then for the entire \(\pref\), after sufficient times of \(O\) operations, every
partition transforms to the continuation of that partition

Thus according to lemma 6, the whole set of \(\pref\) after sufficient times of operations,
turns into a continuation of \(\pref\)

According to lemma 7, this proves that, if this continuation \(C\) has \(C\subseteq \postf\),
then it is a strong invariant to the problem.




