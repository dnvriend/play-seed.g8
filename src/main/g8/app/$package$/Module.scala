package $package$

import akka.util.Timeout
import com.google.inject.AbstractModule
import com.google.inject.name.Names
import play.api.libs.concurrent.AkkaGuiceSupport

import scala.concurrent.duration._

class Module extends AbstractModule with AkkaGuiceSupport {
  override def configure(): Unit = {
    bind(classOf[Int])
      .annotatedWith(Names.named("test.port"))
      .toInstance(9001)

    bind(classOf[Timeout])
      .toInstance(Timeout(10.seconds))
  }
}
