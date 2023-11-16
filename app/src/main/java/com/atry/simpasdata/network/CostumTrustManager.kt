package com.atry.simpasdata.network

import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.X509TrustManager

class CustomTrustManager : X509TrustManager {

    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        // Implementasi sesuai kebutuhan
    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        // Implementasi sesuai kebutuhan
    }

    override fun getAcceptedIssuers(): Array<X509Certificate> {
        return arrayOf() // Atau return arrayOf<X509Certificate>() tergantung pada versi Kotlin Anda
    }
}
