/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.cuni.mff.ufal.textan.textpro.data;

import cz.cuni.mff.ufal.textan.textpro.data.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
/**
 *
 * @author HOANGT
 */
public class TestFeaturesComputeValue {
    
    /*
     Sample test
    */
    @Test
    public void testAdd1Plus1() 
    {
      int x  = 1 ; int y = 1;
      assertEquals("test sum, nothing else", 2, x+y);
    }
    /*
    Test assertTrue
    */
    @Test
    public void testAssertTrue() 
    {
      assertTrue("Test something true", true);
    }
    
    /*
     Test the similarity metrics between two strings
    */
    @Test
    public void testEntityTextAndObjectAlias1() 
    {
      String entityText = "John Jr.";
      String objectAlias = "John";
      double sim = FeaturesComputeValue.EntityTextAndObjectAlias(entityText, objectAlias);
      boolean check = (sim < 100);
      assertTrue("The similarity is smaller than 1", check);
    }
    
    
    /*
     Test if an entity and an object have the same type
    */
    @Test
    public void testEntityTypeAndObjectType() 
    {
      String entityType = "Person";
      String objectType = "Person";
      double sim = FeaturesComputeValue.EntityTypeAndObjectType(entityType, objectType);
      boolean check = (sim == 1);
      assertTrue("Two types are the same", check);
    }
}