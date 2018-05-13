import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MyRunnable20GB implements Runnable{

	//Thread 1 resources
	private  HashMap<String, File> threadFileHashMap1 = new HashMap<>();
	static HashMap<String, File> globalFileOutputArray1 = new HashMap<>();
	private static boolean isRemove1=false;
	private static int outCntr1;
	
	//Thread 2 resources
	private  HashMap<String, File> threadFileHashMap2 = new HashMap<>();
	static HashMap<String, File> globalFileOutputArray2 = new HashMap<>();
	private static boolean isRemove2=false;
	private static int outCntr2;
	
	//Thread 3 resources
		private  HashMap<String, File> threadFileHashMap3 = new HashMap<>();
		static HashMap<String, File> globalFileOutputArray3 = new HashMap<>();
		private static boolean isRemove3=false;
		private static int outCntr3;
	
	private int threadId;
	//private static FileWriter fwriter=null;
	
	public MyRunnable20GB(HashMap<String, File> fileMap, int threadId) {
        this.threadId = threadId;
        if(threadId==1) {
        	outCntr1=1;
        	this.threadFileHashMap1 = fileMap;
    	    
        }
        else if(threadId==2){
        	this.threadFileHashMap2 = fileMap;
        	outCntr2=4;
        }
        else if(threadId==3){
        	this.threadFileHashMap3 = fileMap;
        	outCntr3=7;
        }
    }
	
	@Override
	public void run() {
		File file11 = null;
		File file12 =null;
		String fileName11=null;
		String fileName12=null;
		File file21 = null;
		File file22 =null;
		String fileName21=null;
		String fileName22=null;
		File file31 = null;
		File file32 =null;
		String fileName31=null;
		String fileName32=null;
		
			if(threadId==1) {
			int k=0;
			while(k<=threadFileHashMap1.size()-2) {
			file11 = (File) threadFileHashMap1.values().toArray()[k];
			file12 = (File) threadFileHashMap1.values().toArray()[k+1];
			
			fileName11= threadFileHashMap1.keySet().toArray()[k].toString();
			fileName12 = threadFileHashMap1.keySet().toArray()[k+1].toString();
			mergeTwoFiles(file11,file12,fileName11,fileName12,threadId);
			k+=2;
			}
			if(threadFileHashMap1.size()%2!=0)
			{
				globalFileOutputArray1.put(threadFileHashMap1.keySet().toArray()[threadFileHashMap1.size()-1].toString(), (File)threadFileHashMap1.values().toArray()[threadFileHashMap1.size()-1]);
				
			}
			while(globalFileOutputArray1.size()!=1)
			{
				k=0;
				isRemove1=true;
				while(k<=globalFileOutputArray1.size()-2) {
					file11 = (File) globalFileOutputArray1.values().toArray()[k];
					file12 = (File) globalFileOutputArray1.values().toArray()[k+1];
					
					fileName11= globalFileOutputArray1.keySet().toArray()[k].toString();
					fileName12 = globalFileOutputArray1.keySet().toArray()[k+1].toString();
					mergeTwoFiles(file11,file12,fileName11, fileName12, threadId);
					k+=2;
				}
			}
			
			}
			else if(threadId==2)
			{
				int l=0;
				
				while(l<=threadFileHashMap2.size()-2) {
					file21 = (File) threadFileHashMap2.values().toArray()[l];
					file22 = (File) threadFileHashMap2.values().toArray()[l+1];
					
					fileName21= threadFileHashMap2.keySet().toArray()[l].toString();
					fileName22 = threadFileHashMap2.keySet().toArray()[l+1].toString();
					mergeTwoFiles(file21,file22,fileName21,fileName22,threadId);
					l+=2;
					}
				if(threadFileHashMap2.size()%2!=0)
				{
					globalFileOutputArray2.put(threadFileHashMap2.keySet().toArray()[threadFileHashMap2.size()-1].toString(), (File)threadFileHashMap2.values().toArray()[threadFileHashMap2.size()-1]);
					
				}
					while(globalFileOutputArray2.size()!=1)
					{
						l=0;
						isRemove2=true;
						while(l<=globalFileOutputArray2.size()-2) {
							file21 = (File) globalFileOutputArray2.values().toArray()[l];
							file22 = (File) globalFileOutputArray2.values().toArray()[l+1];
							
							fileName21= globalFileOutputArray2.keySet().toArray()[l].toString();
							fileName22 = globalFileOutputArray2.keySet().toArray()[l+1].toString();
							mergeTwoFiles(file21,file22,fileName21, fileName22, threadId);
							l+=2;
						}
					}
			}
			else if(threadId==3)
			{
				int l=0;
				
				while(l<=threadFileHashMap3.size()-2) {
					file31 = (File) threadFileHashMap3.values().toArray()[l];
					file32 = (File) threadFileHashMap3.values().toArray()[l+1];
					
					fileName31= threadFileHashMap3.keySet().toArray()[l].toString();
					fileName32 = threadFileHashMap3.keySet().toArray()[l+1].toString();
					mergeTwoFiles(file31,file32,fileName31,fileName32,threadId);
					l+=2;
					}
				if(threadFileHashMap3.size()%2!=0)
				{
					globalFileOutputArray3.put(threadFileHashMap3.keySet().toArray()[threadFileHashMap3.size()-1].toString(), (File)threadFileHashMap3.values().toArray()[threadFileHashMap3.size()-1]);
					
				}
					while(globalFileOutputArray3.size()!=1)
					{
						l=0;
						isRemove3=true;
						while(l<=globalFileOutputArray3.size()-2) {
							file31 = (File) globalFileOutputArray3.values().toArray()[l];
							file32 = (File) globalFileOutputArray3.values().toArray()[l+1];
							
							fileName31= globalFileOutputArray3.keySet().toArray()[l].toString();
							fileName32 = globalFileOutputArray3.keySet().toArray()[l+1].toString();
							mergeTwoFiles(file31,file32,fileName31, fileName32, threadId);
							l+=2;
						}
					}
			}
			file11=null;
			file12=null;
			file21=null;
			file22=null;
			file31=null;
			file32=null;
			fileName11=null;
			fileName12=null;
			fileName21=null;
			fileName22=null;
			fileName31=null;
			fileName32=null;
			
	}
	
	
	public static synchronized void mergeTwoFiles(File file1, File file2, String filename1, String filename2, int threadNumber) {
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
			
			File outputFile =null;
			
			if(threadNumber==1) {
				//outputFile = new File("/tmp/TmpOut"+outCntr1); //for cluster
				outputFile = new File("TmpOut"+outCntr1);  //for local
			}
			else if(threadNumber==2)
			{
				//outputFile = new File("/tmp/TmpOut"+outCntr2);  //for cluster
				outputFile = new File("TmpOut"+outCntr2);  //for local
			}
			else if(threadNumber==3)
			{
				//outputFile = new File("/tmp/TmpOut"+outCntr2);  //for cluster
				outputFile = new File("TmpOut"+outCntr3);  //for local
			}
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
			
			if(!globalFileOutputArray1.isEmpty() && threadNumber==1 && isRemove1) {
				globalFileOutputArray1.remove(filename1);
				globalFileOutputArray1.remove(filename2);
				file1.delete();
				file2.delete();
			}
			else if(!globalFileOutputArray2.isEmpty() && threadNumber==2 && isRemove2)
			{
				globalFileOutputArray2.remove(filename1);
				globalFileOutputArray2.remove(filename2);
				file1.delete();
				file2.delete();
				
			}
			else if(!globalFileOutputArray3.isEmpty() && threadNumber==3 && isRemove3)
			{
				globalFileOutputArray3.remove(filename1);
				globalFileOutputArray3.remove(filename2);
				file1.delete();
				file2.delete();
				
			}
		
			if(threadNumber==1) {
				globalFileOutputArray1.put("TmpOut"+outCntr1, outputFile);
				outCntr1++;
			}
			else if(threadNumber==2){
				globalFileOutputArray2.put("TmpOut"+outCntr2, outputFile);
				outCntr2++;
			}
			else if(threadNumber==3){
				globalFileOutputArray3.put("TmpOut"+outCntr3, outputFile);
				outCntr3++;
			}
			
			bufferedWriter.close();
			bufferedReader1.close();
			bufferedReader2.close();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

}
