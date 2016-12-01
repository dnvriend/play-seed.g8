/*
 * Copyright 2016 Dennis Vriend
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dnvriend.component.kafka.controller

import akka.actor.ActorSystem
import akka.kafka.scaladsl.{ Consumer, Producer }
import akka.kafka.{ ConsumerSettings, ProducerSettings, Subscriptions }
import akka.stream.Materializer
import akka.stream.scaladsl.{ Sink, Source }
import com.github.dnvriend.component.kafka.dto.PersonDto
import com.github.dnvriend.component.kafka.repository.KafkaRepository
import com.google.inject.Inject
import org.apache.kafka.clients.producer.ProducerRecord
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.Try

class KafkaController @Inject() (kafkaRepository: KafkaRepository, producerSettings: ProducerSettings[String, Array[Byte]], consumerSettings: ConsumerSettings[String, Array[Byte]])(implicit system: ActorSystem, mat: Materializer, ec: ExecutionContext) extends Controller {
  val logger = Logger(this.getClass)

  def putOnKafka(name: String, age: Int) = Action.async {
    Source.single(PersonDto(name, age))
      .map(e => new ProducerRecord[String, Array[Byte]]("test", "partition", Json.toJson(e).toString.getBytes))
      .runWith(Producer.plainSink(producerSettings))
      .map(_ => Ok)
  }

  val done =
    Consumer.committableSource(consumerSettings, Subscriptions.topics("test"))
      .map { msg =>
        val json = Json.parse(msg.record.value()).as[PersonDto]
        logger.debug(json.toString)
        msg
      }
      .mapAsync(1) { msg =>
        val offset = msg.committableOffset.partitionOffset
        Future.fromTry(Try(kafkaRepository.updateOffset(offset.key.topic, offset.key.groupId, offset.key.partition, offset.offset)))
          .map(_ => msg)
      }
      .mapAsync(1) { msg =>
        msg.committableOffset.commitScaladsl()
      }.runWith(Sink.ignore)
}
