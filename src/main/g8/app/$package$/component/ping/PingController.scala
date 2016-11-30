package $package$.component.ping

import play.api.mvc.{Action, Controller}
import play.api.Logger

class PingController extends Controller {
  val logger = Logger(this.getClass)

  def ping = Action(Ok("pong"))
}