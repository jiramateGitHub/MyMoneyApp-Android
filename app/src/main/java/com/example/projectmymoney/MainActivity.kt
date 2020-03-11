package com.example.projectmymoney

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import com.example.projectmymoney.login.LoginFragment

import com.example.projectmymoney.pages.TransactionFragment
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        debugHashKey()
        try {
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }

        val Transaction_Fragment = TransactionFragment()
        val Login_Fragment = LoginFragment()
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.contentContainer, Login_Fragment, "fragment_login")
        transaction.addToBackStack("fragment_login")
        transaction.commit()

    }

    private fun debugHashKey() {
        try {
            val info = packageManager.getPackageInfo(
                "com.example.projectmymoney",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.getEncoder().encodeToString(md.digest()))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }

    override fun onBackPressed() {
        val manager = supportFragmentManager.findFragmentById(R.id.contentContainer)
        if (manager is LoginFragment ) {
            finish()
        } else {
            super.onBackPressed();
        }
    }

}
