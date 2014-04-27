/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package cz.cuni.mff.ufal.nametag;

public class nametag_javaJNI {
  public final static native long new_Forms__SWIG_0();
  public final static native long new_Forms__SWIG_1(long jarg1);
  public final static native long Forms_size(long jarg1, Forms jarg1_);
  public final static native long Forms_capacity(long jarg1, Forms jarg1_);
  public final static native void Forms_reserve(long jarg1, Forms jarg1_, long jarg2);
  public final static native boolean Forms_isEmpty(long jarg1, Forms jarg1_);
  public final static native void Forms_clear(long jarg1, Forms jarg1_);
  public final static native void Forms_add(long jarg1, Forms jarg1_, String jarg2);
  public final static native String Forms_get(long jarg1, Forms jarg1_, int jarg2);
  public final static native void Forms_set(long jarg1, Forms jarg1_, int jarg2, String jarg3);
  public final static native void delete_Forms(long jarg1);
  public final static native void TokenRange_start_set(long jarg1, TokenRange jarg1_, long jarg2);
  public final static native long TokenRange_start_get(long jarg1, TokenRange jarg1_);
  public final static native void TokenRange_length_set(long jarg1, TokenRange jarg1_, long jarg2);
  public final static native long TokenRange_length_get(long jarg1, TokenRange jarg1_);
  public final static native long new_TokenRange();
  public final static native void delete_TokenRange(long jarg1);
  public final static native long new_TokenRanges__SWIG_0();
  public final static native long new_TokenRanges__SWIG_1(long jarg1);
  public final static native long TokenRanges_size(long jarg1, TokenRanges jarg1_);
  public final static native long TokenRanges_capacity(long jarg1, TokenRanges jarg1_);
  public final static native void TokenRanges_reserve(long jarg1, TokenRanges jarg1_, long jarg2);
  public final static native boolean TokenRanges_isEmpty(long jarg1, TokenRanges jarg1_);
  public final static native void TokenRanges_clear(long jarg1, TokenRanges jarg1_);
  public final static native void TokenRanges_add(long jarg1, TokenRanges jarg1_, long jarg2, TokenRange jarg2_);
  public final static native long TokenRanges_get(long jarg1, TokenRanges jarg1_, int jarg2);
  public final static native void TokenRanges_set(long jarg1, TokenRanges jarg1_, int jarg2, long jarg3, TokenRange jarg3_);
  public final static native void delete_TokenRanges(long jarg1);
  public final static native void NamedEntity_start_set(long jarg1, NamedEntity jarg1_, long jarg2);
  public final static native long NamedEntity_start_get(long jarg1, NamedEntity jarg1_);
  public final static native void NamedEntity_length_set(long jarg1, NamedEntity jarg1_, long jarg2);
  public final static native long NamedEntity_length_get(long jarg1, NamedEntity jarg1_);
  public final static native void NamedEntity_type_set(long jarg1, NamedEntity jarg1_, String jarg2);
  public final static native String NamedEntity_type_get(long jarg1, NamedEntity jarg1_);
  public final static native long new_NamedEntity__SWIG_0();
  public final static native long new_NamedEntity__SWIG_1(long jarg1, long jarg2, String jarg3);
  public final static native void delete_NamedEntity(long jarg1);
  public final static native long new_NamedEntities__SWIG_0();
  public final static native long new_NamedEntities__SWIG_1(long jarg1);
  public final static native long NamedEntities_size(long jarg1, NamedEntities jarg1_);
  public final static native long NamedEntities_capacity(long jarg1, NamedEntities jarg1_);
  public final static native void NamedEntities_reserve(long jarg1, NamedEntities jarg1_, long jarg2);
  public final static native boolean NamedEntities_isEmpty(long jarg1, NamedEntities jarg1_);
  public final static native void NamedEntities_clear(long jarg1, NamedEntities jarg1_);
  public final static native void NamedEntities_add(long jarg1, NamedEntities jarg1_, long jarg2, NamedEntity jarg2_);
  public final static native long NamedEntities_get(long jarg1, NamedEntities jarg1_, int jarg2);
  public final static native void NamedEntities_set(long jarg1, NamedEntities jarg1_, int jarg2, long jarg3, NamedEntity jarg3_);
  public final static native void delete_NamedEntities(long jarg1);
  public final static native void Version_major_set(long jarg1, Version jarg1_, long jarg2);
  public final static native long Version_major_get(long jarg1, Version jarg1_);
  public final static native void Version_minor_set(long jarg1, Version jarg1_, long jarg2);
  public final static native long Version_minor_get(long jarg1, Version jarg1_);
  public final static native void Version_patch_set(long jarg1, Version jarg1_, long jarg2);
  public final static native long Version_patch_get(long jarg1, Version jarg1_);
  public final static native long Version_current();
  public final static native long new_Version();
  public final static native void delete_Version(long jarg1);
  public final static native void delete_Tokenizer(long jarg1);
  public final static native void Tokenizer_setText(long jarg1, Tokenizer jarg1_, String jarg2);
  public final static native boolean Tokenizer_nextSentence(long jarg1, Tokenizer jarg1_, long jarg2, Forms jarg2_, long jarg3, TokenRanges jarg3_);
  public final static native long Tokenizer_newVerticalTokenizer();
  public final static native void delete_Ner(long jarg1);
  public final static native long Ner_load(String jarg1);
  public final static native void Ner_recognize(long jarg1, Ner jarg1_, long jarg2, Forms jarg2_, long jarg3, NamedEntities jarg3_);
  public final static native long Ner_newTokenizer(long jarg1, Ner jarg1_);

  static {
    java.io.File localNametag = new java.io.File(System.mapLibraryName("nametag_java"));

    if (localNametag.exists())
      System.load(localNametag.getAbsolutePath());
    else
      System.loadLibrary("nametag_java");
  }

}
