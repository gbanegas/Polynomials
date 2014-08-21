package br.labsec.thesis.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import br.labsec.thesis.otimization.PentanomialCont;
import br.labsec.thesis.polynomials.Pentanomial;
import br.labsec.thesis.polynomials.Polynomial;

public class PentanomialContTest {
	private Pentanomial pent;
	private PentanomialCont cont; 

	@Before
	public void setUp() throws Exception {
		cont = new PentanomialCont();
	}

	@Test
	public void testCalculate() throws Exception {
		pent = new Pentanomial(Polynomial.createFromString("x^17+x^3+x^2+x^1+x^0"));
		assertEquals(cont.calculate(pent), 59);
		
		pent = new Pentanomial(Polynomial.createFromString("x^17+x^9+x^2+x^1+x^0"));
		assertEquals(cont.calculate(pent), 37);
		
		pent = new Pentanomial(Polynomial.createFromString("x^17+x^16+x^2+x^1+x^0"));
		assertEquals(cont.calculate(pent), 91);
	}

}
