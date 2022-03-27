package fr.iut.proxi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.iut.proxi.model.PublicEvent

class EventAdapter(private var itemList:List<PublicEvent>, val clickListener: (PublicEvent) -> Unit):RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder (view: View):RecyclerView.ViewHolder(view){
        var tvEvent : TextView = view.findViewById(R.id.tvTitle)
        fun bind(publicEvent: PublicEvent, clickListener: (PublicEvent) -> Unit){
            tvEvent.text = publicEvent.name
            tvEvent.setOnClickListener{clickListener(publicEvent)}
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val eventView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return EventViewHolder(eventView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = itemList[position]
        holder.tvEvent.text = item.name
        (holder as EventViewHolder).bind(itemList[position], clickListener)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


}