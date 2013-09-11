/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.model;

import java.util.Comparator;

/**
 *
 * @author Eric Chiang
 */
public class Entity implements java.io.Serializable,
                               Comparable<Entity>
{

    private String text, entityType;
    private int start, end;

    public Entity(String text, String label, int start, int end)
    {
        this.text = text;
        this.entityType = label;
        this.start = start;
        this.end = end;
    }

    /**
     * @return the text
     */
    public String getText()
    {
        return text;
    }

    /**
     * @return the entityType
     */
    public String getEntityType()
    {
        return entityType;
    }

    /**
     * @return the start
     */
    public int getStart()
    {
        return start;
    }

    /**
     * @return the end
     */
    public int getEnd()
    {
        return end;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Entity))
        {
            return false;
        }
        Entity annotation = (Entity) obj;
        if (annotation.getEnd() != this.end)
        {
            return false;
        }
        if (annotation.getStart() != this.start)
        {
            return false;
        }
        if (annotation.getEntityType().compareTo(this.entityType) != 0)
        {
            return false;
        }
        if (annotation.getText().compareTo(this.text) != 0)
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 29 * hash + (this.text != null ? this.text.hashCode() : 0);
        hash = 29 * hash + (this.entityType != null ? this.entityType.hashCode() : 0);
        hash = 29 * hash + this.start;
        hash = 29 * hash + this.end;
        return hash;
    }

    @Override
    public int compareTo(Entity o)
    {
        int oStart = o.getStart();
        int oEnd = o.getEnd();

        // ascending order
        int diff = this.start - oStart;
        if (diff == 0)
        {
            return this.end - oEnd;
        } else
        {
            return diff;
        }

        // descending order
        /*
         int diff = oStart - this.start;
         if(diff == 0){
         return oEnd - this.end;
         } else {
         return diff;
         }

         */
    }
    public static Comparator<Entity> AnnotationComparator = new Comparator<Entity>()
    {
        @Override
        public int compare(Entity o1, Entity o2)
        {
            return o1.compareTo(o2);
        }
    };

    @Override
    public String toString()
    {
        return String.format("%s / %s / %d / %d\n", text, entityType, start, end);
    }
}
