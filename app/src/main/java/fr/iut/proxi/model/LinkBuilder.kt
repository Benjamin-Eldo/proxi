package fr.iut.proxi.model

class LinkBuilder (private var dataSetName: String){

    private val webSiteLink = "https://public.opendatasoft.com/api/records/1.0/search/"

    private val defaultParam = "&q=&facet=tags&facet=placename&facet=department&facet=region&facet=city&facet=date_start&facet=date_end&facet=pricing_info&facet=updated_at&facet=city_district"

    private var requestAttributes : HashMap<String, String>

    init {
        requestAttributes = HashMap()
    }

    fun addParameter(key:String, value:String){
        requestAttributes.put(key, value)
    }

    fun removeParamater(key:String){
        requestAttributes.remove(key)
    }

    fun build():String{
        var result = webSiteLink
        result+="?dataset="+dataSetName
        requestAttributes.forEach {
            result+='&'+generateRequest(it.key,it.value)
        }
        return result;
    }

    private fun generateRequest(attribute:String, value:String):String{
        return "$attribute=$value"
    }

    override fun toString(): String {
        var result = "=========LINK BUILDER=========\n"
        result+= "DATASET NAME : " + dataSetName +"\n"
        result+= "ATTRIBUTES : \n"
        requestAttributes.forEach {
            result+= it.key + "          " + it.value +"\n"
        }
        result+="===========================\n"
        return result
    }
}