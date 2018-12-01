import javax.inject.Singleton;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import pl.jrj.mdb.IMdbManager;

/**
 * REST Web Service
 *
 * @author Maciek Niechaj
 */
@Path("control")
@Singleton
public class RESTcontrol {

    private final IMdbManager mdbManager;
    private final String ALBUM_NR = "124634";
    private final Integer sessionId;
    
    private State currentState = State.STOPPED;
    private int counter = 0;
    private int errorCounter = 0;
    
    public RESTcontrol() {
        mdbManager = lookupMdbManagerRemote();
	sessionId = parseStringNumber(mdbManager.sessionId(ALBUM_NR));
        if (sessionId == null) {
            throw new IllegalStateException("Something went wrong "
                + "during bean registration");
        }
    }

    @Path("start")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String start() {
        if (currentState != State.COUNTING) {
            currentState = State.COUNTING;
        } else {
            errorCounter++;
        }
	return "";
    }
    
    @Path("stop")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String stop() {
        if (currentState != State.STOPPED) {
            currentState = State.STOPPED;
        } else {
            errorCounter++;
        }
	return "";
    }
    
    @Path("res")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String res() {
        if (counter != 0) {
            return Integer.toString(sessionId % counter);
        } else {
            return "Dividing by 0!";
        }
    }
    
    @Path("err")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String err() {
        if (errorCounter != 0) {
            return Integer.toString(sessionId % errorCounter);
        } else {
            return "Dividing by 0!";
        }
    }
    
    @Path("clr")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String clr() {
        errorCounter = 0;
        counter = 0;
        return "";
    }
    
    @Path("icr")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String icr() {
        if (currentState == State.COUNTING) {
            counter++;
        } else {
            errorCounter++;
        }
        return "";
    }
    
    @Path("dcr")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String dcr() {
        if (currentState == State.COUNTING) {
            counter--;
        } else {
            errorCounter++;
        }
        return "";
    }
    
    @Path("icr/{n}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String icrN(@PathParam("n") String n) {
        try {
            int nVal = parseStringNumber(n);
            counter += nVal;
        } catch (IllegalArgumentException e) {
            errorCounter++;
        }
        return "";
    }
    
    @Path("dcr/{n}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String dcrN(@PathParam("n") String n) {
        try {
            int nVal = parseStringNumber(n);
            counter -= nVal;
        } catch (IllegalArgumentException e) {
            errorCounter++;
        }
        return "";
    }
    
    private enum State {
        STOPPED, COUNTING
    }
    
    private Integer parseStringNumber(String n) throws IllegalArgumentException {
        Integer parsedValue;
        try {
            parsedValue = Integer.parseInt(n, 10);
        } catch (NumberFormatException e) {
            try {
                parsedValue = Integer.parseInt(n, 16);
            } catch (NumberFormatException e1) {
                try {
                    parsedValue = Integer.parseInt(n, 8);
                } catch (NumberFormatException e2) {
                    try {
                        parsedValue = Integer.parseInt(n, 2);
                    } catch (NumberFormatException e3) {
                        throw new IllegalArgumentException("Non-parsable n value format for string: " + n);
                    }
                }
            }
        }
        
        return parsedValue;
    }

    private IMdbManager lookupMdbManagerRemote() {
        try {
            Context context = new InitialContext();
            return (IMdbManager) context.lookup("java:global/mdb-project/MdbManager!pl.jrj.mdb.IMdbManager");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
    
}
