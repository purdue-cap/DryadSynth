
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("." "," "/" ":" "M,8.111111,311,chicago" "PM,8.111111,311,chicago" "22ABDDK_3388.XY,9/12/2002" "-" "2002-09-12" "AM,8.111111,311,chicago" "2002-09-12 0" arg0
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
(constraint (= (f "22ABDDK_3388.XY,9/12/2002 6:25:57 PM,8.111111,311,chicago") "2002-09-12 18:25:57"))
(constraint (= (f "22ABDDK_3388.XY,9/12/2002 9:44:40 PM,8.111111,311,chicago") "2002-09-12 21:44:40"))
(constraint (= (f "22ABDDK_3388.XY,9/12/2002 7:00:20 AM,8.111111,311,chicago") "2002-09-12 07:00:20"))
(constraint (= (f "22ABDDK_3388.XY,9/12/2002 11:34:17 PM,8.111111,311,chicago") "2002-09-12 23:34:17"))
(constraint (= (f "22ABDDK_3388.XY,9/12/2002 4:15:08 PM,8.111111,311,chicago") "2002-09-12 16:15:08"))
(constraint (= (f "22ABDDK_3388.XY,9/12/2002 11:48:20 PM,8.111111,311,chicago") "2002-09-12 23:48:20"))
(constraint (= (f "22ABDDK_3388.XY,9/12/2002 10:24:59 PM,8.111111,311,chicago") "2002-09-12 22:24:59"))
(constraint (= (f "22ABDDK_3388.XY,9/12/2002 4:29:27 AM,8.111111,311,chicago") "2002-09-12 04:29:27"))
(constraint (= (f "22ABDDK_3388.XY,9/12/2002 3:25:29 PM,8.111111,311,chicago") "2002-09-12 15:25:29"))
(constraint (= (f "22ABDDK_3388.XY,9/12/2002 8:14:07 AM,8.111111,311,chicago") "2002-09-12 08:14:07"))
(constraint (= (f "22ABDDK_3388.XY,9/12/2002 2:51:17 AM,8.111111,311,chicago") "2002-09-12 02:51:17"))
(constraint (= (f "22ABDDK_3388.XY,9/12/2002 8:05:30 AM,8.111111,311,chicago") "2002-09-12 08:05:30"))
(check-synth)
