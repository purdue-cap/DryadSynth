(set-logic BV)
(define-fun ehad ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000001))
(define-fun arba ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000004))
(define-fun shesh ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000010))
(define-fun smol ((x (_ BitVec 64))) (_ BitVec 64) (bvshl x #x0000000000000001))
(define-fun im ((x (_ BitVec 64)) (y (_ BitVec 64)) (z (_ BitVec 64))) (_ BitVec 64) (ite (= x #x0000000000000001) y z))
(synth-fun f ((x (_ BitVec 64))) (_ BitVec 64)
    ((Start (_ BitVec 64)))
    ((Start (_ BitVec 64) (#x0000000000000000 #x0000000000000001 x (bvnot Start) (smol Start) (ehad Start) (arba Start) (shesh Start) (bvand Start Start) (bvor Start Start) (bvxor Start Start) (bvadd Start Start) (im Start Start Start)))))
(constraint (= (f #x8b48587a0a6781a7) #xfffffffffffffffe))
(constraint (= (f #xa1b0e96d3c7e9cee) #xfffffffffffffffc))
(constraint (= (f #xc48a44ee18e15eec) #xfffffffffffffffc))
(constraint (= (f #xbc7a0e4276038a6e) #xfffffffffffffffc))
(constraint (= (f #xa0181944c852b231) #x0a0181944c852b23))
(constraint (= (f #xc6e7567b9cdc3072) #xfffffffffffffffc))
(constraint (= (f #xd9e5c9e934bdbe51) #x0d9e5c9e934bdbe5))
(constraint (= (f #x10e92d2e6cc78c55) #x010e92d2e6cc78c5))
(constraint (= (f #x0b2799c5d520e56e) #x00b2799c5d520e56))
(constraint (= (f #xd0ca8eba22b16258) #xfffffffffffffffc))
(constraint (= (f #x031633d25b63e5ed) #xfffffffffffffffe))
(constraint (= (f #x6788c0aedbe965bd) #xfffffffffffffffe))
(constraint (= (f #x80b2a007eb364c51) #x080b2a007eb364c5))
(constraint (= (f #x54c5b864a83eb31d) #xfffffffffffffffe))
(constraint (= (f #x91433354a44183be) #x091433354a44183b))
(constraint (= (f #x1893ae8e40a60ec1) #x01893ae8e40a60ec))
(constraint (= (f #x440375eb587d21b9) #xfffffffffffffffe))
(constraint (= (f #x7bd496b9055d4cc5) #x07bd496b9055d4cc))
(constraint (= (f #x8e487d18cdcca92d) #xfffffffffffffffe))
(constraint (= (f #x9150c0a1ed67e58e) #x09150c0a1ed67e58))
(constraint (= (f #xda6cb841dded8b59) #xfffffffffffffffe))
(constraint (= (f #xc68052012431aee4) #xfffffffffffffffc))
(constraint (= (f #x8857c7e92643d6d5) #x08857c7e92643d6d))
(constraint (= (f #x901e35119ebc8075) #x0901e35119ebc807))
(constraint (= (f #xeee14421963e54c7) #x0eee14421963e54c))
(constraint (= (f #xb64b77e5ee752a34) #xfffffffffffffffc))
(constraint (= (f #x2d3e5e101e94d342) #x02d3e5e101e94d34))
(constraint (= (f #xb1e634cb62254b1e) #x0b1e634cb62254b1))
(constraint (= (f #x8282e0654c9c4b2e) #x08282e0654c9c4b2))
(constraint (= (f #xeb0e018b9a95d4ae) #xfffffffffffffffc))
(constraint (= (f #x2a9e81ac7343ee3c) #xfffffffffffffffc))
(constraint (= (f #x75be51e3d2a88435) #x075be51e3d2a8843))
(constraint (= (f #xe71eb2075e861478) #xfffffffffffffffc))
(constraint (= (f #x14cd4b7e930ee611) #x014cd4b7e930ee61))
(constraint (= (f #x537755140e50b2e4) #xfffffffffffffffc))
(constraint (= (f #x4ccb507ca859c0a2) #xfffffffffffffffc))
(constraint (= (f #x0b00e121b055b459) #x00b00e121b055b45))
(constraint (= (f #xe625794d5aac4bae) #x0e625794d5aac4ba))
(constraint (= (f #x07e1d1454683c49d) #x007e1d1454683c49))
(constraint (= (f #x0950dbd852e41898) #xfffffffffffffffc))
(constraint (= (f #x0d6ca7a0a06daae1) #x00d6ca7a0a06daae))
(constraint (= (f #x9b0136722e013607) #x09b0136722e01360))
(constraint (= (f #x178bb6ccdb06c795) #xfffffffffffffffe))
(constraint (= (f #x6cce8026b5bbcd85) #xfffffffffffffffe))
(constraint (= (f #x49ea38a7c4abbc7e) #xfffffffffffffffc))
(constraint (= (f #x0652ecb5c698ee34) #xfffffffffffffffc))
(constraint (= (f #x729c7e56376c55e8) #x0729c7e56376c55e))
(constraint (= (f #x1ec70a4e8ec9c6e0) #xfffffffffffffffc))
(constraint (= (f #x5dbae5ce424e3ae7) #x05dbae5ce424e3ae))
(constraint (= (f #xec9e489ad22820e0) #xfffffffffffffffc))
(constraint (= (f #xac6e886e864595c5) #xfffffffffffffffe))
(constraint (= (f #x8c64daee1b1e5268) #xfffffffffffffffc))
(constraint (= (f #x95a2dc46b960e8a3) #x095a2dc46b960e8a))
(constraint (= (f #x361aa464e524758c) #x0361aa464e524758))
(constraint (= (f #xdb6156d4eb888723) #xfffffffffffffffe))
(constraint (= (f #x8bebb7059ea25de2) #x08bebb7059ea25de))
(constraint (= (f #x8b72428ccbc5ad8d) #xfffffffffffffffe))
(constraint (= (f #x977987a46b67bd01) #xfffffffffffffffe))
(constraint (= (f #xaec88dea479e9ee1) #x0aec88dea479e9ee))
(constraint (= (f #x7cb9ebd73aaed7a0) #x07cb9ebd73aaed7a))
(constraint (= (f #x050aed256ec823cb) #xfffffffffffffffe))
(constraint (= (f #x381c327807088d33) #xfffffffffffffffe))
(constraint (= (f #xc948b25735226e7e) #xfffffffffffffffc))
(constraint (= (f #xbc5971eb9568d4ee) #xfffffffffffffffc))
(constraint (= (f #x085d3eee58ba6dea) #x0085d3eee58ba6de))
(constraint (= (f #x4c21b947e551a07a) #xfffffffffffffffc))
(constraint (= (f #xa53bbe6e182b75a8) #x0a53bbe6e182b75a))
(constraint (= (f #xe585edeea827767e) #xfffffffffffffffc))
(constraint (= (f #xd8a65e380219645e) #xfffffffffffffffc))
(constraint (= (f #x5ba0d208d9da0ae4) #xfffffffffffffffc))
(constraint (= (f #x1e39308594b514ee) #xfffffffffffffffc))
(constraint (= (f #xd338496d5e4c0096) #xfffffffffffffffc))
(constraint (= (f #x6eee85cceaeced57) #xfffffffffffffffe))
(constraint (= (f #xa6d23ac00a3e703b) #x0a6d23ac00a3e703))
(constraint (= (f #x3d358b27893ee0ab) #x03d358b27893ee0a))
(constraint (= (f #xe8da0861ee31145c) #xfffffffffffffffc))
(constraint (= (f #x21852d2a37e14a0d) #x021852d2a37e14a0))
(constraint (= (f #x006d30c1bb00a475) #x0006d30c1bb00a47))
(constraint (= (f #xba215da708a89e9b) #x0ba215da708a89e9))
(constraint (= (f #xb9be0a298bd6eb74) #x0b9be0a298bd6eb7))
(constraint (= (f #x83a71be65530c09e) #xfffffffffffffffc))
(constraint (= (f #x27656d0e295ee1ea) #x027656d0e295ee1e))
(constraint (= (f #x7b59a10356ee645d) #x07b59a10356ee645))
(constraint (= (f #x52332649725a130c) #x052332649725a130))
(constraint (= (f #x9d9a50e910ceec5c) #xfffffffffffffffc))
(constraint (= (f #x4e4a501257b463a2) #x04e4a501257b463a))
(constraint (= (f #xa973bebc1c893e7c) #xfffffffffffffffc))
(constraint (= (f #x41443d2060b18961) #xfffffffffffffffe))
(constraint (= (f #x682b3d752b6131e7) #xfffffffffffffffe))
(constraint (= (f #x77695ce2449ce67b) #x077695ce2449ce67))
(constraint (= (f #x1206332d7eeac97e) #x01206332d7eeac97))
(constraint (= (f #x05e5aea823304020) #xfffffffffffffffc))
(constraint (= (f #x8cc90a3b8ce418a7) #x08cc90a3b8ce418a))
(constraint (= (f #x7c92e6b1ea38024e) #xfffffffffffffffc))
(constraint (= (f #x8c6d3b32e408ec5d) #x08c6d3b32e408ec5))
(constraint (= (f #x31325a171d8ae503) #xfffffffffffffffe))
(constraint (= (f #xa8a220b56a6b9966) #x0a8a220b56a6b996))
(constraint (= (f #xce9e594298156ec4) #xfffffffffffffffc))
(constraint (= (f #x73c3e35e74318e06) #xfffffffffffffffc))
(constraint (= (f #x9dce7941dce2210a) #x09dce7941dce2210))
(check-synth)
