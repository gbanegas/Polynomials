package br.labsec.thesis.reduction;

public class NewReducion {

	public static void main(String[] args) {
		 int[] array = new int[15];
		

		 array[9] = 0x1;
		 int[] reduced = NewReducion.reduction(array);
		 System.out.println(reduced[4]);
	}

	public static int[] reduction(int[] c)
	{
		
		int[] reduced = new int[8];
		reduced[7] = c[7] ^ (c[12] << 1) ^ (c[11] >> 31) ^ (c[14]>>9) & 0x1FF;
		reduced[6] = c[6] ^ (c[11]<< 1) ^ (c[10] >> 30) ^ (c[13] >> 9) ^ (c[14]<<23);
		reduced[5] = c[5] ^ (c[10] << 1) ^ (c[9] >> 31) ^ (c[12]>>9) ^ (c[13]<<23);
		reduced[4] = c[4] ^ (c[9] << 1) ^ (c[8] >> 31) ^ (c[12] << 23) ^ (c[11] >> 9) ^ (c[14] << 2) ^ (c[13]>>30);
		reduced[3] = c[3] ^ (c[8] << 1) ^ (c[10]>>9) ^ (c[11]<<23) ^ (c[13] <<2) ^ (c[12]>>30);
		int t1 = (c[7] << 1) & 0xFFFFFC00;
		int t2 = (c[9] >> 9) ^ (c[10] << 23);
		int t3 = (c[12] << 2) & 0xFFFFFE00;
		int t4 = (c[14] >> 8) & 0x1FF;
		reduced[2] = c[2] ^ t1 ^ t2 ^ t3 ^ t4;
		reduced[1] = c[1] ^ (c[8] >> 9) ^ (c[9] << 23);
		reduced[0] = c[0] ^ (c[7] >> 9) ^ (c[8] << 23);
		
		return reduced;
		
	}
}
