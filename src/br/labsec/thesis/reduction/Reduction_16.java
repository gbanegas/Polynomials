package br.labsec.thesis.reduction;

import br.labsec.thesis.polynomials.Polynomial;

public class Reduction_16 {
	private static int DEGREE = 16;
	public static Polynomial reduction(boolean[] toReduce)
	{
		
		
		boolean[] reduced = new boolean[DEGREE];
		//boolean[] toReduce = complete(toR);
		for(int i = 0 ; i < 4;i++)
		{
			reduced[i] = toReduce[i] ^ toReduce[i+16] ^toReduce[i+20];
		}
		
		
		boolean T0 = toReduce[16] ^toReduce[24];
		boolean T1 = toReduce[17] ^toReduce[25];
		boolean T2 = toReduce[18] ^toReduce[26];
		boolean T3 = toReduce[19] ^toReduce[27];
		reduced[4] = toReduce[4] ^ T0;
		reduced[5] = toReduce[5] ^ T1;
		reduced[6] = toReduce[6] ^ T2;
		reduced[7] = toReduce[7] ^ T3;
		
		reduced[8] = toReduce[8] ^ toReduce[16] ^ toReduce[28];
		reduced[9] = toReduce[9] ^ toReduce[17] ^ toReduce[29];
		reduced[10] = toReduce[10] ^ toReduce[18] ^ toReduce[30];

		reduced[11] = toReduce[11] ^ toReduce[19];
		reduced[12] = toReduce[12] ^ toReduce[16] ;
		reduced[13] = toReduce[13] ^ toReduce[17];
		reduced[14] = toReduce[14] ^ toReduce[18];
		reduced[15] = toReduce[15] ^ toReduce[19];
		
		
		return Polynomial.createFromArrayVector(reduced);
		
	}

	private static boolean[] complete(boolean[] toR) {
		boolean[] temp = new boolean[30+1];
		if(toR.length < 30)
		{
			for (int i = 30; i > toR.length; i--) {
				temp[i] = false;
			}
			for (int i = 0; i < toR.length; i++) {
				temp[i] = toR[i];
			}
		}
		return temp;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean[] toRed = new boolean[31];
		for(int i = 0; i < toRed.length;i++)
			toRed[i] = true;
		
		Polynomial pol = Polynomial.createFromArrayVector(toRed);
		System.out.println(pol.toPolynomialString());
		
		System.out.println(Reduction_16.reduction(toRed).toPolynomialString());

	}

}
