package io.getstream.thestream

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import io.getstream.thestream.databinding.FragmentPeopleBinding
import io.getstream.thestream.services.BackendService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class PeopleFragment : Fragment(), CoroutineScope by MainScope() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentPeopleBinding = FragmentPeopleBinding.inflate(
            inflater, container, false
        )

        val list: ListView = binding.listview

        val adapter = ArrayAdapter(
            binding.root.context,
            android.R.layout.simple_list_item_1,
            mutableListOf<String>()
        )
        list.adapter = adapter

        list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//            val otherUser = adapter.getItem(position).toString()
//            val intent = ChannelActivity.newIntent(this, user, otherUser, virgilToken)
//            startActivity(intent)
        }

        launch(Dispatchers.IO) {
            val users = BackendService.getUsers()
            launch(Dispatchers.Main) { adapter.addAll(users) }
        }

        return binding.root
    }
}
