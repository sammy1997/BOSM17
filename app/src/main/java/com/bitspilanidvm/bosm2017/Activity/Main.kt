package com.bitspilanidvm.bosm2017.Activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.androidnetworking.AndroidNetworking
import com.bitspilanidvm.bosm2017.Adapters.DetailsViewPager
import com.bitspilanidvm.bosm2017.Adapters.NavBarAdapter
import com.bitspilanidvm.bosm2017.ClickListeners.ItemClickListener
import com.bitspilanidvm.bosm2017.Custom.ReverseInterpolator
import com.bitspilanidvm.bosm2017.Firebase.FirebaseFetcher
import com.bitspilanidvm.bosm2017.Fragments.*
import com.bitspilanidvm.bosm2017.Modals.FixtureSportsData
import com.bitspilanidvm.bosm2017.Modals.NavBarItem
import com.bitspilanidvm.bosm2017.Modals.Sports
import com.bitspilanidvm.bosm2017.R
import com.bitspilanidvm.bosm2017.Universal.*
import com.crashlytics.android.Crashlytics
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import io.fabric.sdk.android.Fabric
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Main : AppCompatActivity(), View.OnClickListener, Animator.AnimatorListener, OnMapReadyCallback {

    lateinit var tiles: Array<CardView>
    lateinit var tilesText: Array<TextView>
    lateinit var titlesImages: Array<ImageView>
    lateinit var bosmTextView: TextView
    lateinit var hamburgerIcon: ImageView
    lateinit var drawerLayout: DrawerLayout
    //lateinit var ntb: NavigationTabBar
    lateinit var headerCard: CardView
    lateinit var navBarRecyclerView: RecyclerView

    val displayMetrics = DisplayMetrics()
    val detailsFragment = Details()

    var isCurrentlyInTransition = false
    var isDetailsFragmentPresent = false
    var isAFragmentPresent = false
    var isEntering = true

    val expandedAppBarHeight = 200
    val transitionAnimationDuration = 500L

    val rectCenter = Rect()
    val rectLeft = Array(3) { _ -> Rect() }
    val rectRight = Array(3) { _ -> Rect() }

    val rectInit = Array(4) { _ -> Rect() }
    val rectTextInit = Array(4) { _ -> Rect() }

    var username = ""
    var password = ""

    var shouldBeHandledBySystem = false
    var shouldBackButtonLock = false
    var shouldBackButtonDisable = false

    val addedSports = ArrayList<JSONObject>()
    val availableSports = ArrayList<JSONObject>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Fabric.with(this, Crashlytics())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        AndroidNetworking.initialize(this)


        bosmTextView = findViewById(R.id.bosmTextView)
        hamburgerIcon = findViewById(R.id.hamburgerIcon)
        tiles = arrayOf(findViewById(R.id.schedule), findViewById(R.id.results), findViewById(R.id.ongoing), findViewById(R.id.events))
        tilesText = arrayOf(findViewById(R.id.scheduleText), findViewById(R.id.resultsText), findViewById(R.id.ongoingText), findViewById(R.id.eventsText))
        titlesImages = arrayOf(findViewById(R.id.scheduleImage), findViewById(R.id.resultsImage), findViewById(R.id.ongoingImage), findViewById(R.id.eventsImage))
        drawerLayout = findViewById(R.id.drawerLayout)
        //ntb = findViewById(R.id.ntb_vertical)
        navBarRecyclerView = findViewById(R.id.navBarRecyclerView)
        headerCard = findViewById(R.id.headerCard)

        supportFragmentManager.beginTransaction().add(R.id.rootConstraintLayout, detailsFragment).hide(detailsFragment).commit()

        windowManager.defaultDisplay.getMetrics(displayMetrics)
        window.setBackgroundDrawableResource(R.drawable.background)

        for (i in 0..3)
            picasso(this, GLOBAL_DATA.imageDrawableRes[i]).into(titlesImages[i])

        val ikarosTypeface = Typeface.createFromAsset(assets, "fonts/Ikaros-Regular.otf")
        bosmTextView.typeface = ikarosTypeface

        for (i in tilesText)
            i.typeface = ikarosTypeface

        for (i in tiles)
            i.setOnClickListener(this)

        val navBarListener = object : ItemClickListener {
            override fun onItemClicked(index: Int) {

                shouldBackButtonDisable =  false
                shouldBeHandledBySystem = false
                shouldBackButtonLock = false

                when (index) {
                    0 -> {
                        supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, detailsFragment).hide(detailsFragment).commit()
                        showAllViewsAgain()
                        isDetailsFragmentPresent = false
                        isAFragmentPresent = false
                        drawerLayout.closeDrawer(Gravity.START)
                    }
                    1 -> {
                        clearAllProperties()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            window.navigationBarColor = Color.BLACK
                        supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, Subscribe()).commit()
                        isAFragmentPresent = true
                        drawerLayout.closeDrawer(Gravity.START)
                    }
                    2 -> {
                        clearAllProperties()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            window.navigationBarColor = Color.BLACK
                        supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, Login()).commit()
                        isAFragmentPresent = true
                        drawerLayout.closeDrawer(Gravity.START)
                    }
                    3 -> {
                        clearAllProperties()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            window.navigationBarColor = Color.BLACK
                        supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, Registration()).commit()
                        isAFragmentPresent = true
                        drawerLayout.closeDrawer(Gravity.START)
                    }
                    4 -> {
                        clearAllProperties()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            window.navigationBarColor = Color.BLACK
                        drawerLayout.closeDrawer(Gravity.START)
                        isAFragmentPresent = true
                        val mapFragment = SupportMapFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, mapFragment).commit()
                        mapFragment.getMapAsync(this@Main)
                    }
                    5 -> {
                        clearAllProperties()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            window.navigationBarColor = Color.BLACK
                        supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, Contact()).commit()
                        isAFragmentPresent = true
                        drawerLayout.closeDrawer(Gravity.START)
                    }
                    6 -> {
                        clearAllProperties()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            window.navigationBarColor = Color.BLACK
                        supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, Sponsors()).commit()
                        isAFragmentPresent = true
                        drawerLayout.closeDrawer(Gravity.START)
                    }
                    7 -> {
                        clearAllProperties()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            window.navigationBarColor = Color.BLACK
                        supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, Developers()).commit()
                        isAFragmentPresent = true
                        drawerLayout.closeDrawer(Gravity.START)
                    }
                    8 -> {
                        clearAllProperties()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                            window.navigationBarColor = Color.BLACK
                        supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, Blog()).commit()
                        isAFragmentPresent = true
                        drawerLayout.closeDrawer(Gravity.START)
                    }
                }
            }
        }

        val navBarItems = arrayOf(NavBarItem(R.drawable.sidebar_home, "Home"),
                NavBarItem(R.drawable.sidebar_updates, "Updates"),
                NavBarItem(R.drawable.sidebar_login, "Login"),
                NavBarItem(R.drawable.sidebar_register, "Register"),
                NavBarItem(R.drawable.sidebar_maps, "Map"),
                NavBarItem(R.drawable.sidebar_contact, "Contact Us"),
                NavBarItem(R.drawable.sidebar_sponsors, "Sponsors"),
                NavBarItem(R.drawable.sidebar_developers, "Developers"),
                NavBarItem(R.drawable.sidebar_blog, "Blog"))

        navBarRecyclerView.layoutManager = LinearLayoutManager(this)
        navBarRecyclerView.adapter = NavBarAdapter(navBarItems, navBarListener)

        hamburgerIcon.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START))
                drawerLayout.closeDrawer(GravityCompat.START)
            else
                drawerLayout.openDrawer(GravityCompat.START)
        }

        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener(){
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                for (i in tilesText)
                    i.translationX = slideOffset * 200
                for (i in tiles)
                    i.translationX = slideOffset * 100
            }
        })

        drawerLayout.post { createRectArray() }

        val mDatabase = Utils.database.reference.child("Schedule")

        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                GLOBAL_DATA.sports = Sports()
                GLOBAL_DATA.availableSchedule.clear()
                GLOBAL_DATA.availableResults.clear()

                FirebaseFetcher.fetchAndStore(dataSnapshot)
                getAvailableSchedulesAndResults()
                sortAvailableSchedulesAndResults()
                setUpHeadingsAndDetails()
                fillAvailableOngoingSchedule()
                fillOngoingList()

                for (i in 0..3)
                (detailsFragment.detailsViewPager.adapter as DetailsViewPager).
                        recyclerMap[i]
                        ?.adapter
                        ?.notifyDataSetChanged()

            }
        })
    }

    override fun onClick(view: View) {

        if (!isCurrentlyInTransition) {
            val position = when(view.id){
                R.id.schedule -> 0
                R.id.results -> 1
                R.id.ongoing -> 2
                R.id.events -> 3
                else -> 4
            }

            when(view.id){
                R.id.schedule -> {
                    if (GLOBAL_DATA.headingsSchedule.size == 0)
                        detailsFragment.noItemsText.text = "Schedule isn't available for now. Check out later for updates"
                    else
                        detailsFragment.noItemsText.text = ""
                }
                R.id.results -> {
                    if (GLOBAL_DATA.headingsResults.size == 0)
                        detailsFragment.noItemsText.text = "No Results available for now. Check out later for updates"
                    else
                        detailsFragment.noItemsText.text = ""
                }
                R.id.ongoing -> {
                    if (GLOBAL_DATA.ongoing.size == 0)
                        detailsFragment.noItemsText.text = "No Ongoing Events"
                    else
                        detailsFragment.noItemsText.text = ""
                }
            }

            detailsFragment.headerViewPager.currentItem = position
            detailsFragment.headerViewPager.visibility = View.INVISIBLE
            supportFragmentManager.beginTransaction().show(detailsFragment).commit()

            val oAnimator = ObjectAnimator.ofFloat(detailsFragment.detailsViewPager, "translationY", (drawerLayout.height - 200.toPx()), 0f)
            oAnimator.duration = transitionAnimationDuration
            oAnimator.interpolator = DecelerateInterpolator()
            oAnimator.start()

            isEntering = true
            getCompleteEnterAnimator(position).start()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

//        LatLng me=new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
        val gymg = LatLng(28.359211, 75.590495)
        val medc =  LatLng(28.357417, 75.591219)
        val srground = LatLng(28.365923,75.587759)
        val anc = LatLng(28.360346, 75.589632)
        val sac = LatLng(28.360710, 75.585639)
        val fd3 = LatLng(28.363988, 75.586274)
        val clocktower = LatLng(28.363906, 75.586980)
        val fd2 = LatLng(28.364059, 75.587873)
        val uco = LatLng(28.363257, 75.590715)
        val icici = LatLng(28.357139, 75.590436)
        val axis = LatLng(28.361605, 75.585046)
        val fk = LatLng(28.361076, 75.585457)
        val ltc = LatLng(28.365056, 75.590092)
        val nab = LatLng(28.362228, 75.587346)
        val swimmingPool = LatLng(28.3607699,75.5913962)


        val cameraPosition = CameraPosition.Builder().
                target(clocktower).
                tilt(60f).
                zoom(17f).
                bearing(0f).
                build()

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        googleMap.addMarker(MarkerOptions().position(anc).title("ANC").snippet("All Night Canteen"))
        googleMap.addMarker(MarkerOptions().position(sac).title("SAC").snippet("Student Activity Center"))
        googleMap.addMarker(MarkerOptions().position(fd3).title("FD3").snippet("Faculty Division-III(31xx-32xx)"))
        googleMap.addMarker(MarkerOptions().position(clocktower).title("Clock Tower").snippet("Auditorium"))
        googleMap.addMarker(MarkerOptions().position(fd2).title("FD2").snippet("Faculty Division-II(21xx-22xx)"))
        googleMap.addMarker(MarkerOptions().position(uco).title("UCO Bank ATM"))
        googleMap.addMarker(MarkerOptions().position(icici).title("ICICI ATM"))
        googleMap.addMarker(MarkerOptions().position(axis).title("AXIS Bank ATM"))
        googleMap.addMarker(MarkerOptions().position(fk).title("FoodKing").snippet("Restaurant"))
        googleMap.addMarker(MarkerOptions().position(ltc).title("LTC").snippet("Lecture Theater Complex(510x)"))
        googleMap.addMarker(MarkerOptions().position(nab).title("NAB").snippet("New Academic Block(60xx-61xx)"))
        googleMap.addMarker(MarkerOptions().position(gymg).title("GYMG").snippet("Gym Grounds"))
        googleMap.addMarker(MarkerOptions().position(medc).title("MedC").snippet("Medical Center"))
        googleMap.addMarker(MarkerOptions().position(srground).title("SR Grounds").snippet("SR Bhawan Grounds"))
        googleMap.addMarker(MarkerOptions().position(swimmingPool).title("Swimming Pool").snippet("Bits Swimming Pool"))
//        mMap.addMarker(new MarkerOptions().position(me).title("You are here!").snippet("Consider yourself located"));
//        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.isBuildingsEnabled = true
    }

    fun setUpHeadingsAndDetails(){

        GLOBAL_DATA.headingsSchedule.clear()
        GLOBAL_DATA.headingsResults.clear()
        GLOBAL_DATA.detailsResults.clear()
        GLOBAL_DATA.detailsSchedule.clear()


        var index = 0
        val df = SimpleDateFormat("dd MMM, hh:mm a")

        for (i in GLOBAL_DATA.availableSchedule) {
            GLOBAL_DATA.headingsSchedule.add(GLOBAL_DATA.sportsMap[i] ?: "Not Available")
            GLOBAL_DATA.detailsSchedule.add("Last Updated at ${df.format((GLOBAL_DATA.availableScheduleMap[i] ?: Date()))}")
            index++
        }

        index = 0
        for (i in GLOBAL_DATA.availableResults) {
            GLOBAL_DATA.headingsResults.add(GLOBAL_DATA.sportsMap[i] ?: "Not Available")
            GLOBAL_DATA.detailsResults.add("Last Updated at ${df.format((GLOBAL_DATA.availableResultsMap[i] ?: Date()))}")
            index++
        }
    }

    fun getAvailableSchedulesAndResults(){
        for (i in 0..26) {
            if (GLOBAL_DATA.sports.fixtureSportsDataList[i].isNotEmpty()) {
                GLOBAL_DATA.availableSchedule.add(i)
                if (getWinnerListFromFixtureSportsDataList(GLOBAL_DATA.sports.fixtureSportsDataList[i]).isNotEmpty())
                    GLOBAL_DATA.availableResults.add(i)
            }

            if (GLOBAL_DATA.sports.nonFixtureSportsDataList[i].isNotEmpty()) {
                GLOBAL_DATA.availableSchedule.add(i)
                if (getWinnerListFromNonFixtureSportsDataDecoupledList(convertListToNonFixtureSportsDecoupledList(GLOBAL_DATA.sports.nonFixtureSportsDataList[i])).isNotEmpty())
                    GLOBAL_DATA.availableResults.add(i)
            }
        }

    }

    fun sortAvailableSchedulesAndResults(){

        GLOBAL_DATA.availableSchedule.forEach { i -> GLOBAL_DATA.availableScheduleMap.put(i, getLatestUpdateScheduleDate(i))}
        GLOBAL_DATA.availableResults.forEach { i -> GLOBAL_DATA.availableResultsMap.put(i, getLatestUpdateResultDate(i))}

        Collections.sort(GLOBAL_DATA.availableSchedule, kotlin.Comparator { first, second ->
            return@Comparator GLOBAL_DATA.availableScheduleMap[second]?.compareTo(GLOBAL_DATA.availableScheduleMap[first]) ?: 0
        })

        Collections.sort(GLOBAL_DATA.availableResults, kotlin.Comparator { first, second ->
            return@Comparator GLOBAL_DATA.availableResultsMap[second]?.compareTo(GLOBAL_DATA.availableResultsMap[first]) ?: 0
        })
    }

    fun getLatestUpdateScheduleDate(i: Int): Date {
        if (i in GLOBAL_DATA.fixtures){
            var latestDate: Date? = null
            for (j in GLOBAL_DATA.sports.fixtureSportsDataList[i]) {
                if (latestDate == null)
                    latestDate = j.scheduleTime
                else if (j.scheduleTime > latestDate)
                    latestDate = j.scheduleTime
            }
            return latestDate ?: Date()
        }else{
            var latestDate: Date? = null
            for (j in GLOBAL_DATA.sports.nonFixtureSportsDataList[i]) {
                if (latestDate == null)
                    latestDate = j.scheduleTime
                else if (j.scheduleTime > latestDate)
                    latestDate = j.scheduleTime
            }
            return latestDate ?: Date()
        }
    }

    fun getLatestUpdateResultDate(i: Int): Date {
        if (i in GLOBAL_DATA.fixtures){
            var latestDate: Date? = null
            for (j in getWinnerListFromFixtureSportsDataList(GLOBAL_DATA.sports.fixtureSportsDataList[i])) {
                Log.e("$i", j.resultTime.toString())
                if (latestDate == null)
                    latestDate = j.resultTime
                else if (j.resultTime > latestDate)
                    latestDate = j.resultTime
            }
            return latestDate ?: Date()
        }else{
            var latestDate: Date? = null
            for (j in getWinnerListFromNonFixtureSportsDataDecoupledList(convertListToNonFixtureSportsDecoupledList(GLOBAL_DATA.sports.nonFixtureSportsDataList[i]))) {
                if (latestDate == null)
                    latestDate = j.resultTime
                else if (j.resultTime > latestDate)
                    latestDate = j.resultTime
            }
            return latestDate ?: Date()
        }
    }

    fun fillAvailableOngoingSchedule(){
        GLOBAL_DATA.ongoingFixturesMap.clear()
        GLOBAL_DATA.ongoingNonFixturesMap.clear()
        for (i in GLOBAL_DATA.availableSchedule)
            fillOngoingDetailsFor(i)
    }

    fun fillOngoingDetailsFor(i: Int){
        val dateFormat = SimpleDateFormat("MMM dd yyyy HH:mm:ss")

        if (i in GLOBAL_DATA.fixtures) {
            val results = getWinnerListFromFixtureSportsDataList(GLOBAL_DATA.sports.fixtureSportsDataList[i])
            val list = ArrayList<FixtureSportsData>()
            for (j in GLOBAL_DATA.sports.fixtureSportsDataList[i]) {
                val date = dateFormat.parse("${j.date} ${j.time}")
                if ((System.currentTimeMillis() - date.time < GLOBAL_DATA.ongoingHour) && (System.currentTimeMillis() - date.time >= 0) && (j !in results)) {
                    list.add(j)
                }
            }

            if (list.isNotEmpty()) {
                GLOBAL_DATA.ongoingFixturesMap.put(GLOBAL_DATA.sportsMap[i] ?: "", list)
            }
        }
        else{
            val decoupledList = convertListToNonFixtureSportsDecoupledList(GLOBAL_DATA.sports.nonFixtureSportsDataList[i])
            val results = getWinnerListFromNonFixtureSportsDataDecoupledList(decoupledList)
            val list = ArrayList<NonFixtureSportsDataDecoupled>()
            for (j in decoupledList) {
                val date = dateFormat.parse("${j.date} ${j.time}")
                if ((System.currentTimeMillis() - date.time < GLOBAL_DATA.ongoingHour)  && (System.currentTimeMillis() - date.time >= 0) && (j !in results))
                    list.add(j)
            }

            if (list.isNotEmpty())
                GLOBAL_DATA.ongoingNonFixturesMap.put(GLOBAL_DATA.sportsMap[i] ?: "", list)
        }
    }

    fun fillOngoingList(){
        GLOBAL_DATA.ongoing.clear()

        for ((k, v) in GLOBAL_DATA.ongoingFixturesMap)
            GLOBAL_DATA.ongoing.add(k)

        for ((k, v) in GLOBAL_DATA.ongoingNonFixturesMap)
            GLOBAL_DATA.ongoing.add(k)

        Collections.sort(GLOBAL_DATA.ongoing)
    }

    override fun onAnimationRepeat(p0: Animator?) {}

    override fun onAnimationCancel(p0: Animator?) {}

    override fun onAnimationEnd(p0: Animator?) {
        isCurrentlyInTransition = false

        if (isEntering) {
            detailsFragment.noItemsText.visibility = View.VISIBLE
            detailsFragment.headerViewPager.visibility = View.VISIBLE
            clearAllProperties()
        }else{
            supportFragmentManager.beginTransaction().hide(detailsFragment).commit()
            isDetailsFragmentPresent = false
        }
    }

    override fun onAnimationStart(p0: Animator?) {
        isCurrentlyInTransition = true

        if (isEntering){
            isDetailsFragmentPresent = true
        }else{
            detailsFragment.headerViewPager.visibility = View.INVISIBLE
            detailsFragment.noItemsText.visibility = View.INVISIBLE
            showAllViewsAgain()
        }
    }

    fun getCompleteEnterAnimator(position: Int): Animator {
        val animatorSet = AnimatorSet()
        val animatorList = ArrayList<Animator>(4)
        for (i in 0..3) {
            when {
                i < position -> {
                    animatorList.add(getVtoVScaleAndTranslateAnimator(tiles[i], rectInit[i], rectLeft[position - i - 1]))
                    animatorList.add(getVtoVScaleAndTranslateAnimator(tilesText[i], rectTextInit[i], rectLeft[position - i - 1]))
                }
                i == position -> {
                    animatorList.add(getVtoVScaleAndTranslateAnimator(tiles[i], rectInit[i], rectCenter))
                    animatorList.add(getVtoVScaleAndTranslateAnimator(tilesText[i], rectTextInit[i], rectCenter))
                }
                i > position -> {
                    animatorList.add(getVtoVScaleAndTranslateAnimator(tiles[i], rectInit[i], rectRight[i - position - 1]))
                    animatorList.add(getVtoVScaleAndTranslateAnimator(tilesText[i], rectTextInit[i], rectRight[i - position - 1]))
                }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            animatorList.add(getNavBarColorAnimator(GLOBAL_DATA.imageDrawableRes[position]))

        animatorSet.playTogether(animatorList)
        animatorSet.interpolator = DecelerateInterpolator()
        animatorSet.addListener(this)
        return animatorSet
    }

    fun getCompleteExitAnimator(): Animator {
        val animator = getCompleteEnterAnimator(detailsFragment.headerViewPager.currentItem)
        animator.interpolator = ReverseInterpolator()

        val oAnimator = ObjectAnimator.ofFloat(detailsFragment.detailsViewPager, "translationY", 0f, (drawerLayout.height - 200.toPx()))
        oAnimator.duration = transitionAnimationDuration
        oAnimator.interpolator = DecelerateInterpolator()
        oAnimator.start()

        return animator
    }

    fun createRectArray() {
        val bottom = expandedAppBarHeight.toPx().toInt()
        var left = drawerLayout.width * -3

        for (i in 0..6) {
            when (i) {
                in 0..2 -> rectLeft[2 - i].set(left, 0, left + drawerLayout.width, bottom)
                3 -> rectCenter.set(left, 0, left + drawerLayout.width, bottom)
                in 4..6 -> rectRight[i - 4].set(left, 0, left + drawerLayout.width, bottom)
            }
            left += drawerLayout.width
        }

        for (i in 0..3) {
            tiles[i].getGlobalVisibleRect(rectInit[i])
            tilesText[i].getGlobalVisibleRect(rectTextInit[i])
        }
    }

    fun getVtoVScaleAndTranslateAnimator(viewToAnimate: View, fromViewRect: Rect, toViewRect: Rect): AnimatorSet {

        val animatorSet = AnimatorSet()

        if (viewToAnimate is TextView) {
            val translateXAnimator = ValueAnimator.ofFloat(0f, (toViewRect.left + toViewRect.right) / 2f - (fromViewRect.left + fromViewRect.right) / 2f)
            translateXAnimator.addUpdateListener { valueAnimator -> viewToAnimate.translationX = valueAnimator.animatedValue as Float }

            val translateYAnimator = ValueAnimator.ofFloat(0f, (toViewRect.top + toViewRect.bottom) / 2f - (fromViewRect.top + fromViewRect.bottom) / 2f)
            translateYAnimator.addUpdateListener { valueAnimator -> viewToAnimate.translationY = valueAnimator.animatedValue as Float }

            val textScaleAnimator = ValueAnimator.ofFloat(1f, 2f)
            textScaleAnimator.addUpdateListener { animator ->
                viewToAnimate.scaleX = animator.animatedValue as Float
                viewToAnimate.scaleY = animator.animatedValue as Float
            }
            animatorSet.playTogether(textScaleAnimator, translateXAnimator, translateYAnimator)
        } else {
            val translateXAnimator = ValueAnimator.ofFloat(0f, (toViewRect.left + toViewRect.right) / 2f - (fromViewRect.left + fromViewRect.right) / 2f)
            translateXAnimator.addUpdateListener { valueAnimator -> viewToAnimate.translationX = valueAnimator.animatedValue as Float }

            val translateYAnimator = ValueAnimator.ofFloat(0f, (toViewRect.top + toViewRect.bottom) / 2f - (fromViewRect.top + fromViewRect.bottom) / 2f)
            translateYAnimator.addUpdateListener { valueAnimator -> viewToAnimate.translationY = valueAnimator.animatedValue as Float }

            val scaleXAnimator = ValueAnimator.ofFloat(1f, toViewRect.width().div(fromViewRect.width().toFloat()))
            scaleXAnimator.addUpdateListener { valueAnimator -> viewToAnimate.scaleX = valueAnimator.animatedValue as Float }

            val scaleYAnimator = ValueAnimator.ofFloat(1f, toViewRect.height().div(fromViewRect.height().toFloat()))
            scaleYAnimator.addUpdateListener { valueAnimator -> viewToAnimate.scaleY = valueAnimator.animatedValue as Float }

            animatorSet.playTogether(scaleXAnimator, scaleYAnimator, translateXAnimator, translateYAnimator)
        }

        animatorSet.duration = transitionAnimationDuration
        return animatorSet
    }

    fun getNavBarColorAnimator(resImage: Int): Animator {
        val valueAnimator = ValueAnimator.ofArgb(window.navigationBarColor, getDominantColor(resImage))
        valueAnimator.addUpdateListener { vAnimator -> window.navigationBarColor = vAnimator.animatedValue as Int }
        valueAnimator.duration = transitionAnimationDuration
        return valueAnimator
    }

    fun clearAllProperties() {
        for (i in tiles) {
            i.translationX = 0f
            i.translationY = 0f
            i.scaleX = 1f
            i.scaleY = 1f
            i.visibility = View.INVISIBLE
        }

        for (i in tilesText) {
            i.scaleX = 1f
            i.scaleY = 1f
            i.translationX = 0f
            i.translationY = 0f
            i.visibility = View.INVISIBLE
        }

        headerCard.visibility = View.INVISIBLE
    }

    fun showAllViewsAgain() {
        for (i in tiles)
            i.visibility = View.VISIBLE

        for (i in tilesText)
            i.visibility = View.VISIBLE

        headerCard.visibility = View.VISIBLE
    }

    fun picasso(context: Context, resourceId: Int): RequestCreator {
        return Picasso.with(context)
                .load(resourceId)
                .fit()
    }

    fun Int.toPx() = this * displayMetrics.density

    fun Int.toDp() = (this / displayMetrics.density).toInt()

    fun getDominantColor(res: Int): Int {
        val bitmap = BitmapFactory.decodeResource(resources, res)
        val newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true)
        val color = newBitmap.getPixel(0, 0)
        newBitmap.recycle()
        return color
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else if (shouldBackButtonLock){
            shouldBackButtonLock = false
        } else if (shouldBeHandledBySystem){
            super.onBackPressed()
            shouldBeHandledBySystem = false
        } else if(shouldBackButtonDisable){

        } else if (isAFragmentPresent){
            supportFragmentManager.beginTransaction().replace(R.id.rootConstraintLayout, detailsFragment).hide(detailsFragment).commit()
            showAllViewsAgain()
            isDetailsFragmentPresent = false
            isAFragmentPresent = false
        } else if (isDetailsFragmentPresent) {

            if (detailsFragment.isBottomSheetExpanded()) {
                detailsFragment.hideBottomSheet()
            } else if (GLOBAL_DATA.textScale < 1.05f) {
                detailsFragment.expandAppBarLayout()
            } else {

                if (!isCurrentlyInTransition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        window.navigationBarColor = Color.BLACK

                    isEntering = false
                    detailsFragment.noItemsText.visibility = View.INVISIBLE
                    getCompleteExitAnimator().start()
                }
            }
        } else {
            super.onBackPressed()
        }
    }

}