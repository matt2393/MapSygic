package com.matt.mapsygic

import android.app.Application
import com.sygic.sdk.SygicEngine
import com.sygic.sdk.context.CoreInitException
import com.sygic.sdk.context.SygicContext
import com.sygic.sdk.diagnostics.LogConnector
import java.util.*

class MapSygicApp: Application() {
    override fun onCreate() {
        super.onCreate()
        val config =  SygicEngine.JsonConfigBuilder()
        config.authentication(getString(R.string.id_sygic))
        val path = Objects.requireNonNull(applicationContext.getExternalFilesDir(null)).toString() //default Android/data/
        config.storageFolders().rootPath(path);

        SygicEngine.initialize(this, null, object: LogConnector() {
            override fun onLogReceived(message: String?, logLevel: Int) {
                super.onLogReceived(message, logLevel)
            }
        }, config.build(), object: SygicEngine.OnInitCallback {
            override fun onError(p0: CoreInitException) {
            }

            override fun onInstance(p0: SygicContext) {
            }
        })
    }

}