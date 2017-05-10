package scalaworkshop.implicitclasses.homework

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.prop.PropertyChecks

import scala.language.implicitConversions
import scala.language.reflectiveCalls
import scala.reflect.ClassTag
import scala.reflect._

class ExtensionMethodSuite extends FlatSpec with Matchers with PropertyChecks {
  import scalaworkshop.implicitclasses.homework.Implicits._

  type HasToPure[A] = {def toPure: PureList[A]}
  type HasToList[A] = {def toList: List[A]}
  type HasSum = {def sum: Int}

  implicit def defaultToPure[A: ClassTag](x: Any): HasToPure[A] =
    fail(s"No method with signature [def toPure: PureList[${ classTag[A].runtimeClass.getSimpleName }]] was found on type List[${ classTag[A].runtimeClass.getSimpleName }]")

  implicit def defaultToList[A: ClassTag](x: Any): HasToList[A] =
    fail(s"No method with signature [def toList: List[${ classTag[A].runtimeClass.getSimpleName }]] was found on type PureList[${ classTag[A].runtimeClass.getSimpleName }]")

  "toPure" should "be present on Nil" in {
    Nil.toPure
    succeed
  }

  it should "be present on List[Int]" in {
    List(1).toPure
    succeed
  }

  it should "be present on List[String]" in {
    List("").toPure
    succeed
  }

  it should "convert an empty List" in {
    Nil.toPure shouldBe Empty
  }

  it should "convert a List with a single element" in {
    List(1).toPure shouldBe Cons(1, Empty)
  }

  it should "convert a List with a multiple elements" in {
    List(1, 2).toPure shouldBe Cons(1, Cons(2, Empty))
  }

  "toList" should "be present on Empty" in {
    Empty.toList
    succeed
  }

  it should "be present on PureList[Int]" in {
    (Cons(1, Empty): PureList[Int]).toList
    succeed
  }

  it should "be present on PureList[String]" in {
    (Cons("", Empty): PureList[String]).toList
    succeed
  }

  it should "convert an empty PureList" in {
    Empty.toList shouldBe Nil
  }

  it should "convert a PureList with a single element" in {
    Cons(1, Empty).toList shouldBe List(1)
  }

  it should "convert a PureList with a multiple elements" in {
    Cons(1, Cons(2, Empty)).toList shouldBe List(1, 2)
  }

  "conversions" should "be consistent" in {
    forAll { l: List[Int] =>
      l.toPure.toList shouldBe l
    }
  }

  "sum" should "be present on PureList[Int]" in {
    implicit def defaultSum[A: ClassTag](x: Any): HasSum =
      fail(s"No method with signature [def sum: Int] was found on type PureList[Int]")

    (Cons(1, Empty): PureList[Int]).sum
    succeed
  }

  it should "not be present on PureList[String]" in {
    object EverythingsFineException extends RuntimeException
    implicit def defaultSum[A: ClassTag](x: Any): HasSum = new {def sum: Int = throw EverythingsFineException }

    withClue("Expected sum method to be missing on PureList[String]\n") {
      the[EverythingsFineException.type] thrownBy (Cons("", Empty): PureList[String]).sum
    }
  }

}