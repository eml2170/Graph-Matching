package dc2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Task1 {

	private static final String SPACE = " ";
	static UndirectedGraph<String, DefaultEdge> g1 = null;
	static UndirectedGraph<String, DefaultEdge> g2 = null;
	static int maxDegree = 0;
	static final int THRESHOLD = 2;
	static final int K = 50; 
	static Link newestLink = null;
	static Set<Link> L = new HashSet<Link>();
	static Set<String> identifiedNodesG1 = new HashSet<String>();
	static Set<String> identifiedNodesG2 = new HashSet<String>();

	static PriorityQueue<Link> candidates = new PriorityQueue<Link>();

	static Map<String, Map<String, Link>> matrix = null;


	public static void main(String[] args) throws ClassNotFoundException, IOException{
		System.out.println("start");

		System.out.println("Building graphs");
		buildGraphs();

		System.out.println("Building links");
		buildLinks("L.txt");

		System.out.println("Calculating D");
		calculateD();

		System.out.println("Calculating scores");
		buildScores();

		System.out.println("Running Korula");
		korula();

		System.out.println("Writing links to file");
		write("output.txt");
		System.out.println("Found " + L.size());
		TestAccuracy.printAccuracy("output.txt", "T1.txt");
		System.out.println("end");

	}

	private static void buildGraphs() throws ClassNotFoundException, IOException{
//		g1 = new GraphBuilder().readGraphFromFile("G1");
//		g2 = new GraphBuilder().readGraphFromFile("G2");
		g1 = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
		Scanner scanner = new Scanner(new File("G1.txt"));
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			String[] data = line.split(SPACE);
			if(data.length == 2){
				String s = data[0];
				String t = data[1];
				g1.addVertex(s);
				g1.addVertex(t);
				g1.addEdge(s,t);
			}
		}
		scanner.close();
		
		g2 = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
		scanner = new Scanner(new File("G2.txt"));
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			String[] data = line.split(SPACE);
			if(data.length == 2){
				String s = data[0];
				String t = data[1];
				g2.addVertex(s);
				g2.addVertex(t);
				g2.addEdge(s,t);
			}
		}
		scanner.close();
	}

	private static void buildLinks(String filename) throws FileNotFoundException{
		System.out.println("Building links");
		matrix = new HashMap<String, Map<String, Link>>();
		Scanner scanner = new Scanner(new File(filename));
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			String[] data = line.split(SPACE);
			if(data.length == 2){
				String v1 = data[0];
				String v2 = data[1];
				Link link = getLink(v1,v2);
				L.add(link);
				identifiedNodesG1.add(v1);
				identifiedNodesG2.add(v2);
			}
		}
		scanner.close();
		System.out.println("Links built");
	}


	private static void calculateD(){
		int maxDegreeG1 = 0;
		int maxDegreeG2 = 0;

		for(String vertex : g1.vertexSet()){
			int degree = g1.degreeOf(vertex);
			if(degree > maxDegreeG1)
				maxDegreeG1 = degree;
		}

		for(String vertex : g2.vertexSet()){
			int degree = g2.degreeOf(vertex);
			if(degree > maxDegreeG2)
				maxDegreeG2 = degree;
		}
		maxDegree = Math.min(maxDegreeG1, maxDegreeG2);
	}

	//builds the matrix of similarity scores
	private static void buildScores(){

		for(Link l : L){
			List<String> N_u = Graphs.neighborListOf(g1, l.u);
			List<String> N_v = Graphs.neighborListOf(g2, l.v);

			/*for(String u2 : N_u){

				Map<String, Link> row;
				if(matrix.containsKey(u2))
					row = matrix.get(u2);
				else{
					row = new HashMap<String, Link>();
					matrix.put(u2, row);
				}

				for(String v2 : N_v){
					if(row.containsKey(v2)){
						Link link = row.get(v2);
						link.add(l);
						candidates.remove(link);
						candidates.add(link);
					}
					else{
						Link link = getLink(u2,v2);
						link.add(l);
						row.put(v2, link);
						candidates.add(link);
					}
				}*/
			for(String u2 : N_u){
				for(String v2 : N_v){
					Link link = getLink(u2,v2);
					link.add(l);
					candidates.remove(link);
					candidates.add(link);
				}
			}
		}
	}

	public static void korula(){
		for(int i = 0 ; i < K; i++){
			//System.out.println("----------------" + i + "-------------------");
			int j = maxDegree;
			if(candidates.isEmpty())
				;//System.out.println("Empty at i=" + i);
			while(j >= 1 && !candidates.isEmpty()){
				Link link = candidates.peek();
				//System.out.println("j=" + j + ", best candidate is " + link);
				if(link.score >= THRESHOLD && g1.degreeOf(link.u) >= j && g2.degreeOf(link.v) >= j){
					L.add(candidates.poll());
					newestLink = link;
					identifiedNodesG1.add(link.u);
					identifiedNodesG2.add(link.v);
					updateHeap(j);
				}
				j = j/2;
			}
		}
	}

	private static void updateHeap(int degree){
		Link witness = newestLink;
		//System.out.println("witness=" + witness);
		String u = witness.u;
		String v = witness.v;

		List<String> N_u = Graphs.neighborListOf(g1, u);
		List<String> N_v = Graphs.neighborListOf(g2, v);

		N_u.removeAll(identifiedNodesG1);
		N_v.removeAll(identifiedNodesG2);

		for(String u2 : N_u){
			for(String v2 : N_v){
				//if(g1.degreeOf(u2) >= degree && g2.degreeOf(v2) >= degree){
					Link link = getLink(u2, v2);
					link.add(witness);
					candidates.remove(link);
					candidates.add(link);
				//}
			}
		}
	}

	private static Link getLink(String u, String v){
		Link link;
		if(!matrix.containsKey(u)){
			Map<String, Link> row = new HashMap<String, Link>();
			matrix.put(u, row);

			link = new Link(u,v);
			row.put(v, link);
		}
		else{
			if(!matrix.get(u).containsKey(v)){
				link = new Link(u,v);
				matrix.get(u).put(v, link);
				return link;
			}
		}
		return matrix.get(u).get(v);
	}

	private static void write(String filename) throws IOException{
		FileWriter filewriter = new FileWriter(new File(filename));
		for (Link link : L){
			filewriter.write(link.u + " " + link.v + "\n");
		}
		filewriter.close();
	}
}