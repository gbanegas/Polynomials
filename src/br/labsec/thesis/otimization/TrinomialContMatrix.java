package br.labsec.thesis.otimization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;

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
		this.max_row = (nr * 3) +5;

		this.generateFirstRowAndMatrix();

		this.optmize(nr);

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

	public int getTotalXor() {
		return totalXor;
	}

	private void optmize(int nr) {

		this.reductionBy(0, bigMaxSize);
		this.reductionBy(this.tri.getA().intValue(), bigMaxSize);
		nr = nr - 1;
		for (int i = 0; i < nr; i++) {
			this.othersReductions(i+1);
		}

		this.repeatRemove();

		this.countXor();

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
		for (int i =this.tri.degree().intValue()-1; i < rowToRead.length;i++) {
			int tx = (int) rowToRead[i];
			counter = counter + tx;
		}
		this.totalXor = counter;

	}

	private void repeatRemove() {

		for (int j = 0; j < this.m_row; j++) {
			double[] row = matrix.getRow(j);
			for (int i = this.tri.degree().intValue() - 1; i < matrix.getColumnDimension(); i++) {

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

	private void othersReductions(int interaction) {
		//TODO:refactor the method
		double[] row = matrix.getRow(interaction-1);
		double[] rowToReduce = matrix.getRow(interaction+1);

		HashMap<Double, Double> expWhereToSave = new HashMap<Double, Double>();
		for (int i = 0; i < matrix.getColumnDimension(); i++) {
			if (row[i] != NULL && rowToReduce[i] != NULL) {
				boolean validy = (row[i] >= this.tri.degree().doubleValue());

				if (validy) {
					expWhereToSave.put(row[i], rowToReduce[i]);
				}
			}
		}

		this.mount(expWhereToSave, interaction);
	}

	private void mount(HashMap<Double, Double> expWhereToSave, int interaction) {
		int rowTemp1 = this.m_row;
		Integer size = Integer.valueOf(this.m_row);

		
		boolean changed = false;
		for (int i = size-2; i < size; i++) {
			double[] rowToWrite = matrix.getRow(rowTemp1);
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
			if(changed){
				this.matrix.setRow(rowTemp1, rowToWrite);
				rowTemp1++;
				changed=false;
			}
		}
		this.m_row = rowTemp1;

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
			System.out.println();
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
					if(value == NULL)
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

	private void setfName(String fileName) {
		this.fName = fileName;

	}

	public String getFileName() {
		return this.fName;
	}

}
