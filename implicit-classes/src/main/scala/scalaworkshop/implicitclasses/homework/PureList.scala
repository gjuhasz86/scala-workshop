package scalaworkshop.implicitclasses.homework

// Do not modify this file
sealed trait PureList[+A]
case object Empty extends PureList[Nothing]
case class Cons[A](head: A, tail: PureList[A]) extends PureList[A]