package $package$.controllers

import javax.inject.Inject

import play.api.libs.json.{ JsValue, Json }
import play.api.mvc.{ AbstractController, ControllerComponents }

class IndexController @Inject() (cc: ControllerComponents) extends AbstractController(cc) {

  def index(num: Int) = Action {
    Ok(Json.toJson(List.fill(num)("lorem ipsum").mkString(",")))
  }

}