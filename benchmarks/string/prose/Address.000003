
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("," "(" ")" "-" ",000-" "th Lane" "th Lane ," "09th Lane" "30th Lane" arg0
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
(constraint (= (f "Akila Kadija 643 91th Lane ,Yonkers,AK,(239) 662-6779,000-09-1609,17293") "643 91th Lane"))
(constraint (= (f "Kotonexxxxx Janjic 576 09th Lane ,Surprise,TX,(055) 296-3466,000-47-2445,17293") "576 09th Lane"))
(constraint (= (f "Sonia Rees 319 09th Lane ,Baltimore,NY,(012) 035-3987,000-22-1009,03719") "319 09th Lane"))
(constraint (= (f "Manohar Prabhukonkar 914 30th Lane ,Denver,LA,(330) 688-7008,000-22-8729,38891") "914 30th Lane"))
(constraint (= (f "Fahmi Ibrahim 435 30th Lane ,Tangshan,IL,(471) 189-3968,000-69-9417,38891") "435 30th Lane"))
(check-synth)
