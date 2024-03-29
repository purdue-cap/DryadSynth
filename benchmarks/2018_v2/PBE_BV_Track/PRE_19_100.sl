(set-logic BV)
(define-fun ehad ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000001))
(define-fun arba ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000004))
(define-fun shesh ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000010))
(define-fun smol ((x (_ BitVec 64))) (_ BitVec 64) (bvshl x #x0000000000000001))
(define-fun im ((x (_ BitVec 64)) (y (_ BitVec 64)) (z (_ BitVec 64))) (_ BitVec 64) (ite (= x #x0000000000000001) y z))
(synth-fun f ((x (_ BitVec 64))) (_ BitVec 64)
    ((Start (_ BitVec 64)))
    ((Start (_ BitVec 64) (#x0000000000000000 #x0000000000000001 x (bvnot Start) (smol Start) (ehad Start) (arba Start) (shesh Start) (bvand Start Start) (bvor Start Start) (bvxor Start Start) (bvadd Start Start) (im Start Start Start)))))
(constraint (= (f #x6bab76d7d0eaee98) #x6bab76d7d0eaee9a))
(constraint (= (f #xee71a2508121a3c7) #x0ee71a2508121a3d))
(constraint (= (f #x80e3e67489e5b37e) #x80e3e67489e5b380))
(constraint (= (f #x47a1605419ce4ae7) #x047a1605419ce4af))
(constraint (= (f #x26705d766518d15d) #x026705d766518d15))
(constraint (= (f #xd7147992440cbc4e) #xd7147992440cbc50))
(constraint (= (f #x822c3cc8ee7a3764) #x822c3cc8ee7a3766))
(constraint (= (f #x5eb663e47c88b845) #x05eb663e47c88b85))
(constraint (= (f #x789e4ac7ee04da88) #x789e4ac7ee04da8a))
(constraint (= (f #x34664124ae5bce25) #x034664124ae5bce3))
(constraint (= (f #xb6e20eed7203a02a) #xb6e20eed7203a02c))
(constraint (= (f #x693ea86a50e7428d) #x0693ea86a50e7429))
(constraint (= (f #x8a3b3ae34b352132) #x8a3b3ae34b352134))
(constraint (= (f #xe27e2b9547a25297) #x0e27e2b9547a2529))
(constraint (= (f #x1c8993249c422b5c) #x1c8993249c422b5e))
(constraint (= (f #xab4e762d87eeaecc) #xab4e762d87eeaece))
(constraint (= (f #x75e815e5222ee5e6) #x75e815e5222ee5e8))
(constraint (= (f #x1b667b08796b69e9) #x01b667b08796b69f))
(constraint (= (f #xe4eea4e0918ea8bd) #x0e4eea4e0918ea8b))
(constraint (= (f #x54b6e2cc9cea98ab) #x054b6e2cc9cea98b))
(constraint (= (f #x430eee8e6dcd185e) #x430eee8e6dcd1860))
(constraint (= (f #x0e183b69648b59cd) #x00e183b69648b59d))
(constraint (= (f #xd44a8838975e6549) #x0d44a8838975e655))
(constraint (= (f #xae06345145b2e04d) #x0ae06345145b2e05))
(constraint (= (f #xa3e5757e37dc9bed) #x0a3e5757e37dc9bf))
(constraint (= (f #x530ea641824d38ea) #x530ea641824d38ec))
(constraint (= (f #x8c244e84b9e630de) #x8c244e84b9e630e0))
(constraint (= (f #x1367c37e2e6e4811) #x01367c37e2e6e481))
(constraint (= (f #xd8deec09a06abd97) #x0d8deec09a06abd9))
(constraint (= (f #xcead6bb6010988ec) #xcead6bb6010988ee))
(constraint (= (f #xee98807326c7d598) #xee98807326c7d59a))
(constraint (= (f #xd29e90e456e65abe) #xd29e90e456e65ac0))
(constraint (= (f #xcd62c672ac372c05) #x0cd62c672ac372c1))
(constraint (= (f #x6e53182e17090aea) #x6e53182e17090aec))
(constraint (= (f #xe606ea68eed1122d) #x0e606ea68eed1123))
(constraint (= (f #x2e3e87652599e248) #x2e3e87652599e24a))
(constraint (= (f #x9152a3954ea9623e) #x9152a3954ea96240))
(constraint (= (f #xec8cbceb1e5431cc) #xec8cbceb1e5431ce))
(constraint (= (f #x128ba8b3cc23e8e9) #x0128ba8b3cc23e8f))
(constraint (= (f #x9746eabb7e6d4710) #x9746eabb7e6d4712))
(constraint (= (f #x33d3d1766ed2c314) #x33d3d1766ed2c316))
(constraint (= (f #xb3cdecec2bad2aa5) #x0b3cdecec2bad2ab))
(constraint (= (f #x83abe96697cba498) #x83abe96697cba49a))
(constraint (= (f #x822edc8884be1a7e) #x822edc8884be1a80))
(constraint (= (f #x4116355ce1b9da39) #x04116355ce1b9da3))
(constraint (= (f #xa7a9e6704d334939) #x0a7a9e6704d33493))
(constraint (= (f #xb73b103854160ca4) #xb73b103854160ca6))
(constraint (= (f #x9d81e8993c08a839) #x09d81e8993c08a83))
(constraint (= (f #xb016e18618b76913) #x0b016e18618b7691))
(constraint (= (f #x1d2c18d2749ed90e) #x1d2c18d2749ed910))
(constraint (= (f #xecb1e85e8477a671) #x0ecb1e85e8477a67))
(constraint (= (f #xb6aa2840d10322ed) #x0b6aa2840d10322f))
(constraint (= (f #xb8054a56e8949759) #x0b8054a56e894975))
(constraint (= (f #xad81489961d12b23) #x0ad81489961d12b3))
(constraint (= (f #x176d85dad2ee52e6) #x176d85dad2ee52e8))
(constraint (= (f #xe926a221c3ca2e67) #x0e926a221c3ca2e7))
(constraint (= (f #x08d36b0c3007a0b9) #x008d36b0c3007a0b))
(constraint (= (f #x1a627dcc3c1eb2e3) #x01a627dcc3c1eb2f))
(constraint (= (f #xc15c26bbe7abc6b7) #x0c15c26bbe7abc6b))
(constraint (= (f #x878ea1b15dd6518d) #x0878ea1b15dd6519))
(constraint (= (f #xe8e81e3db74e027e) #xe8e81e3db74e0280))
(constraint (= (f #x1bdaea4b8e2e9706) #x1bdaea4b8e2e9708))
(constraint (= (f #xae7454bb4e5de07e) #xae7454bb4e5de080))
(constraint (= (f #x26013e47bea8d68e) #x26013e47bea8d690))
(constraint (= (f #x17e7990c07298496) #x17e7990c07298498))
(constraint (= (f #x35d3559e57d80ad8) #x35d3559e57d80ada))
(constraint (= (f #x11ce2d34494ea415) #x011ce2d34494ea41))
(constraint (= (f #xe39592b3ee77b97e) #xe39592b3ee77b980))
(constraint (= (f #x55011b181521103e) #x55011b1815211040))
(constraint (= (f #xd6e1ed14be0eedb7) #x0d6e1ed14be0eedb))
(constraint (= (f #x3bee83c1a87eaec7) #x03bee83c1a87eaed))
(constraint (= (f #x4447ae1ee81e9134) #x4447ae1ee81e9136))
(constraint (= (f #x1c65b894248b39d2) #x1c65b894248b39d4))
(constraint (= (f #xa0d4b1c71e8a7188) #xa0d4b1c71e8a718a))
(constraint (= (f #xea2dd616554c80a6) #xea2dd616554c80a8))
(constraint (= (f #x3eea62bc04da086a) #x3eea62bc04da086c))
(constraint (= (f #x3256db1405298437) #x03256db140529843))
(constraint (= (f #xeeaeab1e8199d035) #x0eeaeab1e8199d03))
(constraint (= (f #xa85c1b2e8a9e99e2) #xa85c1b2e8a9e99e4))
(constraint (= (f #xe85922195cc81ad0) #xe85922195cc81ad2))
(constraint (= (f #xc40e59dee288965d) #x0c40e59dee288965))
(constraint (= (f #x168e8ee6ae0d2128) #x168e8ee6ae0d212a))
(constraint (= (f #x09e7386289e4aac4) #x09e7386289e4aac6))
(constraint (= (f #x9ee30e25b0e87947) #x09ee30e25b0e8795))
(constraint (= (f #x9daeede93d2075de) #x9daeede93d2075e0))
(constraint (= (f #x3425bc2829387184) #x3425bc2829387186))
(constraint (= (f #xeb7c317984dab495) #x0eb7c317984dab49))
(constraint (= (f #x8bad322e09b58c17) #x08bad322e09b58c1))
(constraint (= (f #x56e8d848161eda13) #x056e8d848161eda1))
(constraint (= (f #x0eaeb0777c4b6507) #x00eaeb0777c4b651))
(constraint (= (f #xc57e45463239e4ee) #xc57e45463239e4f0))
(constraint (= (f #x1d5aa7cb43728c42) #x1d5aa7cb43728c44))
(constraint (= (f #x03a38b68c163074e) #x03a38b68c1630750))
(constraint (= (f #xe89382ec19e16431) #x0e89382ec19e1643))
(constraint (= (f #x7a1593408b7b9708) #x7a1593408b7b970a))
(constraint (= (f #x62a6e8ab01e19d11) #x062a6e8ab01e19d1))
(constraint (= (f #xba14143ed163246b) #x0ba14143ed163247))
(constraint (= (f #x078487dc2b66a2c8) #x078487dc2b66a2ca))
(constraint (= (f #x6aeadb610deb7e8b) #x06aeadb610deb7e9))
(constraint (= (f #x72188003544dbece) #x72188003544dbed0))
(check-synth)
