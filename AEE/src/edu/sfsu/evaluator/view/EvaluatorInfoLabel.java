/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view;

import edu.sfsu.evaluator.EvaluatorController;
import edu.sfsu.evaluator.EvaluatorViewModel;
import java.awt.Color;
import javax.swing.JLabel;

/**
 * Info label right bellow the text screen in the main GUI. Displays which
 * document version is being viewed by the text screen.
 * @author Eric Chiang
 */
public class EvaluatorInfoLabel extends JLabel implements EvaluatorView
{

    private EvaluatorViewModel viewModel;
    private EvaluatorController controller;
    private boolean isDisplaying = false;
    // Displayed document version
    private String displayedDocName = null;
    private String displayedVerName = null;
    protected static final String INITIAL_MESSAGE =
            "[INFO] No workspace declared. "
            + "Use File -> Set Workspace to declare workspace";

    public EvaluatorInfoLabel(EvaluatorViewModel viewModel,
                         EvaluatorController controller)
    {
        this.viewModel = viewModel;
        this.controller = controller;
        repaintView();
    }

    /**
     * Display the name of the specific document version in the info label.
     * @param displayedDocName
     * @param displayedVerName
     */
    @Override
    public void display(String docName, String verName)
    {
        isDisplaying = true;
        displayedDocName = docName;
        displayedVerName = verName;
        repaintView();
    }

    /**
     * Is this document being displayed?
     * <p/>
     * @param displayedDocName
     * @return
     */
    public boolean isDisplaying(String docName)
    {
        if (!isDisplaying)
        {
            return false;
        }
        return (this.displayedDocName.compareTo(docName) == 0);
    }

    /**
     * Is this document version being displayed?
     * <p/>
     * @param displayedDocName
     * @param displayedVerName
     * @return
     */
    public boolean isDisplaying(String docName, String verName)
    {
        if (!isDisplaying)
        {
            return false;
        }
        return ((this.displayedDocName.compareTo(docName) == 0)
                && (this.displayedVerName.compareTo(verName) == 0));
    }

    /**
     * If displayed document version is renamed record that change.
     * @param docName
     * @param oldVerName
     * @param newVerName
     */
    @Override
    public void updateDocumentVersionRenamed(String docName, String oldVerName,
                                             String newVerName)
    {
        if (isDisplaying(docName, oldVerName))
        {
            displayedVerName = newVerName;
            repaintView();
        }
    }

    /**
     * When model is set, repaint the view.
     */
    @Override
    public void updateModelSet()
    {
        repaintView();
    }

    /**
     * Re-render the label to reflect the current document version being
     * displayed.
     */
    @Override
    public void repaintView()
    {
        // If the model has not been set just display initial message
        if (!viewModel.isModelSet())
        {
            this.setText(INITIAL_MESSAGE);
            setForeground(Color.gray);
            return;
        }

        // show workspace
        String workspace = viewModel.getWorkspacePath();
        // Determine if displayed document version still exists
        if (!viewModel.containsDocumentVersion(displayedDocName, displayedVerName))
        {
            isDisplaying = false;
        }
        String message;
        if (isDisplaying)
        {
            message = String.format(
                    "[INFO] Workspace: (%s)   Document: (%s)   Version: (%s)",
                    workspace, displayedDocName, displayedVerName);
        } else
        {
            message = String.format("[INFO] Workspace: %s", workspace);
        }
        // Set the text
        setText(message);
    }

    /**
     * If displayed document is renamed record that change.
     * @param oldDocName
     * @param newDocName
     */
    @Override
    public void updateDocuentRenamed(String oldDocName, String newDocName)
    {
        if (isDisplaying(oldDocName))
        {
            displayedDocName = newDocName;

        }
    }
}
