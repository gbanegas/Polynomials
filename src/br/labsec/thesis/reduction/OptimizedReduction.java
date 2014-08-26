package br.labsec.thesis.reduction;

public class OptimizedReduction {

	public static void main(String[] args) {
		int[] array = new int[15];
		

		 array[9] = 0x1;
		 int[] reduced = OptimizedReduction.reduction(array);
		 System.out.println(reduced[4]);

	}
	
	public static int[] reduction(int[] c)
	{
		
		int[] reduced = new int[8];
		
		reduced[7] = c[7] ^ (c[12] << 1) ^ (c[11] >> 31) ^ (c[14]>>9) & 0x1FF;
		reduced[6] = c[6] ^ (c[11]<< 1) ^ (c[10] >> 30) ^ (c[13] >> 9) ^ (c[14]<<23);
		reduced[5] = c[5] ^ (c[10] << 1) ^ (c[9] >> 31) ^ (c[12]>>9) ^ (c[13]<<23);
		int T3 = (c[9] << 1) ^ (c[8] >> 30) ;
		T3 = T3 ^ (c[14] << 2) ^ (c[13] >> 30);
		
		reduced[4] = c[4] ^ T3 ^ ((c[12] << 23)  ^ (c[11]>>9) & 0xF0000000) ;
		//reduced[3] = c[3] ^ (c[8] << 1) ^ (c[10]>>9) ^ (c[11]<<23) ^ (c[13] <<2) ^ (c[12]>>30);
		
		int A = (c[7] >> 9) ^ (c[8] << 23);
		int B = (c[12] >> 9) ^ (c[13] << 23);
		int T1 = A ^ B;
		int C = (c[8] >> 9) ^ (c[9] << 23);
		int D = (c[13] >> 9) ^ (c[14] << 23);
		int T2 = C ^ D;
		
		
		reduced[3] = c[3] ^ (T1 << 10) ^ (T2 >> 20);

		reduced[2] = c[2] ;
		reduced[1] = c[1] ^ T2;
		reduced[0] = c[0] ^ T1;
		
		return reduced;
		
	}

}
