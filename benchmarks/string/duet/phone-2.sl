(set-logic SLIA)
(synth-fun f ((name String)) String
    ((Start String (ntString))
     (ntString String (name " "
(str.++ ntString ntString)
(str.replace ntString ntString ntString)
(str.at ntString ntInt)
(int.to.str ntInt)
(ite ntBool ntString ntString)
(str.substr ntString ntInt ntInt)
))
      (ntInt Int (0 1 2 3 4 5
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
(constraint (= (f "938-242-504") "504"))
(constraint (= (f "308-916-545") "545"))
(constraint (= (f "623-599-749") "749"))
(constraint (= (f "981-424-843") "843"))
(constraint (= (f "118-980-214") "214"))
(constraint (= (f "244-655-094") "094"))

(check-synth)

