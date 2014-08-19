package br.labsec.thesis.otimization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import javax.naming.LimitExceededException;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import br.labsec.thesis.polynomials.Trinomial;

public class TrinomialContMatrix implements Runnable {

	private static final double NULL = -1.0;
	private Trinomial tri;
	private int max_size;
	private int max_row;
	private BigInteger bigMaxSize;

	private int m_row;
	private RealMatrix matrix;
	private int totalXor;
	private String fName;

	public TrinomialContMatrix(Trinomial toCalculate) {
		this.tri = toCalculate;
		this.m_row = 1;
	}

	@Override
	public void run() {
		int nr = calculateNR();
		this.max_row = (nr * 3) + 5;
		this.generateFirstRowAndMatrix();
		// ------------------------------------------------------
		this.optmize(nr);

	}

	private void optmize(int nr) {

		firstReduction();
		nr = nr - 1;
		for (int i = 0; i < nr; i++) {
			this.othersReductions(i + 1);
		}

		this.repeatRemove();
		this.countXor();

	}

	private void othersReductions(int interaction) {
		double[] row = matrix.getRow(interaction - 1);
		double[] rowToReduce = matrix.getRow(interaction + 1);
		ArrayList<Integer> degreesToReduce = new ArrayList<Integer>();
		degreesToReduce.add(0);
		degreesToReduce.add(this.tri.getA().intValue());
		Collections.sort(degreesToReduce);
		HashMap<Double, Double> expWhereToSave = new HashMap<Double, Double>();
		for (int j = 0; j < degreesToReduce.size(); j++) {
			int exp = degreesToReduce.get(j);
			for (int i = 0; i < this.tri.degree().intValue(); i++) {
				if (rowToReduce[i] != -1) {
					expWhereToSave.put(row[i], rowToReduce[i]);
				}
			}
			
			this.reduce(expWhereToSave, exp);
			int isNeed =  isNeededColum(interaction + 1);
			if(isNeed != NULL)
			{
				double[] rowTo = matrix.getRow(interaction + 1);
				addRows(rowTo, isNeed);
			}
			this.removeAllRepeated();
			//this.printMatrix();
		}
		
	}


	private void addRows(double[] row, int isNeed) {
		int rowTemp1 = this.defineRow();
		double[] toWrite = this.matrix.getRow(rowTemp1);
		double[] toRead = this.matrix.getRow(0);
		for(int i = isNeed; i < toRead.length;i++)
		{
			if(toRead[i] >= this.tri.degree().intValue())
			{
				toWrite[i] = toRead[i];
			}
		}
		 this.matrix.setRow(rowTemp1,toWrite);
		
	}

	private int isNeededColum(int rowToGet) {
		double[] rowToReduce = matrix.getRow(rowToGet);
		for(int i = 0; i < rowToReduce.length; i++)
		{
			double value = rowToReduce[i];
			if(value != NULL)
			{
				if(this.matrix.getRow(0)[i] >= this.tri.degree().intValue())
				{
					return i;
				}
			}
		}
		return (int) NULL;
	}

	private void reduce(HashMap<Double, Double> expWhereToSave, int exp) {

		int rowTemp1 = this.defineRow();
		int rowTemp2 = this.defineRow();
		rowTemp2++;

		double[] rowToWrite = matrix.getRow(rowTemp1);
		double[] rr = matrix.getRow(rowTemp2);
		Set<Double> set = expWhereToSave.keySet();
		int j = this.max_size - exp;
		ArrayList<Double> keys = new ArrayList<>();
		ArrayList<Double> values = new ArrayList<>();
		for (Double key : set) {
			keys.add(key);
			values.add(expWhereToSave.get(key));
		}
		Collections.sort(keys);
		Collections.sort(values);
		for (int h = 0; h < keys.size(); h++) {
			if (keys.get(h) > this.tri.degree().intValue() - 1) {
				rowToWrite[j] = keys.get(h);
				rr[j] = values.get(h);
				j--;
			}
		}
		this.matrix.setRow(rowTemp1, rowToWrite);
		this.matrix.setRow(rowTemp2, rr);
		rowTemp1++;
		rowTemp2++;
		this.m_row = rowTemp2;
		//this.removeAllRepeated();

	}

	private void removeAllRepeated() {
		for (int j = 0; j < this.matrix.getRowDimension(); j++) {
			double[] row = matrix.getRow(j);
			for (int i = 0; i < matrix.getColumnDimension(); i++) {

				double cell = row[i];
				if (cell != NULL) {
					for (int l = j + 1; l < this.m_row; l++) {
						double[] rowToCompare = matrix.getRow(l);
						double cellToCompare = rowToCompare[i];
						if (cellToCompare != NULL) {
							if (cellToCompare == cell) {
								rowToCompare[i] = NULL;
								row[i] = NULL;
							}
						}
						matrix.setRow(l, rowToCompare);
					}
				}

			}
			matrix.setRow(j, row);
		}

	}

	private int defineRow() {
		for (int i = 0; i < this.matrix.getRowDimension(); i++) {
			double[] row = this.matrix.getRow(i);
			if (isClean(row)) {
				return i;
			}
		}
		return 0;
	}

	private boolean isClean(double[] row) {
		for (int i = 0; i < row.length; i++) {
			if (row[i] != NULL)
				return false;
		}
		return true;
	}

	private void reductionBy(int exp, BigInteger max_size) {
		BigInteger counter = this.tri.degree();
		HashMap<BigInteger, BigInteger> expWhereToSave = new HashMap<BigInteger, BigInteger>();
		BigInteger t = max_size.add(BigInteger.ONE);
		int m_1 = this.tri.degree().intValue();
		BigInteger m1 = new BigInteger(Integer.toString(m_1));

		BigInteger realKey = max_size;
		while (counter.compareTo(t) == -1) {
			BigInteger toPlace = (counter.subtract(this.tri.degree()))
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

	private void generateFirstRowAndMatrix() {
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
			System.out.println("");
		}

	}

	public void saveXLS() throws LimitExceededException {

		if (this.matrix.getColumnDimension() <= 16384) {
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Sheet_1");

			for (int i = 0; i < this.matrix.getRowDimension(); i++) {
				Row row = sheet.createRow(i);
				for (int j = 0; j < this.matrix.getColumnDimension(); j++) {
					Cell cell = row.createCell(j);
					double value = this.matrix.getEntry(i, j);
					if (value == NULL)
						cell.setCellType(Cell.CELL_TYPE_BLANK);
					else
						cell.setCellValue(value);

				}
			}

			try {
				String fileName = "Reduction_" + this.tri.degree().toString()
						+ "_" + this.tri.getA().toString() + ".xls";
				FileOutputStream out = new FileOutputStream(new File(fileName));
				workbook.write(out);
				out.close();
				this.setfName(fileName);
				System.out.println("Excel written successfully..");

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new LimitExceededException(
					"The size of the columns is too big to create a xls file.");
		}
	}

	private void countXor() {

		int nextRow = this.m_row + 1;
		double[] rowToWrite = matrix.getRow(nextRow);
		double[] row = matrix.getRow(0);

		for (int i = this.tri.degree().intValue() - 1; i < matrix
				.getColumnDimension(); i++) {
			int tempXor = 0;
			double cell = row[i];

			if (cell != NULL) {
				for (int l = 1; l < this.m_row; l++) {
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
		this.totalXor = counter;

	}

	private void repeatRemove() {

		for (int j = 0; j < this.m_row; j++) {
			double[] row = matrix.getRow(j);
			for (int i = this.tri.degree().intValue() - 1; i < matrix
					.getColumnDimension(); i++) {

				double cell = row[i];
				if (cell != NULL) {
					for (int l = j + 1; l < this.m_row; l++) {
						double[] rowToCompare = matrix.getRow(l);
						double cellToCompare = rowToCompare[i];
						if (cellToCompare != NULL) {
							if (cellToCompare == cell) {
								rowToCompare[i] = NULL;
								row[i] = NULL;
							}
						}
						matrix.setRow(l, rowToCompare);
					}
				}

			}
			matrix.setRow(j, row);
		}

	}

	private int calculateNR() {
		BigInteger bigMaxSize = (this.tri.degree()
				.multiply(new BigInteger("2"))).subtract(new BigInteger("2"));
		this.bigMaxSize = bigMaxSize;
		this.max_size = bigMaxSize.intValue();

		int nr = 2;
		int deg = (int) Math.floor((this.tri.degree().intValue() + 1) / 2);

		if (this.tri.getA().intValue() > deg) {
			nr = 2 * (this.tri.getA().intValue() + 1)
					- this.tri.degree().intValue();
		}
		return nr;
	}

	private void firstReduction() {
		this.reductionBy(0, bigMaxSize);
		this.reductionBy(this.tri.getA().intValue(), bigMaxSize);
	}

	public int getTotalXor() {
		return totalXor;
	}

	private void setfName(String fileName) {
		this.fName = fileName;

	}

	public String getFileName() {
		return this.fName;
	}

}
