package com.bitspilanidvm.bosm2017.Universal

import com.bitspilanidvm.bosm2017.Modals.FixtureSportsData
import com.bitspilanidvm.bosm2017.Modals.NonFixtureSportsData
import com.google.firebase.database.FirebaseDatabase



fun convertToNonFixtureSportsDecoupledList(data: NonFixtureSportsData): List<NonFixtureSportsDataDecoupled>{
    val list = ArrayList<NonFixtureSportsDataDecoupled>()

    if (data.categoryResult.isEmpty()) {
        data.categoryResult = ArrayList<ArrayList<String>>()
        for (i in 0..(data.categoryName.size - 1))
            data.categoryResult.add(ArrayList())
    }

    if (data.categoryScore.isEmpty()) {
        data.categoryScore = ArrayList<ArrayList<String>>()
        for (i in 0..(data.categoryName.size - 1))
            data.categoryScore.add(ArrayList())
    }

    for (i in 0..(data.categoryName.size - 1)){
        list.add(NonFixtureSportsDataDecoupled(data.categoryName[i],
                data.categoryDesc[i],
                data.categoryResult[i],
                data.date,
                data.time,
                data.venue,
                data.round,
                data.scheduleTime,
                data.resultTime,
                data.categoryScore[i]))
    }
    return list
}

fun convertListToNonFixtureSportsDecoupledList(list: List<NonFixtureSportsData>): List<NonFixtureSportsDataDecoupled>{
    val finalList = ArrayList<NonFixtureSportsDataDecoupled>()

    for (i in list)
        finalList.addAll(convertToNonFixtureSportsDecoupledList(i))

    return finalList
}

fun getWinnerListFromFixtureSportsDataList(list: List<FixtureSportsData>): List<FixtureSportsData>{
    val finalList = ArrayList<FixtureSportsData>()

    list.forEach { data ->
        if (data.winner != null)
            finalList.add(data)
    }

    return finalList
}

fun getWinnerListFromNonFixtureSportsDataDecoupledList(list: List<NonFixtureSportsDataDecoupled>): List<NonFixtureSportsDataDecoupled>{
    val finalList = ArrayList<NonFixtureSportsDataDecoupled>()

    list.forEach { data ->
        if (data.categoryResult.isNotEmpty())
            finalList.add(data)
    }

    return finalList
}

object Utils {
    private var mDatabase: FirebaseDatabase? = null

    val database: FirebaseDatabase
        get() {
            if (mDatabase == null) {
                mDatabase = FirebaseDatabase.getInstance()
                mDatabase!!.setPersistenceEnabled(true)
            }
            return mDatabase!!
        }

}
