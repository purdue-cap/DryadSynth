
    (set-logic SLIA)
    (synth-fun f ((arg0 String)) String 
        (
            (Start String (ntString)) 
            (ntString String ("/" "-" "foo-a" "GUID-" "foo-a4" "/GUID-" "http://example.com/SomePath/another/en/foo-a" "http://example.com/SomePath/another/en/foo-a4/GUID-" "_" "_GUID-" "en_foo-a" "en_foo-a4_GUID-" arg0
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
(constraint (= (f "http://example.com/SomePath/another/en/foo-a4/GUID-8ADE3E75-6C71-4C6A-ACFD-D00D4B7D72E2") "en_foo-a4_GUID-8ADE3E75-6C71-4C6A-ACFD-D00D4B7D72E2"))
(constraint (= (f "http://example.com/SomePath/another/en/foo-a4/GUID-B3D68203-6E71-43CD-ABF1-32156B6848B0") "en_foo-a4_GUID-B3D68203-6E71-43CD-ABF1-32156B6848B0"))
(constraint (= (f "http://example.com/SomePath/another/en/foo-a4/GUID-B9D86D1B-3B9B-4271-B74D-98607EB34A8E") "en_foo-a4_GUID-B9D86D1B-3B9B-4271-B74D-98607EB34A8E"))
(constraint (= (f "http://example.com/SomePath/another/en/foo-a2/GUID-F2E27132-5CF4-4739-A6B7-0DDAEA7D1FDB") "en_foo-a2_GUID-F2E27132-5CF4-4739-A6B7-0DDAEA7D1FDB"))
(constraint (= (f "http://example.com/SomePath/another/en/foo-a4/GUID-754D9B48-D35B-4F57-B164-54426E6F4640") "en_foo-a4_GUID-754D9B48-D35B-4F57-B164-54426E6F4640"))
(constraint (= (f "http://example.com/SomePath/another/en/foo-a4/GUID-98FAB309-BEA9-40E9-A0AA-FC7CD8C2AA70") "en_foo-a4_GUID-98FAB309-BEA9-40E9-A0AA-FC7CD8C2AA70"))
(constraint (= (f "http://example.com/SomePath/another/en/foo-a7/GUID-91C51783-B91C-465A-9F8F-41AD22487AE0") "en_foo-a7_GUID-91C51783-B91C-465A-9F8F-41AD22487AE0"))
(constraint (= (f "http://example.com/SomePath/another/en/foo-a4/GUID-0BE43650-E7EB-4A78-8E9D-C3D477073FD9") "en_foo-a4_GUID-0BE43650-E7EB-4A78-8E9D-C3D477073FD9"))
(constraint (= (f "http://example.com/SomePath/another/en/foo-a9/GUID-8DDB2E31-0FCE-4EA9-8A6D-7CE2CB4A051A") "en_foo-a9_GUID-8DDB2E31-0FCE-4EA9-8A6D-7CE2CB4A051A"))
(constraint (= (f "http://example.com/SomePath/another/en/foo-a4/GUID-194CACB9-C2DE-4492-BAC5-054EE8C17BF5") "en_foo-a4_GUID-194CACB9-C2DE-4492-BAC5-054EE8C17BF5"))
(check-synth)
