package io.getstream.thestream

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import io.getstream.thestream.services.ChatService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

const val CHANNEL_CREATE_SUCCESS = 99

class CreateChannelActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_channel)

        val submit: Button = findViewById(R.id.submit)
        val channelName: EditText = findViewById(R.id.channel_name)

        submit.setOnClickListener {
            launch(Dispatchers.IO) {
                ChatService.createGroupChannel(
                    channelName.text.toString()
                )

                launch(Dispatchers.Main) {
                    setResult(CHANNEL_CREATE_SUCCESS)
                    finish()
                }
            }
        }
    }
}