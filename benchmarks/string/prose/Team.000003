
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("(" ")" arg0
                (str.++ ntString ntString) 
                (str.replace ntString ntString ntString) 
                (str.at ntString ntInt)
                (int.to.str ntInt)
                (ite ntBool ntString ntString)
                (str.substr ntString ntInt ntInt)
            )) 
            (ntInt Int (
                1 0 -1
                (+ ntInt ntInt)
                (- ntInt ntInt)
                (str.len ntString)
                (str.to.int ntString)
                (ite ntBool ntInt ntInt)
                (str.indexof ntString ntString ntInt)
            )) 
            (ntBool Bool (
                true false
                (= ntInt ntInt)
                (str.prefixof ntString ntString)
                (str.suffixof ntString ntString)
                (str.contains ntString ntString)
            ))
        )
    )
(constraint (= (f "Green Bay Packers (2)") "Green Bay Packers"))
(constraint (= (f "New York Jets") "New York Jets"))
(constraint (= (f "Dallas Cowboys (2)") "Dallas Cowboys"))
(constraint (= (f "Baltimore Colts (4)") "Baltimore Colts"))
(constraint (= (f "New England Patriots (5)") "New England Patriots"))
(constraint (= (f "Seattle Seahawks (2)") "Seattle Seahawks"))
(check-synth)
