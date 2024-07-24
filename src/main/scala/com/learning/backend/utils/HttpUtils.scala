package com.learning.backend.utils

import zio.http._

object HttpUtils {
  def combineRoutes(apps: Routes[Any, Throwable]*): Routes[Any, Throwable] = {
    apps.reduce(_ <> _)
  }
}
