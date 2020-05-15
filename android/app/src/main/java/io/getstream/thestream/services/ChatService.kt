package io.getstream.thestream.services

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.getstream.sdk.chat.Chat
import com.getstream.sdk.chat.model.ModelType
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.client.errors.ChatError
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.socket.InitConnectionListener
import io.getstream.chat.android.client.utils.FilterObject

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
        this.client.setUser(this.user, credentials.token, object : InitConnectionListener() {
            override fun onSuccess(data: ConnectionData) {
                Log.i("MainActivity", "setUser completed")
            }

            override fun onError(error: ChatError) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show()
                Log.e("MainActivity", "setUser onError")
            }
        })
    }

    fun createPrivateChannel(otherUser: String): Channel {
        val users = listOf(user.id, otherUser)

        return client
            .createChannel(ModelType.channel_messaging, users)
            .execute()
            .data()
    }
}
