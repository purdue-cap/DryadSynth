(set-logic BV)
(define-fun ehad ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000001))
(define-fun arba ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000004))
(define-fun shesh ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000010))
(define-fun smol ((x (_ BitVec 64))) (_ BitVec 64) (bvshl x #x0000000000000001))
(define-fun im ((x (_ BitVec 64)) (y (_ BitVec 64)) (z (_ BitVec 64))) (_ BitVec 64) (ite (= x #x0000000000000001) y z))
(synth-fun f ((x (_ BitVec 64))) (_ BitVec 64)
    ((Start (_ BitVec 64)))
    ((Start (_ BitVec 64) (#x0000000000000000 #x0000000000000001 x (bvnot Start) (smol Start) (ehad Start) (arba Start) (shesh Start) (bvand Start Start) (bvor Start Start) (bvxor Start Start) (bvadd Start Start) (im Start Start Start)))))
(constraint (= (f #xc7530d614eade656) #x0000c7530d614ead))
(constraint (= (f #x1e3cd9518252a3eb) #x00000000e0c28800))
(constraint (= (f #xb1b29d0633c41b27) #x0000000584802010))
(constraint (= (f #x4e01376eb0546e3e) #x0000000000093100))
(constraint (= (f #x313a52ebad9b4297) #x000029a77b9e7b56))
(constraint (= (f #x8ba015d8231568cb) #x0000ce701f34329f))
(constraint (= (f #x654b6c6726443b08) #x000000020a432130))
(constraint (= (f #x330ee3ed7a5a9e5e) #x0000000010170b42))
(constraint (= (f #xb169b0a468eae4eb) #x0000000109050103))
(constraint (= (f #x7200539320318e02) #x0000720053932031))
(constraint (= (f #x3035bece814e915b) #x0000000181a47400))
(constraint (= (f #xa9e3790ceec4ebeb) #x000000050b084066))
(constraint (= (f #x2918125b9cea038c) #x00000000408090c4))
(constraint (= (f #xa8ae6bda4db25ae8) #x0000000541525240))
(constraint (= (f #xdce0780011ce9ea9) #x0000000603000000))
(constraint (= (f #x4cee4b753261e779) #x00006a996ecfab51))
(constraint (= (f #x55b29c39ae44e33d) #x000000008480c140))
(constraint (= (f #x09ebde3468c49345) #x000000004e50a102))
(constraint (= (f #x5aa200a10391d510) #x00005aa200a10391))
(constraint (= (f #xdea89131a2e44670) #x0000000444008905))
(constraint (= (f #x210e4d123ba0298e) #x0000000000600091))
(constraint (= (f #x079e43e09ea0639b) #x0000000030120404))
(constraint (= (f #x2c3e1ed2d15e0348) #x0000000160f09682))
(constraint (= (f #x0c779b610a5b2310) #x00000c779b610a5b))
(constraint (= (f #x958bb599b3ac06b5) #x000000040c0c8c8d))
(constraint (= (f #x2cee1dc0eb7e3008) #x0000000160600603))
(constraint (= (f #x077a1432b55b72a5) #x000004c71e2beff6))
(constraint (= (f #x785b7dd851e9b0a8) #x0000785b7dd851e9))
(constraint (= (f #xebb2b2233ec6920c) #x0000000515911110))
(constraint (= (f #x7a4541746431e063) #x00004767e1ce5629))
(constraint (= (f #xd51a356d1c6eb118) #x0000000080812860))
(constraint (= (f #x25432eeeae8ecbcd) #x0000000008117574))
(constraint (= (f #xb27ed8986704a1bc) #x0000000192c4c000))
(constraint (= (f #x87953e3200777e8b) #x0000c45fa12b004c))
(constraint (= (f #x5a31ceae697e6162) #x0000000080047143))
(constraint (= (f #xe064865512c741b3) #x00009056c57f9ba4))
(constraint (= (f #x1e9e7eb4821ee14a) #x00000000f0f1a400))
(constraint (= (f #x05db26ed736cea76) #x000000000811230b))
(constraint (= (f #x261e98ee06e8ee23) #x0000000030c44030))
(constraint (= (f #xeb7e5de9ccb4c4b7) #x0000000352e24e44))
(constraint (= (f #x41728ea13a8089b9) #x0000000200140100))
(constraint (= (f #x3bbdda8cc3bc0160) #x00000001ccc44404))
(constraint (= (f #x181121d5abe27e7e) #x0000000080080c0d))
(constraint (= (f #x7deda607a29d730b) #x0000431b750473d3))
(constraint (= (f #xae19eeeb780ebdc9) #x0000000040475340))
(constraint (= (f #xc66b67bb845abebb) #x0000000213191c00))
(constraint (= (f #xd39d50971c9beee6) #x0000d39d50971c9b))
(constraint (= (f #x1a5d828eb507543e) #x00001a5d828eb507))
(constraint (= (f #xee6560e371061eda) #x0000000323030308))
(constraint (= (f #xa4d872959ed2002b) #x00000004028084a4))
(constraint (= (f #xd59a08dae481cae9) #x0000bf570cb796c1))
(constraint (= (f #xab157328c8620588) #x0000000008890042))
(constraint (= (f #xbc64b9da8de3aea9) #x0000e256e537cb12))
(constraint (= (f #x0e85be7703eb14d5) #x000009c7614c821e))
(constraint (= (f #x04c1b82a8e207e5e) #x0000000004014050))
(constraint (= (f #x283a69c0e121e138) #x0000283a69c0e121))
(constraint (= (f #x713b4c21d2633156) #x0000713b4c21d263))
(constraint (= (f #x7ec94036110c74b4) #x0000000242000080))
(constraint (= (f #xccaeabe386e401ad) #x0000000465551c14))
(constraint (= (f #xe87adb7961c6b4ee) #x0000000342d2cb0a))
(constraint (= (f #x9b8ee4e94d17ba2e) #x00009b8ee4e94d17))
(constraint (= (f #xbec5e1e795741762) #x00000004260f0c28))
(constraint (= (f #x6202bbaaa8214a21) #x00005303e67ffc31))
(constraint (= (f #xd43d640eceec422e) #x00000000a1202076))
(constraint (= (f #xb72e261ceb3eba29) #x0000000131302041))
(constraint (= (f #x9a3bb3a5e7b31980) #x00009a3bb3a5e7b3))
(constraint (= (f #x49a5e232a29e73b2) #x000000000d011114))
(constraint (= (f #x8659582c91235047) #x0000c575f43ad9b2))
(constraint (= (f #x51bb2c5a367e81e6) #x0000000089404091))
(constraint (= (f #x2b4013098be19b5a) #x00002b4013098be1))
(constraint (= (f #x1d48d21389c6752a) #x000000004200900c))
(constraint (= (f #x1c71554a78e868d7) #x00000000828a0243))
(constraint (= (f #xee4958dd59c00e1b) #x000000024242c2ca))
(constraint (= (f #x990b0ae6893570ee) #x0000990b0ae68935))
(constraint (= (f #xe1a24779e2e46e7d) #x0000000500120b07))
(constraint (= (f #xe1b312e36c393bec) #x0000e1b312e36c39))
(constraint (= (f #xa2ec4a05c0db5a9a) #x0000a2ec4a05c0db))
(constraint (= (f #xb348b498559537b4) #x0000b348b4985595))
(constraint (= (f #xb4710a29a8aa8c20) #x0000000180004145))
(constraint (= (f #x0ee32e6c911aa78c) #x0000000011116000))
(constraint (= (f #x188b3ed1397881d2) #x0000000040508089))
(constraint (= (f #x10d1ae6b08696c34) #x000010d1ae6b0869))
(constraint (= (f #x6a9267348daeb701) #x0000000010112024))
(constraint (= (f #xd6c49ce47bd5d1e5) #x0000bda6d296463f))
(constraint (= (f #x586e839a277b0c7b) #x00007459c25734c6))
(constraint (= (f #xea466701cb8e02b7) #x000000021230080c))
(constraint (= (f #x66ae278e22cbaec9) #x000055f9344933ae))
(constraint (= (f #x94d7310ea1dd93e2) #x000094d7310ea1dd))
(constraint (= (f #xbe34ee13e1b32e9c) #x0000be34ee13e1b3))
(constraint (= (f #x385e8aad2e50c6ce) #x00000000c0544160))
(constraint (= (f #x9a565ac64325a080) #x00009a565ac64325))
(constraint (= (f #xee289ed65039dcae) #x0000ee289ed65039))
(constraint (= (f #xee519e803320c68e) #x0000000200840001))
(constraint (= (f #xbede5a4c54e0ea1c) #x00000004f2d24222))
(constraint (= (f #x5231483ee5d56aec) #x00005231483ee5d5))
(constraint (= (f #x2a67e56de30921ed) #x00003f5417db128d))
(constraint (= (f #x8a1dce1d252cca59) #x0000000040606029))
(constraint (= (f #x3928ed0413d46a38) #x0000000141402000))
(constraint (= (f #x03e4a022b87c7555) #x0000000005010101))
(constraint (= (f #xbc7e9a0acaeac9e8) #x00000001e0d05056))
(check-synth)
