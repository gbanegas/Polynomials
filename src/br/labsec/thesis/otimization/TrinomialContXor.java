package br.labsec.thesis.otimization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import br.labsec.thesis.polynomials.Trinomial;

public class TrinomialContXor extends Thread {

	private Trinomial trinomial;
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;

	private int m_row = 1;

	private int totalXor;

	private String fName;
	public TrinomialContXor(Trinomial trinomial) {

		this.trinomial = trinomial;
		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet("Sheet_1");
		
	}
	@Override
	public void run(){
		this.optmize();
	}

	public int getTotalXor() {
		
		return totalXor;
	}

	private void optmize() {
		BigInteger max_size = (this.trinomial.degree().multiply(new BigInteger(
				"2"))).subtract(new BigInteger("2"));
		generateFirstROW(max_size);
		int nr = 2;
		int deg = (int) Math
				.floor((this.trinomial.degree().intValue() + 1) / 2);

		if (this.trinomial.getA().intValue() > deg) {
			nr = 2 * (this.trinomial.getA().intValue() + 1)
					- this.trinomial.degree().intValue();
		}

		this.reductionBy(0, max_size);
		this.reductionBy(this.trinomial.getA().intValue(), max_size);
		int t = 1;
		nr = nr - 1;
		for (int i = 0; i < nr; i++) {

			if (t % 2 == 0)
				this.othersReductions(this.trinomial.getA().intValue(), i + 2);
			else
				this.othersReductions(0, i + 2);
			t++;
		}

		this.repeatRemove();

		this.countXor();
		saveXls();
	}

	private void countXor() {

		int nextRow = this.m_row + 2;
		Row rowToWrite = sheet.createRow(nextRow);

		Row row = sheet.getRow(0);

		for (int i = this.trinomial.degree().intValue() - 1; i < sheet.getRow(0).getPhysicalNumberOfCells(); i++) {
			int tempXor = 1;
			Cell cell = row.getCell(i);

			if (cell != null) {
				for (int l = 1; l < this.m_row; l++) {
					Row rowToCompare = sheet.getRow(l);
					Cell cellToCompare = rowToCompare.getCell(i);
					if (cellToCompare != null) {
						tempXor++;
					}
				}
			}

			Cell c = rowToWrite.createCell(i);
			tempXor = tempXor-1;
			c.setCellValue(tempXor);

		}
		
		Row rowToReed = sheet.getRow(nextRow);
		int counter = 0;
		for(Cell xo: rowToReed)
		{
			int tx = (int)xo.getNumericCellValue();
			counter = counter +tx;
		}
		this.totalXor = counter;
		Cell c = rowToWrite.createCell(sheet.getRow(0)
				.getPhysicalNumberOfCells());
		String columnLetter = CellReference
				.convertNumToColString(this.trinomial.degree().intValue() - 1);
		String columnLetter_ = CellReference.convertNumToColString(sheet
				.getRow(0).getPhysicalNumberOfCells() - 1);
		nextRow++;
		String format = "SUM(" + columnLetter + nextRow + ":" + columnLetter_+ nextRow + ")";
		c.setCellFormula(format);
		c.setCellType(Cell.CELL_TYPE_FORMULA);
		
	

	}

	private void repeatRemove() {

		for (int j = 0; j < this.m_row; j++) {
			for (int i = this.trinomial.degree().intValue() - 1; i < sheet
					.getRow(0).getPhysicalNumberOfCells(); i++) {
				Row row = sheet.getRow(j);
				Cell cell = row.getCell(i);
				if (cell != null) {
					for (int l = j + 1; l < this.m_row; l++) {
						Row rowToCompare = sheet.getRow(l);
						Cell cellToCompare = rowToCompare.getCell(i);
						if (cellToCompare != null) {
							if (cellToCompare.getNumericCellValue() == cell
									.getNumericCellValue()) {
								cell.setCellType(Cell.CELL_TYPE_BLANK);
								cellToCompare.setCellType(Cell.CELL_TYPE_BLANK);
							}
						}
					}
				}

			}
		}

	}

	private void othersReductions(int exp, int interaction) {
		Row row = sheet.getRow(interaction - 2);
		Row rowToReduce = sheet.getRow(interaction);
		HashMap<Integer, Integer> expWhereToSave = new HashMap<Integer, Integer>();
		for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
			if (row.getCell(i) != null && rowToReduce.getCell(i) != null) {
				boolean hasNumbers = (row.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC)
						&& (rowToReduce.getCell(i).getCellType() == Cell.CELL_TYPE_NUMERIC);

				boolean validy = hasNumbers
						&& (row.getCell(i).getNumericCellValue() >= this.trinomial
								.degree().intValue());

				if (validy) {
					Cell cel = rowToReduce.getCell(i);
					Integer number = (int) (cel.getNumericCellValue());
					expWhereToSave.put((int) row.getCell(i)
							.getNumericCellValue(), number);
				}
			}
		}

		this.mount(expWhereToSave, interaction, exp);
	}

	private void mount(HashMap<Integer, Integer> expWhereToSave,
			int interaction, int exp) {
		int row_temp = this.m_row;
		Integer size = Integer.valueOf(this.m_row);

		for (int i = interaction - 1; i < size; i++) {
			Row rowToWrite = sheet.createRow(row_temp++);
			Row row = sheet.getRow(i);
			int j = 0;
			for (int h = 0; h < sheet.getRow(0).getPhysicalNumberOfCells(); h++) {
				Cell cell = row.getCell(h);

				if (cell != null) {
					Integer number = (int) (cell.getNumericCellValue());

					if (expWhereToSave.containsKey(((number)))) {

						Cell cellToWrite = rowToWrite.createCell(j);
						cellToWrite.setCellValue(expWhereToSave.get(number));
					}
				}
				j++;
			}
		}
		this.m_row = row_temp;

	}

	private void reductionBy(int exp, BigInteger max_size) {
		BigInteger counter = this.trinomial.degree();
		HashMap<BigInteger, BigInteger> expWhereToSave = new HashMap<BigInteger, BigInteger>();
		BigInteger t = max_size.add(BigInteger.ONE);
		int m_1 = this.trinomial.degree().intValue();
		BigInteger m1 = new BigInteger(Integer.toString(m_1));

		BigInteger realKey = max_size;
		while (counter.compareTo(t) == -1) {
			BigInteger toPlace = (counter.subtract(this.trinomial.degree()))
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
		Row row = sheet.createRow(m_row++);

		for (BigInteger key : expWhereToSave.keySet()) {
			BigInteger value = expWhereToSave.get(key);
			Cell cell = row.createCell(value.intValue());
			cell.setCellValue(key.intValue());
			// System.out.println(key + " = " + value);
		}

		/*
		 * int cellnum = max_size.intValue(); BigInteger temp =
		 * max_size.add(BigInteger.ONE); while (i.compareTo(temp) != 0) { Cell
		 * cell = row.createCell(cellnum--); cell.setCellValue(i.intValue()); i
		 * = i.add(BigInteger.ONE); }
		 */

	}

	private void generateFirstROW(BigInteger max_size) {
		Row row = sheet.createRow(0);
		BigInteger i = new BigInteger("0");

		int cellnum = max_size.intValue();
		BigInteger temp = max_size.add(BigInteger.ONE);
		while (i.compareTo(temp) != 0) {
			Cell cell = row.createCell(cellnum--);
			cell.setCellValue(i.intValue());
			i = i.add(BigInteger.ONE);
		}
	}

	private void saveXls() {
		try {
			String fileName = "Reduction_" + this.trinomial.degree().toString()+"_"+this.trinomial.getA().toString()+".xls";
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
	}

	public String getfName() {
		return fName;
	}

	private void setfName(String fName) {
		this.fName = fName;
	}

}
