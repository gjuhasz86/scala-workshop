package demo

import scalaj.http.Http
import Utils._
import io.circe.optics.JsonPath._
import io.circe.parser._

object JsonOpticsDemo2 {
  def main(args: Array[String]): Unit = {
    val jsonStr = Http("https://api.github.com/search/repositories?q=topic:scala")
      .asString
      .body

    val json = parse(jsonStr).right.get

    json.spaces2.p

    val res = root.items.each.name.as[String].getAll(json)
    res.size.p
    res.fp

  }
}
