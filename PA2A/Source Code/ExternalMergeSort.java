import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ExternalMergeSort {
	private static FileWriter writer=null;
	private static HashMap<String, File> globalTempFilesMap = new HashMap<>();
	static HashMap<String, File> globalFinalOutputMap = new HashMap<>();
	private static HashMap<String, File> thread1klevelFilesMap = new HashMap<>();
	private static HashMap<String, File> thread2klevelFilesMap = new HashMap<>();
	private static HashMap<String, File> thread3klevelFilesMap = new HashMap<>();
	public static int threadParam;
	public static File preFinalOutput= null;
	
	
	private static ArrayList<String> inputArray = new ArrayList<String>();
	
	
	public static void createTmpInputFiles(String inputFileName) {
		try {
			File file = new File(inputFileName);
			
			FileInputStream fileInputStream = null;
			InputStreamReader inputStreamReader = null;
			BufferedReader bufferedReader = null;
			FileWriter fileWriter = null;
			BufferedWriter bufferedWriter = null;
			
			String line;
			int fileNameCntr=1;
			
			fileInputStream = new FileInputStream(file);
			inputStreamReader = new InputStreamReader(fileInputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			
			File tmpfile=null;
			tmpfile = new File("/tmp/TempInput"+fileNameCntr); //for cluster
			//tmpfile = new File("TempInput"+fileNameCntr);  //for local
			
			fileWriter = new FileWriter(tmpfile);
			bufferedWriter = new BufferedWriter(fileWriter);
			
			long lineCountr=0;
			while ((line = bufferedReader.readLine()) != null) {
				lineCountr++;
				bufferedWriter.write(line+"\r\n");	
			//2000000 for cluster, 2 for local
				if(lineCountr==2000000) {
					lineCountr=0;
					bufferedWriter.flush();
					globalTempFilesMap.put("TempInput"+fileNameCntr, tmpfile);
					fileNameCntr++;
					if(fileNameCntr==11) break;
					tmpfile = new File("/tmp/TempInput"+fileNameCntr);  //for cluster
					//tmpfile = new File("TempInput"+fileNameCntr);  //for local
					fileWriter = new FileWriter(tmpfile);
					bufferedWriter = new BufferedWriter(fileWriter); 
				}
			}
			
			/*Iterator it = globalTempFilesMap.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        System.out.println(">>"+pair.getKey() + " = " + pair.getValue());
		        it.remove(); // avoids a ConcurrentModificationException
		    }*/
		    
			bufferedReader.close();
			bufferedWriter.close();
			
			line=null;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void sortTempInputFiles() {
		try 
		{
			Iterator it = globalTempFilesMap.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry pair = (Map.Entry)it.next();
		        File currentFileToSort = (File) pair.getValue();
		        FileReader fileReader = new FileReader(currentFileToSort);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
		        String line2;
				if(inputArray==null)System.out.println("ye phasa!");
				while ((line2 = bufferedReader.readLine()) != null) {
					inputArray.add(line2);
				}
				Collections.sort(inputArray);
				writer = new FileWriter(currentFileToSort); 
				for(int j=0;j<inputArray.size();j++)
				{
					 writer.write(inputArray.get(j)+System.getProperty("line.separator"));
				}
				inputArray.clear();
				writer.close();
				fileReader.close();
				line2=null;
		    }
		    System.gc();
		    
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void startThreads() {
		for(int p=0;p<3;p++)
		{
			thread1klevelFilesMap.put(globalTempFilesMap.keySet().toArray()[p].toString(), (File)globalTempFilesMap.values().toArray()[p]);
		}
		
		for(int p=3;p<6;p++)
		{
			thread2klevelFilesMap.put(globalTempFilesMap.keySet().toArray()[p].toString(), (File)globalTempFilesMap.values().toArray()[p]);
		}
		
		for(int p=6;p<10;p++)
		{
			thread3klevelFilesMap.put(globalTempFilesMap.keySet().toArray()[p].toString(), (File)globalTempFilesMap.values().toArray()[p]);
		}
	
		MyRunnable myRunnable1 = new MyRunnable(thread1klevelFilesMap, 1);
        Thread t1 = new Thread(myRunnable1);
        t1.start();
        
        MyRunnable myRunnable2 = new MyRunnable(thread2klevelFilesMap, 2);
        Thread t2 = new Thread(myRunnable2);
        t2.start();
        
        MyRunnable myRunnable3 = new MyRunnable(thread3klevelFilesMap, 3);
        Thread t3 = new Thread(myRunnable3);
        t3.start();
        
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        Iterator it2 = globalTempFilesMap.entrySet().iterator();
	    while (it2.hasNext()) {
	        Map.Entry pair = (Map.Entry)it2.next();
	        File f= (File)pair.getValue();
	        f.delete();
	        //it.remove(); // avoids a ConcurrentModificationException
	    }
	    
        globalTempFilesMap.clear();
	}
	
	public static void createFinalOutput(File file1, File file2, String filename1, String filename2, int counter) {
		try {
			FileInputStream fileInputStream1 = null;
			InputStreamReader inputStreamReader1 = null;
			BufferedReader bufferedReader1 = null;
			
			FileInputStream fileInputStream2 = null;
			InputStreamReader inputStreamReader2 = null;
			BufferedReader bufferedReader2 = null;
			
			FileWriter fileWriter = null;
			BufferedWriter bufferedWriter = null;
			
			String array1 = null;
			String array2 = null;
			
			fileInputStream1 = new FileInputStream(file1);
			inputStreamReader1 = new InputStreamReader(fileInputStream1);
			bufferedReader1 = new BufferedReader(inputStreamReader1);
			
			fileInputStream2 = new FileInputStream(file2);
			inputStreamReader2 = new InputStreamReader(fileInputStream2);
			bufferedReader2 = new BufferedReader(inputStreamReader2);
			
			File outputFile =new File("/tmp/FinalSortedOutput"+counter);   //for cluster
			//File outputFile =new File("FinalSortedOutput"+counter);  //for local
			
			fileWriter = new FileWriter(outputFile);
			bufferedWriter = new BufferedWriter(fileWriter);
			boolean flag1=true;
			boolean flag2=true;
			
			array1=bufferedReader1.readLine();
			array2=bufferedReader2.readLine();

			int compare;
			
			while(flag1 && flag2) {
				
				compare= array1.compareTo(array2);
				if (compare < 0){
					bufferedWriter.write(array1+"\r\n");
					array1=bufferedReader1.readLine();
					if(array1==null)
					{
						flag1=false;
						break;
					}
				}
				else if (compare > 0) {
					bufferedWriter.write(array2+"\r\n");
					array2=bufferedReader2.readLine();
					if(array2==null)
					{
						flag2=false;
						break;
					}
				}
				else {
					bufferedWriter.write(array1+"\r\n");
					array1=bufferedReader1.readLine();
					if(array1==null)
					{
						flag1=false;
						break;
					}
				}
				
			}
			
			if(flag1)
			{
				bufferedWriter.write(array1+"\r\n");
				while((array1=bufferedReader1.readLine())!=null)
				{
					bufferedWriter.write(array1+"\r\n");
				}
			}
			
			if(flag2)
			{
				bufferedWriter.write(array2+"\r\n");
				while((array2=bufferedReader2.readLine())!=null)
				{
					bufferedWriter.write(array2+"\r\n");
				}
			}
			
			globalFinalOutputMap.put("FinalSortedOutput"+counter, outputFile);
			
			bufferedWriter.close();
			bufferedReader1.close();
			bufferedReader2.close();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void createPreFinalOutput(File file1, File file2, String filename1, String filename2) {
		try {
			FileInputStream fileInputStream1 = null;
			InputStreamReader inputStreamReader1 = null;
			BufferedReader bufferedReader1 = null;
			
			FileInputStream fileInputStream2 = null;
			InputStreamReader inputStreamReader2 = null;
			BufferedReader bufferedReader2 = null;
			
			FileWriter fileWriter = null;
			BufferedWriter bufferedWriter = null;
			
			String array1 = null;
			String array2 = null;
			
			fileInputStream1 = new FileInputStream(file1);
			inputStreamReader1 = new InputStreamReader(fileInputStream1);
			bufferedReader1 = new BufferedReader(inputStreamReader1);
			
			fileInputStream2 = new FileInputStream(file2);
			inputStreamReader2 = new InputStreamReader(fileInputStream2);
			bufferedReader2 = new BufferedReader(inputStreamReader2);
			
			preFinalOutput =new File("/tmp/PreSortedOutput");   //for cluster
			//preFinalOutput =new File("PreSortedOutput");  //for local
			
			fileWriter = new FileWriter(preFinalOutput);
			bufferedWriter = new BufferedWriter(fileWriter);
			boolean flag1=true;
			boolean flag2=true;
			
			array1=bufferedReader1.readLine();
			array2=bufferedReader2.readLine();

			int compare;
			
			while(flag1 && flag2) {
				
				compare= array1.compareTo(array2);
				if (compare < 0){
					bufferedWriter.write(array1+"\r\n");
					array1=bufferedReader1.readLine();
					if(array1==null)
					{
						flag1=false;
						break;
					}
				}
				else if (compare > 0) {
					bufferedWriter.write(array2+"\r\n");
					array2=bufferedReader2.readLine();
					if(array2==null)
					{
						flag2=false;
						break;
					}
				}
				else {
					bufferedWriter.write(array1+"\r\n");
					array1=bufferedReader1.readLine();
					if(array1==null)
					{
						flag1=false;
						break;
					}
				}
				
			}
			
			if(flag1)
			{
				bufferedWriter.write(array1+"\r\n");
				while((array1=bufferedReader1.readLine())!=null)
				{
					bufferedWriter.write(array1+"\r\n");
				}
			}
			
			if(flag2)
			{
				bufferedWriter.write(array2+"\r\n");
				while((array2=bufferedReader2.readLine())!=null)
				{
					bufferedWriter.write(array2+"\r\n");
				}
			}
			
			//globalFinalOutputMap.put("FinalSortedOutput"+counter, outputFile);
			
			bufferedWriter.close();
			bufferedReader1.close();
			bufferedReader2.close();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void callMe(String inputFile, int counter) {
		createTmpInputFiles(inputFile);
		sortTempInputFiles();
		startThreads();
		
		File fp1 = (File) MyRunnable.globalFileOutputArray1.values().toArray()[0];
		File fp2 = (File) MyRunnable.globalFileOutputArray2.values().toArray()[0];
		File fp3 = (File) MyRunnable.globalFileOutputArray3.values().toArray()[0];
		
		String fn1= MyRunnable.globalFileOutputArray1.keySet().toArray()[0].toString();
		String fn2 = MyRunnable.globalFileOutputArray2.keySet().toArray()[0].toString();
		String fn3 = MyRunnable.globalFileOutputArray3.keySet().toArray()[0].toString();
		createPreFinalOutput(fp1, fp2, fn1, fn2);
		createFinalOutput(fp3, preFinalOutput, fn3, "/tmp/PreSortedOutput", counter); //for cluster
		//createFinalOutput(fp3, preFinalOutput, fn3, "PreSortedOutput", counter);  //for local
		
		Iterator it = MyRunnable.globalFileOutputArray1.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        File f= (File)pair.getValue();
	        f.delete();
	    }
		
	    it = MyRunnable.globalFileOutputArray2.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        File f= (File)pair.getValue();
	        f.delete();
	    }
	    
	    it = MyRunnable.globalFileOutputArray3.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        File f= (File)pair.getValue();
	        f.delete();
	    }
	    
	    MyRunnable.globalFileOutputArray1.clear();
		MyRunnable.globalFileOutputArray2.clear();
		MyRunnable.globalFileOutputArray3.clear();
	}
	
	public static void main(String[] args) {
		Date before = new Date();
		/*2GB file path : /input/data-2GB.in*/
		/*local testing : /home/bhagyashree/Desktop/Spring18/CC/PA2/gensort-linux-1.5/64/pennyinput*/
		System.out.println("Dividing 2GB input file into smaller chunks...");
		createTmpInputFiles("/input/data-20GB.in");
		System.out.println("Sorting individual chunks...");
		sortTempInputFiles();
		System.out.println("Merging sorted chunks using k-level merge...");
		startThreads();
		File fp1 = (File) MyRunnable.globalFileOutputArray1.values().toArray()[0];
		File fp2 = (File) MyRunnable.globalFileOutputArray2.values().toArray()[0];
		File fp3 = (File) MyRunnable.globalFileOutputArray3.values().toArray()[0];
		
		String fn1= MyRunnable.globalFileOutputArray1.keySet().toArray()[0].toString();
		String fn2 = MyRunnable.globalFileOutputArray2.keySet().toArray()[0].toString();
		String fn3 = MyRunnable.globalFileOutputArray3.keySet().toArray()[0].toString();
		
		createPreFinalOutput(fp1, fp2, fn1, fn2);
		createFinalOutput(fp3, preFinalOutput, fn3, "/tmp/PreSortedOutput", 0); //for cluster
		//createFinalOutput(fp3, preFinalOutput, fn3, "PreSortedOutput", counter);  //for local
		
		Date after = new Date();
		long diffInSec =    (after.getTime() - before.getTime())/1000;
		System.out.println("Processing completed!");
		System.out.println("Total time of execution : "+diffInSec+" seconds");
	}


}
