
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
(constraint (= (f "Hone Albert") "Hone A"))
(constraint (= (f "Aysu Polat") "Aysu P"))
(constraint (= (f "Fatma Yilmaz") "Fatma Y"))
(constraint (= (f "Artem Kuznetsov") "Artem K"))
(constraint (= (f "Cecep Sutresna") "Cecep S"))
(constraint (= (f "Milica Zujovic") "Milica Z"))
(constraint (= (f "Radmilo Bogic") "Radmilo B"))
(constraint (= (f "Snezhana Bozhilova") "Snezhana B"))
(constraint (= (f "Ojerinde Adeniyi") "Ojerinde A"))
(constraint (= (f "Daniyar Shakenuly") "Daniyar S"))
(check-synth)
