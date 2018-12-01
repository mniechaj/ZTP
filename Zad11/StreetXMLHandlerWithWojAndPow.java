import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Maciek Niechaj
 */
public class StreetXMLHandlerWithWojAndPow extends StreetXMLHandlerWithWoj {

    protected final String powNumString;
    protected String parsedPowNumString = "";
    protected boolean bPow = false;

    public StreetXMLHandlerWithWojAndPow(String wojNumString, String powNumString, String streetName) {
        super(wojNumString, streetName);
        this.powNumString = powNumString;
    }

    @Override
    public void checkSearchFieldEq() {
        if (parsedStreetName1.equals(streetName)
                && parsedWojNumString.equals(wojNumString)
                && parsedPowNumString.equals(powNumString)) {
            streetCounter++;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("POW")) {
            bPow = true;
        }
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (bPow) {
            parsedPowNumString = new String(ch, start, length);
            bPow = false;
        }
        super.characters(ch, start, length);
    }
}
