package br.labsec.thesis.reduction;

import br.labsec.thesis.polynomials.Polynomial;

public class Red_1 {
	private static final int DEGREE = 32;

	public static Polynomial reduction(boolean[] toR)
	{
		boolean[] reduced = new boolean[DEGREE];
		boolean[] toReduce = complete(toR);
		for(int i = 0 ; i < 7;i++)
		{
			reduced[i] = toReduce[i] ^ toReduce[i+32] ^toReduce[i+56];
		}
		
		reduced[7] = toReduce[7] ^ toReduce[39];
		for(int i = 8 ; i < 15;i++)
		{
			reduced[i] = toReduce[i] ^ toReduce[i+32] ^ toReduce[i+24] ^ toReduce[i+48];
		}
		
		for(int i = 15 ; i < 31;i++)
		{
			boolean r  = toReduce[i] ^ toReduce[i+32] ^ toReduce[i+24];
			
			reduced[i] = r;
		}
		
		reduced[31] = toReduce[31] ^ toReduce[55] ;
		
		return Polynomial.createFromArrayVector(reduced);
		
	}

	private static boolean[] complete(boolean[] toR) {
		boolean[] temp = new boolean[62+1];
		if(toR.length < 62)
		{
			for (int i = 62; i > toR.length; i--) {
				temp[i] = false;
			}
			for (int i = 0; i < toR.length; i++) {
				temp[i] = toR[i];
			}
		}
		return temp;
	}
}
