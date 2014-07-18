/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package cz.cuni.mff.ufal.morphodita;

public class Version {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected Version(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(Version obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        morphodita_javaJNI.delete_Version(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setMajor(long value) {
    morphodita_javaJNI.Version_major_set(swigCPtr, this, value);
  }

  public long getMajor() {
    return morphodita_javaJNI.Version_major_get(swigCPtr, this);
  }

  public void setMinor(long value) {
    morphodita_javaJNI.Version_minor_set(swigCPtr, this, value);
  }

  public long getMinor() {
    return morphodita_javaJNI.Version_minor_get(swigCPtr, this);
  }

  public void setPatch(long value) {
    morphodita_javaJNI.Version_patch_set(swigCPtr, this, value);
  }

  public long getPatch() {
    return morphodita_javaJNI.Version_patch_get(swigCPtr, this);
  }

  public static Version current() {
    return new Version(morphodita_javaJNI.Version_current(), true);
  }

  public Version() {
    this(morphodita_javaJNI.new_Version(), true);
  }

}