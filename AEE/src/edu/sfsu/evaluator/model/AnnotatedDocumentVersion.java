/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.model;

import java.util.ArrayList;

/**
 * Entities and complex entities associated with a document version.
 * @author Eric Chiang
 */
public class AnnotatedDocumentVersion implements java.io.Serializable
{

    private ArrayList<Entity> entities;
    private ArrayList<ComplexEntity> complexEntities;

    public AnnotatedDocumentVersion()
    {
        entities = new ArrayList();
        complexEntities = new ArrayList();
    }

    /**
     * Add entity.
     * @param entity
     * @return
     */
    public boolean addEntity(Entity entity)
    {
        entities.add(entity);
        return true;
    }

    /**
     * Add complex entity.
     * @param complexEntity
     * @return
     */
    public boolean addComplexEntity(ComplexEntity complexEntity)
    {
        complexEntities.add(complexEntity);
        return true;
    }

    /**
     * Get all entities.
     * @return
     */
    public ArrayList<Entity> getEntities()
    {
        return new ArrayList(entities);
    }

    /**
     * Get all complex entities.
     * @return
     */
    public ArrayList<ComplexEntity> getComplexEntities()
    {
        return new ArrayList(complexEntities);
    }

    /**
     * Remove entity by object. Return true if successful.
     * @param entity
     * @return
     */
    public boolean removeEntity(Entity entity)
    {
        for (Entity a : entities)
        {
            if (a.equals(entity))
            {
                entities.remove(a);
                return true;
            }
        }
        return false;
    }

    /**
     * Remove complex entity by object. Return true if successful.
     * @param complexEntity
     * @return
     */
    public boolean removeComplexEntity(ComplexEntity complexEntity)
    {
        for (ComplexEntity r : complexEntities)
        {
            if (r.equals(complexEntity))
            {
                complexEntities.remove(r);
                return true;
            }
        }
        return false;
    }
}
