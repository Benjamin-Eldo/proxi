package fr.iut.proxi

import android.os.AsyncTask
import android.os.Build
import android.util.Half
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import fr.iut.proxi.model.Coordinate
import fr.iut.proxi.model.PublicEvent
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.lang.Exception
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class AsyncTaskGetData : AsyncTask<Any, Void, String> () {

    lateinit var parent:MainActivity


    override fun doInBackground(vararg p0: Any?): String {
        val host = p0[0] as String
        parent= p0[1] as MainActivity
        var message = ""

        try {
            val url = URL(host);
            val urlConnection = url.openConnection() as HttpsURLConnection
            if (urlConnection.responseCode == HttpsURLConnection.HTTP_OK) {
                message = urlConnection.inputStream.bufferedReader().use(BufferedReader::readText)
                Log.i("AsyncTaskGetData","doInBackground()")
            }
        }catch(e:Exception){

        }
        return message;

    }


    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)


        try{
            val whole = JSONObject(result)
            val eventsDesc = JSONArray(whole.getString("records"))
            parent.setEventList(ArrayList<PublicEvent>())
            for (i in 0 until eventsDesc.length()){
                val event = JSONObject (eventsDesc.getJSONObject(i).toString())
                val eventInfo = event.getJSONObject("fields")

                var name = ""
                try{
                    name = eventInfo.getString("title")
                }catch(e:Exception){

                }
                val startDate = eventInfo.getString("date_start")
                val endDate = eventInfo.getString("date_end")
                var city =  "";
                try {
                    city += eventInfo.getString("city")
                }catch(e:Exception){

                }
                val point = event.getJSONObject("geometry")
                val coord = point.getJSONArray("coordinates")
                parent.getEventList().add(PublicEvent(name,city, startDate,endDate,
                    Coordinate(coord.get(0).toString().toFloat(),coord.get(1).toString().toFloat())))

            }
            parent.setEventAdapter(EventAdapter(parent.getEventList(),{item : PublicEvent -> parent.partItemClicked(item)}))
            parent.setLayoutManager(LinearLayoutManager(parent.applicationContext))
            parent.getRecyclerView().layoutManager = parent.getLayoutManager()
            parent.getRecyclerView().adapter = parent.getEventAdapter()

            Log.d("AsyncTaskGetData","EventList size :" + parent.getEventList().size)

        }catch(e:Exception){
            Log.i("AsyncTaskGetData","Error on parsing !")
            Log.e("AsyncTaskGetData",e.stackTraceToString())

        }

        Log.d("AsyncTaskGetData","onPostExecute()")

    }

}