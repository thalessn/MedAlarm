package com.gmail.thales_silva_nascimento.alarmmed.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.gmail.thales_silva_nascimento.alarmmed.LembreteCompraReceiver;
import com.gmail.thales_silva_nascimento.alarmmed.Utils;
import com.gmail.thales_silva_nascimento.alarmmed.activity.LembreteCompraRemedio;
import com.gmail.thales_silva_nascimento.alarmmed.dao.LembreteCompraDAO;
import com.gmail.thales_silva_nascimento.alarmmed.model.LembreteCompra;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by thales on 10/01/18.
 */

public class LembreteCompraController {

    private Context context;
    private LembreteCompraDAO lembreteCompraDAO;

    public LembreteCompraController(Context context){
        this.lembreteCompraDAO = LembreteCompraDAO.getInstance(context);
        this.context = context;
    }

    public void cadastrarLembreteCompra(LembreteCompra lembreteCompra){
        lembreteCompraDAO.cadastrarLembreteCompra(lembreteCompra);
    }

    public void excluirLembreteCompraPorIdMed(long idMedicamento){
        lembreteCompraDAO.excluirLembreteCompraPorIdMed(idMedicamento);
    }

    public void registrarLembreteCompra(LembreteCompra lembreteCompra){

        /**
         * Intent a ser chamada para o horário programado.
         */
        Intent intent = new Intent(context, LembreteCompraReceiver.class);
        intent.putExtra("idMedicamento", lembreteCompra.getIdMedicamento());
        /**
         * O valor do resquestCode da pendingIntent será o valor 'hashcode' da id do LembreteCompra
         * que a instanciaAlarme possui.
         */
        int requestCode = Long.valueOf(lembreteCompra.getId()).hashCode();
        /**
         * PendingIntent utilizada para chamar o BroadCastReceiver
         */
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

        /**
         * Reponsável por registrar o Alarme no Sistema
         */
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        //Horário que tocará o alarme
        Calendar alarmeLembrete = getProxLembreteCompra(lembreteCompra.getHorarioAlerta());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmeLembrete.getTimeInMillis(), pendingIntent);
            Log.v("CadastrouLembrete", "alarmManager.setExactAndAllowWhileIdle");
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmeLembrete.getTimeInMillis(), pendingIntent);
            Log.v("CadastrouLembrete", "alarmManager.setExact");
            Log.v("HorarioLembrete", Utils.CalendarToStringData(alarmeLembrete) +" - "+ Utils.CalendarToStringHora(alarmeLembrete));
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmeLembrete.getTimeInMillis(), pendingIntent);
            Log.v("Cadastrou Lembrete", "alarmManager.set");
        }
    }

    private Calendar getProxLembreteCompra(String horario){
        Calendar proxHorario = Utils.StringHoraToCalendar(horario);
        if (proxHorario.getTimeInMillis() <= System.currentTimeMillis()) {
                proxHorario.add(Calendar.DAY_OF_YEAR, 1);
                Log.v("Horario ATRASADO", "Lembrte de compra sera tocado amanhã ADICIONOU UM DIA");
        }
        return proxHorario;
    }

    public LembreteCompra buscarLembretePorIDMed(long idMedicamento){
        LembreteCompra lembrete = lembreteCompraDAO.buscarLembreteCompraPorIdMed(idMedicamento);
        return lembrete;
    }

    public void atualizarLembreteCompra(LembreteCompra lembreteCompra){
        lembreteCompraDAO.atualizarLembreteCompra(lembreteCompra);
    }

}
