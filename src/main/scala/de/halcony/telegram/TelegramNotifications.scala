package de.halcony.telegram

trait TelegramNotifications {

  def sendTelegramMessage(msg: String): Unit = {
    TelegramClient.send(this.getClass.toString.split("\\.").last, msg)
  }

}
