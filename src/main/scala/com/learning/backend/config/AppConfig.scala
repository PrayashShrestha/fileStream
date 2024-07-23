package com.learning.backend.config

import zio.*
import zio.config.*
import zio.config.magnolia.*
import zio.config.typesafe.*

case class HttpConfig(host: String, port: Int)
case class StorageConfig(directory: String)
case class AppConfig(http: HttpConfig, storage: StorageConfig)

object AppConfig {
  val live: ZLayer[Any, Config.Error, AppConfig] =
    ZLayer.fromZIO(
      TypesafeConfigProvider
        .fromResourcePath()
        .load(deriveConfig[AppConfig])
    )
}
