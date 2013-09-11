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
 *
 * @author eric
 */
public class EvaluatorViewModel
{

    public final static String NO_MODEL_LOADED = "No workspace declared!";
    private ArrayList<EvaluatorView> views;
    private EvaluatorModel model;
    private boolean modelLoaded = false;
    private ArrayList<String> unHighlightedEntityTypes;
    private ArrayList<String> unHighlightedEntityRules;

    public EvaluatorViewModel()
    {
        views = new ArrayList();
        unHighlightedEntityTypes = new ArrayList();
        unHighlightedEntityRules = new ArrayList();
    }

    public EvaluatorViewModel(EvaluatorModel model)
    {
        views = new ArrayList();
        unHighlightedEntityTypes = new ArrayList();
        unHighlightedEntityRules = new ArrayList();
        this.model = model;
        modelLoaded = true;
    }

    public void attachView(EvaluatorView view)
    {
        views.add(view);
    }

    public boolean containsDocument(
            String docName)
    {
        return model.containsDocument(docName);
    }

    public boolean containsDocumentVersion(
            String docName,
            String verName)
    {
        return model.containsDocumentVersion(docName, verName);
    }

    public void detachView(EvaluatorView view)
    {
        views.remove(view);
    }

    /**
     * Notify view that a document has been renamed.
     * <p/>
     * @param oldDocName
     * @param newDocName
     */
    public void fireDocuentRenamed(
            String oldDocName,
            String newDocName)
    {
        for (EvaluatorView view
                : views)
        {
            view.updateDocuentRenamed(oldDocName, newDocName);
        }
    }

    /**
     * Notify view that a document version has been renamed.
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
     * Model getters. View may request information from model.
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

    public HashMap<String, Color> getLabels()
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
     * Determine highlight state of a complex entity rule.
     * @param complexEntityRuleName
     * @return
     */
    public boolean isComplexEntityRuleHighlighted(String complexEntityRuleName)
    {
        return !unHighlightedEntityRules.contains(complexEntityRuleName);
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

    public boolean isModelLoaded()
    {
        return modelLoaded;
    }

    public void repaintView()
    {
        for (EvaluatorView view
                : views)
        {
            view.repaintView();
        }
    }

    public void setModel(EvaluatorModel model)
    {
        this.model = model;
        modelLoaded = true;
        unHighlightedEntityTypes.clear();
        unHighlightedEntityRules.clear();
        for (EvaluatorView view
                : views)
        {
            view.updateModelSet();
        }
    }

    public void setEntityTypeHighlight(String label, boolean highlighted)
    {
        if (highlighted)
        {
            unHighlightedEntityTypes.remove(label);
        } else
        {
            unHighlightedEntityTypes.add(label);
        }
    }

    public void setComplexEntityRuleHighlighted(String entityName,
                                                boolean highlighted)
    {
        if (highlighted)
        {
            unHighlightedEntityRules.remove(entityName);
        } else
        {
            unHighlightedEntityRules.add(entityName);
        }
    }
}
