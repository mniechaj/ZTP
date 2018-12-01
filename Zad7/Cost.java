import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Maciek Niechaj
 */
@Stateless
public class Cost implements ICostRemote {
    
    private IPlateRemote plate;

    private final List<Double> xLines = new ArrayList<>();
    private final List<Double> yLines = new ArrayList<>();

    @Override
    public void setHorizontalLines(List<Double> lines) {
        xLines.clear();
        xLines.addAll(lines);
    }

    @Override
    public void setVerticalLines(List<Double> lines) {
        yLines.clear();
        yLines.addAll(lines);
    }

    @Override
    public Double countCost() throws NamingException{
        Context context = new InitialContext();
        plate = (IPlateRemote) context.lookup("java:global/" + "124634" + "/Plate!IPlateRemote");
        Double cost = 0.0;
        plate.setHorizontalLines(xLines);
        plate.setVerticalLines(yLines);
        Integer verticals = 0;
        Integer horizontals = 0;
        int iters = plate.xyLinesSum();
        for (int i = 0; i < iters; i++) {
            Integer xHighestIndex = plate.getHighestValueXIndex();
            Integer yHighestIndex = plate.getHighestValueYIndex();
            Double xHighestValue;
            Double yHighestValue;
            
            if (xHighestIndex == -1) {
                xHighestValue = Double.MIN_VALUE;
            } else {
                xHighestValue = plate.getXCost(xHighestIndex);
            }
            
            if (yHighestIndex == -1) {
                yHighestValue = Double.MIN_VALUE;
            } else {
                yHighestValue = plate.getYCost(yHighestIndex);
            }
            
            if (xHighestValue > yHighestValue) {
                cost += xHighestValue * (verticals + 1);
                horizontals++;
                plate.removeXIndex(xHighestIndex);
            } else {
                cost += yHighestValue * (horizontals + 1);
                verticals++;
                plate.removeYIndex(yHighestIndex);
            }
        }

        return cost;
    }

}
