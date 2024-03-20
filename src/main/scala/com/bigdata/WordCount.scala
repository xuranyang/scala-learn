package com.bigdata

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
    def main(args: Array[String]): Unit = {
        // 创建 SparkConf 对象
        val conf: SparkConf = new SparkConf()
          .setAppName("WordCount")
          .setMaster("local[*]")

        // 创建 SparkContext 对象
        val sc = new SparkContext(conf)

        wordCount_1(sc)
//        wordCount_2(sc)

        // 停止 SparkContext
        sc.stop()
    }

    def wordCount_1(sc: SparkContext): Unit = {
        // 读取文本文件
        val lines: RDD[String] = sc.textFile("src/main/resources/input.txt")

        // 执行 WordCount
        val wordCounts: RDD[(String, Int)] = lines
          .flatMap(line => line.split("\\s+"))
          .map(word => (word, 1))
          .reduceByKey(_ + _)

        // 输出结果
        wordCounts.collect().foreach(println)
    }

    def wordCount_2(sc: SparkContext): Unit = {
        // WordCount 方法2
        val rdd: RDD[String] = sc.makeRDD(List("Hello Scala", "Hello Spark"))
        val words = rdd.flatMap(_.split(" "))
        val group: RDD[(String, Iterable[String])] = words.groupBy(word => word)
        val wordCount: RDD[(String, Int)] = group.mapValues(iter => iter.size)
        wordCount.collect().foreach(println)
    }
}