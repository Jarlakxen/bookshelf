package com.jarlakxen.tools.bookshelf.domain.utils

object ExceptionOption {
	def apply[T]( f : => T ) : Option[T] =  try Some(f) catch { case _ => None }
	def apply[T]( f : => T, fallback : T ) : T = try f catch { case _ => fallback }
}