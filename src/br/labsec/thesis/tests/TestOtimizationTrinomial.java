package br.labsec.thesis.tests;

import org.apache.commons.math3.linear.RealMatrix;

import br.labsec.thesis.algorithm.GenerateAlgorithm;
import br.labsec.thesis.otimization.TriContXor;
import br.labsec.thesis.polynomials.Polynomial;
import br.labsec.thesis.polynomials.Trinomial;

public class TestOtimizationTrinomial {

	public static void main(String[] args) {
		//Trinomial tri_2;
		Trinomial tri_3;
		try {
			TriContXor cont = new TriContXor();
			tri_3 = new Trinomial(Polynomial.createFromString("x^11+x^9+x^0"));
			int count_xor = cont.calculate(tri_3);
			cont.saveXLS();
			RealMatrix m = cont.getMatrixOpt();
			GenerateAlgorithm.generateAlgorithm(tri_3, m, 32);
			System.out.println(tri_3.toPolynomialString());
			System.out.println(count_xor);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

}
