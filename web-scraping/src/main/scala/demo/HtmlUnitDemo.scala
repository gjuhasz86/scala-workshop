package demo

import java.util.logging.{Level, Logger}

import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html._
import demo.Utils._

import scala.collection.JavaConverters._

object HtmlUnitDemo {

  def main(args: Array[String]): Unit = {
    Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF)

    val client = new WebClient()
    client.getOptions.setThrowExceptionOnScriptError(false)
    val page: HtmlPage = client.getPage("https://www.youtube.com/results?search_query=scaladays")

    val tbSearch = page.querySelectorAll(".yt-lockup-title ")
      .asInstanceOf[DomNodeList[_]].asScala.foreach { case e: HtmlHeading3 =>
      e.getFirstChild.getTextContent.p
    }

    println()
    val token = page.executeJavaScript("JSON.stringify(window.ytcsi)").getJavaScriptResult.toString
    println(token)
  }

}

