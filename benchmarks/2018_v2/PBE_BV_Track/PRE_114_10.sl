(set-logic BV)
(define-fun ehad ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000001))
(define-fun arba ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000004))
(define-fun shesh ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000010))
(define-fun smol ((x (_ BitVec 64))) (_ BitVec 64) (bvshl x #x0000000000000001))
(define-fun im ((x (_ BitVec 64)) (y (_ BitVec 64)) (z (_ BitVec 64))) (_ BitVec 64) (ite (= x #x0000000000000001) y z))
(synth-fun f ((x (_ BitVec 64))) (_ BitVec 64)
    ((Start (_ BitVec 64)))
    ((Start (_ BitVec 64) (#x0000000000000000 #x0000000000000001 x (bvnot Start) (smol Start) (ehad Start) (arba Start) (shesh Start) (bvand Start Start) (bvor Start Start) (bvxor Start Start) (bvadd Start Start) (im Start Start Start)))))
(constraint (= (f #xb5d319e72c70b41e) #xb5d319e72c70b41f))
(constraint (= (f #x000a21910c417e93) #x000a729d94a38a87))
(constraint (= (f #x4dd41437805bc948) #x4dd41437805bc948))
(constraint (= (f #x870a55ec609d6896) #x870a55ec609d6897))
(constraint (= (f #xe2eb1cd253e24776) #xe2eb1cd253e24777))
(constraint (= (f #xd20ea744e72c09d8) #xd20ea744e72c09d9))
(constraint (= (f #x2762ed62e9ec9561) #x289e04ce013bfa0c))
(constraint (= (f #xc6cc66655cdc46a4) #xc6cc66655cdc46a4))
(constraint (= (f #x0ec1399e70dd2d07) #x0f37436b6464166f))
(constraint (= (f #x42e06e86367a4e8e) #x42e06e86367a4e8e))
(check-synth)
