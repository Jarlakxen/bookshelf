package com.jarlakxen.tools.bookshelf.domain.utils

import scala.util.{Try, Success}

object ExceptionOption {
	def apply[T]( f : => T ) : Option[T] = Try( f ) match { case Success( x ) => Some( x ) case _ => None }
	def apply[T]( f : => T, fallback : T ) : T = Try( f ) getOrElse fallback
}