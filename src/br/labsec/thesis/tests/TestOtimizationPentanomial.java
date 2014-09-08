package br.labsec.thesis.tests;

import br.labsec.thesis.otimization.PentanomialCont;
import br.labsec.thesis.polynomials.Pentanomial;
import br.labsec.thesis.polynomials.Polynomial;

public class TestOtimizationPentanomial {

	public static void main(String[] args) {
		Pentanomial pent;
		try {
			pent = new Pentanomial(Polynomial.createFromString("x^32+x^12+x^8+x^4+x^0"));
			System.out.println(pent.isIrreducible());
			System.out.println(pent.toPolynomialString());
			PentanomialCont cont = new PentanomialCont();
			System.out.println("Xors = " + cont.calculate(pent));
			cont.saveXLS();
			
			
			pent = new Pentanomial(Polynomial.createFromString("x^32+x^28+x^24+x^20+x^0"));
			System.out.println(pent.isIrreducible());
			System.out.println(pent.toPolynomialString());
			cont = new PentanomialCont();
			System.out.println("Xors = " + cont.calculate(pent));
			cont.saveXLS();
			
	/*		pent = new Pentanomial(Polynomial.createFromString("x^17+x^9+x^2+x^1+x^0"));
			System.out.println(pent.isIrreducible());
			System.out.println(pent.toPolynomialString());
			System.out.println("Xors = " + cont.calculate(pent));
			cont.saveXLS();
			
			
			pent = new Pentanomial(Polynomial.createFromString("x^17+x^16+x^2+x^1+x^0"));
			System.out.println(pent.isIrreducible());
			System.out.println(pent.toPolynomialString());
			System.out.println("Xors = " + cont.calculate(pent));
			cont.saveXLS();
			
			pent = new Pentanomial(Polynomial.createFromString("x^17+x^16+x^9+x^1+x^0"));
			System.out.println(pent.isIrreducible());
			System.out.println(pent.toPolynomialString());
			System.out.println("Xors = " + cont.calculate(pent));
			cont.saveXLS();*/
			
			//cont.saveXLS();
		//	TrinomialContXor cont = new TrinomialContXor(tri);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
