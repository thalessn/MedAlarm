package com.gmail.thales_silva_nascimento.alarmmed.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.R;
import com.gmail.thales_silva_nascimento.alarmmed.Utils;
import com.gmail.thales_silva_nascimento.alarmmed.controller.MedicamentoController;
import com.gmail.thales_silva_nascimento.alarmmed.model.Medicamento;

import java.util.Calendar;
import java.util.List;


public class filtroHistorico extends DialogFragment {

    private TextView dataInicial;
    private TextView dataFinal;
    private Spinner spinner;
    private Button btnCancelar;
    private Button btnConfirmar;
    private MedicamentoController medController;
    private Calendar dInicial;
    private Calendar dFinal;

    public interface filtroHistoricoListener{
        public void filtroHistoricoListenerPositivo(DialogFragment dialog, long idMedicamento,String dataInicial, String dataFinal);
        public void filtroHistoricoListenerNegativo(DialogFragment dialog);
    }

    //Instância do objeto da interface para a entrega das informacoes a activity host
    filtroHistoricoListener interfaceListener;

    public filtroHistorico(){

    }

    //Sobreescreve o método Fragment.onAttach() para instanciar o objeto interfaceListener
    //O código está sobreescrevendo duas vezes, pois para dispositvos abaixo da API 23 o parâmetro de entrado do método
    //onAttach é uma Activity já para API > 23 utiliza-se um Context.
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Verifica se a API do android instalado no aparelho é maior que a API 22 (Lollipop). Se for return para sair e entrar no onAttach seguinte.
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) return;
        if (activity instanceof filtroHistoricoListener) {
            interfaceListener = (filtroHistoricoListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement diasEspecificosListener");
        }
    }

    //onAttach para API > 22
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof filtroHistoricoListener) {
            interfaceListener = (filtroHistoricoListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement diasEspecificosListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        //LayoutInflater para inflar o layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //View customizada do alertDialog
        View alertView = inflater.inflate(R.layout.dialog_filtro_pedido, null);

        //Cria a controladora do medicamento
        medController = new MedicamentoController(getContext());
        //Lista contendo todos os medicamentos
        List<Medicamento> medicamentos = medController.listarTodosMedicamentos();
        //Gambiarra para aparecer Todos os Medicamentos na primeira linha
        medicamentos.add(0,new Medicamento("Todos os Medicamentos",100,"",true,"",""));
        //Adapter utilizado pelo spinner;
        ArrayAdapter<Medicamento> adapter = new ArrayAdapter<Medicamento>(getContext(), R.layout.support_simple_spinner_dropdown_item, medicamentos);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        //Associa o spineer ao objeto
        spinner = (Spinner) alertView.findViewById(R.id.spinnerFiltro);
        //Vincula o adapter ao spinner
        spinner.setAdapter(adapter);
        spinner.setPrompt("Teste");


        //Inicializa os calendários com a data atual.
        dInicial = Calendar.getInstance();
        dFinal = Calendar.getInstance();

        //Insere a data início e final com a data de hoje.
        dataInicial = (TextView) alertView.findViewById(R.id.dInicial);
        dataFinal  = (TextView)  alertView.findViewById(R.id.dFinal);
        String textoDataIni = Utils.formataDataBrasil(Utils.CalendarToStringData(dInicial));
        String textoDataFim = Utils.formataDataBrasil(Utils.CalendarToStringData(dFinal));
        dataInicial.setText(textoDataIni);
        dataFinal.setText(textoDataFim);

        dataInicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int dia = cal.get(Calendar.DAY_OF_WEEK);
                int mes = cal.get(Calendar.MONTH);
                int ano = cal.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String novaDataIni = Utils.formataDataBrasil(i2,i1,i);
                        dataInicial.setText(novaDataIni);
                    }
                }, ano, mes, dia);

                datePickerDialog.show();
            }
        });

        dataFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int dia = cal.get(Calendar.DAY_OF_WEEK);
                int mes = cal.get(Calendar.MONTH);
                int ano = cal.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String novaDataFim = Utils.formataDataBrasil(i2,i1,i);
                        dataFinal.setText(novaDataFim);
                    }
                }, ano, mes, dia);

                datePickerDialog.show();
            }
        });

        btnCancelar = (Button) alertView.findViewById(R.id.btnDiaEspCancel);
        btnConfirmar = (Button) alertView.findViewById(R.id.btnDiaEspDef);



        alert.setView(alertView);
        return alert.create();

    }
}
