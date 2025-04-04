(set-logic SLIA)
(synth-fun f ((firstname String) (lastname String)) String
    ((Start String (ntString))
     (ntString String (firstname lastname " " "."
(str.++ ntString ntString)
(str.replace ntString ntString ntString)
(str.at ntString ntInt)
(int.to.str ntInt)
(ite ntBool ntString ntString)
(str.substr ntString ntInt ntInt)
))
      (ntInt Int (0 1 2
(+ ntInt ntInt)
(- ntInt ntInt)
(str.len ntString)
(str.to.int ntString)
(str.indexof ntString ntString ntInt)
))
(ntBool Bool (true false
(= ntInt ntInt)
(str.prefixof ntString ntString)
(str.suffixof ntString ntString)
(str.contains ntString ntString)
))
))
(constraint (= (f "Launa" "Withers") "L. Withers"))
(constraint (= (f "Lakenya" "Edison") "L. Edison"))
(constraint (= (f "Brendan" "Hage") "B. Hage"))
(constraint (= (f "Bradford" "Lango") "B. Lango"))
(constraint (= (f "Rudolf" "Akiyama") "R. Akiyama"))
(constraint (= (f "Lara" "Constable") "L. Constable"))

(check-synth)

