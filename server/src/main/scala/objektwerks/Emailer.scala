package objektwerks

import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging

import jodd.mail.{Email, MailServer, SmtpServer}
import jodd.mail.EmailFilter.*

import scala.annotation.tailrec
import scala.language.postfixOps
import scala.util.{Failure, Success, Using, Try}

import zio.{ZIO, ZLayer}

final case class Emailer(config: Config) extends LazyLogging:
  private val host = config.getString("host")
  private val sender = config.getString("sender")
  private val password = config.getString("password")

  private val smtpServer: SmtpServer = MailServer.create()
    .host(host)
    .ssl(true)
    .auth(sender, password)
    .buildSmtpMailServer()

  @tailrec
  private def retry[T](attempts: Int)(fn: => T): T =
    Try( fn ) match
      case Success(result) => result
      case Failure(error)  => if attempts >= 1 then retry(attempts - 1)(fn) else throw error

  private def sendEmail(recipients: List[String],
                        subject: String,
                        message: String): Boolean =
    Using( smtpServer.createSession ) { session =>
      val email = Email.create()
        .from(sender)
        .subject(subject)
        .textMessage(message, "UTF-8")
        .cc(sender)
      recipients.foreach( recipient => email.to(recipient) )
      session.open()
      val messageId = session.sendMail(email)
      logger.info("*** Emailer subject: {} to: {} message id: {}", subject, recipients.mkString, messageId)
    }.recover { error =>
      logger.error("*** Emailer subject: {} to: {} failed: {}",  subject, recipients.mkString, error.getMessage)
    }.isSuccess

  def send(recipients: List[String],
           subject: String,
           message: String): Boolean = retry(1)(sendEmail(recipients, subject, message))

object Emailer:
  def layer(config: Config): ZLayer[Any, Nothing, Emailer] =
    ZLayer {
      ZIO.succeed( Emailer(config) )
    }