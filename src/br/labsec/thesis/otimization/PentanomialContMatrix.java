package br.labsec.thesis.otimization;

import java.math.BigInteger;
import java.util.HashMap;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import br.labsec.thesis.polynomials.Pentanomial;

public class PentanomialContMatrix implements Runnable {
	private static final double NULL = -1.0;
	private Pentanomial pent;
	private int max_size;
	private int max_row;
	private BigInteger bigMaxSize;

	private int m_row;
	private RealMatrix matrix;
	private int totalXor;
	private String fName;

	public PentanomialContMatrix(Pentanomial pent) {
		this.pent = pent;
		this.m_row = 1;
	}

	@Override
	public void run() {
		int nr = this.calculateNR();
		this.max_row = (nr * 5) + 5;
		this.generateMatrix();
		this.optmize(nr);
		this.printMatrix();

	}

	private void optmize(int nr) {

		firtsReduction();
		nr = nr - 1;
		/*
		 * for (int i = 0; i < nr; i++) { this.othersReductions(i+1); }
		 */
	}

	private void othersReductions(int interaction) {
		// TODO:refactor the method
		double[] row = matrix.getRow(interaction - 1);
		double[] rowToReduce = matrix.getRow(interaction + 2);

		HashMap<Double, Double> expWhereToSave = new HashMap<Double, Double>();
		for (int i = 0; i < matrix.getColumnDimension(); i++) {
			if (row[i] != NULL && rowToReduce[i] != NULL) {
				boolean validy = (row[i] >= this.pent.degree().doubleValue());

				if (validy) {
					expWhereToSave.put(row[i], rowToReduce[i]);
				}
			}
		}

		this.mount(expWhereToSave, interaction);
	}

	private void mount(HashMap<Double, Double> expWhereToSave, int interaction) {
		int row_temp = this.m_row;
		Integer size = Integer.valueOf(this.m_row);

		boolean changed = false;
		for (int i = size - 2; i < size; i++) {
			double[] rowToWrite = matrix.getRow(row_temp);
			double[] row = matrix.getRow(i);
			int j = 0;
			for (int h = 0; h < matrix.getColumnDimension(); h++) {
				double cell = row[h];

				if (cell != NULL) {
					Double number = (Double) (cell);

					if (expWhereToSave.containsKey(((number)))) {

						rowToWrite[j] = expWhereToSave.get(number);
						changed = true;
					}
				}
				j++;
			}
			if (changed) {
				this.matrix.setRow(row_temp, rowToWrite);
				row_temp++;
				changed = false;
			}
		}
		this.m_row = row_temp;

	}

	private void firtsReduction() {
		this.reductionBy(0, bigMaxSize);
		this.reductionBy(this.pent.getA().intValue(), bigMaxSize);
		this.reductionBy(this.pent.getB().intValue(), bigMaxSize);
		this.reductionBy(this.pent.getC().intValue(), bigMaxSize);
	}

	private void reductionBy(int exp, BigInteger max_size) {
		BigInteger counter = this.pent.degree();
		HashMap<BigInteger, BigInteger> expWhereToSave = new HashMap<BigInteger, BigInteger>();
		BigInteger t = max_size.add(BigInteger.ONE);
		int m_1 = this.pent.degree().intValue();
		BigInteger m1 = new BigInteger(Integer.toString(m_1));

		BigInteger realKey = max_size;
		while (counter.compareTo(t) == -1) {
			BigInteger toPlace = (counter.subtract(this.pent.degree()))
					.add(new BigInteger(Integer.toString(exp)));
			toPlace = toPlace.add(m1.subtract(new BigInteger(Integer
					.toString(exp)).multiply(new BigInteger("2"))));
			expWhereToSave.put(realKey, toPlace);
			counter = counter.add(BigInteger.ONE);
			realKey = realKey.subtract(BigInteger.ONE);
		}

		generateRow(expWhereToSave);
	}

	private void generateRow(HashMap<BigInteger, BigInteger> expWhereToSave) {
		int temp = m_row;
		double[] row = this.matrix.getRow(m_row++);

		for (BigInteger key : expWhereToSave.keySet()) {
			BigInteger value = expWhereToSave.get(key);
			row[value.intValue()] = key.intValue();
		}
		this.matrix.setRow(temp, row);

	}

	private int calculateNR() {
		BigInteger bigMaxSize = (this.pent.degree()
				.multiply(new BigInteger("2"))).subtract(new BigInteger("2"));
		this.bigMaxSize = bigMaxSize;
		this.max_size = bigMaxSize.intValue();

		int nr = 2;
		int deg = (int) Math.floor((this.pent.degree().intValue() + 1) / 2);

		if (this.pent.getC().intValue() > deg) {
			nr = 2 * (this.pent.getC().intValue() + 1)
					- this.pent.degree().intValue();
		}
		return nr;
	}

	private void generateMatrix() {
		this.matrix = MatrixUtils.createRealMatrix(this.max_row,
				this.max_size + 1);
		this.cleanMatrix();
		int temp = this.max_size;
		for (int i = 0; i < this.matrix.getColumnDimension(); i++) {
			this.matrix.setEntry(0, i, temp);
			temp = temp - 1;
		}
	}

	private void cleanMatrix() {
		for (int j = 0; j < this.matrix.getRowDimension(); j++) {
			for (int i = 0; i < this.matrix.getColumnDimension(); i++) {
				this.matrix.setEntry(j, i, -1);
			}
		}

	}

	public void printMatrix() {
		System.out.println();
		for (int j = 0; j < this.matrix.getRowDimension(); j++) {
			for (int i = 0; i < this.matrix.getColumnDimension(); i++) {
				double n = this.matrix.getEntry(j, i);
				System.out.print(n + " ");
			}
			System.out.println();
		}

	}

	public Pentanomial getPent() {
		return pent;
	}

	public BigInteger getBigMaxSize() {
		return bigMaxSize;
	}

	public int getTotalXor() {
		return totalXor;
	}

	public String getfName() {
		return fName;
	}

}
