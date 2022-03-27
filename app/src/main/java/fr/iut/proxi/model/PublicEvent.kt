package fr.iut.proxi.model

import android.os.Parcel
import java.io.Serializable

class PublicEvent(var name: String, var city: String, var startDate: String,
                  var endDate: String, var coordinate: Coordinate) : Serializable {
    override fun toString(): String {
        return "Name : " + this.name + "\nCity : " + this.city + "\nStart_date : " +
                this.startDate + "\nEnd_date : " + this.endDate
    }
}