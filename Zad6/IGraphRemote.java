import java.util.Set;

import javax.ejb.Remote;


@Remote
public interface IGraphRemote {

	public void addEdge(int u, int v);
	
	public Integer getNextUnvisitedNode();
	
	public boolean hasUnvisitedNode();
	
	public boolean isNodeVisited(Integer nodeNum);
	
	public Set<Integer> getAllNeighboursForNode(Integer nodeNum);
	
	public void markNodeAsVisited(Integer nodeNum);
	
	public void setAllNodesAsUnvisited();
	
	public void printGraphData(); 
}
