import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ExternalMerge20GB {
	private static HashMap<String, File> global2GBFilesMap = new HashMap<>();
	private static HashMap<String, File> thread1klevelFilesMap = new HashMap<>();
	private static HashMap<String, File> thread2klevelFilesMap = new HashMap<>();
	private static HashMap<String, File> thread3klevelFilesMap = new HashMap<>();
	
	private static int  noOfThreads=2;
	public static int threadParam;
	static File preOutputFile =null;
	static File outputFile =null;
	
	public static void create2GBInputFiles(String inputFileName) {
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
			tmpfile = new File("/tmp/Temp2GBInput"+fileNameCntr);  //for cluster
			//tmpfile = new File("Temp2GBInput"+fileNameCntr);    //for local
			
			fileWriter = new FileWriter(tmpfile);
			bufferedWriter = new BufferedWriter(fileWriter);
			
			long lineCountr=0;
			while ((line = bufferedReader.readLine()) != null) {
				lineCountr++;
				bufferedWriter.write(line+"\r\n");	
			//20000000 for 2GB, 20 for local
				if(lineCountr==20000000) {
					lineCountr=0;
					bufferedWriter.flush();
					global2GBFilesMap.put("Temp2GBInput"+fileNameCntr, tmpfile);
					fileNameCntr++;
					if(fileNameCntr==11) break;
					tmpfile = new File("/tmp/Temp2GBInput"+fileNameCntr);  //for cluster
					//tmpfile = new File("Temp2GBInput"+fileNameCntr);  //for local
					fileWriter = new FileWriter(tmpfile);
					bufferedWriter = new BufferedWriter(fileWriter); 
				}
			}
			System.out.println("No of 2Gb files : "+global2GBFilesMap.size());
			
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

	public static void createFinalOutput(File file1, File file2, String filename1, String filename2, int level) {
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
			
			
			if(level==1) {
				preOutputFile =new File("/tmp/SortedOutput");  //for cluster
				//preOutputFile =new File("SortedOutput");
				fileWriter = new FileWriter(preOutputFile);
			}
			else if(level==2)
			{
				outputFile =new File("/tmp/FinalSortedOutput");  //for cluster
				//outputFile =new File("FinalSortedOutput");
				fileWriter = new FileWriter(outputFile);
			}
			//File outputFile =new File("20GBSortedOutput"); //for local
			
			
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
			
			bufferedWriter.close();
			bufferedReader1.close();
			bufferedReader2.close();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void startThreads() {

	    for(int p=0;p<3;p++)
		{
			
			thread1klevelFilesMap.put(ExternalMergeSort.globalFinalOutputMap.keySet().toArray()[p].toString(), (File)ExternalMergeSort.globalFinalOutputMap.values().toArray()[p]);
		}
		
		for(int p=3;p<6;p++)
		{
			thread2klevelFilesMap.put(ExternalMergeSort.globalFinalOutputMap.keySet().toArray()[p].toString(), (File)ExternalMergeSort.globalFinalOutputMap.values().toArray()[p]);
		}
		
		for(int p=6;p<10;p++)
		{
			thread3klevelFilesMap.put(ExternalMergeSort.globalFinalOutputMap.keySet().toArray()[p].toString(), (File)ExternalMergeSort.globalFinalOutputMap.values().toArray()[p]);
		}
		
		MyRunnable20GB myRunnable1 = new MyRunnable20GB(thread1klevelFilesMap, 1);
        Thread t1 = new Thread(myRunnable1);
        t1.start();
        
        MyRunnable20GB myRunnable2 = new MyRunnable20GB(thread2klevelFilesMap, 2);
        Thread t2 = new Thread(myRunnable2);
        t2.start();
        
        MyRunnable20GB myRunnable3 = new MyRunnable20GB(thread3klevelFilesMap, 3);
        Thread t3 = new Thread(myRunnable3);
        t3.start();
        
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
	
	public static void main(String[] args) {
		Date before = new Date();
		/*2GB file path : /input/data-2GB.in
		local testing : /home/bhagyashree/Desktop/Spring18/CC/PA2/gensort-linux-1.5/64/pennyinput*/
		
		//create2GBInputFiles("/home/bhagyashree/Desktop/Spring18/CC/PA2/gensort-linux-1.5/64/pennyinput");  //For local
		create2GBInputFiles("/input/data-20GB.in");  //For cluster
		
		//ExternalMergeSort.callMe(global2GBFilesMap.keySet().toArray()[0].toString(), 0);
		//Iterator it=null;
		Iterator it = global2GBFilesMap.entrySet().iterator();
		int counter =0;
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        ExternalMergeSort.callMe("/tmp/"+pair.getKey(), counter);  //For cluster
	        //ExternalMergeSort.callMe(pair.getKey().toString(), counter);   //for local
	        counter++;
	    }
		startThreads();
		File fp1 = (File) MyRunnable20GB.globalFileOutputArray1.values().toArray()[0];
		File fp2 = (File) MyRunnable20GB.globalFileOutputArray2.values().toArray()[0];
		File fp3 = (File) MyRunnable20GB.globalFileOutputArray3.values().toArray()[0];
		
		String fn1= MyRunnable20GB.globalFileOutputArray1.keySet().toArray()[0].toString();
		String fn2 = MyRunnable20GB.globalFileOutputArray2.keySet().toArray()[0].toString();
		String fn3 = MyRunnable20GB.globalFileOutputArray3.keySet().toArray()[0].toString();
		
		createFinalOutput(fp1, fp2, fn1, fn2, 1);
		
		createFinalOutput(fp3, preOutputFile, fn3, "/tmp/SortedOutput", 2);  //for cluster
		//createFinalOutput(fp3, preOutputFile, fn3, "SortedOutput", 2); //for local
	
		it = MyRunnable20GB.globalFileOutputArray1.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        File f= (File)pair.getValue();
	        f.delete();
	    }
		
	    it = MyRunnable20GB.globalFileOutputArray2.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        File f= (File)pair.getValue();
	        f.delete();
	    }
	    it = MyRunnable20GB.globalFileOutputArray3.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        File f= (File)pair.getValue();
	        f.delete();
	    }
	    
	    it = ExternalMergeSort.globalFinalOutputMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        File f= (File)pair.getValue();
	        f.delete();
	    }
	    
	    it = global2GBFilesMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        File f= (File)pair.getValue();
	        f.delete();
	    }
	    
	    MyRunnable20GB.globalFileOutputArray1.clear();
	    MyRunnable20GB.globalFileOutputArray2.clear();
	    MyRunnable20GB.globalFileOutputArray3.clear();
		ExternalMergeSort.globalFinalOutputMap.clear();
		global2GBFilesMap.clear();
		System.out.println("Sorting completed!");
		Date after = new Date();
		long diffInSec =    (after.getTime() - before.getTime())/1000;
		System.out.println("Total time of execution : "+diffInSec);
	}


}
