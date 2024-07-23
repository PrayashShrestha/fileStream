package com.learning.backend.model

import zio.json._

case class FileMetaData(id: String, filename: String, size: Long)

object FileMetaData {
  implicit val decoder: JsonDecoder[FileMetaData] = DeriveJsonDecoder.gen[FileMetaData]
  implicit val encoder: JsonEncoder[FileMetaData] = DeriveJsonEncoder.gen[FileMetaData]
}


