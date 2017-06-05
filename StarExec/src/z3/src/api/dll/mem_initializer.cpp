// Automatically generated file.
#include"debug.h"
#include"gparams.h"
#include"prime_generator.h"
#include"rational.h"
#include"symbol.h"
#include"trace.h"
void mem_initialize() {
rational::initialize();
initialize_symbols();
gparams::init();
}
void mem_finalize() {
finalize_debug();
gparams::finalize();
prime_iterator::finalize();
rational::finalize();
finalize_symbols();
finalize_trace();
}
