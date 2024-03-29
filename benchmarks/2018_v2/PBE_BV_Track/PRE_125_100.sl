(set-logic BV)
(define-fun ehad ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000001))
(define-fun arba ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000004))
(define-fun shesh ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000010))
(define-fun smol ((x (_ BitVec 64))) (_ BitVec 64) (bvshl x #x0000000000000001))
(define-fun im ((x (_ BitVec 64)) (y (_ BitVec 64)) (z (_ BitVec 64))) (_ BitVec 64) (ite (= x #x0000000000000001) y z))
(synth-fun f ((x (_ BitVec 64))) (_ BitVec 64)
    ((Start (_ BitVec 64)))
    ((Start (_ BitVec 64) (#x0000000000000000 #x0000000000000001 x (bvnot Start) (smol Start) (ehad Start) (arba Start) (shesh Start) (bvand Start Start) (bvor Start Start) (bvxor Start Start) (bvadd Start Start) (im Start Start Start)))))
(constraint (= (f #xdcd94a817eb2e80d) #x464d6afd029a2fe5))
(constraint (= (f #xc27ca27eb040b0aa) #x7b06bb029f7e9eab))
(constraint (= (f #x8e9e31418625e929) #xe2c39d7cf3b42dad))
(constraint (= (f #x363202e68e755da5) #xfffffffffffffffe))
(constraint (= (f #x748b826d0c1a08a6) #xffffffffffffffff))
(constraint (= (f #xc9a8d8ec88e2d2b0) #xffffffffffffffff))
(constraint (= (f #x842ee524a8a391cb) #xf7a235b6aeb8dc69))
(constraint (= (f #x8208e631d4621855) #xfffffffffffffffe))
(constraint (= (f #x1b916328973e77ec) #xc8dd39aed1831027))
(constraint (= (f #xb2d511597e10c0d0) #xffffffffffffffff))
(constraint (= (f #x41a002e4e4d42b5e) #x7cbffa363657a943))
(constraint (= (f #xe729a181d3ea2a9d) #x31acbcfc582baac5))
(constraint (= (f #xb43e494c93727203) #xfffffffffffffffe))
(constraint (= (f #x32257c1ae95b0a0a) #x9bb507ca2d49ebeb))
(constraint (= (f #x27721b0c2215de0d) #xb11bc9e7bbd443e5))
(constraint (= (f #x748c9b57523e41e8) #x16e6c9515b837c2f))
(constraint (= (f #xeeb713a51274c729) #x2291d8b5db1671ad))
(constraint (= (f #xcee10d3e02e62086) #xffffffffffffffff))
(constraint (= (f #x7371c1441e836c35) #xfffffffffffffffe))
(constraint (= (f #xb342ade1cbced307) #xfffffffffffffffe))
(constraint (= (f #xd58ec3ec28bcbe97) #xfffffffffffffffe))
(constraint (= (f #x3315d39d2e678382) #xffffffffffffffff))
(constraint (= (f #x2db5670056c4c38b) #xa49531ff527678e9))
(constraint (= (f #x71a660c534cdeccc) #x1cb33e7596642667))
(constraint (= (f #xe876998a25c2ee9b) #x2f12ccebb47a22c9))
(constraint (= (f #x3917b70a79be95ec) #x8dd091eb0c82d427))
(constraint (= (f #xc2ed3bd3d7775b17) #xfffffffffffffffe))
(constraint (= (f #x26dcb9b602dac059) #xb2468c93fa4a7f4d))
(constraint (= (f #xc00d10dee354ce28) #x7fe5de42395663af))
(constraint (= (f #xc7d09dd47d8e640e) #x705ec45704e337e3))
(constraint (= (f #x1dee61893abb88e2) #xffffffffffffffff))
(constraint (= (f #x703c43349b9733cc) #x1f877996c8d19867))
(constraint (= (f #x819e4ed7c9adb8ed) #xfcc362506ca48e25))
(constraint (= (f #x0e3511620351a10b) #xe395dd3bf95cbde9))
(constraint (= (f #x6b8734500b0a684c) #x28f1975fe9eb2f67))
(constraint (= (f #x6bd40885b63be3d8) #x2857eef49388384f))
(constraint (= (f #x80e86225d1a02d44) #xffffffffffffffff))
(constraint (= (f #x2adc863be015e40e) #xaa46f3883fd437e3))
(constraint (= (f #x8bda4bd97790c1e7) #xfffffffffffffffe))
(constraint (= (f #x3d49e53951e7ee87) #xfffffffffffffffe))
(constraint (= (f #xe9c76923ce41ab02) #xffffffffffffffff))
(constraint (= (f #xd55914098a09b3da) #x554dd7ecebec984b))
(constraint (= (f #x2699a93b0983dbeb) #xb2ccad89ecf84829))
(constraint (= (f #x629a40ebd3201329) #x3acb7e2859bfd9ad))
(constraint (= (f #x88b682eee6deed71) #xfffffffffffffffe))
(constraint (= (f #x2bb42de7d914557a) #xa897a4304dd7550b))
(constraint (= (f #xc8ae1e83657be06e) #x6ea3c2f935083f23))
(constraint (= (f #x5ce86333a0e62073) #xfffffffffffffffe))
(constraint (= (f #xc4399ee627e18ce1) #xfffffffffffffffe))
(constraint (= (f #x118159aa2c9e40b4) #xffffffffffffffff))
(constraint (= (f #xdee2e23bea8283a8) #x423a3b882afaf8af))
(constraint (= (f #xe56281927430d1a2) #xffffffffffffffff))
(constraint (= (f #x2ac293007ee27098) #xaa7ad9ff023b1ecf))
(constraint (= (f #xc6677382e624c88e) #x733118fa33b66ee3))
(constraint (= (f #xe262e7056d77c471) #xfffffffffffffffe))
(constraint (= (f #xc8e7ee48c2be1875) #xfffffffffffffffe))
(constraint (= (f #x33e93605e2e97ca6) #xffffffffffffffff))
(constraint (= (f #xabe9654e0493915b) #xa82d3563f6d8dd49))
(constraint (= (f #xdeb2e8e19a4bd75b) #x429a2e3ccb685149))
(constraint (= (f #x529de3d824e24228) #x5ac4384fb63b7baf))
(constraint (= (f #x1c7412426cab2e11) #xfffffffffffffffe))
(constraint (= (f #x4e6d8eeb17c24e07) #xfffffffffffffffe))
(constraint (= (f #xe513b276e9596144) #xffffffffffffffff))
(constraint (= (f #x5ec71e254cdceb9c) #x4271c3b5664628c7))
(constraint (= (f #x84c26b5ee049944b) #xf67b29423f6cd769))
(constraint (= (f #x28d4822ca5ee4476) #xffffffffffffffff))
(constraint (= (f #x8177ebe077737185) #xfffffffffffffffe))
(constraint (= (f #x6ecc847c66b38d25) #xfffffffffffffffe))
(constraint (= (f #xe4b79c6e4e174a0e) #x3690c72363d16be3))
(constraint (= (f #x5162cc8595519c14) #xffffffffffffffff))
(constraint (= (f #x3c80ec5ac9aee4d4) #xffffffffffffffff))
(constraint (= (f #x3c3202c782156e81) #xfffffffffffffffe))
(constraint (= (f #x3a17569ae9d99ee4) #xffffffffffffffff))
(constraint (= (f #xe89d0ece336d4540) #xffffffffffffffff))
(constraint (= (f #x5598e20274eeb779) #x54ce3bfb1622910d))
(constraint (= (f #x23c00a04a8a666b0) #xffffffffffffffff))
(constraint (= (f #xc2d24c388c5b15c7) #xfffffffffffffffe))
(constraint (= (f #xc5e785bc50551720) #xffffffffffffffff))
(constraint (= (f #xc551deea1bcd3217) #xfffffffffffffffe))
(constraint (= (f #x1c0e59534ece4c3c) #xc7e34d5962636787))
(constraint (= (f #x46c39442ea8602b5) #xfffffffffffffffe))
(constraint (= (f #x6baae9cea038e0d7) #xfffffffffffffffe))
(constraint (= (f #xdbada6ea212b4635) #xfffffffffffffffe))
(constraint (= (f #x9b46bd26646ae028) #xc97285b3372a3faf))
(constraint (= (f #xba75e77280e8ea2a) #x8b14311afe2e2bab))
(constraint (= (f #x7a46eec0149a9745) #xfffffffffffffffe))
(constraint (= (f #xebc6d04e54295c91) #xfffffffffffffffe))
(constraint (= (f #x926894955c15403e) #xdb2ed6d547d57f83))
(constraint (= (f #x3441edee8ed70e1e) #x977c2422e251e3c3))
(constraint (= (f #x46e371004c20bae9) #x72391dff67be8a2d))
(constraint (= (f #x4888c68033848824) #xffffffffffffffff))
(constraint (= (f #xc26702d092e81774) #xffffffffffffffff))
(constraint (= (f #x7ad7c7adb6caa1b2) #xffffffffffffffff))
(constraint (= (f #x249910407cd2dbc9) #xb6cddf7f065a486d))
(constraint (= (f #x32c964de2ce30e79) #x9a6d3643a639e30d))
(constraint (= (f #xbde0526138962c16) #xffffffffffffffff))
(constraint (= (f #x821cc49ee64c2cee) #xfbc676c23367a623))
(constraint (= (f #x1a852479ee0d56ee) #xcaf5b70c23e55223))
(constraint (= (f #x9648384dbdde6389) #xd36f8f64844338ed))
(constraint (= (f #xbd856e7e7ce05464) #xffffffffffffffff))
(check-synth)
