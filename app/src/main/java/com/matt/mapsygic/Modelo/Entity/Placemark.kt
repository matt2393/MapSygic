package com.matt.mapsygic.Modelo.Entity

import com.google.gson.annotations.SerializedName
import com.matt.mapsygic.COLOR_DEFAULT
import com.sygic.sdk.position.GeoCoordinates
import java.util.*
import kotlin.collections.ArrayList

data class Placemark(@SerializedName("name") var nombre: String = "",
                     @SerializedName("description") var descripcion: String = "",
                     @SerializedName("Style") var style: Style = Style(),
                     @SerializedName("Polygon") var polygon: Polygon = Polygon()){
    fun parseCoordenadas(): DatoPoligono{
        val data = polygon.parseCoordenadas()
        data.color = style.lineStyle.color
        if(style.lineStyle.width.isNotEmpty()){
            data.width = style.lineStyle.width.toFloat()
        }
        data.nombre = nombre
        data.descripcion = descripcion
        return data
    }
}

data class Style(@SerializedName("LineStyle") var lineStyle: LineStyle = LineStyle(),
                 @SerializedName("PolyStyle") var polyStyle: PolyStyle = PolyStyle())
data class LineStyle(var color: String = COLOR_DEFAULT,
                     var width: String = "")
data class PolyStyle(var color: String = COLOR_DEFAULT)

data class Polygon(@SerializedName("outerBoundaryIs") var outer: Outer = Outer()){
    fun parseCoordenadas() = outer.parseCoordenadas()
}
data class Outer(@SerializedName("LinearRing") var lineasPoligono: LinearRing = LinearRing()){
    fun parseCoordenadas() = lineasPoligono.parseCoordenadas()
}
data class LinearRing(@SerializedName("coordinates") var coordenadasString: String = ""){
    fun parseCoordenadas(): DatoPoligono {
        val tokens = StringTokenizer(coordenadasString)
        val coordenadas = arrayListOf<GeoCoordinates>()
        while (tokens.hasMoreTokens()){
            val aux = tokens.nextToken()
            val coo = StringTokenizer(aux, ",")
            val geo = GeoCoordinates(coo.nextToken().toDouble(), coo.nextToken().toDouble())
            coordenadas.add(geo)
        }
        val a = ACalculate(coordenadas)
        val x = pointX(coordenadas, a)
        val y = pointY(coordenadas, a)
        val data = DatoPoligono()
        data.centro = GeoCoordinates(x, y)
        data.coordenadas = coordenadas
        return data
    }
    private fun ACalculate(points: ArrayList<GeoCoordinates>): Double{
        var aux = 0.0
        for (i in 0 until points.size-1){
            aux += (points[i].latitude * points[i+1].longitude - points[i+1].latitude * points[i].longitude)
        }
        return aux/2.0
    }
    private fun pointX(points: ArrayList<GeoCoordinates>, a: Double): Double {
        var aux = 0.0
        for (i in 0 until points.size-1){
            aux += ((points[i].latitude+points[i+1].latitude)*(points[i].latitude * points[i+1].longitude - points[i+1].latitude * points[i].longitude))
        }
        return aux/(6*a)
    }
    private fun pointY(points: ArrayList<GeoCoordinates>, a: Double): Double {
        var aux = 0.0
        for (i in 0 until points.size-1){
            aux += ((points[i].longitude+points[i+1].longitude)*(points[i].latitude * points[i+1].longitude - points[i+1].latitude * points[i].longitude))
        }
        return aux/(6*a)
    }

}
data class DatoPoligono(var centro: GeoCoordinates = GeoCoordinates(0.0,0.0),
                        var coordenadas: ArrayList<GeoCoordinates> = arrayListOf(),
                        var nombre: String = "",
                        var descripcion: String = "",
                        var color: String = COLOR_DEFAULT,
                        var width: Float = 0f)