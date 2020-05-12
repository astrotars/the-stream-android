package io.getstream.thestream

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.getstream.core.models.Activity
import io.getstream.thestream.services.FeedService
import kotlinx.android.synthetic.main.timeline_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


class TimelineFragment : Fragment(), CoroutineScope by MainScope() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View =
            inflater.inflate(R.layout.fragment_timeline, container, false)

        val listView: ListView =
            rootView.findViewById<View>(R.id.list_timeline) as ListView

        val adapter =
            FeedAdapter(context!!, mutableListOf())
        listView.adapter = adapter

        launch(Dispatchers.IO) {
            val timeline = FeedService.timeline()
            launch(Dispatchers.Main) { adapter.addAll(timeline) }
        }

        return rootView
    }

    class FeedAdapter(context: Context, objects: MutableList<Activity>) :
        ArrayAdapter<Activity>(context, android.R.layout.simple_list_item_1, objects) {

        private data class ViewHolder(
            val author: TextView,
            val message: TextView
        )

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val streamActivity: Activity = getItem(position)!!
            val viewHolder: ViewHolder
            var newView = convertView

            if (newView == null) {
                val inflater = LayoutInflater.from(context)
                newView = inflater.inflate(R.layout.timeline_item, parent, false)
                viewHolder = ViewHolder(
                    newView.timeline_item_author_name as TextView,
                    newView.timeline_item_message as TextView
                )
            } else {
                viewHolder = newView.tag as ViewHolder
            }

            viewHolder.author.text = streamActivity.actor
            viewHolder.message.text = streamActivity.extra["message"] as String

            newView!!.tag = viewHolder

            return newView
        }
    }
}
