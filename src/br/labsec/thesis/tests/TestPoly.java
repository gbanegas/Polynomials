package br.labsec.thesis.tests;
import br.labsec.thesis.polynomials.Polynomial;


public class TestPoly {
	public static void main(String[] args) {
		//Polynomial pa = Trinomial.createFromLong(0x51);
		
		Polynomial aa = Polynomial.createFromString("x^3000 + x^100 + 1");
		
		System.out.println(aa.toPolynomialString());
		
		System.out.println(aa.isIReducible());
		//System.out.println(aa.toPolynomialString());
	/*	System.out.println(pa.toPolynomialString());
		
		try {
			Trinomial t = new Trinomial(pa);
			System.out.println(t.toHexString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Polynomial p2 = Pentanomial.createFromLong(0x153);
		
		try {
			Pentanomial t = new Pentanomial(p2);
			
			System.out.println(t.toPolynomialString());
			System.out.println(t.isIReducible());
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
}
