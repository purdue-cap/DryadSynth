
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
(constraint (= (f "Hone Albert") "A"))
(constraint (= (f "Aysu Polat") "P"))
(constraint (= (f "Fatma Yilmaz") "Y"))
(constraint (= (f "Artem Kuznetsov") "K"))
(constraint (= (f "Cecep Sutresna") "S"))
(constraint (= (f "Milica Zujovic") "Z"))
(constraint (= (f "Radmilo Bogic") "B"))
(constraint (= (f "Snezhana Bozhilova") "B"))
(constraint (= (f "Ojerinde Adeniyi") "A"))
(constraint (= (f "Daniyar Shakenuly") "S"))
(check-synth)
