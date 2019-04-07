package com.apriantrimulyana.mitramas

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_select_menu.*

class SelectMenu : AppCompatActivity() {

    companion object {
        var actTransaksiDelivery: Activity? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_menu)

        actTransaksiDelivery = this
        bt_sl_daftar.setOnClickListener {
            val intent = Intent(
                    this@SelectMenu,
                    Register::class.java)
            startActivity(intent)
        }


        bt_sl_login.setOnClickListener {
            val intent = Intent(
                    this@SelectMenu,
                    Login::class.java)
            startActivity(intent)
        }
    }
}
