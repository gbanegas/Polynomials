package br.labsec.thesis.otimization;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


public class Main {

	public static void main(String[] args) {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Sample sheet");
		 
	

		HashMap data = new HashMap();
		data.put("1", new Object[] {"Emp No.", "Name", "Salary"});
		data.put("2", new Object[] {"1d", "John", "1500000d"});
		data.put("3", new Object[] {"2d", "Sam", "800000d"});
		data.put("4", new Object[] {"3d", "Dean", "700000d"});
		 
		Set keyset = data.keySet();
		int rownum = 0;
		for (Iterator it = keyset.iterator(); it.hasNext(); ) {
			String key = (String)it.next();
		    Row row = sheet.createRow(rownum++);
		    Object[] objArr = (Object[])data.get(key);
		    int cellnum = 0;
		    for (int i = 0; i < objArr.length; i++) {
		    	Object obj = objArr[i];
		        Cell cell = row.createCell(cellnum++);
		        if(obj instanceof Date) 
		            cell.setCellValue((Date)obj);
		        else if(obj instanceof Boolean){
		        	Boolean b = (Boolean) obj;
		            cell.setCellValue(b.booleanValue());
		        }
		        else if(obj instanceof String){
		            cell.setCellValue((String)obj);
		        }
		        else if(obj instanceof Double){
		        	Double d = (Double)obj;
		            cell.setCellValue(d.doubleValue());
		        }
		    }
		}
		 
		try {
		    FileOutputStream out = 
		            new FileOutputStream(new File("new.xls"));
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
