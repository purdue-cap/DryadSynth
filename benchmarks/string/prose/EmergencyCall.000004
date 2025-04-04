
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("&" ";" "-" "@" ":" "; 2015-12-1" "D; LOWER MERION;" "RD; LOWER MERION;" "; LOWER MERION; Station 313; 2015-12-1" "D; LOWER MERION; 2015-12-1" "; 2015-12-10 @" "12 Oct 2015" arg0
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
(constraint (= (f "MONTGOMERY AVE & WOODSIDE RD; LOWER MERION; Station 313; 2015-12-11 @ 04:11:35;") "12 Nov 2015 4AM"))
(constraint (= (f "DREYCOTT LN & W LANCASTER AVE; LOWER MERION; Station 313; 2015-12-11 @ 01:29:52;") "12 Nov 2015 1AM"))
(constraint (= (f "E LEVERING MILL RD & CONSHOHOCKEN STATE RD; LOWER MERION; 2015-12-11 @ 07:29:58;") "12 Nov 2015 7AM"))
(constraint (= (f "PENN VALLEY RD & MANOR RD; LOWER MERION; Station 313; 2015-12-10 @ 20:53:30;") "12 Oct 2015 8PM"))
(constraint (= (f "BELMONT AVE & OVERHILL RD; LOWER MERION; 2015-12-10 @ 23:02:27;") "12 Oct 2015 11PM"))
(constraint (= (f "W MONTGOMERY AVE & PENNSWOOD RD; LOWER MERION; 2015-12-10 @ 19:25:22;") "12 Oct 2015 7PM"))
(constraint (= (f "ROSEMONT AVE & DEAD END; LOWER MERION; Station 313; 2015-12-10 @ 18:43:07;") "12 Oct 2015 6PM"))
(constraint (= (f "AVIGNON DR & DEAD END; LOWER MERION; 2015-12-10 @ 20:01:29-Station:STA24;") "12 Oct 2015 8PM"))
(check-synth)
