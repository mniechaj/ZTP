import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Maciek Niechaj
 */
public class StreetXMLHandler extends DefaultHandler {

    protected int streetCounter;

    protected final String streetName;

    protected String parsedStreetName1 = "";

    protected boolean bStreet1 = false;

    public StreetXMLHandler(String streetName) {
        this.streetCounter = 0;
        this.streetName = streetName;
    }

    public int getStreetOccurenceCount() {
        return streetCounter;
    }

    public void resetStreetCounter() {
        streetCounter = 0;
    }

    public void checkSearchFieldEq() {
        if (parsedStreetName1.equals(streetName)) {
            streetCounter++;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("NAZWA_1")) {
            bStreet1 = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (bStreet1) {
            parsedStreetName1 = new String(ch, start, length);
            checkSearchFieldEq();
            bStreet1 = false;
        }
    }
}
