import java.util.List;

import javax.ejb.Remote;

@Remote
public interface ISolidRemote {

	public double countXZprojection(List<Double> points);
}
