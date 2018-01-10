package com.gmail.thales_silva_nascimento.alarmmed.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.gmail.thales_silva_nascimento.alarmmed.AlarmeReceiver;
import com.gmail.thales_silva_nascimento.alarmmed.Utils;
import com.gmail.thales_silva_nascimento.alarmmed.Weekdays;
import com.gmail.thales_silva_nascimento.alarmmed.dao.InstanciaAlarmeDAO;
import com.gmail.thales_silva_nascimento.alarmmed.model.Alarme;
import com.gmail.thales_silva_nascimento.alarmmed.model.AlarmeInfo;
import com.gmail.thales_silva_nascimento.alarmmed.model.Horario;
import com.gmail.thales_silva_nascimento.alarmmed.model.InstanciaAlarme;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by thales on 06/09/17.
 */

public class InstanciaAlarmeController {

    private InstanciaAlarmeDAO instanciaAlarmeDAO;
    private static InstanciaAlarmeController instanciaAlarmeController = null;
    private HorarioController horarioController;
    private Context context;

    private InstanciaAlarmeController(Context context){
        instanciaAlarmeDAO = InstanciaAlarmeDAO.getInstance(context);
        this.horarioController = HorarioController.getInstance(context);
        this.context = context;
    }

    public static InstanciaAlarmeController getInstanciaAlarmeController(Context context){
        if(instanciaAlarmeController == null){
            return new InstanciaAlarmeController(context);
        }
        return instanciaAlarmeController;
    }

    public void resgistraInstanciaAlarme(Context context, Alarme alarme, AlarmeInfo alarmeInfo){
        //Busca uma nova instancia do alarme
        Calendar dataEHora = getProxInstanciaAlarme(alarme, alarmeInfo.getHorario());
        //Verifica se encontrou um instância
        if(dataEHora == null){
            return; // não existe instância para esse alarme neste horario
        }
        //Busca a id do Horário no Banco
        long idHorario = horarioController.buscarIdHorario(alarmeInfo.getHorario());
        //Cria um objeto InstanciaAlarme
        InstanciaAlarme instanciaAlarme = new InstanciaAlarme(dataEHora, alarmeInfo.getQtd_tomar(),idHorario, alarme.getId());
        //Cadastra a instancia no banco
        cadastrarInstanciaAlarme(instanciaAlarme);

        /**
         * Intent a ser chamada para o horário programado.
         */
        Intent intent = new Intent(context, AlarmeReceiver.class);
        intent.putExtra("horario", instanciaAlarme.getId_horario());

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        /**
         * O valor do resquestCode da pendingIntent será o valor 'hashcode' da idHorario
         * que a instanciaAlarme possui.
         */
        int requestCode = Long.valueOf(instanciaAlarme.getId_horario()).hashCode();

        /**
         * PendingIntent utilizada para chamar o BroadCastReceiver
         */
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

        String data = Utils.CalendarToStringData(instanciaAlarme.getData());
        String hora = Utils.CalendarToStringHora(instanciaAlarme.getData());
        Log.v("InstanciaProgramada", data +"   ---   "+ hora+ " codHorario = " +String.valueOf(instanciaAlarme.getId_horario()) + "" +
                " codAlarme = " + String.valueOf(instanciaAlarme.getId_alarme()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, instanciaAlarme.getData().getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, instanciaAlarme.getData().getTimeInMillis(), pendingIntent);
            Log.v("CadastrouAlarm", "alarmManager.setExact");
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, instanciaAlarme.getData().getTimeInMillis(), pendingIntent);
            Log.v("CadastrouAlarm", "alarmManager.set");
        }
    }

    public void resgistraProxInstanciaAlarme(Context context, Alarme alarme, Horario horario){
        //Busca uma nova instancia do alarme
        Calendar dataEHora = getProxInstanciaAlarme(alarme, horario.getHorario());
        //Verifica se encontrou um instância
        if(dataEHora == null){
            return; // não existe instância para esse alarme neste horario
        }
        //Busca a id do Horário no Banco
        long idHorario = horario.getId();

        //Verifica o alarmeInfo correspondente
        AlarmeInfo info = null;
        String h = horario.getHorario();
        for(AlarmeInfo a: alarme.getAlarmeInfo()){
            if(h.equals(a.getHorario())){
                info = a;
                break;
            }
        }

        //Cria um objeto InstanciaAlarme
        InstanciaAlarme instanciaAlarme = new InstanciaAlarme(dataEHora, info.getQtd_tomar(),idHorario, alarme.getId());
        //Cadastra a instancia no banco
        cadastrarInstanciaAlarme(instanciaAlarme);

        /**
         * Intent a ser chamada para o horário programado.
         */
        Intent intent = new Intent(context, AlarmeReceiver.class);
        intent.putExtra("horario", instanciaAlarme.getId_horario());

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        /**
         * O valor do resquestCode da pendingIntent será o valor 'hashcode' da idHorario
         * que a instanciaAlarme possui.
         */
        int requestCode = Long.valueOf(instanciaAlarme.getId_horario()).hashCode();

        /**
         * PendingIntent utilizada para chamar o BroadCastReceiver
         */
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

        String data = Utils.CalendarToStringData(instanciaAlarme.getData());
        String hora = Utils.CalendarToStringHora(instanciaAlarme.getData());
        Log.v("ProxInstanciaProgramada", data +"   ---   "+ hora+ " codHorario = " +String.valueOf(instanciaAlarme.getId_horario()) + "" +
                " codAlarme = " + String.valueOf(instanciaAlarme.getId_alarme()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, instanciaAlarme.getData().getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, instanciaAlarme.getData().getTimeInMillis(), pendingIntent);
            Log.v("CadastrouAlarm", "alarmManager.setExact");
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, instanciaAlarme.getData().getTimeInMillis(), pendingIntent);
            Log.v("CadastrouAlarm", "alarmManager.set");
        }
    }

    private long cadastrarInstanciaAlarme(InstanciaAlarme instanciaAlarme){
        long id = instanciaAlarmeDAO.cadastrarInstanciaAlarme(instanciaAlarme);

        return id;
    }

    public void deletarTodasInstancias(){
        instanciaAlarmeDAO.deletarTodasInstancias();
    }

    public void deletarTodasInstanciaDoAlarme(long id){
        instanciaAlarmeDAO.deletarTodasInstanciaDoAlarme(id);
    }

    private Calendar getProxInstanciaAlarme(Alarme alarme, String horario) {
        Calendar proxHorario = Utils.StringHoraToCalendar(horario);
        //Data fim do tratamento
        Calendar dataFim = alarme.getDataFim();

        dataFim.set(Calendar.HOUR_OF_DAY, 23);
        dataFim.set(Calendar.MINUTE, 59);
        dataFim.set(Calendar.SECOND, 0);
        Log.v("getProxInstanciaalarme","DataFim " +Utils.CalendarToStringData(dataFim) +" - "+ Utils.CalendarToStringHora(dataFim));
        //Adicionei esses parâmetros na classe Utils verificar se está funcionando
        //proxHorario.set(Calendar.SECOND, 0);
        //proxHorario.set(Calendar.MILLISECOND, 0);

        //Verifica se o próximo horário está atrasado em relação ao atual, se estiver
        // adiciona os dias necessários de acordo com intervalo
        if (proxHorario.getTimeInMillis() <= System.currentTimeMillis()) {
            if (alarme.getTipoRepeticao() == Alarme.REP_DIASINTERVALOS) {
                proxHorario.add(Calendar.DAY_OF_YEAR, alarme.getIntervaloRepeticao());

                Log.v("ATRASADO", "ADICIONOU " + String.valueOf(alarme.getIntervaloRepeticao()) + " DIA(S)");

                return verificaFimTratamento(proxHorario, dataFim) ? null : proxHorario;
            } else {
                proxHorario.add(Calendar.DAY_OF_YEAR, 1);
                Log.v("ATRASADO", "ELSE - ADICIONOU UM DIA");
            }
        }

        if (alarme.getTipoRepeticao() == Alarme.REP_DIASDASEMANA) {
            //Cria um objeto Weedays que transforma int em dias da semana
            Weekdays days = Weekdays.fromBits(alarme.getIntervaloRepeticao());
            //Nº de dias para o proximo dia possível.
            final int addDays = days.getDistanceToNextDay(proxHorario);
            if (addDays > 0) {
                proxHorario.add(Calendar.DAY_OF_WEEK, addDays);
            }

            Log.v("DIAS SEMANA", "ADICIONOU " + String.valueOf(addDays) + " DIA(S)");
            return verificaFimTratamento(proxHorario, dataFim) ? null : proxHorario;

        } else {
            //O tipo do intervalo será a cada N dias então a primeira instancia não tem problema.
            return verificaFimTratamento(proxHorario, dataFim) ? null : proxHorario;

        }
    }

    /**
     * Função para verificar se o próximo horário é anterior a data final do tratamento
     * @param proxHorario
     * @param dataFim
     * @return boolean
     */
    private boolean verificaFimTratamento(Calendar proxHorario, Calendar dataFim){
        Log.v("verificaFimTratamento", String.valueOf(proxHorario.after(dataFim)));
        Log.v("ProxHorario",Utils.CalendarToStringData(proxHorario)+" - " +Utils.CalendarToStringHora(proxHorario));
        Log.v("DataFim", Utils.CalendarToStringData(dataFim)+" - " +Utils.CalendarToStringHora(dataFim));
        return proxHorario.after(dataFim);
    }

    public void deletarInstanciaPorDataAlarmeHorario(String data, long idAlarme, long idHorario){
        instanciaAlarmeDAO.deletarInstanciaPorDataAlarmeHorario(data, idAlarme, idHorario);
    }


    public void adiarInstanciaAlarmePorIdHorario(long idHorario){
        /**
         * Intent a ser chamada para o horário programado.
         */
        Intent intent = new Intent(context, AlarmeReceiver.class);
        intent.putExtra("horario", idHorario);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        /**
         * O valor do resquestCode da pendingIntent será o valor 'hashcode' da idHorario
         * que a instanciaAlarme possui.
         */
        int requestCode = Long.valueOf(idHorario).hashCode();

        /**
         * PendingIntent utilizada para chamar o BroadCastReceiver
         */
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

        /**
         * REFAZER UTILIZANDO O VALOR ADOTADO NAS CONFIGURAÇÕES
         * Adiciona 2 minutos ao horário atual para adiar a instancia
         */
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.MINUTE,2);

        String data = Utils.CalendarToStringData(cal);
        String hora = Utils.CalendarToStringHora(cal);

        Log.v("Instancia Adiada", data +"   ---   "+ hora+ " codHorario = " +String.valueOf(idHorario));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            Log.v("CadastrouAlarm", "alarmManager.setExact");
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
            Log.v("CadastrouAlarm", "alarmManager.set");
        }
    }

}