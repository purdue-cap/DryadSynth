
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("." "@" ".com" "woodgrovebank.com" "@woodgrovebank.com" "s.com" arg0
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
(constraint (= (f "Hone.Albert@southridgevideo.com") "southridgevideo.com"))
(constraint (= (f "Kiriakos.Sarlis@margiestravel.com") "margiestravel.com"))
(constraint (= (f "Artem.Kuznetsov@woodgrovebank.com") "woodgrovebank.com"))
(constraint (= (f "Lisandro.Milanesi@lamnahealtcare.com") "lamnahealtcare.com"))
(constraint (= (f "Radmilo.Bogic@woodgrovebank.com") "woodgrovebank.com"))
(constraint (= (f "Archana.Panigrahi@woodgrovebank.com") "woodgrovebank.com"))
(constraint (= (f "Daniyar.Shakenuly@tailspintoys.com") "tailspintoys.com"))
(constraint (= (f "Fatou.Ndiaye@adventure-works.com") "adventure-works.com"))
(constraint (= (f "Niklas.Bengtsson@wideworldimporters.com") "wideworldimporters.com"))
(constraint (= (f "Akaraporn.Chaiprasit@alpineskihouse.com") "alpineskihouse.com"))
(check-synth)
