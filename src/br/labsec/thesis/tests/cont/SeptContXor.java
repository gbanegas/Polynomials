package br.labsec.thesis.tests.cont;

import br.labsec.thesis.otimization.PolynomialContXor;
import br.labsec.thesis.polynomials.Polynomial;

public class SeptContXor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Polynomial pol = Polynomial.createFromString("x^32 + x^31 + x^30 + x^29 + x^7 + x^6 + 1");
		PolynomialContXor cont  = new PolynomialContXor();
		int xor = cont.calculate(pol);
		System.out.println(xor);
	}

}
