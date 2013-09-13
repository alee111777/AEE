/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Rule for creating a complex entity. Each node contains an entity type and
 * a boolean value for if that entity is optional. And example list of ruleNodes
 * would be:
 * <p/>
 * "first complexEntityType" not optional<p/>
 * "middle complexEntityType" optional<p/>
 * "last complexEntityType" not optional<p/>
 * <p/>
 * For this case the complexEntityType would be something like "name".
 * <p/>
 * @author Eric Chiang
 */
public class ComplexEntityRule implements java.io.Serializable,
                                          Comparable<ComplexEntityRule>
{

    private String complexEntityType;
    private ArrayList<ComplexEntityRuleNode> ruleNodes;
    private Color color;

    public ComplexEntityRule(String complexEntityType,
                             ArrayList<ComplexEntityRuleNode> ruleNodes,
                             Color color)
    {
        this.complexEntityType = complexEntityType;
        this.ruleNodes = new ArrayList(ruleNodes);
        this.color = color;
    }

    public Color getColor()
    {
        return color;
    }

    public String getComplexEntityType()
    {
        return complexEntityType;
    }

    public ArrayList<ComplexEntityRuleNode> getRuleNodes()
    {
        return new ArrayList(ruleNodes);
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof ComplexEntityRule))
        {
            return false;
        }
        ComplexEntityRule cer = (ComplexEntityRule) o;
        return complexEntityType.compareTo(cer.getComplexEntityType()) == 0;
    }

    @Override
    public int compareTo(ComplexEntityRule o)
    {
        return complexEntityType.compareTo(o.getComplexEntityType());
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
