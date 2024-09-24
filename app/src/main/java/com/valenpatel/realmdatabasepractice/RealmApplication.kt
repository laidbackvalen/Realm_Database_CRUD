package com.valenpatel.realmdatabasepractice

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class RealmApplication : Application() {

    private lateinit var config: RealmConfiguration

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        config = RealmConfiguration.Builder()
            .name("Myrealm.realm")
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(0)
            .allowWritesOnUiThread(true)
            .allowQueriesOnUiThread(true)
            .build()

        Realm.setDefaultConfiguration(config)
    }
}
