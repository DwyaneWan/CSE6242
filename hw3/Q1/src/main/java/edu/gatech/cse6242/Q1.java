package edu.gatech.cse6242;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Q1 {

   public static class TargetMapper
       extends Mapper<Object, Text, Text, IntWritable>{

    private Text target = new Text();
    private IntWritable weight = new IntWritable();

    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
        String[] values = value.toString().split("\t");
        if (values.length != 3) return;
        target.set(values[1]);
        weight.set(Integer.parseInt(values[2]));
        context.write(target, weight);
    }
  }

  public static class MinReducer
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values,
                       Context context
                       ) throws IOException, InterruptedException {
      int min = Integer.MAX_VALUE;
      for (IntWritable val : values) {
        min = Math.min(min, val.get());
      }
      result.set(min);
      context.write(key, result);
    }
  }
  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Q1");

    /* TODO: Needs to be implemented */

    job.setJarByClass(Q1.class);
    job.setMapperClass(TargetMapper.class);
    job.setCombinerClass(MinReducer.class);
    job.setReducerClass(MinReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
