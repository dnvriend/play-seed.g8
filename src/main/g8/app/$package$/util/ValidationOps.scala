package $package$.util

import scala.concurrent.{ExecutionContext, Future}
import scalaz.Scalaz._
import scalaz._

object ValidationOps {
  implicit class ValidationNelToFuture[A](val that: ValidationNel[String, A]) extends AnyVal {
    def toFuture: Future[ValidationNel[String, A]] = Future.successful(that)
  }

  implicit class DisjunctionNelToString[A](val that: Disjunction[NonEmptyList[String], A]) extends AnyVal {
    def toFuture: Future[Disjunction[String, A]] = Future.successful(that.leftMap(_.toList.mkString(",")))
  }

  implicit class ValidationNelOps[A](val that: ValidationNel[String, A]) extends AnyVal {
    def toDisjunction: Future[Disjunction[String, A]] = that.disjunction.toFuture
    def liftEither: DisjunctionT[Future, String, A] = toDisjunction.liftEither
  }

  implicit class FutureDisjunctionOps[A](val that: Future[Disjunction[String, A]]) extends AnyVal {
    def liftEither: DisjunctionT[Future, String, A] = EitherT(that)
  }

  implicit class FutureSeqOps[A](val that: Future[Seq[A]]) extends AnyVal {
    def liftEither(implicit ec: ExecutionContext): DisjunctionT[Future, String, Seq[A]] = EitherT(that.map(_.right[String]))
  }

  implicit class FutureOptionOps[A](val that: Future[Option[A]]) extends AnyVal {
    def liftEither(msg: String)(implicit ec: ExecutionContext): DisjunctionT[Future, String, A] = EitherT(that.map(_.toRightDisjunction(msg)))
  }

  implicit class FutureUnitOps(val that: Future[Unit]) extends AnyVal {
    def liftEither(implicit ec: ExecutionContext): DisjunctionT[Future, String, Unit] =
      EitherT(that.map(_ => Disjunction.right[String, Unit](())))
  }

  def validateUUID(fieldName: String, value: String): ValidationNel[String, UUID] =
    Validation.fromTryCatchNonFatal(java.util.UUID.fromString(value))
      .leftMap(t => s"Field '\$fieldName\' is not a UUID, its current value is: '\$value'. The underlying error is: '\${t.toString}'".wrapNel)

  def validateNonEmpty(fieldName: String, value: String): ValidationNel[String, String] =
    Option(value).filter(_.trim.nonEmpty).toSuccessNel(s"Field '\$fieldName' is empty")

  def validateNonZero(fieldName: String, value: Long): ValidationNel[String, Long] =
    Option(value).filter(_ == 0).toSuccessNel(s"Field '\$fieldName' with value '\$value' may not be zero")    

  def validateNonNegative(fieldName: String, value: Long): ValidationNel[String, Long] =
    Option(value).filter(_ < 0).toSuccessNel(s"Field '\$fieldName' with value '\$value' may not be less than zero")    
}