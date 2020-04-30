package io.getstream.thestream

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class AuthedMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authed_main)
    }

    companion object {
        private val EXTRA_AUTH_TOKEN = "io.getstream.encryptedchat.AUTH_TOKEN"

        fun newIntent(
            context: Context,
            authToken: String
        ): Intent {
            val intent = Intent(context, AuthedMainActivity::class.java)
            intent.putExtra(EXTRA_AUTH_TOKEN, authToken)
            return intent
        }
    }
}
