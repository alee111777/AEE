/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.model;

import java.util.ArrayList;

/**
 *
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

    public boolean addEntity(Entity entity)
    {
        entities.add(entity);
        return true;
    }

    public boolean addComplexEntity(ComplexEntity complexEntity)
    {
        complexEntities.add(complexEntity);
        return true;
    }

    public ArrayList<Entity> getEntities()
    {
        return new ArrayList(entities);
    }

    public ArrayList<ComplexEntity> getComplexEntities()
    {
        return new ArrayList(complexEntities);
    }

    public boolean removeEntity(Entity entity)
    {
        for (Entity a
                : entities)
        {
            if (a.equals(entity))
            {
                entities.remove(a);
                return true;
            }
        }
        return false;
    }

    public boolean removeComplexEntity(ComplexEntity complexEntity)
    {
        for (ComplexEntity r
                : complexEntities)
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
