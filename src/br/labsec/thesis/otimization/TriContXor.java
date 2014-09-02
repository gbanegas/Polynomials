package br.labsec.thesis.otimization;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

import javax.naming.LimitExceededException;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import br.labsec.thesis.polynomials.Trinomial;
import br.labsec.thesis.util.XLSWriter;

public class TriContXor {

	private static final double NULL = -1;
	private static final double T = -2;
	private RealMatrix matrix;
	private RealMatrix matrixOpt;
	private Trinomial tri;
	private int max_size;
	private int max_row;
	private int actual_row;

	private ArrayList<Integer> exp;
	private String fileName;

	public int calculate(Trinomial tri) {
		actual_row = 1;
		this.tri = tri;
		int nr = this.calculateNR();
		this.max_row = (nr * tri.degree().intValue() * 3) + 5;
		this.generateMatrix(nr);
		this.reduction(nr);

		this.repeatRemove();
		this.clean();
		this.optimize();
		int countXor = this.countXor();

		return countXor;
	}
	
	

	private void optimize() {
		this.initMatrixOpt();
		this.copyMatrix();
		this.removeNULLrows();

		for (int j = 1; j < this.matrixOpt.getRowDimension()-1; j++) {
			for (int i = this.max_size; i > (this.max_size - this.tri.getA().intValue()+1); i--) {
				double toCompare = this.matrixOpt.getEntry(j, i);
				if(toCompare != NULL)
					this.matrixOpt.setEntry(j, i, T);
			}
		}
		for (int j = 2; j < this.matrixOpt.getRowDimension()-1; j++) {
			for (int i = (this.max_size - this.tri.getA().intValue()); i > (this.max_size - 2*this.tri.getA().intValue() +1); i--) {
				double toCompare = this.matrixOpt.getEntry(j, i);
				if(toCompare != NULL)
					this.matrixOpt.setEntry(j, i, T);
			}
		}

	}

	private void initMatrixOpt() {
		this.matrixOpt = MatrixUtils.createRealMatrix(this.max_row,
				this.max_size + 1);
		
	}



	private void copyMatrix() {
		int j = 0;
		for (int i = 0; i < matrix.getRowDimension(); i++) {
			if(!this.isCleanRow(matrix.getRow(i))){
				this.matrixOpt.setRow(j, matrix.getRow(i));
				j++;
			}
		}
		
	}



	public RealMatrix getMatrixOpt() {
		return matrixOpt;
	}

	private void removeNULLrows() {
		int index = this.findNull();
		this.matrixOpt = MatrixUtils.createRealMatrix(index,
				this.matrix.getColumnDimension());
		for (int i = 0; i < index; i++) {
			for (int j = 0; j < this.matrix.getColumnDimension(); j++) {
				this.matrixOpt.setEntry(i, j, this.matrix.getEntry(i, j));
			}
		}
	}

	private int findNull() {
		for (int i = 0; i < this.matrix.getRowDimension(); i++) {
			double[] row = this.matrix.getRow(i);
			for (int j = 0; j < row.length; j++) {
				if (row[j] != NULL)
					break;

				if (j == row.length - 1)
					return i;
			}
		}
		return -1;
	}

	private void repeatRemove() {
		for (int j = 0; j < this.matrix.getRowDimension(); j++) {
			double[] row = matrix.getRow(j);
			for (int i = this.tri.degree().intValue() - 1; i < matrix
					.getColumnDimension(); i++) {

				double cell = row[i];
				boolean found = false;
				if (cell != NULL) {
					for (int l = j + 1; l < this.max_row; l++) {
						double[] rowToCompare = matrix.getRow(l);
						double cellToCompare = rowToCompare[i];
						if (cellToCompare != NULL) {
							if (cellToCompare == cell) {
								rowToCompare[i] = NULL;
								row[i] = NULL;
								found = true;
							}
						}

						matrix.setRow(l, rowToCompare);
						if (found)
							break;
					}
				}
			}
			this.matrix.setRow(j, row);

		}

	}

	private int countXor() {
		int nextRow = this.actual_row + 2;
		double[] rowToWrite = matrix.getRow(nextRow);
		double[] row = matrix.getRow(0);
		for (int i = this.tri.degree().intValue() - 1; i < matrix
				.getColumnDimension(); i++) {
			int tempXor = 0;
			double cell = row[i];

			if (cell != NULL) {
				for (int l = 1; l < this.actual_row; l++) {
					double[] rowToCompare = matrix.getRow(l);
					double cellToCompare = rowToCompare[i];
					if (cellToCompare != NULL) {
						tempXor++;
					}
				}
			}
			rowToWrite[i] = tempXor;

		}
		matrix.setRow(nextRow, rowToWrite);

		double[] rowToRead = matrix.getRow(nextRow);
		int counter = 0;
		for (int i = this.tri.degree().intValue() - 1; i < rowToRead.length; i++) {
			int tx = (int) rowToRead[i];
			counter = counter + tx;
		}
		return counter;
	}

	private void reduction(int nr) {
		this.firstRow();
		this.extractExp();
		int h = 1;
		for (int j = 0; j < this.exp.size(); j++) {
			this.mountFirst(this.exp.get(j), h);
			h++;
		}
		this.actual_row = h;
		for (int i = 0; i < nr; i++) {
			this.reduceOthers();
		}

	}

	private void reduceOthers() {
		ArrayList<Integer> rowsToReduce = this.getNeedToReduce();
		for (int i = 0; i < rowsToReduce.size(); i++) {
			for (int j = 0; j < this.exp.size(); j++) {
				this.reduceRow(this.exp.get(j), rowsToReduce.get(i));

			}
			this.cleanReduced(rowsToReduce.get(i));
		}

	}

	private void cleanReduced(Integer index) {
		double[] rr = this.matrix.getRow(index);
		for (int j = 0; j < this.tri.degree().intValue() - 1; j++) {
			rr[j] = NULL;
		}
		this.matrix.setRow(index, rr);

	}

	private void reduceRow(Integer exp, Integer indexOfRow) {
		int index = this.max_size;
		ArrayList<Double> elements = new ArrayList<>();
		double[] toWrite = this.matrix.getRow(this.actual_row);

		for (int i = getIndex(indexOfRow); i < this.tri.degree().intValue() - 1; i++) {
			double element = this.matrix.getEntry(indexOfRow, i);
			elements.add(element);

		}
		Collections.sort(elements);

		for (int i = 0; i < elements.size(); i++) {
			toWrite[index - exp] = elements.get(i);
			index--;

		}
		this.matrix.setRow(this.actual_row, toWrite);
		this.actual_row++;

	}

	private int getIndex(Integer indexOfRow) {
		double[] row = this.matrix.getRow(indexOfRow);
		for (int i = 0; i < row.length; i++) {
			if (row[i] != NULL)
				return i;
		}
		return -1;
	}

	private ArrayList<Integer> getNeedToReduce() {
		ArrayList<Integer> indexOfRows = new ArrayList<Integer>();

		for (int i = 2; i < this.matrix.getRowDimension(); i++) {
			if (this.matrix.getEntry(i, this.tri.degree().intValue() - 1) != NULL) {
				indexOfRows.add(i);
			}

		}
		return indexOfRows;
	}

	private void mountFirst(Integer exp, Integer row) {
		int index = this.max_size;
		double[] r = this.matrix.getRow(row);
		ArrayList<Double> elements = new ArrayList<>();
		for (int i = 0; i < this.tri.degree().intValue() - 1; i++) {
			double element = this.matrix.getEntry(0, i);
			elements.add(element);

		}
		Collections.sort(elements);
		for (int i = 0; i < elements.size(); i++) {
			r[index - exp] = elements.get(i);
			index--;
		}
		this.matrix.setRow(row, r);

	}

	private void extractExp() {
		exp = new ArrayList<>();
		exp.add(0);
		exp.add(this.tri.getA().intValue());

		Collections.sort(exp);

	}

	private void firstRow() {
		int temp = this.max_size;
		for (int i = 0; i < this.matrix.getColumnDimension(); i++) {
			this.matrix.setEntry(0, i, temp);
			temp = temp - 1;
		}

	}

	private void generateMatrix(int nr) {
		this.matrix = MatrixUtils.createRealMatrix(this.max_row,
				this.max_size + 1);
		this.cleanMatrix();

	}

	private void cleanMatrix() {
		for (int j = 0; j < this.matrix.getRowDimension(); j++) {
			for (int i = 0; i < this.matrix.getColumnDimension(); i++) {
				this.matrix.setEntry(j, i, NULL);
			}
		}

	}

	private int calculateNR() {
		BigInteger bigMaxSize = (this.tri.degree()
				.multiply(new BigInteger("2"))).subtract(new BigInteger("2"));
		this.max_size = bigMaxSize.intValue();

		int nr = 2;
		int deg = (int) Math.floor((this.tri.degree().intValue() + 1) / 2);

		if (this.tri.getA().intValue() > deg) {
			nr = 2 * (this.tri.getA().intValue() + 1)
					- this.tri.degree().intValue();
		}
		return nr;
	}

	public void saveXLS() throws LimitExceededException {
		for (int i = 0; i < this.calculateNR(); i++)
			this.clean();

		this.optimize();
		if (this.matrix.getColumnDimension() <= 16384) {
			XLSWriter xlsWriter = new XLSWriter();
			xlsWriter.setFileName("Reduction_" + this.tri.degree().toString()
					+ "_" + this.tri.getA().toString() + ".xlsx");
			xlsWriter.save(this.matrix, this.tri, "Not Optimized");
			xlsWriter.save(this.matrixOpt, this.tri, "Optimized");

			xlsWriter.close();

		} else {
			throw new LimitExceededException(
					"The size of the columns is too big to create a xls file.");
		}
	}

	private void clean() {
		for (int i = 1; i < this.matrix.getRowDimension(); i++) {
			double[] row = this.matrix.getRow(i);
			boolean clean = this.isCleanRow(row);
			if (clean) {
				int nextNotEmpty = this.getNextNotEmpty(i);
				if (nextNotEmpty != NULL) {
					double[] rowNotEmpty = this.matrix.getRow(nextNotEmpty);
					this.matrix.setRow(i, rowNotEmpty);
					this.matrix.setRow(nextNotEmpty, row);
				}
			}
		}
	}

	private int getNextNotEmpty(int next) {
		for (int i = next; i < this.matrix.getRowDimension(); i++) {
			double[] row = this.matrix.getRow(i);
			if (!this.isCleanRow(row)) {
				return i;
			}
		}
		return (int) NULL;
	}

	private boolean isCleanRow(double[] row) {
		for (int i = 0; i < row.length; i++) {
			if (row[i] != NULL)
				return false;
		}
		return true;
	}

	public void printMatrix() {
		System.out.println();
		for (int j = 0; j < this.matrix.getRowDimension(); j++) {
			for (int i = 0; i < this.matrix.getColumnDimension(); i++) {
				double n = this.matrix.getEntry(j, i);
				System.out.print(n + " ");
			}
			System.out.println("");
		}

	}

	public String getFileName() {
		return fileName;
	}

}
