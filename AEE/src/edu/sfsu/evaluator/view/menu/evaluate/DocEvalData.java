/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.menu.evaluate;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author anthony
 */
public class DocEvalData extends MentionData {  
    private HashMap<String, MentionData> mentionList;
    
    public DocEvalData(String newDocName) { 
        super(newDocName); 
        mentionList = new HashMap<String, MentionData>();
    }
    
    public MentionData getMentionData(String mentionType) {
        return mentionList.get(mentionType);
    }
    
    public Set<String> getMentionList() {
        return mentionList.keySet();
    }
    
    public boolean containsMention(String mentionType) {
        return mentionList.containsKey(mentionType);
    }
    
    public void addMentionData(String mentionType, MentionData mentionData) {
        mentionList.put(mentionType, mentionData);
    }
   
    @Override
    public void calculate() {
        super.calculate();
        for (String mentionName : mentionList.keySet()) {
            mentionList.get(mentionName).calculate();
        } 
    } 
    
}