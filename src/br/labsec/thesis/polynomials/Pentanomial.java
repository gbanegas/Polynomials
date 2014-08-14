package br.labsec.thesis.polynomials;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.TreeSet;


public class Pentanomial extends Trinomial {

	
	private BigInteger b;
	private BigInteger c;
	public Pentanomial(Polynomial p) throws Exception {
		super(p);
		if(this.degrees.size() > 5)
		{
			throw new Exception("This is not a pentanomial.");
		}
		this.setElements();
	}

	public Pentanomial(TreeSet<BigInteger> degrees) throws Exception {
		super(degrees);
		if(this.degrees.size() > 5)
		{
			throw new Exception("This is not a pentanomial.");
		}
		this.setElements();
	}

	private void setElements() {
		Iterator<BigInteger> it = this.degrees.iterator();

		BigInteger beforeLast = BigInteger.ZERO;
		BigInteger last = this.getA();
		while (it.hasNext()) {
			BigInteger toCompare = it.next();
			if(last.equals(toCompare)){
				this.b = beforeLast;
			}
			else
			{
				beforeLast = toCompare;
			}
			

		}
		
	}

	public BigInteger getB() {
		return b;
	}

	public BigInteger getC() {
		return c;
	}
	

}
