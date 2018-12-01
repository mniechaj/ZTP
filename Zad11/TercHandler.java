import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Maciek Niechaj
 */
public class TercHandler extends DefaultHandler {
    
    private final Map<String, String> wojNameNumMap = new HashMap<>();
    private boolean bWoj = false;
    private boolean bNazwa = false;
    
    private String parsedWojNumString = "";
    private String parsedWojShortName = "";    
    
    public TercHandler() {
        
    }
    
    public Map<String, String> getWojNameNumMap() {
        return wojNameNumMap;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (bWoj == true) {
            parsedWojNumString = new String(ch, start, length);
            bWoj = false;
        } else if (bNazwa == true) {
            parsedWojShortName = new String(ch, start, length);
            if (parsedWojShortName.equals(parsedWojShortName.toUpperCase())) {
                String normalized = Normalizer.normalize(parsedWojShortName.substring(0, 5), Normalizer.Form.NFD);
                parsedWojShortName = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "").replace("Ł", "L");
                wojNameNumMap.put(parsedWojShortName, parsedWojNumString);
            }
            bNazwa = false;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("WOJ")) {
            bWoj = true;
        } else if (qName.equalsIgnoreCase("NAZWA")) {
            bNazwa = true;
        }
    }
    
}

