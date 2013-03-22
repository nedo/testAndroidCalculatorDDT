package com.testcalculator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import junit.framework.TestResult;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import com.jayway.android.robotium.solo.Solo;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

@SuppressWarnings("unchecked")
public class TestApk extends ActivityInstrumentationTestCase2{

private static final String TARGET_PACKAGE_ID="com.calculator";
private static final String LAUNCHER_ACTIVITY_FULL_CLASSNAME="com.calculator.Main";
private static Class launcherActivityClass;
Utility myUtility = new Utility();

Workbook wb;
WritableWorkbook copy ;
WritableSheet sheet ;
Boolean FuntionResult = false;
Cell rowData[] = null;
int rowCount = '0';
WorkbookSettings ws = null;
Workbook workbook = null;
Sheet s = null;
Boolean status;
String[] TestResults;

String[] vFirstValue, vSecondValue, vExpectedValue, vExecute; 

static{
	try
	{
		launcherActivityClass = Class.forName(LAUNCHER_ACTIVITY_FULL_CLASSNAME);
		} catch (ClassNotFoundException e){
		throw new RuntimeException(e);
	}
}

public TestApk()throws ClassNotFoundException{
super(TARGET_PACKAGE_ID,launcherActivityClass);
}

private Solo solo;

@Override
protected void setUp() throws Exception
{
	solo = new Solo(getInstrumentation(),getActivity());
	getTestData();
}

public void testDisplayBlackBox() {
	
	for(int i =1; i<rowCount; i++)
	{
		//Enter first integer/decimal value from file
		solo.clearEditText(0);
		solo.enterText(0, vFirstValue[i].toString());
		
		//Enter second integer/decimal value from file
		solo.clearEditText(1);
		solo.enterText(1, vSecondValue[i].toString());
		
		//Click on Multiply button
		solo.clickOnButton("Multiply");
		
		//Verify that resultant with expected value
		TestResults[i] = vExpectedValue[i].toString();
		
		assertTrue(solo.searchText(vExpectedValue[i].toString()));
	}
	writeResults(TestResults);
}

//-------------------------------Work with TestData ---------------------------------------//
public void getTestData()
{
	FileInputStream fs = null;
	try {		
			fs = getActivity().openFileInput("TestData.csv");	
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
		TestResults = new String[rowCount];
		vFirstValue = new String[rowCount]; 
		vSecondValue = new String[rowCount];
		vExpectedValue = new String[rowCount];
		vExecute = new String[rowCount];
		
		columnCount = s.getColumns();
		rowData = s.getRow(0);

		if (rowData[0].getContents().length() != 0)
		{
			for (int i =1; i < rowCount; i ++)
			{
			
				rowData = s.getRow(i);
				if (rowData[0].getContents().equals("TC1")) 
				{
					
						if (rowData[5].getContents().equals("Yes"))
						{
							System.out.println("Will Execute: " + rowData[1].getContents());
							vFirstValue[i] = rowData[2].getContents().toString();
							vSecondValue[i] = rowData[3].getContents().toString();
							vExpectedValue[i] = rowData[4].getContents().toString();
							vExecute[i] = rowData[5].getContents().toString();
						}
						else 
						{
							System.out.println("We will skip "+rowData[1].getContents());
						}
				}
			//	System.out.println("Read Data is "+vFirstValue[i]+" "+vSecondValue[i]+" "+vExpectedValue[i]+" "+vExecute[i]);	
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
		outputStream = getActivity().openFileOutput("TestResultData.txt", Context.MODE_WORLD_READABLE);
		
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



@Override
public void tearDown() throws Exception {
solo.finishOpenedActivities();
}

}