
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("," "One Madeup Street," arg0
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
(constraint (= (f "2345 Redwood Road NE, Greensboro, VA 38891") "2345"))
(constraint (= (f "78 Seventh Boulevard SE, Denver, WY 03719") "78"))
(constraint (= (f "7890 Pecan Place SW, Vallejo, WY 03719") "7890"))
(constraint (= (f "456 48th Highway SE, Rio de Janeiro, FL 38891") "456"))
(constraint (= (f "One Madeup Street, South Bend, NY 41230") ""))
(constraint (= (f "One Madeup Street, Prague, KY 58712") ""))
(constraint (= (f "890 Third Place SW, Midland, KY 38891") "890"))
(constraint (= (f "One Madeup Street, Belo Horizonte, MD 41230") ""))
(constraint (= (f "345 Fifth Way SW, Lakewood, TX 41230") "345"))
(constraint (= (f "67 12th Way NE, Athens, DC 41230") "67"))
(check-synth)
