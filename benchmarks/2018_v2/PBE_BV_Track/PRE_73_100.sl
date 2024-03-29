(set-logic BV)
(define-fun ehad ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000001))
(define-fun arba ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000004))
(define-fun shesh ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000010))
(define-fun smol ((x (_ BitVec 64))) (_ BitVec 64) (bvshl x #x0000000000000001))
(define-fun im ((x (_ BitVec 64)) (y (_ BitVec 64)) (z (_ BitVec 64))) (_ BitVec 64) (ite (= x #x0000000000000001) y z))
(synth-fun f ((x (_ BitVec 64))) (_ BitVec 64)
    ((Start (_ BitVec 64)))
    ((Start (_ BitVec 64) (#x0000000000000000 #x0000000000000001 x (bvnot Start) (smol Start) (ehad Start) (arba Start) (shesh Start) (bvand Start Start) (bvor Start Start) (bvxor Start Start) (bvadd Start Start) (im Start Start Start)))))
(constraint (= (f #xd6b39bb86deae61e) #x294c6447921519e1))
(constraint (= (f #x7b1d0d7ee087ece7) #x84e2f2811f781318))
(constraint (= (f #xec2d51ec3bed5a18) #x13d2ae13c412a5e7))
(constraint (= (f #xa8bbd6902c7889aa) #x0545deb48163c44d))
(constraint (= (f #x4a8eae8a86297461) #xb571517579d68b9e))
(constraint (= (f #x19898ce613dcb562) #x00cc4c67309ee5ab))
(constraint (= (f #xad81be706a97e5b4) #x527e418f95681a4b))
(constraint (= (f #x9367dd9ee2b0b5b6) #x6c9822611d4f4a49))
(constraint (= (f #x9001c7868657ee46) #x04800e3c3432bf72))
(constraint (= (f #xe8edc4597e14911d) #x17123ba681eb6ee2))
(constraint (= (f #x2bd67929d659a3ea) #x015eb3c94eb2cd1f))
(constraint (= (f #xe4d09a805e58e03d) #x1b2f657fa1a71fc2))
(constraint (= (f #x4ce94dd243d8904d) #xb316b22dbc276fb2))
(constraint (= (f #x8e6178c498cc08d8) #x719e873b6733f727))
(constraint (= (f #x17b77281880e96ee) #x00bdbb940c4074b7))
(constraint (= (f #xce1344ee884cc9e7) #x31ecbb1177b33618))
(constraint (= (f #x170978701adcd6b2) #xe8f6878fe523294d))
(constraint (= (f #xe39e0e2dc224c07e) #x1c61f1d23ddb3f81))
(constraint (= (f #x47eb774760eea5e9) #xb81488b89f115a16))
(constraint (= (f #xe6ee53015a86c145) #x1911acfea5793eba))
(constraint (= (f #x55d2e9e16e78ae56) #xaa2d161e918751a9))
(constraint (= (f #xb0a8ab2ee48602b8) #x4f5754d11b79fd47))
(constraint (= (f #x4dceee9bc10209ba) #xb23111643efdf645))
(constraint (= (f #x946a748562b6e23d) #x6b958b7a9d491dc2))
(constraint (= (f #x0e62e0b3e7e9d528) #x007317059f3f4ea9))
(constraint (= (f #x3db2c0812210875d) #xc24d3f7eddef78a2))
(constraint (= (f #x92346106770a75e8) #x0491a30833b853af))
(constraint (= (f #x284c9c9d6ea3614e) #x014264e4eb751b0a))
(constraint (= (f #xc6bb3c389806aa1b) #x3944c3c767f955e4))
(constraint (= (f #xe8899c0057bae925) #x177663ffa84516da))
(constraint (= (f #xe5bdeba777109b56) #x1a42145888ef64a9))
(constraint (= (f #x48e324d727ee5487) #xb71cdb28d811ab78))
(constraint (= (f #x24cecddbec794cc1) #xdb3132241386b33e))
(constraint (= (f #x1dc4deaba9d3ba4c) #x00ee26f55d4e9dd2))
(constraint (= (f #x18ebe9de3e579a32) #xe7141621c1a865cd))
(constraint (= (f #x0bd2086ee01d0e70) #xf42df7911fe2f18f))
(constraint (= (f #xe4a7d629aa91ea91) #x1b5829d6556e156e))
(constraint (= (f #x973e4084ac0c6ed8) #x68c1bf7b53f39127))
(constraint (= (f #x5e006de644a63a36) #xa1ff9219bb59c5c9))
(constraint (= (f #xd06a2ba5b998eaee) #x0683515d2dccc757))
(constraint (= (f #x445e08c7e065e780) #x0222f0463f032f3c))
(constraint (= (f #x4deececc2e1952c8) #x026f76766170ca96))
(constraint (= (f #x23c9e9a3e7438808) #x011e4f4d1f3a1c40))
(constraint (= (f #x8ce84806be0dc3e6) #x0467424035f06e1f))
(constraint (= (f #xb5ee7d2bd378ec31) #x4a1182d42c8713ce))
(constraint (= (f #xb481923ece7e49d7) #x4b7e6dc13181b628))
(constraint (= (f #xed491611c21ece33) #x12b6e9ee3de131cc))
(constraint (= (f #x4b30136bb73d7038) #xb4cfec9448c28fc7))
(constraint (= (f #x6eeceb17346d94ae) #x03776758b9a36ca5))
(constraint (= (f #x0b8e068b718233db) #xf471f9748e7dcc24))
(constraint (= (f #xe658c6eae8a74e9b) #x19a739151758b164))
(constraint (= (f #x3ada7126cbe693aa) #x01d6d389365f349d))
(constraint (= (f #x0325b54454758786) #x00192daa22a3ac3c))
(constraint (= (f #x2377b36861e968d0) #xdc884c979e16972f))
(constraint (= (f #xc081eb0d0a24ed0c) #x06040f5868512768))
(constraint (= (f #x882194eeb6c6b767) #x77de6b1149394898))
(constraint (= (f #x8d859837523c7db7) #x727a67c8adc38248))
(constraint (= (f #x84aea7545978c3e8) #x0425753aa2cbc61f))
(constraint (= (f #x81c3bac6e7357dee) #x040e1dd63739abef))
(constraint (= (f #x890d1338e9b2c3ae) #x04486899c74d961d))
(constraint (= (f #xe4dc9050e811ce0c) #x0726e48287408e70))
(constraint (= (f #x266e89e4c4251c15) #xd991761b3bdae3ea))
(constraint (= (f #xb17575e0bcadc40e) #x058babaf05e56e20))
(constraint (= (f #x6ed20883be6d33c5) #x912df77c4192cc3a))
(constraint (= (f #xb0be98074acca560) #x0585f4c03a56652b))
(constraint (= (f #x4abe32ee142b14d2) #xb541cd11ebd4eb2d))
(constraint (= (f #xd1d4565ca7e3b1c6) #x068ea2b2e53f1d8e))
(constraint (= (f #x520718203eb3ea89) #xadf8e7dfc14c1576))
(constraint (= (f #x1926e688469d8978) #xe6d91977b9627687))
(constraint (= (f #x8eace367d5663728) #x0475671b3eab31b9))
(constraint (= (f #xb9ea043b95cb456c) #x05cf5021dcae5a2b))
(constraint (= (f #x9814b693a3e6e380) #x04c0a5b49d1f371c))
(constraint (= (f #xe004087589861e00) #x07002043ac4c30f0))
(constraint (= (f #x18ee16c848277b9e) #xe711e937b7d88461))
(constraint (= (f #x4e97e49e1169d98a) #x0274bf24f08b4ecc))
(constraint (= (f #x23c852b5ec0a5e92) #xdc37ad4a13f5a16d))
(constraint (= (f #x527a8109d4de7069) #xad857ef62b218f96))
(constraint (= (f #xdb6e3e838b5a1ea8) #x06db71f41c5ad0f5))
(constraint (= (f #xe899e80a92e19a81) #x176617f56d1e657e))
(constraint (= (f #x91c2ace1240e7ee0) #x048e1567092073f7))
(constraint (= (f #x0be32ab0e82ec87b) #xf41cd54f17d13784))
(constraint (= (f #xdd9b9e62b5972490) #x2264619d4a68db6f))
(constraint (= (f #xe24154017447e2b2) #x1dbeabfe8bb81d4d))
(constraint (= (f #xa1077eca5a185e7e) #x5ef88135a5e7a181))
(constraint (= (f #x5c7ec9244449c0ea) #x02e3f64922224e07))
(constraint (= (f #x8852072b563419b0) #x77adf8d4a9cbe64f))
(constraint (= (f #xae6547b24842cecb) #x519ab84db7bd3134))
(constraint (= (f #xdb99c234b6ac841c) #x24663dcb49537be3))
(constraint (= (f #x3c92e574e0d5cd4c) #x01e4972ba706ae6a))
(constraint (= (f #xdc2651d0eec5c935) #x23d9ae2f113a36ca))
(constraint (= (f #x57e58e32cdd6b4c7) #xa81a71cd32294b38))
(constraint (= (f #x57011416e667359a) #xa8feebe91998ca65))
(constraint (= (f #xe85e9e74ceac7c41) #x17a1618b315383be))
(constraint (= (f #x86c037ea9559b39e) #x793fc8156aa64c61))
(constraint (= (f #xd2d4dea17343c381) #x2d2b215e8cbc3c7e))
(constraint (= (f #x4569d89c1da13a8d) #xba962763e25ec572))
(constraint (= (f #x4e04c5cbcab35038) #xb1fb3a34354cafc7))
(constraint (= (f #x533e331dedced6c2) #x0299f198ef6e76b6))
(constraint (= (f #xbc410b9dad789819) #x43bef462528767e6))
(constraint (= (f #xe858c22ab42746cc) #x0742c61155a13a36))
(check-synth)
