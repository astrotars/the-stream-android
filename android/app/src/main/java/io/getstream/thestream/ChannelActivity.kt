package io.getstream.thestream

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.getstream.sdk.chat.utils.PermissionChecker
import com.getstream.sdk.chat.view.MessageInputView
import com.getstream.sdk.chat.viewmodel.ChannelViewModel
import com.getstream.sdk.chat.viewmodel.ChannelViewModelFactory
import io.getstream.chat.android.client.models.Channel
import io.getstream.thestream.databinding.ActivityChannelBinding

class ChannelActivity : AppCompatActivity(), MessageInputView.PermissionRequestListener {
    private lateinit var binding: ActivityChannelBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val channelType = intent.getStringExtra(EXTRA_CHANNEL_TYPE)!!
        val channelId = intent.getStringExtra(EXTRA_CHANNEL_ID)!!

        binding = DataBindingUtil.setContentView(this, R.layout.activity_channel)
        binding.lifecycleOwner = this

        initViewModel(channelType, channelId)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.messageInput.captureMedia(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        binding.messageInput.permissionResult(requestCode, permissions, grantResults)
    }

    override fun openPermissionRequest() {
        PermissionChecker.permissionCheck(this, null)
    }

    private fun initViewModel(
        channelType: String,
        channelId: String
    ) {
        val viewModelFactory = ChannelViewModelFactory(application, channelType, channelId)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(ChannelViewModel::class.java)

        viewModel.initialized.observe(this, Observer {
            binding.viewModel = viewModel
            binding.messageList.setViewModel(viewModel, this)
            binding.messageInput.setViewModel(viewModel, this)
            binding.channelHeader.setViewModel(viewModel, this)
            binding.messageInput.setPermissionRequestListener(this)
        })
    }

    companion object {
        private const val EXTRA_CHANNEL_TYPE = "com.example.thestream.CHANNEL_TYPE"
        private const val EXTRA_CHANNEL_ID = "com.example.thestream.CHANNEL_ID"

        fun newIntent(context: Context, channel: Channel): Intent {
            val intent = Intent(context, ChannelActivity::class.java)
            intent.putExtra(EXTRA_CHANNEL_TYPE, channel.type)
            intent.putExtra(EXTRA_CHANNEL_ID, channel.id)
            return intent
        }
    }

}
