import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Maciek Niechaj
 */
public class CurrencyXmlHandler extends DefaultHandler {

    private final Map<String, CurrencyRateConverterPair> currencyCodeRateMap = new HashMap<>();
    
    private boolean bCurrencyCode = false;
    private boolean bConverter = false;
    private boolean bExchangeRate = false;
    
    private String parsedCurrencyCode = "";
    private Double parsedExchangeRate = null;
    private Integer parsedConverterValue = null;
    
    
    /**
     * 
     * @return Map of currency code and exchange rate
     */
    public Map<String, CurrencyRateConverterPair> getCurrencyCodeRateMap() {
        return currencyCodeRateMap;
    }
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (bCurrencyCode == true) {
            parsedCurrencyCode = new String(ch, start, length);
            bCurrencyCode = false;
        } else if (bExchangeRate == true) {
            // Replace ',' with '.' so it can be parsed as Java Double value
            String parsedExchangeRateString = new String(ch, start, length).replace(",", ".");
            try {
                parsedExchangeRate = Double.parseDouble(parsedExchangeRateString);
                if (parsedConverterValue != null)
                    currencyCodeRateMap.put(parsedCurrencyCode,
                            new CurrencyRateConverterPair(parsedExchangeRate, parsedConverterValue));
            } catch (NumberFormatException e) {
                // In case of problems with parsing exchange rate or converter
                // value, the entry is not put to map
            }
            bExchangeRate = false;
        } else if (bConverter == true) {
            String parsedConverterValueString = new String(ch, start, length);
            try {
                parsedConverterValue = Integer.parseInt(parsedConverterValueString);
            } catch(NumberFormatException e) {
                parsedConverterValue = null;
            }
            bConverter = false;
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equalsIgnoreCase("KOD_WALUTY")) {
            bCurrencyCode = true;
        } else if (qName.equalsIgnoreCase("KURS_SREDNI")) {
            bExchangeRate = true;
        } else if (qName.equalsIgnoreCase("PRZELICZNIK")) {
            bConverter = true;
        }
    }
    
}

