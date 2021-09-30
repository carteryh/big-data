package cn.itcast.taxi

object EitherTest {

  def main(args: Array[String]): Unit = {
    /**
      * 相当于 Parse 方法
      */
    def process(b: Double): Double = {
      val a = 10.0
      a / b
    }

    // Either => Left Or Right
    // Option => Some None

    def safe(f: Double => Double, b: Double): Either[Double, (Double, Exception)] = {
      try {
        val result = f(b)
        Left(result)
      } catch {
        case e: Exception => Right(b, e)
      }
    }

    //  process(0.0)
    val result = safe(process, 0)
    result.isLeft

    result match {
      case Left(r) => println(r)
      case Right((b, e)) => println(b, e)
    }
  }
}
