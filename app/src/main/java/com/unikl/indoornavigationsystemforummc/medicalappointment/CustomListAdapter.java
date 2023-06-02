package com.unikl.indoornavigationsystemforummc.medicalappointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.indoornavigationsystemforummc.R;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Appointment> appointments;
    private Context context;

    CustomListAdapter (ArrayList<Appointment> inAppointments, Context context){
        this.appointments = inAppointments;
        this.context = context;
    }

    @Override
    public int getCount() {
        return appointments.size();
    }

    @Override
    public Object getItem(int i) {
        return appointments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.appointment_list_item, null);
        }

        TextView displayID = (TextView)view.findViewById(R.id.lblAppointmentID);
        displayID.setText(appointments.get(i).getAppointmentID());

        TextView displayDateTime= (TextView)view.findViewById(R.id.lblAppointmentDateTime);
        displayDateTime.setText("Appointment Date & Time: " + appointments.get(i).getAppointmentDate() + ", " + appointments.get(i).getAppointmentTime());

        TextView displayAppointmentStatus = (TextView) view.findViewById(R.id.lblStatus);
        displayAppointmentStatus.setText("Status: "+ appointments.get(i).getAppointmentStatus());

        return view;
    }
}
