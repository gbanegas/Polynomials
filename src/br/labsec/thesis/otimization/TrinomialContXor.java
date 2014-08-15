package br.labsec.thesis.otimization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import br.labsec.thesis.polynomials.Trinomial;

public class TrinomialContXor {

	private Trinomial trinomial;
	private HSSFWorkbook workbook;
	private HSSFSheet sheet;
	public TrinomialContXor(Trinomial trinomial) {

		this.trinomial = trinomial;
		workbook = new HSSFWorkbook();
		sheet = workbook.createSheet("Sheet_1");
		this.optmize();
	}

	private void optmize() {
		BigInteger max_size = (this.trinomial.degree().multiply(new BigInteger(
				"2"))).subtract(new BigInteger("2"));
		BigInteger i;
		generateFirstROW(max_size);
		
		ArrayList<BigInteger> elements_pass = new ArrayList<BigInteger>();
		
		i = new BigInteger("0");
		while (i.compareTo(this.trinomial.degree()) == -1) {
			
			
			i = i.add(BigInteger.ONE);
		}
		
		
		saveXls();
	}

	private void generateFirstROW(BigInteger max_size) {
		Row row = sheet.createRow(0);
		BigInteger i = new BigInteger("0");
		
		int cellnum = max_size.intValue();
		BigInteger temp = max_size.add(BigInteger.ONE);
		while(i.compareTo(temp) != 0)
		{
			Cell cell = row.createCell(cellnum--);
			cell.setCellValue(i.intValue());
			i = i.add(BigInteger.ONE);
		}
	}

	private void saveXls() {
		try {
		    FileOutputStream out = 
		            new FileOutputStream(new File("Teste.xls"));
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
