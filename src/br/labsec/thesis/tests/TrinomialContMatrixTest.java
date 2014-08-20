package br.labsec.thesis.tests;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.labsec.thesis.otimization.TrinomialCont;
import br.labsec.thesis.polynomials.Polynomial;
import br.labsec.thesis.polynomials.Trinomial;

public class TrinomialContMatrixTest {
	private TrinomialCont cont;

	@Before
	public void setUp(){
		

		cont = new TrinomialCont();
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetTotalXor() throws Exception {
		Trinomial tri = new Trinomial(Polynomial.createFromString("x^17+x^1+x^0"));
		Trinomial tri_1 = new Trinomial(Polynomial.createFromString("x^17+x^3+x^0"));
		Trinomial tri_2 = new Trinomial(Polynomial.createFromString("x^17+x^10+x^0"));
		
		assertEquals(cont.calculate(tri),32);
		assertEquals(cont.calculate(tri_1), 30);
		assertEquals(cont.calculate(tri_2),31);	
		
		Trinomial tri_3 = new Trinomial(Polynomial.createFromString("x^171+x^1+x^0"));
		Trinomial tri_4 = new Trinomial(Polynomial.createFromString("x^171+x^30+x^0"));
		Trinomial tri_5 = new Trinomial(Polynomial.createFromString("x^171+x^88+x^0"));
		int t = tri_3.degree().intValue();
		assertEquals(cont.calculate(tri_3),(2*t)-2);	
		//System.out.println(cont.calculate(tri_4));
		assertEquals(cont.calculate(tri_4),(2*t)-tri_4.getA().intValue() - 1);
		assertEquals(cont.calculate(tri_5),(7*tri_5.getA().intValue()) - (2*t) - 5);
		
		
	}
	

}
