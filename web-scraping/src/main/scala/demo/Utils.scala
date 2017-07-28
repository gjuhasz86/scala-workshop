package demo

import java.io._

object Utils {
  implicit class AnyHelpers[T](self: T) {
    def pc: T = { println(self.getClass); self }
    def print = { println(self); self }
    def p: T = print
    def export(filename: String): T = {
      val pw = new PrintWriter(new File(filename))
      pw.write(self.toString)
      pw.close()
      self
    }
    def fp(implicit ev: T <:< Seq[_]): T = { self.foreach(println); self }
  }
}