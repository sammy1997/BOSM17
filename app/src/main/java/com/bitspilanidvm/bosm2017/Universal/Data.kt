package com.bitspilanidvm.bosm2017.Universal

import com.bitspilanidvm.bosm2017.Modals.FixtureSportsData
import com.bitspilanidvm.bosm2017.Modals.Sports
import com.bitspilanidvm.bosm2017.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object GLOBAL_DATA{

    var textScale = 2f
    val ongoingHour = 3 * 3600000
    val imageDrawableRes = arrayOf(R.drawable.schedule, R.drawable.results, R.drawable.ongoing, R.drawable.events)
    //val imageDrawableRes = arrayOf(R.color.Events, R.color.Ongoing, R.color.Schedule, R.color.Results)
    val headerTitles = arrayOf("SCHEDULE", "RESULTS", "ONGOING", "EVENTS")
    val shadowColors = arrayOf(R.color.Schedule, R.color.Results, R.color.Ongoing, R.color.Events)

    val fixtures = arrayOf(1,4,5,6,9,10,13,11,14,15,16,17,18,19,20,21,23,24,26)

    val sportsMap = mapOf(Pair(1, "Hockey"),
            Pair(2, "Athletics (Boys)"),
            Pair(3, "Athletics (Girls)"),
            Pair(4, "Basketball (Boys)"),
            Pair(5, "Lawn Tennis (Girls)"),
            Pair(6, "Lawn Tennis (Boys)"),
            Pair(7, "Squash"),
            Pair(8, "Swimming (Boys)"),
            Pair(9, "Football (Boys)"),
            Pair(10, "Badminton (Boys)"),
            Pair(11, "Pool"),
            Pair(12, "Powerlifting"),
            Pair(13, "Chess"),
            Pair(14, "Table Tennis (Boys)"),
            Pair(15, "Table Tennis (Girls)"),
            Pair(16, "Taekwondo (Boys)"),
            Pair(17, "Taekwondo (Girls)"),
            Pair(18, "Volleyball (Boys)"),
            Pair(19, "Volleyball (Girls)"),
            Pair(20, "Badminton (Girls)"),
            Pair(21, "Carrom"),
            Pair(22, "Swimming (Girls)"),
            Pair(23, "Cricket"),
            Pair(24, "Football (Girls)"),
            Pair(25, "Snooker"),
            Pair(26, "Basketball (Girls)"))

    val sportsMapReverse = mapOf(Pair("Hockey", 1),
            Pair("Athletics (Boys)", 2),
            Pair("Athletics (Girls)", 3),
            Pair("Basketball (Boys)", 4),
            Pair("Lawn Tennis (Girls)", 5),
            Pair("Lawn Tennis (Boys)", 6),
            Pair("Squash", 7),
            Pair("Swimming (Boys)", 8),
            Pair("Football (Boys)", 9),
            Pair("Badminton (Boys)", 10),
            Pair("Pool", 11),
            Pair("Powerlifting", 12),
            Pair("Chess", 13),
            Pair("Table Tennis (Boys)", 14),
            Pair("Table Tennis (Girls)", 15),
            Pair("Taekwondo (Boys)", 16),
            Pair("Taekwondo (Girls)", 17),
            Pair("Volleyball (Boys)", 18),
            Pair("Volleyball (Girls)", 19),
            Pair("Badminton (Girls)", 20),
            Pair("Carrom", 21),
            Pair("Swimming (Girls)", 22),
            Pair("Cricket", 23),
            Pair("Football (Girls)", 24),
            Pair("Snooker", 25),
            Pair("Basketball (Girls)", 26))

    val sportsImageRes = mapOf<String, Int>(Pair("Hockey", R.drawable.hockey),
            Pair("Athletics (Boys)", R.drawable.atheletics),
            Pair("Athletics (Girls)", R.drawable.atheletics),
            Pair("Basketball (Boys)", R.drawable.basketball),
            Pair("Lawn Tennis (Girls)", R.drawable.tennis),
            Pair("Lawn Tennis (Boys)", R.drawable.tennis),
            Pair("Squash", R.drawable.squash),
            Pair("Swimming (Boys)", R.drawable.swimming),
            Pair("Football (Boys)", R.drawable.football),
            Pair("Badminton (Boys)", R.drawable.badminton),
            Pair("Pool", R.drawable.snooker),
            Pair("Powerlifting", R.drawable.powerlifting),
            Pair("Chess", R.drawable.chess),
            Pair("Table Tennis (Boys)", R.drawable.tabletennis),
            Pair("Table Tennis (Girls)", R.drawable.tabletennis),
            Pair("Taekwondo (Boys)", R.drawable.taekwondo),
            Pair("Taekwondo (Girls)", R.drawable.taekwondo),
            Pair("Volleyball (Boys)", R.drawable.volleyball),
            Pair("Volleyball (Girls)", R.drawable.volleyball),
            Pair("Badminton (Girls)", R.drawable.badminton),
            Pair("Carrom", R.drawable.carrom),
            Pair("Swimming (Girls)", R.drawable.swimming),
            Pair("Cricket", R.drawable.cricket),
            Pair("Football (Girls)", R.drawable.football),
            Pair("Snooker", R.drawable.snooker),
            Pair("Basketball (Girls)", R.drawable.basketball))

    val sportsImageIconRes = mapOf<String, Int>(Pair("Hockey", R.drawable.ic_hockey),
            Pair("Athletics (Boys)", R.drawable.ic_atheletics),
            Pair("Athletics (Girls)", R.drawable.ic_atheletics),
            Pair("Basketball (Boys)", R.drawable.ic_basketball),
            Pair("Lawn Tennis (Girls)", R.drawable.ic_tennis),
            Pair("Lawn Tennis (Boys)", R.drawable.ic_tennis),
            Pair("Squash", R.drawable.ic_squash),
            Pair("Swimming (Boys)", R.drawable.ic_swimming),
            Pair("Football (Boys)", R.drawable.ic_football),
            Pair("Badminton (Boys)", R.drawable.ic_badminton),
            Pair("Pool", R.drawable.snooker),
            Pair("Powerlifting", R.drawable.ic_powerlifting),
            Pair("Chess", R.drawable.ic_chess),
            Pair("Table Tennis (Boys)", R.drawable.ic_tabletennis),
            Pair("Table Tennis (Girls)", R.drawable.ic_tabletennis),
            Pair("Taekwondo (Boys)", R.drawable.ic_taekwondo),
            Pair("Taekwondo (Girls)", R.drawable.ic_taekwondo),
            Pair("Volleyball (Boys)", R.drawable.ic_volleyball),
            Pair("Volleyball (Girls)", R.drawable.ic_volleyball),
            Pair("Badminton (Girls)", R.drawable.ic_badminton),
            Pair("Carrom", R.drawable.carrom),
            Pair("Swimming (Girls)", R.drawable.ic_swimming),
            Pair("Cricket", R.drawable.ic_cricket),
            Pair("Football (Girls)", R.drawable.ic_football),
            Pair("Snooker", R.drawable.snooker),
            Pair("Basketball (Girls)", R.drawable.ic_basketball))

    val imageRes = arrayOf(R.drawable.rahul, R.drawable.sumit)
    val imagePicRes = arrayOf(R.drawable.rahul, R.drawable.football, R.drawable.event_imge_large, R.drawable.football, R.drawable.cricket, R.drawable.event_imge_large, R.drawable.football)

    val heading = arrayOf(
            "The Punch Hour",
            "Freestyle Football",
            "Gatka",
            "Street Football",
            "Box Cricket",
            "Ignition",
            "Arsenal")

    val details = arrayOf(
            "DLE", "DLE", "DLE", "DLE", "DLE",
            "Gaming Club", "Gaming Club")
    val time = arrayOf(
            "23 September | 19:00 | Audi",
            "22 September | 18:00 | Rotunda",
            "22 September | 18:00 | Rotunda",
            "22 September | 18:00 Onwards | Gym G",
            "22 September | 18:00 Onwards | Gym G",
            "22 September Onwards | Old Sac",
            "24 September | 19:00 Onwards | Gym G")

    val description = arrayOf(
            "A free comedy show night featuring the stars of comedy, Rahul Subramanian, the man who considers himself too good for the world of mechanics and Sumit Anand, who, when struck with the realization that his jokes required work, decided to get married and have life do the maximum work for him! Be prepared for a night full of jokes, giggles, hilarity and booming laughter, for the kings are here!",
            "At first, Aarish Ansari comes across as a regular football enthusiast who dribbles his way through the by-lanes of Mumbai, until he stops and effortlessly dances with his football, mesmerising you with his moves and he is going to here in BITS, this BOSM, all set to captivate you with his moves in an open show!",
            "It is an ancient martial art which has been thoroughly battle-tested. Brace yourself for this flamboyant performance by the Guinness World Record holders Daler Khalsa Group which is sure to blow away your minds and leave you star struck!",
            "Where teamwork trumps talent and spontaneity beats preparation. Where improvisation is the name of the game. Assemble your team folks, the game is about to begin!",
            "Smaller arena, higher stakes, louder cheer, unmatched glory. Cricket like you've never played it before!",
            "Welcome to the next level of competitive gaming. Presenting Ignition, BOSM's Official Gaming Competition. All it takes, is all you've got. (CS:GO ,DOTA, FIFA, BLUR)",
            "Activation spot: Simple ball activities with Arsenal Soccer Schools coaches.")

    val availableSchedule = ArrayList<Int>()
    val availableResults = ArrayList<Int>()

    val availableScheduleMap = HashMap<Int, Date>()
    val availableResultsMap = HashMap<Int, Date>()

    val headingsSchedule = ArrayList<String>()
    val headingsResults = ArrayList<String>()
    val detailsSchedule = ArrayList<String>()
    val detailsResults = ArrayList<String>()

    val ongoingFixturesMap = HashMap<String, List<FixtureSportsData>>()
    val ongoingNonFixturesMap = HashMap<String, List<NonFixtureSportsDataDecoupled>>()
    val ongoing = ArrayList<String>()


    val sponsorImageRes = arrayOf(R.drawable.ongc, R.drawable.panasonic, R.drawable.admirallogo, R.drawable.manyalogo, R.drawable.kirtilals, R.drawable.asus, R.drawable.amul, R.drawable.tuskertees, R.drawable.arsenal, R.drawable.pokerbaazi, R.drawable.luckystars, R.drawable.paytm, R.drawable.zebronics, R.drawable.quaker, R.drawable.radiomirchi, R.drawable.ninexm)
    val sponsorText = arrayOf("ONGC", "Panasonic", "Admiral", "Manya", "Kirtilal", "Asus", "Amul", "Tusker Tees", "Arsenal", "PokerBaazi", "Lucky Star", "Paytm", "Zebronics", "Quaker", "Radio Mirchi", "9XM")
    val typeText = arrayOf("Title Sponsor", "Associate Sponsor", "Gold", "Study Abroad Partner", "Gold", "Gaming Technology Partner", "Energy", "Merchandise Partner", "Technical Training", "Poker Partner", "Silver", "Payment", "Ignition Partner", "Breakfast Partner", "Radio Partner", "Media Partner")

    val developerImageRes = arrayOf(R.drawable.vaibhav, R.drawable.sombuddha, R.drawable.laddha, R.drawable.megh, R.drawable.madhur)
    val developerName = arrayOf("VAIBHAV MAHESHWARI", "SOMBUDDHA CHAKRAVARTY", "ADITYA LADDHA", "MEGH THAKKAR", "MADHUR WADHWA")
    val developerDescription = arrayOf("UI/UX Developer | Backend Developer", "Backend Developer", "Backend Developer", "REST API Developer", "UI Designer")

    var sports = Sports()
}

data class NonFixtureSportsDataDecoupled(val categoryName: String,
                                         val categoryDescription: String,
                                         val categoryResult: ArrayList<String>,
                                         val date: String,
                                         val time: String,
                                         val venue: String,
                                         val round: String,
                                         val scheduleTime: Date,
                                         val resultTime: Date,
                                         val categoryScore: ArrayList<String>)

data class EventItem(val imageRes: Int,
                     val heading: String,
                     val time: String,
                     val details: String)