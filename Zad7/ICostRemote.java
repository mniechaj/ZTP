import java.util.List;
import javax.ejb.Remote;
import javax.naming.NamingException;

/**
 *
 * @author Maciek Niechaj
 */
@Remote
public interface ICostRemote {

    Double countCost() throws NamingException;
    
    void setVerticalLines(List<Double> lines);

    void setHorizontalLines(List<Double> lines);

}
