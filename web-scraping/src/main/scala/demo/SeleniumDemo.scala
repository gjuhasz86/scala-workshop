package demo

import java.lang.Thread.sleep

import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.{ExpectedConditions, WebDriverWait}
import org.openqa.selenium.{By, Keys, WebDriver}
import demo.Utils._

import scala.collection.JavaConverters._
import scala.util.Try

object SeleniumDemo {
  def main(args: Array[String]): Unit = { runSearch(3, 7, "scaladays") }

  def runSearch(from: Int, to: Int, searchTerm: String) = {
    // https://sites.google.com/a/chromium.org/chromedriver/downloads
    val chromeDriver = "/home/jaysicks/PRIVATE/programs/chromedriver/chromedriver"

    System.setProperty("webdriver.chrome.driver", chromeDriver)

    implicit val driver: WebDriver = new ChromeDriver()
    implicit val wait: WebDriverWait = new WebDriverWait(driver, 3)

    val details = Try {
      val links = getLinks(searchTerm)
      links.p
      val details = links.slice(from, to).map { link => getDetails(link) }
      driver.quit()
      details
    } getOrElse {
      driver.quit()
      Nil
    }

    details
  }

  def getLinks(searchTerm: String)(implicit driver: WebDriver) = {
    driver.get("https://www.youtube.com")


    val search = Try { driver.findElement(By.id("masthead-search-term")) }
      .getOrElse { driver.findElement(By.id("search-input")) }
      .sendKeys(searchTerm + Keys.ENTER)
    sleep(2000)

    val links = driver.findElements(By.className("yt-lockup-title"))
      .asScala.map { e =>
      val a = e.findElement(By.tagName("a"))
      a.getAttribute("href").p
    }
    links.toList
  }

  def getDetails(url: String)(implicit driver: WebDriver, wait: WebDriverWait) = {
    driver.get(url)

    driver.findElement(By.tagName("body"))
      .sendKeys(Keys.END)

    val title = driver.findElement(By.id("eow-title")).getAttribute("title")
    val viewCount = driver.findElement(By.className("watch-view-count")).getText

    val comments = Try {
      wait.until(ExpectedConditions.elementToBeClickable(By.className("comment-renderer")))
      driver
        .findElements(By.className("comment-renderer"))
        .asScala.toList
        .map { e =>
          val author = e.findElement(By.tagName("img")).getAttribute("alt")
          val text = e.findElement(By.className("comment-renderer-text-content")).getText
          Comment(author, text).p
        }
    }.toOption.getOrElse(Nil)

    VideoDetail(title, viewCount, comments).p
  }
}

case class VideoDetail(title: String, views: String, comments: List[Comment])
case class Comment(author: String, text: String)
