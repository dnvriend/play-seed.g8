package $package$.component.kafka.repository

import javax.inject.Inject

import anorm._
import play.api.Logger
import play.api.db.Database

import scala.util.{ Failure, Try }

class KafkaRepository @Inject() (db: Database) {
  val logger = Logger(this.getClass)

  def updateOffset(topic: String, groupId: String, partition: Int, offset: Long): Try[Int] = db.withConnection { implicit conn =>
    Try(SQL"UPDATE kafka_offsets SET topic_offset=#\$offset WHERE topic='#\$topic' AND group_id='#\$groupId' AND partition=#\$partition".executeUpdate()).recoverWith {
      case t: Throwable =>
        logger.error("Could not update offset", t)
        Try(SQL"INSERT INTO kafka_offsets (group_id, topic, partition, topic_offset) VALUES ('#\$groupId', '#\$topic', #\$partition, #\$offset)".executeUpdate()).recoverWith {
          case t: Throwable =>
            logger.error("Could not insert offset", t)
            Failure(t)
        }
    }
  }

  def findOffset(topic: String, groupId: String, partition: Int): Option[Long] = db.withConnection { implicit conn =>
    SQL"SELECT topic_offset FROM kafka_offsets WHERE topic='#\$topic' AND group_id='#\$groupId' AND partition=#\$partition".as(SqlParser.scalar[Long].singleOpt)
  }
}
