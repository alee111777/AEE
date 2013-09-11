/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view;

/**
 *
 * @author eric
 */
public interface EvaluatorView
{

    /**
     * Display information pertaining to a specific document version.
     * <p/>
     * @param docName
     * @param verName
     */
    public abstract void display(
            String docName,
            String verName);

    public abstract void repaintView();

    public abstract void updateDocuentRenamed(
            String oldDocName,
            String newDocName);

    public abstract void updateDocumentVersionRenamed(
            String docName,
            String oldVerName,
            String newVerName);

    public abstract void updateModelSet();
}
