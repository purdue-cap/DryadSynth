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

Assume that the form of \(\transf\) is:
\[\begin{split}
\transf \Leftrightarrow ( &C_1^{(1)} \land C_2^{(1)} \land \dots
\\ &T^{(1)}_1 \land T^{(1)}_2 \land \dots \land T^{(1)}_n) \lor \\
( &C_1^{(2)} \land C_2^{(2)} \land \dots
\\ &T^{(2)}_1 \land T^{(2)}_2 \land \dots \land T^{(2)}_n) \lor \\
& \dots
\end{split}\]
with elements, or atoms in that form having the form
\[C_j^{(i)} \Leftrightarrow  \begin{cases}x_k \ge c \\ x_k > c \\ x_k < c \\ x_k \le c \\ x_l - x_k \ge c \\ x_l - x_k > c \\ x_l - x_k \le c \\ x_l - x_k < c\end{cases}\]
\[T_j^{(i)} \Leftrightarrow (x_k' = x_k + c)\]

We would like to prove the argument that given this assumption as a constraint, if there exists an invariant, there must exist another invariant that have the form
\[\invf \Leftrightarrow C_1' \land C_2' \dots \]
with \(C_i\) being in the same form as \(C_j^{(i)}\)
