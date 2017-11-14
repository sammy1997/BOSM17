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
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bitspilanidvm.bosm2017.R
import com.bitspilanidvm.bosm2017.Universal.URL
import com.dd.processbutton.iml.ActionProcessButton
import com.google.gson.JsonParseException
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class Registration : Fragment(){

    lateinit var hamburgerIcon: ImageView
    lateinit var registrationText: TextView
    lateinit var name: EditText
    lateinit var gender: RadioGroup
    lateinit var email: EditText
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var state: EditText
    lateinit var city: EditText
    lateinit var college: EditText
    lateinit var phone: EditText
    lateinit var register: ActionProcessButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_registration, container, false)

        hamburgerIcon = view.findViewById(R.id.hamburgerIcon)
        registrationText = view.findViewById(R.id.registration)
        name = view.findViewById(R.id.name)
        gender = view.findViewById(R.id.gender)
        email = view.findViewById(R.id.email)
        username = view.findViewById(R.id.username)
        password = view.findViewById(R.id.password)
        state = view.findViewById(R.id.state)
        city = view.findViewById(R.id.city)
        college = view.findViewById(R.id.college)
        phone = view.findViewById(R.id.phone)
        register = view.findViewById(R.id.register)

        hamburgerIcon.setOnClickListener {
            if (activity.drawerLayout.isDrawerOpen(GravityCompat.START))
                activity.drawerLayout.closeDrawer(GravityCompat.START)
            else
                activity.drawerLayout.openDrawer(GravityCompat.START)
        }

        registrationText.typeface = Typeface.createFromAsset(activity.assets, "fonts/Coves-Bold.otf")

        register.setOnClickListener {
            val name = name.text.toString()
            val gender = if (gender.checkedRadioButtonId == R.id.male) "M" else if (gender.checkedRadioButtonId == R.id.female) "F" else ""
            val email = email.text.toString()
            val username = username.text.toString()
            val password = password.text.toString()
            val state = state.text.toString()
            val city = city.text.toString()
            val college = college.text.toString()
            val phone = phone.text.toString()

            var error = false

            when{
                name == "" -> { Snackbar.make(view, "Enter Name", Snackbar.LENGTH_SHORT).show(); error = true}
                gender == "" -> { Snackbar.make(view, "Select Gender", Snackbar.LENGTH_SHORT).show(); error = true}
                email == "" -> { Snackbar.make(view, "Enter Email", Snackbar.LENGTH_SHORT).show(); error = true}
                username == "" -> { Snackbar.make(view, "Enter Username", Snackbar.LENGTH_SHORT).show(); error = true}
                password == "" -> { Snackbar.make(view, "Enter Password", Snackbar.LENGTH_SHORT).show(); error = true}
                state == "" -> { Snackbar.make(view, "Enter State", Snackbar.LENGTH_SHORT).show(); error = true}
                city == "" -> { Snackbar.make(view, "Enter City", Snackbar.LENGTH_SHORT).show(); error = true}
                college == "" -> { Snackbar.make(view, "Enter College", Snackbar.LENGTH_SHORT).show(); error = true}
                phone == "" -> { Snackbar.make(view, "Enter Phone", Snackbar.LENGTH_SHORT).show(); error = true}
            }

            if (error)
                return@setOnClickListener

            register.setMode(ActionProcessButton.Mode.ENDLESS)
            register.progress = 1


            //preparing profile json object
            val profileData = JSONObject()
            profileData.put("name", name)
            profileData.put("city", city)
            profileData.put("state", state)
            profileData.put("college", college)
            profileData.put("phone", phone)
            profileData.put("gender", gender)
            profileData.put("email", email)

            //preparing registration object
            val regData = JSONObject()
            regData.put("username", username)
            regData.put("password", password)
            regData.put("profile", profileData)

            AndroidNetworking.post(URL.REGISTER)
                    .addJSONObjectBody(regData)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {
                            register.progress = 100
                            Log.e("response", response.toString(4))
                            val messageFragment = Message()
                            val bundle = Bundle()
                            try {
                                bundle.putString("fragment_message", response.getString("fragment_message"))
                                messageFragment.arguments = bundle
                            }catch (e: Exception){
                                e.printStackTrace()
                            }
                            val transaction = activity.supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.rootConstraintLayout, messageFragment)
                            transaction.commit()
                        }

                        override fun onError(anError: ANError) {
                            register.progress = -1
                            try {
                                val error = JSONObject(anError.errorBody)
                                Snackbar.make(view, error.getString("message"), Snackbar.LENGTH_LONG).show()
                            }catch (e: JsonParseException){

                            }
                        }
                    })
        }

        return view
    }
}