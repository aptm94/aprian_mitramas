package com.apriantrimulyana.mitramas

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.apriantrimulyana.mitramas.model.setter
import com.apriantrimulyana.mitramas.util.OtherUtil
import com.apriantrimulyana.mitramas.util.OtherUtil.hideAlertDialog
import com.apriantrimulyana.mitramas.util.OtherUtil.showAlertDialogLoading
import es.dmoral.prefs.Prefs
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject
import java.util.HashMap

class AddActivity : AppCompatActivity() {
    private var etActivity: EditText? = null
    private var hsActivity: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        etActivity = et_activity

        bt_simpan_activity.setOnClickListener(View.OnClickListener {
            hsActivity = etActivity!!.getText().toString().trim({ it <= ' ' })

            if(hsActivity.equals("")){
                Toast.makeText(this@AddActivity, "Activity Harus di Isi", Toast.LENGTH_LONG).show()
            }else{
                sendData()
            }
        })
    }

    private fun sendData(){
        hideAlertDialog()
        showAlertDialogLoading(this@AddActivity, "Please Wait ...")
        val stringRequest = object : StringRequest(Request.Method.POST, setter.URL_SERVICE_3,
                Response.Listener { response ->
                    try {
                        val json = JSONObject(response)
                        val success = json.getString("code")
                        val message = json.getString("message")
                        if (success == "200") {
                            finish()
                        } else {
                            Toast.makeText(this@AddActivity, "Username atau Password tidak sesuai", Toast.LENGTH_LONG).show()
                        }
                        hideAlertDialog()
                    } catch (e: Exception) {
                        Log.e("Login", "error $e")
                        hideAlertDialog()
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this@AddActivity, " Tidak Terhubung " + error.message, Toast.LENGTH_LONG).show()
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

                val login = Prefs.with(this@AddActivity).read("login")
                params["id_user"]  = login
                params["activity"] = hsActivity


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
        OtherUtil.hideAlertDialog()
    }
}
