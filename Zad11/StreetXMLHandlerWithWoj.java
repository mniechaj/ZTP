import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author Maciek Niechaj
 */
public class StreetXMLHandlerWithWoj extends StreetXMLHandler {

    protected final String wojNumString;
    protected String parsedWojNumString = "";
    protected boolean bWoj = false;

    public StreetXMLHandlerWithWoj(String wojNum, String streetName) {
        super(streetName);
        this.wojNumString = wojNum;
    }

    @Override
    public void checkSearchFieldEq() {
        if (parsedStreetName1.equals(streetName)
                && parsedWojNumString.equals(wojNumString)) {
            streetCounter++;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("WOJ")) {
            bWoj = true;
        }
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (bWoj) {
            parsedWojNumString = new String(ch, start, length);
            bWoj = false;
        }
        super.characters(ch, start, length);
    }
}
