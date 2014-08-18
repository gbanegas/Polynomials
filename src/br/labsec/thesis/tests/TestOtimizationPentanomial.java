package br.labsec.thesis.tests;

import br.labsec.thesis.otimization.PentanomialContMatrix;
import br.labsec.thesis.polynomials.Pentanomial;
import br.labsec.thesis.polynomials.Polynomial;

public class TestOtimizationPentanomial {

	public static void main(String[] args) {
		Pentanomial pent;
		try {
			pent = new Pentanomial(Polynomial.createFromString("x^16+x^3+x^2+x^1+x^0"));
			System.out.println(pent.isIrreducible());
			System.out.println(pent.toPolynomialString());
			System.out.println(pent.getA());
			System.out.println(pent.getB());
			System.out.println(pent.getC());
			
			PentanomialContMatrix cont = new PentanomialContMatrix(pent);
			cont.run();
			System.out.println(cont.getTotalXor());
			//cont.saveXLS();
		//	TrinomialContXor cont = new TrinomialContXor(tri);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
