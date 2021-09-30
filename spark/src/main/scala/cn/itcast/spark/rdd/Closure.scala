package cn.itcast.spark.rdd

import org.junit.Test

class Closure {

  /**
    * 编写一个高阶函数, 在这个函数内要有一个变量, 返回一个函数, 通过这个变量完成一个计算
    */
  @Test
  def test(): Unit = {
//    val f: Int => Double = closure()
//    val area = f(5)
//    println(area)

    // 在这能否访问到 Factor, 不能
    // 说明 Factor 在一个单独的作用域中

    // 在拿到 f 的时候, 可以通过 f 间接的访问到 closure 作用域中的内容
    // 说明 f 携带了一个作用域
    // 如果一个函数携带了一个外包的作用域, 这种函数我们称之为叫做闭包
    val f = closure()
    f(5)

    // 闭包的本质是什么?
    // f 就是闭包, 闭包的本质就是一个函数
    // 在 Scala 中函数是一个特殊的类型, FunctionX
    // 闭包也是一个 FunctionX 类型的对象
    // 闭包是一个对象
  }

  /**
    * 返回一个新的函数
    */
  def closure(): Int => Double = {
    val factor = 3.14
    val areaFunction = (r: Int) => {
      math.pow(r, 2) * factor
    }
    areaFunction
  }
}
