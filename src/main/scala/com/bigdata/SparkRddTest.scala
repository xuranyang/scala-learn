package com.bigdata

import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

object SparkRddTest {
    def main(args: Array[String]): Unit = {
        // 准备环境
        val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("RddApp")
        val sc = new SparkContext(sparkConf)

        //        createRddFromMemory(sc)
        //        createRddFromFile(sc)
        // rddChangePartitionNum(sc)
        rddJoin(sc)

        sc.stop()
    }

    def createRddFromMemory(sc: SparkContext): Unit = {
        // TODO 从内存创建RDD
        val seq: Seq[Int] = Seq[Int](1, 2, 3, 4)
        // parallelize 或 makeRDD 都可以从内存创建RDD
        // val rdd: RDD[Int] = sc.parallelize(seq)
        // makeRDD -> parallelize
        val rdd: RDD[Int] = sc.makeRDD(seq)
        rdd.collect().foreach(println)
    }

    def createRddFromFile(sc: SparkContext): Unit = {
        // TODO 从文件创建RDD
        // textFile 或 wholeTextFiles 可以从文件创建RDD
        // val rdd: RDD[String] = sc.textFile("src/main/resources/data.txt")
        // 第二个参数可以手动指定最小分区数
        /**
         * 1.textFile 以每行为单位进行读取
         * spark读取文件，采用的是Hadoop的方式读取，所以一行一行读取，和字节数没有关系
         * 2.数据读取时以偏移量为单位，偏移量不会被重复读取
         *
         * 分区数计算: 以 data.txt的 "1@@2@@3" 为例
         * totalSize = 7
         * goalSize = 7/2 = 3 byte
         * 7/3 = 2 ··· 1
         * 1/3 > 1.1倍 所以要产生1个新的分区
         * 所以分成共 2+1=3 个分区
         *
         * 注：下面用 "@@" 表示特殊符号=>回车(CR)、换行(LF)
         *
         * 1@@ => 012
         * 2@@ => 345
         * 3   => 6
         *
         * 3.数据分区的偏移量计算
         * 0 => [0,3] => 12
         * 1 => [3,6] => 3
         * 2 => [6,7] =>
         */
        val rdd: RDD[String] = sc.textFile("src/main/resources/data.txt", 2)
        rdd.saveAsTextFile("output")

        //        val rdd2: RDD[String] = sc.textFile("src/main/resources/data2.txt", 2)
        //        rdd2.saveAsTextFile("output2")
        /**
         * 同理以 data2.txt 数据为例的话：
         *
         * 1234567@@ => 012345678
         * 89@@      => 9 10 11 12
         * 0         => 13
         *
         * 14/2=7byte
         * [0,7]  => 1234567@@
         * [7,14] => 89@@ 0
         */
    }

    def sparkPersist(rdd: RDD[Any]): Unit = {
        rdd.persist()
        rdd.persist(StorageLevel.MEMORY_ONLY) // 持久化到本地内存
        rdd.persist(StorageLevel.DISK_ONLY) // 持久化到磁盘
        rdd.persist(StorageLevel.MEMORY_AND_DISK) // 内存放不下，则将数据溢写到磁盘上
        rdd.persist(StorageLevel.MEMORY_ONLY_SER) // 序列化后持久到本地内存
        rdd.persist(StorageLevel.MEMORY_AND_DISK_SER) // 内存放序列化后数据，如果放不下，则将数据溢写到磁盘上
        rdd.cache() // MEMORY_ONLY的persist
        rdd.checkpoint()
    }

    def rddTransformOp(sc: SparkContext): Unit = {
        // 常见的 Transformation转换操作
        val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 3))
        val rdd2: RDD[Int] = sc.makeRDD(List(3, 4, 5))
        rdd.map(x => x + 1) // map
        rdd.flatMap(x => x.to(3))
        rdd.filter(x => x != 1)
        rdd.distinct()
        rdd.union(rdd2) // 求并集
        rdd.intersection(rdd2) // 求交集
        rdd.subtract(rdd2) // 求差集
        rdd.cartesian(rdd2) // 笛卡尔积
    }

    def rddActionOp(sc: SparkContext): Unit = {
        // 常见的 Action行动操作
        val rdd: RDD[Int] = sc.makeRDD(List(1, 2, 3, 3))
        rdd.collect()
        rdd.countByValue()
        rdd.take(2)
        rdd.top(2)
        rdd.reduce((x, y) => x + y)
        rdd.fold(0)((x, y) => x + y)
        rdd.foreach(x => println(x))
        rdd.saveAsTextFile("xxx")
    }

    def rddChangePartitionNum(sc: SparkContext): Unit = {
        // 重分区操作是指改变RDD的分区数，有两种常用的方法：repartition和coalesce
        rddRePartition(sc)
        rddCoalesce(sc)
    }

    def rddRePartition(sc: SparkContext): Unit = {
        //假设有一个RDD[Int]，分成3个分区
        val rdd = sc.parallelize(List(1, 2, 3, 4, 5, 6), 3)
        println("repartition:")
        //查看分区数
        println(rdd.partitions.length) // 3
        //使用repartition增加分区数到6
        val rdd2 = rdd.repartition(6)
        //查看分区数
        println(rdd2.partitions.length) // 6
        //使用repartition减少分区数到2
        val rdd3 = rdd.repartition(2)
        //查看分区数
        println(rdd3.partitions.length) // 2
    }

    def rddCoalesce(sc: SparkContext): Unit = {
        //假设有一个RDD[Int]，分成3个分区
        val rdd = sc.parallelize(List(1, 2, 3, 4, 5, 6), 3)
        println("coalesce:")
        //查看分区数
        println(rdd.partitions.length) // 3
        //使用coalesce减少分区数到2，不开启shuffle
        val rdd2 = rdd.coalesce(2)
        //查看分区数
        println(rdd2.partitions.length) // 2
        //使用coalesce增加分区数到4，开启shuffle
        val rdd3 = rdd.coalesce(4, true)
        //查看分区数
        println(rdd3.partitions.length) // 4
    }


    def rddJoin(sc: SparkContext): Unit = {
        val rdd1: RDD[(String, Int)] = sc.makeRDD(List(
            ("a", 1), ("b", 2), ("c", 3), ("d", 4)
        ))

        val rdd2: RDD[(String, String)] = sc.makeRDD(List(
            ("a", "aa"), ("b", "bb"), ("c", "cc")
        ))

        val joinRdd: RDD[(String, (Int, String))] = rdd1.join(rdd2) // join 等价为sql中的 inner join
        val leftJoinRdd: RDD[(String, (Int, Option[String]))] = rdd1.leftOuterJoin(rdd2)
        val rightJoinRdd: RDD[(String, (Option[Int], String))] = rdd1.rightOuterJoin(rdd2)

        joinRdd.collect().foreach(println)
        leftJoinRdd.collect().foreach(println)
        rightJoinRdd.collect().foreach(println)

    }
}
