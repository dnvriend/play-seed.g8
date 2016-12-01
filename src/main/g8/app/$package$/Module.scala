package $package$

import akka.actor.ActorSystem
import akka.kafka.{ ConsumerSettings, ProducerSettings }
import akka.util.Timeout
import com.google.inject.name.Names
import com.google.inject.{ AbstractModule, Provides }
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ ByteArrayDeserializer, ByteArraySerializer, StringDeserializer, StringSerializer }
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
  
  @Provides
  def producerSettings(system: ActorSystem): ProducerSettings[String, Array[Byte]] = {
    ProducerSettings(system, new StringSerializer, new ByteArraySerializer)
      .withBootstrapServers("localhost:9092")
  }

  @Provides
  def consumerSettings(system: ActorSystem): ConsumerSettings[String, Array[Byte]] =
    ConsumerSettings(system, new StringDeserializer, new ByteArrayDeserializer)
      .withBootstrapServers("localhost:9092")
      .withGroupId("group1")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")  
}
