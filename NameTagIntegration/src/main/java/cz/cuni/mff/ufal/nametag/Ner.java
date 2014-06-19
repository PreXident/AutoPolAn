/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package cz.cuni.mff.ufal.nametag;

public class Ner {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected Ner(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Ner obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        nametag_javaJNI.delete_Ner(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static Ner load(String fname) {
    long cPtr = nametag_javaJNI.Ner_load(fname);
    return (cPtr == 0) ? null : new Ner(cPtr, true);
  }

  public void recognize(Forms forms, NamedEntities entities) {
    nametag_javaJNI.Ner_recognize(swigCPtr, this, Forms.getCPtr(forms), forms, NamedEntities.getCPtr(entities), entities);
  }

  public Tokenizer newTokenizer() {
    long cPtr = nametag_javaJNI.Ner_newTokenizer(swigCPtr, this);
    return (cPtr == 0) ? null : new Tokenizer(cPtr, true);
  }

}