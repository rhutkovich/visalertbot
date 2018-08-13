package bot.telegram

import Resources
import messaging.Message
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import java.util.*

class TelegramBot: TelegramLongPollingBot() {
    val currentChats: MutableSet<Long> = HashSet()
    val botName: String = "telegram.bot.name"
    val token: String = "telegram.bot.token"
    val resources: Resources = Resources()

    override fun getBotToken(): String {
        return resources.getResourse(token)
    }

    override fun onUpdateReceived(update: Update?) {
        var sendMsg: SendMessage = SendMessage()
                .setChatId(update?.message?.chatId)
                .setText("Ура! Вы добавлены к списку рассылки! \uD83D\uDE0B")
        execute(sendMsg)
        currentChats.add(update?.message?.chatId!!)
    }

    override fun getBotUsername(): String {
        return resources.getResourse(botName)
    }

    fun alertAll(msg: Message) {
        val dates = msg.dates
        val sb = StringBuilder("Появились даты! \uD83D\uDCC5 \n")
        dates.keys.forEach { key: String -> run {
            sb.append("На дату ").append(key).append(" доступно время: \n")
            dates[key]?.forEach { time: String ->  sb.append("◾ ").append(time).append("\n")}
        } }
        currentChats.forEach { chatId:Long -> run {
            execute(SendMessage()
                    .setChatId(chatId)
                    .setText(sb.toString()))
        }}
    }
}