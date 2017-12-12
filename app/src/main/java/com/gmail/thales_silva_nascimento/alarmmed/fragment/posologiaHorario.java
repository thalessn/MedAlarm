package com.gmail.thales_silva_nascimento.alarmmed.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by thales on 15/01/17.
 */

public class posologiaHorario extends DialogFragment {
    private Spinner sCafe;
    private Spinner sAlmoco;
    private Spinner sJanta;
    private Spinner sDormir;

    private TextView hpCafe;
    private TextView hpJanta;
    private TextView hpAlmoco;
    private TextView hpDormir;

    private Button cancelar;
    private Button salvar;
    Calendar c = Calendar.getInstance();

    @Override
    public void onStart() {
        super.onStart();
        // safety check
        if (getDialog() == null) {
            return;
        }
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();

        // Assign window properties to fill the parent

        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        getDialog().setCanceledOnTouchOutside(false);
        //getDialog().getWindow().setGravity(Gravity.CENTER);
        //getDialog().getWindow().setLayout(800,600);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_posologia_horario, container, false);


        sCafe = (Spinner) rootView.findViewById(R.id.sCafe);
        sAlmoco = (Spinner) rootView.findViewById(R.id.sAlmoco);
        sJanta = (Spinner) rootView.findViewById(R.id.sJanta);
        sDormir = (Spinner) rootView.findViewById(R.id.sDormir);

        List<String> categories = new ArrayList<String>();
        categories.add("Antes");
        categories.add("Depois");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sCafe.setAdapter(dataAdapter);
        sAlmoco.setAdapter(dataAdapter);
        sJanta.setAdapter(dataAdapter);
        sDormir.setAdapter(dataAdapter);



        hpCafe = (TextView) rootView.findViewById(R.id.hpCafe);
        hpJanta = (TextView) rootView.findViewById(R.id.hpJanta);;
        hpAlmoco = (TextView) rootView.findViewById(R.id.hpAlmoco);;
        hpDormir = (TextView) rootView.findViewById(R.id.hpDormir);;
        //Adiciona onclick listener nos textView de horário
        setOnclickListenerHorarios();

        cancelar = (Button) rootView.findViewById(R.id.btnPosCancelar);
        salvar = (Button) rootView.findViewById(R.id.btnPosSalvar);

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Apertou no botão salvar", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
        return rootView;




    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return super.onCreateDialog(savedInstanceState);
    }




    //Adiciona o evento onclick nos textView para selecionar o quanto tempo Antes ou depois
    private void setOnclickListenerHorarios(){
        hpCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timer = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hora = String.valueOf(hourOfDay);
                        String minuto = String.valueOf(minute);
                        if(hourOfDay < 10){
                            hora = "0" + hora;
                        }
                        if(minute < 10){
                            minuto = "0" + minuto;
                        }
                        hpCafe.setText(hora + " : " + minuto);
                    }
                }, hour, minute, true);
                timer.show();
            }
        });

        hpJanta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timer = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hora = String.valueOf(hourOfDay);
                        String minuto = String.valueOf(minute);
                        if(hourOfDay < 10){
                            hora = "0" + hora;
                        }
                        if(minute < 10){
                            minuto = "0" + minuto;
                        }
                        hpJanta.setText(hora + " : " + minuto);
                    }
                }, hour, minute, true);
                timer.show();
            }
        });

        hpAlmoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timer = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hora = String.valueOf(hourOfDay);
                        String minuto = String.valueOf(minute);
                        if(hourOfDay < 10){
                            hora = "0" + hora;
                        }
                        if(minute < 10){
                            minuto = "0" + minuto;
                        }
                        hpAlmoco.setText(hora + " : " + minuto);
                    }
                }, hour, minute, true);
                timer.show();
            }
        });


        hpDormir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timer = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hora = String.valueOf(hourOfDay);
                        String minuto = String.valueOf(minute);
                        if(hourOfDay < 10){
                            hora = "0" + hora;
                        }
                        if(minute < 10){
                            minuto = "0" + minuto;
                        }
                        hpDormir.setText(hora + " : " + minuto);
                    }
                }, hour, minute, true);
                timer.show();
            }
        });
    }
}
