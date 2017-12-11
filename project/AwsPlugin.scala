import com.amazonaws.auth._
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.services.identitymanagement.model.User
import com.amazonaws.services.identitymanagement.{AmazonIdentityManagement, AmazonIdentityManagementClientBuilder}
import com.amazonaws.services.kinesis.model.{CreateStreamResult, DeleteStreamResult, StreamDescription}
import com.amazonaws.services.kinesis.{AmazonKinesis, AmazonKinesisClientBuilder}
import sbt.Keys._
import sbt._
import sbt.complete.DefaultParsers._

import scala.collection.JavaConverters._
import scalaz.Scalaz._

trait AwsPluginKeys {
  // aws clients
  lazy val clientIam = SettingKey[AmazonIdentityManagement]("Returns the amazon identity and access management (IAM client")
  lazy val clientKinesis = SettingKey[AmazonKinesis]("Returns the kinesis client")

  // iam tasks
  lazy val whoAmI = taskKey[Unit]("Returns the current user")
  lazy val iamUser = taskKey[User]("Returns the current user")
  lazy val iamUsers = taskKey[List[User]]("Returns all the users in the account")
  lazy val iamShowUser = taskKey[Unit]("Shows the current user")
  lazy val iamShowUsers = taskKey[Unit]("Shows all the users in the account")

  // kinesis tasks
  lazy val kinesisListStreams = taskKey[List[String]]("Returns the names of the streams associated with the account")
  lazy val kinesisDescribeStream = inputKey[Unit]("Describes a stream")
  lazy val kinesisCreateStream = inputKey[Unit]("Creates a kinesis stream")
  lazy val kinesisDeleteStream = inputKey[Unit]("Deletes a kinesis stream")
}
object AwsPluginKeys extends AwsPluginKeys

object AwsPlugin extends AutoPlugin {
  override def trigger = allRequirements
  override def requires = plugins.JvmPlugin

  val autoImport = AwsPluginKeys
  import autoImport._


  override def projectSettings = Seq(
    // aws clients
    clientKinesis := KinesisOperations.client(),
    clientIam := IamOperations.client(),
    // kinesis tasks
    kinesisListStreams := KinesisOperations.listStreams(clientKinesis.value),
    kinesisListStreams := (kinesisListStreams keepAs kinesisListStreams).value,
    kinesisDescribeStream := {
      val streamName = Defaults.getForParser(kinesisListStreams)((state, functions) => {
        val strings = functions.getOrElse(Nil)
        Space ~> StringBasic.examples(strings: _*)
      }).parsed
      val log = streams.value.log
      val description = KinesisOperations.describeStream(streamName, clientKinesis.value)
      log.info(
        s"""
           |Stream Description
           |====================================
           |StreamName: ${description.getStreamName}
           |StreamStatus: ${description.getStreamStatus}
           |StreamArn:  ${description.getStreamARN}
           |KeyId: ${description.getKeyId}
         """.stripMargin)
    },

    kinesisCreateStream := {
      val log = streams.value.log
      val streamName = Def.spaceDelimited("stream-name").examples(" " + name.value).parsed
      KinesisOperations.createStream(streamName.head, clientKinesis.value)
      log.info(s"Created the stream: $streamName")
    },

    kinesisDeleteStream := {
      val log = streams.value.log
      val streamName = Defaults.getForParser(kinesisListStreams)((state, functions) => {
        val strings = functions.getOrElse(Nil)
        Space ~> StringBasic.examples(strings: _*)
      }).parsed
      val input = UserInput.readInput("Are you sure? (y/n)")
      require(input == "y", "canceled deleting stream")
      KinesisOperations.deleteStream(streamName, clientKinesis.value)
      log.info(s"Deleting the stream: $streamName")
    },

    iamUser := IamOperations.getUser(clientIam.value),
    iamUser := (iamUser keepAs iamUser).value,

    iamShowUser := {
      val log = streams.value.log
      val user = iamUser.value
      log.info(
        s"""
           |User Info
           |================================================
           |Username: ${user.getUserName}
           |UserId: ${user.getUserId}
           |Arn: ${user.getArn}
           |CreateDate: ${user.getCreateDate}
           |PasswordLastUsed: ${user.getPasswordLastUsed}
           |Path: ${user.getPath}
         """.stripMargin)
    },
    whoAmI := iamShowUser.value,

    iamUsers := IamOperations.listUsers(clientIam.value),
    iamUsers := (iamUsers keepAs iamUsers).value,
    iamShowUsers := {
      val log = streams.value.log
      val users = iamUsers.value
      users.foreach(user => log.info(s"* ${user.getUserName} - ${user.getArn} - LastUsed: ${user.getPasswordLastUsed}"))
    },
  )
}

object GetCredentialsProvider {
  def getCredentialsAndRegion(awsAccessKeyId: Option[String],
                              awsSecretAccessKey: Option[String],
                              profile: Option[String]): AWSCredentialsProvider = {

    val chain: List[Option[AWSCredentialsProvider]] = List(
      Option(new SystemPropertiesCredentialsProvider()),
      Option(new EnvironmentVariableCredentialsProvider),
      (awsAccessKeyId |@| awsSecretAccessKey)((accessKey, secretKey) => new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey))),
      Option(new ProfileCredentialsProvider(profile.orNull))
    )
    new AWSCredentialsProviderChain(chain.flatten.asJava)
  }
}

object KinesisOperations {
  def client(): AmazonKinesis = {
    AmazonKinesisClientBuilder.defaultClient()
  }

  /**
    * Creates a Kinesis Stream
    */
  def createStream(streamName: String, client: AmazonKinesis): CreateStreamResult = {
    client.createStream(streamName, 1)
  }

  /**
    * Deletes a Kinesis stream
    */
  def deleteStream(streamName: String, client: AmazonKinesis): DeleteStreamResult = {
    client.deleteStream(streamName)
  }

  /**
    * Returns the names of the streams that are associated with the AWS account making the ListStreams request.
    */
  def listStreams(client: AmazonKinesis): List[String] = {
    client.listStreams().getStreamNames.asScala.toList
  }

  /**
    * Describes the stream, the information returned includes the stream name, Amazon Resource Name (ARN), creation time, enhanced metric
    * configuration, and shard map. The shard map is an array of shard objects. For each shard object, there is the
    * hash key and sequence number ranges that the shard spans, and the IDs of any earlier shards that played in a role
    * in creating the shard. Every record ingested in the stream is identified by a sequence number, which is assigned
    * when the record is put into the stream.
    */
  def describeStream(streamName: String, client: AmazonKinesis): StreamDescription = {
    client.describeStream(streamName).getStreamDescription
  }

  /**
    * Returns all stream descriptions
    */
  def describeAllStreams(client: AmazonKinesis): List[StreamDescription] = {
    val describeStreamFunction = describeStream(_: String, client)
    listStreams(client).map(describeStreamFunction)
  }
}

object IamOperations {
  def client(): AmazonIdentityManagement = {
    AmazonIdentityManagementClientBuilder.defaultClient()
  }

  /**
    * Returns all users in the AWS account.
    */
  def listUsers(client: AmazonIdentityManagement): List[User] = {
    client.listUsers().getUsers.asScala.toList
  }

  /**
    * Returns the user based on the AWS access key ID used to sign the request to this API.
    */
  def getUser(client: AmazonIdentityManagement): User = {
    client.getUser.getUser
  }
}

object UserInput {
  def readInput(prompt: String): String = {
    SimpleReader.readLine(s"$prompt\n") getOrElse {
      val badInputMessage = "Unable to read input"
      val updatedPrompt = if (prompt.startsWith(badInputMessage)) prompt else s"$badInputMessage\n$prompt"
      readInput(updatedPrompt)
    }
  }
}