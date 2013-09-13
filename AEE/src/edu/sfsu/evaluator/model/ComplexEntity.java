/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.model;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * A complex entity contains a complex entity type and list of sub entities
 * (order matters).
 * @author Eric Chiang
 */
public class ComplexEntity implements java.io.Serializable,
                                      Comparable<ComplexEntity>
{

    private String complexEntityType;
    private ArrayList<Entity> subEntities;

    public ComplexEntity(String type, ArrayList<Entity> subEntities)
    {
        this.complexEntityType = type;
        this.subEntities = new ArrayList(subEntities);
    }

    /**
     * Get sub entities.
     * @return
     */
    public ArrayList<Entity> getSubEntities()
    {
        // Create a new ArrayList which contains the subEntities.
        return new ArrayList(subEntities);
    }

    /**
     * Get complex entity type.
     * @return
     */
    public String getComplexEntityType()
    {
        return complexEntityType;
    }

    @Override
    public String toString()
    {
        String string = complexEntityType;
        for (Entity a : subEntities)
        {
            string += '\n' + a.toString();
        }
        return string;
    }

    /**
     * Complex entities are equivalent if their data fields are equal.
     * Essentially tests for weak equivalence.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof ComplexEntity))
        {
            return false;
        }
        ComplexEntity r = (ComplexEntity) o;
        // Check if complex entity type is the same.
        if (r.getComplexEntityType().compareTo(complexEntityType) != 0)
        {
            return false;
        }

        ArrayList<Entity> rAnnos = r.getSubEntities();
        // Check if sub entities match up
        if (rAnnos.size() != subEntities.size())
        {
            return false;
        }
        for (int i = 0; i < subEntities.size(); i++)
        {
            if (!rAnnos.get(i).equals(subEntities.get(i)))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * compareTo only compares the subEntities of both complex entities.
     * @param o
     * @return
     */
    @Override
    public int compareTo(ComplexEntity o)
    {
        ArrayList<Entity> oSubEntities = o.getSubEntities();
        for (int i = 0; i < subEntities.size(); i++)
        {
            // If this complex entity has more nodes than the other return 1
            if (i >= oSubEntities.size())
            {
                return 1;
            }
            // Compare each sub entity util a difference is found.
            Entity a1 = subEntities.get(i);
            Entity a2 = oSubEntities.get(i);
            int result = a1.compareTo(a2);
            if (result != 0)
            {
                return result;
            }
        }
        // All sub entities have been the same up until this point
        if (subEntities.size() == oSubEntities.size())
        {
            return complexEntityType.compareTo(o.complexEntityType);
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
