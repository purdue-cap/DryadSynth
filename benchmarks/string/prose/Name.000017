
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
(constraint (= (f "Hone Albert") "Hone"))
(constraint (= (f "Aysu Polat") "Aysu"))
(constraint (= (f "Fatma Yilmaz") "Fatm"))
(constraint (= (f "Artem Kuznetsov") "Arte"))
(constraint (= (f "Cecep Sutresna") "Cece"))
(constraint (= (f "Milica Zujovic") "Mili"))
(constraint (= (f "Radmilo Bogic") "Radm"))
(constraint (= (f "Snezhana Bozhilova") "Snez"))
(constraint (= (f "Ojerinde Adeniyi") "Ojer"))
(constraint (= (f "Daniyar Shakenuly") "Dani"))
(check-synth)
