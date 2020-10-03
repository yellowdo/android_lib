package com.yellowdo.library.util

import android.content.Context
import android.util.Base64
import com.yellowdo.library.ext.getApiKeyFromManifest
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AES256Chiper {
    private val key = "encryption.key"
    private val ivBytes = byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)

    //AES256 암호화
    @Throws(
        java.io.UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun chiperAesEncoding(context: Context?, str: String): String {
        return context?.run {
            val secretKey = getApiKeyFromManifest(key).let {
                if (it.isNullOrBlank()) {
                    context.packageName.replace('.', '_').let { n -> (n + n + n + n + n).substring(0 until 16) }
                } else {
                    it
                }
            }
            val textBytes = str.toByteArray(charset("UTF-8"))
            val ivSpec = IvParameterSpec(ivBytes)
            val newKey = SecretKeySpec(secretKey.toByteArray(charset("UTF-8")), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec)
            Base64.encodeToString(cipher.doFinal(textBytes), 0)
        } ?: run { "" }
    }

    //AES256 복호화
    @Throws(
        java.io.UnsupportedEncodingException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        InvalidAlgorithmParameterException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun chiperAesDecode(context: Context?, str: String): String {
        return context?.run {
            val secretKey = getApiKeyFromManifest(key).let {
                if (it.isNullOrBlank()) {
                    context.packageName.replace('.', '_').let { n -> (n + n + n + n + n).substring(0 until 16) }
                } else {
                    it
                }
            }
            val textBytes = Base64.decode(str, 0)
            //byte[] textBytes = str.getBytes("UTF-8");
            val ivSpec = IvParameterSpec(ivBytes)
            val newKey = SecretKeySpec(secretKey.toByteArray(charset("UTF-8")), "AES")
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec)
            //return cipher.doFinal(textBytes).decodeToString()
            String(cipher.doFinal(textBytes), charset("UTF-8"))
        } ?: run { "" }
    }
}
