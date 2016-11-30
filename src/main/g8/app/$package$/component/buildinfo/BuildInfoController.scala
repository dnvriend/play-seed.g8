package $package$.component.buildinfo

import play.api.http.ContentTypes
import play.api.mvc.{ Action, Controller }
import play.api.Logger

class BuildInfoController extends Controller {
  val logger = Logger(this.getClass)

  def info = Action(Ok(BuildInfo.toJson).as(ContentTypes.JSON))
}
