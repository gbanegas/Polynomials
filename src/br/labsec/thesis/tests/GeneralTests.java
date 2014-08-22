package br.labsec.thesis.tests;

import br.labsec.thesis.polynomials.Polynomial;
import br.labsec.thesis.reduction.Red_1;

public class GeneralTests {

	public static void main(String[] args) {
		Polynomial p = Polynomial.createFromString("x^38");
		boolean[] b = p.toByte();
		boolean[] c = new boolean[63];
		for(int i = 62; i > b.length; i--)
			c[i] = false;
		for(int i = 0 ; i < b.length;i++)
			c[i] = b[i];
		
		Polynomial p1 = Polynomial.createFromString("x^60 + x^28 + x^1");
		Polynomial result_p1 = Polynomial.createFromString("x^12 + x^4 + x^1");
		String a1 = Red_1.reduction(p1.toByte()).toPolynomialString();
		String r1 = result_p1.toPolynomialString();
		System.out.println(a1 + "  = " + r1);
		
		Polynomial p2 = Polynomial.createFromString("x^33 + x^28 + x^1");
		Polynomial result_p2 = Polynomial.createFromString("x^28 + x^9");
		String a2 = Red_1.reduction(p2.toByte()).toPolynomialString();
		String r2= result_p2.toPolynomialString();
		System.out.println(a2 + "  = " + r2);
		
		
		Polynomial p3 = Polynomial.createFromString("x^60 + x^28 + x^1");
		Polynomial result_p3 = Polynomial.createFromString("x^12 + x^4 + x^1");
		String a3 = Red_1.reduction(p3.toByte()).toPolynomialString();
		String r3 = result_p3.toPolynomialString();
		System.out.println(a3 + "  = " + r3);
		
		
		Polynomial p4 = Polynomial.createFromString("x^38 + x^36 + x^35 + x^34+x^33");
		Polynomial result_p4 = Polynomial.createFromString("x^14 + x^12 + x^11 + x^10 + x^9 + x^6 + x^4 + x^3 + x^2 + x^1");
		String a4 = Red_1.reduction(p4.toByte()).toPolynomialString();
		String r4 = result_p4.toPolynomialString();
		System.out.println(a4 + "  = " + r4);
		
		Polynomial p5 = Polynomial.createFromString("x^31");
		Polynomial result_p5 = Polynomial.createFromString("x^31");
		String a5 = Red_1.reduction(p5.toByte()).toPolynomialString();
		String r5 = result_p5.toPolynomialString();
		System.out.println(a5 + "  = " + r5);
		
		
	}

}
