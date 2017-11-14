package com.bitspilanidvm.bosm2017.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.bitspilanidvm.bosm2017.Activity.Main;
import com.bitspilanidvm.bosm2017.Adapters.ContactAdapter;
import com.bitspilanidvm.bosm2017.Modals.Contacts;
import com.bitspilanidvm.bosm2017.R;

import java.util.ArrayList;
import java.util.List;


public class Contact extends Fragment {

    String names[] = {"Pavan", "Jayshil", "Ashay", "Gautham", "Siddharth", "Jayesh", "Aman", "Shreshtha", "Vihang"};
    String dept_info[] = {"BOSM Controls", "Sports Secretary", "Publications and Correspondence", "Reception and Accomodation", "Joint Sports Secretary", "Sponsorship and Marketing", "Joint Sports Secretary", "Joint Sports Secretary", "Core Website"};
    String numbers[] = {"+91-9828629266", "+91-9828623535", "+91-9929022741", "+91-9444637124", "+91-7733974342", "+91-8897716880", "+91-9714540571", "+91-9873240714", "+91-9828630490"};
    String emails[] = {"controls@bits-bosm.org", "sportssecretary@bits-bosm.org", "pcr@bits-bosm.org", "recnacc@bits-bosm.org", "siddharth@bits-bosm.org","sponsorship@bits-bosm.org", "aman@bits-bosm.org", "shreshtha@bits-bosm.org", "webmaster@bits-bosm.org"};

    ListView listView;
    List<Contacts> list;
    ImageView hamburgerIcon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        list = new ArrayList<>();

        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

        for (int i = 0; i < names.length; i++) {
            Contacts contacts = new Contacts(names[i], numbers[i], dept_info[i], emails[i]);
            list.add(contacts);
        }

        hamburgerIcon = rootView.findViewById(R.id.hamburgerIcon);

        hamburgerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((Main)getActivity()).drawerLayout.isDrawerOpen(GravityCompat.START))
                    ((Main)getActivity()).drawerLayout.closeDrawer(GravityCompat.START);
                else
                    ((Main)getActivity()).drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Coves-Bold.otf");
        ContactAdapter adapter = new ContactAdapter(this.getActivity(), list, typeface);

        listView = rootView.findViewById(R.id.contacts);
        listView.setAdapter(adapter);

        return rootView;

    }
}
