
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("-" ":" "." "/" "+" ";" arg0
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
(constraint (= (f "2008-07-26 18:25:57 142.52.143.231 GET /foobar/ - 49 - 70.74.119.161 MZAWZRI/8.8+(ZASNYR+OC+1.4;+HZQ70;+JI:95.7)+DHXUI/41247641+WVTWRQF/88.0 816 8 2 946") "18:25:57"))
(constraint (= (f "2008-06-17 02:25:20 186.183.32.231 GET /foobar/ - 14 - 138.83.171.119 VVUTYHN/6.3+(HVFEBQ+SQ+9.2;+YBX60;+DC:69.4)+DROFQ/03802799+KNHNNBH/48.6 174 9 1 668") "02:25:20"))
(check-synth)
