package scalaworkshop.implicitclasses.homework

import org.scalatest.FlatSpec
import org.scalatest.Matchers
import org.scalatest.prop.PropertyChecks

import scala.language.implicitConversions
import scala.language.reflectiveCalls

class PureListUtilsSuite extends FlatSpec with Matchers with PropertyChecks {
  import scalaworkshop.implicitclasses.homework.PureListUtils._
  import scalaworkshop.implicitclasses.homework.Implicits._

  type HasToPure[A] = {def toPure: PureList[A]}
  type HasToList[A] = {def toList: List[A]}

  implicit def defaultToPure[A](x: Any): HasToPure[A] = new {def toPure: PureList[A] = ??? }
  implicit def defaultToList[A](x: Any): HasToList[A] = new {def toList: List[A] = ??? }

  implicit def autoPure[A](l: List[A]): PureList[A] = l.toPure

  "sum" should "be 0 for an empty PureList" in {
    sum(Empty) shouldBe 0
  }

  it should "be the value of the single element for a single element PureList" in {
    sum(List(3)) shouldBe 3
  }

  it should "add up the elements of a PureList" in {
    sum(List(3, 6, 2)) shouldBe 11
  }

  "range" should "be 0 for an empty PureList" in {
    range(Empty) shouldBe 0
  }

  it should "be 0 for a single element PureList" in {
    range(List(3)) shouldBe 0
  }

  it should "be the difference between the highest and lowest element #1" in {
    range(List(2, 5)) shouldBe 3
  }

  it should "be the difference between the highest and lowest element #2" in {
    range(List(2, 5, -3, 8, 5)) shouldBe 11
  }

  it should "be the difference between the highest and lowest element #3" in {
    range(List(2, 0, -3, 0, 5)) shouldBe 8
  }

  "ascendPrev" should "be an Empty for an empty PureList" in {
    ascendPrev(Empty) shouldBe Empty
  }

  it should "be true for a PureList with a single positive element" in {
    ascendPrev(List(2)) shouldBe List(true).toPure
  }

  it should "be true for a PureList with a single 0 element" in {
    ascendPrev(List(0)) shouldBe List(true).toPure
  }

  it should "be true for a PureList with a single negative element" in {
    ascendPrev(List(-2)) shouldBe List(true).toPure
  }

  it should "start with true regardless of the second element" in {
    ascendPrev(List(4, 3)) shouldBe List(true, false).toPure
  }

  it should "start with true even if the first element is a very large negative number" in {
    ascendPrev(List(Int.MinValue, 3)) shouldBe List(true, true).toPure
  }

  it should "always start with true" in {
    forAll { l: List[Int] =>
      l match {
        case Nil => ascendPrev(l) shouldBe Empty
        case _ => ascendPrev(l).toList.head shouldBe true
      }
    }
  }

  it should "be true if the element is greater than the previous one for each elemenet in the PureList" in {
    ascendPrev(List(3, 4, -1, -6, 9, 9, 5)) shouldBe List(true, true, false, false, true, false, false).toPure
  }

  "ascendNext" should "be an Empty for an empty PureList" in {
    ascendNext(Empty) shouldBe Empty
  }

  it should "be true for a PureList with a single positive element" in {
    ascendNext(List(2)) shouldBe List(true).toPure
  }

  it should "be true for a PureList with a single 0 element" in {
    ascendNext(List(0)) shouldBe List(true).toPure
  }

  it should "be true for a PureList with a single negative element" in {
    ascendNext(List(-2)) shouldBe List(true).toPure
  }


  it should "end with true regardless of the penultimate element" in {
    ascendNext(List(4, 3)) shouldBe List(false, true).toPure
  }

  it should "end with true even if the last element is a very large negative number" in {
    ascendNext(List(3, Int.MinValue)) shouldBe List(false, true).toPure
  }

  it should "always end with true" in {
    forAll { l: List[Int] =>
      l match {
        case Nil => ascendNext(l) shouldBe Empty
        case _ => ascendNext(l).toList.last shouldBe true
      }
    }
  }

  it should "be true if the following element is greater than the current one for each element in the PureList" in {
    ascendNext(List(3, 4, -1, -6, 9, 9, 5)) shouldBe List(true, false, false, true, false, false, true).toPure
  }

}