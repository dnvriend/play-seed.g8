package $package$

import anorm._
import anorm.{Macro, RowParser}
import play.api.db.Database
import java.sql.Connection
import javax.inject.Inject
import play.api.Logger

object $entityName$ {
  val parser: RowParser[$entityName$] = Macro.namedParser[$entityName$]
}

final case class $entityName$(id: Option[Long])

class $className$ @Inject() (db: Database) {
   val logger = Logger(this.getClass)

   def get$className$(limit: Int, Offset: Int): List[$className$] = db.withConnection { implicit conn =>
     SQL"SELECT * FROM $className$ limit=#\$limit, offset=#\$offset".as($entityName$.parser.*)
   }

   def get$className$ById(id: Long): Option[Person] = db.withConnection { implicit conn =>
     SQL"SELECT * FROM $entityName$ WHERE id=#\$id".as($entityName$.parser.singleOpt)
   }

   def delete$className$(entity: $className$): Unit = db.withConnection { implicit conn =>
     SQL"DELETE FROM $entityName$ WHERE id=#\$id".executeUpdate
   }  

   def add$className$(entity: $className$): $className$ = db.withConnection { implicit conn =>
    import anorm.SqlParser.long
    val theId = SQL"INSERT INTO $entityName$ (id) values (\${$entityName$.id})".executeInsert(long(1).single)
    person.copy(id = Option(theId))
   }
}