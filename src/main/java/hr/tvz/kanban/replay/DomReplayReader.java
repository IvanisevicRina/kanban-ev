package hr.tvz.kanban.replay;

import hr.tvz.kanban.model.DepartmentType;
import hr.tvz.kanban.model.WeekAction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DomReplayReader {

    public List<WeekAction> read(Path replayFile){
        Objects.requireNonNull(replayFile);

        if(!Files.exists(replayFile)){
            throw new IllegalStateException("XML replay datoteka ne prostoji");
        }
        try {
            DocumentBuilderFactory factory = createDocumentFactory();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(replayFile.toFile());
            document.getDocumentElement().normalize();

            NodeList actionNodes = document.getElementsByTagName("action");
            List<WeekAction> actions = new ArrayList<>();

            for (int i=0;i<actionNodes.getLength();i++){
                Element actionElement = (Element) actionNodes.item(i);
                actions.add(createAction(actionElement));
            }
            return List.copyOf(actions);
        } catch (Exception e){
            throw new IllegalStateException("Ne mogu učitat replay pomoću DOM-a",e);
        }
    }

    private WeekAction createAction(Element actionElement) {

        String playerId = getText(actionElement, "playerId");
        int weekNumber = Integer.parseInt(getText(actionElement, "weekNumber"));
        DepartmentType departmentType = DepartmentType.valueOf(getText(actionElement,"departmentType"));
        String description = getText(actionElement,"description");
        return new WeekAction(playerId, departmentType,weekNumber, description);
    }


    private String getText(Element parent, String elementName) {
        return parent.getElementsByTagName(elementName).item(0).getTextContent().trim();
    }




    private DocumentBuilderFactory createDocumentFactory() throws ParserConfigurationException {

            DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING,true);
            factory.setFeature("http://apache.org/xml/features/" + "disallow-doctype-decl", true);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA,"");

            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            return factory;
        }




}


