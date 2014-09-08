package br.labsec.thesis.tests.cont;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import br.labsec.thesis.threads.Lock;
import br.labsec.thesis.threads.ThreadCount;

public class PolynomialContXorTest {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) {
		
		ArrayList<String> polynomials = generateTri();
		ArrayList<String> polynomialsPent = generatePent();
		ArrayList<String> polynomialsSept = generateSept();
		HashMap<String, Integer> hash = new HashMap<String, Integer>();
		Lock lock = new Lock();
		
		ThreadCount c1 = new ThreadCount(polynomials, hash, lock);
		ThreadCount c2 = new ThreadCount(polynomialsPent, hash, lock);
		ThreadCount c3 = new ThreadCount(polynomialsSept, hash, lock);
		
		c1.start();
		//c2.start();
		//c3.start();
		
		while (true) {
		    try {
		        c1.join();
		        c2.join();
		        c3.join();
		        break;
		    }
		    catch (InterruptedException e) {
		        e.printStackTrace();
		    }
		}
		if(!c1.isAlive() && !c2.isAlive() && !c3.isAlive() )
		{
			printResults(hash);
		}
		
		
		
	}

	private static void printResults(HashMap<String, Integer> hash) {
		Set<String> keySet = hash.keySet();
		for (String string : keySet) {
			 int value = hash.get(string);
			 System.out.println("Pol = " + string + " : " + value );
		}
		
	}

	private static ArrayList<String> generateSept() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 1; i < 16; i++) {
			for (int j =1; j < i; j++) {
				for (int h = 1; h < j; h++) {
					for (int l = 1; l < h; l++) {
						for (int k = 1; k < l; k++) {
							String pol = "x^16" + "+x^ " + i + "+x^" + j
									+ "+x^" + h + "+x^" + l + "+x^" + k
									+ "+x^0";
							list.add(pol);
						}
					}
				}
			}
		}
		return list;
	}

	private static ArrayList<String> generatePent() {
		ArrayList<String> list = new ArrayList<String>();

		for (int i = 1; i < 16; i++) {
			for (int j = 1; j < i; j++) {
				for (int h = 1; h < j; h++) {
					String pol = "x^16" + "+x^" + i + "+x^" + j + "+x^" + h
							+ "+x^0";
					list.add(pol);
				}
			}
		}
		return list;
	}

	private static ArrayList<String> generateTri() {
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 1; i < 16; i++) {
			String pol = "x^16" + "+x^" + i + "+x^0";
			list.add(pol);

		}
		return list;

	}


}
