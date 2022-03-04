package com.igaopk10.core.data.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

class AuthorizationInterceptor(
    private val publicKey: String,
    private val privateKey: String,
    private val calendar: Calendar
) : Interceptor {

    companion object {
        private const val QUERY_TIME_STAMP = "ts"
        private const val QUERY_API_KEY = "apikey"
        private const val QUERY_HASH = "hash"
    }

    @Suppress("MagicNumber")
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestUrl = request.url

        val timeStamp = (calendar.timeInMillis / 1000L).toString() // time in seconds
        val hash = "$timeStamp$privateKey$publicKey".toMd5()

        val newUrl = requestUrl.newBuilder()
            .addQueryParameter(QUERY_TIME_STAMP, timeStamp)
            .addQueryParameter(QUERY_API_KEY, publicKey)
            .addQueryParameter(QUERY_HASH, hash)
            .build()

        return chain.proceed(
            request.newBuilder().url(newUrl).build()
        )
    }

    @Suppress("MagicNumber")
    private fun String.toMd5(): String {
        val md5 = MessageDigest.getInstance("MD5")
        return BigInteger(1, md5.digest(toByteArray())).toString(16).padStart(32, '0')
    }
}