package $package$

package com.github.dnvriend.model

import play.api.libs.json.Json

object $className$ {
  implicit val format = Json.format[$className$]
}

final case class $className$(id: Long)