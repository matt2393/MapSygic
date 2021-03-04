package com.matt.mapsygic.Modelo.Entity

import com.google.gson.annotations.SerializedName

data class Document(@SerializedName("Document") var document: Doc = Doc())
data class Doc(var name: String = "",
               @SerializedName("Placemark") var placemark: ArrayList<Placemark> = arrayListOf()){
    fun parseCoordenadas(): ArrayList<DatoPoligono> {
        val array = arrayListOf<DatoPoligono>()
        placemark.forEach {
            array.add(it.parseCoordenadas())
        }
        return array
    }
}
