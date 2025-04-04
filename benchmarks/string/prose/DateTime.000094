
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("-" "-1" "." "/" arg0
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
(constraint (= (f "1973-12-17") "1973-12-17"))
(constraint (= (f "9 April 1987") "1987-04-09"))
(constraint (= (f "14. November 2013") "2013-11-14"))
(constraint (= (f "27.3.1990") "1990-03-27"))
(constraint (= (f "21.02.2006") "2006-02-21"))
(constraint (= (f "19.12.97") "1997-12-19"))
(constraint (= (f "30/9/2004") "2004-09-30"))
(constraint (= (f "18/05/1967") "1967-05-18"))
(check-synth)
