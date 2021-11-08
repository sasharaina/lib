package io.lib.test

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.security.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class Storage {

    class Preferences(context: Context, name: String, secureKey: String, boolean: Boolean){
        private val sharedPrefs: SecurePreferences =
            SecurePreferences(
                context,
                name,
                secureKey,
                boolean
            )


        fun setLastUrl(name: String, lastUrl: String) {
            sharedPrefs.put(name, lastUrl)
        }

        fun getLastUrl(name: String): String? {
            return sharedPrefs.getString(name)
        }

        fun setAdId(name: String, tag: String) {
            sharedPrefs.put(name, tag)
        }

        fun getAdId(name: String): String {
            return sharedPrefs.getString(name).toString()
        }


        fun setADID(name: String, adid: String) {
            sharedPrefs.put(name, adid)
        }

        fun getADID(name: String): String {
            return sharedPrefs.getString(name).toString()
        }

        fun setOnConversionDataSuccess(name: String, flag: String) {
            sharedPrefs.put(name, flag)
        }

        fun getOnConversionDataSuccess(name: String): String {
            return sharedPrefs.getString(name).toString()
        }

        fun setOneSignalID(name: String, id: String) {
            sharedPrefs.put(name, id)
        }

        fun getOneSignalID(name: String): String {
            return sharedPrefs.getString(name).toString()
        }

        fun setAppsUID(name: String, id: String) {
            sharedPrefs.put(name, id)
        }

        fun getAppsUID(name: String): String {
            return sharedPrefs.getString(name).toString()
        }

        fun setState(name: String, state: String) {
            sharedPrefs.put(name, state)
        }

        fun getState(name: String): String? {
            return sharedPrefs.getString(name)
        }

        fun setAppsUrl(name: String, url: String) {
            sharedPrefs.put(name, url)
        }

        fun getAppsUrl(name: String): String? {
            return sharedPrefs.getString(name)
        }

        fun setBaseUrl(name: String, url: String) {
            sharedPrefs.put(name, url)
        }

        fun getBaseUrl(name: String): String? {
            return sharedPrefs.getString(name)
        }

        fun setRemoteLink(name: String, link: String) {
            sharedPrefs.put(name, link)
        }

        fun getRemoteLink(name: String): String {
            return sharedPrefs.getString(name).toString()
        }

        fun setAppsFlyerId(name: String, id: String) {
            sharedPrefs.put(name, id)
        }

        fun getAppsFlyerId(name: String): String {
            return sharedPrefs.getString(name).toString()
        }

        fun setPartOfUrl(name: String, partoflink: String) {
            sharedPrefs.put(name, partoflink)
        }

        fun getPartOfLink(name: String): String {
            return sharedPrefs.getString(name).toString()
        }

        fun setAppsFlyerState(name: String, state: String) {
            sharedPrefs.put(name, state)
        }

        fun getAppsFlyerState(name: String): String? {
            return sharedPrefs.getString(name)
        }


    }

    class SecurePreferences(
        context: Context,
        preferenceName: String?,
        secureKey: String,
        encryptKeys: Boolean
    ) {
        class SecurePreferencesException(e: Throwable?) :
            RuntimeException(e)

        private var encryptKeys = false
        private var writer: Cipher? = null
        private var reader: Cipher? = null
        private var keyWriter: Cipher? = null
        private var preferences: SharedPreferences? = null

        @Throws(
            UnsupportedEncodingException::class,
            NoSuchAlgorithmException::class,
            InvalidKeyException::class,
            InvalidAlgorithmParameterException::class
        )
        protected fun initCiphers(secureKey: String) {
            val ivSpec = iv
            val secretKey = getSecretKey(secureKey)
            writer!!.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec)
            reader!!.init(Cipher.DECRYPT_MODE, secretKey, ivSpec)
            keyWriter!!.init(Cipher.ENCRYPT_MODE, secretKey)
        }

        protected val iv: IvParameterSpec
            protected get() {
                val iv = ByteArray(writer!!.blockSize)
                System.arraycopy(
                    "fldsjfodasjifudslfjdsaofshaufihadsf".toByteArray(),
                    0,
                    iv,
                    0,
                    writer!!.blockSize
                )
                return IvParameterSpec(iv)
            }

        @Throws(
            UnsupportedEncodingException::class,
            NoSuchAlgorithmException::class
        )
        protected fun getSecretKey(key: String): SecretKeySpec {
            val keyBytes = createKeyBytes(key)
            return SecretKeySpec(
                keyBytes,
                TRANSFORMATION
            )
        }

        @Throws(
            UnsupportedEncodingException::class,
            NoSuchAlgorithmException::class
        )
        protected fun createKeyBytes(key: String): ByteArray {
            val md =
                MessageDigest.getInstance(SECRET_KEY_HASH_TRANSFORMATION)
            md.reset()
            return md.digest(key.toByteArray(charset(CHARSET)))
        }

        fun put(key: String, value: String?) {
            if (value == null) {
                preferences!!.edit().remove(toKey(key)).commit()
            } else {
                putValue(toKey(key), value)
            }
        }


        @Throws(SecurePreferencesException::class)
        fun getString(key: String): String? {
            if (preferences!!.contains(toKey(key))) {
                val securedEncodedValue = preferences!!.getString(toKey(key), "")
                return decrypt(securedEncodedValue)
            }
            return null
        }


        private fun toKey(key: String): String {
            return if (encryptKeys) encrypt(key, keyWriter) else key
        }

        @Throws(SecurePreferencesException::class)
        private fun putValue(key: String, value: String) {
            val secureValueEncoded = encrypt(value, writer)
            preferences!!.edit().putString(key, secureValueEncoded).commit()
        }

        @Throws(SecurePreferencesException::class)
        protected fun encrypt(value: String, writer: Cipher?): String {
            val secureValue: ByteArray
            secureValue = try {
                convert(
                    writer,
                    value.toByteArray(charset(CHARSET))
                )
            } catch (e: UnsupportedEncodingException) {
                throw SecurePreferencesException(
                    e
                )
            }
            return Base64.encodeToString(secureValue, Base64.NO_WRAP)
        }
        protected fun decrypt(securedEncodedValue: String?): String {
            val securedValue =
                Base64.decode(securedEncodedValue, Base64.NO_WRAP)
            val value =
                convert(
                    reader,
                    securedValue
                )
            return try {
                String(value, Charset.forName(CHARSET))
            } catch (e: UnsupportedEncodingException) {
                throw SecurePreferencesException(
                    e
                )
            }
        }
        companion object {
            private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"
            private const val KEY_TRANSFORMATION = "AES/ECB/PKCS5Padding"
            private const val SECRET_KEY_HASH_TRANSFORMATION = "SHA-256"
            private const val CHARSET = "UTF-8"

            @Throws(SecurePreferencesException::class)
            private fun convert(cipher: Cipher?, bs: ByteArray): ByteArray {
                return try {
                    cipher!!.doFinal(bs)
                } catch (e: Exception) {
                    throw SecurePreferencesException(
                        e
                    )
                }
            }
        }

        init {
            try {
                writer =
                    Cipher.getInstance(TRANSFORMATION)
                reader =
                    Cipher.getInstance(TRANSFORMATION)
                keyWriter =
                    Cipher.getInstance(KEY_TRANSFORMATION)
                initCiphers(secureKey)
                preferences =
                    context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
                this.encryptKeys = encryptKeys
            } catch (e: GeneralSecurityException) {
                throw SecurePreferencesException(
                    e
                )
            } catch (e: UnsupportedEncodingException) {
                throw SecurePreferencesException(
                    e
                )
            }
        }
    }

}