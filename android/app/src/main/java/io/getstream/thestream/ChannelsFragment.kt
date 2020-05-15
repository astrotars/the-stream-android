package io.getstream.thestream

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.getstream.sdk.chat.viewmodel.ChannelListViewModel
import io.getstream.chat.android.client.models.Filters.eq
import io.getstream.thestream.databinding.FragmentChannelsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

class ChannelsFragment : Fragment(), CoroutineScope by MainScope() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentChannelsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        val viewModel = ViewModelProvider(this).get(ChannelListViewModel::class.java)

        binding.viewModel = viewModel
        binding.channelList.setViewModel(viewModel, this)

        val filter = eq("type", "livestream")
        viewModel.setChannelFilter(filter)

        binding.newChannel.setOnClickListener {
            startActivityForResult(
                Intent(context, CreatePostActivity::class.java),
                POST_SUCCESS
            )
        }

        binding.channelList.setOnChannelClickListener { channel ->
            val intent = ChannelActivity.newIntent(context!!, channel)
            startActivity(intent)
        }

        return binding.root
    }
}
