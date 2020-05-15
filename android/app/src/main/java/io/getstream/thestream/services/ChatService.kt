package io.getstream.thestream.services

import android.content.Context
import com.getstream.sdk.chat.Chat
import com.getstream.sdk.chat.model.ModelType
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.User
import java.util.*

object ChatService {
    private lateinit var client: ChatClient
    private lateinit var user: User

    fun init(context: Context, user: String, credentials: BackendService.StreamCredentials) {
        val chat = Chat.Builder(credentials.apiKey, context)
            .logLevel(ChatLogLevel.ALL)
            .build()

        this.user = User(user)
        this.user.extraData["name"] = user
        this.client = chat.client
        this.client.setUser(this.user, credentials.token)
    }

    fun createPrivateChannel(otherUser: String): Channel {
        val users = listOf(user.id, otherUser)

        return client
            .createChannel(ModelType.channel_messaging, users)
            .execute()
            .data()
    }

    fun createGroupChannel(channelName: String) {
        val channelId = channelName
            .toLowerCase(Locale.getDefault())
            .replace("\\s".toRegex(), "-")

        val result = client
            .createChannel(
                ModelType.channel_livestream,
                channelId,
                mapOf(
                    "name" to channelName,
                    "image" to "https://robohash.org/${channelId}.png"
                )
            )
            .execute()

        if (result.isError) {
            throw result.error()
        }
    }
}
