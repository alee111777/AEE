/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.model;

/**
 * Complex Entity Rule Node is used to construct complex entity rules. Each
 * node contains an entity type and if that entity type is optional.
 * @author Eric Chiang
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
