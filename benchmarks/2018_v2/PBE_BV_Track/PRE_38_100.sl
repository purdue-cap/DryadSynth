(set-logic BV)
(define-fun ehad ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000001))
(define-fun arba ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000004))
(define-fun shesh ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000010))
(define-fun smol ((x (_ BitVec 64))) (_ BitVec 64) (bvshl x #x0000000000000001))
(define-fun im ((x (_ BitVec 64)) (y (_ BitVec 64)) (z (_ BitVec 64))) (_ BitVec 64) (ite (= x #x0000000000000001) y z))
(synth-fun f ((x (_ BitVec 64))) (_ BitVec 64)
    ((Start (_ BitVec 64)))
    ((Start (_ BitVec 64) (#x0000000000000000 #x0000000000000001 x (bvnot Start) (smol Start) (ehad Start) (arba Start) (shesh Start) (bvand Start Start) (bvor Start Start) (bvxor Start Start) (bvadd Start Start) (im Start Start Start)))))
(constraint (= (f #x38c43b6215268d02) #x38c43b6215268d04))
(constraint (= (f #x814ed6600843e6a5) #x0000000000000002))
(constraint (= (f #xce22cede97e5ca77) #x0000000000000002))
(constraint (= (f #xedee4ee087094e64) #x0000edee4ee08709))
(constraint (= (f #xd9371bb2ee9351ee) #x0000d9371bb2ee93))
(constraint (= (f #xdd1318e67cbc669e) #xdd1318e67cbc66a0))
(constraint (= (f #xa430c14810c33a16) #x0000a430c14810c3))
(constraint (= (f #xe59e8d1d797d6076) #x0000e59e8d1d797d))
(constraint (= (f #x33d9d594b88e6c5e) #x33d9d594b88e6c60))
(constraint (= (f #x77e8a576c72cbced) #x000077e8a576c72c))
(constraint (= (f #xa87aaad2e0ce7231) #x0000a87aaad2e0ce))
(constraint (= (f #xe3e8ee35e3217cdc) #x0000e3e8ee35e321))
(constraint (= (f #x801894e478ee19bd) #x0000801894e478ee))
(constraint (= (f #xa469302911e46644) #xa469302911e46646))
(constraint (= (f #xe47775355c198be5) #x0000000000000002))
(constraint (= (f #xb35e102e2c638ebd) #x0000000000000002))
(constraint (= (f #x1e123449dce0e6d5) #x00001e123449dce0))
(constraint (= (f #xa6340a2bb6e37cd5) #x0000000000000002))
(constraint (= (f #xd92da916a7727ec2) #xd92da916a7727ec4))
(constraint (= (f #x560e6ccab8e8e442) #x560e6ccab8e8e444))
(constraint (= (f #x287d03d6d1554190) #x0000287d03d6d155))
(constraint (= (f #x2089c59d213c9c40) #x2089c59d213c9c42))
(constraint (= (f #x6d7353202297ce2c) #x00006d7353202297))
(constraint (= (f #x9e2195de6c6e6e5c) #x9e2195de6c6e6e5e))
(constraint (= (f #xe83d90ee0be3222b) #x0000000000000002))
(constraint (= (f #xea7952e97a7a5008) #xea7952e97a7a500a))
(constraint (= (f #x24e06526112b54a3) #x0000000000000002))
(constraint (= (f #xde04b2a53e04a21d) #x0000de04b2a53e04))
(constraint (= (f #xedb9648cb2776b65) #x0000000000000002))
(constraint (= (f #xcec460807e66d6b4) #xcec460807e66d6b6))
(constraint (= (f #x1d9181e8cdd79e54) #x00001d9181e8cdd7))
(constraint (= (f #x50d7363a1e032502) #x000050d7363a1e03))
(constraint (= (f #x4e00773a2ea57b08) #x00004e00773a2ea5))
(constraint (= (f #x75495638e79bc86a) #x000075495638e79b))
(constraint (= (f #x29bbe45285058d71) #x0000000000000002))
(constraint (= (f #x77bc8ca84007e576) #x000077bc8ca84007))
(constraint (= (f #x41516a20ac5dc02e) #x000041516a20ac5d))
(constraint (= (f #xd543bebe83bee8a1) #x0000d543bebe83be))
(constraint (= (f #xce62e365656a6c97) #x0000ce62e365656a))
(constraint (= (f #x350bed8c54d8e643) #x0000350bed8c54d8))
(constraint (= (f #x95ba3695c91a551e) #x95ba3695c91a5520))
(constraint (= (f #xea09de4135ee29ce) #xea09de4135ee29d0))
(constraint (= (f #x19044e08743a7b9e) #x19044e08743a7ba0))
(constraint (= (f #xde5229523479985e) #x0000de5229523479))
(constraint (= (f #x867174a23b1b18c9) #x0000000000000002))
(constraint (= (f #xa9e08774c9e8e5e7) #x0000a9e08774c9e8))
(constraint (= (f #xbe3125b8c7b269ae) #xbe3125b8c7b269b0))
(constraint (= (f #xd9314e3581d37ba1) #x0000000000000002))
(constraint (= (f #xe04222cb3eb59840) #x0000e04222cb3eb5))
(constraint (= (f #x0e58e5c47e8d0582) #x00000e58e5c47e8d))
(constraint (= (f #xee2a05b97b5b71ab) #x0000000000000002))
(constraint (= (f #x25bade45c6e18ae9) #x0000000000000002))
(constraint (= (f #xe88e05ad74e24ebc) #xe88e05ad74e24ebe))
(constraint (= (f #xeee21cb7e173c1e3) #x0000000000000002))
(constraint (= (f #xc398e0ea72e60213) #x0000c398e0ea72e6))
(constraint (= (f #x32d792d2590d8632) #x000032d792d2590d))
(constraint (= (f #xe6184e6219d40839) #x0000e6184e6219d4))
(constraint (= (f #xed16dbeae54de145) #x0000000000000002))
(constraint (= (f #x17d5d653e5511cae) #x000017d5d653e551))
(constraint (= (f #x48cdc9a8970143b9) #x0000000000000002))
(constraint (= (f #x8c36c14bb5013b0a) #x00008c36c14bb501))
(constraint (= (f #x6ba1c027806d2029) #x0000000000000002))
(constraint (= (f #xd9e15ce3d928a675) #x0000d9e15ce3d928))
(constraint (= (f #xd5011dd35c256317) #x0000000000000002))
(constraint (= (f #xa3d0a487dbe2dd92) #xa3d0a487dbe2dd94))
(constraint (= (f #xbebdb1e62c9e05bb) #x0000bebdb1e62c9e))
(constraint (= (f #xa1e7d80e7e54590e) #xa1e7d80e7e545910))
(constraint (= (f #xe62e767c00a349be) #x0000e62e767c00a3))
(constraint (= (f #x21374523e15a4ee8) #x21374523e15a4eea))
(constraint (= (f #x5a51413edbe47821) #x00005a51413edbe4))
(constraint (= (f #x69447422b7e4e256) #x69447422b7e4e258))
(constraint (= (f #x96172ebc2e1bdece) #x000096172ebc2e1b))
(constraint (= (f #xd7300285310eb6a5) #x0000d7300285310e))
(constraint (= (f #xacb1acb0b1a7b45e) #x0000acb1acb0b1a7))
(constraint (= (f #x11b3ee631ad65a3b) #x000011b3ee631ad6))
(constraint (= (f #xecbb4ecc5cbe7593) #x0000ecbb4ecc5cbe))
(constraint (= (f #xc66c3556ce9117ee) #x0000c66c3556ce91))
(constraint (= (f #x20b728ae6629b603) #x0000000000000002))
(constraint (= (f #xecb37287489bbe38) #x0000ecb37287489b))
(constraint (= (f #x6ce7113403ad72c5) #x0000000000000002))
(constraint (= (f #x6debe07992108876) #x6debe07992108878))
(constraint (= (f #xe8085e2206c3d9a1) #x0000000000000002))
(constraint (= (f #xec976520dcc8a57c) #xec976520dcc8a57e))
(constraint (= (f #x1e16c4e2e71c2029) #x00001e16c4e2e71c))
(constraint (= (f #x268e470205ce50ed) #x0000268e470205ce))
(constraint (= (f #x7bdd9e252e7935e6) #x00007bdd9e252e79))
(constraint (= (f #x21c9e7a2885437c2) #x21c9e7a2885437c4))
(constraint (= (f #x5c28aede7640826d) #x00005c28aede7640))
(constraint (= (f #x33e376e1cac50a4d) #x0000000000000002))
(constraint (= (f #x0e22b5c30ee02829) #x00000e22b5c30ee0))
(constraint (= (f #x66e2985e34d99cd9) #x0000000000000002))
(constraint (= (f #xed4de2c16bb707c6) #x0000ed4de2c16bb7))
(constraint (= (f #xd89b356c52edde86) #x0000d89b356c52ed))
(constraint (= (f #xceac68635d736eab) #x0000000000000002))
(constraint (= (f #xec96a5bec78eb24b) #x0000ec96a5bec78e))
(constraint (= (f #x014a4ec87c73c10c) #x0000014a4ec87c73))
(constraint (= (f #xb624503e9b0450ae) #xb624503e9b0450b0))
(constraint (= (f #xb8ac88ba394c3de9) #x0000b8ac88ba394c))
(constraint (= (f #x81a9ca908dde9952) #x81a9ca908dde9954))
(constraint (= (f #x526db2b03c2a8836) #x526db2b03c2a8838))
(check-synth)
