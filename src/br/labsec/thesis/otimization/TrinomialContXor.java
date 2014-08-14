package br.labsec.thesis.otimization;

import java.math.BigInteger;

import br.labsec.thesis.polynomials.Trinomial;

public class TrinomialContXor {

	Trinomial trinomial;
	
	public TrinomialContXor(Trinomial trinomial) {
		
		this.trinomial = trinomial;
		this.optmize();
	}
	private void optmize() {
		BigInteger max_size = (this.trinomial.degree().multiply(new BigInteger("2"))).subtract(new BigInteger("2"));
		System.out.println(max_size.toString());
		
	}
	
	
	
	
	

}
