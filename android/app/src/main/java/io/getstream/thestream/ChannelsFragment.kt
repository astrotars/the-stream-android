package io.getstream.thestream

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.getstream.sdk.chat.viewmodel.ChannelListViewModel
import io.getstream.chat.android.client.models.Filters.eq
import io.getstream.chat.android.client.models.ModelType
import io.getstream.thestream.databinding.FragmentChannelsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class ChannelsFragment : Fragment(), CoroutineScope by MainScope() {
    private lateinit var viewModel: ChannelListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChannelsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(ChannelListViewModel::class.java)

        binding.viewModel = viewModel
        binding.channelList.setViewModel(viewModel, this)

        val filter = eq("type", ModelType.channel_livestream)
        viewModel.setChannelFilter(filter)

        binding.newChannel.setOnClickListener {
            startActivityForResult(
                Intent(context, CreateChannelActivity::class.java),
                CHANNEL_CREATE_SUCCESS
            )
        }

        binding.channelList.setOnChannelClickListener { channel ->
            startActivity(
                ChannelActivity.newIntent(context!!, channel)
            )
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == CHANNEL_CREATE_SUCCESS) {
            Toast.makeText(context, "Created Channel!", Toast.LENGTH_LONG).show()
        }
    }

}
