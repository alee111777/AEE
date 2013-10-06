/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.util.io;
import edu.sfsu.evaluator.model.Entity;
import java.util.ArrayList;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

/**
 * This is the handler for the EntityReader sax parser.
 * @author anthony
 */
public class SaxHandler extends DefaultHandler {
    

    private String text, label;
    private int start, end;
    private String tag;  
    private ArrayList<Entity> annotations;
    
    SaxHandler() {
        annotations = new ArrayList<Entity>();
    }
    
    @Override
    public void startDocument() throws SAXException {
    }

    @Override
    public void endDocument() throws SAXException {
        
    }

    @Override
    public void startElement(String uri, String localName,
            String qName, Attributes attributes)
    throws SAXException {
        tag = localName;
    }

    @Override
    public void endElement(String uri, String localName, String qName)
    throws SAXException {
        tag = "";
        if (localName.matches("annotation")) {
            Entity ent = new Entity(text, label, start, end);
            annotations.add(ent);  
        }
    }

    @Override
    public void characters(char ch[], int start, int length)
    throws SAXException {
        if (tag.matches("mentionType")) {
            label = String.valueOf(ch, start, length);
        
        } else if (tag.matches("start")) {
            String str = String.valueOf(ch, start, length);
            this.start = Integer.parseInt(str);
            
        } else if (tag.matches("end")) {
            String str = String.valueOf(ch, start, length);
            end = Integer.parseInt(str);
            
        } else if (tag.matches("spannedText")) {
            text = String.valueOf(ch, start, length);
        }
        
    }

    @Override
    public void ignorableWhitespace(char ch[], int start, int length)
    throws SAXException {
    }
    
    public ArrayList<Entity> getAnnotations() {
        return annotations;
    }

}    
