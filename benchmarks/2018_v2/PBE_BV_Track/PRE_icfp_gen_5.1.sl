(set-logic BV)
(define-fun ehad ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000001))
(define-fun arba ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000004))
(define-fun shesh ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000010))
(define-fun smol ((x (_ BitVec 64))) (_ BitVec 64) (bvshl x #x0000000000000001))
(define-fun im ((x (_ BitVec 64)) (y (_ BitVec 64)) (z (_ BitVec 64))) (_ BitVec 64) (ite (= x #x0000000000000001) y z))
(synth-fun f ((x (_ BitVec 64))) (_ BitVec 64)
    ((Start (_ BitVec 64)))
    ((Start (_ BitVec 64) (#x0000000000000000 #x0000000000000001 x (bvnot Start) (smol Start) (ehad Start) (arba Start) (shesh Start) (bvand Start Start) (bvor Start Start) (bvxor Start Start) (bvadd Start Start) (im Start Start Start)))))
(constraint (= (f #x79B3AC34264C0930) #x0000000000000001))
(constraint (= (f #x69FF22B01D71F8D9) #x0000000000000000))
(constraint (= (f #x51A2D153A008A154) #x0000000000000001))
(constraint (= (f #x919F2E24557189F9) #x0000000000000000))
(constraint (= (f #x347C69F9FE8C1570) #x0000000000000001))
(constraint (= (f #x6E9ABE97A7FB7365) #x0000000000000000))
(constraint (= (f #x9D06E5F52AC1A2A8) #x0000000000000001))
(constraint (= (f #x732C337F72924488) #x0000000000000001))
(constraint (= (f #xCCB4659841C359C5) #x0000000000000000))
(constraint (= (f #xCCFD79599B773881) #x0000000000000000))
(constraint (= (f #x3AF46A3B4F158C36) #x00003AF46A3B4F15))
(constraint (= (f #x4DC2404BA7BCB3FF) #x00004DC2404BA7BD))
(constraint (= (f #xA5F0879797A33C3E) #x0000A5F0879797A3))
(constraint (= (f #x152E18EBEE327D3B) #x0000152E18EBEE33))
(constraint (= (f #xFFD3EED7A473EE93) #x0000FFD3EED7A473))
(constraint (= (f #x00000003183C09D9) #x0000000000000000))
(constraint (= (f #x00000003B12C3C94) #x0000000000000001))
(constraint (= (f #x00000003A75E16D8) #x0000000000000001))
(constraint (= (f #x00000002844D76B4) #x0000000000000001))
(constraint (= (f #x00000002D155E2B1) #x0000000000000000))
(constraint (= (f #x9C54D48E55EAD702) #x0000000000000001))
(constraint (= (f #x58E6C2E676003582) #x0000000000000001))
(constraint (= (f #xAAF837734D88C72F) #x0000000000000000))
(constraint (= (f #x65689C6136E0D06E) #x0000000000000001))
(constraint (= (f #xF8F56A690C63786F) #x0000000000000000))
(constraint (= (f #x00000003D510D6E0) #x0000000000000001))
(constraint (= (f #x0000000294E0B644) #x0000000000000001))
(constraint (= (f #x000000037376944C) #x0000000000000001))
(constraint (= (f #x0000000303925704) #x0000000000000001))
(constraint (= (f #x000000033DE6F905) #x0000000000000000))
(constraint (= (f #x00000002FAA4915E) #x000000000002FAA7))
(constraint (= (f #x00000003221C46D3) #x000000000003221F))
(constraint (= (f #x000000036E0A8E7F) #x0000000000036E0D))
(constraint (= (f #x000000030CB8FE9F) #x0000000000030CBD))
(constraint (= (f #x00000002201293B3) #x0000000000022015))
(constraint (= (f #x000000039F565703) #x0000000000000000))
(constraint (= (f #x0000000349ED9E6B) #x0000000000000000))
(constraint (= (f #x00000002146357C7) #x0000000000000000))
(constraint (= (f #x00000003EFD35D2F) #x0000000000000000))
(constraint (= (f #x00000002FE6C636F) #x0000000000000000))
(check-synth)
