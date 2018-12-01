import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import javax.ejb.Stateless;

/**
 *
 * @author Maciek Niechaj
 */
@Stateless
public class Plate implements IPlateRemote {

    private final TreeMap<Integer, Double> xIndexCostMap = new TreeMap<>();
    private final TreeMap<Integer, Double> yIndexCostMap = new TreeMap<>();

    @Override
    public void setVerticalLines(List<Double> lines) {
        for (int i = 0; i < lines.size(); i++) {
            yIndexCostMap.put(i, lines.get(i));
        }
    }

    @Override
    public void setHorizontalLines(List<Double> lines) {
        for (int i = 0; i < lines.size(); i++) {
            xIndexCostMap.put(i, lines.get(i));
        }
    }

    @Override
    public void removeXIndex(Integer index) {
        if (xIndexCostMap.containsKey(index)) {
            xIndexCostMap.remove(index);
        }
    }

    @Override
    public void removeYIndex(Integer index) {
        if (yIndexCostMap.containsKey(index)) {
            yIndexCostMap.remove(index);
        }
    }

    @Override
    public Double getXCost(Integer index) {
        return xIndexCostMap.get(index);
    }

    @Override
    public Double getYCost(Integer index) {
        return yIndexCostMap.get(index);
    }

    @Override
    public Integer getHighestValueXIndex() {
        if (xIndexCostMap.size() >= 1) {
            return xIndexCostMap.entrySet().stream().max((e1, e2) -> e1.getValue() > e2.getValue() ? 1 : -1).get().getKey();
        } else if (xIndexCostMap.size() == 1) {
            return yIndexCostMap.firstKey();
        } else {
            return -1;
        }
    }

    @Override
    public Integer getHighestValueYIndex() {
        if (yIndexCostMap.size() > 1) {
            return yIndexCostMap.entrySet().stream().max((e1, e2) -> e1.getValue() > e2.getValue() ? 1 : -1).get().getKey();
        } else if (yIndexCostMap.size() == 1) {
            return yIndexCostMap.firstKey();
        } else {
            return -1;
        }
    }

    @Override
    public Integer xyLinesSum() {
        return xIndexCostMap.size() + yIndexCostMap.size();
    }

    @Override
    public Collection<Double> getXValues() {
        return xIndexCostMap.values();
    }

    @Override
    public Collection<Double> getYValues() {
        return yIndexCostMap.values();
    }

}
