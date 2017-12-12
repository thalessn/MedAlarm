package com.gmail.thales_silva_nascimento.alarmmed;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public  class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    //USADA NA CLASSE CRIA PERFIL DO USUÀRIO

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String tag = getTag();
        String hora = String.valueOf(hourOfDay);
        String minuto = String.valueOf(minute);
        if(hourOfDay < 10){
            hora = "0" + hora;
        }
        if(minute < 10){
            minuto = "0" + minuto;
        }

        switch (tag) {
            case "timePicker1":
                TextView t = (TextView) getActivity().findViewById(R.id.hCafeManha);
                t.setText(hora + " : " + minuto);
                Toast.makeText(view.getContext(), tag, Toast.LENGTH_SHORT).show();
                break;
            case "timePicker2":
                TextView t1 = (TextView) getActivity().findViewById(R.id.hAlmoco);
                t1.setText(hora + " : " + minuto);
                break;
            case "timePicker3":
                TextView t2 = (TextView) getActivity().findViewById(R.id.hJanta);
                t2.setText(hora + " : " + minuto);
                break;
            case "timePicker4":
                TextView t3 = (TextView) getActivity().findViewById(R.id.hDormi);
                t3.setText(hora + " : " + minuto);
                break;
        }
    }
}
