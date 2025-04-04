
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("," arg0
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
(constraint (= (f "One Cherry Court, Greensboro, VA 38891") ""))
(constraint (= (f "234 Maple Blvd, Edison, AK 58712") "234"))
(constraint (= (f "2345 1st Rte, Kinshasa, OH 38891") "2345"))
(constraint (= (f "23 Fifth Ave SE, Maputo, WY 41230") "23"))
(constraint (= (f "89 123rd Blvd, Corona, CT 17293") "89"))
(constraint (= (f "234 Fourth Ct, Omsk, OK 41230") "234"))
(constraint (= (f "One Main Parkway, Allentown, ND 41230") ""))
(constraint (= (f "One Second Way, Ekurhuleni, NY 58712") ""))
(constraint (= (f "789 Oak Pkwy, Yangon, OR 41230") "789"))
(constraint (= (f "234 CHERRY PL, Knoxville, MN 58712") "234"))
(check-synth)
