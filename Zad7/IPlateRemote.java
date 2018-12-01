import java.util.Collection;
import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author Maciek Niechaj
 */
@Remote
public interface IPlateRemote {

    void setVerticalLines(List<Double> lines);

    void setHorizontalLines(List<Double> lines);
    
    Collection<Double> getXValues();
    
    Collection<Double> getYValues();
    
    Double getXCost(Integer index);
    
    Double getYCost(Integer index);
    
    void removeXIndex(Integer index);
    
    void removeYIndex(Integer index);
    
    Integer getHighestValueXIndex();
    
    Integer getHighestValueYIndex();
    
    Integer xyLinesSum();
    
}
