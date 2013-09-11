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
 *
 * @author eric
 */
public class EvaluatorInfo extends JLabel implements EvaluatorView
{

    private EvaluatorViewModel viewModel;
    private EvaluatorController controller;
    private boolean docLoaded = false;
    private String docName = null;
    private String verName = null;
    protected static final String initialMessage =
            "[INFO] No workspace declared. "
            + "Use File -> Set Workspace to declare workspace";

    public EvaluatorInfo(EvaluatorViewModel viewModel,
                         EvaluatorController controller)
    {
        this.viewModel = viewModel;
        this.controller = controller;
        repaintView();
    }

    @Override
    public void display(String docName, String verName)
    {
        docLoaded = true;
        this.docName = docName;
        this.verName = verName;
        repaintView();
    }

    /**
     * Is this document being displayed?
     * <p/>
     * @param docName
     * @return
     */
    public boolean isDisplaying(String docName)
    {
        if (!docLoaded)
        {
            return false;
        }
        return (this.docName.compareTo(docName) == 0);
    }

    /**
     * Is this document version being displayed?
     * <p/>
     * @param docName
     * @param verName
     * @return
     */
    public boolean isDisplaying(String docName, String verName)
    {
        if (!docLoaded)
        {
            return false;
        }
        return ((this.docName.compareTo(docName) == 0)
                && (this.verName.compareTo(verName) == 0));
    }

    @Override
    public void updateDocumentVersionRenamed(String docName, String oldVerName,
                                             String newVerName)
    {
        if (isDisplaying(docName, oldVerName))
        {
            verName = newVerName;
            repaintView();
        }
    }

    @Override
    public void updateModelSet()
    {
        repaintView();
    }

    @Override
    public void repaintView()
    {
        if (!viewModel.isModelLoaded())
        {
            this.setText(initialMessage);
            setForeground(Color.gray);
            return;
        }
        String workspace = viewModel.getWorkspacePath();
        if (!viewModel.containsDocumentVersion(docName, verName))
        {
            docLoaded = false;
        }
        String message;
        if (docLoaded)
        {
            message = String.format(
                    "[INFO] Workspace: (%s)   Document: (%s)   Version: (%s)",
                    workspace, docName, verName);
        } else
        {
            message = String.format("[INFO] Workspace: %s", workspace);
        }
        setText(message);
    }

    @Override
    public void updateDocuentRenamed(String oldDocName, String newDocName)
    {
        if (isDisplaying(oldDocName))
        {
            docName = newDocName;

        }
    }
}
