package com.gmail.thales_silva_nascimento.alarmmed;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gmail.thales_silva_nascimento.alarmmed.controller.AlarmeController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.HistoricoController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.HorarioController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.InstanciaAlarmeController;
import com.gmail.thales_silva_nascimento.alarmmed.controller.MedicamentoController;
import com.gmail.thales_silva_nascimento.alarmmed.model.Alarme;
import com.gmail.thales_silva_nascimento.alarmmed.model.Horario;
import com.gmail.thales_silva_nascimento.alarmmed.model.InstanciaAlarme;
import com.gmail.thales_silva_nascimento.alarmmed.model.ItemAlarmeHistorico;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.util.Calendar;
import java.util.List;

public class OnBootService extends Service {

    private InstanciaAlarmeController instanciaAlarmeController;
    private HorarioController horarioController;
    private HistoricoController historicoController;
    private List<InstanciaAlarme> instanciaAlarmeList;
    private MedicamentoController medicamentoController;
    private AlarmeController alarmeController;

    @Override
    public void onCreate() {
        Log.v("OnBootService", "Oncreate");
        instanciaAlarmeController = InstanciaAlarmeController.getInstanciaAlarmeController(OnBootService.this);
        horarioController = HorarioController.getInstance(OnBootService.this);
        historicoController = new HistoricoController(OnBootService.this);
        medicamentoController = new MedicamentoController(OnBootService.this);
        alarmeController = new AlarmeController(OnBootService.this);
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("OnBootService", "OnStartCommand");
        //Procura por todas as instancias no banco de dados
        instanciaAlarmeList = instanciaAlarmeController.listarTodasInstancias();

        for (int i=0; i<instanciaAlarmeList.size(); i++){

            InstanciaAlarme ia = instanciaAlarmeList.get(i);
            //Montar o calendar para comparar se a instancia é inferior a data atual
            Horario horario = horarioController.buscarHorarioPorId(ia.getId_horario());
            String h = horario.getHorario().replace(" ","");
            Calendar data = ia.getData();
            data.set(Calendar.HOUR,Integer.parseInt(h.substring(0,2)));
            data.set(Calendar.MINUTE, Integer.parseInt(h.substring(3,5)));
            data.set(Calendar.SECOND, 0);

            //Verifica enquanto a data for menor e diferente de null
            while((data != null) && (data.getTimeInMillis() < Calendar.getInstance().getTimeInMillis())){
                //Encontra o medicamento
                Alarme al = alarmeController.buscarAlarmePorId(ia.getId_alarme());
                Medicamento med = medicamentoController.buscarMedicamentoPorId(al.getIdMedicamento());
                String dataProgramada = Utils.CalendarToStringData(ia.getData());
                String dataAdministrado = "-";
                String horaAdministrado = "-";
                //Cria um Item do AlarmeHistorico
                ItemAlarmeHistorico itemHis = new ItemAlarmeHistorico(med,dataProgramada,horario,ia.getId_alarme(),dataAdministrado,horaAdministrado);
                itemHis.setStatus(ItemAlarmeHistorico.STATUS_NAO_REGISTRADO);
                //Resgistra a instancia perdida no historico
                historicoController.cadastrarHistoricoMedicamento(itemHis);

                //Encontra a próxima instancia. Se exitir e ainda for menor irá repetir o processo novamente.
                data = instanciaAlarmeController.getProxInstanciaAlarme(al,horario.getHorario());
            }
            Log.v("OnBootService", "Saiu do while");

            //Verifica se a data é diferente de nulo
            if (data != null){
                Log.v("OnBootService", "Excluia instancia");
                //Deleta a instancia anterior que exitia do banco de dados
                instanciaAlarmeController.deletarInstanciaAlarmeId(ia.getId());

                //Registra novamente a instancia alarme
                ia.setData(data);
                instanciaAlarmeController.registraInstanciaAlarme(OnBootService.this, ia);
                Log.v("OnBootService", "Registrou uma nova instância");
            }
        }
        stopSelf();


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.v("OnBoot SERVICE", "OnDestroy");

    }
}
