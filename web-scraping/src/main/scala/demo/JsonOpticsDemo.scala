package demo

import io.circe._
import io.circe.optics.JsonPath._
import io.circe.parser._
import demo.Utils._

import scala.io.Source
import scalaj.http.Http

object JsonOpticsDemo {
  def main(args: Array[String]): Unit = {
    val jsonStr = Source.fromFile("scaladays.json").mkString

    val json = parse(jsonStr).getOrElse(Json.Null)

    val authors = root.each.comments.each.author.as[String].getAll(json)
    authors.sorted.fp
  }
}
