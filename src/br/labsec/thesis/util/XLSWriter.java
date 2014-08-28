package br.labsec.thesis.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import br.labsec.thesis.polynomials.Polynomial;

public class XLSWriter {
	String fileName;
	SXSSFWorkbook workbook;
	private static final double NULL = -1;
	public XLSWriter() {
		workbook = new SXSSFWorkbook(-1);
	}
	
	public void save(RealMatrix matrix, Polynomial p, String sheetName)
	{
		
		Sheet sheet = workbook.createSheet(sheetName);

		for (int i = 0; i < matrix.getRowDimension(); i++) {
			Row row = sheet.createRow(i);
			int h = 0;
			for (int j = 0; j < matrix.getColumnDimension(); j++) {
				Cell cell = row.createCell(h);
				double value = matrix.getEntry(i, j);
				if (value == NULL)
					cell.setCellType(Cell.CELL_TYPE_BLANK);
				else
					cell.setCellValue(value);
				h++;
			}
		}

	
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
		
	}

	public void close() {
		
		try {
			
			FileOutputStream out = new FileOutputStream(new File(fileName));
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
