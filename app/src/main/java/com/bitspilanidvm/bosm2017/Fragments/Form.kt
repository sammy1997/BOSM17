package com.bitspilanidvm.bosm2017.Fragments

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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

class Form : Fragment(){

    lateinit var hamburgerIcon: ImageView
    lateinit var heading: TextView
    lateinit var details: TextView
    lateinit var captainName: EditText
    lateinit var captainEmail: EditText
    lateinit var phoneNumber: EditText
    lateinit var coachName: EditText
    lateinit var sex: RadioGroup
    lateinit var addMemberLayout: LinearLayout
    lateinit var addMemberFAB: FloatingActionButton
    lateinit var submit: ActionProcessButton
    lateinit var scrollView: ScrollView
    val displayMetrics = DisplayMetrics()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_form, container, false)

        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)

        hamburgerIcon = view.findViewById(R.id.hamburgerIcon)
        heading = view.findViewById(R.id.heading)
        details = view.findViewById(R.id.details)
        captainName = view.findViewById(R.id.captainName)
        captainEmail = view.findViewById(R.id.captainEmail)
        phoneNumber = view.findViewById(R.id.phoneNumber)
        coachName = view.findViewById(R.id.coachName)
        sex = view.findViewById(R.id.radioSex)
        addMemberLayout = view.findViewById(R.id.addMemberLayout)
        addMemberFAB = view.findViewById(R.id.addMember)
        scrollView = view.findViewById(R.id.scrollView)
        submit = view.findViewById(R.id.submit)

        submit.progress = 0

        hamburgerIcon.setOnClickListener {
            if (activity.drawerLayout.isDrawerOpen(GravityCompat.START))
                activity.drawerLayout.closeDrawer(GravityCompat.START)
            else
                activity.drawerLayout.openDrawer(GravityCompat.START)
        }

        val sportName = arguments.getString("sportName")
        Log.e("retrieved arguments", sportName)
        var id = 0
        var minLimit = 0
        var maxLimit = 0

        for (sport in (activity as Main).addedSports){
            if (sport.getString("name") == sportName){
                id = sport.getInt("id")
                minLimit = sport.getInt("min_limit")
                maxLimit = sport.getInt("max_limit")
                break
            }
        }

        heading.text = "Team Registrations for $sportName"
        details.text = "This is a team event. Total number of participants must be between $minLimit and $maxLimit"

        addMemberFAB.setOnClickListener {
            val editText = EditText(activity)
            editText.setBackgroundResource(R.drawable.focuser)
            editText.hint = "Name of Team Member"
            editText.setPadding(15.toPx().toInt(), 0, 0, 0)
            editText.setTextColor(Color.parseColor("#757575"))
            editText.setHintTextColor(Color.parseColor("#BFC2C3"))
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 36.toPx().toInt())
            layoutParams.topMargin = 10.toPx().toInt()
            editText.layoutParams = layoutParams
            addMemberLayout.addView(editText, 0)

            scrollView.smoothScrollTo(0,0)
            editText.requestFocus()

            if (addMemberLayout.childCount == maxLimit - 1)
                addMemberFAB.visibility = View.GONE
        }



        submit.setOnClickListener {
            submit.setMode(ActionProcessButton.Mode.ENDLESS)
            submit.progress = 1

            val credentials = JSONObject()
            credentials.put("username", (activity as Main).username)
            credentials.put("password", (activity as Main).password)

            val registerCaptainData = JSONObject()
            registerCaptainData.put("event", id)
            registerCaptainData.put("name", captainName.text)
            val gender = if (sex.checkedRadioButtonId == R.id.radioMale) "M" else if (sex.checkedRadioButtonId == R.id.radioFemale) "F" else ""
            registerCaptainData.put("gender", gender)
            registerCaptainData.put("email", captainEmail.text)
            registerCaptainData.put("phone", phoneNumber.text)
            val participants = JSONArray()
            for (index in 0..(addMemberLayout.childCount - 1))
                try {
                    participants.put((addMemberLayout.getChildAt(index) as EditText).text)
                }catch (e: ClassCastException){
                    participants.put((addMemberLayout.getChildAt(index) as LinearLayout).findViewById<EditText>(R.id.member).text)
                }
            registerCaptainData.put("participants", participants.toString())

            AndroidNetworking.post(URL.API_TOKEN)
                    .addJSONObjectBody(credentials)
                    .build()
                    .getAsJSONObject(object : JSONObjectRequestListener {
                        override fun onResponse(response: JSONObject) {
                            val token = response.getString("token")

                            AndroidNetworking.get(URL.GET_ID)
                                    .addHeaders("Authorization", "JWT $token")
                                    .build()
                                    .getAsJSONObject(object : JSONObjectRequestListener{
                                        override fun onResponse(response: JSONObject) {

                                            val g_l = response.getInt("id")

                                            registerCaptainData.put("g_l", g_l)

                                            AndroidNetworking.post(URL.REGISTER_CAPTAIN)
                                                    .addHeaders("Authorization", "JWT $token")
                                                    .addJSONObjectBody(registerCaptainData)
                                                    .build()
                                                    .getAsJSONObject(object : JSONObjectRequestListener{
                                                        override fun onResponse(response: JSONObject) {

                                                            if (response.has("message")) {
                                                                val errorMessage = response.getString("message")
                                                                val messageFragment = Message()
                                                                val arguments = Bundle()
                                                                arguments.putString("fragment_message", errorMessage)
                                                                messageFragment.arguments = arguments
                                                                val transaction = activity.supportFragmentManager.beginTransaction()
                                                                (activity as Main).shouldBeHandledBySystem = true
                                                                submit.progress = 0
                                                                transaction.replace(R.id.rootConstraintLayout, messageFragment)
                                                                transaction.addToBackStack(null)
                                                                transaction.commit()
                                                            }else{

                                                                if (minLimit == 1 && maxLimit == 1){
                                                                    activity.supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, ManageSports()).commit()
                                                                }else{
                                                                    Log.e("response", response.toString(4))
                                                                    submit.progress = 100

                                                                    val captain_id = response.getJSONObject("captain").getInt("id")
                                                                    AndroidNetworking.get(URL.ADD_EVENTS(captain_id))
                                                                            .addHeaders("Authorization", "JWT $token")
                                                                            .build()
                                                                            .getAsJSONObject(object : JSONObjectRequestListener{
                                                                                override fun onResponse(response: JSONObject) {
                                                                                    submit.progress = 100
                                                                                    Log.e("response", response.toString(4))
                                                                                    val addExtraEventFragment = AddExtraEvents()
                                                                                    val arguments = Bundle()
                                                                                    arguments.putString("jsonData", response.toString())
                                                                                    arguments.putInt("tc_id", captain_id)
                                                                                    addExtraEventFragment.arguments = arguments
                                                                                    activity.supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, addExtraEventFragment).commit()
                                                                                }

                                                                                override fun onError(anError: ANError) {

                                                                                }
                                                                            })
                                                                }

                                                            }
                                                        }

                                                        override fun onError(anError: ANError) {
                                                            Log.e("inner error", anError.errorBody)
                                                            submit.progress = -1
                                                        }
                                                    })
                                        }

                                        override fun onError(anError: ANError) {
                                            submit.progress = -1
                                        }
                                    })
                        }

                        override fun onError(anError: ANError) {
                            Log.e("outer error", anError.errorBody)
                            Log.e("error", anError.errorBody)
                            try {
                                val error = JSONObject(anError.errorBody)
                                if (error.has("non_field_errors"))
                                    Snackbar.make(view, error.getJSONArray("non_field_errors")[0].toString(), Snackbar.LENGTH_LONG).show()
                            }catch (e: JsonParseException){

                            }
                            submit.progress = -1
                        }
                    })
        }

        return view
    }

    fun Int.toPx() = this * displayMetrics.density
}