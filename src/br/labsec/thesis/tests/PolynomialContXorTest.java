package br.labsec.thesis.tests;

import br.labsec.thesis.otimization.PolynomialContXor;
import br.labsec.thesis.polynomials.Polynomial;
import br.labsec.thesis.polynomials.Trinomial;

public class PolynomialContXorTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Trinomial pol = new Trinomial(Polynomial.createFromString("x^16+x^1+x^0"));
		PolynomialContXor cont = new PolynomialContXor();
		int xor = cont.calculate(pol);
		System.out.println("XORs = " + xor);

	}

}
