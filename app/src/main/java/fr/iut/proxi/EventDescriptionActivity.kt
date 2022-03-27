package fr.iut.proxi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import fr.iut.proxi.model.PublicEvent
import org.w3c.dom.Text

class EventDescriptionActivity : AppCompatActivity() {
    private lateinit var event : PublicEvent
    private lateinit var tvName : TextView
    private lateinit var tvCity : TextView
    private lateinit var tvStartDate : TextView
    private lateinit var tvEndDate : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_description)

        val bundle: Bundle ?= intent.extras
        bundle?.let{
            bundle.apply {
                event = getSerializable("KeyMain") as PublicEvent
            }
        }

        tvName = findViewById(R.id.tvName)
        tvCity = findViewById(R.id.tvCity)
        tvStartDate = findViewById(R.id.tvStartDate)
        tvEndDate =  findViewById(R.id.tvEndDate)

        tvName.text = event.name
        tvCity.text = event.city
        tvStartDate.text =  event.startDate
        tvEndDate.text = event.endDate
    }
}