
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("." "-" arg0
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
(constraint (= (f "01. Assumenda6 - Natus9") "Assumenda6"))
(constraint (= (f "02. Ad3 - Harum5") "Ad3"))
(constraint (= (f "03. Eos9 - Ipsam8") "Eos9"))
(constraint (= (f "04. Aperiam7 - Recusandae7") "Aperiam7"))
(constraint (= (f "05. Amet5 - Doloribus6") "Amet5"))
(constraint (= (f "06. Vel4 - Quas7") "Vel4"))
(constraint (= (f "07. Et8 - Blanditiis2") "Et8"))
(constraint (= (f "08. Porro6 - Adipisci8") "Porro6"))
(constraint (= (f "09. Vero7 - Eius9") "Vero7"))
(constraint (= (f "010. Quo2 - Dolorem2") "Quo2"))
(check-synth)
