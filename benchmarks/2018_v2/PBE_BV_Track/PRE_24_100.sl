(set-logic BV)
(define-fun ehad ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000001))
(define-fun arba ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000004))
(define-fun shesh ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000010))
(define-fun smol ((x (_ BitVec 64))) (_ BitVec 64) (bvshl x #x0000000000000001))
(define-fun im ((x (_ BitVec 64)) (y (_ BitVec 64)) (z (_ BitVec 64))) (_ BitVec 64) (ite (= x #x0000000000000001) y z))
(synth-fun f ((x (_ BitVec 64))) (_ BitVec 64)
    ((Start (_ BitVec 64)))
    ((Start (_ BitVec 64) (#x0000000000000000 #x0000000000000001 x (bvnot Start) (smol Start) (ehad Start) (arba Start) (shesh Start) (bvand Start Start) (bvor Start Start) (bvxor Start Start) (bvadd Start Start) (im Start Start Start)))))
(constraint (= (f #x4c4eaca91418ea4e) #x1313ab2a45063a93))
(constraint (= (f #x7c7e3b1e1660c788) #x1f1f8ec7859831e2))
(constraint (= (f #xc6c4b696e3e2e052) #x31b12da5b8f8b814))
(constraint (= (f #x14b61da198c935ed) #x14b61da198c935ed))
(constraint (= (f #x973337db04434c98) #x973337db04434c98))
(constraint (= (f #xc35a456a4461aeb6) #x30d6915a91186bad))
(constraint (= (f #xe60ab1c150825ca1) #x0000000000000001))
(constraint (= (f #xed6300bea5a5144d) #x0000000000000001))
(constraint (= (f #x33111ae35a36e7c6) #x0cc446b8d68db9f1))
(constraint (= (f #x639d5158231dbdd7) #x0000000000000001))
(constraint (= (f #x2db03e8aa55ee9ae) #x0b6c0fa2a957ba6b))
(constraint (= (f #x801a0d2135d57a82) #x801a0d2135d57a82))
(constraint (= (f #x580da4e5cb36b647) #x0000000000000001))
(constraint (= (f #x317cb4cde7d586bd) #x317cb4cde7d586bd))
(constraint (= (f #x8ee26e4c038e6139) #x0000000000000001))
(constraint (= (f #x60957ac420ae05a6) #x0000000000000001))
(constraint (= (f #xb65809d8453b7c29) #x0000000000000001))
(constraint (= (f #xb583760a43c09b75) #xb583760a43c09b75))
(constraint (= (f #xaeedd33a1b64e212) #x2bbb74ce86d93884))
(constraint (= (f #x66279b28ae0946b7) #x0000000000000001))
(constraint (= (f #xe53d4c734695e0e7) #x0000000000000001))
(constraint (= (f #x8c9316dda10d51db) #x0000000000000001))
(constraint (= (f #x86ac84ede6789ce2) #x21ab213b799e2738))
(constraint (= (f #x7e2a38eb6de9298a) #x7e2a38eb6de9298a))
(constraint (= (f #x71b5a879e10e7d2e) #x0000000000000001))
(constraint (= (f #xcddbb8a1279dabe4) #x3376ee2849e76af9))
(constraint (= (f #xb1797138e1953e6e) #x0000000000000001))
(constraint (= (f #x4cb12837469b6a6b) #x0000000000000001))
(constraint (= (f #x29477acc30aaee91) #x0000000000000001))
(constraint (= (f #xe354ad328c3b303e) #x0000000000000001))
(constraint (= (f #xc9cea6c98be88558) #x3273a9b262fa2156))
(constraint (= (f #x6a71de1a29ee160b) #x6a71de1a29ee160b))
(constraint (= (f #x8e70e155ed9e57b7) #x0000000000000001))
(constraint (= (f #xcb11e046e290cbee) #x32c47811b8a432fb))
(constraint (= (f #x702288c77e431414) #x702288c77e431414))
(constraint (= (f #xc516582be17ed84a) #x3145960af85fb612))
(constraint (= (f #x92eec490c2270226) #x0000000000000001))
(constraint (= (f #xea7b64374b7aa1d5) #xea7b64374b7aa1d5))
(constraint (= (f #x3ec596e5eee38c87) #x3ec596e5eee38c87))
(constraint (= (f #x685e052c05a43717) #x0000000000000001))
(constraint (= (f #xa04ac5bacd4ed06c) #x2812b16eb353b41b))
(constraint (= (f #x601107aeba7016de) #x601107aeba7016de))
(constraint (= (f #x5a562dcdbbbbd573) #x0000000000000001))
(constraint (= (f #xdc26ec9119bd9792) #x3709bb24466f65e4))
(constraint (= (f #x4129e9ebd06290b8) #x104a7a7af418a42e))
(constraint (= (f #x5bebcdc3544672ce) #x5bebcdc3544672ce))
(constraint (= (f #xe5b381e4b939e40a) #x396ce0792e4e7902))
(constraint (= (f #x5d500d4ab3ba0805) #x0000000000000001))
(constraint (= (f #x83ee16e12b01592a) #x0000000000000001))
(constraint (= (f #x809e520e21052210) #x0000000000000001))
(constraint (= (f #xea660e1c119ecc0a) #x3a9983870467b302))
(constraint (= (f #x22ad2e7c34b4a4ed) #x0000000000000001))
(constraint (= (f #x5d7ec609e35ede62) #x175fb18278d7b798))
(constraint (= (f #xb89930e32954479e) #xb89930e32954479e))
(constraint (= (f #x131ecbac21eaedbe) #x04c7b2eb087abb6f))
(constraint (= (f #x0b63dd9a3e46ccb7) #x0b63dd9a3e46ccb7))
(constraint (= (f #xc28b01edb5e8e70e) #x30a2c07b6d7a39c3))
(constraint (= (f #x08645bce4dd0ae65) #x08645bce4dd0ae65))
(constraint (= (f #xeeb452bcd0ed10b5) #xeeb452bcd0ed10b5))
(constraint (= (f #xce637e048875b4bb) #xce637e048875b4bb))
(constraint (= (f #x0e9dbd324d9aeed3) #x0000000000000001))
(constraint (= (f #x68194e4a5250ec00) #x1a06539294943b00))
(constraint (= (f #x402c4ee8eaa0b512) #x100b13ba3aa82d44))
(constraint (= (f #x6a6695ebdaa1b70b) #x0000000000000001))
(constraint (= (f #x250268362c85e374) #x09409a0d8b2178dd))
(constraint (= (f #x731d6e72bdae1325) #x0000000000000001))
(constraint (= (f #x0e80278a30a6bd92) #x03a009e28c29af64))
(constraint (= (f #x12e0edb961388e93) #x0000000000000001))
(constraint (= (f #x06aa0d745ac0e607) #x06aa0d745ac0e607))
(constraint (= (f #xbd1180c142939894) #x2f44603050a4e625))
(constraint (= (f #x86c98109ee4e4303) #x86c98109ee4e4303))
(constraint (= (f #x3d685706ee022189) #x0000000000000001))
(constraint (= (f #xa14752042deb28ca) #xa14752042deb28ca))
(constraint (= (f #xe40b62d5ace2889c) #x3902d8b56b38a227))
(constraint (= (f #x110679e96aec532e) #x110679e96aec532e))
(constraint (= (f #xb0a416e45019c9b2) #x2c2905b91406726c))
(constraint (= (f #xe8d33ae95a7e8ae1) #xe8d33ae95a7e8ae1))
(constraint (= (f #x7d422746ea55eaaa) #x1f5089d1ba957aaa))
(constraint (= (f #xa9cec9ee6717c206) #x2a73b27b99c5f081))
(constraint (= (f #x2bce26e5a005e15e) #x0af389b968017857))
(constraint (= (f #xa034736305586b2e) #xa034736305586b2e))
(constraint (= (f #x301db65db84999e9) #x301db65db84999e9))
(constraint (= (f #x06625a47d442789e) #x06625a47d442789e))
(constraint (= (f #xec700c37337825d3) #xec700c37337825d3))
(constraint (= (f #x6ceda1eebca4185a) #x0000000000000001))
(constraint (= (f #x68e291805a802d84) #x0000000000000001))
(constraint (= (f #xe9048e39bab00e50) #x0000000000000001))
(constraint (= (f #xe74c43b743b407ab) #x0000000000000001))
(constraint (= (f #x1aedd5eaace039e0) #x1aedd5eaace039e0))
(constraint (= (f #x65584c325348527c) #x65584c325348527c))
(constraint (= (f #x6ed8e86c3ed74a37) #x6ed8e86c3ed74a37))
(constraint (= (f #x6c146997c47babd5) #x6c146997c47babd5))
(constraint (= (f #x7acb27a8de8a6db6) #x0000000000000001))
(constraint (= (f #x30bb80e5e7e2d13c) #x0c2ee03979f8b44f))
(constraint (= (f #x6505ecb83965eb5a) #x19417b2e0e597ad6))
(constraint (= (f #x3bc41b5803e0eac2) #x0ef106d600f83ab0))
(constraint (= (f #xc0a42d9ee13dc018) #x30290b67b84f7006))
(constraint (= (f #xd65c120417ceaeaa) #x3597048105f3abaa))
(constraint (= (f #x6382c6c834374c01) #x0000000000000001))
(constraint (= (f #x9466be4aa5c0e4a1) #x9466be4aa5c0e4a1))
(check-synth)
