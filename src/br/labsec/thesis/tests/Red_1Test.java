package br.labsec.thesis.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.labsec.thesis.polynomials.Polynomial;
import br.labsec.thesis.reduction.Red_1;

public class Red_1Test {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testReduction() {
		Polynomial p1 = Polynomial.createFromString("x^60 + x^28 + x^1");
		Polynomial result_p1 = Polynomial.createFromString("x^12 + x^4 + x^1");
		assertEquals(Red_1.reduction(p1.toByte()).toPolynomialString(), result_p1.toPolynomialString());
		
		Polynomial p2 = Polynomial.createFromString("x^33 + x^28 + x^1");
		Polynomial result_p2 = Polynomial.createFromString("x^28 + x^9");
		assertEquals(Red_1.reduction(p2.toByte()).toPolynomialString(), result_p2.toPolynomialString());
		
		Polynomial p3 = Polynomial.createFromString("x^60 + x^28 + x^1");
		Polynomial result_p3 = Polynomial.createFromString("x^12 + x^4 + x^1");
		assertEquals(Red_1.reduction(p3.toByte()).toPolynomialString(), result_p3.toPolynomialString());
		
		Polynomial p4 = Polynomial.createFromString("x^38 + x^36 + x^35 + x^34+x^33");
		Polynomial result_p4 = Polynomial.createFromString("x^14 + x^12 + x^11 + x^10 + x^9 + x^6 + x^4 + x^3 + x^2 + x^1");
		assertEquals(Red_1.reduction(p4.toByte()).toPolynomialString(), result_p4.toPolynomialString());
		
		Polynomial p5 = Polynomial.createFromString("x^31");
		Polynomial result_p5 = Polynomial.createFromString("x^31");
		assertEquals(Red_1.reduction(p5.toByte()).toPolynomialString(), result_p5.toPolynomialString());

	}

}
