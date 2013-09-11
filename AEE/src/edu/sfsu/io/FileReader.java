/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author eric
 */
public class FileReader
{

    public static String getTextFromFile(String path) throws
            FileNotFoundException
    {
        String fileText = null;
        BufferedReader br = new BufferedReader(new java.io.FileReader(path));
        try
        {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null)
            {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            fileText = sb.toString();
        } catch (IOException e)
        {
            System.err.println("[ERROR] " + e.getMessage());
        } finally
        {
            try
            {
                br.close();
            } catch (IOException e)
            {
                System.err.println("[ERROR] " + e.getMessage());
            }
        }
        return fileText;
    }
}
