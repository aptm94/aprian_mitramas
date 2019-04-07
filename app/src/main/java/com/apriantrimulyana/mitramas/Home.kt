package com.apriantrimulyana.mitramas

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.apriantrimulyana.mitramas.adapter.ListActivityRViewAdapter
import com.apriantrimulyana.mitramas.model.ListModel
import com.apriantrimulyana.mitramas.model.setter.Companion.URL_SERVICE_4
import com.apriantrimulyana.mitramas.util.RecyclerTouchListener
import es.dmoral.prefs.Prefs
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject
import java.util.ArrayList
import java.util.HashMap

class Home : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private var varSwipe: SwipeRefreshLayout? = null
    private var rvView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var postArrayList: ArrayList<ListModel>? = null
    private var adapter: ListActivityRViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        varSwipe = swipe
        rvView = rv_main

        varSwipe!!.setOnRefreshListener(this)
        //varSwipe!!.post { calllistData() }

        rvView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        rvView!!.layoutManager = layoutManager
        rvView!!.addOnItemTouchListener(RecyclerTouchListener(this,
                rvView!!, object : RecyclerTouchListener.ClickListener {

            override fun onClick(view: View, position: Int) {
                val j = postArrayList!![position]
                val id = j.getData1()

            }

            override fun onLongClick(view: View?, position: Int) {

            }
        }))


        box_logout.setOnClickListener(View.OnClickListener {
            Prefs.with(this@Home).clear()
            val moveLogin = Intent(this@Home, Login::class.java)
            startActivity(moveLogin)
            finish()
        })

        fab_add.setOnClickListener(View.OnClickListener {
            val moveAdd = Intent(this@Home, AddActivity::class.java)
            startActivity(moveAdd)
        })

        val login_nama = Prefs.with(this@Home).read("login_nama")
        if (login_nama != "") {
            et_hi.setText("Hi "+login_nama+" !")
        } else {
            et_hi.setText("Hi")
        }
    }

    fun calllistData() {
        varSwipe!!.isRefreshing = true

        val stringRequest = object : StringRequest(Request.Method.POST, URL_SERVICE_4,
                Response.Listener { response ->
                    postArrayList = ArrayList<ListModel>()
                    try {
                        val json = JSONObject(response)
                        val success = json.getString("code")
                        val message = json.getString("message")

                        if (success == "200") {
                            val cek_data = json.getString("data")
                            if (cek_data != "") {
                                val data = json.getJSONArray("data")
                                for (i in 0 until cek_data.length) {
                                    val ar = data.getJSONObject(i)

                                    val id_activity = ar.getString("id_activity")
                                    val activity    = ar.getString("activity")
                                    val entry_date   = ar.getString("entry_date")

                                    postArrayList!!.add(ListModel(id_activity, activity, entry_date))
                                }
                            }
                        }


                    } catch (e: Exception) {
                        Log.e("list activity", "error $e")
                    }

                    adapter = ListActivityRViewAdapter(this@Home, postArrayList!!)
                    rvView!!.adapter = adapter
                    varSwipe!!.isRefreshing = false
                },
                Response.ErrorListener { error ->
                    varSwipe!!.isRefreshing = false
                    Toast.makeText(this@Home, "Tidak Terhubung", Toast.LENGTH_LONG).show()
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
                val login = Prefs.with(this@Home).read("login")
                params["id_user"]  = login
                Log.w("DATA_POST", " list : $params")
                //returning parameter
                return params
            }
        }
        val requestQueue = Volley.newRequestQueue(this)
        val policy = DefaultRetryPolicy(10000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        stringRequest.retryPolicy = policy
        requestQueue.add(stringRequest)
    }

    override fun onRefresh() {
        calllistData()
    }

    override fun onResume() {
        super.onResume()

        calllistData()
    }

}
