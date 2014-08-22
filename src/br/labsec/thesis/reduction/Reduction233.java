package br.labsec.thesis.reduction;

import br.labsec.thesis.polynomials.Polynomial;

public class Reduction233 {

	private static final int DEGREE = 233;

	public Reduction233() {
		
	}

	public static Polynomial reduction(boolean[] toReduce)
	{
		boolean[] reduced = new boolean[DEGREE];
		for(int i = 0 ; i < 73;i++)
		{
			reduced[i] = toReduce[i] ^ toReduce[i+392];
		}
		reduced[73] = toReduce[73] ^ toReduce[306];
		for(int i = 74 ; i < 147;i++)
		{
			reduced[i] = toReduce[i] ^ toReduce[i+233] ^ toReduce[i+318];
		}
		for(int i = 147 ; i < 232;i++)
		{
			reduced[i] = toReduce[i] ^ toReduce[i+159] ^ toReduce[i+233];
		}
		reduced[232] = toReduce[232] ^ toReduce[391] ;
		return Polynomial.createFromArrayVector(reduced);
		
	}
			
}
