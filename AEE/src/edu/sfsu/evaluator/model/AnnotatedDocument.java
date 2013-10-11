/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Annotated document. Only information associated with this class is the
 * document text and the list of document versions.
 * @author Eric Chiang
 */
public class AnnotatedDocument implements java.io.Serializable
{

    // Labels associated with every document version.
    private String documentText;
    // Annotation versions
    private HashMap<String, AnnotatedDocumentVersion> versions;
    private String baseLineVersion;

    public AnnotatedDocument(String documentText)
    {
        this.documentText = documentText;
        versions = new HashMap();
    }

    /**
     * Get document text.
     * @return
     */
    public String getDocumentText()
    {
        return documentText;
    }
    
    public AnnotatedDocumentVersion getBaseLineVersion() {
        return versions.get(baseLineVersion);
    }
    
    public void setBaseLine(String version) {
        baseLineVersion = version;
    }
    
    public ArrayList<AnnotatedDocumentVersion> getVersions() {
        ArrayList<AnnotatedDocumentVersion> out;
        out = new ArrayList<AnnotatedDocumentVersion>();
        
        for (String key : versions.keySet()) {
            out.add(versions.get(key));
        }
        
        return out;
    }

    /**
     * Creates a version of this document. Returns true if successful.
     * @param name
     * @return
     */
    public boolean createAnnotationVersion(String name)
    {
        // Ensure that name is an unique one.
        if (versions.containsKey(name))
        {
            System.err.printf("Already an annotated version named '%s'", name);
            return false;
        }
        versions.put(name, new AnnotatedDocumentVersion());
        return true;
    }

    /**
     * Get a version by name.
     * @param name String
     * @return
     */
    public AnnotatedDocumentVersion getAnnotationVersion(String name)
    {
        if (!versions.containsKey(name))
        {
            System.err.printf("No version named '%s'", name);
            return null;
        }
        return versions.get(name);
    }

    /**
     * Get the list of version names.
     * @return
     */
    public ArrayList<String> getAnnotationVersionsNames()
    {
        return new ArrayList(versions.keySet());
    }

    /**
     * Remove a version by name. Returns true if successful.
     * @param name
     * @return
     */
    public boolean removeAnnotationVersion(String name)
    {
        if (!versions.containsKey(name))
        {
            System.err.printf("No version named '%s'", name);
            return false;
        }
        versions.remove(name);
        return true;
    }

    /**
     * Rename a version. Return true if successful.
     * @param oldName
     * @param newName
     * @return
     */
    public boolean renameAnnotationVersion(String oldName, String newName)
    {
        if (!versions.containsKey(oldName))
        {
            System.err.printf("No version named '%s'", oldName);
            return false;
        }
        if (versions.containsKey(newName))
        {
            System.err.printf("Already a version named '%s'", newName);
            return false;
        }
        versions.put(newName, versions.remove(oldName));
        return true;
    }
}
