package com.bitspilanidvm.bosm2017.Universal

object URL{
    val DOMAIN_PREFIX = "https://bits-bosm.org/2017/api/"
    //val DOMAIN_PREFIX = "http://192.168.43.141:8000/api/"
    val LOGIN = "${DOMAIN_PREFIX}login"
    val REGISTER = "${DOMAIN_PREFIX}register"
    val SHOW_SPORTS = "${DOMAIN_PREFIX}show_sports"
    val REGISTER_CAPTAIN = "${DOMAIN_PREFIX}register_captain"
    val MANAGE_SPORTS = "${DOMAIN_PREFIX}manage_sports"
    val LOGOUT = "${DOMAIN_PREFIX}logout"
    val API_TOKEN = "${DOMAIN_PREFIX}api_token"
    val API_TOKEN_REFRESH = "${DOMAIN_PREFIX}api_token_refresh"
    val GET_ID = "${DOMAIN_PREFIX}get_id"
    fun SPORT_TEST(id: Int) = "${DOMAIN_PREFIX}sport_test/$id"
    fun ADD_EVENTS(tc_id: Int) = "${DOMAIN_PREFIX}add_events/$tc_id"
    fun ADD_EXTRA_EVENT(tc_id: Int) = "${DOMAIN_PREFIX}add_extra_event/$tc_id"
}