package br.labsec.thesis.otimization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import br.labsec.thesis.polynomials.Trinomial;

public class TrinomialContXor {

	private Trinomial trinomial;
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;

	private int m_row = 1;

	public TrinomialContXor(Trinomial trinomial) {

		this.trinomial = trinomial;
		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet("Sheet_1");
		this.optmize();
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
		nr = nr-1;
		for (int i = 0; i < nr; i++) {
			
			if (t % 2 == 0)
				this.othersReductions(this.trinomial.getA().intValue(), i + 2);
			else
				this.othersReductions(0, i + 2);
			t++;
		}

		saveXls();
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

		for (int i = interaction-1; i < size; i++) {
			Row rowToWrite = sheet.createRow(row_temp++);
			Row row = sheet.getRow(i);
			int j = 0;
			for (int h = 0; h < sheet.getRow(0).getPhysicalNumberOfCells();h++ ) {
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
			FileOutputStream out = new FileOutputStream(new File("Teste.xls"));
			workbook.write(out);
			out.close();
			System.out.println("Excel written successfully..");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
