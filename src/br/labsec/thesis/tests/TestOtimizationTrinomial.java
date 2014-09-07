package br.labsec.thesis.tests;

import br.labsec.thesis.otimization.TriContXor;
import br.labsec.thesis.polynomials.Polynomial;
import br.labsec.thesis.polynomials.Trinomial;

public class TestOtimizationTrinomial {

	public static void main(String[] args) {
		//Trinomial tri_2;
		Trinomial tri_3;
		try {
			TriContXor cont = new TriContXor();
			tri_3 = new Trinomial(Polynomial.createFromString("x^16+x^3+x^0"));
			int count_xor = cont.calculate(tri_3);
			cont.saveXLS();
			System.out.println(tri_3.toPolynomialString());
			System.out.println(count_xor);
			
			tri_3 = new Trinomial(Polynomial.createFromString("x^16+x^9+x^0"));
			count_xor = cont.calculate(tri_3);
			cont.saveXLS();
			System.out.println(tri_3.toPolynomialString());
			System.out.println(count_xor);
			
			tri_3 = new Trinomial(Polynomial.createFromString("x^16+x^14+x^0"));
			count_xor = cont.calculate(tri_3);
			cont.saveXLS();
			System.out.println(tri_3.toPolynomialString());
			System.out.println(count_xor);
			
			tri_3 = new Trinomial(Polynomial.createFromString("x^16+x^15+x^0"));
			count_xor = cont.calculate(tri_3);
			cont.saveXLS();
			System.out.println(tri_3.toPolynomialString());
			System.out.println(count_xor);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

}
