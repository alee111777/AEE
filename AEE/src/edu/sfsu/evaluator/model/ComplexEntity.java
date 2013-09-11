/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.model;

import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author eric
 */
public class ComplexEntity implements java.io.Serializable,
                                      Comparable<ComplexEntity>
{

    private String complexEntityRuleType;
    private ArrayList<Entity> entities;

    public ComplexEntity(String type, ArrayList<Entity> entities)
    {
        this.complexEntityRuleType = type;
        this.entities = new ArrayList(entities);
    }

    public ArrayList<Entity> getEntities()
    {
        return new ArrayList(entities);
    }

    public String getEntityType()
    {
        return complexEntityRuleType;
    }

    @Override
    public String toString()
    {
        String string = complexEntityRuleType;
        for (Entity a
                : entities)
        {
            string += '\n' + a.toString();
        }
        return string;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof ComplexEntity))
        {
            return false;
        }
        ComplexEntity r = (ComplexEntity) o;
        if (r.getEntityType().compareTo(complexEntityRuleType) != 0)
        {
            return false;
        }
        ArrayList<Entity> rAnnos = r.getEntities();
        if (rAnnos.size() != entities.size())
        {
            return false;
        }
        for (int i = 0;
                i < entities.size();
                i++)
        {
            if (!rAnnos.get(i).equals(entities.get(i)))
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public int compareTo(ComplexEntity o)
    {
        ArrayList<Entity> oAnnos = o.getEntities();
        for (int i = 0;
                i < entities.size();
                i++)
        {
            if (i >= oAnnos.size())
            {
                return 1;
            }
            Entity a1 = entities.get(i);
            Entity a2 = oAnnos.get(i);
            int result = a1.getStart() - a2.getStart();
            if (result == 0)
            {
                result = a1.getEnd() - a2.getEnd();
            }
            if (result != 0)
            {
                return result;
            }
        }
        return -1;
    }
    public static Comparator<ComplexEntity> ComplexEntityComparator = new Comparator<ComplexEntity>()
    {
        @Override
        public int compare(ComplexEntity o1, ComplexEntity o2)
        {
            return o1.compareTo(o2);
        }
    };
}
