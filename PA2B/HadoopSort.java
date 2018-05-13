package com.bhagyashree;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * @author bhagyashree
 * This class implements Hadoop sort application by overriding Mapper and Reducer
 */

public class HadoopSort 
{
	//This is starting point of the application
	public static void main(String[] args) throws Exception{
		
		//Start timer
		long startTime = System.currentTimeMillis(); 

		//Configuring a new Hadoop job with all the required parameters
		Configuration conf = new Configuration(); 
		//Specify job name
		Job job = new Job(conf,"MyHadoopSort");
		job.setJarByClass(HadoopSort.class);
		job.setMapperClass(HMapper.class);
		job.setCombinerClass(HReducer.class);
		job.setReducerClass(HReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		//The input file of sorting is read from command line argument
		FileInputFormat.addInputPath(job, new Path(args[0])); 
		
		//The sorted output is  is read from command line argument
		FileOutputFormat.setOutputPath(job, new Path("/tmp/HadoopSortedOutput")); 
		
		long endTime = System.currentTimeMillis(); 
		
		//End timer
		long totalTime = endTime - startTime;
		
		//Submit the job to the cluster and wait for it to finish.
		if (job.waitForCompletion(true)) {
			System.out.println("Total Time Elapsed " + totalTime);			
			System.exit(0);
		}else {
			System.out.println("Total Time Elapsed " + totalTime);
			System.exit(1);	
		}
		
	}
	
	public static class HMapper extends Mapper<Object, Text, Text, Text>{

		private Text keys = new Text();
		private Text values = new Text();
		
		public void map (Object key, Text value, Context context)throws IOException, InterruptedException{
	        
			  String val= value.toString();
	          String keyForLine = val.substring(0, 10);
	          String line=val;
	          
	          Text keytext = new Text(keyForLine);
	          Text keyval = new Text(line);
	          
	          context.write(keytext,keyval);
		}
	}
	
	public static class HReducer extends Reducer<Text, Text, Text, Text>{
		
		private Text outKey = new Text();
		private Text outValue = new Text();
		public void reduce (Text key, Iterable<Text> values, Context context)throws IOException, InterruptedException{
		
			outKey = key;
		
		for (Text val : values){
			outValue = val;
		}
		context.write(outKey,outValue);
		}

	}
}



