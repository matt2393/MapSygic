package com.matt.mapsygic

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.matt.mapsygic.Modelo.Entity.Document
import com.sygic.sdk.context.CoreInitCallback
import com.sygic.sdk.context.CoreInitException
import com.sygic.sdk.map.MapFragment
import com.sygic.sdk.map.MapView
import com.sygic.sdk.map.`object`.MapPolygon
import com.sygic.sdk.map.listeners.OnMapInitListener
import com.sygic.sdk.online.OnlineManager
import com.sygic.sdk.online.OnlineManagerProvider
import java.io.ByteArrayOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val doc = loadJson()
        val datos = doc.document.parseCoordenadas()

        val mapFragment = getSupportFragmentManager().findFragmentById(R.id.mapFragment) as MapFragment

        // getMapAsync() will internally wait for engine initialization
        mapFragment.getMapAsync(object : OnMapInitListener {
            override fun onMapInitializationInterrupted() {
            }

            override fun onMapReady(p0: MapView) {
                OnlineManagerProvider.getInstance(object : CoreInitCallback<OnlineManager> {
                    override fun onInstance(p0: OnlineManager) {
                        p0.enableOnlineMapStreaming(object : OnlineManager.MapStreamingListener {
                            override fun onSuccess() {
                            }

                            override fun onError(p0: OnlineManager.MapStreamingError?) {
                            }
                        })
                    }

                    override fun onError(p0: CoreInitException) {
                    }
                })

                datos.forEach {
                    val polygon = MapPolygon.of(it.centro, it.coordenadas)
                        .setCenterColor(Color.parseColor(it.color))
                        .setBorderColor(Color.parseColor(it.color))
                        .build()
                    p0.mapDataModel.addMapObject(polygon)
                }
            }

        })




        
    }

    private fun loadJson(): Document{
        val inputStream = resources.openRawResource(R.raw.geofences)
        val outputStream = ByteArrayOutputStream()
        val buf = ByteArray(1024)
        var len: Int
        try {
            while (inputStream.read(buf).also { len = it } != -1) {
                outputStream.write(buf, 0, len)
            }
            outputStream.close()
            inputStream.close()
        } catch (e: IOException) {
        }
        val gson = Gson()
        return gson.fromJson(outputStream.toString(), Document::class.java)
    }
}