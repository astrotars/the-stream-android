package io.getstream.thestream

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import io.getstream.thestream.services.BackendService
import io.getstream.thestream.services.FeedService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class PeopleFragment : Fragment(), CoroutineScope by MainScope() {
    private var users: List<String> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_people, container, false)

        val list: ListView = rootView.findViewById(R.id.list_people)

        val adapter = ArrayAdapter(
            rootView.context,
            android.R.layout.simple_list_item_1,
            mutableListOf<String>()
        )
        list.adapter = adapter

        list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(rootView.context)

            alertDialogBuilder.setTitle("Pick an action")
            alertDialogBuilder.setPositiveButton("Follow") { dialog, _ ->
                FeedService.follow(users[position])
                dialog.dismiss()
                Snackbar
                    .make(
                        activity!!.findViewById(android.R.id.content),
                        "Successfully followed ${users[position]}",
                        Snackbar.LENGTH_LONG
                    )
                    .show()
            }

            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            alertDialogBuilder.show()
        }

        launch(Dispatchers.IO) {
            val users = BackendService.getUsers()
            this@PeopleFragment.users = users

            launch(Dispatchers.Main) { adapter.addAll(users) }
        }

        return rootView
    }
}
