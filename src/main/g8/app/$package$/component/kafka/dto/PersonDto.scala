package $package$.component.kafka.dto

import play.api.libs.json.Json

object PersonDto {
  implicit val format = Json.format[PersonDto]
}
final case class PersonDto(name: String, age: Int)
