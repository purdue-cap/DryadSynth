
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("-" "." ".9" "/xP" "MFM-" "/xPhone-" "/xPad-" "/xPhone-5." arg0
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
(constraint (= (f "MFM-5.2.59/xPhone-4.9") "4.9"))
(constraint (= (f "MFM-6.4.90/xPad-8.9") "8.9"))
(constraint (= (f "MFM-8.9.06/xPhone-5.9") "5.9"))
(constraint (= (f "MFM-1.4.29/xPad-6.7.0") "6.7.0"))
(constraint (= (f "MFM-5.7.12/xPhone-9.7") "9.7"))
(constraint (= (f "MFM-2.4.76/xPad-4.1.8") "4.1.8"))
(constraint (= (f "MFM-6.6.28/xPhone-8.0") "8.0"))
(constraint (= (f "MFM-8.2.94/xPhone-6.7.8") "6.7.8"))
(constraint (= (f "MFM-1.9.14/xPhone-5.3.6") "5.3.6"))
(constraint (= (f "MFM-7.7.92/xPhone-5.6") "5.6"))
(constraint (= (f "MFM-2.1.06/xPhone-6.6") "6.6"))
(check-synth)
