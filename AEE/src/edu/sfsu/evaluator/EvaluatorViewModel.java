/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator;

import edu.sfsu.evaluator.exceptions.BadModelRequestException;
import edu.sfsu.evaluator.model.Entity;
import edu.sfsu.evaluator.model.ComplexEntity;
import edu.sfsu.evaluator.model.ComplexEntityRule;
import edu.sfsu.evaluator.view.EvaluatorView;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class which allows views to get information about the model, and in turn
 * allows the model to update the view.
 * @author Eric Chiang
 */
public class EvaluatorViewModel
{

    public final static String MODEL_NOT_SET = "No workspace declared!";
    private ArrayList<EvaluatorView> views;
    private EvaluatorModel model;
    private boolean isModelSet = false;
    /*
     * Highlight information. Easier to store which entities aren't highlighted
     * than the ones that are. This way when new entities are created, they're
     * automatically be considered highlighted.
     */
    private ArrayList<String> unHighlightedEntityTypes;
    private ArrayList<String> unHighlightedEntityRules;

    /**
     * Initiate view model
     */
    public EvaluatorViewModel()
    {
        views = new ArrayList();
        unHighlightedEntityTypes = new ArrayList();
        unHighlightedEntityRules = new ArrayList();
    }

    /**
     * Attach a view. This will allow the ViewModel to update that view.
     * @param view
     */
    public void attachView(EvaluatorView view)
    {
        views.add(view);
    }

    /**
     * Does the model contain a specific document?
     * @param docName
     * @return
     */
    public boolean containsDocument(String docName)
    {
        return model.containsDocument(docName);
    }

    /**
     * Does the model contain a specific document version?
     * @param docName
     * @param verName
     * @return
     */
    public boolean containsDocumentVersion(
            String docName,
            String verName)
    {
        return model.containsDocumentVersion(docName, verName);
    }

    /**
     * Detach a view.
     * @param view
     */
    public void detachView(EvaluatorView view)
    {
        views.remove(view);
    }

    /**
     * Called by the model to notify view that a document has been renamed. This
     * is necessary for View element which display information relevant to a
     * specific document. For example the Entity tree. Those View elements need
     * to know what docName to use to request new information when repainting.
     * <p/>
     * @param oldDocName
     * @param newDocName
     */
    public void fireDocumentRenamed(
            String oldDocName,
            String newDocName)
    {
        for (EvaluatorView view : views)
        {
            view.updateDocuentRenamed(oldDocName, newDocName);
        }
    }

    /**
     * Called by the model to notify view that a document version has been
     * renamed. This is necessary for View element which display information
     * relevant to a specific document version. For example the Entity tree.
     * Those View elements need to know what verName to use to request new '
     * information when repainting.
     * <p/>
     * @param docName
     * @param oldVerName
     * @param newVerName
     */
    public void fireDocumentVersionRenamed(
            String docName,
            String oldVerName,
            String newVerName)
    {
        for (EvaluatorView view
                : views)
        {
            view.updateDocumentVersionRenamed(docName, oldVerName, newVerName);
        }
    }

    /**
     * Model getters. For view to request information from model.
     */
    public ArrayList<Entity> getEntities(
            String docName,
            String verName)
            throws BadModelRequestException
    {
        return model.getEntities(docName, verName);
    }

    public ArrayList<String> getAvailableDocuments()
    {
        return model.getAvailableDocuments();
    }

    public ArrayList<String> getAvailableDocumentVersions(
            String docName)
            throws BadModelRequestException
    {
        return model.getAvailableDocumenVersions(docName);
    }

    public ArrayList<ComplexEntityRule> getComplexEntityRules()
    {
        return model.getComplexEntityRules();
    }

    public ArrayList<ComplexEntity> getComplexEntities(String docName,
                                                       String verName) throws
            BadModelRequestException
    {
        return model.getComplexEntities(docName, verName);
    }

    public String getDocumentText(
            String docName)
            throws BadModelRequestException
    {
        return model.getDocumentText(docName);
    }

    public HashMap<String, Color> getEntityTypes()
    {
        return model.getEntityTypes();
    }

    public String getWorkspacePath()
    {
        return model.getWorkspacePath();
    }
    /**
     * End of model getters.
     */

    /**
     * Determine highlight state of a complex entity type.
     * @param complexEntityType
     * @return
     */
    public boolean isComplexEntityTypeHighlighted(String complexEntityType)
    {
        return !unHighlightedEntityRules.contains(complexEntityType);
    }

    /**
     * Determine highlight state of entity type.
     * @param entityType
     * @return
     */
    public boolean isEntityTypeHighlighted(String entityType)
    {
        return !unHighlightedEntityTypes.contains(entityType);
    }

    public boolean isModelSet()
    {
        return isModelSet;
    }

    /**
     * Repaint all view elements. This will cause them to re-request data from
     * the model and change to reflect any changes since the last repaint.
     */
    public void repaintView()
    {
        for (EvaluatorView view : views)
        {
            view.repaintView();
        }
    }

    /**
     * Called by the controller to set the EvaluatorModel for which this
     * class communicates with.
     * @param model
     */
    public void setModel(EvaluatorModel model)
    {
        this.model = model;
        isModelSet = true;
        unHighlightedEntityTypes.clear();
        unHighlightedEntityRules.clear();
        for (EvaluatorView view : views)
        {
            view.updateModelSet();
        }
    }

    /**
     * Set the highlight policy of a specific entity type. That information is
     * stored in the class, and is accessed by the View elements through
     * <code>isEntityTypeHighlighted()</code> when they are repainted.
     * @param entityType
     * @param highlighted
     */
    public void setEntityTypeHighlight(String entityType, boolean highlighted)
    {
        if (highlighted)
        {
            unHighlightedEntityTypes.remove(entityType);
        } else
        {
            unHighlightedEntityTypes.add(entityType);
        }
    }

    /**
     * Set the highlight policy of a specific complex entity type. That
     * information is stored in the class, and is accessed by the View elements
     * through <code>isComplexEntityTypeHighlighted()</code> when they are
     * repainted.
     * @param complexEntityType
     * @param highlighted
     */
    public void setComplexEntityRuleHighlighted(String complexEntityType,
                                                boolean highlighted)
    {
        if (highlighted)
        {
            unHighlightedEntityRules.remove(complexEntityType);
        } else
        {
            unHighlightedEntityRules.add(complexEntityType);
        }
    }
}
