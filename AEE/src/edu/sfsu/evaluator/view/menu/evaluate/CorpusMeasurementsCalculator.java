/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.sfsu.evaluator.view.menu.evaluate;

import edu.sfsu.evaluator.model.AnnotatedDocument;
import edu.sfsu.evaluator.model.AnnotatedDocumentVersion;
import edu.sfsu.evaluator.model.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author anthony
 */
public class CorpusMeasurementsCalculator
{

    //array of document for display per doc.
    private HashMap<String, DocEvalData> docEvalList;

    public CorpusMeasurementsCalculator()
    {
        DocEvalData corpusData = new DocEvalData("Corpus");
        docEvalList = new HashMap<String, DocEvalData>();
        docEvalList.put(corpusData.getName(), corpusData);
    }

    public DocEvalData getDocEvalData(String docName)
    {
        return docEvalList.get(docName);
    }

    public Set<String> getDocNames()
    {
        return docEvalList.keySet();
    }

    public void calculate(ArrayList<AnnotatedDocument> documents)
    {


        AnnotatedDocumentVersion baseLineVersion;
        ArrayList<Entity> truthEntities;
        ArrayList<Entity> testEntities;
        for (AnnotatedDocument doc : documents)
        {
            String docName = doc.getDocName();
            docEvalList.put(docName, new DocEvalData(docName));
            DocEvalData docEvalData = docEvalList.get(doc.getDocName());
            ArrayList<AnnotatedDocumentVersion> versions;
            versions = doc.getVersions();
            baseLineVersion = doc.getBaseLineVersion();
            truthEntities = baseLineVersion.getEntities();
            for (AnnotatedDocumentVersion anoVersion : versions)
            {

                if (anoVersion != baseLineVersion)
                {
                    testEntities = anoVersion.getEntities();
                    addUp(truthEntities, testEntities, docEvalData);
                }
            }

            docEvalData.calculate();
        }

        docEvalList.get("Corpus").calculate();
    }

    private void addUp(ArrayList<Entity> truthEntities,
                       ArrayList<Entity> testEntities,
                       DocEvalData docEvalData)
    {
        MentionData mentionData;
        DocEvalData corpusEvalData = docEvalList.get("Corpus");
        for (Entity testEntity : testEntities)
        {
            boolean isFP = true; // Is false positive
            for (Entity truthEntity : truthEntities)
            {
                if (truthEntity.equals(testEntity))
                {
                    isFP = false;
                    break;
                }
            }
            if (isFP)
            {
                corpusEvalData.incrementFalsePos();
                docEvalData.incrementFalsePos();
                String mentionName = testEntity.getEntityType();
                if (corpusEvalData.containsMention(mentionName))
                {
                    corpusEvalData.getMentionData(mentionName).
                            incrementFalsePos();
                } else
                {
                    mentionData = new MentionData(mentionName);
                    mentionData.incrementFalsePos();
                    corpusEvalData.addMentionData(mentionName, mentionData);
                }

                if (docEvalData.containsMention(mentionName))
                {
                    docEvalData.getMentionData(mentionName).incrementFalsePos();
                } else
                {
                    mentionData = new MentionData(mentionName);
                    mentionData.incrementFalsePos();
                    docEvalData.addMentionData(mentionName, mentionData);
                }

            } else
            {
                corpusEvalData.incrementTruePos();
                docEvalData.incrementTruePos();
                String mentionName = testEntity.getEntityType();
                if (corpusEvalData.containsMention(mentionName))
                {
                    corpusEvalData.getMentionData(mentionName).
                            incrementTruePos();
                } else //if not in map then must add it
                {
                    mentionData = new MentionData(mentionName);
                    mentionData.incrementTruePos();
                    corpusEvalData.addMentionData(mentionName, mentionData);
                }

                if (docEvalData.containsMention(mentionName))
                {
                    docEvalData.getMentionData(mentionName).incrementTruePos();
                } else
                {
                    mentionData = new MentionData(mentionName);
                    mentionData.incrementTruePos();
                    docEvalData.addMentionData(mentionName, mentionData);
                }
            }
        }

        for (Entity truthEntity : truthEntities)
        {
            boolean isFN = true; // Is false negative
            for (Entity testEntity : testEntities)
            {
                if (truthEntity.equals(testEntity))
                {
                    isFN = false;
                    break;
                }
            }
            if (isFN)
            {
                corpusEvalData.incrementFalseNeg();
                docEvalData.incrementFalseNeg();
                String mentionName = truthEntity.getEntityType();
                if (corpusEvalData.containsMention(mentionName))
                {
                    corpusEvalData.getMentionData(mentionName).
                            incrementFalseNeg();
                } else
                {
                    mentionData = new MentionData(mentionName);
                    mentionData.incrementFalseNeg();
                    corpusEvalData.addMentionData(mentionName, mentionData);
                }

                if (docEvalData.containsMention(mentionName))
                {
                    docEvalData.getMentionData(mentionName).
                            incrementFalseNeg();
                } else
                {
                    mentionData = new MentionData(mentionName);
                    mentionData.incrementFalseNeg();
                    docEvalData.addMentionData(mentionName, mentionData);
                }
            }
        }
    }

    

public Vector<Vector<String>> getFormattedDocData()
    {
        Vector<Vector<String>> data = new Vector<Vector<String>>();
        Set<String> docNameList = docEvalList.keySet();
        Vector<String> row = new Vector<String>();
        DocEvalData docData = docEvalList.get("Corpus");
        row.add("Corpus");
        row.add(String.valueOf(docData.getPrecision()));
        row.add(String.valueOf(docData.getRecall()));
        row.add(String.valueOf(docData.getFMeasure()));
        data.add(row);
        
        for (String docName : docNameList)
        {
            if (docName.compareTo("Corpus") != 0)
            {
                row = new Vector<String>();
                docData = docEvalList.get(docName);
                row.add(docName);
                row.add(String.valueOf(docData.getPrecision()));
                row.add(String.valueOf(docData.getRecall()));
                row.add(String.valueOf(docData.getFMeasure()));
                data.add(row);
            }
        }

        return data;
    }

    public Vector<Vector<String>> getFormattedMentionData()
    {
        Vector<Vector<String>> data = new Vector<Vector<String>>();
        Set<String> docNameList = docEvalList.keySet();
        Vector<String> corpusMentionList = getMentionNames();
        DocEvalData docEvalData = docEvalList.get("Corpus");
        Vector<String> row = new Vector<String>();
        for (String mentionName : corpusMentionList)
        {
            MentionData mentionData = docEvalData.getMentionData(mentionName);
            row.add(String.valueOf(mentionData.getPrecision()));
            row.add(String.valueOf(mentionData.getRecall()));
            row.add(String.valueOf(mentionData.getFMeasure()));
        }
        
        data.add(row);
                    
        for (String docName : docNameList)
        {
            if (docName.compareTo("Corpus") != 0)
            {
                row = new Vector<String>();
                docEvalData = docEvalList.get(docName);
                Set<String> docMentionNameList = docEvalData.getMentionList();
                for (String mentionName : corpusMentionList)
                {
                    if (docMentionNameList.contains(mentionName))
                    {
                        MentionData mentionData = docEvalData.getMentionData(
                                mentionName);
                        row.add(String.valueOf(mentionData.getPrecision()));
                        row.add(String.valueOf(mentionData.getRecall()));
                        row.add(String.valueOf(mentionData.getFMeasure()));
                    } else
                    {
                        row.add("  -");
                        row.add("  -");
                        row.add("  -");
                    }
                }

                data.add(row);
            }
        }
            

        return data;
    }

    public Vector<String> getMentionNames()
    {
        Vector<String> names = new Vector<String>();
        DocEvalData docEvalData = docEvalList.get("Corpus");
        Set<String> corpusMentionNames = docEvalData.getMentionList();
        for (String mentionName : corpusMentionNames)
        {
            names.add(mentionName);
        }

        return names;
    }
}
