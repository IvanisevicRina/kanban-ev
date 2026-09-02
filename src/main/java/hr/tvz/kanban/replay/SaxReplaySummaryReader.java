package hr.tvz.kanban.replay;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SaxReplaySummaryReader {

    public ReplaySummary read(Path replayFile){

        Objects.requireNonNull(replayFile);
        try{
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/" + "disallow-doctype-decl", true);
            factory.setFeature("http://xml.org/sax/features/"+"external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/" + "external-parameter-entities", false);

            SAXParser parser = factory.newSAXParser();
            SummaryHandler handler = new SummaryHandler();

            parser.parse(replayFile.toFile(), handler);
            return handler.createSummary();
        } catch (Exception e){
            throw new IllegalStateException("Nije moguće pročitat sažetak replaya pomoću SAX-a.", e);
        }

    }


    private static class SummaryHandler extends DefaultHandler {
        private final Set<String> playerIds = new HashSet<>();
        private final StringBuilder text = new StringBuilder();
        private int actionCount;
        private int lastWeekNumber;

        @Override
        public void startElement(String uri, String localName, String qualifiedName, Attributes attributes){
            text.setLength(0);
        }

        @Override
        public void characters(char[] characters, int start, int length){
            text.append(characters,start,length);
        }

        @Override
        public void endElement(String uri, String localName, String qualifiedName){
                String value = text.toString().trim();
                switch (qualifiedName){
                    case "playerId" -> playerIds.add(value);
                    case "weekNumber"-> updateLastWeek(value);
                    case "action" -> actionCount++;
                    default -> {
                        //ostali XL elementi nisu potrebni za sažetak replaya
                    }
                }
        }

        private void updateLastWeek(String value) {
                int weekNumber = Integer.parseInt(value);
                lastWeekNumber=Math.max(lastWeekNumber, weekNumber);

        }

        private ReplaySummary createSummary(){
            return new ReplaySummary(actionCount,lastWeekNumber,playerIds.size());
        }


    }
}
