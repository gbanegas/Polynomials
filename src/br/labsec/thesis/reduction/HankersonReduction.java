package br.labsec.thesis.reduction;

public class HankersonReduction {

	public static void main(String[] args) {
		

	}
	
	public static int[] reduction(int[] c)
	{
		for(int i = 15; i <= 8; i++)
		{
			int T = c[i];
			c[i-8] = c[i-8] ^(T<<23);
			c[i-7] = c[i-7] ^(T>>9);
			c[i-5] = c[i-5] ^(T<<1);
			c[i-4] = c[i-4] ^(T>>31);
		}
		int T = c[7] >> 9;
		c[0] = c[0] ^ T;
		c[2] = c[2]^(T<<10);
		c[3] = c[3] ^(T>>22);
		c[7] = c[7] & 0x1FF;
		
		return c;
	}

}
