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
import com.apriantrimulyana.mitramas.model.setter.Companion.URL_SERVICE_1
import com.apriantrimulyana.mitramas.util.OtherUtil.hideAlertDialog
import com.apriantrimulyana.mitramas.util.OtherUtil.showAlertDialogLoading
import es.dmoral.prefs.Prefs
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import java.util.HashMap

class Login : AppCompatActivity() {

    private var etEmail: EditText? = null
    private var etPassword: EditText?= null

    private var hsEmail: String = ""
    private var hsPassword: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail     = et_email
        etPassword  = et_password

        bt_login.setOnClickListener(View.OnClickListener {
            hsEmail     = etEmail!!.getText().toString().trim({ it <= ' ' })
            hsPassword  = etPassword!!.getText().toString().trim({ it <= ' ' })

            if(hsEmail.equals("")){
                Toast.makeText(this@Login, "Email Harus di Isi", Toast.LENGTH_LONG).show()
            }else if(hsPassword.equals("")){
                Toast.makeText(this@Login, "Password Harus di Isi", Toast.LENGTH_LONG).show()
            }else{
                sendData()
            }
        })

    }


    private fun sendData(){
        hideAlertDialog()
        showAlertDialogLoading(this@Login, "Please Wait ...")
        val stringRequest = object : StringRequest(Request.Method.POST, URL_SERVICE_1,
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

                                Prefs.with(this@Login).write("login", id_user)
                                Prefs.with(this@Login).write("login_nama", nama)
                                Prefs.with(this@Login).write("login_email", email)

                                finish()
                                if(SelectMenu.actTransaksiDelivery != null){
                                    SelectMenu.actTransaksiDelivery!!.finish()
                                }
                                val intent = Intent(this@Login, Home::class.java)
                                startActivity(intent)
                            }
                        } else {
                            Toast.makeText(this@Login, "Username atau Password tidak sesuai", Toast.LENGTH_LONG).show()
                        }
                        hideAlertDialog()
                    } catch (e: Exception) {
                        Log.e("Login", "error $e")
                        hideAlertDialog()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this@Login, " Tidak Terhubung " + error.message, Toast.LENGTH_LONG).show()
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
