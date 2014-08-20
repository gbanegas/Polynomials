package br.labsec.thesis.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.labsec.thesis.otimization.TContMatrix;
import br.labsec.thesis.otimization.TrinomialContMatrix;
import br.labsec.thesis.polynomials.Polynomial;
import br.labsec.thesis.polynomials.Trinomial;

public class TrinomialContMatrixTest {
	private Trinomial tri;
	private Trinomial tri_1;
	private Trinomial tri_2;

	private TContMatrix cont;
	private TContMatrix cont_1;
	private TContMatrix cont_2;

	@Before
	public void setUp() throws Exception {
		tri = new Trinomial(Polynomial.createFromString("x^17+x^1+x^0"));
		tri_1 = new Trinomial(Polynomial.createFromString("x^17+x^3+x^0"));
		tri_2 = new Trinomial(Polynomial.createFromString("x^17+x^10+x^0"));

		cont = new TContMatrix();
		cont_1 = new TContMatrix();
		cont_2 = new TContMatrix();
		
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
