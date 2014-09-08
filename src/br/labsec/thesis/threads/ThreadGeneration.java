package br.labsec.thesis.threads;

import java.util.ArrayList;

import br.labsec.thesis.polynomials.Polynomial;

public class ThreadGeneration extends Thread {

	private ArrayList<String> polynomials;
	private int degree;
	private int terms;

	public ThreadGeneration(int terms, int degree) {
		polynomials = new ArrayList<String>();
		this.degree = degree;
		this.terms = terms;
	}

	@Override
	public void run() {
		System.out.println("Starting thread... " + this.getId());
		switch (terms) {
		case 3:
			for (int i = 1; i < degree; i++) {
				String pol = "x^" + degree + "+x^" + i + "+x^0";
				if (Polynomial.createFromString(pol).isIrreducible())
					polynomials.add(pol);
			}
			break;
			
		case 5:
			for (int i = 1; i < degree; i++) {
				for (int j = 1; j < i; j++) {
					for (int h = 1; h < j; h++) {
						String pol = "x^"+ degree + "+x^" + i + "+x^" + j + "+x^" + h
								+ "+x^0";
						if(Polynomial.createFromString(pol).isIrreducible())
							polynomials.add(pol);
					}
				}
			}
			break;
			
		case 7:
			for (int i = 1; i < degree; i++) {
				for (int j = 1; j < i; j++) {
					for (int h = 1; h < j; h++) {
						for (int l = 1; l < h; l++) {
							for (int k = 1; k < l; k++) {
								String pol = "x^"+ degree + "+x^ " + i + "+x^" + j
										+ "+x^" + h + "+x^" + l + "+x^" + k
										+ "+x^0";
								if(Polynomial.createFromString(pol).isIrreducible())
									polynomials.add(pol);
							}
						}
					}
				}
			}
			break;

		default:
			break;
		}

	}

	public ArrayList<String> getPolynomials() {
		return polynomials;
	}
	

}
