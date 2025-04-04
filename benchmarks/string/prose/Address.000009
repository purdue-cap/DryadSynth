
    (set-logic SLIA)
    (synth-fun f ((arg0 String) (arg1 String)) String 
        (
            (Start String (ntString)) 
            (ntString String (";" "case" ": return" "case 5" "case 8" arg0 arg1
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
(constraint (= (f "Caribbean" "359") "case 359: return Caribbean;"))
(constraint (= (f "India" "550") "case 550: return India;"))
(constraint (= (f "Equatorial Guinea" "967") "case 967: return Equatorial Guinea;"))
(constraint (= (f "Botswana" "713") "case 713: return Botswana;"))
(constraint (= (f "Angola" "581") "case 581: return Angola;"))
(constraint (= (f "Gambia" "955") "case 955: return Gambia;"))
(constraint (= (f "Finland" "879") "case 879: return Finland;"))
(constraint (= (f "South Africa" "827") "case 827: return South Africa;"))
(constraint (= (f "China" "812") "case 812: return China;"))
(constraint (= (f "Afghanistan" "523") "case 523: return Afghanistan;"))
(check-synth)
