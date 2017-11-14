package com.bitspilanidvm.bosm2017.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitspilanidvm.bosm2017.Modals.Contacts;
import com.bitspilanidvm.bosm2017.R;

import java.util.List;

/**
 * Created by sammy on 29/8/17.
 */

public class ContactAdapter extends ArrayAdapter<Contacts> {
    Context context;
    Typeface typeface;
    int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 434;

    public ContactAdapter(Context context, List<Contacts> list, Typeface typeface) {
        super(context, R.layout.contact_cards, list);
        this.context = context;
        this.typeface = typeface;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.contact_cards, null, false);
            viewHolder = new ViewHolder();

            viewHolder.name = convertView.findViewById(R.id.contact_name);
            viewHolder.department = convertView.findViewById(R.id.department);
            viewHolder.call = convertView.findViewById(R.id.call);
            viewHolder.mail = convertView.findViewById(R.id.mail);

            viewHolder.name.setTypeface(typeface);
            viewHolder.department.setTypeface(typeface);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        Contacts item = getItem(position);
        viewHolder.name.setText(item.getName());
        viewHolder.department.setText(item.getDept_info());
        setClickListners(viewHolder, item);
        return convertView;
    }

    void setClickListners(final ViewHolder viewHolder, final Contacts item) {

        viewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + item.getNumber()));
                viewHolder.call.getContext().startActivity(intent);

            }
        });

        viewHolder.call.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(view.getContext(), item.getNumber(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        viewHolder.mail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(view.getContext(), item.getEmail(), Toast.LENGTH_LONG).show();
                return true;
            }
        });

        viewHolder.mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(item.getEmail());
            }
        });

    }

    protected void sendEmail(String email) {
        Log.i("Send email", "");

        String[] TO = {email};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            Log.i("Finished email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    static class ViewHolder {
        TextView name;
        TextView department;
        ImageView call;
        ImageView mail;
    }
}
