import java.util.Set;
import java.util.Stack;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;


@Stateless
@LocalBean
public class Search implements ISearchRemote {
	
	public Search() {
		
	}
	
    @Override
    public int countConnectedComponents(IGraphRemote graph) {
    	int connectedComponentsCount = 0;
    	
    	graph.setAllNodesAsUnvisited();
    	while(graph.hasUnvisitedNode()) {
    		connectedComponentsCount++;
    		Stack<Integer> nStack = new Stack<>();
    		Integer nodeNum = graph.getNextUnvisitedNode();
    		graph.markNodeAsVisited(nodeNum);
    		nStack.push(nodeNum);
    		
    		// DFS
    		while(!nStack.isEmpty()) {
    			Integer num = nStack.pop();
    			Set<Integer> neighbours = graph.getAllNeighboursForNode(num);
    			neighbours.forEach(neighbour -> {
    				if (!graph.isNodeVisited(neighbour)) {
    					nStack.push(neighbour);
    					graph.markNodeAsVisited(neighbour);
    				}
    			});
    		}
    	}
    	
    	return connectedComponentsCount;
    }

}
