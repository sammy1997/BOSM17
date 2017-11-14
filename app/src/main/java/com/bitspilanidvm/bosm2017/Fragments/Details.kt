package com.bitspilanidvm.bosm2017.Fragments

import android.animation.*
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bitspilanidvm.bosm2017.Activity.Main
import com.bitspilanidvm.bosm2017.Adapters.*
import com.bitspilanidvm.bosm2017.Adapters.EventItem
import com.bitspilanidvm.bosm2017.ClickListeners.DetailsRecyclerViewClickListener
import com.bitspilanidvm.bosm2017.ClickListeners.StarClickListener
import com.bitspilanidvm.bosm2017.Custom.CustomPager
import com.bitspilanidvm.bosm2017.Custom.Transformer_HeaderPage
import com.bitspilanidvm.bosm2017.Modals.FixtureSportsData
import com.bitspilanidvm.bosm2017.Notifications.Notifications
import com.bitspilanidvm.bosm2017.R
import com.bitspilanidvm.bosm2017.Universal.*
import com.bitspilanidvm.bosm2017.ViewHolder.DetailedItem
import java.util.*
import kotlin.collections.ArrayList

class Details : Fragment() {

    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var headerViewPager: CustomPager
    lateinit var appBarLayout: AppBarLayout
    lateinit var detailsViewPager: CustomPager
    lateinit var bottomSheetParent: CardView
    lateinit var bottomSheetBehaviour: BottomSheetBehavior<CardView>
    lateinit var bottomSheetRecyclerView: RecyclerView
    lateinit var close: ImageView
    lateinit var typeface: Typeface
    lateinit var backButton: ImageView
    lateinit var noItemsText: TextView

    var viewHolderDetailTemp: DetailedItem? = null
    var cardX: Float = 0f
    var titleX: Float = 0f
    var detailsX: Float = 0f

    val starred = ArrayList<String>()
    val notificationKeys = ArrayList<String>()
    lateinit var tinyDB: TinyDB

    var starClickListener = object : StarClickListener {
        override fun onStarClicked(key: String, isChecked: Boolean, title: String, text: String, imgRes: Int, date: Date) {
            val notificationKey = "${date.time - System.currentTimeMillis() - 1800000}-|-|-${(System.currentTimeMillis() / 100).toInt()}-|-|-$text"
            if (isChecked) {
                starred.add(key)
                notificationKeys.add(notificationKey)
                Notifications.scheduleNotification(activity, date.time - System.currentTimeMillis() - 1800000, (System.currentTimeMillis() / 100).toInt(), "$title Event Reminder", text, imgRes)
                Toast.makeText(context, "Starred. You will be notified 30 minutes before the event", Toast.LENGTH_SHORT).show()
            } else {
                for (i in notificationKeys) {
                    val data = i.split("-|-|-")
                    if (data[2] == text) {
                        val delay = data[0].toLong()
                        val id = data[1].toInt()
                        Notifications.cancelNotification(activity, delay, id, "$title Event Reminder", text, imgRes)
                        Toast.makeText(context, "Reminder Removed", Toast.LENGTH_SHORT).show()
                        starred.remove(key)
                        notificationKeys.remove(notificationKey)
                    }
                }

            }
            tinyDB.putListString("starred", starred)
            tinyDB.putListString("notificationKeys", notificationKeys)
        }
    }

    val listener1 = object : DetailsRecyclerViewClickListener {
        override fun onItemClick(itemHolder: DetailedItem, position: Int) {

            animateItemExit(itemHolder)
                bottomSheetRecyclerView.adapter = EventItem(com.bitspilanidvm.bosm2017.Universal.EventItem(
                        GLOBAL_DATA.imagePicRes[position],
                        GLOBAL_DATA.heading[position],
                        GLOBAL_DATA.time[position],
                        GLOBAL_DATA.description[position]
                ))
        }
    }
    val listener2 = object : DetailsRecyclerViewClickListener {
        override fun onItemClick(itemHolder: DetailedItem, position: Int) {

            var sportNo = GLOBAL_DATA.sportsMapReverse[itemHolder.titleTextView.text] ?: 0

            if (sportNo in GLOBAL_DATA.fixtures) {
                bottomSheetRecyclerView.adapter = ScheduleFixture(
                        GLOBAL_DATA.ongoingFixturesMap[itemHolder.titleTextView.text] ?: ArrayList<FixtureSportsData>(),
                        typeface,
                        null,
                        null,
                        null,
                        null,
                        true)
            } else {
                bottomSheetRecyclerView.adapter = ScheduleNonFixture(
                        GLOBAL_DATA.ongoingNonFixturesMap[itemHolder.titleTextView.text] ?: ArrayList<NonFixtureSportsDataDecoupled>(),
                        typeface,
                        null,
                        null,
                        null,
                        null,
                        true)
            }
            bottomSheetRecyclerView.adapter.notifyDataSetChanged()
            animateItemExit(itemHolder)
        }
    }
    val listener3 = object : DetailsRecyclerViewClickListener {
        override fun onItemClick(itemHolder: DetailedItem, position: Int) {


            Log.e("clicked", position.toString())
            var sportNo = GLOBAL_DATA.sportsMapReverse[itemHolder.titleTextView.text] ?: 0

            if (sportNo in GLOBAL_DATA.fixtures) {
                Collections.sort(GLOBAL_DATA.sports.fixtureSportsDataList[sportNo], kotlin.Comparator { obj1, obj2 ->
                    return@Comparator obj2.scheduleTime.compareTo(obj1.scheduleTime)
                })
                bottomSheetRecyclerView.adapter = ScheduleFixture(GLOBAL_DATA.sports.fixtureSportsDataList[sportNo], typeface, starClickListener, starred, "${itemHolder.titleTextView.text}", sportNo)
            } else {
                Collections.sort(GLOBAL_DATA.sports.nonFixtureSportsDataList[sportNo], kotlin.Comparator { obj1, obj2 ->
                    return@Comparator obj2.scheduleTime.compareTo(obj1.scheduleTime)
                })
                bottomSheetRecyclerView.adapter = ScheduleNonFixture(convertListToNonFixtureSportsDecoupledList(GLOBAL_DATA.sports.nonFixtureSportsDataList[sportNo]), typeface, starClickListener, starred, "${itemHolder.titleTextView.text}", sportNo)
            }


            bottomSheetRecyclerView.adapter.notifyDataSetChanged()
            animateItemExit(itemHolder)
        }
    }
    val listener4 = object : DetailsRecyclerViewClickListener {
        override fun onItemClick(itemHolder: DetailedItem, position: Int) {

            var sportNo = GLOBAL_DATA.sportsMapReverse[itemHolder.titleTextView.text] ?: 0

            if (sportNo in GLOBAL_DATA.fixtures) {
                Collections.sort(GLOBAL_DATA.sports.fixtureSportsDataList[sportNo], kotlin.Comparator { obj1, obj2 ->
                    return@Comparator obj2.resultTime.compareTo(obj1.resultTime)
                })
                bottomSheetRecyclerView.adapter = ResultsFixture(getWinnerListFromFixtureSportsDataList(GLOBAL_DATA.sports.fixtureSportsDataList[sportNo]), typeface)
            } else {
                Collections.sort(GLOBAL_DATA.sports.nonFixtureSportsDataList[sportNo], kotlin.Comparator { obj1, obj2 ->
                    return@Comparator obj2.resultTime.compareTo(obj1.resultTime)
                })
                bottomSheetRecyclerView.adapter = ResultsNonFixture(getWinnerListFromNonFixtureSportsDataDecoupledList(convertListToNonFixtureSportsDecoupledList(GLOBAL_DATA.sports.nonFixtureSportsDataList[sportNo])), typeface)
            }
            bottomSheetRecyclerView.adapter.notifyDataSetChanged()
            animateItemExit(itemHolder)
        }
    }

    lateinit var header: HeaderViewPager

    val argbEvaluator = ArgbEvaluator()
    val floatEvaluator = FloatEvaluator()

    var bottomSheetPrevState = BottomSheetBehavior.STATE_HIDDEN

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context != null)
            header = HeaderViewPager(context)

        tinyDB = TinyDB(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_details, container, false)

        starred.clear()
        starred.addAll(tinyDB.getListString("starred"))
        notificationKeys.clear()
        notificationKeys.addAll(tinyDB.getListString("notificationKeys"))

        // inflating views
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout)
        headerViewPager = view.findViewById(R.id.headerViewPager)
        appBarLayout = view.findViewById(R.id.appBarLayout)
        detailsViewPager = view.findViewById(R.id.detailsViewPager)
        bottomSheetParent = view.findViewById(R.id.bottomSheetParent)
        bottomSheetRecyclerView = view.findViewById(R.id.bottomSheetRecylerView)
        backButton = view.findViewById(R.id.back_button)
        close = view.findViewById(R.id.close)
        noItemsText = view.findViewById(R.id.noItems)

        detailsViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING){
                    noItemsText.visibility = View.INVISIBLE
                }
                if (state == ViewPager.SCROLL_STATE_SETTLING){
                    noItemsText.visibility = View.VISIBLE
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        noItemsText.visibility = View.VISIBLE
                        if (GLOBAL_DATA.headingsSchedule.size == 0)
                            noItemsText.text = "Schedule isn't available for now. Check out later for updates"
                        else
                            noItemsText.text = ""
                    }
                    1 -> {
                        noItemsText.visibility = View.VISIBLE
                        if (GLOBAL_DATA.headingsResults.size == 0)
                            noItemsText.text = "No Results available for now. Check out later for updates"
                        else
                            noItemsText.text = ""
                    }
                    2 -> {
                        noItemsText.visibility = View.VISIBLE
                        if (GLOBAL_DATA.ongoing.size == 0)
                            noItemsText.text = "No Ongoing Events"
                        else
                            noItemsText.text = ""
                    }
                    3 -> {
                        noItemsText.visibility = View.VISIBLE
                        noItemsText.text = ""
                    }
                }
            }
        })


        // getting bottom sheet behaviour
        bottomSheetBehaviour = BottomSheetBehavior.from(bottomSheetParent)
        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_HIDDEN

        // synchronizing header and details view pager
        headerViewPager.setViewPager(detailsViewPager)
        detailsViewPager.setViewPager(headerViewPager)

        typeface = Typeface.createFromAsset(activity.assets, "fonts/Coves-Bold.otf")

        // setting adapters
        headerViewPager.adapter = header
        detailsViewPager.adapter = DetailsViewPager(context,
                arrayOf(DetailsRecyclerView(GLOBAL_DATA.headingsSchedule, GLOBAL_DATA.detailsSchedule, listener3, activity),
                        DetailsRecyclerView(GLOBAL_DATA.headingsResults, GLOBAL_DATA.detailsResults, listener4, activity),
                        DetailsRecyclerView(GLOBAL_DATA.ongoing, GLOBAL_DATA.ongoing, listener2, activity),
                        DetailsRecyclerView(ArrayList(GLOBAL_DATA.heading.asList()), ArrayList(GLOBAL_DATA.details.asList()), listener1, activity)))

        // setting up page transformer for header view pager
        headerViewPager.setPageTransformer(true, Transformer_HeaderPage())

        // Setting bottom Sheet Recycler View
        bottomSheetRecyclerView.layoutManager = LinearLayoutManager(context)

        // Navigation bar color array
        val navColorArray = arrayOf(getDominantColor(R.drawable.schedule),
                getDominantColor(R.drawable.results),
                getDominantColor(R.drawable.ongoing),
                getDominantColor(R.drawable.events))

        // If sdk is greater than lollipop then synchronize nav bar color with header view pager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            headerViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                    if (p0 < header.count - 1 && p0 < navColorArray.size - 1) {
                        activity.window.navigationBarColor = argbEvaluator.evaluate(p1, navColorArray[p0], navColorArray[p0 + 1]) as Int
                    } else {
                        activity.window.navigationBarColor = navColorArray[navColorArray.size - 1]
                    }
                }
            })
        }

        backButton.setOnClickListener { activity.onBackPressed() }

        close.setOnClickListener { hideBottomSheet() }

        // Header offset listener
        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->

            GLOBAL_DATA.textScale = floatEvaluator.evaluate(verticalOffset / appBarLayout.totalScrollRange.toFloat() * -1, 2f, 1f)
            header.pageMap[headerViewPager.currentItem]?.scaleX = GLOBAL_DATA.textScale
            header.pageMap[headerViewPager.currentItem]?.scaleY = GLOBAL_DATA.textScale
        }

        // Bottom Sheet offset listener
        bottomSheetBehaviour.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                bottomSheetParent.alpha = 1 + slideOffset

                if (bottomSheetPrevState == BottomSheetBehavior.STATE_EXPANDED) {
                    viewHolderDetailTemp?.cardView?.translationX = floatEvaluator.evaluate(slideOffset * -1, cardX, 0f)
                    viewHolderDetailTemp?.titleTextView?.translationX = floatEvaluator.evaluate(slideOffset * -1, titleX, 0f)
                    viewHolderDetailTemp?.detailsTextView?.translationX = floatEvaluator.evaluate(slideOffset * -1, detailsX, 0f)
                }
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    (activity as Main).shouldBackButtonDisable = false
                    bottomSheetPrevState = BottomSheetBehavior.STATE_HIDDEN

                    viewHolderDetailTemp?.imageView?.translationX = 0f
                    viewHolderDetailTemp?.cardView?.translationX = 0f
                    viewHolderDetailTemp?.detailsTextView?.translationX = 0f
                    viewHolderDetailTemp?.titleTextView?.translationX = 0f

                } else if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    (activity as Main).shouldBackButtonDisable = false
                    bottomSheetPrevState = BottomSheetBehavior.STATE_EXPANDED

                    if (cardX == 0f) {
                        cardX = viewHolderDetailTemp?.cardView?.translationX ?: 0f
                        titleX = viewHolderDetailTemp?.titleTextView?.translationX ?: 0f
                        detailsX = viewHolderDetailTemp?.detailsTextView?.translationX ?: 0f
                    }
                } else if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    (activity as Main).shouldBackButtonDisable = true
                } else if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    (activity as Main).shouldBackButtonDisable = true
                }
            }
        })

        return view
    }


    // get dominant color from header view pager image for the navigation bar color
    fun getDominantColor(res: Int): Int {
        val bitmap = BitmapFactory.decodeResource(resources, res)
        val newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true)
        val color = newBitmap.getPixel(0, 0)
        newBitmap.recycle()
        return color
    }

    // Animate item while opening a sheet
    fun animateItemExit(itemHolder: DetailedItem) {

        viewHolderDetailTemp = itemHolder

        val cardViewAnimator = ObjectAnimator.ofFloat(itemHolder.cardView, "translationX", 0f, -(itemHolder.cardView.left + itemHolder.cardView.width).toFloat())
        val headingAnimator = ObjectAnimator.ofFloat(itemHolder.titleTextView, "translationX", 0f, (itemHolder.itemView.width - itemHolder.titleTextView.left).toFloat())
        val detailsAnimator = ObjectAnimator.ofFloat(itemHolder.detailsTextView, "translationX", 0f, (itemHolder.itemView.width - itemHolder.detailsTextView.left).toFloat())

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(cardViewAnimator, headingAnimator, detailsAnimator)
        animatorSet.duration = 200
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {}

            override fun onAnimationEnd(p0: Animator?) {
                bottomSheetBehaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }

            override fun onAnimationCancel(p0: Animator?) {}

            override fun onAnimationStart(p0: Animator?) {}
        })
        animatorSet.start()
    }

    fun isBottomSheetExpanded() = bottomSheetBehaviour.state == BottomSheetBehavior.STATE_EXPANDED || bottomSheetBehaviour.state == BottomSheetBehavior.STATE_COLLAPSED

    fun hideBottomSheet() {
        bottomSheetBehaviour.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun expandAppBarLayout() {
        appBarLayout.setExpanded(true, true)
    }


    //Extension method
    fun Int.toDp(): Int {
        var displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return (this / displayMetrics.density).toInt()
    }

    fun Int.toPx(): Int {
        var displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return (this * displayMetrics.density).toInt()
    }

}
