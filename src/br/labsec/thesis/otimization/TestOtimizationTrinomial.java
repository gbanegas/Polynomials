package br.labsec.thesis.otimization;

import br.labsec.thesis.polynomials.Polynomial;
import br.labsec.thesis.polynomials.Trinomial;

public class TestOtimizationTrinomial {

	public static void main(String[] args) {
		Trinomial tri;
		try {
			tri = new Trinomial(Polynomial.createFromString("x^1223+x^1000+x^0"));
			System.out.println(tri.toPolynomialString());
			TrinomialContMatrix cont = new TrinomialContMatrix(tri);
			cont.run();
			System.out.println(cont.getTotalXor());
		//	TrinomialContXor cont = new TrinomialContXor(tri);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
