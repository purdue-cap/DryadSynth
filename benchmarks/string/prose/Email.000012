
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("." "@" ".com" "@fabrikam" arg0
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
(constraint (= (f "Hone.Albert@southridgevideo.com") "Albert"))
(constraint (= (f "Kiriakos.Sarlis@margiestravel.com") "Sarlis"))
(constraint (= (f "Artem.KuznetsovNabiyeva@fabrikam.com") "KuznetsovNabiyeva"))
(constraint (= (f "Milica.Zujovic@nodpublishers.com") "Zujovic"))
(constraint (= (f "Anna.Traver@adventure-works.com") "Traver"))
(constraint (= (f "Ojerinde.AdeniyiLabrecque@fabrikamresidences.com") "AdeniyiLabrecque"))
(constraint (= (f "Snorre.Kolstad@woodgrovebank.com") "Kolstad"))
(constraint (= (f "Rodel.Torres@lucernepublishing.com") "Torres"))
(constraint (= (f "Habiba.HaidariChaiprasit@alpineskihouse.com") "HaidariChaiprasit"))
(constraint (= (f "Svetlana.Konovalova@fabrikam.com") "Konovalova"))
(check-synth)
