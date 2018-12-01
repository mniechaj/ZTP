import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;


@Stateless
@LocalBean
public class Graph implements IGraphRemote {
	private Map<Integer, Node> nodes = new HashMap<>();
    
	public Graph() {
		
	}
	
	@Override
	public void addEdge(int u, int v) {
		createNode(u);
		createNode(v);
		updateNode(u, v);
		updateNode(v, u);
	}
	
	@Override
	public Integer getNextUnvisitedNode() {
		Integer result = null;
		for (Map.Entry<Integer, Node> entry : nodes.entrySet()) {
			Node node = entry.getValue();
			if (!node.isVisited()) {
				result = node.getId();
				break;
			}
		}
		return result;
	}
	
	@Override
	public boolean hasUnvisitedNode() {
		boolean hasUnvisitedNode = false;
		if (getNextUnvisitedNode() != null) {
			hasUnvisitedNode = true;
		}
		return hasUnvisitedNode;
	}
	
	@Override
	public void setAllNodesAsUnvisited() {
		nodes.forEach((k, v) -> v.markAsUnvisited());
	}
	
	@Override
	public void markNodeAsVisited(Integer nodeNum) {
		Node node = null;
		node = nodes.get(nodeNum);
		if (node != null) {
			node.markAsVisited();
		}
	}
	
	@Override
	public Set<Integer> getAllNeighboursForNode(Integer nodeNum) {
		Set<Integer> neighbours = new HashSet<>();
		if (nodes.get(nodeNum) != null) {
			nodes.get(nodeNum).getNeighbours().forEach(neighbour -> neighbours.add(neighbour.getId()));
		}
		return neighbours;
	}
	
	@Override
	public boolean isNodeVisited(Integer nodeNum) {
		boolean visited = false;
		if (nodes.get(nodeNum) != null) {
			visited = nodes.get(nodeNum).isVisited();
		}
		return visited;
	}
	
	private void createNode(int num) {
		if (!nodes.containsKey(num)) {
			nodes.put(num, new Node(num));
		}
	}
	
	private void updateNode(int nodeNum, int neighbourNum) {
		nodes.get(nodeNum).addNeighbour(nodes.get(neighbourNum));
	}
	
	@Override
	public void printGraphData() {
		System.out.println("Graph described by nodes: ");
		nodes.forEach((k, v) -> {
			System.out.println("Node: " + k + " with " + v.getNeighbours().size() + " neighbours");
		});
	}

    public class Node implements Serializable {
    	
		private static final long serialVersionUID = 1L;
		private final int id;
    	private boolean visited = false;
    	private Set<Node> neighbours = new HashSet<>();
    	
    	private Node(int id) {
    		this.id = id;
    	}
    	
    	private int getId() {
    		return id;
    	}
    	
    	private boolean isVisited() {
    		return visited;
    	}
    	
    	private void markAsVisited() {
    		visited = true;
    	}
    	
    	private void markAsUnvisited() {
    		visited = false;
    	}
    	
    	private Set<Node> getNeighbours() {
    		return neighbours;
    	}
    	
    	private void addNeighbour(Node node) {
    		neighbours.add(node);
    	}
    	
    	@Override
       	public String toString() {
    		return "Node with " + neighbours.size() + " neighbours";
    	}
    }
    
}
