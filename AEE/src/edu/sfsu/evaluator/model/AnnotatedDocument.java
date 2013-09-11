/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Eric Chiang
 */
public class AnnotatedDocument implements java.io.Serializable
{

    /*
     * Labels associated with every document version.
     */
    private String documentText;
    private HashMap<String, AnnotatedDocumentVersion> versions;

    public AnnotatedDocument(String documentText)
    {
        this.documentText = documentText;
        versions = new HashMap();
    }

    public String getDocumentText()
    {
        return documentText;
    }

    public boolean createAnnotationVersion(String name)
    {
        if (versions.containsKey(name))
        {
            System.err.printf("Already an annotated version named '%s'", name);
            return false;
        }
        versions.put(name, new AnnotatedDocumentVersion());
        return true;
    }

    public AnnotatedDocumentVersion getAnnotationVersion(String name)
    {
        if (!versions.containsKey(name))
        {
            System.err.printf("No version named '%s'", name);
            return null;
        }
        return versions.get(name);
    }

    public ArrayList<String> getAnnotationVersionsNames()
    {
        return new ArrayList(versions.keySet());
    }

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
