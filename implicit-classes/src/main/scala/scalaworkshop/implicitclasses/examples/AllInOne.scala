package scalaworkshop.implicitclasses.examples


object example01 {

  def add2(x: Int) = x + 2
  def mul2(x: Int) = x * 2
  def sub2(x: Int) = x - 2

  // hard to read: execution order is right to left
  sub2(mul2(add2(42)))
  //  42.add2.mul2.sub2

  def add(x: Int, n: Int) = x + n
  def mul(x: Int, n: Int) = x * n
  def sub(x: Int, n: Int) = x - n

  // even worse with multiple parameters
  sub(mul(add(42, 2), 2), 2)
  //  42.add(2).mul(2).sub(2)
}

object example02 {
  // java like solution using a Wrapper
  case class IntWrapper(x: Int) {
    def add(n: Int): IntWrapper = IntWrapper(x + n)
    def mul(n: Int): IntWrapper = IntWrapper(x * n)
    def sub(n: Int): IntWrapper = IntWrapper(x - n)
  }

  // executing left to right, but there are some unnecessary boilerplate
  val res = IntWrapper(42).add(3).mul(3).x
}

object example03 {
  // using an implicit class
  implicit class MyInt(val x: Int) extends AnyVal {
    def add(n: Int): Int = x + n
    def mul(n: Int): Int = x * n
    def sub(n: Int): Int = x - n
  }

  // the compiler tries hard to make it compile
  42.add(2).mul(2).sub(2)
  // this is what it looks like in the end
  MyInt(MyInt(42).add(2)).sub(2)
}

object example04v0 {
  // Why AnyVal?
  implicit class RichInt(val x: Int) {
    def mul(n: Int): Int = x * n
  }

  def triple(x: RichInt): RichInt = x.mul(3)

  // scala> :javap example04v0
  //
  //  public example04v0$RichInt triple(example04v0$RichInt);
  //  descriptor: (Lexample04v0$RichInt;)Lexample04v0$RichInt;
  //  flags: ACC_PUBLIC
  //  Code:
  //    stack=3, locals=2, args_size=2
  //  0: aload_0
  //  1: aload_1
  //  2: iconst_3
  //  3: invokevirtual #28                 // Method example04v0$RichInt.mul:(I)I
  //  6: invokevirtual #30                 // Method RichInt:(I)Lexample04v0$RichInt;
  //  9: areturn

}

object example04v1 {
  // Why AnyVal?
  implicit class RichInt(val x: Int) extends AnyVal {
    def mul(n: Int): Int = x * n
  }
  def triple(x: RichInt): Int = x.mul(3)

  // scala> :javap example04v1
  //
  //    public int triple(int);
  //    descriptor: (I)I
  //      flags: ACC_PUBLIC
  //    Code:
  //      stack=3, locals=2, args_size=2
  //    0: getstatic     #23                 // Field example4v0$RichInt$.MODULE$:Lexample4v0$RichInt$;
  //    3: iload_1
  //    4: iconst_3
  //    5: invokevirtual #27                 // Method example4v0$RichInt$.mul$extension:(II)I
  //    8: ireturn

}

object example04v2 {
  // Why AnyVal?
  object RichInt {
    def mul(x: Int, n: Int): Int = x * n
  }
  def triple(x: Int): Int = RichInt.mul(x, 3)

  //    public int triple(int);
  //    descriptor: (I)I
  //      flags: ACC_PUBLIC
  //    Code:
  //      stack=3, locals=2, args_size=2
  //    0: getstatic     #19                 // Field example4v1$RichInt$.MODULE$:Lexample4v1$RichInt$;
  //    3: iload_1
  //    4: iconst_3
  //    5: invokevirtual #23                 // Method example4v1$RichInt$.mul:(II)I
  //    8: ireturn

}

object example05 {
  // extension methods can be put on anything

  implicit final class RichSeq[T](val seq: Seq[T]) extends AnyVal {
    def only = seq match {
      case Seq() => throw new IllegalArgumentException()
      case Seq(x) => x
      case other => throw new IllegalArgumentException()
    }
  }

  val seq = Seq(1)
  val onlyElem = seq.only
}

object example06v1 {

  import scala.language.implicitConversions

  implicit def string2Int(str: String) = str.toInt
  //  implicit def string2Int(str: String): Int = str.toInt // fails to compile - puzzler

  def double(n: Int) = n * 2
  double("42")
}

object example06v2 {

  import scala.language.implicitConversions

  def convertToInt(str: String): Int = str.toInt

  object hidden {

    implicit def string2Int(str: String): Int = convertToInt(str)

    def double(n: Int) = n * 2
    double("42")
  }
}

object example07 {
  // Implicit resolution rules can be complex
  object Hidden {
    implicit class MyInt(val x: Int) {
      def add(n: Int): Int = x + n
      def mul(n: Int): Int = x * n
    }
  }
  object Calc {

    import Hidden.MyInt

    def calc(x: MyInt) = x.mul(2).add(2)
  }

  object User {
    //      val nope = 42.add(2)
    Calc.calc(42)
  }
}

object example08 {
  // conflicting names don't compile

  implicit class MyInt1(val x: Int) {
    def add(n: Int): Int = x + n
  }

  implicit class MyInt2(val x: Int) {
    def add(n: Int): Int = x + n
  }

  //    val ambiguous = 42.add(2)
}

object example09 {
  // conflicting class names don't compile even if they have different methods

  object FirstPackage {
    implicit class MyInt(val x: Int) {
      def add(n: Int): Int = x + n
    }
  }

  object SecondPackage {
    implicit class MyInt(val x: Int) {
      def sub(n: Int): Int = x - n
    }
  }

  object FailsToCompile {
    //      val ambiguous = 42.add(2)
  }

  object Compiles {
    // use import alias to resolve
    import FirstPackage.{MyInt => MyInt2}
    import SecondPackage.MyInt

    val fortyfour = 42.add(2) // compiles

    val forty = 42.sub(2) // compiles
  }
}

object example10 extends App {
  trait Animal
  trait CanFly
  trait CanSwim

  class Duck extends Animal with CanFly with CanSwim {
  }

  object Implicits {

    implicit class AnimalsCanRest(x: Animal) {
      def rest() = println("Resting animal")
    }

    implicit class FlyersCanRest(x: CanFly) {
      def rest() = println("Resting flying animal")
    }

    implicit class SwimmersCanRest(x: CanSwim) {
      def rest() = println("Resting swimming animal")
    }

    implicit class DucksCanRest(x: Duck) {
      def rest() = println("Resting duck")
    }

  }

  object Test1 {
    println("--- Test 1 ---")

    import Implicits.FlyersCanRest

    val duck = new Duck
    duck.rest()
  }
  Test1

  object Test2 {
    println("--- Test 2 ---")

    val duck = new Duck
    //    duck.rest() // does not compile
  }
  Test2

  object Test3 {
    println("--- Test 3 ---")

    import Implicits.DucksCanRest
    // import order does not matter

    val duck = new Duck
    duck.rest() // does compile
  }
  Test3

  object Test4 {
    println("--- Test 4 ---")

    import Implicits._

    val duck = new Duck
    duck.rest()

    val animal: Animal = new Duck
    animal.rest()

    val flyer: CanFly = new Duck
    flyer.rest()

    val swimmer: CanSwim = new Duck
    swimmer.rest()
  }
  Test4

}