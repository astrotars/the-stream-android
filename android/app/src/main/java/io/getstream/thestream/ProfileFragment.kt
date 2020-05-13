package io.getstream.thestream

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import io.getstream.thestream.services.FeedService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(), CoroutineScope by MainScope() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_profile, container, false)
        val listView: ListView = rootView.findViewById(R.id.list_profile)
        val adapter = FeedAdapter(rootView.context, mutableListOf())

        listView.adapter = adapter

        launch(Dispatchers.IO) {
            val profileFeed = FeedService.profileFeed()

            launch(Dispatchers.Main) { adapter.addAll(profileFeed) }
        }

        val newPost: View = rootView.findViewById(R.id.new_post)
        newPost.setOnClickListener { view ->
            startActivity(
                Intent(rootView.context, CreatePostActivity::class.java)
            )
        }


        return rootView
    }
}
