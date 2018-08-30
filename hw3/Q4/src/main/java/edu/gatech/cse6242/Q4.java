package edu.gatech.cse6242;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;

public class Q4 {
    
    public static class DegreeMapper extends Mapper<Object, Text, Text, IntWritable> {
        
        private Text node = new Text();
        private IntWritable out = new IntWritable(1);
        private IntWritable in = new IntWritable(-1);
        
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] edge = value.toString().split("\t");
            if (edge.length != 2) return;
            node.set(edge[0]);
            context.write(node, out);
            node.set(edge[1]);
            context.write(node, in);
        }
    }
    
    public static class DegreeReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        
        private IntWritable total = new IntWritable();
        
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int count = 0;
            for (IntWritable val : values) {
                count += val.get();
            }
            total.set(count);
            context.write(key, total);
        }
    }
    
    public static class FreqMapper extends Mapper<Object, Text, Text, IntWritable> {
        
        private Text diff = new Text();
        private IntWritable one = new IntWritable(1);
        
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] parts = value.toString().split("\t");
            if (parts.length != 2) return;
            diff.set(parts[1]);
            context.write(diff, one);
        }
    }
    
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job1 = Job.getInstance(conf, "job1");
        
        /* TODO: Needs to be implemented */
        job1.setJarByClass(Q4.class);
        job1.setMapperClass(DegreeMapper.class);
        job1.setCombinerClass(DegreeReducer.class);
        job1.setReducerClass(DegreeReducer.class);
        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job1, new Path(args[0]));
        FileOutputFormat.setOutputPath(job1, new Path("tmp"));
        job1.waitForCompletion(true);
        
        Job job2 = Job.getInstance(conf, "job2");
        job2.setJarByClass(Q4.class);
        job2.setMapperClass(FreqMapper.class);
        job2.setCombinerClass(DegreeReducer.class);
        job2.setReducerClass(DegreeReducer.class);
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job2, new Path("tmp"));
        FileOutputFormat.setOutputPath(job2, new Path(args[1]));
        System.exit(job2.waitForCompletion(true) ? 0:1);
    }
}
