
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ( arg0
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
(constraint (= (f "Hone Albert") "ha"))
(constraint (= (f "Aysu Polat") "ap"))
(constraint (= (f "Fatma Yilmaz") "fy"))
(constraint (= (f "Artem Kuznetsov") "ak"))
(constraint (= (f "Cecep Sutresna") "cs"))
(constraint (= (f "Milica Zujovic") "mz"))
(constraint (= (f "Radmilo Bogic") "rb"))
(constraint (= (f "Snezhana Bozhilova") "sb"))
(constraint (= (f "Ojerinde Adeniyi") "oa"))
(constraint (= (f "Daniyar Shakenuly") "ds"))
(check-synth)
