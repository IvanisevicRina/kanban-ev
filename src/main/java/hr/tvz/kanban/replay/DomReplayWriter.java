package hr.tvz.kanban.replay;

import hr.tvz.kanban.model.WeekAction;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;


public class DomReplayWriter {

    private static final Path REPLAY_DIRECTORY = Path.of("replays");

    public Path write(List<WeekAction> actions, String fileName) {
        Objects.requireNonNull(actions);
        Objects.requireNonNull(fileName);

        if (actions.isEmpty()) {
            throw new IllegalStateException("Nema poteza za pisanje u xml");
        }
        try {
            Files.createDirectories(REPLAY_DIRECTORY);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element rootElement = document.createElement("gameReplay");

            document.appendChild(rootElement);

            for (WeekAction action : actions) {
                rootElement.appendChild(createActionElement(document, action));
            }
            Path replayFile = createReplayPath(fileName);

            writeDocument(document, replayFile);

            return replayFile;


        } catch (Exception e) {
            throw new IllegalStateException("Nije moguće spremit replay u xml");
        }


    }

    private Node createActionElement(Document document, WeekAction action) {

        Element actionElement = document.createElement("action");

        appendTextElement(document, actionElement, "playerId", action.playerId());

        appendTextElement(document, actionElement, "weekNumber", String.valueOf(action.weekNumber()));

        appendTextElement(document, actionElement, "departmentType", action.departmentType().name());

        appendTextElement(document, actionElement, "description", action.description());

        return actionElement;


    }

    private void appendTextElement(Document document, Element parent, String elementName, String value) {

        Element element = document.createElement(elementName);
        element.setTextContent(value);
        parent.appendChild(element);

    }

    private Path createReplayPath(String fileName){
        String safeFileName = Path.of(fileName).getFileName().toString();

        if(!safeFileName.endsWith(".xml")){
            safeFileName =safeFileName+".xml";
        }
        return REPLAY_DIRECTORY.resolve(safeFileName);
    }

    private void writeDocument(Document document, Path replayFile) throws Exception{

        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD,"");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");
        transformer.transform(new DOMSource(document), new StreamResult(replayFile.toFile()));
    }


}
