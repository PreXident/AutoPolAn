/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package cz.cuni.mff.ufal.morphodita;

public class Forms {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected Forms(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Forms obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        morphodita_javaJNI.delete_Forms(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public Forms() {
    this(morphodita_javaJNI.new_Forms__SWIG_0(), true);
  }

  public Forms(long n) {
    this(morphodita_javaJNI.new_Forms__SWIG_1(n), true);
  }

  public long size() {
    return morphodita_javaJNI.Forms_size(swigCPtr, this);
  }

  public long capacity() {
    return morphodita_javaJNI.Forms_capacity(swigCPtr, this);
  }

  public void reserve(long n) {
    morphodita_javaJNI.Forms_reserve(swigCPtr, this, n);
  }

  public boolean isEmpty() {
    return morphodita_javaJNI.Forms_isEmpty(swigCPtr, this);
  }

  public void clear() {
    morphodita_javaJNI.Forms_clear(swigCPtr, this);
  }

  public void add(String x) {
    morphodita_javaJNI.Forms_add(swigCPtr, this, x);
  }

  public String get(int i) {
    return morphodita_javaJNI.Forms_get(swigCPtr, this, i);
  }

  public void set(int i, String val) {
    morphodita_javaJNI.Forms_set(swigCPtr, this, i, val);
  }

}
