
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("." "@" ".com" "@woodgrovebank.com" arg0
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
(constraint (= (f "Hone.Albert@southridgevideo.com") "Hone"))
(constraint (= (f "Kiriakos.Sarlis@margiestravel.com") "Kiriakos"))
(constraint (= (f "Artem.Kuznetsov@woodgrovebank.com") "Artem"))
(constraint (= (f "Lisandro.Milanesi@lamnahealtcare.com") "Lisandro"))
(constraint (= (f "Radmilo.Bogic@woodgrovebank.com") "Radmilo"))
(constraint (= (f "Archana.Panigrahi@woodgrovebank.com") "Archana"))
(constraint (= (f "Daniyar.Shakenuly@tailspintoys.com") "Daniyar"))
(constraint (= (f "Fatou.Ndiaye@adventure-works.com") "Fatou"))
(constraint (= (f "Niklas.Bengtsson@wideworldimporters.com") "Niklas"))
(constraint (= (f "Akaraporn.Chaiprasit@alpineskihouse.com") "Akaraporn"))
(check-synth)
