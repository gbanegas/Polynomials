package br.labsec.thesis.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.labsec.thesis.otimization.TrinomialContMatrix;
import br.labsec.thesis.polynomials.Polynomial;
import br.labsec.thesis.polynomials.Trinomial;

public class TrinomialContMatrixTest {
	private Trinomial tri;
	private Trinomial tri_1;
	private Trinomial tri_2;

	private TrinomialContMatrix cont;
	private TrinomialContMatrix cont_1;
	private TrinomialContMatrix cont_2;

	@Before
	public void setUp() throws Exception {
		tri = new Trinomial(Polynomial.createFromString("x^17+x^1+x^0"));
		tri_1 = new Trinomial(Polynomial.createFromString("x^17+x^3+x^0"));
		tri_2 = new Trinomial(Polynomial.createFromString("x^17+x^10+x^0"));

		cont = new TrinomialContMatrix(tri);
		cont_1 = new TrinomialContMatrix(tri_1);
		cont_2 = new TrinomialContMatrix(tri_2);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetTotalXor() {
		cont.run();
		assertEquals(cont.getTotalXor(),32);
		cont_1.run();
		assertEquals(cont_1.getTotalXor(), 30);
		cont_2.run();
		assertEquals(cont_2.getTotalXor(),31);		
	}

}
