/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator;

import edu.sfsu.evaluator.exceptions.BadModelRequestException;
import edu.sfsu.evaluator.model.AnnotatedDocument;
import edu.sfsu.evaluator.model.Entity;
import edu.sfsu.evaluator.model.ComplexEntity;
import edu.sfsu.evaluator.model.ComplexEntityRule;
import edu.sfsu.io.Serializer;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The evaluator model contains all information in the dataspace. This includes
 * entities, documents, document versions, etc.
 * <p/>
 * @author eric
 */
public class EvaluatorModel
{

    private EvaluatorViewModel viewModel;

    // Links documents with document name
    private HashMap<String, AnnotatedDocument> documents;
    // Stores entity type names and colors
    private HashMap<String, Color> entityTypes;
    private ArrayList<ComplexEntityRule> complexEntityRules;
    private String workspacePath;

    // Relative path of all serializable elements;
    private final String DOCUMENTS_SER = "documents.ser";
    private final String ENTITY_TYPE_SER = "entity_type.ser";
    private final String ENTITIES_SER = "entities.ser";

    /**
     * @param viewModel
     * @param workspacePath
     * @throws FileNotFoundException
     */
    public EvaluatorModel(EvaluatorViewModel viewModel, String workspacePath)
            throws FileNotFoundException
    {
        init(viewModel, workspacePath);
    }

    /**
     * Attempt to retrieve serialized data.
     * @param viewModel
     * @param workspacePath
     * @throws FileNotFoundException
     */
    private void init(EvaluatorViewModel viewModel, String workspacePath)
            throws FileNotFoundException
    {
        this.workspacePath = workspacePath;
        this.viewModel = viewModel;
        if (!(new File(workspacePath).isDirectory()))
        {
            throw new FileNotFoundException();
        }

        // Attempt to retrieve documents
        try
        {
            String docSerPath = mergePath(workspacePath, DOCUMENTS_SER);
            if (!(new File(docSerPath).isFile()))
            {
                throw new FileNotFoundException();
            }
            Object object = edu.sfsu.io.Deserializer.deserialize(docSerPath);
            if (!(object instanceof HashMap))
            {
                throw new ClassNotFoundException();
            }
            documents = (HashMap) object;
        } catch (Exception e)
        {
            // Do nothing
        }
        if (documents == null)
        {
            documents = new HashMap();
        }

        // Attempt to retrieve entity types
        try
        {
            String entityTypeSerPath =
                    mergePath(workspacePath, ENTITY_TYPE_SER);
            if (!(new File(entityTypeSerPath).isFile()))
            {
                throw new FileNotFoundException();
            }
            Object object =
                    edu.sfsu.io.Deserializer.deserialize(entityTypeSerPath);
            if (!(object instanceof HashMap))
            {
                throw new ClassNotFoundException();
            }
            entityTypes = (HashMap) object;
        } catch (Exception e)
        {
            // Do nothing
        }
        if (entityTypes == null)
        {
            entityTypes = new HashMap();
        }

        // Attempt to retrieve complex entity rules.
        try
        {
            String entitiesSerPath = mergePath(workspacePath, ENTITIES_SER);
            if (!(new File(entitiesSerPath).isFile()))
            {
                throw new FileNotFoundException();
            }
            Object object =
                    edu.sfsu.io.Deserializer.deserialize(entitiesSerPath);
            if (!(object instanceof ArrayList))
            {
                throw new ClassNotFoundException();
            }
            complexEntityRules = (ArrayList) object;
        } catch (Exception e)
        {
            // Do nothing
        }
        if (complexEntityRules == null)
        {
            complexEntityRules = new ArrayList();
            documents = new HashMap();
        }
    }

    /**
     * Check is document either exists or not. For internal use as entity quick way
     * to throw an exception when entity document does exist when it shouldn't or
     * vice versa.
     * <p/>
     * @param docName
     * @param shouldExist
     * @throws Exception
     */
    private void checkDocument(String docName, boolean shouldExist)
            throws BadModelRequestException
    {
        if (containsDocument(docName) != shouldExist)
        {
            throw new BadModelRequestException(
                    String.format("Bad document name: %s\n", docName));
        }
    }

    /**
     * Checks if document version either exists or not. Same as above method for
     * document versions.
     * <p/>
     * @param docName
     * @param verName
     * @param shouldExist
     * @throws BadModelRequestException
     */
    private void checkDocumentVersion(String docName, String verName,
                                      boolean shouldExist)
            throws BadModelRequestException
    {
        if (containsDocumentVersion(docName, verName) != shouldExist)
        {
            throw new BadModelRequestException(
                    String.format("Bad document name: %s -- %s\n", docName,
                                  verName));
        }
    }

    /**
     * Internal method to merge two paths.
     * <p/>
     * Examples:
     * <p/>
     * <t/>'usr/bin' + '/java' = '/usr/bin/java'
     * <p/>
     * <t/>'usr/bin/' + 'java' = '/usr/bin/java'
     * <p/>
     * @param path1
     * @param path2
     * @return
     */
    private String mergePath(String path1, String path2)
    {
        if (path1.length() <= 0)
        {
            return path2;
        }
        String fileSepKey = "file.separator";
        String fileSeparator = System.getProperty(fileSepKey);
        if (fileSeparator == null || fileSeparator.length() == 0)
        {
            System.err.printf("Bad system property: '%s'\n", fileSepKey);
            return null;
        }
        // if the last char is the file separator
        if (path1.lastIndexOf(fileSeparator)
                == path1.length() - fileSeparator.length())
        {
            return path1 + path2;
        } else
        {
            return path1 + fileSeparator + path2;
        }
    }

    /**
     * Does this model contain entity document named docName?
     * <p/>
     * @param docName
     * @return
     */
    public boolean containsDocument(String docName)
    {
        return documents.containsKey(docName);
    }

    /**
     * Does this model contain entity document named docName, with entity version named
     * verName?
     * <p/>
     * @param docName
     * @param verName
     * @return
     */
    public boolean containsDocumentVersion(String docName, String verName)
    {
        if (containsDocument(docName))
        {
            return documents.get(docName)
                    .getAnnotationVersionsNames().contains(verName);
        }
        return false;
    }

    /**
     * Get entities of entity  for entity specific document.
     * <p/>
     * @param docName
     * @param verName
     * @return
     * @throws Exception
     */
    public ArrayList<Entity> getEntities(String docName, String verName)
            throws BadModelRequestException
    {
        checkDocumentVersion(docName, verName, true);
        return documents.get(docName)
                .getAnnotationVersion(verName).getEntities();
    }

    /**
     * Get names of all available documents
     * <p/>
     * @return
     */
    public ArrayList<String> getAvailableDocuments()
    {
        return new ArrayList(documents.keySet());
    }

    /**
     * Get names of all available versions of entity specific document version
     * <p/>
     * @param docName
     * @return
     * @throws Exception
     */
    public ArrayList<String> getAvailableDocumenVersions(String docName)
            throws BadModelRequestException
    {
        checkDocument(docName, true);
        return documents.get(docName).getAnnotationVersionsNames();
    }

    /**
     * Get the rule structure of allowed complex entityTypes.
     * <p/>
     * @return
     */
    public ArrayList<ComplexEntityRule> getComplexEntityRules()
    {
        return new ArrayList(complexEntityRules);
    }

    /**
     * Get the complex entities of entity specific document version.
     * @param docName
     * @param verName
     * @return
     * @throws BadModelRequestException
     */
    public ArrayList<ComplexEntity> getComplexEntities(
            String docName,
            String verName) throws BadModelRequestException
    {
        checkDocumentVersion(docName, verName, true);
        return documents.get(docName).getAnnotationVersion(verName).
                getComplexEntities();
    }

    /**
     * Get the text of entity specific document.
     * <p/>
     * @param docName
     * @return
     * @throws Exception
     */
    public String getDocumentText(String docName) throws
            BadModelRequestException
    {
        checkDocument(docName, true);
        return documents.get(docName).getDocumentText();
    }

    /**
     * Get all entityTypes for this workspacePath.
     * <p/>
     * @return
     */
    public HashMap<String, Color> getEntityTypes()
    {
        return new HashMap(entityTypes);
    }

    /**
     * Get path of workspacePath
     * <p/>
     * @return
     */
    public String getWorkspacePath()
    {
        return workspacePath;
    }

    /**
     * Request to add entity new entity.
     * <p/>
     * @param docName
     * @param verName
     * @param entity
     * @throws Exception
     */
    public void requestEntityAdd(
            String docName,
            String verName,
            Entity entity) throws BadModelRequestException
    {
        checkDocumentVersion(docName, verName, true);
        documents.get(docName)
                .getAnnotationVersion(verName).addEntity(entity);
    }

    /**
     * Request to delete an entity
     * <p/>
     * @param docName
     * @param verName
     * @param entity
     * @throws Exception
     */
    public void requestEntityDelete(
            String docName,
            String verName,
            Entity entity) throws BadModelRequestException
    {
        checkDocumentVersion(docName, verName, true);
        documents.get(docName)
                .getAnnotationVersion(verName).removeEntity(entity);
        ArrayList<ComplexEntity> complexEntities =
                documents.get(docName).getAnnotationVersion(verName)
                .getComplexEntities();
        for (ComplexEntity complexEntity
                : complexEntities)
        {
            if (complexEntity.getEntities().contains(entity))
            {
                requestComplexEntityDelete(docName, verName, complexEntity);
            }
        }
    }

    /**
     * Request to add entity new complex entity.
     * @param docName
     * @param verName
     * @param complexEntity
     * @throws BadModelRequestException
     */
    public void requestComplexEntityAdd(
            String docName,
            String verName,
            ComplexEntity complexEntity) throws BadModelRequestException
    {
        checkDocumentVersion(docName, verName, true);
        documents.get(docName)
                .getAnnotationVersion(verName).addComplexEntity(complexEntity);
    }

    /**
     * Request to delete entity complex entity.
     * @param docName
     * @param verName
     * @param complexEntity
     * @throws BadModelRequestException
     */
    public void requestComplexEntityDelete(
            String docName,
            String verName,
            ComplexEntity complexEntity) throws BadModelRequestException
    {
        checkDocumentVersion(docName, verName, true);
        documents.get(docName)
                .getAnnotationVersion(verName).
                removeComplexEntity(complexEntity);
    }

    /**
     * Request to add entity new complex entity rule.
     * @param complexEntityRule
     * @throws BadModelRequestException
     */
    public void requestComplexEntityRuleAdd(
            ComplexEntityRule complexEntityRule) throws BadModelRequestException
    {
        String entityName = complexEntityRule.getEntityRuleName();
        for (ComplexEntityRule rule
                : complexEntityRules)
        {
            if (rule.getEntityRuleName().compareTo(entityName) == 0)
            {
                throw new BadModelRequestException(
                        String.format(
                        "Complex entity rule '%s' already contained.",
                        entityName));
            }
        }

        complexEntityRules.add(complexEntityRule);
    }

    /**
     * Request to change the color of entity complex entity rule.
     * @param complexEntityRuleName
     * @param color
     * @throws BadModelRequestException
     */
    public void requestComplexEntityRuleColorChanged(
            String complexEntityRuleName, Color color) throws
            BadModelRequestException
    {
        boolean contained = false;
        for (ComplexEntityRule rule
                : complexEntityRules)
        {
            if (rule.getEntityRuleName().compareTo(complexEntityRuleName) == 0)
            {
                rule.setColor(color);
                contained = true;
                break;
            }
        }
        if (!contained)
        {
            throw new BadModelRequestException(
                    String.format("Complex entity rule '%s' not contained.",
                                  complexEntityRuleName));
        }
    }

    /**
     * Request to delete entity complex entity rule by name.
     * @param complexEntityRuleName
     * @throws BadModelRequestException
     */
    public void requestComplexEntityRuleDelete(
            String complexEntityRuleName) throws BadModelRequestException
    {
        boolean containsRule = false;
        for (ComplexEntityRule rule
                : complexEntityRules)
        {
            if (rule.getEntityRuleName().compareTo(complexEntityRuleName) == 0)
            {
                complexEntityRules.remove(rule);
                containsRule = true;
                break;
            }
        }
        if (!containsRule)
        {
            throw new BadModelRequestException(
                    String.format("Complex entity rule '%s' not contained.",
                                  complexEntityRuleName));
        }
        // Remove all associated entityTypes
        for (String docName
                : documents.keySet())
        {
            for (String verName
                    : documents.get(docName).getAnnotationVersionsNames())
            {
                for (ComplexEntity c
                        : documents.get(docName).getAnnotationVersion(verName).
                        getComplexEntities())
                {
                    if (c.getEntityType().compareTo(complexEntityRuleName) == 0)
                    {
                        requestComplexEntityDelete(docName, verName, c);
                    }
                }
            }
        }
    }

    /**
     * Request to add entity document.
     * @param docName
     * @param docText
     * @throws BadModelRequestException
     */
    public void requestDocumentAdd(
            String docName,
            String docText)
            throws BadModelRequestException
    {
        checkDocument(docName, false);
        documents.put(docName, new AnnotatedDocument(docText));
    }

    /**
     * Request to delete entity document.
     * @param docName
     * @throws BadModelRequestException
     */
    public void requestDocumentDelete(
            String docName)
            throws BadModelRequestException
    {
        checkDocument(docName, true);
        documents.remove(docName);
    }

    public void requestDocumentRename(
            String oldDocName,
            String newDocName)
            throws BadModelRequestException
    {
        checkDocument(oldDocName, true);
        checkDocument(newDocName, false);
        documents.put(newDocName, documents.remove(oldDocName));
        viewModel.fireDocuentRenamed(oldDocName, newDocName);
    }

    public void requestDocumentVersionAdd(
            String docName,
            String verName)
            throws BadModelRequestException
    {
        checkDocument(docName, true);
        checkDocumentVersion(docName, verName, false);
        documents.get(docName).createAnnotationVersion(verName);
    }

    public void requestDocumentVersionDelete(
            String docName,
            String verName)
            throws BadModelRequestException
    {
        checkDocumentVersion(docName, verName, true);
        documents.get(docName).removeAnnotationVersion(verName);
    }

    public void requestDocumentVersionRename(
            String docName,
            String oldVerName,
            String newVerName)
            throws BadModelRequestException
    {
        checkDocumentVersion(docName, oldVerName, true);
        checkDocumentVersion(docName, newVerName, false);
        documents.get(docName).renameAnnotationVersion(oldVerName, newVerName);
        viewModel.fireDocumentVersionRenamed(docName, oldVerName, newVerName);
    }

    public void requestEntityTypeAdd(
            String entityType,
            Color color)
            throws BadModelRequestException
    {
        if (entityTypes.containsKey(entityType))
        {
            throw new BadModelRequestException(
                    String.format("Already a label named %s", entityType));
        }
        entityTypes.put(entityType, color);
    }

    public void requestEntityTypeColorChange(
            String entityType,
            Color newColor)
            throws BadModelRequestException
    {
        if (!entityTypes.containsKey(entityType))
        {
            throw new BadModelRequestException(
                    String.format("Entity type not contained: %s", entityType));
        }
        entityTypes.put(entityType, newColor);
    }

    public void requestEntityTypeDelete(
            String entityType)
            throws BadModelRequestException
    {
        if (!entityTypes.containsKey(entityType))
        {
            throw new BadModelRequestException(String.format(
                    "Entity type not contained: %s", entityType));
        }
        // Remove all associated entityTypes
        for (String docName
                : documents.keySet())
        {
            for (String verName
                    : documents.get(docName).getAnnotationVersionsNames())
            {
                for (Entity a
                        : documents.get(docName).getAnnotationVersion(verName).getEntities())
                {
                    if (a.getEntityType().compareTo(entityType) == 0)
                    {
                        requestEntityDelete(docName, verName, a);
                    }
                }
            }
        }
        entityTypes.remove(entityType);
    }

    public void requestEntityTypeRename(
            String oldName,
            String newName)
            throws BadModelRequestException
    {
        if (!entityTypes.containsKey(oldName))
        {
            throw new BadModelRequestException(
                    String.format("Label not contained: %s", oldName));
        }
        if (entityTypes.containsKey(newName))
        {
            throw new BadModelRequestException(
                    String.format("Label already contained: %s", newName));
        }

        // Rename all associated entityTypes
        for (String docName
                : documents.keySet())
        {
            for (String verName
                    : documents.get(docName).getAnnotationVersionsNames())
            {
                for (Entity entity
                        : documents.get(docName).getAnnotationVersion(verName).getEntities())
                {
                    if (entity.getEntityType().compareTo(oldName) == 0)
                    {
                        Entity renamedAnnotation = new Entity(entity.getText(),
                                                              newName,
                                                              entity.getStart(), entity.
                                getEnd());
                        documents.get(docName).
                                getAnnotationVersion(verName).removeEntity(entity);
                        documents.get(docName).getAnnotationVersion(verName).addEntity(renamedAnnotation);
                    }
                }
            }
        }
        entityTypes.put(newName, entityTypes.remove(oldName));
    }

    public void requestSaveState() throws Exception
    {
        try
        {
            Serializer.serialize(mergePath(workspacePath, DOCUMENTS_SER), documents);
        } catch (Exception e)
        {
            System.err.println("Could not save documents");
        }

        try
        {
            Serializer.serialize(mergePath(workspacePath, ENTITY_TYPE_SER), entityTypes);
        } catch (Exception e)
        {
            System.err.println("Could not save labels");
        }

        try
        {
            Serializer.serialize(mergePath(workspacePath, ENTITIES_SER),
                                 complexEntityRules);
        } catch (Exception e)
        {
            System.err.println("Could not save entity rules");
        }
    }
}
