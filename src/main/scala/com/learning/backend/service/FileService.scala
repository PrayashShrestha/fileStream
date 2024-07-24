package com.learning.backend.service

import com.example.repository._
import zio._
import zio.stream._

trait FileService {
  def upload(file: Chunk[Byte], metadata: FileMetadata): IO[AppError, Unit]
  def download(fileId: String): IO[AppError, Chunk[Byte]]
  def stream(fileId: String): IO[AppError, ZStream[Any, Throwable, Byte]]
  def accept(fileId: String): IO[AppError, Unit]
  def softDelete(fileId: String): IO[AppError, Unit]
}

object FileService {
  val live: ZLayer[FileRepository, Nothing, FileService] = ZLayer.fromFunction { repo: FileRepository =>
    new FileService {
      def upload(file: Chunk[Byte], metadata: FileMetadata): IO[AppError, Unit] = repo.save(file, metadata)

      def download(fileId: String): IO[AppError, Chunk[Byte]] = repo.read(fileId)

      def stream(fileId: String): IO[AppError, ZStream[Any, Throwable, Byte]] = ZIO.succeed {
        ZStream.fromInputStream(
          java.nio.file.Files.newInputStream(java.nio.file.Paths.get(s"${repo.storageDir}/$fileId"))
        )
      }

      def accept(fileId: String): IO[AppError, Unit] = repo.read(fileId).flatMap { _ =>
        // Implement your logic to accept a file
        ZIO.succeed(())
      }

      def softDelete(fileId: String): IO[AppError, Unit] = repo.delete(fileId)
    }
  }
}
