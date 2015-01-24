package dc2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Task2 {

	//checkin behavior per user
	static List<Checkin> c_train;
	static List<Checkin> c_test;
	static Set<String> locations = new HashSet<String>();
	static FileWriter writer;

	static final String TAB = "\t";
	static double alpha = 0.00005;

	public static void main(String[] args) throws IOException{
		System.out.println("Begin");
		long startTime = System.currentTimeMillis();

		System.out.println("Building training checkins");
		c_train = buildCheckins("N2.txt");

		System.out.println("Building testing checkins");
		c_test = buildCheckins("N1.txt");

		writer = new FileWriter(new File("outputT2.txt"));
		System.out.println("Identify first user");
		for(int user = 0; user < c_test.size(); user++)
			writer.write(c_test.get(user).userId + " " + c_train.get(identifyUser(user)).userId + "\n");
		writer.close();
		System.out.println("End");
		double time_elapsed = (System.currentTimeMillis() - startTime)/60000.0;
		System.out.println("Time elapsed: " + time_elapsed + " mins");
		TestAccuracy.printAccuracy("outputT2.txt");
	}

	public static List<Checkin> buildCheckins(String filename) throws FileNotFoundException{
		List<Checkin> list = new ArrayList<Checkin>();
		Scanner scanner = new Scanner(new File(filename));
		String firstLine = scanner.nextLine();
		String[] firstdata = firstLine.split(TAB);
		int firstuser = Integer.parseInt(firstdata[0]);
		String firstlocation = firstdata[4];
		Checkin firstcheckin = new Checkin(firstuser);
		firstcheckin.add(firstlocation);

		int index = firstuser;
		list.add(firstcheckin);

		while(scanner.hasNext()){
			String line = scanner.nextLine();
			String[] data = line.split(TAB);
			int user = Integer.parseInt(data[0]);
			String location = data[4];
			if(user == index)
				list.get(list.size()-1).add(location);
			else{
				index = user;
				Checkin checkin = new Checkin(user);
				checkin.add(location);
				list.add(checkin);
			}
			locations.add(location);
		}
		scanner.close();
		return list;
	}

	public static int identifyUser(int index){
		double max = 0.0;
		int realUser = -1;

		for(int i = 0; i < c_train.size(); i++){
			int candidate = i;
			double score = score(index, candidate);
			if(score > max){
				max = score;
				realUser = candidate;
			}
		}
		//System.out.println("Real user=" + realUser);
		//System.out.println("Score=" + max);
		return realUser;
	}

	private static double score(int anonymous, int candidate){
		Map<String, Integer> checkins_anon = c_test.get(anonymous).freqs;
		double product = 1;
		for(String location : checkins_anon.keySet()){
			int freq = checkins_anon.get(location);
			product *= Math.pow(conditionalProb(location, candidate), freq);
		}

		return product;
	}

	private static double conditionalProb(String location, int v){
		Checkin checkin = c_train.get(v);		
		double num = !checkin.freqs.containsKey(location) ? alpha : checkin.freqs.get(location) + alpha;
		double denom = checkin.totalCheckins + alpha*locations.size();
		return num/denom;
	}
}
