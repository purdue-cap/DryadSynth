
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("." "\u20b9" "," arg0
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
(constraint (= (f "36345645.34") "\u20b93,63,45,645"))
(constraint (= (f "12.31") "\u20b912"))
(constraint (= (f "2323.23") "\u20b92,323"))
(constraint (= (f "121") "\u20b9121"))
(constraint (= (f "56343") "\u20b956,343"))
(constraint (= (f "34534.21") "\u20b934,534"))
(constraint (= (f "74563.22") "\u20b974,563"))
(constraint (= (f "3214523.67") "\u20b932,14,524"))
(constraint (= (f "23.54") "\u20b924"))
(constraint (= (f "454676.99") "\u20b94,54,677"))
(check-synth)
