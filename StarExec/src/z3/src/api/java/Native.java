// Automatically generated file
package com.microsoft.z3;
import com.microsoft.z3.enumerations.*;
public final class Native {
  public static class IntPtr { public int value; }
  public static class LongPtr { public long value; }
  public static class StringPtr { public String value; }
  public static class ObjArrayPtr { public long[] value; }
  public static class UIntArrayPtr { public int[] value; }
  public static native void setInternalErrorHandler(long ctx);

  static {
    try { System.loadLibrary("z3java"); }
    catch (UnsatisfiedLinkError ex) { System.loadLibrary("libz3java"); }
  }

  protected static native void INTERNALglobalParamSet(String a0, String a1);
  protected static native void INTERNALglobalParamResetAll();
  protected static native boolean INTERNALglobalParamGet(String a0, StringPtr a1);
  protected static native long INTERNALmkConfig();
  protected static native void INTERNALdelConfig(long a0);
  protected static native void INTERNALsetParamValue(long a0, String a1, String a2);
  protected static native long INTERNALmkContext(long a0);
  protected static native long INTERNALmkContextRc(long a0);
  protected static native void INTERNALdelContext(long a0);
  protected static native void INTERNALincRef(long a0, long a1);
  protected static native void INTERNALdecRef(long a0, long a1);
  protected static native void INTERNALupdateParamValue(long a0, String a1, String a2);
  protected static native void INTERNALinterrupt(long a0);
  protected static native long INTERNALmkParams(long a0);
  protected static native void INTERNALparamsIncRef(long a0, long a1);
  protected static native void INTERNALparamsDecRef(long a0, long a1);
  protected static native void INTERNALparamsSetBool(long a0, long a1, long a2, boolean a3);
  protected static native void INTERNALparamsSetUint(long a0, long a1, long a2, int a3);
  protected static native void INTERNALparamsSetDouble(long a0, long a1, long a2, double a3);
  protected static native void INTERNALparamsSetSymbol(long a0, long a1, long a2, long a3);
  protected static native String INTERNALparamsToString(long a0, long a1);
  protected static native void INTERNALparamsValidate(long a0, long a1, long a2);
  protected static native void INTERNALparamDescrsIncRef(long a0, long a1);
  protected static native void INTERNALparamDescrsDecRef(long a0, long a1);
  protected static native int INTERNALparamDescrsGetKind(long a0, long a1, long a2);
  protected static native int INTERNALparamDescrsSize(long a0, long a1);
  protected static native long INTERNALparamDescrsGetName(long a0, long a1, int a2);
  protected static native String INTERNALparamDescrsGetDocumentation(long a0, long a1, long a2);
  protected static native String INTERNALparamDescrsToString(long a0, long a1);
  protected static native long INTERNALmkIntSymbol(long a0, int a1);
  protected static native long INTERNALmkStringSymbol(long a0, String a1);
  protected static native long INTERNALmkUninterpretedSort(long a0, long a1);
  protected static native long INTERNALmkBoolSort(long a0);
  protected static native long INTERNALmkIntSort(long a0);
  protected static native long INTERNALmkRealSort(long a0);
  protected static native long INTERNALmkBvSort(long a0, int a1);
  protected static native long INTERNALmkFiniteDomainSort(long a0, long a1, long a2);
  protected static native long INTERNALmkArraySort(long a0, long a1, long a2);
  protected static native long INTERNALmkTupleSort(long a0, long a1, int a2, long[] a3, long[] a4, LongPtr a5, long[] a6);
  protected static native long INTERNALmkEnumerationSort(long a0, long a1, int a2, long[] a3, long[] a4, long[] a5);
  protected static native long INTERNALmkListSort(long a0, long a1, long a2, LongPtr a3, LongPtr a4, LongPtr a5, LongPtr a6, LongPtr a7, LongPtr a8);
  protected static native long INTERNALmkConstructor(long a0, long a1, long a2, int a3, long[] a4, long[] a5, int[] a6);
  protected static native void INTERNALdelConstructor(long a0, long a1);
  protected static native long INTERNALmkDatatype(long a0, long a1, int a2, long[] a3);
  protected static native long INTERNALmkConstructorList(long a0, int a1, long[] a2);
  protected static native void INTERNALdelConstructorList(long a0, long a1);
  protected static native void INTERNALmkDatatypes(long a0, int a1, long[] a2, long[] a3, long[] a4);
  protected static native void INTERNALqueryConstructor(long a0, long a1, int a2, LongPtr a3, LongPtr a4, long[] a5);
  protected static native long INTERNALmkFuncDecl(long a0, long a1, int a2, long[] a3, long a4);
  protected static native long INTERNALmkApp(long a0, long a1, int a2, long[] a3);
  protected static native long INTERNALmkConst(long a0, long a1, long a2);
  protected static native long INTERNALmkFreshFuncDecl(long a0, String a1, int a2, long[] a3, long a4);
  protected static native long INTERNALmkFreshConst(long a0, String a1, long a2);
  protected static native long INTERNALmkTrue(long a0);
  protected static native long INTERNALmkFalse(long a0);
  protected static native long INTERNALmkEq(long a0, long a1, long a2);
  protected static native long INTERNALmkDistinct(long a0, int a1, long[] a2);
  protected static native long INTERNALmkNot(long a0, long a1);
  protected static native long INTERNALmkIte(long a0, long a1, long a2, long a3);
  protected static native long INTERNALmkIff(long a0, long a1, long a2);
  protected static native long INTERNALmkImplies(long a0, long a1, long a2);
  protected static native long INTERNALmkXor(long a0, long a1, long a2);
  protected static native long INTERNALmkAnd(long a0, int a1, long[] a2);
  protected static native long INTERNALmkOr(long a0, int a1, long[] a2);
  protected static native long INTERNALmkAdd(long a0, int a1, long[] a2);
  protected static native long INTERNALmkMul(long a0, int a1, long[] a2);
  protected static native long INTERNALmkSub(long a0, int a1, long[] a2);
  protected static native long INTERNALmkUnaryMinus(long a0, long a1);
  protected static native long INTERNALmkDiv(long a0, long a1, long a2);
  protected static native long INTERNALmkMod(long a0, long a1, long a2);
  protected static native long INTERNALmkRem(long a0, long a1, long a2);
  protected static native long INTERNALmkPower(long a0, long a1, long a2);
  protected static native long INTERNALmkLt(long a0, long a1, long a2);
  protected static native long INTERNALmkLe(long a0, long a1, long a2);
  protected static native long INTERNALmkGt(long a0, long a1, long a2);
  protected static native long INTERNALmkGe(long a0, long a1, long a2);
  protected static native long INTERNALmkInt2real(long a0, long a1);
  protected static native long INTERNALmkReal2int(long a0, long a1);
  protected static native long INTERNALmkIsInt(long a0, long a1);
  protected static native long INTERNALmkBvnot(long a0, long a1);
  protected static native long INTERNALmkBvredand(long a0, long a1);
  protected static native long INTERNALmkBvredor(long a0, long a1);
  protected static native long INTERNALmkBvand(long a0, long a1, long a2);
  protected static native long INTERNALmkBvor(long a0, long a1, long a2);
  protected static native long INTERNALmkBvxor(long a0, long a1, long a2);
  protected static native long INTERNALmkBvnand(long a0, long a1, long a2);
  protected static native long INTERNALmkBvnor(long a0, long a1, long a2);
  protected static native long INTERNALmkBvxnor(long a0, long a1, long a2);
  protected static native long INTERNALmkBvneg(long a0, long a1);
  protected static native long INTERNALmkBvadd(long a0, long a1, long a2);
  protected static native long INTERNALmkBvsub(long a0, long a1, long a2);
  protected static native long INTERNALmkBvmul(long a0, long a1, long a2);
  protected static native long INTERNALmkBvudiv(long a0, long a1, long a2);
  protected static native long INTERNALmkBvsdiv(long a0, long a1, long a2);
  protected static native long INTERNALmkBvurem(long a0, long a1, long a2);
  protected static native long INTERNALmkBvsrem(long a0, long a1, long a2);
  protected static native long INTERNALmkBvsmod(long a0, long a1, long a2);
  protected static native long INTERNALmkBvult(long a0, long a1, long a2);
  protected static native long INTERNALmkBvslt(long a0, long a1, long a2);
  protected static native long INTERNALmkBvule(long a0, long a1, long a2);
  protected static native long INTERNALmkBvsle(long a0, long a1, long a2);
  protected static native long INTERNALmkBvuge(long a0, long a1, long a2);
  protected static native long INTERNALmkBvsge(long a0, long a1, long a2);
  protected static native long INTERNALmkBvugt(long a0, long a1, long a2);
  protected static native long INTERNALmkBvsgt(long a0, long a1, long a2);
  protected static native long INTERNALmkConcat(long a0, long a1, long a2);
  protected static native long INTERNALmkExtract(long a0, int a1, int a2, long a3);
  protected static native long INTERNALmkSignExt(long a0, int a1, long a2);
  protected static native long INTERNALmkZeroExt(long a0, int a1, long a2);
  protected static native long INTERNALmkRepeat(long a0, int a1, long a2);
  protected static native long INTERNALmkBvshl(long a0, long a1, long a2);
  protected static native long INTERNALmkBvlshr(long a0, long a1, long a2);
  protected static native long INTERNALmkBvashr(long a0, long a1, long a2);
  protected static native long INTERNALmkRotateLeft(long a0, int a1, long a2);
  protected static native long INTERNALmkRotateRight(long a0, int a1, long a2);
  protected static native long INTERNALmkExtRotateLeft(long a0, long a1, long a2);
  protected static native long INTERNALmkExtRotateRight(long a0, long a1, long a2);
  protected static native long INTERNALmkInt2bv(long a0, int a1, long a2);
  protected static native long INTERNALmkBv2int(long a0, long a1, boolean a2);
  protected static native long INTERNALmkBvaddNoOverflow(long a0, long a1, long a2, boolean a3);
  protected static native long INTERNALmkBvaddNoUnderflow(long a0, long a1, long a2);
  protected static native long INTERNALmkBvsubNoOverflow(long a0, long a1, long a2);
  protected static native long INTERNALmkBvsubNoUnderflow(long a0, long a1, long a2, boolean a3);
  protected static native long INTERNALmkBvsdivNoOverflow(long a0, long a1, long a2);
  protected static native long INTERNALmkBvnegNoOverflow(long a0, long a1);
  protected static native long INTERNALmkBvmulNoOverflow(long a0, long a1, long a2, boolean a3);
  protected static native long INTERNALmkBvmulNoUnderflow(long a0, long a1, long a2);
  protected static native long INTERNALmkSelect(long a0, long a1, long a2);
  protected static native long INTERNALmkStore(long a0, long a1, long a2, long a3);
  protected static native long INTERNALmkConstArray(long a0, long a1, long a2);
  protected static native long INTERNALmkMap(long a0, long a1, int a2, long[] a3);
  protected static native long INTERNALmkArrayDefault(long a0, long a1);
  protected static native long INTERNALmkSetSort(long a0, long a1);
  protected static native long INTERNALmkEmptySet(long a0, long a1);
  protected static native long INTERNALmkFullSet(long a0, long a1);
  protected static native long INTERNALmkSetAdd(long a0, long a1, long a2);
  protected static native long INTERNALmkSetDel(long a0, long a1, long a2);
  protected static native long INTERNALmkSetUnion(long a0, int a1, long[] a2);
  protected static native long INTERNALmkSetIntersect(long a0, int a1, long[] a2);
  protected static native long INTERNALmkSetDifference(long a0, long a1, long a2);
  protected static native long INTERNALmkSetComplement(long a0, long a1);
  protected static native long INTERNALmkSetMember(long a0, long a1, long a2);
  protected static native long INTERNALmkSetSubset(long a0, long a1, long a2);
  protected static native long INTERNALmkArrayExt(long a0, long a1, long a2);
  protected static native long INTERNALmkNumeral(long a0, String a1, long a2);
  protected static native long INTERNALmkReal(long a0, int a1, int a2);
  protected static native long INTERNALmkInt(long a0, int a1, long a2);
  protected static native long INTERNALmkUnsignedInt(long a0, int a1, long a2);
  protected static native long INTERNALmkInt64(long a0, long a1, long a2);
  protected static native long INTERNALmkUnsignedInt64(long a0, long a1, long a2);
  protected static native long INTERNALmkSeqSort(long a0, long a1);
  protected static native boolean INTERNALisSeqSort(long a0, long a1);
  protected static native long INTERNALmkReSort(long a0, long a1);
  protected static native boolean INTERNALisReSort(long a0, long a1);
  protected static native long INTERNALmkStringSort(long a0);
  protected static native boolean INTERNALisStringSort(long a0, long a1);
  protected static native long INTERNALmkString(long a0, String a1);
  protected static native boolean INTERNALisString(long a0, long a1);
  protected static native String INTERNALgetString(long a0, long a1);
  protected static native long INTERNALmkSeqEmpty(long a0, long a1);
  protected static native long INTERNALmkSeqUnit(long a0, long a1);
  protected static native long INTERNALmkSeqConcat(long a0, int a1, long[] a2);
  protected static native long INTERNALmkSeqPrefix(long a0, long a1, long a2);
  protected static native long INTERNALmkSeqSuffix(long a0, long a1, long a2);
  protected static native long INTERNALmkSeqContains(long a0, long a1, long a2);
  protected static native long INTERNALmkSeqExtract(long a0, long a1, long a2, long a3);
  protected static native long INTERNALmkSeqReplace(long a0, long a1, long a2, long a3);
  protected static native long INTERNALmkSeqAt(long a0, long a1, long a2);
  protected static native long INTERNALmkSeqLength(long a0, long a1);
  protected static native long INTERNALmkSeqIndex(long a0, long a1, long a2, long a3);
  protected static native long INTERNALmkSeqToRe(long a0, long a1);
  protected static native long INTERNALmkSeqInRe(long a0, long a1, long a2);
  protected static native long INTERNALmkRePlus(long a0, long a1);
  protected static native long INTERNALmkReStar(long a0, long a1);
  protected static native long INTERNALmkReOption(long a0, long a1);
  protected static native long INTERNALmkReUnion(long a0, int a1, long[] a2);
  protected static native long INTERNALmkReConcat(long a0, int a1, long[] a2);
  protected static native long INTERNALmkPattern(long a0, int a1, long[] a2);
  protected static native long INTERNALmkBound(long a0, int a1, long a2);
  protected static native long INTERNALmkForall(long a0, int a1, int a2, long[] a3, int a4, long[] a5, long[] a6, long a7);
  protected static native long INTERNALmkExists(long a0, int a1, int a2, long[] a3, int a4, long[] a5, long[] a6, long a7);
  protected static native long INTERNALmkQuantifier(long a0, boolean a1, int a2, int a3, long[] a4, int a5, long[] a6, long[] a7, long a8);
  protected static native long INTERNALmkQuantifierEx(long a0, boolean a1, int a2, long a3, long a4, int a5, long[] a6, int a7, long[] a8, int a9, long[] a10, long[] a11, long a12);
  protected static native long INTERNALmkForallConst(long a0, int a1, int a2, long[] a3, int a4, long[] a5, long a6);
  protected static native long INTERNALmkExistsConst(long a0, int a1, int a2, long[] a3, int a4, long[] a5, long a6);
  protected static native long INTERNALmkQuantifierConst(long a0, boolean a1, int a2, int a3, long[] a4, int a5, long[] a6, long a7);
  protected static native long INTERNALmkQuantifierConstEx(long a0, boolean a1, int a2, long a3, long a4, int a5, long[] a6, int a7, long[] a8, int a9, long[] a10, long a11);
  protected static native int INTERNALgetSymbolKind(long a0, long a1);
  protected static native int INTERNALgetSymbolInt(long a0, long a1);
  protected static native String INTERNALgetSymbolString(long a0, long a1);
  protected static native long INTERNALgetSortName(long a0, long a1);
  protected static native int INTERNALgetSortId(long a0, long a1);
  protected static native long INTERNALsortToAst(long a0, long a1);
  protected static native boolean INTERNALisEqSort(long a0, long a1, long a2);
  protected static native int INTERNALgetSortKind(long a0, long a1);
  protected static native int INTERNALgetBvSortSize(long a0, long a1);
  protected static native boolean INTERNALgetFiniteDomainSortSize(long a0, long a1, LongPtr a2);
  protected static native long INTERNALgetArraySortDomain(long a0, long a1);
  protected static native long INTERNALgetArraySortRange(long a0, long a1);
  protected static native long INTERNALgetTupleSortMkDecl(long a0, long a1);
  protected static native int INTERNALgetTupleSortNumFields(long a0, long a1);
  protected static native long INTERNALgetTupleSortFieldDecl(long a0, long a1, int a2);
  protected static native int INTERNALgetDatatypeSortNumConstructors(long a0, long a1);
  protected static native long INTERNALgetDatatypeSortConstructor(long a0, long a1, int a2);
  protected static native long INTERNALgetDatatypeSortRecognizer(long a0, long a1, int a2);
  protected static native long INTERNALgetDatatypeSortConstructorAccessor(long a0, long a1, int a2, int a3);
  protected static native long INTERNALdatatypeUpdateField(long a0, long a1, long a2, long a3);
  protected static native int INTERNALgetRelationArity(long a0, long a1);
  protected static native long INTERNALgetRelationColumn(long a0, long a1, int a2);
  protected static native long INTERNALmkAtmost(long a0, int a1, long[] a2, int a3);
  protected static native long INTERNALmkPble(long a0, int a1, long[] a2, int[] a3, int a4);
  protected static native long INTERNALmkPbeq(long a0, int a1, long[] a2, int[] a3, int a4);
  protected static native long INTERNALfuncDeclToAst(long a0, long a1);
  protected static native boolean INTERNALisEqFuncDecl(long a0, long a1, long a2);
  protected static native int INTERNALgetFuncDeclId(long a0, long a1);
  protected static native long INTERNALgetDeclName(long a0, long a1);
  protected static native int INTERNALgetDeclKind(long a0, long a1);
  protected static native int INTERNALgetDomainSize(long a0, long a1);
  protected static native int INTERNALgetArity(long a0, long a1);
  protected static native long INTERNALgetDomain(long a0, long a1, int a2);
  protected static native long INTERNALgetRange(long a0, long a1);
  protected static native int INTERNALgetDeclNumParameters(long a0, long a1);
  protected static native int INTERNALgetDeclParameterKind(long a0, long a1, int a2);
  protected static native int INTERNALgetDeclIntParameter(long a0, long a1, int a2);
  protected static native double INTERNALgetDeclDoubleParameter(long a0, long a1, int a2);
  protected static native long INTERNALgetDeclSymbolParameter(long a0, long a1, int a2);
  protected static native long INTERNALgetDeclSortParameter(long a0, long a1, int a2);
  protected static native long INTERNALgetDeclAstParameter(long a0, long a1, int a2);
  protected static native long INTERNALgetDeclFuncDeclParameter(long a0, long a1, int a2);
  protected static native String INTERNALgetDeclRationalParameter(long a0, long a1, int a2);
  protected static native long INTERNALappToAst(long a0, long a1);
  protected static native long INTERNALgetAppDecl(long a0, long a1);
  protected static native int INTERNALgetAppNumArgs(long a0, long a1);
  protected static native long INTERNALgetAppArg(long a0, long a1, int a2);
  protected static native boolean INTERNALisEqAst(long a0, long a1, long a2);
  protected static native int INTERNALgetAstId(long a0, long a1);
  protected static native int INTERNALgetAstHash(long a0, long a1);
  protected static native long INTERNALgetSort(long a0, long a1);
  protected static native boolean INTERNALisWellSorted(long a0, long a1);
  protected static native int INTERNALgetBoolValue(long a0, long a1);
  protected static native int INTERNALgetAstKind(long a0, long a1);
  protected static native boolean INTERNALisApp(long a0, long a1);
  protected static native boolean INTERNALisNumeralAst(long a0, long a1);
  protected static native boolean INTERNALisAlgebraicNumber(long a0, long a1);
  protected static native long INTERNALtoApp(long a0, long a1);
  protected static native long INTERNALtoFuncDecl(long a0, long a1);
  protected static native String INTERNALgetNumeralString(long a0, long a1);
  protected static native String INTERNALgetNumeralDecimalString(long a0, long a1, int a2);
  protected static native long INTERNALgetNumerator(long a0, long a1);
  protected static native long INTERNALgetDenominator(long a0, long a1);
  protected static native boolean INTERNALgetNumeralSmall(long a0, long a1, LongPtr a2, LongPtr a3);
  protected static native boolean INTERNALgetNumeralInt(long a0, long a1, IntPtr a2);
  protected static native boolean INTERNALgetNumeralUint(long a0, long a1, IntPtr a2);
  protected static native boolean INTERNALgetNumeralUint64(long a0, long a1, LongPtr a2);
  protected static native boolean INTERNALgetNumeralInt64(long a0, long a1, LongPtr a2);
  protected static native boolean INTERNALgetNumeralRationalInt64(long a0, long a1, LongPtr a2, LongPtr a3);
  protected static native long INTERNALgetAlgebraicNumberLower(long a0, long a1, int a2);
  protected static native long INTERNALgetAlgebraicNumberUpper(long a0, long a1, int a2);
  protected static native long INTERNALpatternToAst(long a0, long a1);
  protected static native int INTERNALgetPatternNumTerms(long a0, long a1);
  protected static native long INTERNALgetPattern(long a0, long a1, int a2);
  protected static native int INTERNALgetIndexValue(long a0, long a1);
  protected static native boolean INTERNALisQuantifierForall(long a0, long a1);
  protected static native int INTERNALgetQuantifierWeight(long a0, long a1);
  protected static native int INTERNALgetQuantifierNumPatterns(long a0, long a1);
  protected static native long INTERNALgetQuantifierPatternAst(long a0, long a1, int a2);
  protected static native int INTERNALgetQuantifierNumNoPatterns(long a0, long a1);
  protected static native long INTERNALgetQuantifierNoPatternAst(long a0, long a1, int a2);
  protected static native int INTERNALgetQuantifierNumBound(long a0, long a1);
  protected static native long INTERNALgetQuantifierBoundName(long a0, long a1, int a2);
  protected static native long INTERNALgetQuantifierBoundSort(long a0, long a1, int a2);
  protected static native long INTERNALgetQuantifierBody(long a0, long a1);
  protected static native long INTERNALsimplify(long a0, long a1);
  protected static native long INTERNALsimplifyEx(long a0, long a1, long a2);
  protected static native String INTERNALsimplifyGetHelp(long a0);
  protected static native long INTERNALsimplifyGetParamDescrs(long a0);
  protected static native long INTERNALupdateTerm(long a0, long a1, int a2, long[] a3);
  protected static native long INTERNALsubstitute(long a0, long a1, int a2, long[] a3, long[] a4);
  protected static native long INTERNALsubstituteVars(long a0, long a1, int a2, long[] a3);
  protected static native long INTERNALtranslate(long a0, long a1, long a2);
  protected static native void INTERNALmodelIncRef(long a0, long a1);
  protected static native void INTERNALmodelDecRef(long a0, long a1);
  protected static native boolean INTERNALmodelEval(long a0, long a1, long a2, boolean a3, LongPtr a4);
  protected static native long INTERNALmodelGetConstInterp(long a0, long a1, long a2);
  protected static native boolean INTERNALmodelHasInterp(long a0, long a1, long a2);
  protected static native long INTERNALmodelGetFuncInterp(long a0, long a1, long a2);
  protected static native int INTERNALmodelGetNumConsts(long a0, long a1);
  protected static native long INTERNALmodelGetConstDecl(long a0, long a1, int a2);
  protected static native int INTERNALmodelGetNumFuncs(long a0, long a1);
  protected static native long INTERNALmodelGetFuncDecl(long a0, long a1, int a2);
  protected static native int INTERNALmodelGetNumSorts(long a0, long a1);
  protected static native long INTERNALmodelGetSort(long a0, long a1, int a2);
  protected static native long INTERNALmodelGetSortUniverse(long a0, long a1, long a2);
  protected static native boolean INTERNALisAsArray(long a0, long a1);
  protected static native long INTERNALgetAsArrayFuncDecl(long a0, long a1);
  protected static native void INTERNALfuncInterpIncRef(long a0, long a1);
  protected static native void INTERNALfuncInterpDecRef(long a0, long a1);
  protected static native int INTERNALfuncInterpGetNumEntries(long a0, long a1);
  protected static native long INTERNALfuncInterpGetEntry(long a0, long a1, int a2);
  protected static native long INTERNALfuncInterpGetElse(long a0, long a1);
  protected static native int INTERNALfuncInterpGetArity(long a0, long a1);
  protected static native void INTERNALfuncEntryIncRef(long a0, long a1);
  protected static native void INTERNALfuncEntryDecRef(long a0, long a1);
  protected static native long INTERNALfuncEntryGetValue(long a0, long a1);
  protected static native int INTERNALfuncEntryGetNumArgs(long a0, long a1);
  protected static native long INTERNALfuncEntryGetArg(long a0, long a1, int a2);
  protected static native int INTERNALopenLog(String a0);
  protected static native void INTERNALappendLog(String a0);
  protected static native void INTERNALcloseLog();
  protected static native void INTERNALtoggleWarningMessages(boolean a0);
  protected static native void INTERNALsetAstPrintMode(long a0, int a1);
  protected static native String INTERNALastToString(long a0, long a1);
  protected static native String INTERNALpatternToString(long a0, long a1);
  protected static native String INTERNALsortToString(long a0, long a1);
  protected static native String INTERNALfuncDeclToString(long a0, long a1);
  protected static native String INTERNALmodelToString(long a0, long a1);
  protected static native String INTERNALbenchmarkToSmtlibString(long a0, String a1, String a2, String a3, String a4, int a5, long[] a6, long a7);
  protected static native long INTERNALparseSmtlib2String(long a0, String a1, int a2, long[] a3, long[] a4, int a5, long[] a6, long[] a7);
  protected static native long INTERNALparseSmtlib2File(long a0, String a1, int a2, long[] a3, long[] a4, int a5, long[] a6, long[] a7);
  protected static native void INTERNALparseSmtlibString(long a0, String a1, int a2, long[] a3, long[] a4, int a5, long[] a6, long[] a7);
  protected static native void INTERNALparseSmtlibFile(long a0, String a1, int a2, long[] a3, long[] a4, int a5, long[] a6, long[] a7);
  protected static native int INTERNALgetSmtlibNumFormulas(long a0);
  protected static native long INTERNALgetSmtlibFormula(long a0, int a1);
  protected static native int INTERNALgetSmtlibNumAssumptions(long a0);
  protected static native long INTERNALgetSmtlibAssumption(long a0, int a1);
  protected static native int INTERNALgetSmtlibNumDecls(long a0);
  protected static native long INTERNALgetSmtlibDecl(long a0, int a1);
  protected static native int INTERNALgetSmtlibNumSorts(long a0);
  protected static native long INTERNALgetSmtlibSort(long a0, int a1);
  protected static native String INTERNALgetSmtlibError(long a0);
  protected static native int INTERNALgetErrorCode(long a0);
  protected static native void INTERNALsetError(long a0, int a1);
  protected static native String INTERNALgetErrorMsg(long a0, int a1);
  protected static native void INTERNALgetVersion(IntPtr a0, IntPtr a1, IntPtr a2, IntPtr a3);
  protected static native String INTERNALgetFullVersion();
  protected static native void INTERNALenableTrace(String a0);
  protected static native void INTERNALdisableTrace(String a0);
  protected static native void INTERNALresetMemory();
  protected static native void INTERNALfinalizeMemory();
  protected static native long INTERNALmkGoal(long a0, boolean a1, boolean a2, boolean a3);
  protected static native void INTERNALgoalIncRef(long a0, long a1);
  protected static native void INTERNALgoalDecRef(long a0, long a1);
  protected static native int INTERNALgoalPrecision(long a0, long a1);
  protected static native void INTERNALgoalAssert(long a0, long a1, long a2);
  protected static native boolean INTERNALgoalInconsistent(long a0, long a1);
  protected static native int INTERNALgoalDepth(long a0, long a1);
  protected static native void INTERNALgoalReset(long a0, long a1);
  protected static native int INTERNALgoalSize(long a0, long a1);
  protected static native long INTERNALgoalFormula(long a0, long a1, int a2);
  protected static native int INTERNALgoalNumExprs(long a0, long a1);
  protected static native boolean INTERNALgoalIsDecidedSat(long a0, long a1);
  protected static native boolean INTERNALgoalIsDecidedUnsat(long a0, long a1);
  protected static native long INTERNALgoalTranslate(long a0, long a1, long a2);
  protected static native String INTERNALgoalToString(long a0, long a1);
  protected static native long INTERNALmkTactic(long a0, String a1);
  protected static native void INTERNALtacticIncRef(long a0, long a1);
  protected static native void INTERNALtacticDecRef(long a0, long a1);
  protected static native long INTERNALmkProbe(long a0, String a1);
  protected static native void INTERNALprobeIncRef(long a0, long a1);
  protected static native void INTERNALprobeDecRef(long a0, long a1);
  protected static native long INTERNALtacticAndThen(long a0, long a1, long a2);
  protected static native long INTERNALtacticOrElse(long a0, long a1, long a2);
  protected static native long INTERNALtacticParOr(long a0, int a1, long[] a2);
  protected static native long INTERNALtacticParAndThen(long a0, long a1, long a2);
  protected static native long INTERNALtacticTryFor(long a0, long a1, int a2);
  protected static native long INTERNALtacticWhen(long a0, long a1, long a2);
  protected static native long INTERNALtacticCond(long a0, long a1, long a2, long a3);
  protected static native long INTERNALtacticRepeat(long a0, long a1, int a2);
  protected static native long INTERNALtacticSkip(long a0);
  protected static native long INTERNALtacticFail(long a0);
  protected static native long INTERNALtacticFailIf(long a0, long a1);
  protected static native long INTERNALtacticFailIfNotDecided(long a0);
  protected static native long INTERNALtacticUsingParams(long a0, long a1, long a2);
  protected static native long INTERNALprobeConst(long a0, double a1);
  protected static native long INTERNALprobeLt(long a0, long a1, long a2);
  protected static native long INTERNALprobeGt(long a0, long a1, long a2);
  protected static native long INTERNALprobeLe(long a0, long a1, long a2);
  protected static native long INTERNALprobeGe(long a0, long a1, long a2);
  protected static native long INTERNALprobeEq(long a0, long a1, long a2);
  protected static native long INTERNALprobeAnd(long a0, long a1, long a2);
  protected static native long INTERNALprobeOr(long a0, long a1, long a2);
  protected static native long INTERNALprobeNot(long a0, long a1);
  protected static native int INTERNALgetNumTactics(long a0);
  protected static native String INTERNALgetTacticName(long a0, int a1);
  protected static native int INTERNALgetNumProbes(long a0);
  protected static native String INTERNALgetProbeName(long a0, int a1);
  protected static native String INTERNALtacticGetHelp(long a0, long a1);
  protected static native long INTERNALtacticGetParamDescrs(long a0, long a1);
  protected static native String INTERNALtacticGetDescr(long a0, String a1);
  protected static native String INTERNALprobeGetDescr(long a0, String a1);
  protected static native double INTERNALprobeApply(long a0, long a1, long a2);
  protected static native long INTERNALtacticApply(long a0, long a1, long a2);
  protected static native long INTERNALtacticApplyEx(long a0, long a1, long a2, long a3);
  protected static native void INTERNALapplyResultIncRef(long a0, long a1);
  protected static native void INTERNALapplyResultDecRef(long a0, long a1);
  protected static native String INTERNALapplyResultToString(long a0, long a1);
  protected static native int INTERNALapplyResultGetNumSubgoals(long a0, long a1);
  protected static native long INTERNALapplyResultGetSubgoal(long a0, long a1, int a2);
  protected static native long INTERNALapplyResultConvertModel(long a0, long a1, int a2, long a3);
  protected static native long INTERNALmkSolver(long a0);
  protected static native long INTERNALmkSimpleSolver(long a0);
  protected static native long INTERNALmkSolverForLogic(long a0, long a1);
  protected static native long INTERNALmkSolverFromTactic(long a0, long a1);
  protected static native long INTERNALsolverTranslate(long a0, long a1, long a2);
  protected static native String INTERNALsolverGetHelp(long a0, long a1);
  protected static native long INTERNALsolverGetParamDescrs(long a0, long a1);
  protected static native void INTERNALsolverSetParams(long a0, long a1, long a2);
  protected static native void INTERNALsolverIncRef(long a0, long a1);
  protected static native void INTERNALsolverDecRef(long a0, long a1);
  protected static native void INTERNALsolverPush(long a0, long a1);
  protected static native void INTERNALsolverPop(long a0, long a1, int a2);
  protected static native void INTERNALsolverReset(long a0, long a1);
  protected static native int INTERNALsolverGetNumScopes(long a0, long a1);
  protected static native void INTERNALsolverAssert(long a0, long a1, long a2);
  protected static native void INTERNALsolverAssertAndTrack(long a0, long a1, long a2, long a3);
  protected static native long INTERNALsolverGetAssertions(long a0, long a1);
  protected static native int INTERNALsolverCheck(long a0, long a1);
  protected static native int INTERNALsolverCheckAssumptions(long a0, long a1, int a2, long[] a3);
  protected static native int INTERNALgetImpliedEqualities(long a0, long a1, int a2, long[] a3, int[] a4);
  protected static native int INTERNALsolverGetConsequences(long a0, long a1, long a2, long a3, long a4);
  protected static native long INTERNALsolverGetModel(long a0, long a1);
  protected static native long INTERNALsolverGetProof(long a0, long a1);
  protected static native long INTERNALsolverGetUnsatCore(long a0, long a1);
  protected static native String INTERNALsolverGetReasonUnknown(long a0, long a1);
  protected static native long INTERNALsolverGetStatistics(long a0, long a1);
  protected static native String INTERNALsolverToString(long a0, long a1);
  protected static native String INTERNALstatsToString(long a0, long a1);
  protected static native void INTERNALstatsIncRef(long a0, long a1);
  protected static native void INTERNALstatsDecRef(long a0, long a1);
  protected static native int INTERNALstatsSize(long a0, long a1);
  protected static native String INTERNALstatsGetKey(long a0, long a1, int a2);
  protected static native boolean INTERNALstatsIsUint(long a0, long a1, int a2);
  protected static native boolean INTERNALstatsIsDouble(long a0, long a1, int a2);
  protected static native int INTERNALstatsGetUintValue(long a0, long a1, int a2);
  protected static native double INTERNALstatsGetDoubleValue(long a0, long a1, int a2);
  protected static native long INTERNALgetEstimatedAllocSize();
  protected static native long INTERNALmkAstVector(long a0);
  protected static native void INTERNALastVectorIncRef(long a0, long a1);
  protected static native void INTERNALastVectorDecRef(long a0, long a1);
  protected static native int INTERNALastVectorSize(long a0, long a1);
  protected static native long INTERNALastVectorGet(long a0, long a1, int a2);
  protected static native void INTERNALastVectorSet(long a0, long a1, int a2, long a3);
  protected static native void INTERNALastVectorResize(long a0, long a1, int a2);
  protected static native void INTERNALastVectorPush(long a0, long a1, long a2);
  protected static native long INTERNALastVectorTranslate(long a0, long a1, long a2);
  protected static native String INTERNALastVectorToString(long a0, long a1);
  protected static native long INTERNALmkAstMap(long a0);
  protected static native void INTERNALastMapIncRef(long a0, long a1);
  protected static native void INTERNALastMapDecRef(long a0, long a1);
  protected static native boolean INTERNALastMapContains(long a0, long a1, long a2);
  protected static native long INTERNALastMapFind(long a0, long a1, long a2);
  protected static native void INTERNALastMapInsert(long a0, long a1, long a2, long a3);
  protected static native void INTERNALastMapErase(long a0, long a1, long a2);
  protected static native void INTERNALastMapReset(long a0, long a1);
  protected static native int INTERNALastMapSize(long a0, long a1);
  protected static native long INTERNALastMapKeys(long a0, long a1);
  protected static native String INTERNALastMapToString(long a0, long a1);
  protected static native boolean INTERNALalgebraicIsValue(long a0, long a1);
  protected static native boolean INTERNALalgebraicIsPos(long a0, long a1);
  protected static native boolean INTERNALalgebraicIsNeg(long a0, long a1);
  protected static native boolean INTERNALalgebraicIsZero(long a0, long a1);
  protected static native int INTERNALalgebraicSign(long a0, long a1);
  protected static native long INTERNALalgebraicAdd(long a0, long a1, long a2);
  protected static native long INTERNALalgebraicSub(long a0, long a1, long a2);
  protected static native long INTERNALalgebraicMul(long a0, long a1, long a2);
  protected static native long INTERNALalgebraicDiv(long a0, long a1, long a2);
  protected static native long INTERNALalgebraicRoot(long a0, long a1, int a2);
  protected static native long INTERNALalgebraicPower(long a0, long a1, int a2);
  protected static native boolean INTERNALalgebraicLt(long a0, long a1, long a2);
  protected static native boolean INTERNALalgebraicGt(long a0, long a1, long a2);
  protected static native boolean INTERNALalgebraicLe(long a0, long a1, long a2);
  protected static native boolean INTERNALalgebraicGe(long a0, long a1, long a2);
  protected static native boolean INTERNALalgebraicEq(long a0, long a1, long a2);
  protected static native boolean INTERNALalgebraicNeq(long a0, long a1, long a2);
  protected static native long INTERNALalgebraicRoots(long a0, long a1, int a2, long[] a3);
  protected static native int INTERNALalgebraicEval(long a0, long a1, int a2, long[] a3);
  protected static native long INTERNALpolynomialSubresultants(long a0, long a1, long a2, long a3);
  protected static native void INTERNALrcfDel(long a0, long a1);
  protected static native long INTERNALrcfMkRational(long a0, String a1);
  protected static native long INTERNALrcfMkSmallInt(long a0, int a1);
  protected static native long INTERNALrcfMkPi(long a0);
  protected static native long INTERNALrcfMkE(long a0);
  protected static native long INTERNALrcfMkInfinitesimal(long a0);
  protected static native int INTERNALrcfMkRoots(long a0, int a1, long[] a2, long[] a3);
  protected static native long INTERNALrcfAdd(long a0, long a1, long a2);
  protected static native long INTERNALrcfSub(long a0, long a1, long a2);
  protected static native long INTERNALrcfMul(long a0, long a1, long a2);
  protected static native long INTERNALrcfDiv(long a0, long a1, long a2);
  protected static native long INTERNALrcfNeg(long a0, long a1);
  protected static native long INTERNALrcfInv(long a0, long a1);
  protected static native long INTERNALrcfPower(long a0, long a1, int a2);
  protected static native boolean INTERNALrcfLt(long a0, long a1, long a2);
  protected static native boolean INTERNALrcfGt(long a0, long a1, long a2);
  protected static native boolean INTERNALrcfLe(long a0, long a1, long a2);
  protected static native boolean INTERNALrcfGe(long a0, long a1, long a2);
  protected static native boolean INTERNALrcfEq(long a0, long a1, long a2);
  protected static native boolean INTERNALrcfNeq(long a0, long a1, long a2);
  protected static native String INTERNALrcfNumToString(long a0, long a1, boolean a2, boolean a3);
  protected static native String INTERNALrcfNumToDecimalString(long a0, long a1, int a2);
  protected static native void INTERNALrcfGetNumeratorDenominator(long a0, long a1, LongPtr a2, LongPtr a3);
  protected static native long INTERNALmkFixedpoint(long a0);
  protected static native void INTERNALfixedpointIncRef(long a0, long a1);
  protected static native void INTERNALfixedpointDecRef(long a0, long a1);
  protected static native void INTERNALfixedpointAddRule(long a0, long a1, long a2, long a3);
  protected static native void INTERNALfixedpointAddFact(long a0, long a1, long a2, int a3, int[] a4);
  protected static native void INTERNALfixedpointAssert(long a0, long a1, long a2);
  protected static native int INTERNALfixedpointQuery(long a0, long a1, long a2);
  protected static native int INTERNALfixedpointQueryRelations(long a0, long a1, int a2, long[] a3);
  protected static native long INTERNALfixedpointGetAnswer(long a0, long a1);
  protected static native String INTERNALfixedpointGetReasonUnknown(long a0, long a1);
  protected static native void INTERNALfixedpointUpdateRule(long a0, long a1, long a2, long a3);
  protected static native int INTERNALfixedpointGetNumLevels(long a0, long a1, long a2);
  protected static native long INTERNALfixedpointGetCoverDelta(long a0, long a1, int a2, long a3);
  protected static native void INTERNALfixedpointAddCover(long a0, long a1, int a2, long a3, long a4);
  protected static native long INTERNALfixedpointGetStatistics(long a0, long a1);
  protected static native void INTERNALfixedpointRegisterRelation(long a0, long a1, long a2);
  protected static native void INTERNALfixedpointSetPredicateRepresentation(long a0, long a1, long a2, int a3, long[] a4);
  protected static native long INTERNALfixedpointGetRules(long a0, long a1);
  protected static native long INTERNALfixedpointGetAssertions(long a0, long a1);
  protected static native void INTERNALfixedpointSetParams(long a0, long a1, long a2);
  protected static native String INTERNALfixedpointGetHelp(long a0, long a1);
  protected static native long INTERNALfixedpointGetParamDescrs(long a0, long a1);
  protected static native String INTERNALfixedpointToString(long a0, long a1, int a2, long[] a3);
  protected static native long INTERNALfixedpointFromString(long a0, long a1, String a2);
  protected static native long INTERNALfixedpointFromFile(long a0, long a1, String a2);
  protected static native void INTERNALfixedpointPush(long a0, long a1);
  protected static native void INTERNALfixedpointPop(long a0, long a1);
  protected static native long INTERNALmkOptimize(long a0);
  protected static native void INTERNALoptimizeIncRef(long a0, long a1);
  protected static native void INTERNALoptimizeDecRef(long a0, long a1);
  protected static native void INTERNALoptimizeAssert(long a0, long a1, long a2);
  protected static native int INTERNALoptimizeAssertSoft(long a0, long a1, long a2, String a3, long a4);
  protected static native int INTERNALoptimizeMaximize(long a0, long a1, long a2);
  protected static native int INTERNALoptimizeMinimize(long a0, long a1, long a2);
  protected static native void INTERNALoptimizePush(long a0, long a1);
  protected static native void INTERNALoptimizePop(long a0, long a1);
  protected static native int INTERNALoptimizeCheck(long a0, long a1);
  protected static native String INTERNALoptimizeGetReasonUnknown(long a0, long a1);
  protected static native long INTERNALoptimizeGetModel(long a0, long a1);
  protected static native void INTERNALoptimizeSetParams(long a0, long a1, long a2);
  protected static native long INTERNALoptimizeGetParamDescrs(long a0, long a1);
  protected static native long INTERNALoptimizeGetLower(long a0, long a1, int a2);
  protected static native long INTERNALoptimizeGetUpper(long a0, long a1, int a2);
  protected static native String INTERNALoptimizeToString(long a0, long a1);
  protected static native void INTERNALoptimizeFromString(long a0, long a1, String a2);
  protected static native void INTERNALoptimizeFromFile(long a0, long a1, String a2);
  protected static native String INTERNALoptimizeGetHelp(long a0, long a1);
  protected static native long INTERNALoptimizeGetStatistics(long a0, long a1);
  protected static native long INTERNALoptimizeGetAssertions(long a0, long a1);
  protected static native long INTERNALoptimizeGetObjectives(long a0, long a1);
  protected static native long INTERNALmkInterpolant(long a0, long a1);
  protected static native long INTERNALmkInterpolationContext(long a0);
  protected static native long INTERNALgetInterpolant(long a0, long a1, long a2, long a3);
  protected static native int INTERNALcomputeInterpolant(long a0, long a1, long a2, LongPtr a3, LongPtr a4);
  protected static native String INTERNALinterpolationProfile(long a0);
  protected static native int INTERNALreadInterpolationProblem(long a0, IntPtr a1, ObjArrayPtr a2, UIntArrayPtr a3, String a4, StringPtr a5, IntPtr a6, ObjArrayPtr a7);
  protected static native int INTERNALcheckInterpolant(long a0, int a1, long[] a2, int[] a3, long[] a4, StringPtr a5, int a6, long[] a7);
  protected static native void INTERNALwriteInterpolationProblem(long a0, int a1, long[] a2, int[] a3, String a4, int a5, long[] a6);
  protected static native long INTERNALmkFpaRoundingModeSort(long a0);
  protected static native long INTERNALmkFpaRoundNearestTiesToEven(long a0);
  protected static native long INTERNALmkFpaRne(long a0);
  protected static native long INTERNALmkFpaRoundNearestTiesToAway(long a0);
  protected static native long INTERNALmkFpaRna(long a0);
  protected static native long INTERNALmkFpaRoundTowardPositive(long a0);
  protected static native long INTERNALmkFpaRtp(long a0);
  protected static native long INTERNALmkFpaRoundTowardNegative(long a0);
  protected static native long INTERNALmkFpaRtn(long a0);
  protected static native long INTERNALmkFpaRoundTowardZero(long a0);
  protected static native long INTERNALmkFpaRtz(long a0);
  protected static native long INTERNALmkFpaSort(long a0, int a1, int a2);
  protected static native long INTERNALmkFpaSortHalf(long a0);
  protected static native long INTERNALmkFpaSort16(long a0);
  protected static native long INTERNALmkFpaSortSingle(long a0);
  protected static native long INTERNALmkFpaSort32(long a0);
  protected static native long INTERNALmkFpaSortDouble(long a0);
  protected static native long INTERNALmkFpaSort64(long a0);
  protected static native long INTERNALmkFpaSortQuadruple(long a0);
  protected static native long INTERNALmkFpaSort128(long a0);
  protected static native long INTERNALmkFpaNan(long a0, long a1);
  protected static native long INTERNALmkFpaInf(long a0, long a1, boolean a2);
  protected static native long INTERNALmkFpaZero(long a0, long a1, boolean a2);
  protected static native long INTERNALmkFpaFp(long a0, long a1, long a2, long a3);
  protected static native long INTERNALmkFpaNumeralFloat(long a0, float a1, long a2);
  protected static native long INTERNALmkFpaNumeralDouble(long a0, double a1, long a2);
  protected static native long INTERNALmkFpaNumeralInt(long a0, int a1, long a2);
  protected static native long INTERNALmkFpaNumeralIntUint(long a0, boolean a1, int a2, int a3, long a4);
  protected static native long INTERNALmkFpaNumeralInt64Uint64(long a0, boolean a1, long a2, long a3, long a4);
  protected static native long INTERNALmkFpaAbs(long a0, long a1);
  protected static native long INTERNALmkFpaNeg(long a0, long a1);
  protected static native long INTERNALmkFpaAdd(long a0, long a1, long a2, long a3);
  protected static native long INTERNALmkFpaSub(long a0, long a1, long a2, long a3);
  protected static native long INTERNALmkFpaMul(long a0, long a1, long a2, long a3);
  protected static native long INTERNALmkFpaDiv(long a0, long a1, long a2, long a3);
  protected static native long INTERNALmkFpaFma(long a0, long a1, long a2, long a3, long a4);
  protected static native long INTERNALmkFpaSqrt(long a0, long a1, long a2);
  protected static native long INTERNALmkFpaRem(long a0, long a1, long a2);
  protected static native long INTERNALmkFpaRoundToIntegral(long a0, long a1, long a2);
  protected static native long INTERNALmkFpaMin(long a0, long a1, long a2);
  protected static native long INTERNALmkFpaMax(long a0, long a1, long a2);
  protected static native long INTERNALmkFpaLeq(long a0, long a1, long a2);
  protected static native long INTERNALmkFpaLt(long a0, long a1, long a2);
  protected static native long INTERNALmkFpaGeq(long a0, long a1, long a2);
  protected static native long INTERNALmkFpaGt(long a0, long a1, long a2);
  protected static native long INTERNALmkFpaEq(long a0, long a1, long a2);
  protected static native long INTERNALmkFpaIsNormal(long a0, long a1);
  protected static native long INTERNALmkFpaIsSubnormal(long a0, long a1);
  protected static native long INTERNALmkFpaIsZero(long a0, long a1);
  protected static native long INTERNALmkFpaIsInfinite(long a0, long a1);
  protected static native long INTERNALmkFpaIsNan(long a0, long a1);
  protected static native long INTERNALmkFpaIsNegative(long a0, long a1);
  protected static native long INTERNALmkFpaIsPositive(long a0, long a1);
  protected static native long INTERNALmkFpaToFpBv(long a0, long a1, long a2);
  protected static native long INTERNALmkFpaToFpFloat(long a0, long a1, long a2, long a3);
  protected static native long INTERNALmkFpaToFpReal(long a0, long a1, long a2, long a3);
  protected static native long INTERNALmkFpaToFpSigned(long a0, long a1, long a2, long a3);
  protected static native long INTERNALmkFpaToFpUnsigned(long a0, long a1, long a2, long a3);
  protected static native long INTERNALmkFpaToUbv(long a0, long a1, long a2, int a3);
  protected static native long INTERNALmkFpaToSbv(long a0, long a1, long a2, int a3);
  protected static native long INTERNALmkFpaToReal(long a0, long a1);
  protected static native int INTERNALfpaGetEbits(long a0, long a1);
  protected static native int INTERNALfpaGetSbits(long a0, long a1);
  protected static native boolean INTERNALfpaGetNumeralSign(long a0, long a1, IntPtr a2);
  protected static native String INTERNALfpaGetNumeralSignificandString(long a0, long a1);
  protected static native boolean INTERNALfpaGetNumeralSignificandUint64(long a0, long a1, LongPtr a2);
  protected static native String INTERNALfpaGetNumeralExponentString(long a0, long a1);
  protected static native boolean INTERNALfpaGetNumeralExponentInt64(long a0, long a1, LongPtr a2);
  protected static native long INTERNALmkFpaToIeeeBv(long a0, long a1);
  protected static native long INTERNALmkFpaToFpIntReal(long a0, long a1, long a2, long a3, long a4);


  public static void globalParamSet(String a0, String a1)
  {
      INTERNALglobalParamSet(a0, a1);
  }

  public static void globalParamResetAll()
  {
      INTERNALglobalParamResetAll();
  }

  public static boolean globalParamGet(String a0, StringPtr a1)
  {
      boolean res = INTERNALglobalParamGet(a0, a1);
      return res;
  }

  public static long mkConfig()
  {
      long res = INTERNALmkConfig();
      return res;
  }

  public static void delConfig(long a0)
  {
      INTERNALdelConfig(a0);
  }

  public static void setParamValue(long a0, String a1, String a2)
  {
      INTERNALsetParamValue(a0, a1, a2);
  }

  public static long mkContext(long a0) throws Z3Exception
  {
      long res = INTERNALmkContext(a0);
      if (res == 0)
          throw new Z3Exception("Object allocation failed.");
      return res;
  }

  public static long mkContextRc(long a0) throws Z3Exception
  {
      long res = INTERNALmkContextRc(a0);
      if (res == 0)
          throw new Z3Exception("Object allocation failed.");
      return res;
  }

  public static void delContext(long a0) throws Z3Exception
  {
      INTERNALdelContext(a0);
  }

  public static void incRef(long a0, long a1) throws Z3Exception
  {
      INTERNALincRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void decRef(long a0, long a1) throws Z3Exception
  {
      INTERNALdecRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void updateParamValue(long a0, String a1, String a2) throws Z3Exception
  {
      INTERNALupdateParamValue(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void interrupt(long a0) throws Z3Exception
  {
      INTERNALinterrupt(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static long mkParams(long a0) throws Z3Exception
  {
      long res = INTERNALmkParams(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void paramsIncRef(long a0, long a1) throws Z3Exception
  {
      INTERNALparamsIncRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void paramsDecRef(long a0, long a1) throws Z3Exception
  {
      INTERNALparamsDecRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void paramsSetBool(long a0, long a1, long a2, boolean a3) throws Z3Exception
  {
      INTERNALparamsSetBool(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void paramsSetUint(long a0, long a1, long a2, int a3) throws Z3Exception
  {
      INTERNALparamsSetUint(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void paramsSetDouble(long a0, long a1, long a2, double a3) throws Z3Exception
  {
      INTERNALparamsSetDouble(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void paramsSetSymbol(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      INTERNALparamsSetSymbol(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static String paramsToString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALparamsToString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void paramsValidate(long a0, long a1, long a2) throws Z3Exception
  {
      INTERNALparamsValidate(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void paramDescrsIncRef(long a0, long a1) throws Z3Exception
  {
      INTERNALparamDescrsIncRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void paramDescrsDecRef(long a0, long a1) throws Z3Exception
  {
      INTERNALparamDescrsDecRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static int paramDescrsGetKind(long a0, long a1, long a2) throws Z3Exception
  {
      int res = INTERNALparamDescrsGetKind(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int paramDescrsSize(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALparamDescrsSize(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long paramDescrsGetName(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALparamDescrsGetName(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String paramDescrsGetDocumentation(long a0, long a1, long a2) throws Z3Exception
  {
      String res = INTERNALparamDescrsGetDocumentation(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String paramDescrsToString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALparamDescrsToString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkIntSymbol(long a0, int a1) throws Z3Exception
  {
      long res = INTERNALmkIntSymbol(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkStringSymbol(long a0, String a1) throws Z3Exception
  {
      long res = INTERNALmkStringSymbol(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkUninterpretedSort(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkUninterpretedSort(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBoolSort(long a0) throws Z3Exception
  {
      long res = INTERNALmkBoolSort(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkIntSort(long a0) throws Z3Exception
  {
      long res = INTERNALmkIntSort(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkRealSort(long a0) throws Z3Exception
  {
      long res = INTERNALmkRealSort(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvSort(long a0, int a1) throws Z3Exception
  {
      long res = INTERNALmkBvSort(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFiniteDomainSort(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkFiniteDomainSort(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkArraySort(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkArraySort(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkTupleSort(long a0, long a1, int a2, long[] a3, long[] a4, LongPtr a5, long[] a6) throws Z3Exception
  {
      long res = INTERNALmkTupleSort(a0, a1, a2, a3, a4, a5, a6);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkEnumerationSort(long a0, long a1, int a2, long[] a3, long[] a4, long[] a5) throws Z3Exception
  {
      long res = INTERNALmkEnumerationSort(a0, a1, a2, a3, a4, a5);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkListSort(long a0, long a1, long a2, LongPtr a3, LongPtr a4, LongPtr a5, LongPtr a6, LongPtr a7, LongPtr a8) throws Z3Exception
  {
      long res = INTERNALmkListSort(a0, a1, a2, a3, a4, a5, a6, a7, a8);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkConstructor(long a0, long a1, long a2, int a3, long[] a4, long[] a5, int[] a6) throws Z3Exception
  {
      long res = INTERNALmkConstructor(a0, a1, a2, a3, a4, a5, a6);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void delConstructor(long a0, long a1) throws Z3Exception
  {
      INTERNALdelConstructor(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static long mkDatatype(long a0, long a1, int a2, long[] a3) throws Z3Exception
  {
      long res = INTERNALmkDatatype(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkConstructorList(long a0, int a1, long[] a2) throws Z3Exception
  {
      long res = INTERNALmkConstructorList(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void delConstructorList(long a0, long a1) throws Z3Exception
  {
      INTERNALdelConstructorList(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void mkDatatypes(long a0, int a1, long[] a2, long[] a3, long[] a4) throws Z3Exception
  {
      INTERNALmkDatatypes(a0, a1, a2, a3, a4);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void queryConstructor(long a0, long a1, int a2, LongPtr a3, LongPtr a4, long[] a5) throws Z3Exception
  {
      INTERNALqueryConstructor(a0, a1, a2, a3, a4, a5);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static long mkFuncDecl(long a0, long a1, int a2, long[] a3, long a4) throws Z3Exception
  {
      long res = INTERNALmkFuncDecl(a0, a1, a2, a3, a4);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkApp(long a0, long a1, int a2, long[] a3) throws Z3Exception
  {
      long res = INTERNALmkApp(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkConst(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkConst(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFreshFuncDecl(long a0, String a1, int a2, long[] a3, long a4) throws Z3Exception
  {
      long res = INTERNALmkFreshFuncDecl(a0, a1, a2, a3, a4);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFreshConst(long a0, String a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkFreshConst(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkTrue(long a0) throws Z3Exception
  {
      long res = INTERNALmkTrue(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFalse(long a0) throws Z3Exception
  {
      long res = INTERNALmkFalse(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkEq(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkEq(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkDistinct(long a0, int a1, long[] a2) throws Z3Exception
  {
      long res = INTERNALmkDistinct(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkNot(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkNot(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkIte(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALmkIte(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkIff(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkIff(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkImplies(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkImplies(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkXor(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkXor(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkAnd(long a0, int a1, long[] a2) throws Z3Exception
  {
      long res = INTERNALmkAnd(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkOr(long a0, int a1, long[] a2) throws Z3Exception
  {
      long res = INTERNALmkOr(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkAdd(long a0, int a1, long[] a2) throws Z3Exception
  {
      long res = INTERNALmkAdd(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkMul(long a0, int a1, long[] a2) throws Z3Exception
  {
      long res = INTERNALmkMul(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSub(long a0, int a1, long[] a2) throws Z3Exception
  {
      long res = INTERNALmkSub(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkUnaryMinus(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkUnaryMinus(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkDiv(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkDiv(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkMod(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkMod(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkRem(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkRem(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkPower(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkPower(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkLt(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkLt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkLe(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkLe(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkGt(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkGt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkGe(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkGe(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkInt2real(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkInt2real(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkReal2int(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkReal2int(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkIsInt(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkIsInt(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvnot(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkBvnot(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvredand(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkBvredand(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvredor(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkBvredor(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvand(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvand(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvor(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvor(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvxor(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvxor(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvnand(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvnand(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvnor(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvnor(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvxnor(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvxnor(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvneg(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkBvneg(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvadd(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvadd(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvsub(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvsub(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvmul(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvmul(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvudiv(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvudiv(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvsdiv(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvsdiv(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvurem(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvurem(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvsrem(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvsrem(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvsmod(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvsmod(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvult(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvult(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvslt(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvslt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvule(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvule(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvsle(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvsle(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvuge(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvuge(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvsge(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvsge(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvugt(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvugt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvsgt(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvsgt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkConcat(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkConcat(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkExtract(long a0, int a1, int a2, long a3) throws Z3Exception
  {
      long res = INTERNALmkExtract(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSignExt(long a0, int a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkSignExt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkZeroExt(long a0, int a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkZeroExt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkRepeat(long a0, int a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkRepeat(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvshl(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvshl(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvlshr(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvlshr(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvashr(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvashr(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkRotateLeft(long a0, int a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkRotateLeft(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkRotateRight(long a0, int a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkRotateRight(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkExtRotateLeft(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkExtRotateLeft(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkExtRotateRight(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkExtRotateRight(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkInt2bv(long a0, int a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkInt2bv(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBv2int(long a0, long a1, boolean a2) throws Z3Exception
  {
      long res = INTERNALmkBv2int(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvaddNoOverflow(long a0, long a1, long a2, boolean a3) throws Z3Exception
  {
      long res = INTERNALmkBvaddNoOverflow(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvaddNoUnderflow(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvaddNoUnderflow(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvsubNoOverflow(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvsubNoOverflow(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvsubNoUnderflow(long a0, long a1, long a2, boolean a3) throws Z3Exception
  {
      long res = INTERNALmkBvsubNoUnderflow(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvsdivNoOverflow(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvsdivNoOverflow(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvnegNoOverflow(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkBvnegNoOverflow(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvmulNoOverflow(long a0, long a1, long a2, boolean a3) throws Z3Exception
  {
      long res = INTERNALmkBvmulNoOverflow(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBvmulNoUnderflow(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBvmulNoUnderflow(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSelect(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkSelect(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkStore(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALmkStore(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkConstArray(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkConstArray(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkMap(long a0, long a1, int a2, long[] a3) throws Z3Exception
  {
      long res = INTERNALmkMap(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkArrayDefault(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkArrayDefault(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSetSort(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkSetSort(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkEmptySet(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkEmptySet(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFullSet(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkFullSet(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSetAdd(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkSetAdd(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSetDel(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkSetDel(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSetUnion(long a0, int a1, long[] a2) throws Z3Exception
  {
      long res = INTERNALmkSetUnion(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSetIntersect(long a0, int a1, long[] a2) throws Z3Exception
  {
      long res = INTERNALmkSetIntersect(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSetDifference(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkSetDifference(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSetComplement(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkSetComplement(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSetMember(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkSetMember(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSetSubset(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkSetSubset(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkArrayExt(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkArrayExt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkNumeral(long a0, String a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkNumeral(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkReal(long a0, int a1, int a2) throws Z3Exception
  {
      long res = INTERNALmkReal(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkInt(long a0, int a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkInt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkUnsignedInt(long a0, int a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkUnsignedInt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkInt64(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkInt64(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkUnsignedInt64(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkUnsignedInt64(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSeqSort(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkSeqSort(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean isSeqSort(long a0, long a1) throws Z3Exception
  {
      boolean res = INTERNALisSeqSort(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkReSort(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkReSort(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean isReSort(long a0, long a1) throws Z3Exception
  {
      boolean res = INTERNALisReSort(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkStringSort(long a0) throws Z3Exception
  {
      long res = INTERNALmkStringSort(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean isStringSort(long a0, long a1) throws Z3Exception
  {
      boolean res = INTERNALisStringSort(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkString(long a0, String a1) throws Z3Exception
  {
      long res = INTERNALmkString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean isString(long a0, long a1) throws Z3Exception
  {
      boolean res = INTERNALisString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String getString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALgetString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSeqEmpty(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkSeqEmpty(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSeqUnit(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkSeqUnit(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSeqConcat(long a0, int a1, long[] a2) throws Z3Exception
  {
      long res = INTERNALmkSeqConcat(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSeqPrefix(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkSeqPrefix(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSeqSuffix(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkSeqSuffix(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSeqContains(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkSeqContains(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSeqExtract(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALmkSeqExtract(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSeqReplace(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALmkSeqReplace(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSeqAt(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkSeqAt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSeqLength(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkSeqLength(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSeqIndex(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALmkSeqIndex(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSeqToRe(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkSeqToRe(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSeqInRe(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkSeqInRe(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkRePlus(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkRePlus(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkReStar(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkReStar(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkReOption(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkReOption(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkReUnion(long a0, int a1, long[] a2) throws Z3Exception
  {
      long res = INTERNALmkReUnion(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkReConcat(long a0, int a1, long[] a2) throws Z3Exception
  {
      long res = INTERNALmkReConcat(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkPattern(long a0, int a1, long[] a2) throws Z3Exception
  {
      long res = INTERNALmkPattern(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkBound(long a0, int a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkBound(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkForall(long a0, int a1, int a2, long[] a3, int a4, long[] a5, long[] a6, long a7) throws Z3Exception
  {
      long res = INTERNALmkForall(a0, a1, a2, a3, a4, a5, a6, a7);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkExists(long a0, int a1, int a2, long[] a3, int a4, long[] a5, long[] a6, long a7) throws Z3Exception
  {
      long res = INTERNALmkExists(a0, a1, a2, a3, a4, a5, a6, a7);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkQuantifier(long a0, boolean a1, int a2, int a3, long[] a4, int a5, long[] a6, long[] a7, long a8) throws Z3Exception
  {
      long res = INTERNALmkQuantifier(a0, a1, a2, a3, a4, a5, a6, a7, a8);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkQuantifierEx(long a0, boolean a1, int a2, long a3, long a4, int a5, long[] a6, int a7, long[] a8, int a9, long[] a10, long[] a11, long a12) throws Z3Exception
  {
      long res = INTERNALmkQuantifierEx(a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkForallConst(long a0, int a1, int a2, long[] a3, int a4, long[] a5, long a6) throws Z3Exception
  {
      long res = INTERNALmkForallConst(a0, a1, a2, a3, a4, a5, a6);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkExistsConst(long a0, int a1, int a2, long[] a3, int a4, long[] a5, long a6) throws Z3Exception
  {
      long res = INTERNALmkExistsConst(a0, a1, a2, a3, a4, a5, a6);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkQuantifierConst(long a0, boolean a1, int a2, int a3, long[] a4, int a5, long[] a6, long a7) throws Z3Exception
  {
      long res = INTERNALmkQuantifierConst(a0, a1, a2, a3, a4, a5, a6, a7);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkQuantifierConstEx(long a0, boolean a1, int a2, long a3, long a4, int a5, long[] a6, int a7, long[] a8, int a9, long[] a10, long a11) throws Z3Exception
  {
      long res = INTERNALmkQuantifierConstEx(a0, a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getSymbolKind(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetSymbolKind(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getSymbolInt(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetSymbolInt(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String getSymbolString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALgetSymbolString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getSortName(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALgetSortName(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getSortId(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetSortId(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long sortToAst(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALsortToAst(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean isEqSort(long a0, long a1, long a2) throws Z3Exception
  {
      boolean res = INTERNALisEqSort(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getSortKind(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetSortKind(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getBvSortSize(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetBvSortSize(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean getFiniteDomainSortSize(long a0, long a1, LongPtr a2) throws Z3Exception
  {
      boolean res = INTERNALgetFiniteDomainSortSize(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getArraySortDomain(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALgetArraySortDomain(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getArraySortRange(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALgetArraySortRange(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getTupleSortMkDecl(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALgetTupleSortMkDecl(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getTupleSortNumFields(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetTupleSortNumFields(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getTupleSortFieldDecl(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALgetTupleSortFieldDecl(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getDatatypeSortNumConstructors(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetDatatypeSortNumConstructors(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getDatatypeSortConstructor(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALgetDatatypeSortConstructor(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getDatatypeSortRecognizer(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALgetDatatypeSortRecognizer(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getDatatypeSortConstructorAccessor(long a0, long a1, int a2, int a3) throws Z3Exception
  {
      long res = INTERNALgetDatatypeSortConstructorAccessor(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long datatypeUpdateField(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALdatatypeUpdateField(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getRelationArity(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetRelationArity(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getRelationColumn(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALgetRelationColumn(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkAtmost(long a0, int a1, long[] a2, int a3) throws Z3Exception
  {
      long res = INTERNALmkAtmost(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkPble(long a0, int a1, long[] a2, int[] a3, int a4) throws Z3Exception
  {
      long res = INTERNALmkPble(a0, a1, a2, a3, a4);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkPbeq(long a0, int a1, long[] a2, int[] a3, int a4) throws Z3Exception
  {
      long res = INTERNALmkPbeq(a0, a1, a2, a3, a4);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long funcDeclToAst(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALfuncDeclToAst(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean isEqFuncDecl(long a0, long a1, long a2) throws Z3Exception
  {
      boolean res = INTERNALisEqFuncDecl(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getFuncDeclId(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetFuncDeclId(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getDeclName(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALgetDeclName(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getDeclKind(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetDeclKind(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getDomainSize(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetDomainSize(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getArity(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetArity(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getDomain(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALgetDomain(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getRange(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALgetRange(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getDeclNumParameters(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetDeclNumParameters(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getDeclParameterKind(long a0, long a1, int a2) throws Z3Exception
  {
      int res = INTERNALgetDeclParameterKind(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getDeclIntParameter(long a0, long a1, int a2) throws Z3Exception
  {
      int res = INTERNALgetDeclIntParameter(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static double getDeclDoubleParameter(long a0, long a1, int a2) throws Z3Exception
  {
      double res = INTERNALgetDeclDoubleParameter(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getDeclSymbolParameter(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALgetDeclSymbolParameter(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getDeclSortParameter(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALgetDeclSortParameter(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getDeclAstParameter(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALgetDeclAstParameter(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getDeclFuncDeclParameter(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALgetDeclFuncDeclParameter(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String getDeclRationalParameter(long a0, long a1, int a2) throws Z3Exception
  {
      String res = INTERNALgetDeclRationalParameter(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long appToAst(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALappToAst(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getAppDecl(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALgetAppDecl(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getAppNumArgs(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetAppNumArgs(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getAppArg(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALgetAppArg(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean isEqAst(long a0, long a1, long a2) throws Z3Exception
  {
      boolean res = INTERNALisEqAst(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getAstId(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetAstId(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getAstHash(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetAstHash(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getSort(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALgetSort(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean isWellSorted(long a0, long a1) throws Z3Exception
  {
      boolean res = INTERNALisWellSorted(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getBoolValue(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetBoolValue(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getAstKind(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetAstKind(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean isApp(long a0, long a1) throws Z3Exception
  {
      boolean res = INTERNALisApp(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean isNumeralAst(long a0, long a1) throws Z3Exception
  {
      boolean res = INTERNALisNumeralAst(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean isAlgebraicNumber(long a0, long a1) throws Z3Exception
  {
      boolean res = INTERNALisAlgebraicNumber(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long toApp(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALtoApp(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long toFuncDecl(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALtoFuncDecl(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String getNumeralString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALgetNumeralString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String getNumeralDecimalString(long a0, long a1, int a2) throws Z3Exception
  {
      String res = INTERNALgetNumeralDecimalString(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getNumerator(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALgetNumerator(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getDenominator(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALgetDenominator(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean getNumeralSmall(long a0, long a1, LongPtr a2, LongPtr a3) throws Z3Exception
  {
      boolean res = INTERNALgetNumeralSmall(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean getNumeralInt(long a0, long a1, IntPtr a2) throws Z3Exception
  {
      boolean res = INTERNALgetNumeralInt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean getNumeralUint(long a0, long a1, IntPtr a2) throws Z3Exception
  {
      boolean res = INTERNALgetNumeralUint(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean getNumeralUint64(long a0, long a1, LongPtr a2) throws Z3Exception
  {
      boolean res = INTERNALgetNumeralUint64(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean getNumeralInt64(long a0, long a1, LongPtr a2) throws Z3Exception
  {
      boolean res = INTERNALgetNumeralInt64(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean getNumeralRationalInt64(long a0, long a1, LongPtr a2, LongPtr a3) throws Z3Exception
  {
      boolean res = INTERNALgetNumeralRationalInt64(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getAlgebraicNumberLower(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALgetAlgebraicNumberLower(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getAlgebraicNumberUpper(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALgetAlgebraicNumberUpper(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long patternToAst(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALpatternToAst(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getPatternNumTerms(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetPatternNumTerms(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getPattern(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALgetPattern(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getIndexValue(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetIndexValue(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean isQuantifierForall(long a0, long a1) throws Z3Exception
  {
      boolean res = INTERNALisQuantifierForall(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getQuantifierWeight(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetQuantifierWeight(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getQuantifierNumPatterns(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetQuantifierNumPatterns(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getQuantifierPatternAst(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALgetQuantifierPatternAst(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getQuantifierNumNoPatterns(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetQuantifierNumNoPatterns(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getQuantifierNoPatternAst(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALgetQuantifierNoPatternAst(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getQuantifierNumBound(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgetQuantifierNumBound(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getQuantifierBoundName(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALgetQuantifierBoundName(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getQuantifierBoundSort(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALgetQuantifierBoundSort(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getQuantifierBody(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALgetQuantifierBody(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long simplify(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALsimplify(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long simplifyEx(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALsimplifyEx(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String simplifyGetHelp(long a0) throws Z3Exception
  {
      String res = INTERNALsimplifyGetHelp(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long simplifyGetParamDescrs(long a0) throws Z3Exception
  {
      long res = INTERNALsimplifyGetParamDescrs(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long updateTerm(long a0, long a1, int a2, long[] a3) throws Z3Exception
  {
      long res = INTERNALupdateTerm(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long substitute(long a0, long a1, int a2, long[] a3, long[] a4) throws Z3Exception
  {
      long res = INTERNALsubstitute(a0, a1, a2, a3, a4);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long substituteVars(long a0, long a1, int a2, long[] a3) throws Z3Exception
  {
      long res = INTERNALsubstituteVars(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long translate(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALtranslate(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void modelIncRef(long a0, long a1) throws Z3Exception
  {
      INTERNALmodelIncRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void modelDecRef(long a0, long a1) throws Z3Exception
  {
      INTERNALmodelDecRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static boolean modelEval(long a0, long a1, long a2, boolean a3, LongPtr a4) throws Z3Exception
  {
      boolean res = INTERNALmodelEval(a0, a1, a2, a3, a4);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long modelGetConstInterp(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmodelGetConstInterp(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean modelHasInterp(long a0, long a1, long a2) throws Z3Exception
  {
      boolean res = INTERNALmodelHasInterp(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long modelGetFuncInterp(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmodelGetFuncInterp(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int modelGetNumConsts(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALmodelGetNumConsts(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long modelGetConstDecl(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALmodelGetConstDecl(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int modelGetNumFuncs(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALmodelGetNumFuncs(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long modelGetFuncDecl(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALmodelGetFuncDecl(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int modelGetNumSorts(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALmodelGetNumSorts(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long modelGetSort(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALmodelGetSort(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long modelGetSortUniverse(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmodelGetSortUniverse(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean isAsArray(long a0, long a1) throws Z3Exception
  {
      boolean res = INTERNALisAsArray(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getAsArrayFuncDecl(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALgetAsArrayFuncDecl(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void funcInterpIncRef(long a0, long a1) throws Z3Exception
  {
      INTERNALfuncInterpIncRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void funcInterpDecRef(long a0, long a1) throws Z3Exception
  {
      INTERNALfuncInterpDecRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static int funcInterpGetNumEntries(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALfuncInterpGetNumEntries(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long funcInterpGetEntry(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALfuncInterpGetEntry(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long funcInterpGetElse(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALfuncInterpGetElse(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int funcInterpGetArity(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALfuncInterpGetArity(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void funcEntryIncRef(long a0, long a1) throws Z3Exception
  {
      INTERNALfuncEntryIncRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void funcEntryDecRef(long a0, long a1) throws Z3Exception
  {
      INTERNALfuncEntryDecRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static long funcEntryGetValue(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALfuncEntryGetValue(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int funcEntryGetNumArgs(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALfuncEntryGetNumArgs(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long funcEntryGetArg(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALfuncEntryGetArg(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int openLog(String a0)
  {
      int res = INTERNALopenLog(a0);
      return res;
  }

  public static void appendLog(String a0)
  {
      INTERNALappendLog(a0);
  }

  public static void closeLog()
  {
      INTERNALcloseLog();
  }

  public static void toggleWarningMessages(boolean a0)
  {
      INTERNALtoggleWarningMessages(a0);
  }

  public static void setAstPrintMode(long a0, int a1) throws Z3Exception
  {
      INTERNALsetAstPrintMode(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static String astToString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALastToString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String patternToString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALpatternToString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String sortToString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALsortToString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String funcDeclToString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALfuncDeclToString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String modelToString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALmodelToString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String benchmarkToSmtlibString(long a0, String a1, String a2, String a3, String a4, int a5, long[] a6, long a7) throws Z3Exception
  {
      String res = INTERNALbenchmarkToSmtlibString(a0, a1, a2, a3, a4, a5, a6, a7);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long parseSmtlib2String(long a0, String a1, int a2, long[] a3, long[] a4, int a5, long[] a6, long[] a7) throws Z3Exception
  {
      long res = INTERNALparseSmtlib2String(a0, a1, a2, a3, a4, a5, a6, a7);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long parseSmtlib2File(long a0, String a1, int a2, long[] a3, long[] a4, int a5, long[] a6, long[] a7) throws Z3Exception
  {
      long res = INTERNALparseSmtlib2File(a0, a1, a2, a3, a4, a5, a6, a7);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void parseSmtlibString(long a0, String a1, int a2, long[] a3, long[] a4, int a5, long[] a6, long[] a7) throws Z3Exception
  {
      INTERNALparseSmtlibString(a0, a1, a2, a3, a4, a5, a6, a7);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void parseSmtlibFile(long a0, String a1, int a2, long[] a3, long[] a4, int a5, long[] a6, long[] a7) throws Z3Exception
  {
      INTERNALparseSmtlibFile(a0, a1, a2, a3, a4, a5, a6, a7);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static int getSmtlibNumFormulas(long a0) throws Z3Exception
  {
      int res = INTERNALgetSmtlibNumFormulas(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getSmtlibFormula(long a0, int a1) throws Z3Exception
  {
      long res = INTERNALgetSmtlibFormula(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getSmtlibNumAssumptions(long a0) throws Z3Exception
  {
      int res = INTERNALgetSmtlibNumAssumptions(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getSmtlibAssumption(long a0, int a1) throws Z3Exception
  {
      long res = INTERNALgetSmtlibAssumption(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getSmtlibNumDecls(long a0) throws Z3Exception
  {
      int res = INTERNALgetSmtlibNumDecls(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getSmtlibDecl(long a0, int a1) throws Z3Exception
  {
      long res = INTERNALgetSmtlibDecl(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getSmtlibNumSorts(long a0) throws Z3Exception
  {
      int res = INTERNALgetSmtlibNumSorts(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getSmtlibSort(long a0, int a1) throws Z3Exception
  {
      long res = INTERNALgetSmtlibSort(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String getSmtlibError(long a0) throws Z3Exception
  {
      String res = INTERNALgetSmtlibError(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getErrorCode(long a0) throws Z3Exception
  {
      int res = INTERNALgetErrorCode(a0);
      return res;
  }

  public static void setError(long a0, int a1) throws Z3Exception
  {
      INTERNALsetError(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static String getErrorMsg(long a0, int a1) throws Z3Exception
  {
      String res = INTERNALgetErrorMsg(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void getVersion(IntPtr a0, IntPtr a1, IntPtr a2, IntPtr a3)
  {
      INTERNALgetVersion(a0, a1, a2, a3);
  }

  public static String getFullVersion()
  {
      String res = INTERNALgetFullVersion();
      return res;
  }

  public static void enableTrace(String a0)
  {
      INTERNALenableTrace(a0);
  }

  public static void disableTrace(String a0)
  {
      INTERNALdisableTrace(a0);
  }

  public static void resetMemory()
  {
      INTERNALresetMemory();
  }

  public static void finalizeMemory()
  {
      INTERNALfinalizeMemory();
  }

  public static long mkGoal(long a0, boolean a1, boolean a2, boolean a3) throws Z3Exception
  {
      long res = INTERNALmkGoal(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void goalIncRef(long a0, long a1) throws Z3Exception
  {
      INTERNALgoalIncRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void goalDecRef(long a0, long a1) throws Z3Exception
  {
      INTERNALgoalDecRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static int goalPrecision(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgoalPrecision(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void goalAssert(long a0, long a1, long a2) throws Z3Exception
  {
      INTERNALgoalAssert(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static boolean goalInconsistent(long a0, long a1) throws Z3Exception
  {
      boolean res = INTERNALgoalInconsistent(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int goalDepth(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgoalDepth(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void goalReset(long a0, long a1) throws Z3Exception
  {
      INTERNALgoalReset(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static int goalSize(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgoalSize(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long goalFormula(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALgoalFormula(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int goalNumExprs(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALgoalNumExprs(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean goalIsDecidedSat(long a0, long a1) throws Z3Exception
  {
      boolean res = INTERNALgoalIsDecidedSat(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean goalIsDecidedUnsat(long a0, long a1) throws Z3Exception
  {
      boolean res = INTERNALgoalIsDecidedUnsat(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long goalTranslate(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALgoalTranslate(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String goalToString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALgoalToString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkTactic(long a0, String a1) throws Z3Exception
  {
      long res = INTERNALmkTactic(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void tacticIncRef(long a0, long a1) throws Z3Exception
  {
      INTERNALtacticIncRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void tacticDecRef(long a0, long a1) throws Z3Exception
  {
      INTERNALtacticDecRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static long mkProbe(long a0, String a1) throws Z3Exception
  {
      long res = INTERNALmkProbe(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void probeIncRef(long a0, long a1) throws Z3Exception
  {
      INTERNALprobeIncRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void probeDecRef(long a0, long a1) throws Z3Exception
  {
      INTERNALprobeDecRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static long tacticAndThen(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALtacticAndThen(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long tacticOrElse(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALtacticOrElse(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long tacticParOr(long a0, int a1, long[] a2) throws Z3Exception
  {
      long res = INTERNALtacticParOr(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long tacticParAndThen(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALtacticParAndThen(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long tacticTryFor(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALtacticTryFor(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long tacticWhen(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALtacticWhen(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long tacticCond(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALtacticCond(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long tacticRepeat(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALtacticRepeat(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long tacticSkip(long a0) throws Z3Exception
  {
      long res = INTERNALtacticSkip(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long tacticFail(long a0) throws Z3Exception
  {
      long res = INTERNALtacticFail(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long tacticFailIf(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALtacticFailIf(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long tacticFailIfNotDecided(long a0) throws Z3Exception
  {
      long res = INTERNALtacticFailIfNotDecided(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long tacticUsingParams(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALtacticUsingParams(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long probeConst(long a0, double a1) throws Z3Exception
  {
      long res = INTERNALprobeConst(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long probeLt(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALprobeLt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long probeGt(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALprobeGt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long probeLe(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALprobeLe(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long probeGe(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALprobeGe(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long probeEq(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALprobeEq(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long probeAnd(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALprobeAnd(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long probeOr(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALprobeOr(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long probeNot(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALprobeNot(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getNumTactics(long a0) throws Z3Exception
  {
      int res = INTERNALgetNumTactics(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String getTacticName(long a0, int a1) throws Z3Exception
  {
      String res = INTERNALgetTacticName(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getNumProbes(long a0) throws Z3Exception
  {
      int res = INTERNALgetNumProbes(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String getProbeName(long a0, int a1) throws Z3Exception
  {
      String res = INTERNALgetProbeName(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String tacticGetHelp(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALtacticGetHelp(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long tacticGetParamDescrs(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALtacticGetParamDescrs(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String tacticGetDescr(long a0, String a1) throws Z3Exception
  {
      String res = INTERNALtacticGetDescr(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String probeGetDescr(long a0, String a1) throws Z3Exception
  {
      String res = INTERNALprobeGetDescr(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static double probeApply(long a0, long a1, long a2) throws Z3Exception
  {
      double res = INTERNALprobeApply(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long tacticApply(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALtacticApply(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long tacticApplyEx(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALtacticApplyEx(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void applyResultIncRef(long a0, long a1) throws Z3Exception
  {
      INTERNALapplyResultIncRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void applyResultDecRef(long a0, long a1) throws Z3Exception
  {
      INTERNALapplyResultDecRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static String applyResultToString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALapplyResultToString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int applyResultGetNumSubgoals(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALapplyResultGetNumSubgoals(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long applyResultGetSubgoal(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALapplyResultGetSubgoal(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long applyResultConvertModel(long a0, long a1, int a2, long a3) throws Z3Exception
  {
      long res = INTERNALapplyResultConvertModel(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSolver(long a0) throws Z3Exception
  {
      long res = INTERNALmkSolver(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSimpleSolver(long a0) throws Z3Exception
  {
      long res = INTERNALmkSimpleSolver(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSolverForLogic(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkSolverForLogic(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkSolverFromTactic(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkSolverFromTactic(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long solverTranslate(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALsolverTranslate(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String solverGetHelp(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALsolverGetHelp(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long solverGetParamDescrs(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALsolverGetParamDescrs(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void solverSetParams(long a0, long a1, long a2) throws Z3Exception
  {
      INTERNALsolverSetParams(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void solverIncRef(long a0, long a1) throws Z3Exception
  {
      INTERNALsolverIncRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void solverDecRef(long a0, long a1) throws Z3Exception
  {
      INTERNALsolverDecRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void solverPush(long a0, long a1) throws Z3Exception
  {
      INTERNALsolverPush(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void solverPop(long a0, long a1, int a2) throws Z3Exception
  {
      INTERNALsolverPop(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void solverReset(long a0, long a1) throws Z3Exception
  {
      INTERNALsolverReset(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static int solverGetNumScopes(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALsolverGetNumScopes(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void solverAssert(long a0, long a1, long a2) throws Z3Exception
  {
      INTERNALsolverAssert(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void solverAssertAndTrack(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      INTERNALsolverAssertAndTrack(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static long solverGetAssertions(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALsolverGetAssertions(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int solverCheck(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALsolverCheck(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int solverCheckAssumptions(long a0, long a1, int a2, long[] a3) throws Z3Exception
  {
      int res = INTERNALsolverCheckAssumptions(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int getImpliedEqualities(long a0, long a1, int a2, long[] a3, int[] a4) throws Z3Exception
  {
      int res = INTERNALgetImpliedEqualities(a0, a1, a2, a3, a4);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int solverGetConsequences(long a0, long a1, long a2, long a3, long a4) throws Z3Exception
  {
      int res = INTERNALsolverGetConsequences(a0, a1, a2, a3, a4);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long solverGetModel(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALsolverGetModel(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long solverGetProof(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALsolverGetProof(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long solverGetUnsatCore(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALsolverGetUnsatCore(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String solverGetReasonUnknown(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALsolverGetReasonUnknown(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long solverGetStatistics(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALsolverGetStatistics(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String solverToString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALsolverToString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String statsToString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALstatsToString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void statsIncRef(long a0, long a1) throws Z3Exception
  {
      INTERNALstatsIncRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void statsDecRef(long a0, long a1) throws Z3Exception
  {
      INTERNALstatsDecRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static int statsSize(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALstatsSize(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String statsGetKey(long a0, long a1, int a2) throws Z3Exception
  {
      String res = INTERNALstatsGetKey(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean statsIsUint(long a0, long a1, int a2) throws Z3Exception
  {
      boolean res = INTERNALstatsIsUint(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean statsIsDouble(long a0, long a1, int a2) throws Z3Exception
  {
      boolean res = INTERNALstatsIsDouble(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int statsGetUintValue(long a0, long a1, int a2) throws Z3Exception
  {
      int res = INTERNALstatsGetUintValue(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static double statsGetDoubleValue(long a0, long a1, int a2) throws Z3Exception
  {
      double res = INTERNALstatsGetDoubleValue(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long getEstimatedAllocSize()
  {
      long res = INTERNALgetEstimatedAllocSize();
      return res;
  }

  public static long mkAstVector(long a0) throws Z3Exception
  {
      long res = INTERNALmkAstVector(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void astVectorIncRef(long a0, long a1) throws Z3Exception
  {
      INTERNALastVectorIncRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void astVectorDecRef(long a0, long a1) throws Z3Exception
  {
      INTERNALastVectorDecRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static int astVectorSize(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALastVectorSize(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long astVectorGet(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALastVectorGet(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void astVectorSet(long a0, long a1, int a2, long a3) throws Z3Exception
  {
      INTERNALastVectorSet(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void astVectorResize(long a0, long a1, int a2) throws Z3Exception
  {
      INTERNALastVectorResize(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void astVectorPush(long a0, long a1, long a2) throws Z3Exception
  {
      INTERNALastVectorPush(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static long astVectorTranslate(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALastVectorTranslate(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String astVectorToString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALastVectorToString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkAstMap(long a0) throws Z3Exception
  {
      long res = INTERNALmkAstMap(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void astMapIncRef(long a0, long a1) throws Z3Exception
  {
      INTERNALastMapIncRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void astMapDecRef(long a0, long a1) throws Z3Exception
  {
      INTERNALastMapDecRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static boolean astMapContains(long a0, long a1, long a2) throws Z3Exception
  {
      boolean res = INTERNALastMapContains(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long astMapFind(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALastMapFind(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void astMapInsert(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      INTERNALastMapInsert(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void astMapErase(long a0, long a1, long a2) throws Z3Exception
  {
      INTERNALastMapErase(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void astMapReset(long a0, long a1) throws Z3Exception
  {
      INTERNALastMapReset(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static int astMapSize(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALastMapSize(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long astMapKeys(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALastMapKeys(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String astMapToString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALastMapToString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean algebraicIsValue(long a0, long a1) throws Z3Exception
  {
      boolean res = INTERNALalgebraicIsValue(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean algebraicIsPos(long a0, long a1) throws Z3Exception
  {
      boolean res = INTERNALalgebraicIsPos(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean algebraicIsNeg(long a0, long a1) throws Z3Exception
  {
      boolean res = INTERNALalgebraicIsNeg(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean algebraicIsZero(long a0, long a1) throws Z3Exception
  {
      boolean res = INTERNALalgebraicIsZero(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int algebraicSign(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALalgebraicSign(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long algebraicAdd(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALalgebraicAdd(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long algebraicSub(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALalgebraicSub(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long algebraicMul(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALalgebraicMul(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long algebraicDiv(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALalgebraicDiv(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long algebraicRoot(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALalgebraicRoot(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long algebraicPower(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALalgebraicPower(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean algebraicLt(long a0, long a1, long a2) throws Z3Exception
  {
      boolean res = INTERNALalgebraicLt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean algebraicGt(long a0, long a1, long a2) throws Z3Exception
  {
      boolean res = INTERNALalgebraicGt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean algebraicLe(long a0, long a1, long a2) throws Z3Exception
  {
      boolean res = INTERNALalgebraicLe(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean algebraicGe(long a0, long a1, long a2) throws Z3Exception
  {
      boolean res = INTERNALalgebraicGe(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean algebraicEq(long a0, long a1, long a2) throws Z3Exception
  {
      boolean res = INTERNALalgebraicEq(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean algebraicNeq(long a0, long a1, long a2) throws Z3Exception
  {
      boolean res = INTERNALalgebraicNeq(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long algebraicRoots(long a0, long a1, int a2, long[] a3) throws Z3Exception
  {
      long res = INTERNALalgebraicRoots(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int algebraicEval(long a0, long a1, int a2, long[] a3) throws Z3Exception
  {
      int res = INTERNALalgebraicEval(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long polynomialSubresultants(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALpolynomialSubresultants(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void rcfDel(long a0, long a1) throws Z3Exception
  {
      INTERNALrcfDel(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static long rcfMkRational(long a0, String a1) throws Z3Exception
  {
      long res = INTERNALrcfMkRational(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long rcfMkSmallInt(long a0, int a1) throws Z3Exception
  {
      long res = INTERNALrcfMkSmallInt(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long rcfMkPi(long a0) throws Z3Exception
  {
      long res = INTERNALrcfMkPi(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long rcfMkE(long a0) throws Z3Exception
  {
      long res = INTERNALrcfMkE(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long rcfMkInfinitesimal(long a0) throws Z3Exception
  {
      long res = INTERNALrcfMkInfinitesimal(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int rcfMkRoots(long a0, int a1, long[] a2, long[] a3) throws Z3Exception
  {
      int res = INTERNALrcfMkRoots(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long rcfAdd(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALrcfAdd(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long rcfSub(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALrcfSub(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long rcfMul(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALrcfMul(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long rcfDiv(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALrcfDiv(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long rcfNeg(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALrcfNeg(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long rcfInv(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALrcfInv(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long rcfPower(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALrcfPower(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean rcfLt(long a0, long a1, long a2) throws Z3Exception
  {
      boolean res = INTERNALrcfLt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean rcfGt(long a0, long a1, long a2) throws Z3Exception
  {
      boolean res = INTERNALrcfGt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean rcfLe(long a0, long a1, long a2) throws Z3Exception
  {
      boolean res = INTERNALrcfLe(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean rcfGe(long a0, long a1, long a2) throws Z3Exception
  {
      boolean res = INTERNALrcfGe(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean rcfEq(long a0, long a1, long a2) throws Z3Exception
  {
      boolean res = INTERNALrcfEq(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean rcfNeq(long a0, long a1, long a2) throws Z3Exception
  {
      boolean res = INTERNALrcfNeq(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String rcfNumToString(long a0, long a1, boolean a2, boolean a3) throws Z3Exception
  {
      String res = INTERNALrcfNumToString(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String rcfNumToDecimalString(long a0, long a1, int a2) throws Z3Exception
  {
      String res = INTERNALrcfNumToDecimalString(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void rcfGetNumeratorDenominator(long a0, long a1, LongPtr a2, LongPtr a3) throws Z3Exception
  {
      INTERNALrcfGetNumeratorDenominator(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static long mkFixedpoint(long a0) throws Z3Exception
  {
      long res = INTERNALmkFixedpoint(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void fixedpointIncRef(long a0, long a1) throws Z3Exception
  {
      INTERNALfixedpointIncRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void fixedpointDecRef(long a0, long a1) throws Z3Exception
  {
      INTERNALfixedpointDecRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void fixedpointAddRule(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      INTERNALfixedpointAddRule(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void fixedpointAddFact(long a0, long a1, long a2, int a3, int[] a4) throws Z3Exception
  {
      INTERNALfixedpointAddFact(a0, a1, a2, a3, a4);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void fixedpointAssert(long a0, long a1, long a2) throws Z3Exception
  {
      INTERNALfixedpointAssert(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static int fixedpointQuery(long a0, long a1, long a2) throws Z3Exception
  {
      int res = INTERNALfixedpointQuery(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int fixedpointQueryRelations(long a0, long a1, int a2, long[] a3) throws Z3Exception
  {
      int res = INTERNALfixedpointQueryRelations(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long fixedpointGetAnswer(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALfixedpointGetAnswer(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String fixedpointGetReasonUnknown(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALfixedpointGetReasonUnknown(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void fixedpointUpdateRule(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      INTERNALfixedpointUpdateRule(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static int fixedpointGetNumLevels(long a0, long a1, long a2) throws Z3Exception
  {
      int res = INTERNALfixedpointGetNumLevels(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long fixedpointGetCoverDelta(long a0, long a1, int a2, long a3) throws Z3Exception
  {
      long res = INTERNALfixedpointGetCoverDelta(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void fixedpointAddCover(long a0, long a1, int a2, long a3, long a4) throws Z3Exception
  {
      INTERNALfixedpointAddCover(a0, a1, a2, a3, a4);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static long fixedpointGetStatistics(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALfixedpointGetStatistics(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void fixedpointRegisterRelation(long a0, long a1, long a2) throws Z3Exception
  {
      INTERNALfixedpointRegisterRelation(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void fixedpointSetPredicateRepresentation(long a0, long a1, long a2, int a3, long[] a4) throws Z3Exception
  {
      INTERNALfixedpointSetPredicateRepresentation(a0, a1, a2, a3, a4);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static long fixedpointGetRules(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALfixedpointGetRules(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long fixedpointGetAssertions(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALfixedpointGetAssertions(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void fixedpointSetParams(long a0, long a1, long a2) throws Z3Exception
  {
      INTERNALfixedpointSetParams(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static String fixedpointGetHelp(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALfixedpointGetHelp(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long fixedpointGetParamDescrs(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALfixedpointGetParamDescrs(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String fixedpointToString(long a0, long a1, int a2, long[] a3) throws Z3Exception
  {
      String res = INTERNALfixedpointToString(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long fixedpointFromString(long a0, long a1, String a2) throws Z3Exception
  {
      long res = INTERNALfixedpointFromString(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long fixedpointFromFile(long a0, long a1, String a2) throws Z3Exception
  {
      long res = INTERNALfixedpointFromFile(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void fixedpointPush(long a0, long a1) throws Z3Exception
  {
      INTERNALfixedpointPush(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void fixedpointPop(long a0, long a1) throws Z3Exception
  {
      INTERNALfixedpointPop(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static long mkOptimize(long a0) throws Z3Exception
  {
      long res = INTERNALmkOptimize(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void optimizeIncRef(long a0, long a1) throws Z3Exception
  {
      INTERNALoptimizeIncRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void optimizeDecRef(long a0, long a1) throws Z3Exception
  {
      INTERNALoptimizeDecRef(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void optimizeAssert(long a0, long a1, long a2) throws Z3Exception
  {
      INTERNALoptimizeAssert(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static int optimizeAssertSoft(long a0, long a1, long a2, String a3, long a4) throws Z3Exception
  {
      int res = INTERNALoptimizeAssertSoft(a0, a1, a2, a3, a4);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int optimizeMaximize(long a0, long a1, long a2) throws Z3Exception
  {
      int res = INTERNALoptimizeMaximize(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int optimizeMinimize(long a0, long a1, long a2) throws Z3Exception
  {
      int res = INTERNALoptimizeMinimize(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void optimizePush(long a0, long a1) throws Z3Exception
  {
      INTERNALoptimizePush(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void optimizePop(long a0, long a1) throws Z3Exception
  {
      INTERNALoptimizePop(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static int optimizeCheck(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALoptimizeCheck(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String optimizeGetReasonUnknown(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALoptimizeGetReasonUnknown(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long optimizeGetModel(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALoptimizeGetModel(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void optimizeSetParams(long a0, long a1, long a2) throws Z3Exception
  {
      INTERNALoptimizeSetParams(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static long optimizeGetParamDescrs(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALoptimizeGetParamDescrs(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long optimizeGetLower(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALoptimizeGetLower(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long optimizeGetUpper(long a0, long a1, int a2) throws Z3Exception
  {
      long res = INTERNALoptimizeGetUpper(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String optimizeToString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALoptimizeToString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void optimizeFromString(long a0, long a1, String a2) throws Z3Exception
  {
      INTERNALoptimizeFromString(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static void optimizeFromFile(long a0, long a1, String a2) throws Z3Exception
  {
      INTERNALoptimizeFromFile(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static String optimizeGetHelp(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALoptimizeGetHelp(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long optimizeGetStatistics(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALoptimizeGetStatistics(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long optimizeGetAssertions(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALoptimizeGetAssertions(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long optimizeGetObjectives(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALoptimizeGetObjectives(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkInterpolant(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkInterpolant(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkInterpolationContext(long a0) throws Z3Exception
  {
      long res = INTERNALmkInterpolationContext(a0);
      if (res == 0)
          throw new Z3Exception("Object allocation failed.");
      return res;
  }

  public static long getInterpolant(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALgetInterpolant(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int computeInterpolant(long a0, long a1, long a2, LongPtr a3, LongPtr a4) throws Z3Exception
  {
      int res = INTERNALcomputeInterpolant(a0, a1, a2, a3, a4);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String interpolationProfile(long a0) throws Z3Exception
  {
      String res = INTERNALinterpolationProfile(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int readInterpolationProblem(long a0, IntPtr a1, ObjArrayPtr a2, UIntArrayPtr a3, String a4, StringPtr a5, IntPtr a6, ObjArrayPtr a7) throws Z3Exception
  {
      int res = INTERNALreadInterpolationProblem(a0, a1, a2, a3, a4, a5, a6, a7);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int checkInterpolant(long a0, int a1, long[] a2, int[] a3, long[] a4, StringPtr a5, int a6, long[] a7) throws Z3Exception
  {
      int res = INTERNALcheckInterpolant(a0, a1, a2, a3, a4, a5, a6, a7);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static void writeInterpolationProblem(long a0, int a1, long[] a2, int[] a3, String a4, int a5, long[] a6) throws Z3Exception
  {
      INTERNALwriteInterpolationProblem(a0, a1, a2, a3, a4, a5, a6);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
  }

  public static long mkFpaRoundingModeSort(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaRoundingModeSort(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaRoundNearestTiesToEven(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaRoundNearestTiesToEven(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaRne(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaRne(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaRoundNearestTiesToAway(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaRoundNearestTiesToAway(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaRna(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaRna(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaRoundTowardPositive(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaRoundTowardPositive(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaRtp(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaRtp(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaRoundTowardNegative(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaRoundTowardNegative(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaRtn(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaRtn(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaRoundTowardZero(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaRoundTowardZero(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaRtz(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaRtz(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaSort(long a0, int a1, int a2) throws Z3Exception
  {
      long res = INTERNALmkFpaSort(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaSortHalf(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaSortHalf(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaSort16(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaSort16(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaSortSingle(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaSortSingle(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaSort32(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaSort32(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaSortDouble(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaSortDouble(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaSort64(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaSort64(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaSortQuadruple(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaSortQuadruple(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaSort128(long a0) throws Z3Exception
  {
      long res = INTERNALmkFpaSort128(a0);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaNan(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkFpaNan(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaInf(long a0, long a1, boolean a2) throws Z3Exception
  {
      long res = INTERNALmkFpaInf(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaZero(long a0, long a1, boolean a2) throws Z3Exception
  {
      long res = INTERNALmkFpaZero(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaFp(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALmkFpaFp(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaNumeralFloat(long a0, float a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkFpaNumeralFloat(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaNumeralDouble(long a0, double a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkFpaNumeralDouble(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaNumeralInt(long a0, int a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkFpaNumeralInt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaNumeralIntUint(long a0, boolean a1, int a2, int a3, long a4) throws Z3Exception
  {
      long res = INTERNALmkFpaNumeralIntUint(a0, a1, a2, a3, a4);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaNumeralInt64Uint64(long a0, boolean a1, long a2, long a3, long a4) throws Z3Exception
  {
      long res = INTERNALmkFpaNumeralInt64Uint64(a0, a1, a2, a3, a4);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaAbs(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkFpaAbs(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaNeg(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkFpaNeg(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaAdd(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALmkFpaAdd(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaSub(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALmkFpaSub(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaMul(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALmkFpaMul(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaDiv(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALmkFpaDiv(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaFma(long a0, long a1, long a2, long a3, long a4) throws Z3Exception
  {
      long res = INTERNALmkFpaFma(a0, a1, a2, a3, a4);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaSqrt(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkFpaSqrt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaRem(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkFpaRem(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaRoundToIntegral(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkFpaRoundToIntegral(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaMin(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkFpaMin(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaMax(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkFpaMax(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaLeq(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkFpaLeq(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaLt(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkFpaLt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaGeq(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkFpaGeq(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaGt(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkFpaGt(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaEq(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkFpaEq(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaIsNormal(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkFpaIsNormal(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaIsSubnormal(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkFpaIsSubnormal(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaIsZero(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkFpaIsZero(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaIsInfinite(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkFpaIsInfinite(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaIsNan(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkFpaIsNan(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaIsNegative(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkFpaIsNegative(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaIsPositive(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkFpaIsPositive(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaToFpBv(long a0, long a1, long a2) throws Z3Exception
  {
      long res = INTERNALmkFpaToFpBv(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaToFpFloat(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALmkFpaToFpFloat(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaToFpReal(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALmkFpaToFpReal(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaToFpSigned(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALmkFpaToFpSigned(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaToFpUnsigned(long a0, long a1, long a2, long a3) throws Z3Exception
  {
      long res = INTERNALmkFpaToFpUnsigned(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaToUbv(long a0, long a1, long a2, int a3) throws Z3Exception
  {
      long res = INTERNALmkFpaToUbv(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaToSbv(long a0, long a1, long a2, int a3) throws Z3Exception
  {
      long res = INTERNALmkFpaToSbv(a0, a1, a2, a3);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaToReal(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkFpaToReal(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int fpaGetEbits(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALfpaGetEbits(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static int fpaGetSbits(long a0, long a1) throws Z3Exception
  {
      int res = INTERNALfpaGetSbits(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean fpaGetNumeralSign(long a0, long a1, IntPtr a2) throws Z3Exception
  {
      boolean res = INTERNALfpaGetNumeralSign(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String fpaGetNumeralSignificandString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALfpaGetNumeralSignificandString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean fpaGetNumeralSignificandUint64(long a0, long a1, LongPtr a2) throws Z3Exception
  {
      boolean res = INTERNALfpaGetNumeralSignificandUint64(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static String fpaGetNumeralExponentString(long a0, long a1) throws Z3Exception
  {
      String res = INTERNALfpaGetNumeralExponentString(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static boolean fpaGetNumeralExponentInt64(long a0, long a1, LongPtr a2) throws Z3Exception
  {
      boolean res = INTERNALfpaGetNumeralExponentInt64(a0, a1, a2);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaToIeeeBv(long a0, long a1) throws Z3Exception
  {
      long res = INTERNALmkFpaToIeeeBv(a0, a1);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

  public static long mkFpaToFpIntReal(long a0, long a1, long a2, long a3, long a4) throws Z3Exception
  {
      long res = INTERNALmkFpaToFpIntReal(a0, a1, a2, a3, a4);
      Z3_error_code err = Z3_error_code.fromInt(INTERNALgetErrorCode(a0));
      if (err != Z3_error_code.Z3_OK)
          throw new Z3Exception(INTERNALgetErrorMsg(a0, err.toInt()));
      return res;
  }

}
