package dc2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class LinksBuilder {
	Map<String, String> links;
	private static final String SPACE = " ";

	public LinksBuilder(){
		links = new HashMap<String, String>();
	}

	public void buildInternal(String filename) throws FileNotFoundException{
		System.out.println("Building links");
		Scanner scanner = new Scanner(new File(filename));
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			String[] data = line.split(SPACE);
			if(data.length == 2){
				String v1 = data[0];
				String v2 = data[1];
				links.put(v1, v2);
			}
		}
		scanner.close();
		System.out.println("Links built");
	}
	
	public static Stack<Link> build(String filename) throws FileNotFoundException{
		System.out.println("Building links");
		Stack<Link> stack = new Stack<Link>();
		Scanner scanner = new Scanner(new File(filename));
		while(scanner.hasNextLine()){
			String line = scanner.nextLine();
			String[] data = line.split(SPACE);
			if(data.length == 2){
				String v1 = data[0];
				String v2 = data[1];
				Link link = new Link(v1, v2);
				stack.add(link);
			}
		}
		scanner.close();
		System.out.println("Links built");
		return stack;
	}

	public void writeLinksToFile(String output) throws IOException{
		File file = new File(output);
		FileOutputStream f = new FileOutputStream(file);
		ObjectOutputStream s = new ObjectOutputStream(f);
		s.writeObject(links);
		s.close();
	}

	public Map<String, String> readLinksFromFile(String filename) throws IOException, ClassNotFoundException{
		File file = new File(filename);
		FileInputStream f = new FileInputStream(file);
		ObjectInputStream s = new ObjectInputStream(f);
		links = (Map<String, String>) s.readObject();
		return links;
	}
	

	public static void main(String[] args){
		LinksBuilder lb = new LinksBuilder();
		
		try {
			lb.build("L.txt");
			lb.writeLinksToFile("L");
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
