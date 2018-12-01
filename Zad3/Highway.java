import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Highway extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private final String connectionParameterName = "conn";
    private final String query = "SELECT * FROM HData";
    private List<Arc> arcs = new LinkedList<>();
    private List<Node> nodes = new LinkedList<>(); 
    
    public Highway() {
    	
    }
    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String databaseConnection = request.getParameter(connectionParameterName);
		try {
			Connection connection = DriverManager.getConnection(databaseConnection);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			
			while (resultSet.next()) {
				int i1 = resultSet.getInt(2);
				int i2 = resultSet.getInt(3);
				arcs.add(new Arc(i1, i2));
			}
			arcs.forEach(a -> nodes.add(new Node(a)));
			nodes.forEach(node -> {
				nodes.forEach(other -> {
					Arc arc = node.arc;
					Arc otherArc = other.arc;
					if (arc.doCross(otherArc)) {
						node.neighbours.add(other);
					}
				});
			});
			
			PrintWriter writer = response.getWriter();
			if (isHighwayNetPossibile())
				writer.println("Wynik : 1");
			else
				writer.println("Wynik : 0");
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			arcs.clear();
			nodes.clear();
		}		
	}
    
    private boolean isHighwayNetPossibile() {
    	Stack<Node> stack = new Stack<>();
    	nodes.get(0).color = 1;
    	stack.push(nodes.get(1));
   
    	while (!stack.isEmpty()) {
    		Node node = stack.pop();
    		for (Node neighbour : node.neighbours) {
    			if (neighbour.color == 0) {
    				if (node.color == 1) {
    					neighbour.color = -1;
    				} else {
    					neighbour.color = 1;
    				}
    				stack.push(neighbour);
    			} else {
    				if (node.color == neighbour.color)
    					return false;
    			}
    		}    		
    	}

    	return true;
    }
    
    private class Arc {
    	Integer from;
    	Integer to;
    	
    	public Arc (Integer i1, Integer i2) {
    		if (i2 > i1) {
    			this.from = i1;
    			this.to = i2;
    		} else {
    			this.from = i2;
    			this.to = i1;
    		}
    	}
    	
    	public boolean doCross(Arc that) {
    		boolean ret = false;
    		if (this.to > that.to) {
    			if (this.from > that.from && this.from < that.to) {
    				ret = true;
    			}
    		} else if (this.to < that.to) {
    			if (this.from < that.from && this.to > that.from) {
    				ret = true;
    			}
    		} else {
    			ret = false;
    		}

    		return ret;
    	}
    	
    	@Override
    	public String toString() {
    		return "from :" + from + " to: " + to;
    	}
    }
    
    private class Node {
    	int color = 0;
    	Arc arc; 
    	Set<Node> neighbours = new HashSet<>();

    	public Node (Arc arc) {
    		this.arc = arc;
		}
    	
    	@Override
    	public String toString() {
    		return " arc: " + this.arc + " edges: " + this.neighbours;
    	}
    }

}
