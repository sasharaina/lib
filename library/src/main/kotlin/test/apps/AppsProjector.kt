package io.lib.test.apps

import android.app.Activity
import android.content.Context
import io.lib.test.utils.Constants.appsDevKey
import io.lib.test.firebase.FirebaseRemoteConfig
import io.lib.test.onesignsl.OneSignal
import io.lib.test.utils.Constants
import io.lib.test.Repository
import io.lib.test.bd.LinkDatabase
import io.lib.test.Storage

object AppsProjector {


    lateinit var preferences: Storage.Preferences
    var repository: Repository? = null



    fun createRemoteConfigInstance(activity: Activity): FirebaseRemoteConfig {
        preferences = Storage.Preferences(activity, Constants.NAME,
            Constants.MAINKEY,
            Constants.CHYPRBOOL
        )
        return FirebaseRemoteConfig(activity)
    }

    fun createAppsInstance(context: Context, devKey: String): AppsflyerManager {
        appsDevKey = devKey
       return AppsflyerManager(context, devKey)
    }

    fun createOneSignalInstance(context: Context, oneSignalId: String): OneSignal {
        return OneSignal(context, oneSignalId)
    }

    fun createRepoInstance(context: Context): Repository {
        if (repository == null){
            return Repository(LinkDatabase.getDatabase(context).linkDao())
        } else {
            return repository as Repository
        }
    }

}