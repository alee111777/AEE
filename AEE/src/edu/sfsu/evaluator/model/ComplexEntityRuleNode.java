/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.model;

/**
 *
 * @author eric
 */
public class ComplexEntityRuleNode implements java.io.Serializable
{

    private String entityType;
    private boolean optional;

    public ComplexEntityRuleNode(String entityType, boolean optional)
    {
        this.entityType = entityType;
        this.optional = optional;
    }

    public String getAnnotationType()
    {
        return entityType;
    }

    public boolean isOptional()
    {
        return optional;
    }
}
