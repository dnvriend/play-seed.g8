package $package$

import javax.inject.Inject
import play.api.libs.ws.{WSClient, WSRequest}
import scala.concurrent.{ExecutionContext, Future}
import play.api.Logger
import $package$.$className$._

object $className$ {
  def getUrl(path: String, tls: Boolean = false): String =
    if (tls) s"https://$Host:443$path" else s"http://$Host$path"
}

class $className$ @Inject() (wsClient: WSClient)(implicit ec: ExecutionContext) {
   val logger = Logger(this.getClass)

   def get(): Future[Int] =
      wsClient.url(getUrl("/get"))
        .flatMap(_.get().map(_.status))

  def getTls(): Future[Int] =
      wsClient.url(getUrl("/get", tls = true))
        .flatMap(_.get().map(_.status))

  def basicAuth(username: String, password: String): Future[Int] =
      wsClient.url(getUrl("/basic-auth/foo/bar"))
        .flatMap(_.withAuth(username, password, WSAuthScheme.BASIC).get().map(_.status))

  def basicAuthTls(username: String, password: String): Future[Int] =
      wsClient.url(getUrl("/basic-auth/foo/bar", tls = true))
        .flatMap(_.withAuth(username, password, WSAuthScheme.BASIC).get().map(_.status))

  def post[A: Format](a: A): Future[Int] =
      wsClient.url(getUrl("/post"))
        .flatMap(_.post(Json.toJson(a)).map(_.status))

  def postTls[A: Format](a: A): Future[Int] =
      wsClient.url(getUrl("/post", tls = true))
        .flatMap(_.post(Json.toJson(a)).map(_.status))

  def put[A: Format](a: A): Future[Int] =
      wsClient.url(getUrl("/put"))
        .flatMap(_.put(Json.toJson(a)).map(_.status))

  def delete(): Future[Int] =
      wsClient.url(getUrl("/delete"))
        .flatMap(_.delete().map(_.status))

  def patch[A: Format](a: A): Future[Int] =
      wsClient.url(getUrl("/patch"))
        .flatMap(_.patch(Json.toJson(a)).map(_.status))
}