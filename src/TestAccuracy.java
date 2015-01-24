package dc2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class TestAccuracy {
	
	public static void printAccuracy(String inputfile, String answerFile) throws FileNotFoundException{
		Set<String> answers = readAnswers(answerFile);
		int mistakes = 0;
		Scanner scanner = new Scanner(new File(inputfile));
		while(scanner.hasNext()){
			if(!answers.contains(scanner.nextLine()))
				mistakes++;
		}
		scanner.close();
		System.out.println("Num of mistakes: " + mistakes);
	}
	
	private static Set<String> readAnswers(String filename) throws FileNotFoundException{
		Set<String> answers = new HashSet<String>();
		Scanner scanner = new Scanner(new File(filename));
		while(scanner.hasNext()){
			answers.add(scanner.nextLine());
		}
		scanner.close();
		return answers;
	}

}
