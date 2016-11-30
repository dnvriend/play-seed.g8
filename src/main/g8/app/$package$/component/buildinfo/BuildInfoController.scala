package $package$.component.buildinfo

import play.api.http.ContentTypes
import play.api.mvc.{ Action, Controller }

class BuildInfoController extends Controller {
  def info = Action(Ok(BuildInfo.toJson).as(ContentTypes.JSON))
}
