package com.apriantrimulyana.mitramas

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.apriantrimulyana.mitramas.SelectMenu.Companion.actTransaksiDelivery
import com.apriantrimulyana.mitramas.model.setter
import com.apriantrimulyana.mitramas.util.OtherUtil
import com.apriantrimulyana.mitramas.util.OtherUtil.hideAlertDialog
import com.apriantrimulyana.mitramas.util.OtherUtil.showAlertDialogLoading
import es.dmoral.prefs.Prefs
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONObject
import java.util.HashMap

class Register : AppCompatActivity() {

    private var etNama: EditText?= null
    private var etEmail: EditText?= null
    private var etPassword: EditText?= null

    private var hsNama: String = ""
    private var hsEmail: String = ""
    private var hsPassword: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etNama      = et_nama
        etEmail     = et_email
        etPassword  = et_password
        bt_daftar.setOnClickListener(View.OnClickListener {
            hsNama      = etNama!!.getText().toString().trim({ it <= ' ' })
            hsEmail     = etEmail!!.getText().toString().trim({ it <= ' ' })
            hsPassword  = etPassword!!.getText().toString().trim({ it <= ' ' })

            if(hsNama.equals("")){
                Toast.makeText(this@Register, "Nama Harus di Isi", Toast.LENGTH_LONG).show()
            }else if(hsEmail.equals("")){
                Toast.makeText(this@Register, "Email Harus di Isi", Toast.LENGTH_LONG).show()
            }else if(hsPassword.equals("")){
                Toast.makeText(this@Register, "Password Harus di Isi", Toast.LENGTH_LONG).show()
            }else{
                sendData()
            }
        })
    }


    private fun sendData(){
        hideAlertDialog()
        showAlertDialogLoading(this@Register, "Please Wait ...")
        val stringRequest = object : StringRequest(Request.Method.POST, setter.URL_SERVICE_2,
                Response.Listener { response ->
                    try {
                        val json = JSONObject(response)
                        val success = json.getString("code")
                        val message = json.getString("message")
                        if (success == "200") {
                            val cek_data = json.getString("data")
                            if (cek_data != "") {
                                val data = json.getJSONArray("data")
                                val ar = data.getJSONObject(0)
                                val id_user = ar.getString("id_user")
                                val nama    = ar.getString("nama")
                                val email   = ar.getString("email")

                                Prefs.with(this@Register).write("login", id_user)
                                Prefs.with(this@Register).write("login_nama", nama)
                                Prefs.with(this@Register).write("login_email", email)

                                finish()
                                if(SelectMenu.actTransaksiDelivery != null){
                                    SelectMenu.actTransaksiDelivery!!.finish()
                                }
                                val intent = Intent(this@Register, Home::class.java)
                                startActivity(intent)
                            }
                        } else {
                            Toast.makeText(this@Register, "Username atau Password tidak sesuai", Toast.LENGTH_LONG).show()
                        }
                        hideAlertDialog()
                    } catch (e: Exception) {
                        Log.e("Login", "error $e")
                        hideAlertDialog()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this@Register, " Tidak Terhubung " + error.message, Toast.LENGTH_LONG).show()
                    hideAlertDialog()
                    if (error is TimeoutError || error is NoConnectionError) {

                    } else if (error is AuthFailureError) {
                        //TODO
                        Log.e("VOLLEYERROR", "AuthFailureError")
                    } else if (error is ServerError) {
                        Log.e("VOLLEYERROR", "ServerError")
                        //TODO
                    } else if (error is NetworkError) {
                        Log.e("VOLLEYERROR", "NetworkError")
                        //TODO
                    } else if (error is ParseError) {
                        Log.e("VOLLEYERROR", "ParseError")
                        //TODO
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["nama"]      = hsNama
                params["email"]     = hsEmail
                params["password"]  = hsPassword

                Log.w("DATA_POST", "login : $params")
                //returning parameter
                return params
            }
        }
        var requestQueue = Volley.newRequestQueue(this)
        val policy = DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        stringRequest.retryPolicy = policy
        requestQueue.add(stringRequest)
    }

    override fun onDestroy() {
        super.onDestroy()
        hideAlertDialog()
    }

}
