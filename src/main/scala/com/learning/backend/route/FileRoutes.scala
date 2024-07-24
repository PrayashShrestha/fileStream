package com.learning.backend.route

import com.example.services.FileService
import zio._
import zio.http._
import com.example.domain._

object FileRoutes {
  private val prefix = "file"

  val routes = Http.collectZIO[Request] {
    case req @ Method.POST -> !! / `prefix` / "upload" =>
      for {
        file    <- req.body.asChunk
        metadata = FileMetadata("fileId", "filename", file.size)
        _       <- FileService.upload(file, metadata)
      } yield Response.text("File uploaded successfully")

    case Method.GET -> !! / `prefix` / "download" / fileId =>
      for {
        file <- FileService.download(fileId)
      } yield Response(
        data = HttpData.fromChunk(file),
        headers = List(Header("Content-Disposition", s"attachment; filename=$fileId"))
      )

    case Method.GET -> !! / `prefix` / "stream" / fileId =>
      for {
        stream <- FileService.stream(fileId)
      } yield Response(data = HttpData.fromStream(stream))

    case Method.POST -> !! / `prefix` / "accept" / fileId =>
      for {
        _ <- FileService.accept(fileId)
      } yield Response.text(s"File $fileId accepted")

    case Method.POST -> !! / `prefix` / "softdelete" / fileId =>
      for {
        _ <- FileService.softDelete(fileId)
      } yield Response.text(s"File $fileId soft deleted")
  }
}
