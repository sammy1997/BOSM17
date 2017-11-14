package com.bitspilanidvm.bosm2017.Fragments

import android.graphics.Typeface
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bitspilanidvm.bosm2017.Activity.Main
import com.bitspilanidvm.bosm2017.R
import com.bitspilanidvm.bosm2017.Universal.URL
import com.dd.processbutton.iml.ActionProcessButton
import com.google.gson.JsonParseException
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject

class Login : Fragment(){

    lateinit var hamburgerIcon: ImageView
    lateinit var loginText: TextView
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var register: Button
    lateinit var login: ActionProcessButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        hamburgerIcon = view.findViewById(R.id.hamburgerIcon)
        loginText = view.findViewById(R.id.loginText)
        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        register = view.findViewById(R.id.register)
        login = view.findViewById(R.id.login)

        loginText.typeface = Typeface.createFromAsset(activity.assets, "fonts/Coves-Bold.otf")

        hamburgerIcon.setOnClickListener {
            if (activity.drawerLayout.isDrawerOpen(GravityCompat.START))
                activity.drawerLayout.closeDrawer(GravityCompat.START)
            else
                activity.drawerLayout.openDrawer(GravityCompat.START)
        }

        if ((activity as Main).username != "" && (activity as Main).password != null)
            activity.supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, ManageSports()).commit()


        login.setOnClickListener {
            val username = username.text.toString()
            val password = password.text.toString()

            var error = false

            when{
                username == "" -> {Snackbar.make(view, "Enter Username", Snackbar.LENGTH_SHORT).show(); error = true}
                password == "" -> {Snackbar.make(view, "Enter Password", Snackbar.LENGTH_SHORT).show(); error = true}
            }

            if (error)
                return@setOnClickListener

            login.setMode(ActionProcessButton.Mode.ENDLESS)
            login.progress = 1

            val data = JSONObject()
            data.put("username", username)
            data.put("password", password)

            AndroidNetworking.post(URL.API_TOKEN)
                .addJSONObjectBody(data)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        Log.e("response", response.toString())
                        (activity as Main).username = username
                        (activity as Main).password = password

                        AndroidNetworking.get(URL.SHOW_SPORTS)
                                .addHeaders("Authorization", "JWT ${response.getString("token")}")
                                .build()
                                .getAsJSONObject(object : JSONObjectRequestListener{
                                    override fun onResponse(response: JSONObject) {
                                        login.progress = 100

                                        var sportsAddedArray = JSONArray()
                                        var sportsLeftArray = JSONArray()

                                        if (response.has("sports_added"))
                                            sportsAddedArray = response.getJSONArray("sports_added")

                                        if (response.has("sports_left"))
                                            sportsLeftArray = response.getJSONArray("sports_left")

                                        (activity as Main).addedSports.clear()
                                        (activity as Main).availableSports.clear()

                                        for (i in 0..(sportsAddedArray.length() - 1))
                                            (activity as Main).addedSports.add(sportsAddedArray.getJSONObject(i))

                                        for (i in 0..(sportsLeftArray.length() - 1))
                                            (activity as Main).availableSports.add(sportsLeftArray.getJSONObject(i))

                                        activity.supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, ManageSports()).commit()
                                    }

                                    override fun onError(anError: ANError) {
                                        login.progress = -1
                                    }
                                })
                    }

                    override fun onError(anError: ANError) {
                        Log.e("error", anError.errorBody)
                        try {
                            val error = JSONObject(anError.errorBody)
                            if (error.has("non_field_errors"))
                                Snackbar.make(view, error.getJSONArray("non_field_errors")[0].toString(), Snackbar.LENGTH_LONG).show()
                        }catch (e: JsonParseException){

                        }
                        login.progress = -1
                    }
                })

        }

        register.setOnClickListener {
            activity.supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, Registration()).commit()
        }

        return view
    }
}