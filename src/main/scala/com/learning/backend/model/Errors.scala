package com.learning.backend.model

sealed trait AppError extends Throwable

case object UserNotFound extends AppError
case object FileNotFound extends AppError

case class InvalidCredentials(reason: String) extends AppError
case class InvalidInput(reason: String)       extends AppError
case class ServerError(reason: String)        extends AppError
