(set-logic BV)
(define-fun ehad ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000001))
(define-fun arba ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000004))
(define-fun shesh ((x (_ BitVec 64))) (_ BitVec 64) (bvlshr x #x0000000000000010))
(define-fun smol ((x (_ BitVec 64))) (_ BitVec 64) (bvshl x #x0000000000000001))
(define-fun im ((x (_ BitVec 64)) (y (_ BitVec 64)) (z (_ BitVec 64))) (_ BitVec 64) (ite (= x #x0000000000000001) y z))
(synth-fun f ((x (_ BitVec 64))) (_ BitVec 64)
    ((Start (_ BitVec 64)))
    ((Start (_ BitVec 64) (#x0000000000000000 #x0000000000000001 x (bvnot Start) (smol Start) (ehad Start) (arba Start) (shesh Start) (bvand Start Start) (bvor Start Start) (bvxor Start Start) (bvadd Start Start) (im Start Start Start)))))
(constraint (= (f #x6c494ee123767de9) #x276d623db913042e))
(constraint (= (f #x376e3077aec1e188) #x91239f10a27c3cec))
(constraint (= (f #x3d3307c73d41615d) #x8599f071857d3d46))
(constraint (= (f #x84c316c57b374d5e) #x42618b62bd9ba6af))
(constraint (= (f #xd0ee81b39c65eadd) #x5e22fc98c7342a46))
(constraint (= (f #x4143b7051ba1e42b) #x20a1db828dd0f216))
(constraint (= (f #x484010adde560967) #x24200856ef2b04b4))
(constraint (= (f #xde84ee03a010a431) #x42f623f8bfdeb79e))
(constraint (= (f #xe9302876ceea5e07) #x7498143b67752f04))
(constraint (= (f #x2cd828e762ecc7c8) #xa64fae313a26706c))
(constraint (= (f #xe674674bcc3a995b) #x733a33a5e61d4cae))
(constraint (= (f #x4926a2c323463b67) #x2493516191a31db4))
(constraint (= (f #x264dd15820bc8929) #xb3645d4fbe86edae))
(constraint (= (f #x2adea2ac653641b2) #x156f5156329b20d9))
(constraint (= (f #x382ced5a55c347b2) #x1c1676ad2ae1a3d9))
(constraint (= (f #x9b268c3ea2115156) #x4d93461f5108a8ab))
(constraint (= (f #x8c7c5ded6049ec3a) #x463e2ef6b024f61d))
(constraint (= (f #xbc37b1ae3d97e3d3) #x5e1bd8d71ecbf1ea))
(constraint (= (f #x88ad838cabe0e64c) #xeea4f8e6a83e3364))
(constraint (= (f #xeebe3490e1215e77) #x775f1a487090af3c))
(constraint (= (f #xe164e1da4c870da8) #x3d363c4b66f1e4ac))
(constraint (= (f #x8338c2320628647e) #x419c61190314323f))
(constraint (= (f #x74b5c6404038ca14) #x1694737f7f8e6bd4))
(constraint (= (f #x6eca7ca9b425e503) #x37653e54da12f282))
(constraint (= (f #xeca0d89141375777) #x76506c48a09babbc))
(constraint (= (f #x1b1eaac18e75878e) #x0d8f5560c73ac3c7))
(constraint (= (f #x25ea64803511ec53) #x12f532401a88f62a))
(constraint (= (f #x95254b790bee64e0) #xd5b5690de823363c))
(constraint (= (f #x3bae7ae02e4c527e) #x1dd73d701726293f))
(constraint (= (f #x1014c2ba8b323419) #xdfd67a8ae99b97ce))
(constraint (= (f #x2b839112b5a3833e) #x15c1c8895ad1c19f))
(constraint (= (f #xd64e9ee9dcad6e78) #x5362c22c46a5230c))
(constraint (= (f #xeab80a78c818c403) #x755c053c640c6202))
(constraint (= (f #x550a1c70e1dea9ee) #x2a850e3870ef54f7))
(constraint (= (f #x1a71e81280671664) #xcb1c2fdaff31d334))
(constraint (= (f #xc2dc295bd303ce6e) #x616e14ade981e737))
(constraint (= (f #x2089b3753a3e622c) #xbeec99158b833ba4))
(constraint (= (f #x5bdd68cc28e138a2) #x2deeb46614709c51))
(constraint (= (f #x310d423176ece7de) #x1886a118bb7673ef))
(constraint (= (f #x19ccee87545b0713) #x0ce67743aa2d838a))
(constraint (= (f #x4ebeca6dc96abeae) #x275f6536e4b55f57))
(constraint (= (f #x8cddc1217784e54b) #x466ee090bbc272a6))
(constraint (= (f #xe28ae41e1d970178) #x3aea37c3c4d1fd0c))
(constraint (= (f #x6414c7912d40a66a) #x320a63c896a05335))
(constraint (= (f #x929bdd5841eb4e84) #xdac8454f7c2962f4))
(constraint (= (f #xc90ae7aba4a77eca) #x648573d5d253bf65))
(constraint (= (f #xea72bba228b4c1a0) #x2b1a88bbae967cbc))
(constraint (= (f #x331e8ea9498d6ebc) #x99c2e2ad6ce52284))
(constraint (= (f #xd9725ab4db4e52a7) #x6cb92d5a6da72954))
(constraint (= (f #x000865d9dd778cb4) #xffef344c4510e694))
(constraint (= (f #x769cdd74c11c6e33) #x3b4e6eba608e371a))
(constraint (= (f #x2bd199c0e036d6aa) #x15e8cce0701b6b55))
(constraint (= (f #x8115a34ae2aee6e0) #xfdd4b96a3aa2323c))
(constraint (= (f #xb3ecea460617e9c7) #x59f67523030bf4e4))
(constraint (= (f #xea78b54ce847e807) #x753c5aa67423f404))
(constraint (= (f #x0b03e9136e4be54b) #x0581f489b725f2a6))
(constraint (= (f #x9e546e610789acae) #x4f2a373083c4d657))
(constraint (= (f #xc944869ad08b1ebb) #x64a2434d68458f5e))
(constraint (= (f #xe404db8ec1333361) #x37f648e27d99993e))
(constraint (= (f #xa821d73152b068ec) #xafbc519d5a9f2e24))
(constraint (= (f #x289e8cded133d4d0) #xaec2e6425d98565c))
(constraint (= (f #x134be038d33b03e7) #x09a5f01c699d81f4))
(constraint (= (f #xb8849995a7de9816) #x5c424ccad3ef4c0b))
(constraint (= (f #x3ce9d7b924dc8b4a) #x1e74ebdc926e45a5))
(constraint (= (f #x1aeb7d5b1ab9bb66) #x0d75bead8d5cddb3))
(constraint (= (f #xc8c437432157b495) #x6e779179bd5096d6))
(constraint (= (f #xb4101ce889d1c109) #x97dfc62eec5c7dee))
(constraint (= (f #xe893e4e9dc0e4ece) #x7449f274ee072767))
(constraint (= (f #xe670abb47beb4ee0) #x331ea8970829623c))
(constraint (= (f #xed59703e5825ed51) #x254d1f834fb4255e))
(constraint (= (f #x4aa9989767d318ac) #x6aacced13059cea4))
(constraint (= (f #x9756a76e0702e83b) #x4bab53b70381741e))
(constraint (= (f #x060ae7bea39ee7c5) #xf3ea3082b8c23076))
(constraint (= (f #x9e22eee808613808) #xc3ba222fef3d8fec))
(constraint (= (f #xdae66c47ea60e88e) #x6d733623f5307447))
(constraint (= (f #x1338a25e3ae6a446) #x099c512f1d735223))
(constraint (= (f #x197c22595a7e10d3) #x0cbe112cad3f086a))
(constraint (= (f #xa8632e2c3d702c65) #xaf39a3a7851fa736))
(constraint (= (f #x19126adc29ece992) #x0c89356e14f674c9))
(constraint (= (f #xdc9eaec6903758e3) #x6e4f5763481bac72))
(constraint (= (f #x879dbd28505bed83) #x43cede94282df6c2))
(constraint (= (f #x29cbe3a239ebbcc1) #xac6838bb8c28867e))
(constraint (= (f #x73dd61e5676811e0) #x18453c35312fdc3c))
(constraint (= (f #x4dca3ee48094e383) #x26e51f72404a71c2))
(constraint (= (f #xe1d8570ae0d319e0) #x3c4f51ea3e59cc3c))
(constraint (= (f #xcd194845569c55ea) #x668ca422ab4e2af5))
(constraint (= (f #x353e34be24736131) #x95839683b7193d9e))
(constraint (= (f #xe3e7d4985d7820ed) #x383056cf450fbe26))
(constraint (= (f #x5c281604709b11e8) #x47afd3f71ec9dc2c))
(constraint (= (f #x2c68c1e10177e0b8) #xa72e7c3dfd103e8c))
(constraint (= (f #x3ea0c84676812c1b) #x1f5064233b40960e))
(constraint (= (f #xe436ee6eb7e92b88) #x37922322902da8ec))
(constraint (= (f #x0a2aed83ac9b9dee) #x051576c1d64dcef7))
(constraint (= (f #xde0e18ec19e4664e) #x6f070c760cf23327))
(constraint (= (f #xae89c57461803321) #xa2ec75173cff99be))
(constraint (= (f #x89b99a3c2e2ad6e6) #x44dccd1e17156b73))
(constraint (= (f #xb41e3899a9444eed) #x97c38eccad776226))
(constraint (= (f #xce1553a5e37cbde8) #x63d558b43906842c))
(constraint (= (f #x0bd086a1d370e64c) #xe85ef2bc591e3364))
(constraint (= (f #xe9bc6d3ee30635c8) #x2c87258239f3946c))
(check-synth)
