package edu.gatech.cse6242

import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.functions._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row

object Q2 {
def main(args: Array[String]) {
val sc = new SparkContext(new SparkConf().setAppName("Q2"))
val sqlContext = new SQLContext(sc)
import sqlContext.implicits._

// read the file
val file = sc.textFile("hdfs://localhost:8020" + args(0))
/* TODO: Needs to be implemented */

val edges = file.map(_.split("\t"))

val filtered = edges.map(e => (e(0), e(1), e(2).toInt)).filter(_._3 >= 5).toDF("src", "tgt", "weight")

val out = filtered.select(filtered("src"), -filtered("weight")).toDF("node", "weight")

val in = filtered.select(filtered("tgt"), filtered("weight")).toDF("node", "weight")

val combined = in.unionAll(out)

val res = combined.groupBy($"node").sum("weight").rdd.map(_.mkString("\t"))

// store output on given HDFS path.
// YOU NEED TO CHANGE THIS
res.saveAsTextFile("hdfs://localhost:8020" + args(1))
}
}
