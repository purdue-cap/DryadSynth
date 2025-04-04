
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("[CPT-" "[CPT-003" "]" "[CPT-00340]" "[CPT-11" "[CPT-115" "[CPT-11536" "[CPT-11536]" "[CPT-11222]" "[CPT-115]" arg0
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
(constraint (= (f "[CPT-00350") "[CPT-00350]"))
(constraint (= (f "[CPT-00340") "[CPT-00340]"))
(constraint (= (f "[CPT-11536") "[CPT-11536]"))
(constraint (= (f "[CPT-11222") "[CPT-11222]"))
(constraint (= (f "[CPT-115]") "[CPT-115]"))
(constraint (= (f "[CPT-1153622]") "[CPT-1153622]"))
(constraint (= (f "[CPT-00340]") "[CPT-00340]"))
(constraint (= (f "[CPT-11536]") "[CPT-11536]"))
(constraint (= (f "[CPT-11222]") "[CPT-11222]"))
(constraint (= (f "[CPT-115") "[CPT-115]"))
(constraint (= (f "[CPT-1163") "[CPT-1163]"))
(check-synth)
