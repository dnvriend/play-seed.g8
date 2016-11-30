package $package$.util

import play.api.mvc.{Result, Results}

import scala.concurrent.{ExecutionContext, Future}

object ActionOps extends Results {

  implicit class FutureImplicits(val self: Future[_]) extends AnyVal {
    def mapOk(message: String)(implicit ec: ExecutionContext): Future[Result] =
      self.map(_ => Ok(message))
  }

  implicit class ResultFutureImplicits(val self: Future[Result]) extends AnyVal {
    def recoverInternalServerError(implicit ec: ExecutionContext): Future[Result] =
      self.recover {
        case e: Throwable =>
          InternalServerError(e.getMessage)
      }
  }
}