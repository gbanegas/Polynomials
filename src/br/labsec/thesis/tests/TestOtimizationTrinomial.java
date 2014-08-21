package br.labsec.thesis.tests;

import br.labsec.thesis.otimization.TrinomialCont;
import br.labsec.thesis.polynomials.Polynomial;
import br.labsec.thesis.polynomials.Trinomial;

public class TestOtimizationTrinomial {

	public static void main(String[] args) {
		Trinomial tri_2;
		Trinomial tri_3;
		try {
			TrinomialCont cont = new TrinomialCont();
			/*tri = new Trinomial(Polynomial.createFromString("x^17+x^10+x^0"));
			System.out.println(tri.isIrreducible());
			System.out.println(tri.toPolynomialString());
			
			System.out.println(cont.calculate(tri));
			cont.saveXLS();*/
			
			tri_2 = new Trinomial(Polynomial.createFromString("x^17+x^14+x^0"));
			System.out.println(tri_2.isIrreducible());
			System.out.println(tri_2.toPolynomialString());
			System.out.println(cont.calculate(tri_2));
			cont.saveXLS();
			
			/*tri_3 = new Trinomial(Polynomial.createFromString("x^1255 +x^74+x^0"));
			//System.out.println(tri_3.isIrreducible());
			System.out.println(tri_3.toPolynomialString());
			System.out.println(cont.calculate(tri_3));
			cont.saveXLS();*/
		//	TrinomialContX cont = new TrinomialContXor(tri);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
