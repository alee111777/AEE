/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.exceptions;

/**
 *
 * @author eric
 */
public class BadModelRequestException extends Exception
{

    public BadModelRequestException()
    {
        super();
    }

    public BadModelRequestException(String message)
    {
        super(message);
    }

    public BadModelRequestException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public BadModelRequestException(Throwable cause)
    {
        super(cause);
    }
}
