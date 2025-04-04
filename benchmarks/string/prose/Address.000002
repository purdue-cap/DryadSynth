
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("," "(" ")" "-" ",000-" "th Place" "03th Place" "th Place SE" "th Place NE" arg0
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
(constraint (= (f "Aysu Fatma Ahmed 492 24th Place NW,Edison,AK,(896) 388-9065,000-93-6876,38891") "492 24th Place"))
(constraint (= (f "Fiamma Greco 967 03th Place SE,Long Beach,OK,(129) 734-1247,000-61-4879,03719") "967 03th Place SE"))
(constraint (= (f "Muralixxxxx Hasmik Drakou 880 81th Place SE,Bras\u00edlia,WY,(467) 817-7191,000-47-5407,17293") "880 81th Place"))
(constraint (= (f "Joyikuttyxxxxx Charles Arjun 256 32th Place NE,Kiev,AR,(666) 929-0960,000-14-1069,41230") "256 32th Place"))
(constraint (= (f "Elise Vadeboncoeur 526 03th Place NE,Curitiba,OH,(993) 525-5024,000-84-6131,03719") "526 03th Place NE"))
(check-synth)
