import javax.ejb.Remote;


@Remote
public interface ISearchRemote {
	
	public int countConnectedComponents(IGraphRemote graph);
}
