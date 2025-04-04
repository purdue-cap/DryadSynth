
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("Word" "Word 1" "0 Word" "Word 2" "3 Word" "," "Word 3" "1 Word" arg0
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
(constraint (= (f "Word 10 Word 6 Word 2143 Word") "October 6, 2143"))
(constraint (= (f "Word 3 Word 30 Word 2002 Word") "March 30, 2002"))
(constraint (= (f "Word 1 Word 31 Word 1846 Word") "January 31, 1846"))
(constraint (= (f "Word 4 Word 22 Word 1953 Word") "April 22, 1953"))
(constraint (= (f "Word 3 Word 30 Word 2241 Word") "March 30, 2241"))
(check-synth)
