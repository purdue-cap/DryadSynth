(set-logic BV)
(define-fun ehad ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000001))
(define-fun arba ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000004))
(define-fun shesh ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000010))
(define-fun smol ((x (_ BitVec 64))) (_ BitVec 64) (bvshl x #x0000000000000001))
(define-fun im ((x (_ BitVec 64)) (y (_ BitVec 64)) (z (_ BitVec 64))) (_ BitVec 64) (ite (= x #x0000000000000001) y z))
(synth-fun f ((x (_ BitVec 64))) (_ BitVec 64)
    ((Start (_ BitVec 64)))
    ((Start (_ BitVec 64) (#x0000000000000000 #x0000000000000001 x (bvnot Start) (smol Start) (ehad Start) (arba Start) (shesh Start) (bvand Start Start) (bvor Start Start) (bvxor Start Start) (bvadd Start Start) (im Start Start Start)))))
(constraint (= (f #x0c0d2aab34e291bd) #x242780019ea7b537))
(constraint (= (f #x8da44c4e87676ed5) #xa8ece4eb96364c7f))
(constraint (= (f #x8d5ab782c2097ce9) #x3fffff0f8c37fbf4))
(constraint (= (f #xdc0c4e87c4054e7a) #x9424eb974c0feb6e))
(constraint (= (f #x95d48ae8c25e96e3) #x7ffb3ff38dff7fcc))
(constraint (= (f #x1ea3b28d7942bc3b) #x5beb17a86bc834b1))
(constraint (= (f #x078702499a80c632) #x169506dccf825296))
(constraint (= (f #xaace09b0ba0acee6) #xffbc37e3fc3fbfde))
(constraint (= (f #x62c7c08b7d416e3d) #x285741a277c44ab7))
(constraint (= (f #x9be640025d59e205) #x7fdd800dfff7cc1c))
(constraint (= (f #xc533e85c4e1e715d) #x4f9bb914ea5b5417))
(constraint (= (f #x7ede4e98329ec256) #x7c9aebc897dc4702))
(constraint (= (f #x928cb453ab1eec83) #x6f3bf9effe7ffb0c))
(constraint (= (f #x3407e475e8393417) #x9c17ad61b8ab9c45))
(constraint (= (f #x83ac93e9ebabc529) #x0ffb6ff7ffff9ef4))
(constraint (= (f #x5c36bc7e1c4147dd) #x14a4357a54c3d797))
(constraint (= (f #x6379cb33da7c4c9e) #x2a6d619b8f74e5da))
(constraint (= (f #xb8e9dde0473e294d) #xf3f7ffc19efcf7bc))
(constraint (= (f #x97739025a324e554) #xc65ab070e96eaffc))
(constraint (= (f #x3e9cad75de6ede8e) #xff7bfffffdffff3e))
(constraint (= (f #x15be6a3eb4e350e5) #x7ffdfcfffbcfe3dc))
(constraint (= (f #x4a5668481b2b78b2) #xdf0338d851826a16))
(constraint (= (f #xc7259dd14dc99319) #x5570d973e95cb94b))
(constraint (= (f #x39a3317b5c4a73a1) #xf7cee7fff9bdefc4))
(constraint (= (f #x913e4e2ad3e2d75e) #xb3baea807ba8861a))
(constraint (= (f #x36eed548cc205073) #xa4cc7fda6460f159))
(constraint (= (f #x1ac0c2db3cdd54e1) #x7f838ffefbfffbc4))
(constraint (= (f #xee589ab7b98a40e2) #xfdf37ffff73d83ce))
(constraint (= (f #x543e5ec31e98823a) #xfcbb1c495bc986ae))
(constraint (= (f #xd1e820023443729d) #x75b860069cca57d7))
(constraint (= (f #xa47eb36b07e94326) #xd9ffeffe1ff78ede))
(constraint (= (f #x65ab4ac5ee926e22) #xdfffbf9fff6dfcce))
(constraint (= (f #x8dbe8e5b19b29b76) #xa93bab114d17d262))
(constraint (= (f #x31c845aa3ac08395) #x9558d0feb0418abf))
(constraint (= (f #x4b99d75145720a78) #xe2cd85f3d0561f68))
(constraint (= (f #xbb9178b0eebe936a) #xff67f3e3ffff6ffe))
(constraint (= (f #xc9ac5ed116547483) #xb7f9ffe67df9fb0c))
(constraint (= (f #xe7e76811c7cc5260) #xdfdff0679fb9edc2))
(constraint (= (f #x50ed80263aee0be5) #xe3ff00dcfffc3fdc))
(constraint (= (f #x8a18e8b314dea27e) #x9e4aba193e9be77a))
(constraint (= (f #x50699e8151246eba) #xf13cdb83f36d4c2e))
(constraint (= (f #x37ad3d1642eee9b9) #xa707b742c8ccbd2b))
(constraint (= (f #xcb233812770c89e6) #xbecef06dfe3b37de))
(constraint (= (f #x1ec2aa48aeb078a5) #x7f8ffdb3ffe1f3dc))
(constraint (= (f #xd9002e7e8bacebbe) #x8b008b7ba306c33a))
(constraint (= (f #xe8c515110047b804) #xf39e7e66019ff01a))
(constraint (= (f #xb0a49377adabad9a) #x11edba67090308ce))
(constraint (= (f #xcc78d7b339055027) #xb9f3ffeef61fe0dc))
(constraint (= (f #xa5e08e514c9aa0e1) #xdfc33de7bb7fc3c4))
(constraint (= (f #x8382616c0be9c316) #x8a87244423bd4942))
(constraint (= (f #xb78b57ee573232b0) #x26a207cb05969810))
(constraint (= (f #x6c40450eac5340ee) #xf9819e3ff9ef83fe))
(constraint (= (f #x8991bd8a947c39c3) #x3767ff3f79f8f78c))
(constraint (= (f #x855081ebecb7611e) #x8ff185c3c626235a))
(constraint (= (f #x2aee2337be9b9a10) #x80ca69a73bd2ce30))
(constraint (= (f #xa7edad0c6db4ebde) #xf7c90725491ec39a))
(constraint (= (f #x410315abd79a5030) #xc309410386cef090))
(constraint (= (f #x2dedab2d15da0ce6) #xfffffefe7ffc3bde))
(constraint (= (f #x0741e3e594b03e02) #x1f87cfdf7be0fc0e))
(constraint (= (f #x6baaddac6eedd449) #xfffffff9fffff9b4))
(constraint (= (f #x2a25194aecc17cc9) #xfcde77bffb87fbb4))
(constraint (= (f #x2cd77162e76dd23e) #x86865428b64976ba))
(constraint (= (f #x0eee6257e95139c7) #x3ffdcdfff7e6f79c))
(constraint (= (f #xbdeabe96c692152d) #xffffff7f9f6c7efc))
(constraint (= (f #xaeb744474b08843d) #x0c25ccd5e1198cb7))
(constraint (= (f #xac2ab1288b8e2bc3) #xf8ffe6f33f3cff8c))
(constraint (= (f #xc203b9592d903b67) #x8c0ff7f6ff60ffdc))
(constraint (= (f #x810957ad3ccaa644) #x0637fffefbbfdd9a))
(constraint (= (f #x1c618719b7c1abe6) #x79c71e77ff87ffde))
(constraint (= (f #x80193758393cee5a) #x804ba608abb6cb0e))
(constraint (= (f #xee5b669c2872e635) #xcb1233d47958b29f))
(constraint (= (f #x300466622cd43610) #x900d3326867ca230))
(constraint (= (f #xa6290442361a112a) #xdcf6198cfc7c66fe))
(constraint (= (f #xa712985d1326185b) #xf537c91739724911))
(constraint (= (f #x7795130a1426aa79) #x66bf391e3c73ff6b))
(constraint (= (f #xb1edae83952a0079) #x15c90b8abf7e016b))
(constraint (= (f #xe12e194e4e53430a) #xc6fc77bdbdef8e3e))
(constraint (= (f #xc6ee2aa355dde063) #x9ffcffcfffffc1cc))
(constraint (= (f #x7cb0518ed70a9be3) #xfbe1e73ffe3f7fcc))
(constraint (= (f #x6e0873211de13c59) #x4a19596359a3b50b))
(constraint (= (f #x476be3e41117507e) #xd643abac3345f17a))
(constraint (= (f #xe72e1b92084dd167) #xdefc7f6c31bfe7dc))
(constraint (= (f #xa026663e17e42840) #xc0dddcfc7fd8f182))
(constraint (= (f #xe811d465cc9b74dd) #xb8357d3165d25e97))
(constraint (= (f #xe02651ede17be8ee) #xc0dde7ffc7fff3fe))
(constraint (= (f #x7eb73beeb85c3b59) #x7c25b3cc2914b20b))
(constraint (= (f #x25eed0d7e87d0a42) #xdfffe3fff1fe3d8e))
(constraint (= (f #x502c069a6cc81617) #xf08413cf46584245))
(constraint (= (f #x243e50e81e3780ac) #xd8fde3f07cff03fa))
(constraint (= (f #xa7963c885ce13ee2) #xdf7cfb31fbc6ffce))
(constraint (= (f #x1edca9b7eb5a0110) #x5c95fd27c20e0330))
(constraint (= (f #x7e3da0940344bd8c) #xfcffc3780f9bff3a))
(constraint (= (f #xb1c124145d2d1e7c) #x15436c3d17875b74))
(constraint (= (f #xb806ea6784913ce8) #xf01ffddf1b66fbf2))
(constraint (= (f #x714123102a2d8e31) #x53c369307e88aa93))
(constraint (= (f #x11ca163be83e6c89) #x67bc7cfff0fdfb34))
(constraint (= (f #x6138d905ebcdee20) #xc6f3f61fffbffcc2))
(constraint (= (f #xd82d1e0cd54cb528) #xf0fe7c3bffbbfef2))
(constraint (= (f #x227cbe6a033a24e6) #xcdfbfdfc0efcdbde))
(constraint (= (f #xd210bbd916425d37) #x7632338b42c717a5))
(check-synth)
