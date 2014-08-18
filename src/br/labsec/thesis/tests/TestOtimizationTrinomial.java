package br.labsec.thesis.tests;

import br.labsec.thesis.otimization.TrinomialContMatrix;
import br.labsec.thesis.polynomials.Polynomial;
import br.labsec.thesis.polynomials.Trinomial;

public class TestOtimizationTrinomial {

	public static void main(String[] args) {
		Trinomial tri;
		try {
			tri = new Trinomial(Polynomial.createFromString("x^16+x^14+x^0"));
			System.out.println(tri.isIrreducible());
			System.out.println(tri.toPolynomialString());
			TrinomialContMatrix cont = new TrinomialContMatrix(tri);
			cont.run();
			System.out.println(cont.getTotalXor());
			cont.saveXLS();
		//	TrinomialContXor cont = new TrinomialContXor(tri);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}