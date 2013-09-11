/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author eric
 */
public class ComplexEntityRule implements java.io.Serializable,
                                          Comparable<ComplexEntityRule>
{

    private String name;
    private ArrayList<ComplexEntityRuleNode> nodes;
    private Color color;

    public ComplexEntityRule(String name,
                             ArrayList<ComplexEntityRuleNode> nodes, Color color)
    {
        this.name = name;
        this.nodes = new ArrayList(nodes);
        this.color = color;
    }

    public Color getColor()
    {
        return color;
    }

    public String getEntityRuleName()
    {
        return name;
    }

    public ArrayList<ComplexEntityRuleNode> getNodes()
    {
        return new ArrayList(nodes);
    }

    public void setColor(Color color)
    {
        this.color = color;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof ComplexEntityRule))
        {
            return false;
        }
        ComplexEntityRule cer = (ComplexEntityRule) o;
        return name.compareTo(cer.getEntityRuleName()) == 0;
    }

    @Override
    public int compareTo(ComplexEntityRule o)
    {
        return name.compareTo(o.getEntityRuleName());
    }
    public static Comparator<ComplexEntityRule> ComplexEntityRuleComparator = new Comparator<ComplexEntityRule>()
    {
        @Override
        public int compare(ComplexEntityRule o1, ComplexEntityRule o2)
        {
            return o1.compareTo(o2);
        }
    };
}
