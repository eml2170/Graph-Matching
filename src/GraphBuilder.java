package dc2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.Set;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class GraphBuilder{

	UndirectedGraph<String, DefaultEdge> g;
	private static final String SPACE = " ";

	public GraphBuilder(){
		g = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
	}

	public void build(String filename) throws FileNotFoundException{
		System.out.println("Building graph");
		Scanner scanner = new Scanner(new File(filename));
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			String[] data = line.split(SPACE);
			if(data.length == 2){
				String s = data[0];
				String t = data[1];
				g.addVertex(s);
				g.addVertex(t);
				g.addEdge(s,t);
			}
		}
		scanner.close();
		System.out.println("Graph built");
	}

	public void writeGraphToFile(String output) throws IOException{
		File file = new File(output);
		FileOutputStream f = new FileOutputStream(file);
		ObjectOutputStream s = new ObjectOutputStream(f);
		s.writeObject(g);
		s.close();
	}

	public UndirectedGraph<String, DefaultEdge> readGraphFromFile(String filename) throws IOException, ClassNotFoundException{
		File file = new File(filename);
		FileInputStream f = new FileInputStream(file);
		ObjectInputStream s = new ObjectInputStream(f);
		g = (UndirectedGraph<String, DefaultEdge>) s.readObject();
		return g;
	}
	
	
	public Set<DefaultEdge> query(String v){
		return g.edgesOf(v);
	}

	public static void main(String[] args){
		GraphBuilder gb = new GraphBuilder();
		
		try {
			gb.build("G1.txt");
			gb.writeGraphToFile("G1");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		/**
		try {
			UndirectedGraph<String, DefaultEdge> g = gb.readGraphFromFile("graph");
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
	}


}