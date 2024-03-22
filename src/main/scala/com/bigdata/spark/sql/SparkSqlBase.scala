package com.bigdata.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

object SparkSqlBase {
    def main(args: Array[String]): Unit = {
        // TODO 创建SparkSQL环境
        val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("sparkSqlApp")
        val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
        import spark.implicits._

        // TODO 执行逻辑操作
        // DataFrame
        // dataFrameToSql(spark)

        // DataSet
        // createDataSet(spark)

        // TODO RDD <=> DataFrame
        // RDD -> DataFrame
        println("RDD -> DataFrame")
        val rdd: RDD[(Int, String, Int)] = spark.sparkContext.makeRDD(
            List((1, "zhangsan", 30), (2, "lisi", 40))
        )
        val df: DataFrame = rdd.toDF("id", "name", "age")
        df.show()

        // DataFrame -> RDD
        println("DataFrame -> RDD")
        val rowRDD: RDD[Row] = df.rdd
        rowRDD.collect().foreach(println)

        // TODO DataFrame <=> DataSet
        // DataFrame -> DataSet
        println("DataFrame -> DataSet")
        val ds: Dataset[User] = df.as[User]
        ds.show()

        // DataSet -> DataFrame
        println("DataSet -> DataFrame")
        val df2: DataFrame = ds.toDF()
        df2.collect().foreach(println)

        // TODO RDD <=> DataSet
        // RDD -> DataSet
        println("RDD -> DataSet")
        val userRDD: RDD[User] = rdd.map {
            case (id, name, age) => {
                User(id, name, age)
            }
        }
        val ds2: Dataset[User] = userRDD.toDS()
        ds2.show()

        // DataSet -> RDD
        println("DataSet -> RDD")
        val userRDD2: RDD[User] = ds2.rdd
        userRDD2.collect().foreach(println)

        // TODO 关闭环境
        spark.close()
    }

    case class User(id: Int, name: String, age: Int)

    def createDataSet(spark: SparkSession): Unit = {
        import spark.implicits._
        val seq: Seq[Int] = Seq(1, 2, 3, 4)
        val ds: Dataset[Int] = seq.toDS()
        ds.show()
    }

    def dataFrameToSql(spark: SparkSession): Unit = {
        import spark.implicits._
        // DataFrame -> SQL
        val df: DataFrame = spark.read.json("src/main/resources/user.json")
        df.show()

        df.createOrReplaceTempView("user")
        spark.sql("select * from user").show()
        spark.sql("select name,age from user").show()
        spark.sql("select avg(age) from user").show()

        // DataFrame -> DSL
        // import spark.implicits._
        df.select("name", "age").show()
        df.select($"age" + 1).show()
        df.select('age + 1).show()

    }
}
