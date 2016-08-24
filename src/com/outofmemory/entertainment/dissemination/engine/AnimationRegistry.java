package com.outofmemory.entertainment.dissemination.engine;

import com.outofmemory.entertainment.dissemination.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class AnimationRegistry {
    private final HashMap<String, HashMap<String, AnimationImpl>> animations =
            new HashMap<String, HashMap<String, AnimationImpl>>();

    public AnimationImpl get(String objectId, String animationId) {
        return animations.get(objectId).get(animationId);
    }

    public void add(String objectId, String animationId, AnimationImpl animation) {
        if (!animations.containsKey(objectId)) {
            animations.put(objectId, new HashMap<String, AnimationImpl>());
        }
        HashMap<String, AnimationImpl> animationsByObject = animations.get(objectId);
        animationsByObject.put(animationId, animation);
    }

    public void parse(String fileName) {
        InputStream file = getClass().getResourceAsStream("/" + fileName);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            NodeList objectNodes = doc.getElementsByTagName("object");

            for (Node objectNode : XmlUtil.asList(objectNodes)) {
                Element objectElement = (Element) objectNode;
                String objectId = objectElement.getAttribute("id");
                NodeList animationNodes = objectElement.getElementsByTagName("animation");
                for (Node animationNode : XmlUtil.asList(animationNodes)) {
                    Element animationElement = (Element) animationNode;
                    String animationId = animationElement.getAttribute("id");
                    AnimationImpl animation = buildAnimation(objectId, animationElement);
                    add(objectId, animationId, animation);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Exception occured during parsing " + fileName, e);
        }

    }

    private AnimationImpl buildAnimation(String objectId, Element animationElement) {
        String symmetricAnimationId = animationElement.getAttribute("symmetric");
        if (!symmetricAnimationId.equals("")) {
            return flipAnimation(objectId, symmetricAnimationId);
        }
        String timesStr = animationElement.getAttribute("times");
        Integer times = Integer.parseInt(timesStr.equals("") ? "1" : timesStr);

        ArrayList<Frame> frames = new ArrayList<Frame>();
        NodeList frameNodes = animationElement.getElementsByTagName("frame");
        for (Node frameNode : XmlUtil.asList(frameNodes)) {
            Element frameElement = (Element) frameNode;
            NodeList rowNodes = frameElement.getElementsByTagName("row");

            ArrayList<String> rows = new ArrayList<String>();
            for (Node rowNode : XmlUtil.asList(rowNodes)) {
                rows.add(rowNode.getTextContent());
            }
            for (int i = 0; i < times; i++) {
                frames.add(new Frame(rows));
            }
        }
        return new AnimationImpl(frames);
    }

    private AnimationImpl flipAnimation(String objectId, String symmetricAnimationId) {
        AnimationImpl symmetricAnimation = get(objectId, symmetricAnimationId);
        ArrayList<Frame> frames = new ArrayList<Frame>();
        for (Frame symmetricFrame : symmetricAnimation.getFrames()) {
            frames.add(flipFrame(symmetricFrame));
        }
        return new AnimationImpl(frames);
    }

    private Frame flipFrame(Frame symmetricFrame) {
        ArrayList<String> rows = new ArrayList<String>();
        for (String symmetricRow : symmetricFrame.getRows()) {
            rows.add(flipRow(symmetricRow));
        }
        return new Frame(rows);
    }

    private String flipRow(String symmetricRow) {
        StringBuilder rowBuilder = new StringBuilder();
        for (int i = symmetricRow.length() - 1; i >= 0; i--) {
            char character = symmetricRow.charAt(i);
            rowBuilder.append(flipChar(character));
        }
        return rowBuilder.toString();
    }

    private char flipChar(char character) {
        switch (character) {
            case '[':
                return ']';
            case ']':
                return '[';
            case '\\':
                return '/';
            case '/':
                return '\\';
            case '>':
                return '<';
        }
        return character;
    }
}
