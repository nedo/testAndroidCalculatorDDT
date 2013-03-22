package com.testcalculator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
 
public class Utility extends Activity{
	
  Workbook wb;
  WritableWorkbook copy ;
  WritableSheet sheet ;
  Boolean FuntionResult = false;
  Cell rowData[] = null;
  int rowCount = '0';
  WorkbookSettings ws = null;
  Workbook workbook = null;
  Sheet s = null;
  
  String[] City, CheckInDate, CheckOutDate, NoOfGuests, NoOfRooms;
	
	public Utility()
	{
	//	getTestDataFile();
		//TestResults = new String[rowCount];
		
			for (int i = 1; i < rowCount; i ++ )
			{}
		
	}
	public void getTestData()
	{
		Log.i("DDTUtility", "Into Utillitiy.....");
		 FileInputStream fs = null;
			try {	
				File file = getBaseContext().getFileStreamPath("/sdcard/TestData.csv");
				if(file.exists())
					Log.i("DDTUtility", "file exists.....");
				else Log.i("DDTUtility", "file not exists.....");
					
				Log.i("DDTUtility", "starting reading.....");
				fs = openFileInput("/sdcard/TestData.csv");
				Log.i("DDTUtility", "done reading.....");
				readTestData(fs);
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				try {
					fs.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}	
	}

	public void readTestData(InputStream fileInputStream) throws Exception {
		ws = null;
		workbook = null;
		s = null;
		
		int columnCount = '0';
		int totalSheet = 0;
		String firstSheet = "TestCases";
		String secondSheet = "TestScenarios";

		try {
			ws = new WorkbookSettings();
			ws.setLocale(new Locale("en", "EN"));
			workbook = Workbook.getWorkbook(fileInputStream, ws);

			totalSheet = workbook.getNumberOfSheets();
			if(totalSheet > 0) {
				 
					if (!workbook.getSheet(0).getName().equals(firstSheet))
					{
						System.out.println ("contents are not fine");
					}
					
					if (!workbook.getSheet(1).getName().equals(secondSheet))
					{
						System.out.println ("contents are not fine");
					}			 
				}
			
			else 
				System.out.println ("There is not any sheet available.");
		
			s = workbook.getSheet(1);
			
			rowCount = s.getRows();
			
			City = new String[rowCount]; 
			CheckInDate = new String[rowCount];
			CheckOutDate = new String[rowCount];
			NoOfGuests = new String[rowCount];
			NoOfRooms = new String[rowCount];
			
			columnCount = s.getColumns();
			rowData = s.getRow(0);

			if (rowData[0].getContents().length() != 0)
			{
				for (int i =1; i < rowCount; i ++)
				{
				
					rowData = s.getRow(i);
					
					if (rowData[7].getContents().equals("Yes"))
					{
						System.out.println("Executed: " + rowData[1].getContents());
						City[i] = rowData[2].getContents().toString();
						CheckInDate[i] = rowData[3].getContents().toString();
						CheckOutDate[i] = rowData[4].getContents().toString();
						NoOfGuests[i] = rowData[5].getContents().toString();
						NoOfRooms[i] = rowData[6].getContents().toString();
					}
					else 
					{
						System.out.println("We will skip "+rowData[1].getContents());
					}
				}
				System.out.println("Success");

			}			
			workbook.close();			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		}
	}

	public void writeResults(String[] results) 
	{
		FileOutputStream outputStream = null;   
		
		try{
			outputStream = openFileOutput("TestResultData.txt", Context.MODE_WORLD_READABLE);
			
			for (int i=0; i <results.length; i ++)
			{
			outputStream.write(results[1].toString().getBytes());
			}
			
			}catch (IOException e) {
				e.printStackTrace();
			}finally {
					if (outputStream != null) {
		            try {
		                outputStream.flush();
		                outputStream.close();
		            } catch (IOException e) {
		            	e.printStackTrace();}
						}
			}
		}
	

	}