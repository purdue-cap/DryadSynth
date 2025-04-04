
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
(constraint (= (f "Hone Albert") "Albe"))
(constraint (= (f "Aysu Polat") "Pola"))
(constraint (= (f "Fatma Yilmaz") "Yilm"))
(constraint (= (f "Artem Kuznetsov") "Kuzn"))
(constraint (= (f "Cecep Sutresna") "Sutr"))
(constraint (= (f "Milica Zujovic") "Zujo"))
(constraint (= (f "Radmilo Bogic") "Bogi"))
(constraint (= (f "Snezhana Bozhilova") "Bozh"))
(constraint (= (f "Ojerinde Adeniyi") "Aden"))
(constraint (= (f "Daniyar Shakenuly") "Shak"))
(check-synth)
