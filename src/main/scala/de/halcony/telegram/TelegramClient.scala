package de.halcony.telegram

import sttp.client4.quick._
import sttp.client4.Response
import wvlet.log.LogSupport

class TelegramClient(val token: String, val chat: Long) extends LogSupport {

  protected def sendMessage(msg: String): Unit = {
    try {
      val ps = Map("chat_id" -> chat, "text" -> msg)
      val response: Response[String] = quickRequest
        .get(uri"https://api.telegram.org/bot$token/sendMessage?$ps")
        .send()
      if (!response.is200)
        error(s"the response was ${response.code} saying ${response.body}")
    } catch {
      case e: Throwable => error(e.getMessage)
    }
  }

}

object TelegramClient {

  private var vomSingleton: Option[TelegramClient] = None

  def startTelegramClient(token: String, chat: Long): Unit = {
    vomSingleton match {
      case Some(_) =>
      case None    => vomSingleton = Some(new TelegramClient(token, chat))
    }
  }

  def send(context: String, msg: String): Unit = {
    vomSingleton match {
      case Some(vom) => vom.sendMessage(s"[$context] $msg")
      case None      =>
    }
  }

}
