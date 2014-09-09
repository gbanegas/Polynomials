package br.labsec.thesis.tests.cont;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import br.labsec.thesis.threads.Lock;
import br.labsec.thesis.threads.ThreadCount;

public class PolynomialContXorTest {

	private static final int DEGREE = 32;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		System.out.println("Starting running...");
		ArrayList<String> polynomials = readFile();
		HashMap<String, Integer> hash = new HashMap<String, Integer>();

		Lock lock = new Lock();

		ArrayList<ThreadCount> threads = new ArrayList<ThreadCount>();
			ThreadCount c1 = new ThreadCount(polynomials.subList(0, polynomials.size()/2), hash,lock);
			ThreadCount c2 = new ThreadCount(polynomials.subList( polynomials.size()/2, polynomials.size()), hash,lock);
			threads.add(c1);
			threads.add(c2);

		System.out.println("Starting threads...");

		for (int i = 0; i < threads.size(); i++) {
			threads.get(i).start();
		}
		while (true) {
			try {
				for (int i = 0; i < threads.size(); i++) {
					threads.get(i).join();
				}
				break;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Finished threads...");
		System.out.println("Calculate...");
		printResults(hash);
		findBest(hash);

	}

	private static ArrayList<String> readFile() {
		ArrayList<String> list = new ArrayList<String>();
		String fileName = "list_" + DEGREE + ".txt";
		String line = null;
		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				line = line.replace("+ 1", "+x^0");
				System.out.println(line);
				list.add(line);
			}

			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
		return list;
	}

	private static void findBest(HashMap<String, Integer> hash) {
		int valueToCompare = 999999999;
		Set<String> keySet = hash.keySet();
		String result = "";
		for (String string : keySet) {
			int value = hash.get(string);
			if (value < valueToCompare) {
				result = "Pol = " + string + " : " + value;
				valueToCompare = value;
			}

		}
		System.out.println("Best Result is: ");
		System.out.println(result);

	}

	private static void printResults(HashMap<String, Integer> hash) {
		Set<String> keySet = hash.keySet();
		File file = new File("result_" + DEGREE + ".txt");
		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			for (String string : keySet) {
				int value = hash.get(string);
				String result = "Pol = " + string + " : " + value;
				bw.write(result + "\n");
				System.out.println(result);
			}
			bw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	/*
	 * private static ArrayList<String> generateSept() { HashSet<String> list =
	 * new HashSet<String>(); System.out.println("Generating Septanomials...");
	 * for (int i = 1; i < DEGREE; i++) { for (int j = 1; j < i; j++) { for (int
	 * h = 1; h < j; h++) { for (int l = 1; l < h; l++) { for (int k = 1; k < l;
	 * k++) { String pol = "x^" + DEGREE + "+x^ " + i + "+x^" + j + "+x^" + h +
	 * "+x^" + l + "+x^" + k + "+x^0"; list.add(pol); } } } } }
	 * System.out.println("Finished"); return new ArrayList<String>(list); }
	 * 
	 * private static ArrayList<String> generatePent() { ArrayList<String> list
	 * = new ArrayList<String>();
	 * System.out.println("Generating Pentanomials..."); for (int i = 1; i <
	 * DEGREE; i++) { for (int j = 1; j < i; j++) { for (int h = 1; h < j; h++)
	 * { String pol = "x^" + DEGREE + "+x^" + i + "+x^" + j + "+x^" + h +
	 * "+x^0"; list.add(pol); } } } return list; }
	 * 
	 * private static ArrayList<String> generateTri() {
	 * System.out.println("Generating Trinomials..."); ArrayList<String> list =
	 * new ArrayList<String>(); for (int i = 1; i < DEGREE; i++) { String pol =
	 * "x^" + DEGREE + "+x^" + i + "+x^0"; list.add(pol);
	 * 
	 * } return list;
	 * 
	 * }
	 */

}
