@0xdadadedadeded00d;

struct SygusProblem {
  logic       @0 :Text;
  functions   @1 :List(FunctionDecl);
  constraints @2 :List(Constraint);
}

struct FunctionDecl {
  name     @0 :Text;
  params   @1 :List(Var);
  retType  @2 :Text;
  grammar  @3 :Text;
}

struct Constraint {
  expr     @0 :Text;
}

struct Var {
  name     @0 :Text;
  sort     @1 :Text;
}

struct SygusSolution {
  funcs    @0 :List(FuncDef);
}

struct FuncDef {
  name     @0 :Text;
  body     @1 :Text;
}




