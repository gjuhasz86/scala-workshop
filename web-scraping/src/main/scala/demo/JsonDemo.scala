package demo

import io.circe.generic.auto._
import io.circe.syntax._
import demo.Utils._

object JsonDemo {
  def main(args: Array[String]): Unit = {
    val details = SeleniumDemo.runSearch(3, 7, "scaladays")
    val json =
      details.asJson.spaces2
        .print
        .export("scaladays.json")

  }

}

