
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("," "/" arg0
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
(constraint (= (f "Sunday, 2143W12") "2143 24 3"))
(constraint (= (f "2192 100") "2192 9 4"))
(constraint (= (f "7/2/1952") "1952 7 2"))
(constraint (= (f "Wednesday, 1712W36") "1712 7 9"))
(constraint (= (f "14/7/2143") "2143 14 7"))
(constraint (= (f "26/6/2079") "2079 26 6"))
(constraint (= (f "Monday, 1995W25") "1995 19 6"))
(constraint (= (f "8/7/2237") "2237 8 7"))
(constraint (= (f "3/11/1904") "1904 3 11"))
(constraint (= (f "2088 331") "2088 26 11"))
(check-synth)
