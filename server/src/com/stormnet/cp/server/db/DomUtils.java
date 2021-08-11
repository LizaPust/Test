package com.stormnet.cp.server.db;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.time.LocalDate;

public class DomUtils {

    public static Integer getChildTagIntegerData(Element parentTag, String childTagName) {

        String dataStr = getChildTagData(parentTag, childTagName);
        return Integer.valueOf(dataStr);
    }

    public static LocalDate getChildTagLocalDateData(Element parentTag, String childTagName) {

        String dataStr = getChildTagData(parentTag, childTagName);
        return LocalDate.parse(dataStr);
    }

    public static boolean getChildTagBooleanData(Element parentTag, String childTagName) {

        String dataStr = getChildTagData(parentTag, childTagName);
        return Boolean.parseBoolean(dataStr);
    }

    public static String getChildTagData(Element parentTag, String childTagName) {

         return parentTag.getElementsByTagName(childTagName).item(0).getTextContent();
    }

    public static Element appendTagToParentTag(Document document, String childTagName, String  childTagData, Element parentTag){

        Element childTag = document.createElement(childTagName);
        Text text = document.createTextNode(childTagData);
        childTag.appendChild(text);
        parentTag.appendChild(childTag);

        return childTag;

    }

    public static void saveDocument(Document document, String filePath) throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(filePath);

        transformer.transform(domSource, streamResult);

    }
}
