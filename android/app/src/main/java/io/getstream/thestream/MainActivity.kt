package io.getstream.thestream

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.getstream.thestream.databinding.ActivityMainBinding
import io.getstream.thestream.services.BackendService
import io.getstream.thestream.services.FeedService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        binding.submit.setOnClickListener {
            val user: String = binding.user.text.toString()

            launch(Dispatchers.IO) {
                BackendService.signIn(user)
                val feedCredentials = BackendService.getFeedCredentials()
                FeedService.init(user, feedCredentials)

                launch(Dispatchers.Main) {
                    val intent = AuthedMainActivity.newIntent(applicationContext)
                    startActivity(intent)
                }
            }
        }
    }
}