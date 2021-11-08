package io.lib.test.utils

import android.content.Context
import com.appsflyer.AppsFlyerLib
import io.lib.test.utils.Constants.appsDevKey
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class Utils {

    companion object {

        fun concatCampaign(naming: String): String {
            return naming.replace("||", "&").replace("()", "=")
        }

        fun getFinalUrl(baseUrl: String, naming: String, context: Context, campaignId: String, mediaSource: String, advertisingId: String): String {
            return baseUrl + "?" + "sub12=" + getAppBundle(context) +
                    "&afToken=" + appsDevKey +
                    "&afid=" + AppsFlyerLib.getInstance().getAppsFlyerUID(context) +
                    "&sub11=" + campaignId +
                    "&media_source=" + mediaSource +
                    "&advertising_id=" + advertisingId +
                    "&triger=" + concatCampaign(naming)
        }

        fun getAppBundle(context: Context): String {
            return context.packageName
        }


        fun getResponseCode(url: String): Int{
            var int = 0
            try {
                val openUrl = URL(url)
                val http: HttpURLConnection = openUrl.openConnection() as HttpURLConnection
                int = http.responseCode

            } catch (exception: IOException){

            }
            return int
        }
    }

}