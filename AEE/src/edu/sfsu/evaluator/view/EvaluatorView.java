/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view;

/**
 * The View interface which the ViewModel uses to interact with GUI elements.
 * @author Eric Chiang
 */
public interface EvaluatorView
{

    /**
     * Display information pertaining to a specific document version. For some
     * classes like the <code>EvaluatorCorpusTree</code> that may mean doing
     * nothing. For others like the <code>EvaluatorTextScreen</code> that may
     * mean doing a great deal to load the document.
     * <p/>
     * @param docName
     * @param verName
     */
    public abstract void display(
            String docName,
            String verName);

    /**
     * Re-render this view element to reflect changes in the model.
     */
    public abstract void repaintView();

    /**
     * Notify the view that a document has been renamed.
     * @param oldDocName
     * @param newDocName
     */
    public abstract void updateDocuentRenamed(
            String oldDocName,
            String newDocName);

    /**
     * Notify the view that a document version has been renamed.
     * @param docName
     * @param oldVerName
     * @param newVerName
     */
    public abstract void updateDocumentVersionRenamed(
            String docName,
            String oldVerName,
            String newVerName);

    /**
     * Notify the view that the model has been set.
     */
    public abstract void updateModelSet();
}