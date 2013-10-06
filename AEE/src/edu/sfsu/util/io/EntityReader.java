/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.util.io;

import edu.sfsu.evaluator.model.Entity;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.*;
import org.xml.sax.*;

/**
 * Read entities from xml style representation of entities with SAX parser. 
 * @author Anthony
 */
public class EntityReader
{
    private static SaxHandler handler;
    private static SAXParser parser;
    private static SAXParserFactory factory;
    
    /**
     * 
     * @param filePath String
     * @return
     * @throws FileNotFoundException 
     */
    public static ArrayList<Entity> readEntities(String filePath)
            throws FileNotFoundException
    {
        try {
            factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newSAXParser();
            handler = new SaxHandler();
            parser.parse(filePath, handler);
        } 
        catch (FactoryConfigurationError e) {
            // unable to get a document builder factory
        } 
        catch (ParserConfigurationException e) {
            // parser was unable to be configured
        } catch (SAXException e) {
            // parsing error
        } 
        catch (IOException e) {
        // i/o error
        }
        
        return handler.getAnnotations();
    }
     
}
