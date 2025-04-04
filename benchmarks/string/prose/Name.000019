
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
(constraint (= (f "Hone Albert") "Hone Al"))
(constraint (= (f "Aysu Polat") "Aysu Po"))
(constraint (= (f "Fatma Yilmaz") "Fatma Yi"))
(constraint (= (f "Artem Kuznetsov") "Artem Ku"))
(constraint (= (f "Cecep Sutresna") "Cecep Su"))
(constraint (= (f "Milica Zujovic") "Milica Zu"))
(constraint (= (f "Radmilo Bogic") "Radmilo Bo"))
(constraint (= (f "Snezhana Bozhilova") "Snezhana Bo"))
(constraint (= (f "Ojerinde Adeniyi") "Ojerinde Ad"))
(constraint (= (f "Daniyar Shakenuly") "Daniyar Sh"))
(check-synth)
