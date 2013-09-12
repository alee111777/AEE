/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.util.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 *
 * @author eric
 */
public class Deserializer
{

    private Deserializer()
    {
    }

    public static Object deserialize(String filePath) throws IOException,
            ClassNotFoundException
    {
        FileInputStream fileIn = new FileInputStream(filePath);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        return in.readObject();
    }
}
