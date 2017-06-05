// Automatically generated file
#ifdef _CYGWIN
typedef long long __int64;
#endif
#include<jni.h>
#include<stdlib.h>
#include"z3.h"
#ifdef __cplusplus
extern "C" {
#endif

#ifdef __GNUC__
#if __GNUC__ >= 4
#define DLL_VIS __attribute__ ((visibility ("default")))
#else
#define DLL_VIS
#endif
#else
#define DLL_VIS
#endif

#if defined(_M_X64) || defined(_AMD64_)

#define GETLONGAELEMS(T,OLD,NEW)                                   \
  T * NEW = (OLD == 0) ? 0 : (T*) jenv->GetLongArrayElements(OLD, NULL);
#define RELEASELONGAELEMS(OLD,NEW)                                 \
  if (OLD != 0) jenv->ReleaseLongArrayElements(OLD, (jlong *) NEW, JNI_ABORT);     

#define GETLONGAREGION(T,OLD,Z,SZ,NEW)                               \
  jenv->GetLongArrayRegion(OLD,Z,(jsize)SZ,(jlong*)NEW);             
#define SETLONGAREGION(OLD,Z,SZ,NEW)                               \
  jenv->SetLongArrayRegion(OLD,Z,(jsize)SZ,(jlong*)NEW)              

#else

#define GETLONGAELEMS(T,OLD,NEW)                                   \
  T * NEW = 0; {                                                   \
  jlong * temp = (OLD == 0) ? 0 : jenv->GetLongArrayElements(OLD, NULL); \
  unsigned int size = (OLD == 0) ? 0 :jenv->GetArrayLength(OLD);     \
  if (OLD != 0) {                                                    \
    NEW = (T*) (new int[size]);                                      \
    for (unsigned i=0; i < size; i++)                                \
      NEW[i] = reinterpret_cast<T>(temp[i]);                         \
    jenv->ReleaseLongArrayElements(OLD, temp, JNI_ABORT);            \
  }                                                                  \
  }                                                                    

#define RELEASELONGAELEMS(OLD,NEW)                                   \
  delete [] NEW;                                                     

#define GETLONGAREGION(T,OLD,Z,SZ,NEW)                              \
  {                                                                 \
    jlong * temp = new jlong[SZ];                                   \
    jenv->GetLongArrayRegion(OLD,Z,(jsize)SZ,(jlong*)temp);         \
    for (int i = 0; i < (SZ); i++)                                  \
      NEW[i] = reinterpret_cast<T>(temp[i]);                        \
    delete [] temp;                                                 \
  }

#define SETLONGAREGION(OLD,Z,SZ,NEW)                                \
  {                                                                 \
    jlong * temp = new jlong[SZ];                                   \
    for (int i = 0; i < (SZ); i++)                                  \
      temp[i] = reinterpret_cast<jlong>(NEW[i]);                    \
    jenv->SetLongArrayRegion(OLD,Z,(jsize)SZ,temp);                 \
    delete [] temp;                                                 \
  }

#endif

void Z3JavaErrorHandler(Z3_context c, Z3_error_code e)
{
  // Internal do-nothing error handler. This is required to avoid that Z3 calls exit()
  // upon errors, but the actual error handling is done by throwing exceptions in the
  // wrappers below.
}

DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_setInternalErrorHandler(JNIEnv * jenv, jclass cls, jlong a0)
{
  Z3_set_error_handler((Z3_context)a0, Z3JavaErrorHandler);
}

DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALglobalParamSet(JNIEnv * jenv, jclass cls, jstring a0, jstring a1) {
  Z3_string _a0 = (Z3_string) jenv->GetStringUTFChars(a0, NULL);
  Z3_string _a1 = (Z3_string) jenv->GetStringUTFChars(a1, NULL);
  Z3_global_param_set(_a0, _a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALglobalParamResetAll(JNIEnv * jenv, jclass cls) {
  Z3_global_param_reset_all();
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALglobalParamGet(JNIEnv * jenv, jclass cls, jstring a0, jobject a1) {
  Z3_string _a0 = (Z3_string) jenv->GetStringUTFChars(a0, NULL);
  Z3_string _a1;
  Z3_bool result = Z3_global_param_get(_a0, &_a1);
  {
     jclass mc    = jenv->GetObjectClass(a1);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a1, fid, (jlong) _a1);
  }
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkConfig(JNIEnv * jenv, jclass cls) {
  Z3_config result = Z3_mk_config();
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALdelConfig(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_del_config((Z3_config)a0);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALsetParamValue(JNIEnv * jenv, jclass cls, jlong a0, jstring a1, jstring a2) {
  Z3_string _a1 = (Z3_string) jenv->GetStringUTFChars(a1, NULL);
  Z3_string _a2 = (Z3_string) jenv->GetStringUTFChars(a2, NULL);
  Z3_set_param_value((Z3_config)a0, _a1, _a2);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkContext(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_context result = Z3_mk_context((Z3_config)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkContextRc(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_context result = Z3_mk_context_rc((Z3_config)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALdelContext(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_del_context((Z3_context)a0);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALincRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_inc_ref((Z3_context)a0, (Z3_ast)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALdecRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_dec_ref((Z3_context)a0, (Z3_ast)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALupdateParamValue(JNIEnv * jenv, jclass cls, jlong a0, jstring a1, jstring a2) {
  Z3_string _a1 = (Z3_string) jenv->GetStringUTFChars(a1, NULL);
  Z3_string _a2 = (Z3_string) jenv->GetStringUTFChars(a2, NULL);
  Z3_update_param_value((Z3_context)a0, _a1, _a2);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALinterrupt(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_interrupt((Z3_context)a0);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkParams(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_params result = Z3_mk_params((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALparamsIncRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_params_inc_ref((Z3_context)a0, (Z3_params)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALparamsDecRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_params_dec_ref((Z3_context)a0, (Z3_params)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALparamsSetBool(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jboolean a3) {
  Z3_params_set_bool((Z3_context)a0, (Z3_params)a1, (Z3_symbol)a2, (Z3_bool)a3);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALparamsSetUint(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jint a3) {
  Z3_params_set_uint((Z3_context)a0, (Z3_params)a1, (Z3_symbol)a2, (unsigned)a3);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALparamsSetDouble(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jdouble a3) {
  Z3_params_set_double((Z3_context)a0, (Z3_params)a1, (Z3_symbol)a2, (double)a3);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALparamsSetSymbol(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_params_set_symbol((Z3_context)a0, (Z3_params)a1, (Z3_symbol)a2, (Z3_symbol)a3);
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALparamsToString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_params_to_string((Z3_context)a0, (Z3_params)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALparamsValidate(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_params_validate((Z3_context)a0, (Z3_params)a1, (Z3_param_descrs)a2);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALparamDescrsIncRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_param_descrs_inc_ref((Z3_context)a0, (Z3_param_descrs)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALparamDescrsDecRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_param_descrs_dec_ref((Z3_context)a0, (Z3_param_descrs)a1);
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALparamDescrsGetKind(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  unsigned result = Z3_param_descrs_get_kind((Z3_context)a0, (Z3_param_descrs)a1, (Z3_symbol)a2);
  return (jint) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALparamDescrsSize(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_param_descrs_size((Z3_context)a0, (Z3_param_descrs)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALparamDescrsGetName(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_symbol result = Z3_param_descrs_get_name((Z3_context)a0, (Z3_param_descrs)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALparamDescrsGetDocumentation(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_string result = Z3_param_descrs_get_documentation((Z3_context)a0, (Z3_param_descrs)a1, (Z3_symbol)a2);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALparamDescrsToString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_param_descrs_to_string((Z3_context)a0, (Z3_param_descrs)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkIntSymbol(JNIEnv * jenv, jclass cls, jlong a0, jint a1) {
  Z3_symbol result = Z3_mk_int_symbol((Z3_context)a0, (int)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkStringSymbol(JNIEnv * jenv, jclass cls, jlong a0, jstring a1) {
  Z3_string _a1 = (Z3_string) jenv->GetStringUTFChars(a1, NULL);
  Z3_symbol result = Z3_mk_string_symbol((Z3_context)a0, _a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkUninterpretedSort(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_sort result = Z3_mk_uninterpreted_sort((Z3_context)a0, (Z3_symbol)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBoolSort(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_sort result = Z3_mk_bool_sort((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkIntSort(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_sort result = Z3_mk_int_sort((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkRealSort(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_sort result = Z3_mk_real_sort((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvSort(JNIEnv * jenv, jclass cls, jlong a0, jint a1) {
  Z3_sort result = Z3_mk_bv_sort((Z3_context)a0, (unsigned)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFiniteDomainSort(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_sort result = Z3_mk_finite_domain_sort((Z3_context)a0, (Z3_symbol)a1, (__uint64)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkArraySort(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_sort result = Z3_mk_array_sort((Z3_context)a0, (Z3_sort)a1, (Z3_sort)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkTupleSort(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlongArray a3, jlongArray a4, jobject a5, jlongArray a6) {
  GETLONGAELEMS(Z3_symbol, a3, _a3);
  GETLONGAELEMS(Z3_sort, a4, _a4);
  Z3_func_decl _a5;
  Z3_func_decl * _a6 = (Z3_func_decl *) malloc(((unsigned)a2) * sizeof(Z3_func_decl));
  GETLONGAREGION(Z3_func_decl, a6, 0, a2, _a6);
  Z3_sort result = Z3_mk_tuple_sort((Z3_context)a0, (Z3_symbol)a1, (unsigned)a2, _a3, _a4, &_a5, _a6);
  RELEASELONGAELEMS(a3, _a3);
  RELEASELONGAELEMS(a4, _a4);
  {
     jclass mc    = jenv->GetObjectClass(a5);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a5, fid, (jlong) _a5);
  }
  SETLONGAREGION(a6, 0, a2, _a6);
  free(_a6);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkEnumerationSort(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlongArray a3, jlongArray a4, jlongArray a5) {
  GETLONGAELEMS(Z3_symbol, a3, _a3);
  Z3_func_decl * _a4 = (Z3_func_decl *) malloc(((unsigned)a2) * sizeof(Z3_func_decl));
  GETLONGAREGION(Z3_func_decl, a4, 0, a2, _a4);
  Z3_func_decl * _a5 = (Z3_func_decl *) malloc(((unsigned)a2) * sizeof(Z3_func_decl));
  GETLONGAREGION(Z3_func_decl, a5, 0, a2, _a5);
  Z3_sort result = Z3_mk_enumeration_sort((Z3_context)a0, (Z3_symbol)a1, (unsigned)a2, _a3, _a4, _a5);
  RELEASELONGAELEMS(a3, _a3);
  SETLONGAREGION(a4, 0, a2, _a4);
  free(_a4);
  SETLONGAREGION(a5, 0, a2, _a5);
  free(_a5);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkListSort(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jobject a3, jobject a4, jobject a5, jobject a6, jobject a7, jobject a8) {
  Z3_func_decl _a3;
  Z3_func_decl _a4;
  Z3_func_decl _a5;
  Z3_func_decl _a6;
  Z3_func_decl _a7;
  Z3_func_decl _a8;
  Z3_sort result = Z3_mk_list_sort((Z3_context)a0, (Z3_symbol)a1, (Z3_sort)a2, &_a3, &_a4, &_a5, &_a6, &_a7, &_a8);
  {
     jclass mc    = jenv->GetObjectClass(a3);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a3, fid, (jlong) _a3);
  }
  {
     jclass mc    = jenv->GetObjectClass(a4);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a4, fid, (jlong) _a4);
  }
  {
     jclass mc    = jenv->GetObjectClass(a5);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a5, fid, (jlong) _a5);
  }
  {
     jclass mc    = jenv->GetObjectClass(a6);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a6, fid, (jlong) _a6);
  }
  {
     jclass mc    = jenv->GetObjectClass(a7);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a7, fid, (jlong) _a7);
  }
  {
     jclass mc    = jenv->GetObjectClass(a8);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a8, fid, (jlong) _a8);
  }
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkConstructor(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jint a3, jlongArray a4, jlongArray a5, jintArray a6) {
  GETLONGAELEMS(Z3_symbol, a4, _a4);
  GETLONGAELEMS(Z3_sort, a5, _a5);
  unsigned * _a6 = (unsigned*) jenv->GetIntArrayElements(a6, NULL);
  Z3_constructor result = Z3_mk_constructor((Z3_context)a0, (Z3_symbol)a1, (Z3_symbol)a2, (unsigned)a3, _a4, _a5, _a6);
  RELEASELONGAELEMS(a4, _a4);
  RELEASELONGAELEMS(a5, _a5);
  jenv->ReleaseIntArrayElements(a6, (jint*)_a6, JNI_ABORT);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALdelConstructor(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_del_constructor((Z3_context)a0, (Z3_constructor)a1);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkDatatype(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlongArray a3) {
  GETLONGAELEMS(Z3_constructor, a3, _a3);
  Z3_sort result = Z3_mk_datatype((Z3_context)a0, (Z3_symbol)a1, (unsigned)a2, _a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkConstructorList(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2) {
  GETLONGAELEMS(Z3_constructor, a2, _a2);
  Z3_constructor_list result = Z3_mk_constructor_list((Z3_context)a0, (unsigned)a1, _a2);
  RELEASELONGAELEMS(a2, _a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALdelConstructorList(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_del_constructor_list((Z3_context)a0, (Z3_constructor_list)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALmkDatatypes(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2, jlongArray a3, jlongArray a4) {
  GETLONGAELEMS(Z3_symbol, a2, _a2);
  Z3_sort * _a3 = (Z3_sort *) malloc(((unsigned)a1) * sizeof(Z3_sort));
  GETLONGAREGION(Z3_sort, a3, 0, a1, _a3);
  GETLONGAELEMS(Z3_constructor_list, a4, _a4);
  Z3_mk_datatypes((Z3_context)a0, (unsigned)a1, _a2, _a3, _a4);
  RELEASELONGAELEMS(a2, _a2);
  SETLONGAREGION(a3, 0, a1, _a3);
  free(_a3);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALqueryConstructor(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jobject a3, jobject a4, jlongArray a5) {
  Z3_func_decl _a3;
  Z3_func_decl _a4;
  Z3_func_decl * _a5 = (Z3_func_decl *) malloc(((unsigned)a2) * sizeof(Z3_func_decl));
  GETLONGAREGION(Z3_func_decl, a5, 0, a2, _a5);
  Z3_query_constructor((Z3_context)a0, (Z3_constructor)a1, (unsigned)a2, &_a3, &_a4, _a5);
  {
     jclass mc    = jenv->GetObjectClass(a3);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a3, fid, (jlong) _a3);
  }
  {
     jclass mc    = jenv->GetObjectClass(a4);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a4, fid, (jlong) _a4);
  }
  SETLONGAREGION(a5, 0, a2, _a5);
  free(_a5);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFuncDecl(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlongArray a3, jlong a4) {
  GETLONGAELEMS(Z3_sort, a3, _a3);
  Z3_func_decl result = Z3_mk_func_decl((Z3_context)a0, (Z3_symbol)a1, (unsigned)a2, _a3, (Z3_sort)a4);
  RELEASELONGAELEMS(a3, _a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkApp(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlongArray a3) {
  GETLONGAELEMS(Z3_ast, a3, _a3);
  Z3_ast result = Z3_mk_app((Z3_context)a0, (Z3_func_decl)a1, (unsigned)a2, _a3);
  RELEASELONGAELEMS(a3, _a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkConst(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_const((Z3_context)a0, (Z3_symbol)a1, (Z3_sort)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFreshFuncDecl(JNIEnv * jenv, jclass cls, jlong a0, jstring a1, jint a2, jlongArray a3, jlong a4) {
  Z3_string _a1 = (Z3_string) jenv->GetStringUTFChars(a1, NULL);
  GETLONGAELEMS(Z3_sort, a3, _a3);
  Z3_func_decl result = Z3_mk_fresh_func_decl((Z3_context)a0, _a1, (unsigned)a2, _a3, (Z3_sort)a4);
  RELEASELONGAELEMS(a3, _a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFreshConst(JNIEnv * jenv, jclass cls, jlong a0, jstring a1, jlong a2) {
  Z3_string _a1 = (Z3_string) jenv->GetStringUTFChars(a1, NULL);
  Z3_ast result = Z3_mk_fresh_const((Z3_context)a0, _a1, (Z3_sort)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkTrue(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_ast result = Z3_mk_true((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFalse(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_ast result = Z3_mk_false((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkEq(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_eq((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkDistinct(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2) {
  GETLONGAELEMS(Z3_ast, a2, _a2);
  Z3_ast result = Z3_mk_distinct((Z3_context)a0, (unsigned)a1, _a2);
  RELEASELONGAELEMS(a2, _a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkNot(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_not((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkIte(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_ast result = Z3_mk_ite((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_ast)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkIff(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_iff((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkImplies(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_implies((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkXor(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_xor((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkAnd(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2) {
  GETLONGAELEMS(Z3_ast, a2, _a2);
  Z3_ast result = Z3_mk_and((Z3_context)a0, (unsigned)a1, _a2);
  RELEASELONGAELEMS(a2, _a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkOr(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2) {
  GETLONGAELEMS(Z3_ast, a2, _a2);
  Z3_ast result = Z3_mk_or((Z3_context)a0, (unsigned)a1, _a2);
  RELEASELONGAELEMS(a2, _a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkAdd(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2) {
  GETLONGAELEMS(Z3_ast, a2, _a2);
  Z3_ast result = Z3_mk_add((Z3_context)a0, (unsigned)a1, _a2);
  RELEASELONGAELEMS(a2, _a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkMul(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2) {
  GETLONGAELEMS(Z3_ast, a2, _a2);
  Z3_ast result = Z3_mk_mul((Z3_context)a0, (unsigned)a1, _a2);
  RELEASELONGAELEMS(a2, _a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSub(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2) {
  GETLONGAELEMS(Z3_ast, a2, _a2);
  Z3_ast result = Z3_mk_sub((Z3_context)a0, (unsigned)a1, _a2);
  RELEASELONGAELEMS(a2, _a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkUnaryMinus(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_unary_minus((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkDiv(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_div((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkMod(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_mod((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkRem(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_rem((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkPower(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_power((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkLt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_lt((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkLe(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_le((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkGt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_gt((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkGe(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_ge((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkInt2real(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_int2real((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkReal2int(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_real2int((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkIsInt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_is_int((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvnot(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_bvnot((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvredand(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_bvredand((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvredor(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_bvredor((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvand(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvand((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvor(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvor((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvxor(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvxor((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvnand(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvnand((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvnor(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvnor((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvxnor(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvxnor((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvneg(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_bvneg((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvadd(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvadd((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvsub(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvsub((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvmul(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvmul((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvudiv(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvudiv((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvsdiv(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvsdiv((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvurem(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvurem((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvsrem(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvsrem((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvsmod(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvsmod((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvult(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvult((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvslt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvslt((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvule(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvule((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvsle(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvsle((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvuge(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvuge((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvsge(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvsge((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvugt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvugt((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvsgt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvsgt((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkConcat(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_concat((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkExtract(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jint a2, jlong a3) {
  Z3_ast result = Z3_mk_extract((Z3_context)a0, (unsigned)a1, (unsigned)a2, (Z3_ast)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSignExt(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlong a2) {
  Z3_ast result = Z3_mk_sign_ext((Z3_context)a0, (unsigned)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkZeroExt(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlong a2) {
  Z3_ast result = Z3_mk_zero_ext((Z3_context)a0, (unsigned)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkRepeat(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlong a2) {
  Z3_ast result = Z3_mk_repeat((Z3_context)a0, (unsigned)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvshl(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvshl((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvlshr(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvlshr((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvashr(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvashr((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkRotateLeft(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlong a2) {
  Z3_ast result = Z3_mk_rotate_left((Z3_context)a0, (unsigned)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkRotateRight(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlong a2) {
  Z3_ast result = Z3_mk_rotate_right((Z3_context)a0, (unsigned)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkExtRotateLeft(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_ext_rotate_left((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkExtRotateRight(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_ext_rotate_right((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkInt2bv(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlong a2) {
  Z3_ast result = Z3_mk_int2bv((Z3_context)a0, (unsigned)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBv2int(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jboolean a2) {
  Z3_ast result = Z3_mk_bv2int((Z3_context)a0, (Z3_ast)a1, (Z3_bool)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvaddNoOverflow(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jboolean a3) {
  Z3_ast result = Z3_mk_bvadd_no_overflow((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_bool)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvaddNoUnderflow(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvadd_no_underflow((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvsubNoOverflow(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvsub_no_overflow((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvsubNoUnderflow(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jboolean a3) {
  Z3_ast result = Z3_mk_bvsub_no_underflow((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_bool)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvsdivNoOverflow(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvsdiv_no_overflow((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvnegNoOverflow(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_bvneg_no_overflow((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvmulNoOverflow(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jboolean a3) {
  Z3_ast result = Z3_mk_bvmul_no_overflow((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_bool)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBvmulNoUnderflow(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_bvmul_no_underflow((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSelect(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_select((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkStore(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_ast result = Z3_mk_store((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_ast)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkConstArray(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_const_array((Z3_context)a0, (Z3_sort)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkMap(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlongArray a3) {
  GETLONGAELEMS(Z3_ast, a3, _a3);
  Z3_ast result = Z3_mk_map((Z3_context)a0, (Z3_func_decl)a1, (unsigned)a2, _a3);
  RELEASELONGAELEMS(a3, _a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkArrayDefault(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_array_default((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSetSort(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_sort result = Z3_mk_set_sort((Z3_context)a0, (Z3_sort)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkEmptySet(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_empty_set((Z3_context)a0, (Z3_sort)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFullSet(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_full_set((Z3_context)a0, (Z3_sort)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSetAdd(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_set_add((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSetDel(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_set_del((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSetUnion(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2) {
  GETLONGAELEMS(Z3_ast, a2, _a2);
  Z3_ast result = Z3_mk_set_union((Z3_context)a0, (unsigned)a1, _a2);
  RELEASELONGAELEMS(a2, _a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSetIntersect(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2) {
  GETLONGAELEMS(Z3_ast, a2, _a2);
  Z3_ast result = Z3_mk_set_intersect((Z3_context)a0, (unsigned)a1, _a2);
  RELEASELONGAELEMS(a2, _a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSetDifference(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_set_difference((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSetComplement(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_set_complement((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSetMember(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_set_member((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSetSubset(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_set_subset((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkArrayExt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_array_ext((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkNumeral(JNIEnv * jenv, jclass cls, jlong a0, jstring a1, jlong a2) {
  Z3_string _a1 = (Z3_string) jenv->GetStringUTFChars(a1, NULL);
  Z3_ast result = Z3_mk_numeral((Z3_context)a0, _a1, (Z3_sort)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkReal(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jint a2) {
  Z3_ast result = Z3_mk_real((Z3_context)a0, (int)a1, (int)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkInt(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlong a2) {
  Z3_ast result = Z3_mk_int((Z3_context)a0, (int)a1, (Z3_sort)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkUnsignedInt(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlong a2) {
  Z3_ast result = Z3_mk_unsigned_int((Z3_context)a0, (unsigned)a1, (Z3_sort)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkInt64(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_int64((Z3_context)a0, (__int64)a1, (Z3_sort)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkUnsignedInt64(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_unsigned_int64((Z3_context)a0, (__uint64)a1, (Z3_sort)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSeqSort(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_sort result = Z3_mk_seq_sort((Z3_context)a0, (Z3_sort)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALisSeqSort(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_bool result = Z3_is_seq_sort((Z3_context)a0, (Z3_sort)a1);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkReSort(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_sort result = Z3_mk_re_sort((Z3_context)a0, (Z3_sort)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALisReSort(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_bool result = Z3_is_re_sort((Z3_context)a0, (Z3_sort)a1);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkStringSort(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_sort result = Z3_mk_string_sort((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALisStringSort(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_bool result = Z3_is_string_sort((Z3_context)a0, (Z3_sort)a1);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkString(JNIEnv * jenv, jclass cls, jlong a0, jstring a1) {
  Z3_string _a1 = (Z3_string) jenv->GetStringUTFChars(a1, NULL);
  Z3_ast result = Z3_mk_string((Z3_context)a0, _a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALisString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_bool result = Z3_is_string((Z3_context)a0, (Z3_ast)a1);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALgetString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_get_string((Z3_context)a0, (Z3_ast)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSeqEmpty(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_seq_empty((Z3_context)a0, (Z3_sort)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSeqUnit(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_seq_unit((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSeqConcat(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2) {
  GETLONGAELEMS(Z3_ast, a2, _a2);
  Z3_ast result = Z3_mk_seq_concat((Z3_context)a0, (unsigned)a1, _a2);
  RELEASELONGAELEMS(a2, _a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSeqPrefix(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_seq_prefix((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSeqSuffix(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_seq_suffix((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSeqContains(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_seq_contains((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSeqExtract(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_ast result = Z3_mk_seq_extract((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_ast)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSeqReplace(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_ast result = Z3_mk_seq_replace((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_ast)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSeqAt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_seq_at((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSeqLength(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_seq_length((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSeqIndex(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_ast result = Z3_mk_seq_index((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_ast)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSeqToRe(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_seq_to_re((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSeqInRe(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_seq_in_re((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkRePlus(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_re_plus((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkReStar(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_re_star((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkReOption(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_re_option((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkReUnion(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2) {
  GETLONGAELEMS(Z3_ast, a2, _a2);
  Z3_ast result = Z3_mk_re_union((Z3_context)a0, (unsigned)a1, _a2);
  RELEASELONGAELEMS(a2, _a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkReConcat(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2) {
  GETLONGAELEMS(Z3_ast, a2, _a2);
  Z3_ast result = Z3_mk_re_concat((Z3_context)a0, (unsigned)a1, _a2);
  RELEASELONGAELEMS(a2, _a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkPattern(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2) {
  GETLONGAELEMS(Z3_ast, a2, _a2);
  Z3_pattern result = Z3_mk_pattern((Z3_context)a0, (unsigned)a1, _a2);
  RELEASELONGAELEMS(a2, _a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkBound(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlong a2) {
  Z3_ast result = Z3_mk_bound((Z3_context)a0, (unsigned)a1, (Z3_sort)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkForall(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jint a2, jlongArray a3, jint a4, jlongArray a5, jlongArray a6, jlong a7) {
  GETLONGAELEMS(Z3_pattern, a3, _a3);
  GETLONGAELEMS(Z3_sort, a5, _a5);
  GETLONGAELEMS(Z3_symbol, a6, _a6);
  Z3_ast result = Z3_mk_forall((Z3_context)a0, (unsigned)a1, (unsigned)a2, _a3, (unsigned)a4, _a5, _a6, (Z3_ast)a7);
  RELEASELONGAELEMS(a3, _a3);
  RELEASELONGAELEMS(a5, _a5);
  RELEASELONGAELEMS(a6, _a6);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkExists(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jint a2, jlongArray a3, jint a4, jlongArray a5, jlongArray a6, jlong a7) {
  GETLONGAELEMS(Z3_pattern, a3, _a3);
  GETLONGAELEMS(Z3_sort, a5, _a5);
  GETLONGAELEMS(Z3_symbol, a6, _a6);
  Z3_ast result = Z3_mk_exists((Z3_context)a0, (unsigned)a1, (unsigned)a2, _a3, (unsigned)a4, _a5, _a6, (Z3_ast)a7);
  RELEASELONGAELEMS(a3, _a3);
  RELEASELONGAELEMS(a5, _a5);
  RELEASELONGAELEMS(a6, _a6);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkQuantifier(JNIEnv * jenv, jclass cls, jlong a0, jboolean a1, jint a2, jint a3, jlongArray a4, jint a5, jlongArray a6, jlongArray a7, jlong a8) {
  GETLONGAELEMS(Z3_pattern, a4, _a4);
  GETLONGAELEMS(Z3_sort, a6, _a6);
  GETLONGAELEMS(Z3_symbol, a7, _a7);
  Z3_ast result = Z3_mk_quantifier((Z3_context)a0, (Z3_bool)a1, (unsigned)a2, (unsigned)a3, _a4, (unsigned)a5, _a6, _a7, (Z3_ast)a8);
  RELEASELONGAELEMS(a4, _a4);
  RELEASELONGAELEMS(a6, _a6);
  RELEASELONGAELEMS(a7, _a7);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkQuantifierEx(JNIEnv * jenv, jclass cls, jlong a0, jboolean a1, jint a2, jlong a3, jlong a4, jint a5, jlongArray a6, jint a7, jlongArray a8, jint a9, jlongArray a10, jlongArray a11, jlong a12) {
  GETLONGAELEMS(Z3_pattern, a6, _a6);
  GETLONGAELEMS(Z3_ast, a8, _a8);
  GETLONGAELEMS(Z3_sort, a10, _a10);
  GETLONGAELEMS(Z3_symbol, a11, _a11);
  Z3_ast result = Z3_mk_quantifier_ex((Z3_context)a0, (Z3_bool)a1, (unsigned)a2, (Z3_symbol)a3, (Z3_symbol)a4, (unsigned)a5, _a6, (unsigned)a7, _a8, (unsigned)a9, _a10, _a11, (Z3_ast)a12);
  RELEASELONGAELEMS(a6, _a6);
  RELEASELONGAELEMS(a8, _a8);
  RELEASELONGAELEMS(a10, _a10);
  RELEASELONGAELEMS(a11, _a11);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkForallConst(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jint a2, jlongArray a3, jint a4, jlongArray a5, jlong a6) {
  GETLONGAELEMS(Z3_app, a3, _a3);
  GETLONGAELEMS(Z3_pattern, a5, _a5);
  Z3_ast result = Z3_mk_forall_const((Z3_context)a0, (unsigned)a1, (unsigned)a2, _a3, (unsigned)a4, _a5, (Z3_ast)a6);
  RELEASELONGAELEMS(a3, _a3);
  RELEASELONGAELEMS(a5, _a5);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkExistsConst(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jint a2, jlongArray a3, jint a4, jlongArray a5, jlong a6) {
  GETLONGAELEMS(Z3_app, a3, _a3);
  GETLONGAELEMS(Z3_pattern, a5, _a5);
  Z3_ast result = Z3_mk_exists_const((Z3_context)a0, (unsigned)a1, (unsigned)a2, _a3, (unsigned)a4, _a5, (Z3_ast)a6);
  RELEASELONGAELEMS(a3, _a3);
  RELEASELONGAELEMS(a5, _a5);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkQuantifierConst(JNIEnv * jenv, jclass cls, jlong a0, jboolean a1, jint a2, jint a3, jlongArray a4, jint a5, jlongArray a6, jlong a7) {
  GETLONGAELEMS(Z3_app, a4, _a4);
  GETLONGAELEMS(Z3_pattern, a6, _a6);
  Z3_ast result = Z3_mk_quantifier_const((Z3_context)a0, (Z3_bool)a1, (unsigned)a2, (unsigned)a3, _a4, (unsigned)a5, _a6, (Z3_ast)a7);
  RELEASELONGAELEMS(a4, _a4);
  RELEASELONGAELEMS(a6, _a6);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkQuantifierConstEx(JNIEnv * jenv, jclass cls, jlong a0, jboolean a1, jint a2, jlong a3, jlong a4, jint a5, jlongArray a6, jint a7, jlongArray a8, jint a9, jlongArray a10, jlong a11) {
  GETLONGAELEMS(Z3_app, a6, _a6);
  GETLONGAELEMS(Z3_pattern, a8, _a8);
  GETLONGAELEMS(Z3_ast, a10, _a10);
  Z3_ast result = Z3_mk_quantifier_const_ex((Z3_context)a0, (Z3_bool)a1, (unsigned)a2, (Z3_symbol)a3, (Z3_symbol)a4, (unsigned)a5, _a6, (unsigned)a7, _a8, (unsigned)a9, _a10, (Z3_ast)a11);
  RELEASELONGAELEMS(a6, _a6);
  RELEASELONGAELEMS(a8, _a8);
  RELEASELONGAELEMS(a10, _a10);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetSymbolKind(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_symbol_kind((Z3_context)a0, (Z3_symbol)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetSymbolInt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  int result = Z3_get_symbol_int((Z3_context)a0, (Z3_symbol)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALgetSymbolString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_get_symbol_string((Z3_context)a0, (Z3_symbol)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetSortName(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_symbol result = Z3_get_sort_name((Z3_context)a0, (Z3_sort)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetSortId(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_sort_id((Z3_context)a0, (Z3_sort)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALsortToAst(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_sort_to_ast((Z3_context)a0, (Z3_sort)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALisEqSort(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_bool result = Z3_is_eq_sort((Z3_context)a0, (Z3_sort)a1, (Z3_sort)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetSortKind(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_sort_kind((Z3_context)a0, (Z3_sort)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetBvSortSize(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_bv_sort_size((Z3_context)a0, (Z3_sort)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALgetFiniteDomainSortSize(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jobject a2) {
  __uint64 _a2;
  Z3_bool result = Z3_get_finite_domain_sort_size((Z3_context)a0, (Z3_sort)a1, &_a2);
  {
     jclass mc    = jenv->GetObjectClass(a2);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a2, fid, (jlong) _a2);
  }
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetArraySortDomain(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_sort result = Z3_get_array_sort_domain((Z3_context)a0, (Z3_sort)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetArraySortRange(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_sort result = Z3_get_array_sort_range((Z3_context)a0, (Z3_sort)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetTupleSortMkDecl(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_func_decl result = Z3_get_tuple_sort_mk_decl((Z3_context)a0, (Z3_sort)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetTupleSortNumFields(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_tuple_sort_num_fields((Z3_context)a0, (Z3_sort)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetTupleSortFieldDecl(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_func_decl result = Z3_get_tuple_sort_field_decl((Z3_context)a0, (Z3_sort)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetDatatypeSortNumConstructors(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_datatype_sort_num_constructors((Z3_context)a0, (Z3_sort)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetDatatypeSortConstructor(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_func_decl result = Z3_get_datatype_sort_constructor((Z3_context)a0, (Z3_sort)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetDatatypeSortRecognizer(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_func_decl result = Z3_get_datatype_sort_recognizer((Z3_context)a0, (Z3_sort)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetDatatypeSortConstructorAccessor(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jint a3) {
  Z3_func_decl result = Z3_get_datatype_sort_constructor_accessor((Z3_context)a0, (Z3_sort)a1, (unsigned)a2, (unsigned)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALdatatypeUpdateField(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_ast result = Z3_datatype_update_field((Z3_context)a0, (Z3_func_decl)a1, (Z3_ast)a2, (Z3_ast)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetRelationArity(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_relation_arity((Z3_context)a0, (Z3_sort)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetRelationColumn(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_sort result = Z3_get_relation_column((Z3_context)a0, (Z3_sort)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkAtmost(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2, jint a3) {
  GETLONGAELEMS(Z3_ast, a2, _a2);
  Z3_ast result = Z3_mk_atmost((Z3_context)a0, (unsigned)a1, _a2, (unsigned)a3);
  RELEASELONGAELEMS(a2, _a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkPble(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2, jintArray a3, jint a4) {
  GETLONGAELEMS(Z3_ast, a2, _a2);
  int * _a3 = (int*) jenv->GetIntArrayElements(a3, NULL);
  Z3_ast result = Z3_mk_pble((Z3_context)a0, (unsigned)a1, _a2, _a3, (int)a4);
  RELEASELONGAELEMS(a2, _a2);
  jenv->ReleaseIntArrayElements(a3, (jint*)_a3, JNI_ABORT);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkPbeq(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2, jintArray a3, jint a4) {
  GETLONGAELEMS(Z3_ast, a2, _a2);
  int * _a3 = (int*) jenv->GetIntArrayElements(a3, NULL);
  Z3_ast result = Z3_mk_pbeq((Z3_context)a0, (unsigned)a1, _a2, _a3, (int)a4);
  RELEASELONGAELEMS(a2, _a2);
  jenv->ReleaseIntArrayElements(a3, (jint*)_a3, JNI_ABORT);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALfuncDeclToAst(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_func_decl_to_ast((Z3_context)a0, (Z3_func_decl)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALisEqFuncDecl(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_bool result = Z3_is_eq_func_decl((Z3_context)a0, (Z3_func_decl)a1, (Z3_func_decl)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetFuncDeclId(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_func_decl_id((Z3_context)a0, (Z3_func_decl)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetDeclName(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_symbol result = Z3_get_decl_name((Z3_context)a0, (Z3_func_decl)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetDeclKind(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_decl_kind((Z3_context)a0, (Z3_func_decl)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetDomainSize(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_domain_size((Z3_context)a0, (Z3_func_decl)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetArity(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_arity((Z3_context)a0, (Z3_func_decl)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetDomain(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_sort result = Z3_get_domain((Z3_context)a0, (Z3_func_decl)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetRange(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_sort result = Z3_get_range((Z3_context)a0, (Z3_func_decl)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetDeclNumParameters(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_decl_num_parameters((Z3_context)a0, (Z3_func_decl)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetDeclParameterKind(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  unsigned result = Z3_get_decl_parameter_kind((Z3_context)a0, (Z3_func_decl)a1, (unsigned)a2);
  return (jint) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetDeclIntParameter(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  int result = Z3_get_decl_int_parameter((Z3_context)a0, (Z3_func_decl)a1, (unsigned)a2);
  return (jint) result;
}
DLL_VIS JNIEXPORT jdouble JNICALL Java_com_microsoft_z3_Native_INTERNALgetDeclDoubleParameter(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  double result = Z3_get_decl_double_parameter((Z3_context)a0, (Z3_func_decl)a1, (unsigned)a2);
  return (jdouble) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetDeclSymbolParameter(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_symbol result = Z3_get_decl_symbol_parameter((Z3_context)a0, (Z3_func_decl)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetDeclSortParameter(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_sort result = Z3_get_decl_sort_parameter((Z3_context)a0, (Z3_func_decl)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetDeclAstParameter(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_ast result = Z3_get_decl_ast_parameter((Z3_context)a0, (Z3_func_decl)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetDeclFuncDeclParameter(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_func_decl result = Z3_get_decl_func_decl_parameter((Z3_context)a0, (Z3_func_decl)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALgetDeclRationalParameter(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_string result = Z3_get_decl_rational_parameter((Z3_context)a0, (Z3_func_decl)a1, (unsigned)a2);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALappToAst(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_app_to_ast((Z3_context)a0, (Z3_app)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetAppDecl(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_func_decl result = Z3_get_app_decl((Z3_context)a0, (Z3_app)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetAppNumArgs(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_app_num_args((Z3_context)a0, (Z3_app)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetAppArg(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_ast result = Z3_get_app_arg((Z3_context)a0, (Z3_app)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALisEqAst(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_bool result = Z3_is_eq_ast((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetAstId(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_ast_id((Z3_context)a0, (Z3_ast)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetAstHash(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_ast_hash((Z3_context)a0, (Z3_ast)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetSort(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_sort result = Z3_get_sort((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALisWellSorted(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_bool result = Z3_is_well_sorted((Z3_context)a0, (Z3_ast)a1);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetBoolValue(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  int result = Z3_get_bool_value((Z3_context)a0, (Z3_ast)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetAstKind(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_ast_kind((Z3_context)a0, (Z3_ast)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALisApp(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_bool result = Z3_is_app((Z3_context)a0, (Z3_ast)a1);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALisNumeralAst(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_bool result = Z3_is_numeral_ast((Z3_context)a0, (Z3_ast)a1);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALisAlgebraicNumber(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_bool result = Z3_is_algebraic_number((Z3_context)a0, (Z3_ast)a1);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtoApp(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_app result = Z3_to_app((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtoFuncDecl(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_func_decl result = Z3_to_func_decl((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALgetNumeralString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_get_numeral_string((Z3_context)a0, (Z3_ast)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALgetNumeralDecimalString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_string result = Z3_get_numeral_decimal_string((Z3_context)a0, (Z3_ast)a1, (unsigned)a2);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetNumerator(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_get_numerator((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetDenominator(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_get_denominator((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALgetNumeralSmall(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jobject a2, jobject a3) {
  __int64 _a2;
  __int64 _a3;
  Z3_bool result = Z3_get_numeral_small((Z3_context)a0, (Z3_ast)a1, &_a2, &_a3);
  {
     jclass mc    = jenv->GetObjectClass(a2);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a2, fid, (jlong) _a2);
  }
  {
     jclass mc    = jenv->GetObjectClass(a3);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a3, fid, (jlong) _a3);
  }
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALgetNumeralInt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jobject a2) {
  int _a2;
  Z3_bool result = Z3_get_numeral_int((Z3_context)a0, (Z3_ast)a1, &_a2);
  {
     jclass mc    = jenv->GetObjectClass(a2);
     jfieldID fid = jenv->GetFieldID(mc, "value", "I");
     jenv->SetIntField(a2, fid, (jint) _a2);
  }
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALgetNumeralUint(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jobject a2) {
  unsigned _a2;
  Z3_bool result = Z3_get_numeral_uint((Z3_context)a0, (Z3_ast)a1, &_a2);
  {
     jclass mc    = jenv->GetObjectClass(a2);
     jfieldID fid = jenv->GetFieldID(mc, "value", "I");
     jenv->SetIntField(a2, fid, (jint) _a2);
  }
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALgetNumeralUint64(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jobject a2) {
  __uint64 _a2;
  Z3_bool result = Z3_get_numeral_uint64((Z3_context)a0, (Z3_ast)a1, &_a2);
  {
     jclass mc    = jenv->GetObjectClass(a2);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a2, fid, (jlong) _a2);
  }
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALgetNumeralInt64(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jobject a2) {
  __int64 _a2;
  Z3_bool result = Z3_get_numeral_int64((Z3_context)a0, (Z3_ast)a1, &_a2);
  {
     jclass mc    = jenv->GetObjectClass(a2);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a2, fid, (jlong) _a2);
  }
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALgetNumeralRationalInt64(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jobject a2, jobject a3) {
  __int64 _a2;
  __int64 _a3;
  Z3_bool result = Z3_get_numeral_rational_int64((Z3_context)a0, (Z3_ast)a1, &_a2, &_a3);
  {
     jclass mc    = jenv->GetObjectClass(a2);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a2, fid, (jlong) _a2);
  }
  {
     jclass mc    = jenv->GetObjectClass(a3);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a3, fid, (jlong) _a3);
  }
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetAlgebraicNumberLower(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_ast result = Z3_get_algebraic_number_lower((Z3_context)a0, (Z3_ast)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetAlgebraicNumberUpper(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_ast result = Z3_get_algebraic_number_upper((Z3_context)a0, (Z3_ast)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALpatternToAst(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_pattern_to_ast((Z3_context)a0, (Z3_pattern)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetPatternNumTerms(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_pattern_num_terms((Z3_context)a0, (Z3_pattern)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetPattern(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_ast result = Z3_get_pattern((Z3_context)a0, (Z3_pattern)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetIndexValue(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_index_value((Z3_context)a0, (Z3_ast)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALisQuantifierForall(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_bool result = Z3_is_quantifier_forall((Z3_context)a0, (Z3_ast)a1);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetQuantifierWeight(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_quantifier_weight((Z3_context)a0, (Z3_ast)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetQuantifierNumPatterns(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_quantifier_num_patterns((Z3_context)a0, (Z3_ast)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetQuantifierPatternAst(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_pattern result = Z3_get_quantifier_pattern_ast((Z3_context)a0, (Z3_ast)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetQuantifierNumNoPatterns(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_quantifier_num_no_patterns((Z3_context)a0, (Z3_ast)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetQuantifierNoPatternAst(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_ast result = Z3_get_quantifier_no_pattern_ast((Z3_context)a0, (Z3_ast)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetQuantifierNumBound(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_get_quantifier_num_bound((Z3_context)a0, (Z3_ast)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetQuantifierBoundName(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_symbol result = Z3_get_quantifier_bound_name((Z3_context)a0, (Z3_ast)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetQuantifierBoundSort(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_sort result = Z3_get_quantifier_bound_sort((Z3_context)a0, (Z3_ast)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetQuantifierBody(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_get_quantifier_body((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALsimplify(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_simplify((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALsimplifyEx(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_simplify_ex((Z3_context)a0, (Z3_ast)a1, (Z3_params)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALsimplifyGetHelp(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_string result = Z3_simplify_get_help((Z3_context)a0);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALsimplifyGetParamDescrs(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_param_descrs result = Z3_simplify_get_param_descrs((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALupdateTerm(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlongArray a3) {
  GETLONGAELEMS(Z3_ast, a3, _a3);
  Z3_ast result = Z3_update_term((Z3_context)a0, (Z3_ast)a1, (unsigned)a2, _a3);
  RELEASELONGAELEMS(a3, _a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALsubstitute(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlongArray a3, jlongArray a4) {
  GETLONGAELEMS(Z3_ast, a3, _a3);
  GETLONGAELEMS(Z3_ast, a4, _a4);
  Z3_ast result = Z3_substitute((Z3_context)a0, (Z3_ast)a1, (unsigned)a2, _a3, _a4);
  RELEASELONGAELEMS(a3, _a3);
  RELEASELONGAELEMS(a4, _a4);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALsubstituteVars(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlongArray a3) {
  GETLONGAELEMS(Z3_ast, a3, _a3);
  Z3_ast result = Z3_substitute_vars((Z3_context)a0, (Z3_ast)a1, (unsigned)a2, _a3);
  RELEASELONGAELEMS(a3, _a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtranslate(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_translate((Z3_context)a0, (Z3_ast)a1, (Z3_context)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALmodelIncRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_model_inc_ref((Z3_context)a0, (Z3_model)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALmodelDecRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_model_dec_ref((Z3_context)a0, (Z3_model)a1);
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALmodelEval(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jboolean a3, jobject a4) {
  Z3_ast _a4;
  Z3_bool result = Z3_model_eval((Z3_context)a0, (Z3_model)a1, (Z3_ast)a2, (Z3_bool)a3, &_a4);
  {
     jclass mc    = jenv->GetObjectClass(a4);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a4, fid, (jlong) _a4);
  }
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmodelGetConstInterp(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_model_get_const_interp((Z3_context)a0, (Z3_model)a1, (Z3_func_decl)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALmodelHasInterp(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_bool result = Z3_model_has_interp((Z3_context)a0, (Z3_model)a1, (Z3_func_decl)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmodelGetFuncInterp(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_func_interp result = Z3_model_get_func_interp((Z3_context)a0, (Z3_model)a1, (Z3_func_decl)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALmodelGetNumConsts(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_model_get_num_consts((Z3_context)a0, (Z3_model)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmodelGetConstDecl(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_func_decl result = Z3_model_get_const_decl((Z3_context)a0, (Z3_model)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALmodelGetNumFuncs(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_model_get_num_funcs((Z3_context)a0, (Z3_model)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmodelGetFuncDecl(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_func_decl result = Z3_model_get_func_decl((Z3_context)a0, (Z3_model)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALmodelGetNumSorts(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_model_get_num_sorts((Z3_context)a0, (Z3_model)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmodelGetSort(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_sort result = Z3_model_get_sort((Z3_context)a0, (Z3_model)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmodelGetSortUniverse(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast_vector result = Z3_model_get_sort_universe((Z3_context)a0, (Z3_model)a1, (Z3_sort)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALisAsArray(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_bool result = Z3_is_as_array((Z3_context)a0, (Z3_ast)a1);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetAsArrayFuncDecl(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_func_decl result = Z3_get_as_array_func_decl((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALfuncInterpIncRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_func_interp_inc_ref((Z3_context)a0, (Z3_func_interp)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALfuncInterpDecRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_func_interp_dec_ref((Z3_context)a0, (Z3_func_interp)a1);
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALfuncInterpGetNumEntries(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_func_interp_get_num_entries((Z3_context)a0, (Z3_func_interp)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALfuncInterpGetEntry(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_func_entry result = Z3_func_interp_get_entry((Z3_context)a0, (Z3_func_interp)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALfuncInterpGetElse(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_func_interp_get_else((Z3_context)a0, (Z3_func_interp)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALfuncInterpGetArity(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_func_interp_get_arity((Z3_context)a0, (Z3_func_interp)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALfuncEntryIncRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_func_entry_inc_ref((Z3_context)a0, (Z3_func_entry)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALfuncEntryDecRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_func_entry_dec_ref((Z3_context)a0, (Z3_func_entry)a1);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALfuncEntryGetValue(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_func_entry_get_value((Z3_context)a0, (Z3_func_entry)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALfuncEntryGetNumArgs(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_func_entry_get_num_args((Z3_context)a0, (Z3_func_entry)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALfuncEntryGetArg(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_ast result = Z3_func_entry_get_arg((Z3_context)a0, (Z3_func_entry)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALopenLog(JNIEnv * jenv, jclass cls, jstring a0) {
  Z3_string _a0 = (Z3_string) jenv->GetStringUTFChars(a0, NULL);
  int result = Z3_open_log(_a0);
  return (jint) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALappendLog(JNIEnv * jenv, jclass cls, jstring a0) {
  Z3_string _a0 = (Z3_string) jenv->GetStringUTFChars(a0, NULL);
  Z3_append_log(_a0);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALcloseLog(JNIEnv * jenv, jclass cls) {
  Z3_close_log();
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALtoggleWarningMessages(JNIEnv * jenv, jclass cls, jboolean a0) {
  Z3_toggle_warning_messages((Z3_bool)a0);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALsetAstPrintMode(JNIEnv * jenv, jclass cls, jlong a0, jint a1) {
  Z3_set_ast_print_mode((Z3_context)a0, (Z3_ast_print_mode)a1);
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALastToString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_ast_to_string((Z3_context)a0, (Z3_ast)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALpatternToString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_pattern_to_string((Z3_context)a0, (Z3_pattern)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALsortToString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_sort_to_string((Z3_context)a0, (Z3_sort)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALfuncDeclToString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_func_decl_to_string((Z3_context)a0, (Z3_func_decl)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALmodelToString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_model_to_string((Z3_context)a0, (Z3_model)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALbenchmarkToSmtlibString(JNIEnv * jenv, jclass cls, jlong a0, jstring a1, jstring a2, jstring a3, jstring a4, jint a5, jlongArray a6, jlong a7) {
  Z3_string _a1 = (Z3_string) jenv->GetStringUTFChars(a1, NULL);
  Z3_string _a2 = (Z3_string) jenv->GetStringUTFChars(a2, NULL);
  Z3_string _a3 = (Z3_string) jenv->GetStringUTFChars(a3, NULL);
  Z3_string _a4 = (Z3_string) jenv->GetStringUTFChars(a4, NULL);
  GETLONGAELEMS(Z3_ast, a6, _a6);
  Z3_string result = Z3_benchmark_to_smtlib_string((Z3_context)a0, _a1, _a2, _a3, _a4, (unsigned)a5, _a6, (Z3_ast)a7);
  RELEASELONGAELEMS(a6, _a6);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALparseSmtlib2String(JNIEnv * jenv, jclass cls, jlong a0, jstring a1, jint a2, jlongArray a3, jlongArray a4, jint a5, jlongArray a6, jlongArray a7) {
  Z3_string _a1 = (Z3_string) jenv->GetStringUTFChars(a1, NULL);
  GETLONGAELEMS(Z3_symbol, a3, _a3);
  GETLONGAELEMS(Z3_sort, a4, _a4);
  GETLONGAELEMS(Z3_symbol, a6, _a6);
  GETLONGAELEMS(Z3_func_decl, a7, _a7);
  Z3_ast result = Z3_parse_smtlib2_string((Z3_context)a0, _a1, (unsigned)a2, _a3, _a4, (unsigned)a5, _a6, _a7);
  RELEASELONGAELEMS(a3, _a3);
  RELEASELONGAELEMS(a4, _a4);
  RELEASELONGAELEMS(a6, _a6);
  RELEASELONGAELEMS(a7, _a7);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALparseSmtlib2File(JNIEnv * jenv, jclass cls, jlong a0, jstring a1, jint a2, jlongArray a3, jlongArray a4, jint a5, jlongArray a6, jlongArray a7) {
  Z3_string _a1 = (Z3_string) jenv->GetStringUTFChars(a1, NULL);
  GETLONGAELEMS(Z3_symbol, a3, _a3);
  GETLONGAELEMS(Z3_sort, a4, _a4);
  GETLONGAELEMS(Z3_symbol, a6, _a6);
  GETLONGAELEMS(Z3_func_decl, a7, _a7);
  Z3_ast result = Z3_parse_smtlib2_file((Z3_context)a0, _a1, (unsigned)a2, _a3, _a4, (unsigned)a5, _a6, _a7);
  RELEASELONGAELEMS(a3, _a3);
  RELEASELONGAELEMS(a4, _a4);
  RELEASELONGAELEMS(a6, _a6);
  RELEASELONGAELEMS(a7, _a7);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALparseSmtlibString(JNIEnv * jenv, jclass cls, jlong a0, jstring a1, jint a2, jlongArray a3, jlongArray a4, jint a5, jlongArray a6, jlongArray a7) {
  Z3_string _a1 = (Z3_string) jenv->GetStringUTFChars(a1, NULL);
  GETLONGAELEMS(Z3_symbol, a3, _a3);
  GETLONGAELEMS(Z3_sort, a4, _a4);
  GETLONGAELEMS(Z3_symbol, a6, _a6);
  GETLONGAELEMS(Z3_func_decl, a7, _a7);
  Z3_parse_smtlib_string((Z3_context)a0, _a1, (unsigned)a2, _a3, _a4, (unsigned)a5, _a6, _a7);
  RELEASELONGAELEMS(a3, _a3);
  RELEASELONGAELEMS(a4, _a4);
  RELEASELONGAELEMS(a6, _a6);
  RELEASELONGAELEMS(a7, _a7);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALparseSmtlibFile(JNIEnv * jenv, jclass cls, jlong a0, jstring a1, jint a2, jlongArray a3, jlongArray a4, jint a5, jlongArray a6, jlongArray a7) {
  Z3_string _a1 = (Z3_string) jenv->GetStringUTFChars(a1, NULL);
  GETLONGAELEMS(Z3_symbol, a3, _a3);
  GETLONGAELEMS(Z3_sort, a4, _a4);
  GETLONGAELEMS(Z3_symbol, a6, _a6);
  GETLONGAELEMS(Z3_func_decl, a7, _a7);
  Z3_parse_smtlib_file((Z3_context)a0, _a1, (unsigned)a2, _a3, _a4, (unsigned)a5, _a6, _a7);
  RELEASELONGAELEMS(a3, _a3);
  RELEASELONGAELEMS(a4, _a4);
  RELEASELONGAELEMS(a6, _a6);
  RELEASELONGAELEMS(a7, _a7);
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetSmtlibNumFormulas(JNIEnv * jenv, jclass cls, jlong a0) {
  unsigned result = Z3_get_smtlib_num_formulas((Z3_context)a0);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetSmtlibFormula(JNIEnv * jenv, jclass cls, jlong a0, jint a1) {
  Z3_ast result = Z3_get_smtlib_formula((Z3_context)a0, (unsigned)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetSmtlibNumAssumptions(JNIEnv * jenv, jclass cls, jlong a0) {
  unsigned result = Z3_get_smtlib_num_assumptions((Z3_context)a0);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetSmtlibAssumption(JNIEnv * jenv, jclass cls, jlong a0, jint a1) {
  Z3_ast result = Z3_get_smtlib_assumption((Z3_context)a0, (unsigned)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetSmtlibNumDecls(JNIEnv * jenv, jclass cls, jlong a0) {
  unsigned result = Z3_get_smtlib_num_decls((Z3_context)a0);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetSmtlibDecl(JNIEnv * jenv, jclass cls, jlong a0, jint a1) {
  Z3_func_decl result = Z3_get_smtlib_decl((Z3_context)a0, (unsigned)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetSmtlibNumSorts(JNIEnv * jenv, jclass cls, jlong a0) {
  unsigned result = Z3_get_smtlib_num_sorts((Z3_context)a0);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetSmtlibSort(JNIEnv * jenv, jclass cls, jlong a0, jint a1) {
  Z3_sort result = Z3_get_smtlib_sort((Z3_context)a0, (unsigned)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALgetSmtlibError(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_string result = Z3_get_smtlib_error((Z3_context)a0);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetErrorCode(JNIEnv * jenv, jclass cls, jlong a0) {
  unsigned result = Z3_get_error_code((Z3_context)a0);
  return (jint) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALsetError(JNIEnv * jenv, jclass cls, jlong a0, jint a1) {
  Z3_set_error((Z3_context)a0, (Z3_error_code)a1);
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALgetErrorMsg(JNIEnv * jenv, jclass cls, jlong a0, jint a1) {
  Z3_string result = Z3_get_error_msg((Z3_context)a0, (Z3_error_code)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALgetVersion(JNIEnv * jenv, jclass cls, jobject a0, jobject a1, jobject a2, jobject a3) {
  unsigned _a0;
  unsigned _a1;
  unsigned _a2;
  unsigned _a3;
  Z3_get_version(&_a0, &_a1, &_a2, &_a3);
  {
     jclass mc    = jenv->GetObjectClass(a0);
     jfieldID fid = jenv->GetFieldID(mc, "value", "I");
     jenv->SetIntField(a0, fid, (jint) _a0);
  }
  {
     jclass mc    = jenv->GetObjectClass(a1);
     jfieldID fid = jenv->GetFieldID(mc, "value", "I");
     jenv->SetIntField(a1, fid, (jint) _a1);
  }
  {
     jclass mc    = jenv->GetObjectClass(a2);
     jfieldID fid = jenv->GetFieldID(mc, "value", "I");
     jenv->SetIntField(a2, fid, (jint) _a2);
  }
  {
     jclass mc    = jenv->GetObjectClass(a3);
     jfieldID fid = jenv->GetFieldID(mc, "value", "I");
     jenv->SetIntField(a3, fid, (jint) _a3);
  }
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALgetFullVersion(JNIEnv * jenv, jclass cls) {
  Z3_string result = Z3_get_full_version();
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALenableTrace(JNIEnv * jenv, jclass cls, jstring a0) {
  Z3_string _a0 = (Z3_string) jenv->GetStringUTFChars(a0, NULL);
  Z3_enable_trace(_a0);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALdisableTrace(JNIEnv * jenv, jclass cls, jstring a0) {
  Z3_string _a0 = (Z3_string) jenv->GetStringUTFChars(a0, NULL);
  Z3_disable_trace(_a0);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALresetMemory(JNIEnv * jenv, jclass cls) {
  Z3_reset_memory();
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALfinalizeMemory(JNIEnv * jenv, jclass cls) {
  Z3_finalize_memory();
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkGoal(JNIEnv * jenv, jclass cls, jlong a0, jboolean a1, jboolean a2, jboolean a3) {
  Z3_goal result = Z3_mk_goal((Z3_context)a0, (Z3_bool)a1, (Z3_bool)a2, (Z3_bool)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALgoalIncRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_goal_inc_ref((Z3_context)a0, (Z3_goal)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALgoalDecRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_goal_dec_ref((Z3_context)a0, (Z3_goal)a1);
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgoalPrecision(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_goal_precision((Z3_context)a0, (Z3_goal)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALgoalAssert(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_goal_assert((Z3_context)a0, (Z3_goal)a1, (Z3_ast)a2);
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALgoalInconsistent(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_bool result = Z3_goal_inconsistent((Z3_context)a0, (Z3_goal)a1);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgoalDepth(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_goal_depth((Z3_context)a0, (Z3_goal)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALgoalReset(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_goal_reset((Z3_context)a0, (Z3_goal)a1);
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgoalSize(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_goal_size((Z3_context)a0, (Z3_goal)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgoalFormula(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_ast result = Z3_goal_formula((Z3_context)a0, (Z3_goal)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgoalNumExprs(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_goal_num_exprs((Z3_context)a0, (Z3_goal)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALgoalIsDecidedSat(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_bool result = Z3_goal_is_decided_sat((Z3_context)a0, (Z3_goal)a1);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALgoalIsDecidedUnsat(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_bool result = Z3_goal_is_decided_unsat((Z3_context)a0, (Z3_goal)a1);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgoalTranslate(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_goal result = Z3_goal_translate((Z3_context)a0, (Z3_goal)a1, (Z3_context)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALgoalToString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_goal_to_string((Z3_context)a0, (Z3_goal)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkTactic(JNIEnv * jenv, jclass cls, jlong a0, jstring a1) {
  Z3_string _a1 = (Z3_string) jenv->GetStringUTFChars(a1, NULL);
  Z3_tactic result = Z3_mk_tactic((Z3_context)a0, _a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALtacticIncRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_tactic_inc_ref((Z3_context)a0, (Z3_tactic)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALtacticDecRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_tactic_dec_ref((Z3_context)a0, (Z3_tactic)a1);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkProbe(JNIEnv * jenv, jclass cls, jlong a0, jstring a1) {
  Z3_string _a1 = (Z3_string) jenv->GetStringUTFChars(a1, NULL);
  Z3_probe result = Z3_mk_probe((Z3_context)a0, _a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALprobeIncRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_probe_inc_ref((Z3_context)a0, (Z3_probe)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALprobeDecRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_probe_dec_ref((Z3_context)a0, (Z3_probe)a1);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtacticAndThen(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_tactic result = Z3_tactic_and_then((Z3_context)a0, (Z3_tactic)a1, (Z3_tactic)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtacticOrElse(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_tactic result = Z3_tactic_or_else((Z3_context)a0, (Z3_tactic)a1, (Z3_tactic)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtacticParOr(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2) {
  GETLONGAELEMS(Z3_tactic, a2, _a2);
  Z3_tactic result = Z3_tactic_par_or((Z3_context)a0, (unsigned)a1, _a2);
  RELEASELONGAELEMS(a2, _a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtacticParAndThen(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_tactic result = Z3_tactic_par_and_then((Z3_context)a0, (Z3_tactic)a1, (Z3_tactic)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtacticTryFor(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_tactic result = Z3_tactic_try_for((Z3_context)a0, (Z3_tactic)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtacticWhen(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_tactic result = Z3_tactic_when((Z3_context)a0, (Z3_probe)a1, (Z3_tactic)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtacticCond(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_tactic result = Z3_tactic_cond((Z3_context)a0, (Z3_probe)a1, (Z3_tactic)a2, (Z3_tactic)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtacticRepeat(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_tactic result = Z3_tactic_repeat((Z3_context)a0, (Z3_tactic)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtacticSkip(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_tactic result = Z3_tactic_skip((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtacticFail(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_tactic result = Z3_tactic_fail((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtacticFailIf(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_tactic result = Z3_tactic_fail_if((Z3_context)a0, (Z3_probe)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtacticFailIfNotDecided(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_tactic result = Z3_tactic_fail_if_not_decided((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtacticUsingParams(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_tactic result = Z3_tactic_using_params((Z3_context)a0, (Z3_tactic)a1, (Z3_params)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALprobeConst(JNIEnv * jenv, jclass cls, jlong a0, jdouble a1) {
  Z3_probe result = Z3_probe_const((Z3_context)a0, (double)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALprobeLt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_probe result = Z3_probe_lt((Z3_context)a0, (Z3_probe)a1, (Z3_probe)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALprobeGt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_probe result = Z3_probe_gt((Z3_context)a0, (Z3_probe)a1, (Z3_probe)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALprobeLe(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_probe result = Z3_probe_le((Z3_context)a0, (Z3_probe)a1, (Z3_probe)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALprobeGe(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_probe result = Z3_probe_ge((Z3_context)a0, (Z3_probe)a1, (Z3_probe)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALprobeEq(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_probe result = Z3_probe_eq((Z3_context)a0, (Z3_probe)a1, (Z3_probe)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALprobeAnd(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_probe result = Z3_probe_and((Z3_context)a0, (Z3_probe)a1, (Z3_probe)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALprobeOr(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_probe result = Z3_probe_or((Z3_context)a0, (Z3_probe)a1, (Z3_probe)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALprobeNot(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_probe result = Z3_probe_not((Z3_context)a0, (Z3_probe)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetNumTactics(JNIEnv * jenv, jclass cls, jlong a0) {
  unsigned result = Z3_get_num_tactics((Z3_context)a0);
  return (jint) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALgetTacticName(JNIEnv * jenv, jclass cls, jlong a0, jint a1) {
  Z3_string result = Z3_get_tactic_name((Z3_context)a0, (unsigned)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetNumProbes(JNIEnv * jenv, jclass cls, jlong a0) {
  unsigned result = Z3_get_num_probes((Z3_context)a0);
  return (jint) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALgetProbeName(JNIEnv * jenv, jclass cls, jlong a0, jint a1) {
  Z3_string result = Z3_get_probe_name((Z3_context)a0, (unsigned)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALtacticGetHelp(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_tactic_get_help((Z3_context)a0, (Z3_tactic)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtacticGetParamDescrs(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_param_descrs result = Z3_tactic_get_param_descrs((Z3_context)a0, (Z3_tactic)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALtacticGetDescr(JNIEnv * jenv, jclass cls, jlong a0, jstring a1) {
  Z3_string _a1 = (Z3_string) jenv->GetStringUTFChars(a1, NULL);
  Z3_string result = Z3_tactic_get_descr((Z3_context)a0, _a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALprobeGetDescr(JNIEnv * jenv, jclass cls, jlong a0, jstring a1) {
  Z3_string _a1 = (Z3_string) jenv->GetStringUTFChars(a1, NULL);
  Z3_string result = Z3_probe_get_descr((Z3_context)a0, _a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jdouble JNICALL Java_com_microsoft_z3_Native_INTERNALprobeApply(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  double result = Z3_probe_apply((Z3_context)a0, (Z3_probe)a1, (Z3_goal)a2);
  return (jdouble) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtacticApply(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_apply_result result = Z3_tactic_apply((Z3_context)a0, (Z3_tactic)a1, (Z3_goal)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALtacticApplyEx(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_apply_result result = Z3_tactic_apply_ex((Z3_context)a0, (Z3_tactic)a1, (Z3_goal)a2, (Z3_params)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALapplyResultIncRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_apply_result_inc_ref((Z3_context)a0, (Z3_apply_result)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALapplyResultDecRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_apply_result_dec_ref((Z3_context)a0, (Z3_apply_result)a1);
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALapplyResultToString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_apply_result_to_string((Z3_context)a0, (Z3_apply_result)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALapplyResultGetNumSubgoals(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_apply_result_get_num_subgoals((Z3_context)a0, (Z3_apply_result)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALapplyResultGetSubgoal(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_goal result = Z3_apply_result_get_subgoal((Z3_context)a0, (Z3_apply_result)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALapplyResultConvertModel(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlong a3) {
  Z3_model result = Z3_apply_result_convert_model((Z3_context)a0, (Z3_apply_result)a1, (unsigned)a2, (Z3_model)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSolver(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_solver result = Z3_mk_solver((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSimpleSolver(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_solver result = Z3_mk_simple_solver((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSolverForLogic(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_solver result = Z3_mk_solver_for_logic((Z3_context)a0, (Z3_symbol)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkSolverFromTactic(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_solver result = Z3_mk_solver_from_tactic((Z3_context)a0, (Z3_tactic)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALsolverTranslate(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_solver result = Z3_solver_translate((Z3_context)a0, (Z3_solver)a1, (Z3_context)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALsolverGetHelp(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_solver_get_help((Z3_context)a0, (Z3_solver)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALsolverGetParamDescrs(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_param_descrs result = Z3_solver_get_param_descrs((Z3_context)a0, (Z3_solver)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALsolverSetParams(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_solver_set_params((Z3_context)a0, (Z3_solver)a1, (Z3_params)a2);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALsolverIncRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_solver_inc_ref((Z3_context)a0, (Z3_solver)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALsolverDecRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_solver_dec_ref((Z3_context)a0, (Z3_solver)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALsolverPush(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_solver_push((Z3_context)a0, (Z3_solver)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALsolverPop(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_solver_pop((Z3_context)a0, (Z3_solver)a1, (unsigned)a2);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALsolverReset(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_solver_reset((Z3_context)a0, (Z3_solver)a1);
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALsolverGetNumScopes(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_solver_get_num_scopes((Z3_context)a0, (Z3_solver)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALsolverAssert(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_solver_assert((Z3_context)a0, (Z3_solver)a1, (Z3_ast)a2);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALsolverAssertAndTrack(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_solver_assert_and_track((Z3_context)a0, (Z3_solver)a1, (Z3_ast)a2, (Z3_ast)a3);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALsolverGetAssertions(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast_vector result = Z3_solver_get_assertions((Z3_context)a0, (Z3_solver)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALsolverCheck(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  int result = Z3_solver_check((Z3_context)a0, (Z3_solver)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALsolverCheckAssumptions(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlongArray a3) {
  GETLONGAELEMS(Z3_ast, a3, _a3);
  int result = Z3_solver_check_assumptions((Z3_context)a0, (Z3_solver)a1, (unsigned)a2, _a3);
  RELEASELONGAELEMS(a3, _a3);
  return (jint) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALgetImpliedEqualities(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlongArray a3, jintArray a4) {
  GETLONGAELEMS(Z3_ast, a3, _a3);
  unsigned * _a4 = (unsigned *) malloc(((unsigned)a2) * sizeof(unsigned));
  jenv->GetIntArrayRegion(a4, 0, (jsize)a2, (jint*)_a4);
  int result = Z3_get_implied_equalities((Z3_context)a0, (Z3_solver)a1, (unsigned)a2, _a3, _a4);
  RELEASELONGAELEMS(a3, _a3);
  jenv->SetIntArrayRegion(a4, 0, (jsize)a2, (jint*)_a4);
  free(_a4);
  return (jint) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALsolverGetConsequences(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3, jlong a4) {
  int result = Z3_solver_get_consequences((Z3_context)a0, (Z3_solver)a1, (Z3_ast_vector)a2, (Z3_ast_vector)a3, (Z3_ast_vector)a4);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALsolverGetModel(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_model result = Z3_solver_get_model((Z3_context)a0, (Z3_solver)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALsolverGetProof(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_solver_get_proof((Z3_context)a0, (Z3_solver)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALsolverGetUnsatCore(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast_vector result = Z3_solver_get_unsat_core((Z3_context)a0, (Z3_solver)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALsolverGetReasonUnknown(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_solver_get_reason_unknown((Z3_context)a0, (Z3_solver)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALsolverGetStatistics(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_stats result = Z3_solver_get_statistics((Z3_context)a0, (Z3_solver)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALsolverToString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_solver_to_string((Z3_context)a0, (Z3_solver)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALstatsToString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_stats_to_string((Z3_context)a0, (Z3_stats)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALstatsIncRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_stats_inc_ref((Z3_context)a0, (Z3_stats)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALstatsDecRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_stats_dec_ref((Z3_context)a0, (Z3_stats)a1);
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALstatsSize(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_stats_size((Z3_context)a0, (Z3_stats)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALstatsGetKey(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_string result = Z3_stats_get_key((Z3_context)a0, (Z3_stats)a1, (unsigned)a2);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALstatsIsUint(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_bool result = Z3_stats_is_uint((Z3_context)a0, (Z3_stats)a1, (unsigned)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALstatsIsDouble(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_bool result = Z3_stats_is_double((Z3_context)a0, (Z3_stats)a1, (unsigned)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALstatsGetUintValue(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  unsigned result = Z3_stats_get_uint_value((Z3_context)a0, (Z3_stats)a1, (unsigned)a2);
  return (jint) result;
}
DLL_VIS JNIEXPORT jdouble JNICALL Java_com_microsoft_z3_Native_INTERNALstatsGetDoubleValue(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  double result = Z3_stats_get_double_value((Z3_context)a0, (Z3_stats)a1, (unsigned)a2);
  return (jdouble) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetEstimatedAllocSize(JNIEnv * jenv, jclass cls) {
  __uint64 result = Z3_get_estimated_alloc_size();
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkAstVector(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_ast_vector result = Z3_mk_ast_vector((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALastVectorIncRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast_vector_inc_ref((Z3_context)a0, (Z3_ast_vector)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALastVectorDecRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast_vector_dec_ref((Z3_context)a0, (Z3_ast_vector)a1);
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALastVectorSize(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_ast_vector_size((Z3_context)a0, (Z3_ast_vector)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALastVectorGet(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_ast result = Z3_ast_vector_get((Z3_context)a0, (Z3_ast_vector)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALastVectorSet(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlong a3) {
  Z3_ast_vector_set((Z3_context)a0, (Z3_ast_vector)a1, (unsigned)a2, (Z3_ast)a3);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALastVectorResize(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_ast_vector_resize((Z3_context)a0, (Z3_ast_vector)a1, (unsigned)a2);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALastVectorPush(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast_vector_push((Z3_context)a0, (Z3_ast_vector)a1, (Z3_ast)a2);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALastVectorTranslate(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast_vector result = Z3_ast_vector_translate((Z3_context)a0, (Z3_ast_vector)a1, (Z3_context)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALastVectorToString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_ast_vector_to_string((Z3_context)a0, (Z3_ast_vector)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkAstMap(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_ast_map result = Z3_mk_ast_map((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALastMapIncRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast_map_inc_ref((Z3_context)a0, (Z3_ast_map)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALastMapDecRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast_map_dec_ref((Z3_context)a0, (Z3_ast_map)a1);
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALastMapContains(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_bool result = Z3_ast_map_contains((Z3_context)a0, (Z3_ast_map)a1, (Z3_ast)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALastMapFind(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_ast_map_find((Z3_context)a0, (Z3_ast_map)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALastMapInsert(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_ast_map_insert((Z3_context)a0, (Z3_ast_map)a1, (Z3_ast)a2, (Z3_ast)a3);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALastMapErase(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast_map_erase((Z3_context)a0, (Z3_ast_map)a1, (Z3_ast)a2);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALastMapReset(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast_map_reset((Z3_context)a0, (Z3_ast_map)a1);
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALastMapSize(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_ast_map_size((Z3_context)a0, (Z3_ast_map)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALastMapKeys(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast_vector result = Z3_ast_map_keys((Z3_context)a0, (Z3_ast_map)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALastMapToString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_ast_map_to_string((Z3_context)a0, (Z3_ast_map)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicIsValue(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_bool result = Z3_algebraic_is_value((Z3_context)a0, (Z3_ast)a1);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicIsPos(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_bool result = Z3_algebraic_is_pos((Z3_context)a0, (Z3_ast)a1);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicIsNeg(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_bool result = Z3_algebraic_is_neg((Z3_context)a0, (Z3_ast)a1);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicIsZero(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_bool result = Z3_algebraic_is_zero((Z3_context)a0, (Z3_ast)a1);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicSign(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  int result = Z3_algebraic_sign((Z3_context)a0, (Z3_ast)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicAdd(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_algebraic_add((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicSub(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_algebraic_sub((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicMul(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_algebraic_mul((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicDiv(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_algebraic_div((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicRoot(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_ast result = Z3_algebraic_root((Z3_context)a0, (Z3_ast)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicPower(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_ast result = Z3_algebraic_power((Z3_context)a0, (Z3_ast)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicLt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_bool result = Z3_algebraic_lt((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicGt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_bool result = Z3_algebraic_gt((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicLe(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_bool result = Z3_algebraic_le((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicGe(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_bool result = Z3_algebraic_ge((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicEq(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_bool result = Z3_algebraic_eq((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicNeq(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_bool result = Z3_algebraic_neq((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicRoots(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlongArray a3) {
  GETLONGAELEMS(Z3_ast, a3, _a3);
  Z3_ast_vector result = Z3_algebraic_roots((Z3_context)a0, (Z3_ast)a1, (unsigned)a2, _a3);
  RELEASELONGAELEMS(a3, _a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALalgebraicEval(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlongArray a3) {
  GETLONGAELEMS(Z3_ast, a3, _a3);
  int result = Z3_algebraic_eval((Z3_context)a0, (Z3_ast)a1, (unsigned)a2, _a3);
  RELEASELONGAELEMS(a3, _a3);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALpolynomialSubresultants(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_ast_vector result = Z3_polynomial_subresultants((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_ast)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALrcfDel(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_rcf_del((Z3_context)a0, (Z3_rcf_num)a1);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALrcfMkRational(JNIEnv * jenv, jclass cls, jlong a0, jstring a1) {
  Z3_string _a1 = (Z3_string) jenv->GetStringUTFChars(a1, NULL);
  Z3_rcf_num result = Z3_rcf_mk_rational((Z3_context)a0, _a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALrcfMkSmallInt(JNIEnv * jenv, jclass cls, jlong a0, jint a1) {
  Z3_rcf_num result = Z3_rcf_mk_small_int((Z3_context)a0, (int)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALrcfMkPi(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_rcf_num result = Z3_rcf_mk_pi((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALrcfMkE(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_rcf_num result = Z3_rcf_mk_e((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALrcfMkInfinitesimal(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_rcf_num result = Z3_rcf_mk_infinitesimal((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALrcfMkRoots(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2, jlongArray a3) {
  GETLONGAELEMS(Z3_rcf_num, a2, _a2);
  Z3_rcf_num * _a3 = (Z3_rcf_num *) malloc(((unsigned)a1) * sizeof(Z3_rcf_num));
  GETLONGAREGION(Z3_rcf_num, a3, 0, a1, _a3);
  unsigned result = Z3_rcf_mk_roots((Z3_context)a0, (unsigned)a1, _a2, _a3);
  RELEASELONGAELEMS(a2, _a2);
  SETLONGAREGION(a3, 0, a1, _a3);
  free(_a3);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALrcfAdd(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_rcf_num result = Z3_rcf_add((Z3_context)a0, (Z3_rcf_num)a1, (Z3_rcf_num)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALrcfSub(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_rcf_num result = Z3_rcf_sub((Z3_context)a0, (Z3_rcf_num)a1, (Z3_rcf_num)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALrcfMul(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_rcf_num result = Z3_rcf_mul((Z3_context)a0, (Z3_rcf_num)a1, (Z3_rcf_num)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALrcfDiv(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_rcf_num result = Z3_rcf_div((Z3_context)a0, (Z3_rcf_num)a1, (Z3_rcf_num)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALrcfNeg(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_rcf_num result = Z3_rcf_neg((Z3_context)a0, (Z3_rcf_num)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALrcfInv(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_rcf_num result = Z3_rcf_inv((Z3_context)a0, (Z3_rcf_num)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALrcfPower(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_rcf_num result = Z3_rcf_power((Z3_context)a0, (Z3_rcf_num)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALrcfLt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_bool result = Z3_rcf_lt((Z3_context)a0, (Z3_rcf_num)a1, (Z3_rcf_num)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALrcfGt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_bool result = Z3_rcf_gt((Z3_context)a0, (Z3_rcf_num)a1, (Z3_rcf_num)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALrcfLe(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_bool result = Z3_rcf_le((Z3_context)a0, (Z3_rcf_num)a1, (Z3_rcf_num)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALrcfGe(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_bool result = Z3_rcf_ge((Z3_context)a0, (Z3_rcf_num)a1, (Z3_rcf_num)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALrcfEq(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_bool result = Z3_rcf_eq((Z3_context)a0, (Z3_rcf_num)a1, (Z3_rcf_num)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALrcfNeq(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_bool result = Z3_rcf_neq((Z3_context)a0, (Z3_rcf_num)a1, (Z3_rcf_num)a2);
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALrcfNumToString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jboolean a2, jboolean a3) {
  Z3_string result = Z3_rcf_num_to_string((Z3_context)a0, (Z3_rcf_num)a1, (Z3_bool)a2, (Z3_bool)a3);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALrcfNumToDecimalString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_string result = Z3_rcf_num_to_decimal_string((Z3_context)a0, (Z3_rcf_num)a1, (unsigned)a2);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALrcfGetNumeratorDenominator(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jobject a2, jobject a3) {
  Z3_rcf_num _a2;
  Z3_rcf_num _a3;
  Z3_rcf_get_numerator_denominator((Z3_context)a0, (Z3_rcf_num)a1, &_a2, &_a3);
  {
     jclass mc    = jenv->GetObjectClass(a2);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a2, fid, (jlong) _a2);
  }
  {
     jclass mc    = jenv->GetObjectClass(a3);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a3, fid, (jlong) _a3);
  }
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFixedpoint(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_fixedpoint result = Z3_mk_fixedpoint((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointIncRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_fixedpoint_inc_ref((Z3_context)a0, (Z3_fixedpoint)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointDecRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_fixedpoint_dec_ref((Z3_context)a0, (Z3_fixedpoint)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointAddRule(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_fixedpoint_add_rule((Z3_context)a0, (Z3_fixedpoint)a1, (Z3_ast)a2, (Z3_symbol)a3);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointAddFact(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jint a3, jintArray a4) {
  unsigned * _a4 = (unsigned*) jenv->GetIntArrayElements(a4, NULL);
  Z3_fixedpoint_add_fact((Z3_context)a0, (Z3_fixedpoint)a1, (Z3_func_decl)a2, (unsigned)a3, _a4);
  jenv->ReleaseIntArrayElements(a4, (jint*)_a4, JNI_ABORT);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointAssert(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_fixedpoint_assert((Z3_context)a0, (Z3_fixedpoint)a1, (Z3_ast)a2);
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointQuery(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  int result = Z3_fixedpoint_query((Z3_context)a0, (Z3_fixedpoint)a1, (Z3_ast)a2);
  return (jint) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointQueryRelations(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlongArray a3) {
  GETLONGAELEMS(Z3_func_decl, a3, _a3);
  int result = Z3_fixedpoint_query_relations((Z3_context)a0, (Z3_fixedpoint)a1, (unsigned)a2, _a3);
  RELEASELONGAELEMS(a3, _a3);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointGetAnswer(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_fixedpoint_get_answer((Z3_context)a0, (Z3_fixedpoint)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointGetReasonUnknown(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_fixedpoint_get_reason_unknown((Z3_context)a0, (Z3_fixedpoint)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointUpdateRule(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_fixedpoint_update_rule((Z3_context)a0, (Z3_fixedpoint)a1, (Z3_ast)a2, (Z3_symbol)a3);
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointGetNumLevels(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  unsigned result = Z3_fixedpoint_get_num_levels((Z3_context)a0, (Z3_fixedpoint)a1, (Z3_func_decl)a2);
  return (jint) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointGetCoverDelta(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlong a3) {
  Z3_ast result = Z3_fixedpoint_get_cover_delta((Z3_context)a0, (Z3_fixedpoint)a1, (int)a2, (Z3_func_decl)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointAddCover(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlong a3, jlong a4) {
  Z3_fixedpoint_add_cover((Z3_context)a0, (Z3_fixedpoint)a1, (int)a2, (Z3_func_decl)a3, (Z3_ast)a4);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointGetStatistics(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_stats result = Z3_fixedpoint_get_statistics((Z3_context)a0, (Z3_fixedpoint)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointRegisterRelation(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_fixedpoint_register_relation((Z3_context)a0, (Z3_fixedpoint)a1, (Z3_func_decl)a2);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointSetPredicateRepresentation(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jint a3, jlongArray a4) {
  GETLONGAELEMS(Z3_symbol, a4, _a4);
  Z3_fixedpoint_set_predicate_representation((Z3_context)a0, (Z3_fixedpoint)a1, (Z3_func_decl)a2, (unsigned)a3, _a4);
  RELEASELONGAELEMS(a4, _a4);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointGetRules(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast_vector result = Z3_fixedpoint_get_rules((Z3_context)a0, (Z3_fixedpoint)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointGetAssertions(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast_vector result = Z3_fixedpoint_get_assertions((Z3_context)a0, (Z3_fixedpoint)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointSetParams(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_fixedpoint_set_params((Z3_context)a0, (Z3_fixedpoint)a1, (Z3_params)a2);
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointGetHelp(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_fixedpoint_get_help((Z3_context)a0, (Z3_fixedpoint)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointGetParamDescrs(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_param_descrs result = Z3_fixedpoint_get_param_descrs((Z3_context)a0, (Z3_fixedpoint)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointToString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2, jlongArray a3) {
  GETLONGAELEMS(Z3_ast, a3, _a3);
  Z3_string result = Z3_fixedpoint_to_string((Z3_context)a0, (Z3_fixedpoint)a1, (unsigned)a2, _a3);
  RELEASELONGAELEMS(a3, _a3);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointFromString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jstring a2) {
  Z3_string _a2 = (Z3_string) jenv->GetStringUTFChars(a2, NULL);
  Z3_ast_vector result = Z3_fixedpoint_from_string((Z3_context)a0, (Z3_fixedpoint)a1, _a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointFromFile(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jstring a2) {
  Z3_string _a2 = (Z3_string) jenv->GetStringUTFChars(a2, NULL);
  Z3_ast_vector result = Z3_fixedpoint_from_file((Z3_context)a0, (Z3_fixedpoint)a1, _a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointPush(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_fixedpoint_push((Z3_context)a0, (Z3_fixedpoint)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALfixedpointPop(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_fixedpoint_pop((Z3_context)a0, (Z3_fixedpoint)a1);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkOptimize(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_optimize result = Z3_mk_optimize((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeIncRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_optimize_inc_ref((Z3_context)a0, (Z3_optimize)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeDecRef(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_optimize_dec_ref((Z3_context)a0, (Z3_optimize)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeAssert(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_optimize_assert((Z3_context)a0, (Z3_optimize)a1, (Z3_ast)a2);
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeAssertSoft(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jstring a3, jlong a4) {
  Z3_string _a3 = (Z3_string) jenv->GetStringUTFChars(a3, NULL);
  unsigned result = Z3_optimize_assert_soft((Z3_context)a0, (Z3_optimize)a1, (Z3_ast)a2, _a3, (Z3_symbol)a4);
  return (jint) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeMaximize(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  unsigned result = Z3_optimize_maximize((Z3_context)a0, (Z3_optimize)a1, (Z3_ast)a2);
  return (jint) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeMinimize(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  unsigned result = Z3_optimize_minimize((Z3_context)a0, (Z3_optimize)a1, (Z3_ast)a2);
  return (jint) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizePush(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_optimize_push((Z3_context)a0, (Z3_optimize)a1);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizePop(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_optimize_pop((Z3_context)a0, (Z3_optimize)a1);
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeCheck(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  int result = Z3_optimize_check((Z3_context)a0, (Z3_optimize)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeGetReasonUnknown(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_optimize_get_reason_unknown((Z3_context)a0, (Z3_optimize)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeGetModel(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_model result = Z3_optimize_get_model((Z3_context)a0, (Z3_optimize)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeSetParams(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_optimize_set_params((Z3_context)a0, (Z3_optimize)a1, (Z3_params)a2);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeGetParamDescrs(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_param_descrs result = Z3_optimize_get_param_descrs((Z3_context)a0, (Z3_optimize)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeGetLower(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_ast result = Z3_optimize_get_lower((Z3_context)a0, (Z3_optimize)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeGetUpper(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jint a2) {
  Z3_ast result = Z3_optimize_get_upper((Z3_context)a0, (Z3_optimize)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeToString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_optimize_to_string((Z3_context)a0, (Z3_optimize)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeFromString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jstring a2) {
  Z3_string _a2 = (Z3_string) jenv->GetStringUTFChars(a2, NULL);
  Z3_optimize_from_string((Z3_context)a0, (Z3_optimize)a1, _a2);
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeFromFile(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jstring a2) {
  Z3_string _a2 = (Z3_string) jenv->GetStringUTFChars(a2, NULL);
  Z3_optimize_from_file((Z3_context)a0, (Z3_optimize)a1, _a2);
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeGetHelp(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_optimize_get_help((Z3_context)a0, (Z3_optimize)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeGetStatistics(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_stats result = Z3_optimize_get_statistics((Z3_context)a0, (Z3_optimize)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeGetAssertions(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast_vector result = Z3_optimize_get_assertions((Z3_context)a0, (Z3_optimize)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALoptimizeGetObjectives(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast_vector result = Z3_optimize_get_objectives((Z3_context)a0, (Z3_optimize)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkInterpolant(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_interpolant((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkInterpolationContext(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_context result = Z3_mk_interpolation_context((Z3_config)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALgetInterpolant(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_ast_vector result = Z3_get_interpolant((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_params)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALcomputeInterpolant(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jobject a3, jobject a4) {
  Z3_ast_vector _a3;
  Z3_model _a4;
  int result = Z3_compute_interpolant((Z3_context)a0, (Z3_ast)a1, (Z3_params)a2, &_a3, &_a4);
  {
     jclass mc    = jenv->GetObjectClass(a3);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a3, fid, (jlong) _a3);
  }
  {
     jclass mc    = jenv->GetObjectClass(a4);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a4, fid, (jlong) _a4);
  }
  return (jint) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALinterpolationProfile(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_string result = Z3_interpolation_profile((Z3_context)a0);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALreadInterpolationProblem(JNIEnv * jenv, jclass cls, jlong a0, jobject a1, jlong a2, jlong a3, jstring a4, jobject a5, jobject a6, jlong a7) {
  unsigned _a1;
  Z3_ast * _a2 = 0;
  unsigned * _a3 = 0;
  Z3_string _a4 = (Z3_string) jenv->GetStringUTFChars(a4, NULL);
  Z3_string _a5;
  unsigned _a6;
  Z3_ast * _a7 = 0;
  int result = Z3_read_interpolation_problem((Z3_context)a0, &_a1, &_a2, &_a3, _a4, &_a5, &_a6, &_a7);
  {
     jclass mc    = jenv->GetObjectClass(a1);
     jfieldID fid = jenv->GetFieldID(mc, "value", "I");
     jenv->SetIntField(a1, fid, (jint) _a1);
  }
  *(jlong**)a2 = (jlong*)_a2;
  *(jlong**)a3 = (jlong*)_a3;
  {
     jclass mc    = jenv->GetObjectClass(a5);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a5, fid, (jlong) _a5);
  }
  {
     jclass mc    = jenv->GetObjectClass(a6);
     jfieldID fid = jenv->GetFieldID(mc, "value", "I");
     jenv->SetIntField(a6, fid, (jint) _a6);
  }
  *(jlong**)a7 = (jlong*)_a7;
  return (jint) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALcheckInterpolant(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2, jintArray a3, jlongArray a4, jobject a5, jint a6, jlongArray a7) {
  GETLONGAELEMS(Z3_ast, a2, _a2);
  unsigned * _a3 = (unsigned*) jenv->GetIntArrayElements(a3, NULL);
  GETLONGAELEMS(Z3_ast, a4, _a4);
  Z3_string _a5;
  GETLONGAELEMS(Z3_ast, a7, _a7);
  int result = Z3_check_interpolant((Z3_context)a0, (unsigned)a1, _a2, _a3, _a4, &_a5, (unsigned)a6, _a7);
  RELEASELONGAELEMS(a2, _a2);
  jenv->ReleaseIntArrayElements(a3, (jint*)_a3, JNI_ABORT);
  RELEASELONGAELEMS(a4, _a4);
  {
     jclass mc    = jenv->GetObjectClass(a5);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a5, fid, (jlong) _a5);
  }
  RELEASELONGAELEMS(a7, _a7);
  return (jint) result;
}
DLL_VIS JNIEXPORT void JNICALL Java_com_microsoft_z3_Native_INTERNALwriteInterpolationProblem(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlongArray a2, jintArray a3, jstring a4, jint a5, jlongArray a6) {
  GETLONGAELEMS(Z3_ast, a2, _a2);
  unsigned * _a3 = (unsigned*) jenv->GetIntArrayElements(a3, NULL);
  Z3_string _a4 = (Z3_string) jenv->GetStringUTFChars(a4, NULL);
  GETLONGAELEMS(Z3_ast, a6, _a6);
  Z3_write_interpolation_problem((Z3_context)a0, (unsigned)a1, _a2, _a3, _a4, (unsigned)a5, _a6);
  RELEASELONGAELEMS(a2, _a2);
  jenv->ReleaseIntArrayElements(a3, (jint*)_a3, JNI_ABORT);
  RELEASELONGAELEMS(a6, _a6);
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaRoundingModeSort(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_sort result = Z3_mk_fpa_rounding_mode_sort((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaRoundNearestTiesToEven(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_ast result = Z3_mk_fpa_round_nearest_ties_to_even((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaRne(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_ast result = Z3_mk_fpa_rne((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaRoundNearestTiesToAway(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_ast result = Z3_mk_fpa_round_nearest_ties_to_away((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaRna(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_ast result = Z3_mk_fpa_rna((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaRoundTowardPositive(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_ast result = Z3_mk_fpa_round_toward_positive((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaRtp(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_ast result = Z3_mk_fpa_rtp((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaRoundTowardNegative(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_ast result = Z3_mk_fpa_round_toward_negative((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaRtn(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_ast result = Z3_mk_fpa_rtn((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaRoundTowardZero(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_ast result = Z3_mk_fpa_round_toward_zero((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaRtz(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_ast result = Z3_mk_fpa_rtz((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaSort(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jint a2) {
  Z3_sort result = Z3_mk_fpa_sort((Z3_context)a0, (unsigned)a1, (unsigned)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaSortHalf(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_sort result = Z3_mk_fpa_sort_half((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaSort16(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_sort result = Z3_mk_fpa_sort_16((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaSortSingle(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_sort result = Z3_mk_fpa_sort_single((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaSort32(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_sort result = Z3_mk_fpa_sort_32((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaSortDouble(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_sort result = Z3_mk_fpa_sort_double((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaSort64(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_sort result = Z3_mk_fpa_sort_64((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaSortQuadruple(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_sort result = Z3_mk_fpa_sort_quadruple((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaSort128(JNIEnv * jenv, jclass cls, jlong a0) {
  Z3_sort result = Z3_mk_fpa_sort_128((Z3_context)a0);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaNan(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_fpa_nan((Z3_context)a0, (Z3_sort)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaInf(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jboolean a2) {
  Z3_ast result = Z3_mk_fpa_inf((Z3_context)a0, (Z3_sort)a1, (Z3_bool)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaZero(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jboolean a2) {
  Z3_ast result = Z3_mk_fpa_zero((Z3_context)a0, (Z3_sort)a1, (Z3_bool)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaFp(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_ast result = Z3_mk_fpa_fp((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_ast)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaNumeralFloat(JNIEnv * jenv, jclass cls, jlong a0, jfloat a1, jlong a2) {
  Z3_ast result = Z3_mk_fpa_numeral_float((Z3_context)a0, (float)a1, (Z3_sort)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaNumeralDouble(JNIEnv * jenv, jclass cls, jlong a0, jdouble a1, jlong a2) {
  Z3_ast result = Z3_mk_fpa_numeral_double((Z3_context)a0, (double)a1, (Z3_sort)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaNumeralInt(JNIEnv * jenv, jclass cls, jlong a0, jint a1, jlong a2) {
  Z3_ast result = Z3_mk_fpa_numeral_int((Z3_context)a0, (int)a1, (Z3_sort)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaNumeralIntUint(JNIEnv * jenv, jclass cls, jlong a0, jboolean a1, jint a2, jint a3, jlong a4) {
  Z3_ast result = Z3_mk_fpa_numeral_int_uint((Z3_context)a0, (Z3_bool)a1, (int)a2, (unsigned)a3, (Z3_sort)a4);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaNumeralInt64Uint64(JNIEnv * jenv, jclass cls, jlong a0, jboolean a1, jlong a2, jlong a3, jlong a4) {
  Z3_ast result = Z3_mk_fpa_numeral_int64_uint64((Z3_context)a0, (Z3_bool)a1, (__int64)a2, (__uint64)a3, (Z3_sort)a4);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaAbs(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_fpa_abs((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaNeg(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_fpa_neg((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaAdd(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_ast result = Z3_mk_fpa_add((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_ast)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaSub(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_ast result = Z3_mk_fpa_sub((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_ast)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaMul(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_ast result = Z3_mk_fpa_mul((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_ast)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaDiv(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_ast result = Z3_mk_fpa_div((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_ast)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaFma(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3, jlong a4) {
  Z3_ast result = Z3_mk_fpa_fma((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_ast)a3, (Z3_ast)a4);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaSqrt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_fpa_sqrt((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaRem(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_fpa_rem((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaRoundToIntegral(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_fpa_round_to_integral((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaMin(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_fpa_min((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaMax(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_fpa_max((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaLeq(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_fpa_leq((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaLt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_fpa_lt((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaGeq(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_fpa_geq((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaGt(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_fpa_gt((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaEq(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_fpa_eq((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaIsNormal(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_fpa_is_normal((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaIsSubnormal(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_fpa_is_subnormal((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaIsZero(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_fpa_is_zero((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaIsInfinite(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_fpa_is_infinite((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaIsNan(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_fpa_is_nan((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaIsNegative(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_fpa_is_negative((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaIsPositive(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_fpa_is_positive((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaToFpBv(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2) {
  Z3_ast result = Z3_mk_fpa_to_fp_bv((Z3_context)a0, (Z3_ast)a1, (Z3_sort)a2);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaToFpFloat(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_ast result = Z3_mk_fpa_to_fp_float((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_sort)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaToFpReal(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_ast result = Z3_mk_fpa_to_fp_real((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_sort)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaToFpSigned(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_ast result = Z3_mk_fpa_to_fp_signed((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_sort)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaToFpUnsigned(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3) {
  Z3_ast result = Z3_mk_fpa_to_fp_unsigned((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_sort)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaToUbv(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jint a3) {
  Z3_ast result = Z3_mk_fpa_to_ubv((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (unsigned)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaToSbv(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jint a3) {
  Z3_ast result = Z3_mk_fpa_to_sbv((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (unsigned)a3);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaToReal(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_fpa_to_real((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALfpaGetEbits(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_fpa_get_ebits((Z3_context)a0, (Z3_sort)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jint JNICALL Java_com_microsoft_z3_Native_INTERNALfpaGetSbits(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  unsigned result = Z3_fpa_get_sbits((Z3_context)a0, (Z3_sort)a1);
  return (jint) result;
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALfpaGetNumeralSign(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jobject a2) {
  int _a2;
  Z3_bool result = Z3_fpa_get_numeral_sign((Z3_context)a0, (Z3_ast)a1, &_a2);
  {
     jclass mc    = jenv->GetObjectClass(a2);
     jfieldID fid = jenv->GetFieldID(mc, "value", "I");
     jenv->SetIntField(a2, fid, (jint) _a2);
  }
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALfpaGetNumeralSignificandString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_fpa_get_numeral_significand_string((Z3_context)a0, (Z3_ast)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALfpaGetNumeralSignificandUint64(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jobject a2) {
  __uint64 _a2;
  Z3_bool result = Z3_fpa_get_numeral_significand_uint64((Z3_context)a0, (Z3_ast)a1, &_a2);
  {
     jclass mc    = jenv->GetObjectClass(a2);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a2, fid, (jlong) _a2);
  }
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jstring JNICALL Java_com_microsoft_z3_Native_INTERNALfpaGetNumeralExponentString(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_string result = Z3_fpa_get_numeral_exponent_string((Z3_context)a0, (Z3_ast)a1);
  return jenv->NewStringUTF(result);
}
DLL_VIS JNIEXPORT jboolean JNICALL Java_com_microsoft_z3_Native_INTERNALfpaGetNumeralExponentInt64(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jobject a2) {
  __int64 _a2;
  Z3_bool result = Z3_fpa_get_numeral_exponent_int64((Z3_context)a0, (Z3_ast)a1, &_a2);
  {
     jclass mc    = jenv->GetObjectClass(a2);
     jfieldID fid = jenv->GetFieldID(mc, "value", "J");
     jenv->SetLongField(a2, fid, (jlong) _a2);
  }
  return (jboolean) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaToIeeeBv(JNIEnv * jenv, jclass cls, jlong a0, jlong a1) {
  Z3_ast result = Z3_mk_fpa_to_ieee_bv((Z3_context)a0, (Z3_ast)a1);
  return (jlong) result;
}
DLL_VIS JNIEXPORT jlong JNICALL Java_com_microsoft_z3_Native_INTERNALmkFpaToFpIntReal(JNIEnv * jenv, jclass cls, jlong a0, jlong a1, jlong a2, jlong a3, jlong a4) {
  Z3_ast result = Z3_mk_fpa_to_fp_int_real((Z3_context)a0, (Z3_ast)a1, (Z3_ast)a2, (Z3_ast)a3, (Z3_sort)a4);
  return (jlong) result;
}
#ifdef __cplusplus
}
#endif
