
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("," "ir" "Air" "an" "Canad" "Canada" "Canadian" "Airlines" arg0
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
(constraint (= (f " Austin Airways, acquired by Air Ontario") "Austin Airways"))
(constraint (= (f " Canada 3000 folded, discount airline") "Canada 3000 folded"))
(constraint (= (f " Canadian Pacific Airlines, acquired by Canadian Airlines") "Canadian Pacific Airlines"))
(constraint (= (f " Canadian Regional Airlines, merged with Air Canada Jazz") "Canadian Regional Airlines"))
(constraint (= (f " Greyhound Air, winnow") "Greyhound Air"))
(check-synth)
