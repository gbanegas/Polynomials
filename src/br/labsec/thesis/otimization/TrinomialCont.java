package br.labsec.thesis.otimization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;

import javax.naming.LimitExceededException;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import br.labsec.thesis.polynomials.Trinomial;

public class TrinomialCont {

	private static final double NULL = -1;
	private RealMatrix matrix;
	private Trinomial tri;
	private int max_size;
	private int max_row;
	private int actual_row;

	private ArrayList<Integer> exp;
	private String fileName;

	public TrinomialCont() {

	}

	public int calculate(Trinomial tri) {
		actual_row = 1;
		this.tri = tri;
		int nr = this.calculateNR();
		this.max_row = (nr * 6) + 5;
		this.generateMatrix(nr);
		this.reduction(nr);

		this.repeatRemove();

		return countXor();
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
		for (int i = 1; i < nr; i++) {
			this.madeOthersReductions(i);
		}
		if(nr > 2)
			this.addColums();

	}

	private int countXor() {

		int nextRow = this.actual_row + 1;
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
						if(found)
							break;
					}
				}
			}
			this.matrix.setRow(j, row);

		}

	}

	private void madeOthersReductions(int actual_nr) {
		int index_rowToGet = this.actual_row - actual_nr;
		double[] toReduce = this.matrix.getRow(index_rowToGet);
		this.mountOthers(toReduce, index_rowToGet);
	}

	private void mountOthers(double[] toReduce, int index_rowToGet) {

		int temp = this.actual_row + 1;
		for (int j = 0; j < this.exp.size(); j++) {
			int index = this.max_size;
			ArrayList<Double> elements = new ArrayList<>();
			ArrayList<Double> elements_2 = new ArrayList<>();
			double[] toSave = this.matrix.getRow(this.actual_row);
			double[] toSave_2 = this.matrix.getRow(temp);

			for (int i = 0; i < this.tri.degree().intValue() - 1; i++) {
				double reduced = toReduce[i];
				if (reduced != NULL) {
					if (reduced >= this.tri.degree().intValue()) {
						elements_2
								.add(this.matrix.getRow(index_rowToGet - 2)[i]);
						elements.add(toReduce[i]);

					}
				}
			}
			Collections.sort(elements);
			Collections.sort(elements_2);
			for (int i = 0; i < elements.size(); i++) {
				toSave[index - this.exp.get(j)] = elements.get(i);
				toSave_2[index - this.exp.get(j)] = elements_2.get(i);
				index--;
			}

			this.matrix.setRow(this.actual_row, toSave);
			this.matrix.setRow(temp, toSave_2);
			temp++;
			this.actual_row = temp + 1;
		}
		this.actual_row++;

	}

	private void addColums() {
		int index_row = findTheRowLessSize();
		int index_colum = findColum(index_row);
		for (int i = 0; i < index_row; i++) {
			double[] rowToGet = this.matrix.getRow(i);
			if (rowToGet[index_colum] != NULL) {
				ArrayList<Double> elements = new ArrayList<>();

				for (int j = index_colum; j < this.tri.degree().intValue() - 1; j++) {
					elements.add(rowToGet[j]);
				}

				Collections.sort(elements);

				for (int j = 0; j < this.exp.size(); j++) {
					int index = this.max_size;
					double[] toSave = this.matrix.getRow(this.actual_row);
					for (int h = 0; h < elements.size(); h++) {
						toSave[index - this.exp.get(j)] = elements.get(h);
						index--;
					}

					this.matrix.setRow(this.actual_row, toSave);
					this.actual_row++;
				}
			}
		}
		// this.printMatrix();

	}

	private int findColum(int index_row) {
		double[] row = this.matrix.getRow(index_row);
		for (int i = 0; i < this.tri.degree().intValue() - 1; i++) {
			double element = row[i];
			if (element != NULL) {
				return i;
			}
		}
		return (int) NULL;
	}

	private int findTheRowLessSize() {
		int r = -1;
		int temp = 99999999;
		for (int i = this.actual_row; i > 0; i--) {
			double[] row = this.matrix.getRow(i);
			int count = 0;
			boolean isAllNull = true;
			for (int j = 0; j < this.tri.degree().intValue() - 1; j++) {
				double element = row[j];
				if (element != NULL) {
					count++;
					isAllNull = false;
				}
			}
			if (!isAllNull) {
				if (count < temp) {
					r = i;
					temp = count;
				}
				isAllNull = true;
			}

		}
		return r;
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
		this.matrix = MatrixUtils.createRealMatrix(this.max_row,this.max_size + 1);
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
				this.fileName = fileName;
				workbook.write(out);
				out.close();
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
		// TODO Auto-generated method stub
		return fileName;
	}
}