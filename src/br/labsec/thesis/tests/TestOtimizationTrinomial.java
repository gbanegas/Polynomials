package br.labsec.thesis.tests;

import br.labsec.thesis.otimization.TContMatrix;
import br.labsec.thesis.polynomials.Polynomial;
import br.labsec.thesis.polynomials.Trinomial;

public class TestOtimizationTrinomial {

	public static void main(String[] args) {
		Trinomial tri;
		Trinomial tri_2;
		try {
			tri = new Trinomial(Polynomial.createFromString("x^17+x^10+x^0"));
			System.out.println(tri.isIrreducible());
			System.out.println(tri.toPolynomialString());
			TContMatrix cont = new TContMatrix();
			System.out.println(cont.calculate(tri));
			cont.saveXLS();
			
			tri_2 = new Trinomial(Polynomial.createFromString("x^17+x^3+x^0"));
			System.out.println(tri_2.isIrreducible());
			System.out.println(tri_2.toPolynomialString());
			System.out.println(cont.calculate(tri_2));
			cont.saveXLS();
		//	TrinomialContXor cont = new TrinomialContXor(tri);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
