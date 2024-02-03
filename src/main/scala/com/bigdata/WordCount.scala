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

        // 读取文本文件
        val lines: RDD[String] = sc.textFile("src/main/resources/input.txt")

        // 执行 WordCount
        val wordCounts: RDD[(String, Int)] = lines
          .flatMap(line => line.split("\\s+"))
          .map(word => (word, 1))
          .reduceByKey(_ + _)

        // 输出结果
        wordCounts.collect().foreach(println)

        // 停止 SparkContext
        sc.stop()
    }
}