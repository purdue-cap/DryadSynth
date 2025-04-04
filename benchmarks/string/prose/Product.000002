
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("+" "&" "Concrete" "-" arg0
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
(constraint (= (f "692 Practi+al Concrete Chips & 275 186, 531gm") "531gm"))
(constraint (= (f "Incredible Concrete Keyboard Small Rubber Mouse & In+elligent Rubber Fish - Unbranded Granite Mouse - 502g") "502g"))
(constraint (= (f "267 & 869 Handcrafted Granite Ball 562gm") "N/A"))
(constraint (= (f "Rustic Steel Mo?se - 322 - 701ml") "701ml"))
(constraint (= (f "Licensed Fresh Sausages & 384 - Rustic Concrete +ar - 954 g") "954 g"))
(constraint (= (f "Intell+gent Fresh Towels Intelligent Concrete Fi?h 235 Handcrafted Metal Chair - 146Ml") "146Ml"))
(constraint (= (f "Handcrafted Fresh Fish & 324, 457ml") "457ml"))
(constraint (= (f "742 & Sleek Steel Ball - Handmade Frozen Chair - 515 g") "515 g"))
(constraint (= (f "Handmade Soft Salad & Awesome Plastic+Mouse - Licensed Granite Sausages - 586g") "586g"))
(constraint (= (f "548 - Intelligent Soft Ball & 407 - 497Ml") "497Ml"))
(check-synth)
