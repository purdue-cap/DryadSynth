
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("&" ";" "-" ":" "; Station 3" "; 2015-12-10 @ 1" "; 2015-12-10 @ 16:" arg0
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
(constraint (= (f "REINDEER CT & DEAD END; NEW HANOVER; Station 332; 2015-12-10 @ 17:10:52;") "New Hanover"))
(constraint (= (f "HAWS AVE; NORRISTOWN; 2015-12-10 @ 14:39:21-Station:STA27;") "Norristown"))
(constraint (= (f "AIRY ST & SWEDE ST; NORRISTOWN; Station 308A; 2015-12-10 @ 16:47:36;") "Norristown"))
(constraint (= (f "BRIAR PATH & WHITEMARSH LN; HATFIELD TOWNSHIP; Station 345; 2015-12-10 @ 17:29:21;") "Hatfield Township"))
(constraint (= (f "CHERRYWOOD CT & DEAD END; LOWER POTTSGROVE; Station 329; 2015-12-10 @ 16:56:52;") "Lower Pottsgrove"))
(constraint (= (f "CANNON AVE & W 9TH ST; LANSDALE; Station 345; 2015-12-10 @ 15:39:04;") "Lansdale"))
(constraint (= (f "LAUREL AVE & OAKDALE AVE; HORSHAM; Station 352; 2015-12-10 @ 16:46:48;") "Horsham"))
(constraint (= (f "COLLEGEVILLE RD & LYWISKI RD; SKIPPACK; Station 336; 2015-12-10 @ 16:17:05;") "Skippack"))
(constraint (= (f "MAIN ST & OLD SUMNEYTOWN PIKE; LOWER SALFORD; Station 344; 2015-12-10 @ 16:51:42;") "Lower Salford"))
(constraint (= (f "BLUEROUTE & RAMP I476 NB TO CHEMICAL RD; PLYMOUTH; 2015-12-10 @ 17:35:41;") "Plymouth"))
(constraint (= (f "RT202 PKWY & KNAPP RD; MONTGOMERY; 2015-12-10 @ 17:33:50;") "Montgomery"))
(constraint (= (f "BROOK RD & COLWELL LN; PLYMOUTH; 2015-12-10 @ 16:32:10;") "Plymouth"))
(check-synth)
