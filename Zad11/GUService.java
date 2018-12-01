import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 * REST Web Service
 *
 * @author Maciek Niechaj
 */
@Path("pl")
public class GUService {
    
    private final String ULIC_URI_STRING = "http://cuda.iti.pk.edu.pl/ULIC_Urzedowy_2018-01-18.zip";
    private final String TERC_URI_STRING = "http://cuda.iti.pk.edu.pl/TERC_Urzedowy_2018-01-18.zip";
    private final InputStream ulicInputStream;
    private final InputStream tercInputStream;
    private final Map<String, String> wojNameNumMap;
    
    public GUService() throws URISyntaxException, MalformedURLException, IOException,
            ParserConfigurationException, SAXException {
        ulicInputStream = getInputStreamFromUrlString(ULIC_URI_STRING);
        tercInputStream = getInputStreamFromUrlString(TERC_URI_STRING);
        if (ulicInputStream == null) {
            throw new IllegalArgumentException(ULIC_URI_STRING + " URL is incorrect");
        } else if (tercInputStream == null) {
            throw new IllegalArgumentException(TERC_URI_STRING + " URL is incorrect");
        }
        
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        TercHandler tercHandler = new TercHandler();
        saxParser.parse(tercInputStream, tercHandler);
        wojNameNumMap = tercHandler.getWojNameNumMap();
    }
    
    @Path("{streetName}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getStreetCount(@PathParam("streetName") String streetName) throws UnsupportedEncodingException {
        StreetXMLHandler streetXMLHandler = new StreetXMLHandler(URLDecoder.decode(streetName, "UTF-8"));
        return getStreetOccurenceString(streetXMLHandler);
    }
    
    @Path("{woj : [0-9]{2}|[A-Za-z]{5}}/{streetName}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getStreetCountWithWoj(@PathParam("streetName") String streetName,
            @PathParam("woj") String woj) throws UnsupportedEncodingException {
        String resolvedWojNumString = resolveWojNum(woj);
        StreetXMLHandler streetXMLHandler = new StreetXMLHandlerWithWoj(resolvedWojNumString, URLDecoder.decode(streetName, "UTF-8"));
        return resolvedWojNumString == null ? "" : getStreetOccurenceString(streetXMLHandler);
    }
    
    @Path("{woj : [0-9]{2}|[A-Za-z]{5}}/{pow : [0-9]{2}}/{streetName}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getStreetCountWithWojAndPow(@PathParam("streetName") String streetName,
            @PathParam("woj") String woj, @PathParam("pow") String powNumString) throws UnsupportedEncodingException {
        String resolvedWojNumString = resolveWojNum(woj);
        StreetXMLHandler streetXMLHandler = new StreetXMLHandlerWithWojAndPow(resolvedWojNumString, powNumString, URLDecoder.decode(streetName, "UTF-8"));
        return resolvedWojNumString == null ? "" : getStreetOccurenceString(streetXMLHandler);
    }
    
    @Path("{wojWithPow : [A-Za-z]{5}[0-9]{2}|[0-9]{4}}/{streetName}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getStreetCountWithWojPowCompound(@PathParam("streetName") String streetName,
            @PathParam("wojWithPow") String wojWithPow) throws UnsupportedEncodingException {
        String woj = wojWithPow.substring(0, wojWithPow.length() - 2);
        String resolvedWojNumString = resolveWojNum(woj);
        String powNumString = wojWithPow.substring(wojWithPow.length() - 2);
        StreetXMLHandler streetXMLHandler = new StreetXMLHandlerWithWojAndPow(resolvedWojNumString, powNumString, URLDecoder.decode(streetName, "UTF-8"));
        return resolvedWojNumString == null ? "" : getStreetOccurenceString(streetXMLHandler);
    }
    
    private String resolveWojNum(String woj) {
        String wojNum = null;
        // Podanie wprost numeru
        if (woj.length() == 2) {
            wojNum = woj;
        // Podanie 5 - literowego kodu (wartość pozostaje null, gdy jest błędna)
        // podana wartość stringa zawsze rzutowana na upperCase -> Case insensitive
        } else if (wojNameNumMap.get(woj.toUpperCase()) != null) {
            wojNum = wojNameNumMap.get(woj.toUpperCase());
        }
        
        return wojNum;
    }
    
    private String getStreetOccurenceString(StreetXMLHandler streetXMLHandler) {
        try {
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = saxParserFactory.newSAXParser();
            saxParser.parse(ulicInputStream, streetXMLHandler);
            return Integer.toString(streetXMLHandler.getStreetOccurenceCount());
        } catch (ParserConfigurationException e) {
            return e.getMessage();
        } catch (SAXException e) {
            return e.getMessage();
        } catch (IOException e) {
            return e.getMessage();
        }
    }
    
    private InputStream getInputStreamFromUrlString(final String URLString) 
        throws URISyntaxException, MalformedURLException, IOException {
        InputStream inputStream = null;
        ZipInputStream zipInputStream = new ZipInputStream(
            new URI(URLString).toURL().openStream());
        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (zipEntry.getName().endsWith(".xml")) {
                inputStream = zipInputStream;
                break;
            }
        }
        
        return inputStream;
    }
    
}

