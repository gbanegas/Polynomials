package br.labsec.thesis.tests;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.labsec.thesis.otimization.TrinomialCont;
import br.labsec.thesis.polynomials.Polynomial;
import br.labsec.thesis.polynomials.Trinomial;

public class TrinomialContMatrixTest {
	private Trinomial tri;
	private Trinomial tri_1;
	private Trinomial tri_2;

	private TrinomialCont cont;
	private TrinomialCont cont_1;
	private TrinomialCont cont_2;

	@Before
	public void setUp() throws Exception {
		tri = new Trinomial(Polynomial.createFromString("x^17+x^1+x^0"));
		tri_1 = new Trinomial(Polynomial.createFromString("x^17+x^3+x^0"));
		tri_2 = new Trinomial(Polynomial.createFromString("x^17+x^10+x^0"));

		cont = new TrinomialCont();
		cont_1 = new TrinomialCont();
		cont_2 = new TrinomialCont();
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetTotalXor() {
		
		assertEquals(cont.calculate(tri),32);
		assertEquals(cont_1.calculate(tri_1), 30);
		assertEquals(cont_2.calculate(tri_2),31);		
	}

}
