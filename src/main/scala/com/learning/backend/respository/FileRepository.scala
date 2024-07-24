package com.learning.backend.respository

import zio._
import java.nio.file.{Files, Paths}

trait FileRepository {
  def save(file: Chunk[Byte], metadata: FileMetadata): IO[AppError, Unit]
  def read(fileId: String): IO[AppError, Chunk[Byte]]
  def delete(fileId: String): IO[AppError, Unit]
}

object FileRepository {
  val live: ZLayer[AppConfig, Nothing, FileRepository] = ZLayer.fromService { config: AppConfig =>
    new FileRepository {
      private val storageDir = config.storage.directory

      def save(file: Chunk[Byte], metadata: FileMetadata): IO[AppError, Unit] = ZIO
        .attempt {
          val path = Paths.get(s"$storageDir/${metadata.filename}")
          Files.write(path, file.toArray)
        }
        .refineToOrDie[AppError]

      def read(fileId: String): IO[AppError, Chunk[Byte]] = ZIO
        .attempt {
          val path = Paths.get(s"$storageDir/$fileId")
          Chunk.fromArray(Files.readAllBytes(path))
        }
        .refineToOrDie[AppError]

      def delete(fileId: String): IO[AppError, Unit] = ZIO
        .attempt {
          val path = Paths.get(s"$storageDir/$fileId")
          Files.delete(path)
        }
        .refineToOrDie[AppError]
    }
  }
}
