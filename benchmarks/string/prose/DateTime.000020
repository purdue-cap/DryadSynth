
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("-" "/" arg0
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
(constraint (= (f "06-Oct-2143") ""))
(constraint (= (f "Mar 30 2002") ""))
(constraint (= (f "01311846") ""))
(constraint (= (f "22 Apr 1953 11:34") "22/4/1953"))
(constraint (= (f "03302241") ""))
(constraint (= (f "02-Aug-2160") ""))
(constraint (= (f "23 May 1984 10:24") "23/5/1984"))
(constraint (= (f "15 August 1740") ""))
(constraint (= (f "Jul 08 2237") ""))
(constraint (= (f "3 Nov 1904") "3/11/1904"))
(check-synth)
