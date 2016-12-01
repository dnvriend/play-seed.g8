package $package$

import anorm._
import anorm.{Macro, RowParser}
import play.api.db.Database
import java.sql.Connection
import javax.inject.Inject
import play.api.Logger

object $entityName$ {
  val namedParser: RowParser[$entityName$] = Macro.namedParser[$entityName$]
  val indexedParser: RowParser[$entityName$] = Macro.indexedParser[$entityName$]
  val offsetParser: RowParser[$entityName$] = Macro.offsetParser[$entityName$]
}

final case class $entityName$(id: Option[Long])

class $className$ @Inject() (db: Database) {
   val logger = Logger(this.getClass)

   def take(limit: Int, offset: Int): List[$entityName$] = db.withConnection { implicit conn =>
     SQL"SELECT * FROM $entityName;format="lower"$ limit=#\$limit, offset=#\$offset".as($entityName$.parser.*)
   }

   def findById(id: Long): Option[$entityName$] = db.withConnection { implicit conn =>
     SQL"SELECT * FROM $entityName;format="lower"$ WHERE id=#\$id".as($entityName$.parser.singleOpt)
   }

   def delete($entityName;format="lower"$: $entityName$): Unit = db.withConnection { implicit conn =>
     SQL"DELETE FROM $entityName;format="lower"$ WHERE id=\${$entityName;format="lower"$.id}".executeUpdate
   }  

   def add($entityName;format="lower"$: $entityName$): $entityName$ = db.withConnection { implicit conn =>
    import anorm.SqlParser.long
    val theId = SQL"INSERT INTO $entityName;format="lower"$ (id) values (\${$entityName;format="lower"$.id})".executeInsert(long(1).single)
    $entityName;format="lower"$.copy(id = Option(theId))
   }
}