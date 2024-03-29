(set-logic BV)
(define-fun ehad ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000001))
(define-fun arba ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000004))
(define-fun shesh ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000010))
(define-fun smol ((x (_ BitVec 64))) (_ BitVec 64) (bvshl x #x0000000000000001))
(define-fun im ((x (_ BitVec 64)) (y (_ BitVec 64)) (z (_ BitVec 64))) (_ BitVec 64) (ite (= x #x0000000000000001) y z))
(synth-fun f ((x (_ BitVec 64))) (_ BitVec 64)
    ((Start (_ BitVec 64)))
    ((Start (_ BitVec 64) (#x0000000000000000 #x0000000000000001 x (bvnot Start) (smol Start) (ehad Start) (arba Start) (shesh Start) (bvand Start Start) (bvor Start Start) (bvxor Start Start) (bvadd Start Start) (im Start Start Start)))))
(constraint (= (f #x56c0589b4e2ec480) #x56c0589b4e2ec481))
(constraint (= (f #x93e615d130aea9a9) #x93e615d130aea9a8))
(constraint (= (f #xb869894ee852197a) #xb869894ee852197b))
(constraint (= (f #xa15deb9740deaeb1) #xa15deb9740deaeb0))
(constraint (= (f #x1e3ac305e8e19215) #x070e29e7d0b8f36f))
(constraint (= (f #xa26da99aa6a71dce) #x02ec92b32acac711))
(constraint (= (f #x1b501e7c2142c48e) #x1b501e7c2142c48f))
(constraint (= (f #x6329698557a151e4) #x04e6b4b3d542f570))
(constraint (= (f #x2c4e35c93a432c4e) #x069d8e51b62de69d))
(constraint (= (f #x43ac0ede1e184057) #x43ac0ede1e184056))
(constraint (= (f #xd863b275cdbed0b0) #xd863b275cdbed0b1))
(constraint (= (f #xe1e500bad7db2184) #x00f0d7fa294126f3))
(constraint (= (f #x80cba1ba3045a888) #x03f9a2f22e7dd2bb))
(constraint (= (f #xbee5d9e1699551c4) #x0208d130f4b35571))
(constraint (= (f #x2e77acba0bce5c38) #x2e77acba0bce5c39))
(constraint (= (f #x4e66db8a11726a4c) #x4e66db8a11726a4d))
(constraint (= (f #x7ae0e9b0a37aada3) #x7ae0e9b0a37aada2))
(constraint (= (f #x663e0e9d0b617c3c) #x04ce0f8b17a4f41e))
(constraint (= (f #x767ae65d648127db) #x044c28cd14dbf6c1))
(constraint (= (f #xcecde666c6c2db50) #xcecde666c6c2db51))
(constraint (= (f #xd5713db2473bb8ed) #x015476126dc62238))
(constraint (= (f #x04e0bac5d9cb9aa3) #x07d8fa29d131a32a))
(constraint (= (f #x9c16380b02c8c247) #x9c16380b02c8c246))
(constraint (= (f #x0ab5b8d52ddece5e) #x0ab5b8d52ddece5f))
(constraint (= (f #xc10ea0d9ee19280e) #x01f78af9308f36bf))
(constraint (= (f #xe9acee1e8509e3b2) #x00b2988f0bd7b0e2))
(constraint (= (f #x8d5a61deec2343ce) #x03952cf1089ee5e1))
(constraint (= (f #x8cd0e84ee1bb5d41) #x039978bd88f22515))
(constraint (= (f #x154ac3cbe5c8e839) #x154ac3cbe5c8e838))
(constraint (= (f #x0a83b1e6568a5565) #x0a83b1e6568a5564))
(constraint (= (f #x71e34c39c3e1c1ec) #x0470e59e31e0f1f0))
(constraint (= (f #x53ac12829e276433) #x05629f6beb0ec4de))
(constraint (= (f #x5d971008ec46c108) #x5d971008ec46c109))
(constraint (= (f #x9c4bab4460245030) #x9c4bab4460245031))
(constraint (= (f #xd538ccd97684e389) #xd538ccd97684e388))
(constraint (= (f #xec515e5aeeba337e) #xec515e5aeeba337f))
(constraint (= (f #x3618dc401d7d460e) #x064f391dff1415cf))
(constraint (= (f #x00489bb69a9a6568) #x00489bb69a9a6569))
(constraint (= (f #xc33807eed5ee3235) #xc33807eed5ee3234))
(constraint (= (f #xce269db5078333e0) #x018ecb1257c3e660))
(constraint (= (f #xe6bbca0d77351656) #x00ca21af9446574d))
(constraint (= (f #xb3de336a3bc7d6e7) #x02610e64ae21c148))
(constraint (= (f #x62a1436e626697e0) #x62a1436e626697e1))
(constraint (= (f #xc3ce267b20a8811b) #xc3ce267b20a8811a))
(constraint (= (f #x87d3172a2b91ccde) #x03c16746aea37199))
(constraint (= (f #xeed848b6545205d1) #xeed848b6545205d0))
(constraint (= (f #x81b46b11e2a6d438) #x81b46b11e2a6d439))
(constraint (= (f #x3d3cba2694bd4071) #x06161a2ecb5a15fc))
(constraint (= (f #x83507e3075816580) #x03e57c0e7c53f4d3))
(constraint (= (f #x64ce5ac88ecee0c2) #x64ce5ac88ecee0c3))
(constraint (= (f #x3a3b594eea04a36e) #x3a3b594eea04a36f))
(constraint (= (f #x79537e1e4e040169) #x79537e1e4e040168))
(constraint (= (f #xb2355e991c532d4e) #x026e550b371d6695))
(constraint (= (f #xeb5272e2e35a24e0) #xeb5272e2e35a24e1))
(constraint (= (f #x5e6c235535d85c24) #x5e6c235535d85c25))
(constraint (= (f #xc6acd1e57cb76bce) #x01ca9970d41a44a1))
(constraint (= (f #xc4b766eac4e9e378) #x01da44c8a9d8b0e4))
(constraint (= (f #x6600b94534506e0c) #x6600b94534506e0d))
(constraint (= (f #x26904e898a25ac8b) #x06cb7d8bb3aed29b))
(constraint (= (f #x705e459c5684d9b7) #x705e459c5684d9b6))
(constraint (= (f #x0ee0b6717bc39d30) #x0788fa4c7421e316))
(constraint (= (f #x202417eee36975bc) #x06fedf4088e4b452))
(constraint (= (f #xd67da420b189444a) #x014c12defa73b5dd))
(constraint (= (f #xed6eecd40e8ca069) #xed6eecd40e8ca068))
(constraint (= (f #xa2ee08078b9cdde2) #xa2ee08078b9cdde3))
(constraint (= (f #x41ca5587ba51aa7e) #x05f1ad53c22d72ac))
(constraint (= (f #xa5e028bc714d49cb) #x02d0feba1c7595b1))
(constraint (= (f #x882edc20e44d24e8) #x03be891ef8dd96d8))
(constraint (= (f #x882697c50a9d1e68) #x03becb41d7ab170c))
(constraint (= (f #x3b115e9e099b084e) #x0627750b0fb327bd))
(constraint (= (f #x5ceeee7d592eaed2) #x5ceeee7d592eaed3))
(constraint (= (f #xb6d8e6e8ae49083e) #x024938c8ba8db7be))
(constraint (= (f #xc842eee15aade573) #x01bde888f52a90d4))
(constraint (= (f #xe54e2247668e6e0e) #xe54e2247668e6e0f))
(constraint (= (f #x675c80854bbce4e9) #x675c80854bbce4e8))
(constraint (= (f #xe83dc2b2698a6d75) #xe83dc2b2698a6d74))
(constraint (= (f #x90eb6eac73d3aae2) #x0378a48a9c6162a8))
(constraint (= (f #xbb87894075c78382) #x0223c3b5fc51c3e3))
(constraint (= (f #x6410d7d71d5ce8e1) #x6410d7d71d5ce8e0))
(constraint (= (f #x35643d4423edbe63) #x0654de15dee0920c))
(constraint (= (f #x718d82ee28e0e531) #x718d82ee28e0e530))
(constraint (= (f #x681a17bd46a39868) #x04bf2f4215cae33c))
(constraint (= (f #x86467e5551bb1aeb) #x03cdcc0d55722728))
(constraint (= (f #x99ae5bcc51b5bb9a) #x03328d219d725223))
(constraint (= (f #xcae3da0c7d1870c8) #xcae3da0c7d1870c9))
(constraint (= (f #x92e9010eb68ea91e) #x92e9010eb68ea91f))
(constraint (= (f #xa9ea18083de88be1) #xa9ea18083de88be0))
(constraint (= (f #x8e5e0d4a4802350c) #x8e5e0d4a4802350d))
(constraint (= (f #x453b1e637982888d) #x453b1e637982888c))
(constraint (= (f #xe40c99dabeda2beb) #xe40c99dabeda2bea))
(constraint (= (f #x8c07ebc072e31809) #x039fc0a1fc68e73f))
(constraint (= (f #x9884e6ae309b286b) #x033bd8ca8e7b26bc))
(constraint (= (f #x69bd609e8b2439e9) #x69bd609e8b2439e8))
(constraint (= (f #x7a56e822026a5b62) #x7a56e822026a5b63))
(constraint (= (f #xc73386844e035ee1) #x01c663cbdd8fe508))
(constraint (= (f #x4c5e09d17e9e70aa) #x4c5e09d17e9e70ab))
(constraint (= (f #xe5b6e39ee795e244) #x00d248e308c350ed))
(constraint (= (f #x7d1aad3e784eb96d) #x7d1aad3e784eb96c))
(constraint (= (f #x4e56c4ac22e44e81) #x4e56c4ac22e44e80))
(constraint (= (f #xd912de2e69a7564e) #x0137690e8cb2c54d))
(check-synth)
