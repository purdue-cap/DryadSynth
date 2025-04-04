
    (set-logic SLIA)
    (synth-fun f ((arg0 String) (arg1 String) (arg2 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("/" "/2" "/200" "/21/200" arg0 arg1 arg2
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
(constraint (= (f "5" "6" "2001") "05/06/2001"))
(constraint (= (f "11" "21" "2001") "11/21/2001"))
(constraint (= (f "4" "20" "2002") "04/20/2002"))
(constraint (= (f "12" "5" "2002") "12/05/2002"))
(constraint (= (f "8" "16" "2001") "08/16/2001"))
(constraint (= (f "2" "21" "2002") "02/21/2002"))
(constraint (= (f "9" "21" "2002") "09/21/2002"))
(constraint (= (f "10" "5" "2001") "10/05/2001"))
(check-synth)
