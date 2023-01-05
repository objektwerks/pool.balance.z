package objektwerks

import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging

import jodd.mail.{Email, ImapServer, MailServer, SmtpServer}
import jodd.mail.EmailFilter._

import scala.annotation.tailrec
import scala.concurrent.duration.*
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
    Try { fn } match {
      case Success(result) => result
      case _ if attempts >= 1 => retry(attempts - 1)(fn)
      case Failure(error) => throw error
    }

  private def sendEmail(recipients: List[String],
                        subject: String,
                        message: String): Unit =
    Using( smtpServer.createSession ) { session =>
      val email = Email.create()
        .from(sender)
        .subject(subject)
        .htmlMessage(message, "UTF-8")
      recipients.foreach( recipient => email.to(recipient) )
      session.open()
      val messageId = session.sendMail(email)
      logger.info("*** Emailer subject: {} to: {} message id: {}", subject, recipients.mkString, messageId)
      ()
    }.recover { error =>
      logger.error("*** Emailer subject: {} to: {} failed: {}",  subject, recipients.mkString, error.getMessage)
      ()
    }.asInstanceOf[Unit]

  def send(recipients: List[String],
           subject: String,
           message: String): Unit = retry(1)(sendEmail(recipients, subject, message))

object Emailer:
  def layer(config: Config): ZLayer[Any, Nothing, Emailer] =
    ZLayer {
      ZIO.succeed( Emailer(config) )
    }