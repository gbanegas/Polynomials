package br.labsec.thesis.reduction;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class NewReducionTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReduction() {
		 int[] array = new int[16];
		 array[9] = 0x1;
		 int[] r = NewReducion.reduction(array);
		 array = new int[16];
		 array[9] = 0x1;
		 
		 int[] r2 = HankersonReduction.reduction(array);
		 
		 for(int i = 0; i < r.length;i++){
			 assertEquals(r[i], r2[i]);
		 }
		 
		 array = new int[16];
		 array[8] = 0x1;
		 array[9] = 0x1;
		 array[10] = 0x1;
		 array[11] = 0x1;
		 r = NewReducion.reduction(array);
		 
		 array = new int[16];
		 array[8] = 0x1;
		 array[9] = 0x1;
		 array[10] = 0x1;
		 array[11] = 0x1;
		 r2 = HankersonReduction.reduction(array);
		 for(int i = 0; i < r.length;i++){
			 assertEquals(r[i], r2[i]);
		 }
		 
		 array = new int[16];
		 array[8] = 0xFF;
		 array[9] = 0xFF;
		 array[10] = 0xFF;
		 array[11] = 0xFF;
		 r = NewReducion.reduction(array);
		 
		 array = new int[16];
		 array[8] = 0xFF;
		 array[9] = 0xFF;
		 array[10] = 0xFF;
		 array[11] = 0xFF;
		 r2 = HankersonReduction.reduction(array);
		 for(int i = 0; i < r.length;i++){
			 assertEquals(r[i], r2[i]);
		 }
		 
	}

}
