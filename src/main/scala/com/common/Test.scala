package com.common

object Test {
  def add(x: Int, y: Int) = x + y

  val sub = (x: Int, y: Int) => x - y

  def double(a: Int): Int = {
    return a * 2
  }

  def main(args: Array[String]): Unit = {

    val VAL: Int = 10; // 常量
    var v1: String = "Foo"; // 变量

    val VAL_2 = 100
    var v2 = "foo"

    println(VAL, VAL_2, v1, v2)

    // [1,3]
    for (a <- 1 to 3) {
      println("a:", a)
    }

    // [1,3)
    for (b <- 1 until 3) {
      println("b", b)
    }


    val numList = List(1, 2, 3, 4, 5, 6);
    // for 循环
    for (a <- numList) {
      println("Value of a: " + a);
    }

    val typeArr = Array("Byte", "Short", "Int", "Long", "Float", "Double", "Char", "String", "Boolean", "Unit", "Null", "Nothing", "Any", "AntRef");
    typeArr.foreach(s => {
      println(s)
    })

    var myMap = Map('a' -> 1, 'b' -> 2, 'c' -> 3)
    myMap += ('d' -> 4)
    myMap.foreach(kv => {
      var k = kv._1
      var v = kv._2
      println("kv", kv, k, v)
    })


    println("add", add(1, 2))
    println("sub", sub(3, 1))
    println("double", double(2))


    val fred = new Employee
    fred.name = "Fred"
    fred.salary = 50000
    println(fred)

    val p1 = new Point(2, 3)
    val p2 = new Point(2, 4)
    val p3 = new Point(3, 3)
    println(p1.isNotEqual(p2))
    println(p1.isNotEqual(p3))
  }
}

class Person {
  var name = ""

  override def toString = getClass.getName + "[name=" + name + "]"
}

class Employee extends Person {
  var salary = 0.0

  override def toString = super.toString + "[salary=" + salary + "]"
}

trait Equal {
  def isEqual(x: Any): Boolean

  def isNotEqual(x: Any): Boolean = !isEqual(x)
}

class Point(xc: Int, yc: Int) extends Equal {
  var x: Int = xc
  var y: Int = yc

  def isEqual(obj: Any) =
    obj.isInstanceOf[Point] &&
      obj.asInstanceOf[Point].x == x
}
