package com.bhagyashree;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

/**
 * @author bhagyashree
 *
 */
public class SparkSort 
{
    public static void main( String[] args )
    {
    	SparkConf sparkConf = new SparkConf().setAppName("SparkSort").setMaster("local");
		JavaSparkContext jsc = new JavaSparkContext(sparkConf);
		
		//read input file into RDD
		JavaRDD<String> lines = jsc.textFile(args[0]);

		//divide each row of file into key value pair
		JavaPairRDD<String, String> pairRdd = lines.mapToPair(new PairFunction<String, String, String>() {
			public Tuple2<String, String> call(String str) throws Exception {
			String key = str.substring(0, 9);
			String value = str.substring(10);
			return new Tuple2<String, String>(key, value);
			}
			});
		
		//sort on key
		JavaPairRDD<String, String> sortedRdd = pairRdd.sortByKey();
		
		//write sorted rows to outputfile
		sortedRdd.saveAsTextFile("/tmp/SparkSortedOutput");
		jsc.close();
    }
}
