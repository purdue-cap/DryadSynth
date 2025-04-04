
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("@" "." ".com" "@fabrikam.com" arg0
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
(constraint (= (f "assumenda iste quia natus et @ humongousinsurance.com ad nostrum totam harum totam") ""))
(constraint (= (f "eos @ woodgrovebank.com ipsam illum pariatur doloribus") ""))
(constraint (= (f "non recusandae est unde l@humongousinsurance.com doloribus facilis") "l@humongousinsurance.com"))
(constraint (= (f "vel @ firstupconsultants.com quas porro sed ad et") ""))
(constraint (= (f "blanditiis ratione @ contososuites.com porro dolorum corrupti") ""))
(constraint (= (f "dolorem id vero soluta fuga 33f@treyresearch.net facilis") "33f@treyresearch.net"))
(constraint (= (f "repellat deleniti @ northwindtraders.com consequatur aut qui porro") ""))
(constraint (= (f "consectetur voluptatum ut voluptas tempore zyxv6@fabrikam.com nam sit quis") "zyxv6@fabrikam.com"))
(constraint (= (f "amet quasi @ nodpublishers.com et molestiae modi non") ""))
(constraint (= (f "cum explicabo voluptatem odit est ejqjnmaw0d@fabrikam.com a doloribus iure cupiditate") "ejqjnmaw0d@fabrikam.com"))
(check-synth)
