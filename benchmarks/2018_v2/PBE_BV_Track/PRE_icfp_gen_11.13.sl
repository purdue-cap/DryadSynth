(set-logic BV)
(define-fun ehad ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000001))
(define-fun arba ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000004))
(define-fun shesh ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000010))
(define-fun smol ((x (_ BitVec 64))) (_ BitVec 64) (bvshl x #x0000000000000001))
(define-fun im ((x (_ BitVec 64)) (y (_ BitVec 64)) (z (_ BitVec 64))) (_ BitVec 64) (ite (= x #x0000000000000001) y z))
(synth-fun f ((x (_ BitVec 64))) (_ BitVec 64)
    ((Start (_ BitVec 64)))
    ((Start (_ BitVec 64) (#x0000000000000000 #x0000000000000001 x (bvnot Start) (smol Start) (ehad Start) (arba Start) (shesh Start) (bvand Start Start) (bvor Start Start) (bvxor Start Start) (bvadd Start Start) (im Start Start Start)))))
(constraint (= (f #x6DFC68E0D96E01DB) #x9203971F2691FE23))
(constraint (= (f #x2926221AEAC013AD) #xD6D9DDE5153FEC51))
(constraint (= (f #xE8BF12A2FF02FFA7) #x1740ED5D00FD0057))
(constraint (= (f #xB16153BB7FCAB0C1) #x4E9EAC4480354F3D))
(constraint (= (f #xB91E4DF7B2A8B2AD) #x46E1B2084D574D51))
(constraint (= (f #xD35F9188C1674B9E) #x1A6BF231182CE972))
(constraint (= (f #xCB35A76CA60BA329) #x1966B4ED94C17464))
(constraint (= (f #x5A9281282B959154) #x0B5250250572B22A))
(constraint (= (f #x9DB84290AD8BB0CE) #x13B7085215B17618))
(constraint (= (f #x8AA866825A455DF7) #x11550CD04B48ABBE))
(constraint (= (f #x0000000000000001) #xFFFFFFFFFFFFFFFD))
(constraint (= (f #x0000000000000035) #xFFFFFFFFFFFFFF95))
(constraint (= (f #x000000000000003B) #xFFFFFFFFFFFFFF89))
(constraint (= (f #x0000000000000020) #xFFFFFFFFFFFFFFBF))
(constraint (= (f #x0000000000000022) #xFFFFFFFFFFFFFFBB))
(constraint (= (f #x000000000000002A) #xFFFFFFFFFFFFFFAB))
(constraint (= (f #xACEF0567782B3BC2) #x5310FA9887D4C43D))
(constraint (= (f #xACEF0567782B3BC5) #x5310FA9887D4C439))
(constraint (= (f #xFFFFFFFFFFFFFFFD) #x0000000000000000))
(check-synth)
