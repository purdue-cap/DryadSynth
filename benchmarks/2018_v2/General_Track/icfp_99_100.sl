(set-logic BV)
(define-fun shr1 ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000001))
(define-fun shr4 ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000004))
(define-fun shr16 ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000010))
(define-fun shl1 ((x (_ BitVec 64))) (_ BitVec 64) (bvshl x #x0000000000000001))
(define-fun if0 ((x (_ BitVec 64)) (y (_ BitVec 64)) (z (_ BitVec 64))) (_ BitVec 64) (ite (= x #x0000000000000001) y z))
(synth-fun f ((x (_ BitVec 64))) (_ BitVec 64)
    ((Start (_ BitVec 64)))
    ((Start (_ BitVec 64) (#x0000000000000000 #x0000000000000001 x (bvnot Start) (shl1 Start) (shr1 Start) (shr4 Start) (shr16 Start) (bvand Start Start) (bvor Start Start) (bvxor Start Start) (bvadd Start Start) (if0 Start Start Start)))))
(constraint (= (f #x6aeac7e04407dbc8) #x6aeace8ef085e008))
(constraint (= (f #xa76c0b166d05450a) #xa76c158d2db6abda))
(constraint (= (f #x5b64ace2eab7e15b) #x0000000000000000))
(constraint (= (f #x26d26ec23cd48120) #x26d2712f63c0a4ed))
(constraint (= (f #xa588512b7caa625d) #x0000000000000000))
(constraint (= (f #xc7ae5ac750beb769) #xc7ae5ac750beb769))
(constraint (= (f #x91eee7a069287313) #x0000000000000000))
(constraint (= (f #x04ec3b5b27751c63) #x04ec3b5b27751c63))
(constraint (= (f #x32c4685c405cba7c) #x0000000000000000))
(constraint (= (f #x76709abe6d95ce7c) #x0000000000000000))
(constraint (= (f #x2a722031763a1aa5) #x2a722031763a1aa5))
(constraint (= (f #x76e16897cd22aeb4) #x0000000000000000))
(constraint (= (f #x0be4729e5204ee70) #x0000000000000000))
(constraint (= (f #x960d894720de6370) #x0000000000000000))
(constraint (= (f #xd82b77272b9b4e9d) #x0000000000000000))
(constraint (= (f #xd1ae7b4a0cd6b3e3) #xd1ae7b4a0cd6b3e3))
(constraint (= (f #xe5ee9c2472638c01) #xe5ee9c2472638c01))
(constraint (= (f #xaeb7532eddbde58e) #xaeb75e1a52f0d369))
(constraint (= (f #x0c39e0846eb46c6d) #x0c39e0846eb46c6d))
(constraint (= (f #xa55e33e2239a28eb) #xa55e33e2239a28eb))
(constraint (= (f #x07e56e3a98ae8a3a) #x0000000000000000))
(constraint (= (f #x2d5486179783b513) #x0000000000000000))
(constraint (= (f #xcd07c167700d0e42) #xcd07ce37ec238542))
(constraint (= (f #x1ea83eadb37eccc1) #x1ea83eadb37eccc1))
(constraint (= (f #xdb0388eced6d24d7) #x0000000000000000))
(constraint (= (f #xc9db211b664e24be) #x0000000000000000))
(constraint (= (f #x1bd8e6117e9955b6) #x0000000000000000))
(constraint (= (f #xeace3c5d629d3db2) #x0000000000000000))
(constraint (= (f #xe580146121a3929e) #x0000000000000000))
(constraint (= (f #x5246e85bc14e6890) #x0000000000000000))
(constraint (= (f #x385bbd214d98bc9a) #x0000000000000000))
(constraint (= (f #x834a29be427c2c1b) #x0000000000000000))
(constraint (= (f #xe107b533e0dce1ad) #xe107b533e0dce1ad))
(constraint (= (f #xecbd003a6e4e98de) #x0000000000000000))
(constraint (= (f #x61b0ee5310eb2d5c) #x0000000000000000))
(constraint (= (f #x184691c32d43d1b3) #x0000000000000000))
(constraint (= (f #xe0984eebb714d7ec) #xe0985cf53c03935d))
(constraint (= (f #x37be283a2ee32e8d) #x37be283a2ee32e8d))
(constraint (= (f #x2843e9c830ae0e82) #x2843ec4c6f4a918c))
(constraint (= (f #xa5aca99e6d0d0128) #xa5acb3f937a6e7f8))
(constraint (= (f #x8ed24248be260b57) #x0000000000000000))
(constraint (= (f #x5e9da375374a7986) #x5e9da95f1181ccfa))
(constraint (= (f #xeeeb7290e01b2455) #x0000000000000000))
(constraint (= (f #x620b63d516b7b2e0) #x620b69f5ccf5044b))
(constraint (= (f #xe92876439db59e92) #x0000000000000000))
(constraint (= (f #x7c07de786923265a) #x0000000000000000))
(constraint (= (f #x167a7be0360da99b) #x0000000000000000))
(constraint (= (f #x22de9d405dd35a78) #x0000000000000000))
(constraint (= (f #x3ee745a340216eca) #x3ee74991b47ba2cc))
(constraint (= (f #x51eebc2ab3071ed7) #x0000000000000000))
(constraint (= (f #xc8e001116de76363) #xc8e001116de76363))
(constraint (= (f #xb4377c69ee6b9b9b) #x0000000000000000))
(constraint (= (f #x390c41a98a3ccb20) #x390c453a4e5763c3))
(constraint (= (f #x723eedab0de45842) #x723ef4cefcbf0920))
(constraint (= (f #xd7488213ceeee445) #xd7488213ceeee445))
(constraint (= (f #xa27e70866d621350) #x0000000000000000))
(constraint (= (f #x3ae489e3cd3abc12) #x0000000000000000))
(constraint (= (f #x1e5100911de71a0c) #x1e5102762df02bea))
(constraint (= (f #x4244cd11a432a6e0) #x4244d135f103c123))
(constraint (= (f #x48cea9d832c9ea14) #x0000000000000000))
(constraint (= (f #x7998e379eaea9ed7) #x0000000000000000))
(constraint (= (f #xc60a2ee860797dce) #xc60a3b49036803d5))
(constraint (= (f #x50a54b4dd6582d3c) #x0000000000000000))
(constraint (= (f #x5cb737d72a73589e) #x0000000000000000))
(constraint (= (f #xbdc6e80701d4e356) #x0000000000000000))
(constraint (= (f #x2410560588416ba6) #x241058468da1c42a))
(constraint (= (f #x8dadec459ea9e054) #x0000000000000000))
(constraint (= (f #xaee3cb130b23c4c0) #xaee3d60147d4f572))
(constraint (= (f #x746a1dcd35215a41) #x746a1dcd35215a41))
(constraint (= (f #x329e30e82eb9e6a9) #x329e30e82eb9e6a9))
(constraint (= (f #x2226a9e694e77173) #x0000000000000000))
(constraint (= (f #x85c02d0ac00d4a07) #x85c02d0ac00d4a07))
(constraint (= (f #xd77aee73e4158c12) #x0000000000000000))
(constraint (= (f #x56d63303d1cd44d8) #x0000000000000000))
(constraint (= (f #xbc0ede10d8887597) #x0000000000000000))
(constraint (= (f #xe95adda0e39c8e7d) #x0000000000000000))
(constraint (= (f #xa2790e152b9cb85e) #x0000000000000000))
(constraint (= (f #xcce5a736a2e09ae5) #xcce5a736a2e09ae5))
(constraint (= (f #x9da2e5b9ea8e5c5e) #x0000000000000000))
(constraint (= (f #x58c5c5d585554763) #x58c5c5d585554763))
(constraint (= (f #x89eb7b3c81bacb01) #x89eb7b3c81bacb01))
(constraint (= (f #xed99e3764ee39e5a) #x0000000000000000))
(constraint (= (f #xe500eab5c17eb5b0) #x0000000000000000))
(constraint (= (f #x7c177cedbe53e320) #x7c1784af3622bf05))
(constraint (= (f #xc74d186842594e84) #xc74d24dd13dfd2a9))
(constraint (= (f #x00ec2c62be7b4e7e) #x0000000000000000))
(constraint (= (f #x6034129769c2e9ae) #x6034189aaaec604a))
(constraint (= (f #xc94158bde7b2dcce) #xc9416551fd3ebb49))
(constraint (= (f #x341dc9e433947a21) #x341dc9e433947a21))
(constraint (= (f #xaa46c6717356e586) #xaa46d115dfbdfcbb))
(constraint (= (f #xa15c059bd25c841e) #x0000000000000000))
(constraint (= (f #x86b5ce9e22b69155) #x0000000000000000))
(constraint (= (f #x1ee9e1cceedd3b96) #x0000000000000000))
(constraint (= (f #xea744bd74d1e4829) #xea744bd74d1e4829))
(constraint (= (f #xb8eebeda96e8417c) #x0000000000000000))
(constraint (= (f #xc19aac9c5ee5c0de) #x0000000000000000))
(constraint (= (f #x0de9cdcca0caed60) #x0de9ceab3da7b76c))
(constraint (= (f #xd54290bc1219c4d2) #x0000000000000000))
(constraint (= (f #xe5a8eec8e9e8d6be) #x0000000000000000))
(constraint (= (f #x27d7e26cebd19a6c) #x27d7e4ea69f86929))
(check-synth)
