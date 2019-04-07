package com.apriantrimulyana.mitramas

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import es.dmoral.prefs.Prefs

class SplashScreen : AppCompatActivity() {
    private val PERMISSION_ALL = 1
    private val PERMISSIONS = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    private fun check() {
        val login_check = Prefs.with(this@SplashScreen).read("login")
        if (login_check != "") {
            directHome()
        } else {
            directLogin()
        }
    }

    private fun directLogin() {
        val welcomeScreenDisplay = 3000 // 3000 = 3 detik
        val welcomeThread = object : Thread() {
            internal var wait = 0
            override fun run() {
                try {
                    super.run()
                    while (wait < welcomeScreenDisplay) {
                        Thread.sleep(100)
                        wait += 100
                    }
                } catch (e: Exception) {
                    println("EXc=$e")
                } finally {
                    finish()
                    val intent = Intent(
                            this@SplashScreen,
                            SelectMenu::class.java)
                    startActivity(intent)
                }
            }
        }
        welcomeThread.start()
    }

    private fun directHome() {
        val welcomeScreenDisplay = 3000 // 3000 = 3 detik
        val welcomeThread = object : Thread() {
            internal var wait = 0
            override fun run() {
                try {
                    super.run()
                    while (wait < welcomeScreenDisplay) {
                        Thread.sleep(100)
                        wait += 100
                    }
                } catch (e: Exception) {
                    println("EXc=$e")
                } finally {
                    finish()
                    val intent = Intent(
                            this@SplashScreen,
                            Home::class.java)
                    startActivity(intent)
                }
            }
        }
        welcomeThread.start()
    }

    fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null &&
                permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        if (hasPermissions(this@SplashScreen, *PERMISSIONS)) {
            check()
        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)
        }
    }
}
