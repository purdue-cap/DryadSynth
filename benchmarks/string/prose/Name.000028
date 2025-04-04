
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("-" arg0
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
(constraint (= (f "Hone-Kiriakos Yilmaz") "Hone"))
(constraint (= (f "Artem Milanesi") "Artem"))
(constraint (= (f "Bidisha-Snezhana Panigrahi") "Bidisha"))
(constraint (= (f "Maureen-Snorre Ndiaye") "Maureen"))
(constraint (= (f "Rodel-Habiba Chaiprasit") "Rodel"))
(constraint (= (f "Jan Evans") "Jan"))
(constraint (= (f "Antonia Lusis") "Antonia"))
(constraint (= (f "Barbara Susila") "Barbara"))
(constraint (= (f "Meena Orav") "Meena"))
(constraint (= (f "Afonso-Igor Krause") "Afonso"))
(check-synth)
