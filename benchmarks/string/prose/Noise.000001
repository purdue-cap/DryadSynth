
    (set-logic SLIA)
    (synth-fun f ((arg0 String) (arg1 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("#" "*" "^" "%" "(" ")" "!" "$" "@" arg0 arg1
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
(constraint (= (f "#uhu*q kkrxr b^ z" "%^ bzt(z") "#uhu*q kkrxr b^ z%^ bzt(z"))
(constraint (= (f "gqk" "x#bom)s!ek*$mqfip!ws") "gqkx#bom)s!ek*$mqfip!ws"))
(constraint (= (f "%@^zxi**d^gx%i ry!" "d!!e*grumyr%$$#(k") "%@^zxi**d^gx%i ry!d!!e*grumyr%$$#(k"))
(constraint (= (f "mk%igcxzx(i(c*" "dfdw)sfzviwan^c") "mk%igcxzx(i(c*dfdw)sfzviwan^c"))
(constraint (= (f ") otkstcks^wd@o" "ezw%zvggwj vcj^vxt)") ") otkstcks^wd@oezw%zvggwj vcj^vxt)"))
(check-synth)
