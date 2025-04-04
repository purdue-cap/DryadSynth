
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("." arg0
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
(constraint (= (f "Abdoulaye Drubych\n123 First Ln.\nVancouver\nMI\n38891") "123 First Ln."))
(constraint (= (f "Rohini Che\n90 Cherry Pkwy.\nCleveland\nNE\n38891") "90 Cherry Pkwy."))
(constraint (= (f "Nitin Lapsa\n3456 Pecan Ave.\nRiyadh\nCO\n17293") "3456 Pecan Ave."))
(check-synth)
