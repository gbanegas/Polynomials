package br.labsec.thesis.polynomials;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.TreeSet;

public class Trinomial extends Polynomial {

	private BigInteger a;

	public Trinomial(Polynomial p) throws Exception {
		super(p);
		if(this.degrees.size() > 3 && p instanceof Trinomial)
		{
			throw new Exception("This is not a Trinomial.");
		}
		this.setA();
	}

	public Trinomial(TreeSet<BigInteger> degrees) throws Exception {
		super(degrees);
		if(this.degrees.size() > 3)
		{
			throw new Exception("This is not a Trinomial.");
		}
		this.setA();
	}

	private void setA() {
		Iterator<BigInteger> it = this.degrees.iterator();

		BigInteger beforeLast = BigInteger.ZERO;
		while (it.hasNext()) {
			BigInteger toCompare = it.next();
			if(this.degrees.last().equals(toCompare)){
				this.a = beforeLast;
			}
			else
			{
				beforeLast = toCompare;
			}
			

		}

	}
	
	public BigInteger getA()
	{
		return this.a;
	}

}
