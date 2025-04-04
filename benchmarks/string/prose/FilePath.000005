
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("\\" "-" arg0
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
(constraint (= (f "Quia\\Natus\\Ad Nostrum Totam Harum Totam - Doloremque Eos Asperiores Cum Ipsam\\Aperiam Cumque Non") "Aperiam Cumque Non"))
(constraint (= (f "Amet\\Qui\\omnis\\A Quas - Sed") "A Quas - Sed"))
(constraint (= (f "Blanditiis\\Ratione\\Dolorum Corrupti Adipisci Debitis - Id Vero Soluta Fuga Eius\\Quo") "Quo"))
(constraint (= (f "Repellat\\Deleniti\\aut\\Et") "Et"))
(check-synth)
