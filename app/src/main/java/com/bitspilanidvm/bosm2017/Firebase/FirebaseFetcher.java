package com.bitspilanidvm.bosm2017.Firebase;

import com.bitspilanidvm.bosm2017.Modals.FixtureSportsData;
import com.bitspilanidvm.bosm2017.Modals.NonFixtureSportsData;
import com.bitspilanidvm.bosm2017.Universal.GLOBAL_DATA;
import com.google.firebase.database.DataSnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FirebaseFetcher {

    final static List<Integer> Fixtures= new ArrayList<>();
    final static List<Integer> NonFixtures= new ArrayList<>();

    public static void initialize(){
        Fixtures.add(0,1);
        NonFixtures.add(0,2);
        NonFixtures.add(1,3);
        Fixtures.add(1,4);
        Fixtures.add(2,5);
        Fixtures.add(3,6);
        NonFixtures.add(2,7);
        NonFixtures.add(3,8);
        Fixtures.add(4,9);
        Fixtures.add(5,10);
        NonFixtures.add(4,12);
        Fixtures.add(6,13);
        Fixtures.add(7,14);
        Fixtures.add(8,15);
        Fixtures.add(9,16);
        Fixtures.add(10,17);
        Fixtures.add(11,18);
        Fixtures.add(12,19);
        Fixtures.add(13,20);
        NonFixtures.add(5,21);
        Fixtures.add(14,23);
        Fixtures.add(15,24);
        Fixtures.add(16,26);
        NonFixtures.add(6,25);
        NonFixtures.add(7, 11);
    }

    public static void fetchAndStore(DataSnapshot dataSnapshot){

        for (DataSnapshot sportType : dataSnapshot.getChildren()) {
            if(Arrays.asList(GLOBAL_DATA.INSTANCE.getFixtures()).contains(Integer.valueOf(sportType.getKey()))){
                List<FixtureSportsData> SportList= new ArrayList();
                for (DataSnapshot sportEvent : sportType.getChildren()) {
                    ArrayList<String> categoryResultList = new ArrayList<>();

                    if(sportEvent.getKey().compareTo("rounds")==0){
                        String round =(String) sportEvent.getValue();
                        List<String> RoundList = Arrays.asList(round.split(","));
                        GLOBAL_DATA.INSTANCE.getSports().SportsRoundList.add(Integer.valueOf(sportType.getKey()),RoundList);
                    }

                    else if(sportEvent.getKey().compareTo("matches")==0)
                    {
                        for (DataSnapshot sportMatches : sportType.child("matches").getChildren())
                        {
                            String TeamA = (String) sportMatches.child("Team1").getValue();
                            String TeamB = (String) sportMatches.child("Team2").getValue();
                            String date = (String) sportMatches.child("Date").getValue();
                            String time = (String) sportMatches.child("Time").getValue();
                            String venue= (String) sportMatches.child("Venue").getValue();
                            String round = (String) sportMatches.child("Round").getValue();
                            String winner = (String) sportMatches.child("Winner").getValue();
                            String scheduleTime = (String) sportMatches.child("ScheduleDate").getValue();
                            String resultTime = (String) sportMatches.child("ResultTime").getValue();
                            String teamAScore = (String) sportMatches.child("ScoreA").getValue();
                            String teamBScore = (String) sportMatches.child("ScoreB").getValue();
                            Date scheduleDate = Calendar.getInstance().getTime();
                            Date resultDate = Calendar.getInstance().getTime();
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            try {
                                if (scheduleTime != null)
                                    scheduleDate = df.parse(scheduleTime);

                                if (resultTime != null)
                                    resultDate = df.parse(resultTime);
                            }catch (ParseException e){
                                e.printStackTrace();
                            }
                            FixtureSportsData data = new FixtureSportsData(TeamA,TeamB,date,time,venue,round,winner, scheduleDate, resultDate, teamAScore, teamBScore);
                            SportList.add(data);
                        }
                    }
                }
                GLOBAL_DATA.INSTANCE.getSports().fixtureSportsDataList.add(Integer.valueOf(sportType.getKey()),SportList);

            }
            else /*if(NonFixtures.contains(Integer.valueOf(sportType.getKey())))*/ {

                List<NonFixtureSportsData> DataList= new ArrayList();
                for (DataSnapshot sportEvent : sportType.getChildren()) {

                    if (sportEvent.getKey().compareTo("rounds") == 0) {
                        String round = (String) sportEvent.getValue();
                        List<String> RoundList = new ArrayList<String>();
                        RoundList = Arrays.asList(round.split(","));
                        GLOBAL_DATA.INSTANCE.getSports().SportsRoundList.add(Integer.valueOf(sportType.getKey()), RoundList);
                    }
                    else if (sportEvent.getKey().compareTo("matches") == 0) {

                        for( DataSnapshot sportMatches : sportType.child("matches").getChildren()){

                            String date = (String) sportMatches.child("Date").getValue();
                            String time = (String) sportMatches.child("Time").getValue();
                            String venue = (String) sportMatches.child("Venue").getValue();
                            String round = (String) sportMatches.child("Round").getValue();
                            String scheduleTime = (String) sportMatches.child("ScheduleDate").getValue();
                            String resultTime = (String) sportMatches.child("ResultTime").getValue();
                            Date scheduleDate = Calendar.getInstance().getTime();
                            Date resultDate = Calendar.getInstance().getTime();
                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                            try {
                                if (scheduleTime != null)
                                        scheduleDate = df.parse(scheduleTime);

                                if (resultTime != null)
                                    resultDate = df.parse(resultTime);

                            }catch (ParseException e){
                                e.printStackTrace();
                            }

                            ArrayList<String> categoryNameList = new ArrayList<>();
                            ArrayList<String> categoryDescList = new ArrayList<>();
                            ArrayList<ArrayList<String>> categoryResultList = new ArrayList<>();
                            ArrayList<ArrayList<String>> categoryScoreList = new ArrayList<>();

                            for (DataSnapshot sportEventsDetails : sportMatches.getChildren()) {
                                if (sportEventsDetails.getKey().compareTo("CategoryDescription") == 0) {
                                    for (DataSnapshot catDesc : sportEventsDetails.getChildren()) {
                                        String categoryDescElement = (String) catDesc.getValue();
                                        categoryDescList.add(categoryDescElement);
                                    }
                                } else if (sportEventsDetails.getKey().compareTo("CategoryName") == 0) {
                                    for (DataSnapshot catName : sportEventsDetails.getChildren()) {
                                        String categoryNameElement = (String) catName.getValue();
                                        categoryNameList.add(categoryNameElement);
                                    }
                                } else if (sportEventsDetails.getKey().compareTo("CategoryResult") == 0) {

                                    for (DataSnapshot catName : sportEventsDetails.getChildren()) {
                                        ArrayList<String> CategoryWiseResult = new ArrayList<String>();
                                        for (DataSnapshot catResult : catName.getChildren()) {
                                            String resultElement = (String) catResult.getValue();
                                            CategoryWiseResult.add(resultElement);

                                            for (int i = categoryResultList.size(); i <= Integer.valueOf(catName.getKey()); i++) {

                                                ArrayList<String> dummyCategoryWiseResult = new ArrayList<String>();
                                                categoryResultList.add(i, dummyCategoryWiseResult);

                                            }
                                        }
                                        categoryResultList.add(Integer.valueOf(catName.getKey()),CategoryWiseResult);
                                    }
                                } else if (sportEventsDetails.getKey().compareTo("CategoryScore") == 0) {

                                    for (DataSnapshot catName : sportEventsDetails.getChildren()) {
                                        ArrayList<String> CategoryWiseResult = new ArrayList<String>();
                                        for (DataSnapshot catResult : catName.getChildren()) {
                                            String resultElement = (String) catResult.getValue();
                                            CategoryWiseResult.add(resultElement);

                                            for (int i = categoryScoreList.size(); i <= Integer.valueOf(catName.getKey()); i++) {

                                                ArrayList<String> dummyCategoryWiseResult = new ArrayList<String>();
                                                categoryScoreList.add(i, dummyCategoryWiseResult);

                                            }
                                        }
                                        categoryScoreList.add(Integer.valueOf(catName.getKey()),CategoryWiseResult);
                                    }
                                }
                            }
                            NonFixtureSportsData data = new NonFixtureSportsData(categoryNameList, categoryDescList, date, time, venue, round, categoryResultList, scheduleDate, resultDate, categoryScoreList);
                            DataList.add(data);
                        }}}
                GLOBAL_DATA.INSTANCE.getSports().nonFixtureSportsDataList.add(Integer.valueOf(sportType.getKey()),DataList);
            }
        }
    }
}

