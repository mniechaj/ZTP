import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import pl.jrj.mdb.IMdbManager;

/**
 * REST Web Service
 *
 * @author Maciek Niechaj
 */
@Path("exchangeRate")
public class ExchRates {

    private final String NBP_EXCH_URI = "http://www.nbp.pl/kursy/xml/LastA.xml";

    @GET
    @Path("{currency : [A-Za-z]{3}}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getExchangeRate(@PathParam("currency") String currencyString) 
            throws NamingException, ParserConfigurationException, SAXException,
            MalformedURLException, URISyntaxException, IOException {
        IMdbManager mdbManager = lookupMdbManagerRemote();
        InputStream exchangeFileInputStream = new URI(NBP_EXCH_URI).toURL().openStream();
        
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = parserFactory.newSAXParser();
        CurrencyXmlHandler currencyXmlHandler = new CurrencyXmlHandler();
        saxParser.parse(exchangeFileInputStream, currencyXmlHandler);
        
        Map<String, CurrencyRateConverterPair> currencyCodeRateMap = currencyXmlHandler.getCurrencyCodeRateMap();
        // Default value for PLN currency which is not present in xml file
        currencyCodeRateMap.put("PLN", new CurrencyRateConverterPair(1.0, 1));
        
        CurrencyRateConverterPair referenceCurrencyRateConverterPair = currencyCodeRateMap.get(mdbManager.currencyId().toUpperCase());
        CurrencyRateConverterPair parameterCurrencyRateConverterPair = currencyCodeRateMap.get(currencyString.toUpperCase());
        if (referenceCurrencyRateConverterPair != null && parameterCurrencyRateConverterPair != null) {
            Double currencyRateRatio = parameterCurrencyRateConverterPair.getRateRatio(referenceCurrencyRateConverterPair);
            Double currencyConverterRatio = parameterCurrencyRateConverterPair.getConverterRatio(referenceCurrencyRateConverterPair);
            Double currencyRatio = currencyRateRatio / currencyConverterRatio;
	    DecimalFormat df = new DecimalFormat("#.####");
            df.setRoundingMode(RoundingMode.HALF_UP);
            return df.format(currencyRatio);
        } else {
            return "";
        }
    }

    private IMdbManager lookupMdbManagerRemote() throws NamingException {
        try {
            InitialContext context = new InitialContext();
            return (IMdbManager) context.lookup("java:global/mdb-project/MdbManager!pl.jrj.mdb.IMdbManager");
        } catch (NamingException e) {
            throw new NamingException("MdbManagerRemote bean was not found!");
        }
    }
    
}
