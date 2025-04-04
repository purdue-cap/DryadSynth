
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
(constraint (= (f "Hone Albert") "Alb"))
(constraint (= (f "Aysu Polat") "Pol"))
(constraint (= (f "Fatma Yilmaz") "Yil"))
(constraint (= (f "Artem Kuznetsov") "Kuz"))
(constraint (= (f "Cecep Sutresna") "Sut"))
(constraint (= (f "Milica Zujovic") "Zuj"))
(constraint (= (f "Radmilo Bogic") "Bog"))
(constraint (= (f "Snezhana Bozhilova") "Boz"))
(constraint (= (f "Ojerinde Adeniyi") "Ade"))
(constraint (= (f "Daniyar Shakenuly") "Sha"))
(check-synth)
