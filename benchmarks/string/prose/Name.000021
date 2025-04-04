
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
(constraint (= (f "Hone Albert") "Al"))
(constraint (= (f "Aysu Polat") "Po"))
(constraint (= (f "Fatma Yilmaz") "Yi"))
(constraint (= (f "Artem Kuznetsov") "Ku"))
(constraint (= (f "Cecep Sutresna") "Su"))
(constraint (= (f "Milica Zujovic") "Zu"))
(constraint (= (f "Radmilo Bogic") "Bo"))
(constraint (= (f "Snezhana Bozhilova") "Bo"))
(constraint (= (f "Ojerinde Adeniyi") "Ad"))
(constraint (= (f "Daniyar Shakenuly") "Sh"))
(check-synth)
