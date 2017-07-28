package demo

import scala.io.Source
import scalaj.http.Http
import Utils._
import io.circe.parser._
import io.circe.syntax._
import io.circe.generic.auto._

object HttpClientDemo {

  def main(args: Array[String]): Unit = {
    val json = Source.fromFile("scaladays.json").mkString

    val vids = decode[List[VideoDetail]](json).right.get
      .map(v => v.copy(comments = Nil))

    val result = Http("https://requestb.in/18chxor1")
      .header("content-type", "application/json")
      .cookie("foo", "bar")
      .postData(vids.asJson.spaces2)
      .asString

    result.p
  }
}
