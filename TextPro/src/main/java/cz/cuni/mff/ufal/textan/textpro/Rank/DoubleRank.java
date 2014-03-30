/*
 * The main ranking, written in March 29, 2014
 */
package cz.cuni.mff.ufal.textan.textpro.Rank;

import cz.cuni.mff.ufal.textan.Data.Entity;
import java.util.ArrayList;

/**
 *
 * @author Tamhd
 */
public class DoubleRank {
    // The input document : Petr was born 27.03.2014 at Malastranska. He and his friend David.
    public String inputText;
    
    // The input list of entity
    public ArrayList<Entity> inputEntityList;
    
    // The list of objects in database
    public ArrayList<Object> database;
    
    /*
     * Blank Constructor
     */
    public DoubleRank(){
        
    }
    
    /*
     * Constructor
     */
    public DoubleRank(String input){
        this.inputText = input;
    }
    
    
    /*
     * Compare entity and object if they share the same alias
     */
    public boolean CompareEntityAndObject(Entity e, Object o){
        ArrayList<String> alias = (ArrayList<String>)o.getAlias();
        if(alias.indexOf(e.getText()) == -1) {
            return false;
        }
        return true;
    }
    
    /*
     * Check if the object is in the text or not
     * 
     */
    public boolean CheckObjectInText(Object e, ArrayList<Entity> en) {
        for (int i = 0; i < en.size(); i ++) {
            if(CompareEntityAndObject(en.get(i), e)) {
                return true;
            }
        }
        return false;
    }
    /*
     * Find all objects in database which may appear in text
     */
    public ArrayList<Object> FindObjects(Entity e){
        ArrayList<Object> result = new ArrayList<Object>();
        for (int i = 0; i < database.size(); i ++) {
            if(CompareEntityAndObject(e, database.get(i))) {
                result.add(database.get(i));
            }
        }
        return result;
    }
    
    /*
     * Find all objects in database which may appear in text
     */
    public ArrayList<Object> FindAllObjects(){
        ArrayList<Object> result = new ArrayList<Object>();
        for (int i = 0; i < database.size(); i ++) {
            if(CheckObjectInText(database.get(i), this.inputEntityList)) {
                result.add(database.get(i));
            }
        }
        return result;
    }
    
    
    /*
     * Rank the objects
     */
    
    
}
