package $package$

import akka.util.Timeout
import akka.event.{Logging, LoggingAdapter}
import akka.pattern.CircuitBreaker
import com.google.inject.name.Names
import com.google.inject.{AbstractModule, Provider, Provides}
import play.api.libs.concurrent.AkkaGuiceSupport
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class Module extends AbstractModule with AkkaGuiceSupport {
  override def configure(): Unit = {
    bind(classOf[Int])
      .annotatedWith(Names.named("test.port"))
      .toInstance(9001)

    bind(classOf[Timeout])
      .toInstance(Timeout(10.seconds))
  }

  @Provides
  def loggingAdapter(system: ActorSystem): LoggingAdapter = {
    Logging(system, this.getClass)
  }
}
