import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.naming.InitialContext;


public class AppClient {

	private ISearchRemote graphSearcher;
	private IGraphRemote graph;
	
	public static void main(String[] args) {
		String inputFile = args[0];
		try {
			AppClient appClient = new AppClient();
			List<Pair<Integer>> edges = appClient.parseInput(inputFile);
			InitialContext context = new InitialContext();
			appClient.graphSearcher =  (ISearchRemote)context.lookup("java:global/124634/Search!ISearchRemote");
			appClient.graph =  (IGraphRemote)context.lookup("java:global/124634/Graph!IGraphRemote");
	    	edges.forEach(edge -> appClient.graph.addEdge(edge.getFirst(), edge.getSecond()));
			
			int connectedComponents = appClient.graphSearcher.countConnectedComponents(appClient.graph);
			System.out.println("Ilość składowych : " + connectedComponents);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private List<Pair<Integer>> parseInput(String fileName) throws IOException {
		List<Pair<Integer>> edges = new LinkedList<>();
		Files.lines(Paths.get(fileName)).forEach(line -> {
			List<String> split = Arrays.asList(line.split("\\s+"));
			split = split.subList(0, split.size() - (split.size() % 2));
			for (int i = 0; i < split.size() ; i += 2) {
				edges.add(new Pair<Integer>(Integer.parseInt(split.get(i)), Integer.parseInt(split.get(i + 1))));
			}
		});

		return edges;
	}
	
	public class Pair<T> {
		private T first;
		private T second;
		
		public Pair (T first, T second) {
			this.first = first;
			this.second = second;
		}
		
		public T getFirst() {
			return first;
		}
		
		public T getSecond() {
			return second;
		}
		
		@Override
		public String toString() {
			return "first: " + first.toString() + " second: " + second.toString();
		}
	}

}

