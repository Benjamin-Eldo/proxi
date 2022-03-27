package fr.iut.proxi.model

import java.io.Serializable

class Coordinate (var lat : Float, var lon : Float) : Serializable{
    override fun toString(): String {
        return "Coordinate(lat=$lat, lon=$lon)"
    }
}