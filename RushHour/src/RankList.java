import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class RankList {
    // record file path
    private static final String rankFilePath = "./rank.xml";

    static class Record implements Comparable<Record> {
        String user;
        int time;
        int steps;

        Record(String user, int time, int steps) {
            this.user = user;
            this.time = time;
            this.steps = steps;
        }

        @Override
        public int compareTo(Record rhs) {
            if (steps != rhs.steps)
                return steps - rhs.steps;
            if (time != rhs.time)
                return time - rhs.time;
            return user.compareTo(rhs.user);
        }

        @Override
        public String toString() {
            return user + ", " + steps + " steps, " + time + "s";
        }
    }

    static String readRank(int stage) {
        StringBuilder rankStr = new StringBuilder();
        List<Record> list = new ArrayList<>();
        File rankFile = new File(rankFilePath);
        secureFile(rankFile);

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(rankFile);
            NodeList stageList = doc.getElementsByTagName("stage");

            for (int i = 0; i < stageList.getLength(); i++) {
                Node stageNode = stageList.item(i);

                if (stageNode.getAttributes().getNamedItem("number").getNodeValue()
                        .equals(Integer.toString(stage))) {
                    NodeList recordList = stageNode.getChildNodes();
                    for (int j = 0; j < recordList.getLength(); j++) {
                        Node record = recordList.item(j);
                        list.add(new Record(
                                record.getAttributes().getNamedItem("name").getNodeValue(),
                                Integer.parseInt(record.getAttributes().getNamedItem("time").getNodeValue()),
                                Integer.parseInt(record.getAttributes().getNamedItem("steps").getNodeValue())
                        ));
                    }
                    break;
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to read record file!");
            System.exit(-1);
        }

        Collections.sort(list);
        int rank = 1;
        for (Record entry : list) {
            rankStr.append("Rank ").append(rank).append(": ").append(entry).append("\n");
            rank++;
        }
        return rankStr.toString();
    }

    static void addNewRecord(String name, int time, int steps, int stage) {
        File rankFile = new File(rankFilePath);
        secureFile(rankFile);
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(rankFile);
            NodeList stageList = doc.getElementsByTagName("stage");


            for (int i = 0; i < stageList.getLength(); i++) {
                Node stageNode = stageList.item(i);

                if (stageNode.getAttributes().getNamedItem("number").getNodeValue()
                        .equals(Integer.toString(stage))) {

                    // if records for the stage less than 10, just append a new one
                    if (stageNode.getChildNodes().getLength() < 10) {
                        Element record = doc.createElement("record");
                        record.setAttribute("name", name);
                        record.setAttribute("time", Integer.toString(time));
                        record.setAttribute("steps", Integer.toString(steps));
                        stageNode.appendChild(record);
                        break;
                    }

                    // otherwise, find the least one then replace it
                    NodeList recordList = stageNode.getChildNodes();
                    int nodeToBeReplaced = -1;
                    int targetSteps = steps;
                    int targetTime = time;
                    for (int j = 0; j < recordList.getLength(); j++) {
                        Node record = recordList.item(j);
                        if (compare(targetSteps, targetTime,
                                Integer.parseInt(record.getAttributes().getNamedItem("steps").getNodeValue()),
                                Integer.parseInt(record.getAttributes().getNamedItem("time").getNodeValue())
                        ) < 0) {
                            nodeToBeReplaced = j;
                            targetSteps = Integer.parseInt(record.getAttributes().getNamedItem("steps").getNodeValue());
                            targetTime = Integer.parseInt(record.getAttributes().getNamedItem("time").getNodeValue());
                        }
                    }
                    if (nodeToBeReplaced != -1) {
                        Element record = doc.createElement("record");
                        record.setAttribute("name", name);
                        record.setAttribute("time", Integer.toString(time));
                        record.setAttribute("steps", Integer.toString(steps));
                        stageNode.removeChild(recordList.item(nodeToBeReplaced));
                        stageNode.appendChild(record);
                    }
                    break;
                }
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource domSource = new DOMSource(doc);
            StreamResult streamResult = new StreamResult(rankFilePath);
            transformer.transform(domSource, streamResult);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to write record file!");
            System.exit(-1);
        }
    }


    // return value less than 0: left side is better
    // return value greater than 0: right side is better
    private static int compare(int lSteps, int lTime, int rSteps, int rTime) {
        if (lSteps != rSteps)
            return lSteps - rSteps;
        if (lTime != rTime)
            return lTime - rTime;
        return 0;
    }

    // if the record file not exist, create and initialize it
    private static void secureFile(File rankFile) {
        try {
            if (rankFile.createNewFile()) {
                // create new record file
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.newDocument();
                Element root = doc.createElement("rank_list");
                for (int i = 1; i <= 3; i++) {
                    Element stage = doc.createElement("stage");
                    stage.setAttribute("number", Integer.toString(i));
                    root.appendChild(stage);
                }
                doc.appendChild(root);

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                DOMSource domSource = new DOMSource(doc);
                StreamResult streamResult = new StreamResult(rankFile);
                transformer.transform(domSource, streamResult);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to create record file!");
            System.exit(-1);
        }
    }
}

