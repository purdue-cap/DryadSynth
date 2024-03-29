(set-logic BV)
(define-fun ehad ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000001))
(define-fun arba ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000004))
(define-fun shesh ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000010))
(define-fun smol ((x (_ BitVec 64))) (_ BitVec 64) (bvshl x #x0000000000000001))
(define-fun im ((x (_ BitVec 64)) (y (_ BitVec 64)) (z (_ BitVec 64))) (_ BitVec 64) (ite (= x #x0000000000000001) y z))
(synth-fun f ((x (_ BitVec 64))) (_ BitVec 64)
    ((Start (_ BitVec 64)))
    ((Start (_ BitVec 64) (#x0000000000000000 #x0000000000000001 x (bvnot Start) (smol Start) (ehad Start) (arba Start) (shesh Start) (bvand Start Start) (bvor Start Start) (bvxor Start Start) (bvadd Start Start) (im Start Start Start)))))
(constraint (= (f #x38c9e02aaaea1406) #x02da977e0003070f))
(constraint (= (f #xb922680db9482ed8) #xfffff46dd97f246b))
(constraint (= (f #x766c3d7e374ecec6) #x01952ee1f698b2b2))
(constraint (= (f #x02ee6cdc223a74de) #xffffffd119323ddc))
(constraint (= (f #x320c261b303b1441) #x02a7ae574abecb0c))
(constraint (= (f #xd05410bd758accd4) #xfffff2fabef428a7))
(constraint (= (f #xe5e133258db2170e) #x034772aa45a4a71b))
(constraint (= (f #x600ca1d0c0603e7b) #xfffff9ff35e2f3f9))
(constraint (= (f #xe8ce7eb5ede7aa06) #x031ab5f087275c07))
(constraint (= (f #xb0971773be117a87) #x00b91b19acf731c1))
(constraint (= (f #xd8a3b9c643028a71) #xfffff275c4639bcf))
(constraint (= (f #x4a62e9b1a082e34b) #x00856314b479e368))
(constraint (= (f #xcec36c779b23a553) #xfffff313c938864d))
(constraint (= (f #xebc4e7b964a97043) #x030ecb5cd14811bc))
(constraint (= (f #x8ed81914201c07e7) #x01b25f530e7f6fdf))
(constraint (= (f #xddcabc200de4d597) #xfffff223543dff21))
(constraint (= (f #x37c1e721dea28e1a) #xfffffc83e18de215))
(constraint (= (f #x5ded1c6e444a2beb) #x0067236d34cc860f))
(constraint (= (f #xec04831caede7e06) #x032fc9eb683275f7))
(constraint (= (f #x9ee683aa5076eec1) #x017351ec043d9332))
(constraint (= (f #x1d0ee55b6eab7918) #xfffffe2f11aa4915))
(constraint (= (f #x008db1cc755bcb83) #x03f9a4b6ad804e8d))
(constraint (= (f #xc2a78b466231d6b0) #xfffff3d5874b99dc))
(constraint (= (f #x6531bc4d87236474) #xfffff9ace43b278d))
(constraint (= (f #x4215e30eae8ea919) #xfffffbdea1cf1517))
(constraint (= (f #x956db9c61388ce00) #x010124d6d72d9ab7))
(constraint (= (f #x2020e43138d5b0a4) #x027e7b4eb2da04b8))
(constraint (= (f #x3e70e0a9838b71ca) #x02f5bb7815ed89b6))
(constraint (= (f #xeb1b5ee0e9c19e52) #xfffff14e4a11f163))
(constraint (= (f #x6732eba33231aa51) #xfffff98cd145ccdc))
(constraint (= (f #x53c9b4d21eb0610d) #x002e948a2770bd73))
(constraint (= (f #xed9223b27a7c1317) #xfffff126ddc4d858))
(constraint (= (f #x819c88196d76aa96) #xfffff7e6377e6928))
(constraint (= (f #x0e6b6e83ea7a6e88) #x03b50931ef05c531))
(constraint (= (f #x2e0cb072784e7ec1) #x0237a8bda5dcb5f2))
(constraint (= (f #x636899a21ee43604) #x0169195467734e97))
(constraint (= (f #x9de1eae75a70ae3d) #xfffff621e1518a58))
(constraint (= (f #xc42ae5523eee28a6) #x02ce034026f33618))
(constraint (= (f #x214d7a2602a7983a) #xfffffdeb285d9fd5))
(constraint (= (f #x2ea488bdee98575e) #xfffffd15b7742116))
(constraint (= (f #x4e52373ec5159cb2) #xfffffb1adc8c13ae))
(constraint (= (f #x39960980ece04dd2) #xfffffc669f67f131))
(constraint (= (f #x5c69c1ac630e723c) #xfffffa3963e539cf))
(constraint (= (f #x866ac8e8a9c99131) #xfffff79953717563))
(constraint (= (f #x266ace097c3c9ede) #xfffffd99531f683c))
(constraint (= (f #xb1e53c30aee7e888) #x00b742eeb8335f19))
(constraint (= (f #xd9a479859cc1a188) #x02544dd5c56af475))
(constraint (= (f #x183aba1c147c35bc) #xfffffe7c545e3eb8))
(constraint (= (f #x6c0187323b3b9469) #x012ff5daa6cacd0d))
(constraint (= (f #xea85da0ce82335ad) #x0301c647ab1e6a84))
(constraint (= (f #x6ca4d701edb4ebbb) #xfffff935b28fe124))
(constraint (= (f #x4d3a7a592892325e) #xfffffb2c585a6d76))
(constraint (= (f #x1cac58157aa17576) #xfffffe353a7ea855))
(constraint (= (f #xc9c6aae4dde05275) #xfffff3639551b221))
(constraint (= (f #x867dad6c12ae6ae1) #x01d5e4212f203503))
(constraint (= (f #xe5c7c6e7076851ce) #x0346ded35bd91c36))
(constraint (= (f #x24b9d467b876c85e) #xfffffdb462b98478))
(constraint (= (f #x4794210346b60403) #x00dd0e73e8d097cf))
(constraint (= (f #x2c81ec7a29b1e94b) #x0229f72dc614b710))
(constraint (= (f #x6973353807e10e2e) #x0111aa82dfdf73b6))
(constraint (= (f #x352b12bd72e190ce) #x02820b20e1a3753a))
(constraint (= (f #x3354eb2bb5784a6c) #x02a80b0a0c81dc85))
(constraint (= (f #xde7cebe0163b4e76) #xfffff2183141fe9c))
(constraint (= (f #x4c51e7ced0e44ea4) #x00ac375eb23b4cb0))
(constraint (= (f #xe55e42589b613e2a) #x034074e4594972f6))
(constraint (= (f #xb461304d71a247c7) #x008d72bca1b464de))
(constraint (= (f #xd2e9153db02cb637) #xfffff2d16eac24fd))
(constraint (= (f #x6ca01e3e959eca91) #xfffff935fe1c16a6))
(constraint (= (f #x39e0159c385e9224) #x02d77f056edc7126))
(constraint (= (f #x7ce1e6e702e62352) #xfffff831e1918fd1))
(constraint (= (f #xe5ab1e16dab13cc2) #x03440b771240b2ea))
(constraint (= (f #xbb21ddeb5703ab51) #xfffff44de2214a8f))
(constraint (= (f #xeeed22b0e890307e) #xfffff1112dd4f176))
(constraint (= (f #x2755db654e13e94a) #x0258064940b72f10))
(constraint (= (f #xa8d204b8e4c9a209) #x001a27c8db4a9467))
(constraint (= (f #x0cae58ecad8963a1) #x03a8345b2825916c))
(constraint (= (f #x611e025644d0b76a) #x017377e414ca3899))
(constraint (= (f #x4917c1ee096829e8) #x00931ef737911e17))
(constraint (= (f #x579dcd8c2bb5ab0e) #x001d66a5ae0c840b))
(constraint (= (f #x816c48de8eadeb53) #xfffff7e93b721715))
(constraint (= (f #x0e0e0b931b0b0ee0) #x03b7b78d2b4b8bb3))
(constraint (= (f #x7876455363a41e59) #xfffff8789baac9c5))
(constraint (= (f #x48e683b27eb9c6a8) #x009b51eca5f0d6d0))
(constraint (= (f #x8c317e1e2aed42e7) #x01aeb1f7760320e3))
(constraint (= (f #x58ee3c2b2131e3e8) #x005b36ee0a72b76f))
(constraint (= (f #x87cabb9e92b45343) #x01de80cd71208c28))
(constraint (= (f #xe4b5da7408ece11d) #xfffff1b4a258bf71))
(constraint (= (f #x02ce07614a6187d3) #xffffffd31f89eb59))
(constraint (= (f #x3e0e8321ae9ed42e) #x02f7b1ea7431720e))
(constraint (= (f #x0e5e4057c8e3c5db) #xffffff1a1bfa8371))
(constraint (= (f #x99eee92bb1eba3e6) #x015733120cb70c6f))
(constraint (= (f #xe4ace0860b413937) #xfffff1b531f79f4b))
(constraint (= (f #xd67e6da44bc755ec) #x0215f5244c8ed807))
(constraint (= (f #xe694ac9a36ceec4a) #x035108294692b32c))
(constraint (= (f #x794adbb743505d50) #xfffff86b52448bca))
(constraint (= (f #x9c7e05e0a393420c) #x016df7c7786d28e7))
(constraint (= (f #xdb718a4e779e534d) #x0249b584b59d7428))
(constraint (= (f #x7d8ed6b2ac18b1c1) #x01e5b210a02f58b6))
(constraint (= (f #xa6d9b9c370b53c3e) #xfffff5926463c8f4))
(constraint (= (f #x328d5e470b1cc88e) #x02a1a074db8b6a99))
(check-synth)
