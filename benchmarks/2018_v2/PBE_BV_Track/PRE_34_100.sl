(set-logic BV)
(define-fun ehad ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000001))
(define-fun arba ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000004))
(define-fun shesh ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000010))
(define-fun smol ((x (_ BitVec 64))) (_ BitVec 64) (bvshl x #x0000000000000001))
(define-fun im ((x (_ BitVec 64)) (y (_ BitVec 64)) (z (_ BitVec 64))) (_ BitVec 64) (ite (= x #x0000000000000001) y z))
(synth-fun f ((x (_ BitVec 64))) (_ BitVec 64)
    ((Start (_ BitVec 64)))
    ((Start (_ BitVec 64) (#x0000000000000000 #x0000000000000001 x (bvnot Start) (smol Start) (ehad Start) (arba Start) (shesh Start) (bvand Start Start) (bvor Start Start) (bvxor Start Start) (bvadd Start Start) (im Start Start Start)))))
(constraint (= (f #x75e509ca0daa0eba) #x8a1af635f255f145))
(constraint (= (f #x50de71118caee533) #xaf218eee73511acd))
(constraint (= (f #x5a121e177e8adeed) #xf5a121e177e8adee))
(constraint (= (f #x1479eb1c58d3ecaa) #xeb8614e3a72c1355))
(constraint (= (f #x6a3e755c7c40b058) #x95c18aa383bf4fa7))
(constraint (= (f #x6128834b0bd317d3) #x9ed77cb4f42ce82d))
(constraint (= (f #x8a189647ddccb2a2) #x75e769b822334d5d))
(constraint (= (f #xc58d280e115ed561) #xfc58d280e115ed56))
(constraint (= (f #xe46445dadc531133) #x1b9bba2523aceecd))
(constraint (= (f #xcc9e4c59bb9eb1ae) #x3361b3a644614e51))
(constraint (= (f #xd24ea113281e9ad4) #x2db15eecd7e1652b))
(constraint (= (f #x8ea991c89e51c595) #x71566e3761ae3a6b))
(constraint (= (f #x6c0d2b1d3e298ebd) #x93f2d4e2c1d67143))
(constraint (= (f #xa316e775827d65b1) #x5ce9188a7d829a4f))
(constraint (= (f #xc7c9a2d84be068a5) #xfc7c9a2d84be068a))
(constraint (= (f #x035848809c4b2bee) #xfca7b77f63b4d411))
(constraint (= (f #x940a597972e61288) #x6bf5a6868d19ed77))
(constraint (= (f #x71aa3189dad1aab7) #x8e55ce76252e5549))
(constraint (= (f #xc4226eb8edeec86e) #x3bdd914712113791))
(constraint (= (f #x0e6ebd46e00e9b43) #xf0e6ebd46e00e9b4))
(constraint (= (f #x888d4abcb0c6bde6) #x7772b5434f394219))
(constraint (= (f #x8185714bcce1e4ae) #x7e7a8eb4331e1b51))
(constraint (= (f #x80aeee61a97d62e2) #x7f51119e56829d1d))
(constraint (= (f #x558ea10120ed5c62) #xaa715efedf12a39d))
(constraint (= (f #x704d8cecb508192d) #xf704d8cecb508192))
(constraint (= (f #x5431a2dbacb87bb4) #xabce5d245347844b))
(constraint (= (f #xbe40e87079b366e3) #xfbe40e87079b366e))
(constraint (= (f #x907e8d194ae0e6d0) #x6f8172e6b51f192f))
(constraint (= (f #xed9000973a832259) #x126fff68c57cdda7))
(constraint (= (f #x7d3602e30b20c171) #x82c9fd1cf4df3e8f))
(constraint (= (f #x71e454464908d89e) #x8e1babb9b6f72761))
(constraint (= (f #xe6b78581ee34c7cb) #xfe6b78581ee34c7c))
(constraint (= (f #xe849446cbb9d0e1d) #x17b6bb934462f1e3))
(constraint (= (f #xec18e0b7c591a8ea) #x13e71f483a6e5715))
(constraint (= (f #x7061ec623d899b9e) #x8f9e139dc2766461))
(constraint (= (f #x4d604ae19e7a5d57) #xb29fb51e6185a2a9))
(constraint (= (f #x73b3d651c67bd9ce) #x8c4c29ae39842631))
(constraint (= (f #xe9cb19c7379cc024) #x1634e638c8633fdb))
(constraint (= (f #xa3ae5a34405c1dd5) #x5c51a5cbbfa3e22b))
(constraint (= (f #x649759d1d58ee57b) #x9b68a62e2a711a85))
(constraint (= (f #x1cee05d2e18664ae) #xe311fa2d1e799b51))
(constraint (= (f #x96251bb9ca5e7513) #x69dae44635a18aed))
(constraint (= (f #x777e495a0c80e6c3) #xf777e495a0c80e6c))
(constraint (= (f #xec06829a273b772e) #x13f97d65d8c488d1))
(constraint (= (f #x93e01243c5c42117) #x6c1fedbc3a3bdee9))
(constraint (= (f #x7d69e0dc6848bae6) #x82961f2397b74519))
(constraint (= (f #x77ebc8e3942aac48) #x8814371c6bd553b7))
(constraint (= (f #x286012c1aa381944) #xd79fed3e55c7e6bb))
(constraint (= (f #x74549db5e67e084b) #xf74549db5e67e084))
(constraint (= (f #x4bdd39d05687310e) #xb422c62fa978cef1))
(constraint (= (f #x8e50853c9c971cd6) #x71af7ac36368e329))
(constraint (= (f #x624404aeee0ee6a1) #xf624404aeee0ee6a))
(constraint (= (f #x260e81a88eb7be58) #xd9f17e57714841a7))
(constraint (= (f #x876ee1da8d04d211) #x78911e2572fb2def))
(constraint (= (f #x19b445cb82152ce0) #xe64bba347dead31f))
(constraint (= (f #xebe491e5eed284e3) #xfebe491e5eed284e))
(constraint (= (f #xdcb450e2bcd91420) #x234baf1d4326ebdf))
(constraint (= (f #xd8d40e04306a397e) #x272bf1fbcf95c681))
(constraint (= (f #xb8dcee7b1766dbbe) #x47231184e8992441))
(constraint (= (f #x2053a8b34054638c) #xdfac574cbfab9c73))
(constraint (= (f #xd4212272ee8e57ed) #xfd4212272ee8e57e))
(constraint (= (f #x4ce8ce7715de1d4a) #xb3173188ea21e2b5))
(constraint (= (f #x8181c1ed1bc759c8) #x7e7e3e12e438a637))
(constraint (= (f #x158b270944a8a694) #xea74d8f6bb57596b))
(constraint (= (f #x8e6ae9b24db63e80) #x7195164db249c17f))
(constraint (= (f #x40475be57e05c9c2) #xbfb8a41a81fa363d))
(constraint (= (f #xb25d9337403e1c53) #x4da26cc8bfc1e3ad))
(constraint (= (f #xa201b3d89037785b) #x5dfe4c276fc887a5))
(constraint (= (f #x1ee8392475d2c4b4) #xe117c6db8a2d3b4b))
(constraint (= (f #x4de17e0687185ad7) #xb21e81f978e7a529))
(constraint (= (f #xe28e72e140917d4c) #x1d718d1ebf6e82b3))
(constraint (= (f #xe06754972462857c) #x1f98ab68db9d7a83))
(constraint (= (f #x78c33c687b72be19) #x873cc397848d41e7))
(constraint (= (f #xab7e16e3e34e9e7d) #x5481e91c1cb16183))
(constraint (= (f #xa4072e786158ab3e) #x5bf8d1879ea754c1))
(constraint (= (f #xee75e4b1ac6cd283) #xfee75e4b1ac6cd28))
(constraint (= (f #x2eaac26d7c0970a5) #xf2eaac26d7c0970a))
(constraint (= (f #xa9ed9312ad8e4eb2) #x56126ced5271b14d))
(constraint (= (f #xc0c4584bbc5a23b8) #x3f3ba7b443a5dc47))
(constraint (= (f #x8946dce1c445e8bd) #x76b9231e3bba1743))
(constraint (= (f #xd3d6e6aea7b77744) #x2c291951584888bb))
(constraint (= (f #x2825be038d483eae) #xd7da41fc72b7c151))
(constraint (= (f #x76e0122d05611233) #x891fedd2fa9eedcd))
(constraint (= (f #x69d352e65be9eed1) #x962cad19a416112f))
(constraint (= (f #x9ebe3eeccc94a2c7) #xf9ebe3eeccc94a2c))
(constraint (= (f #x76bc41b1d4aa0143) #xf76bc41b1d4aa014))
(constraint (= (f #x86671339ece380c5) #xf86671339ece380c))
(constraint (= (f #x3eb28a0e750bc744) #xc14d75f18af438bb))
(constraint (= (f #x05e48a5ca1dae69e) #xfa1b75a35e251961))
(constraint (= (f #x97de73977c515b28) #x68218c6883aea4d7))
(constraint (= (f #x09819959d3bc9c83) #xf09819959d3bc9c8))
(constraint (= (f #xdd183543ec282ed1) #x22e7cabc13d7d12f))
(constraint (= (f #x76170471de77d64b) #xf76170471de77d64))
(constraint (= (f #x46e781c6c086d430) #xb9187e393f792bcf))
(constraint (= (f #x7c77161e42948238) #x8388e9e1bd6b7dc7))
(constraint (= (f #x72168da4ad0ebdbd) #x8de9725b52f14243))
(constraint (= (f #x73d1a2ac5bece421) #xf73d1a2ac5bece42))
(constraint (= (f #xad47cb9e765dd8ee) #x52b8346189a22711))
(constraint (= (f #x9da15b89669e3427) #xf9da15b89669e342))
(constraint (= (f #x0c04a0ba62224386) #xf3fb5f459dddbc79))
(check-synth)
