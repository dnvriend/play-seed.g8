package $package$

import javax.inject.Inject
import play.api.mvc.{ Action, Controller }
import play.api.Logger

class $className$ @Inject() () extends Controller {
   val logger = Logger(this.getClass)
   def action = Action(Ok)
}