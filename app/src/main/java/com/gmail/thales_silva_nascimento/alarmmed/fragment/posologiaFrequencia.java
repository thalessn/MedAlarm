package com.gmail.thales_silva_nascimento.alarmmed.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by thales on 16/01/17.
 */

public class posologiaFrequencia extends DialogFragment implements AdapterView.OnItemSelectedListener{

    private Spinner spinner;
    private ArrayAdapter<String> dataAdapter;
    private List<String> repeticao;
    private boolean spinnerHourController = true;
    private boolean spinnerIntervalController = true;
    private ArrayList<TextView> tvHorarios;
    private Button btnCancelar;
    private Button btnSalvar;

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

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_posologia_frequencia, container, false);

        btnCancelar = (Button) rootView.findViewById(R.id.btnPosFreqCancelar);
        btnSalvar = (Button) rootView.findViewById(R.id.btnPosFreqSalvar);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Apertou no botão salvar", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        //Inicializa a varialvel ArrayList intervalosTextViews
        tvHorarios = new ArrayList<>();

        //Popula o Spinner
        spinner = (Spinner) rootView.findViewById(R.id.posFreqSpinner);
        repeticao = new ArrayList<>();
        repeticao.add("Uma vez ao dia");
        repeticao.add("Duas vezes ao dia");
        repeticao.add("3 vezes ao dia");
        repeticao.add(" ... ");
        repeticao.add("Intervalos");
        repeticao.add("A cada 12 horas");
        repeticao.add("A cada 8 horas");
        repeticao.add("A cada 6 horas");
        repeticao.add(" ... ");


        dataAdapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_item, repeticao);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        //Adiciona o oclickItemSelected
        spinner.setOnItemSelectedListener(this);


        return rootView;

    }



    private int setHourFrequencyTextView(int qtd) {
        if (qtd != 0) {
            if (qtd == 1) {
                return 0;
            }
            //Hora Inicial
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, 8);
            c.set(Calendar.MINUTE, 0);
            //Hora final
            Calendar d = Calendar.getInstance();
            d.set(Calendar.HOUR_OF_DAY, 23);
            d.set(Calendar.MINUTE, 0);
            //Calcula a quantidade de minutos entre a hora final e a hora inicial em milisegundos
            long td = d.getTimeInMillis() - c.getTimeInMillis();
            //Intervalo de tempo que será acrescentado na hora inicial para dividir o intervalo de tempo (td) de forma igual
            int result = (int) ((((td / (qtd - 1)) / 1000) / 60));
            //Retorna o intervalo
            return result;
        }
        return 0;
    }

    private void criaTimeTextView(int qtd) {
        //Pego a referência do elemento onde irei inserir novas views
        LinearLayout LL = (LinearLayout) getView().findViewById(R.id.posHorarioLL);
        //Crio uma instancia do objeto que recebe o método onclick listener para as Views criadas dinamicamente
        //Desse jeito posso adicionar um "Onclicklistener" somente nas devidas views criadas dinamicamente
        //Neste caso para cada TextView criada abaixo eu adiciono este método
        View.OnClickListener Otm = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int id = v.getId(); //Define final para poder passar como argumento
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int min = c.get(Calendar.MINUTE);
                final View vClick = v; //Define uma view do tipo final para passar como argumento
                //Cria o TimePicker e já adiciona o evento de click
                TimePickerDialog time = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hora = hourOfDay < 10 ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                        String minuto = minute < 10 ? ("0" + String.valueOf(minute)) : String.valueOf(minute);
                        TextView horario = (TextView) vClick;
                        horario.setText(hora + " : " + minuto);
                    }
                }, hour, min, true);
                time.show();
            }
        };

        int i;
        //Remove todos os componetes já adicionados na view
        LL.removeAllViews();
        //Remove os elementos do arraylist de text view
        tvHorarios.clear();


        //Calcula com o parametro qtd o intervalo entre as horas
        int minutosAdd = setHourFrequencyTextView(qtd);
        //Variável que armazena a hora inicial no caso (08:00) para ser utilizada para o incremento do tempo no "for" abaixo
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 8);
        c.set(Calendar.MINUTE, 0);
        String hora;
        String minuto;
        for (i = 0; i < qtd; i++) {
            TextView t = new TextView(getView().getContext());
            t.setId(i + 1);
            //Tamanho do Texto pela varialvel "sp" (TypeValue.Complex_unit_SP)
            t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            //Adiciona o método OnClickListener
            t.setOnClickListener(Otm);
            //Objeto necessário para adicionar margin ao texto. (Usou-se Linear Layout pois é a view parent da TextView)
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 10, 0, 0);
            lp.gravity = Gravity.CENTER;
            //Adiciona a margin ao textView
            t.setLayoutParams(lp);
            //hora e minuto - Variaveis do tipo String que recebem o valor da hora e do  minuto respectivamento do horario de tratamento (c.Calendar)
            hora = (c.get(Calendar.HOUR_OF_DAY)) < 10 ? ("0" + String.valueOf(c.get(Calendar.HOUR_OF_DAY))) : String.valueOf(c.get(Calendar.HOUR_OF_DAY));
            minuto = (c.get(Calendar.MINUTE)) < 10 ? ("0" + String.valueOf(c.get(Calendar.MINUTE))) : String.valueOf(c.get(Calendar.MINUTE));
            //Adiciona o texto
            t.setText(hora + " : " + minuto);
            //Adiciona o TextView no LinearLayout
            LL.addView(t);
            //Adiciona aou arraylist de textviews
            tvHorarios.add(t);
            //Incrementa o horário do tratamento
            c.add(Calendar.MINUTE, minutosAdd);
        }
    }



    //Função que monitora quando o usuário clicar no spinner na opção "..." e adicionar mais escolhas.
    //Recebe como parâmetro de entrada a posição da escolha feita no spinner
    private void spinnerControllerOptions(int id) {
        if (id == 3) {
            repeticao.remove(3);
            String[] freq = {"4 vezes ao dia", "5 vezes ao dia", "6 vezes ao dia", "7 vezes ao dia", "8 vezes ao dia", "9 vezes ao dia", "10 vezes ao dia", "11 vezes ao dia", "12 vezes ao dia"};
            int i = 0;
            int b;
            for (b = 0; b < freq.length; b++) {
                dataAdapter.insert(freq[b], (id + i));
                i += 1;
            }
            dataAdapter.notifyDataSetChanged();
            spinner.setSelection(0);
            spinner.performClick();
        }
        if (id == 8 || id == 16) {
            id = (id == 8 ? 8 : 16);
            repeticao.remove(id);
            String[] inter = {"A cada 4 horas", "A cada 3 horas", "A cada 2 horas", "Por hora"};
            int ii = 0;
            for (int bb = 0; bb < inter.length; bb++) {
                dataAdapter.insert(inter[bb], (id + ii));
                ii += 1;
            }
            dataAdapter.notifyDataSetChanged();
            spinner.setSelection(0);
            spinner.performClick();
        }
    }

    //Função para recalcular o intervalo quando o usuário define um nova hora inicial.
    private void recalculaIntervaloHoras(int opcao, int hNew, int mNew){
        //Crio o calendário que receberá a nova hora inicial definida pelo usuário
        Calendar novo = Calendar.getInstance();
        novo.set(Calendar.HOUR_OF_DAY, hNew);
        novo.set(Calendar.MINUTE, mNew);
        //Acrescento zero se for necessário a hora e ao minuto
        String hour = hNew < 10 ? ("0" + String.valueOf(hNew)) : String.valueOf(hNew);
        String minute = mNew < 10 ? ("0" + String.valueOf(mNew)) : String.valueOf(mNew);
        //Percorro todos os TextViews alterando para as novas horas.
        for(int i = 0; i < (tvHorarios.size()); i++){
            TextView v = tvHorarios.get(i);
            v.setText(hour + " : " + minute);
            novo.add(Calendar.HOUR_OF_DAY, opcao);
            hour = novo.get(Calendar.HOUR_OF_DAY) < 10 ? ("0" + String.valueOf(novo.get(Calendar.HOUR_OF_DAY))) : String.valueOf(novo.get(Calendar.HOUR_OF_DAY));
            minute = novo.get(Calendar.MINUTE) < 10 ? ("0" + String.valueOf(novo.get(Calendar.MINUTE))) : String.valueOf(novo.get(Calendar.MINUTE));
        }

    }


    //Função para recalcular o intervalo quando o usuário define um nova hora final.
    private void recalculaIntervaloHorasFinal(int opcao, int horaFinalNew, int minutoFinalNew){
        //Calendário com a hora inicial
        Calendar hInicio = Calendar.getInstance();
        String[] horarioInicial = tvHorarios.get(0).getText().toString().split(" : ");
        hInicio.set(Calendar.HOUR_OF_DAY, Integer.parseInt(horarioInicial[0]));
        hInicio.set(Calendar.MINUTE, Integer.parseInt(horarioInicial[1]));

        //Calendário com a Hora final
        Calendar hfinal = Calendar.getInstance();
        hfinal.set(Calendar.HOUR_OF_DAY, horaFinalNew);
        hfinal.set(Calendar.MINUTE, minutoFinalNew);

        //Verifica se a hora final está entre 00:00 e  08:00
        //Se verdadeiro adiciona um dia ao calendário para se considerado um novo dia e não haver um intervalo negativo.
        if(hfinal.before(hInicio)){
            hfinal.add(Calendar.DATE, 1);
        }

        //Duração do tempo em milisegundo da diferença entre a Hora final e a Inicial
        long duracao = hfinal.getTimeInMillis() - hInicio.getTimeInMillis();
        //O intervalo em horas da duração do intervalo entre as horas final e inicial.
        int intervalo = (int) (((duracao/1000)/60)/60);
        //Quantidade de vezes que o intervalo será divido em relação a "opcao" que o usuário escolheu.
        //Se caso o intervalo não de para dividir pela opcao, então é setado pelo menos um intervalo.
        int qtdIntervalo = intervalo/opcao == 0? 1 : (intervalo/opcao);

        //Pega a referência do Layout onde o TextView será inserido
        LinearLayout LL = (LinearLayout) getView().findViewById(R.id.posHorarioLL);
        //"For" para retirar os TextView do layout e retirar do Arraylist, menos o primeiro.
        for(int i = tvHorarios.size()-1; i > 0; i--){
            LL.removeView(tvHorarios.get(i));
            tvHorarios.remove(i);
        }

        //String utilizadas para formatar as horas
        String hora, minuto;
        //Cria os TextView do novo intervalo.
        for(int b = 0; b < qtdIntervalo; b++){
            TextView add = new TextView(getView().getContext());
            add.setId(21+b);
            //Determina o tamanho do texto
            add.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            //Objeto necessário para adicionar margin ao texto. (Usou-se Linear Layout pois é a view parent da TextView)
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            lp.setMargins(0, 10, 0, 0);
            //Adiciona a margin ao textView
            add.setLayoutParams(lp);
            //Adiciona o Intervalo ao horário
            hInicio.add(Calendar.HOUR_OF_DAY, (opcao));
            //hora e minuto - Variaveis do tipo String que recebem o valor da hora e do  minuto respectivamento do horario de tratamento (c.Calendar)
            hora = (hInicio.get(Calendar.HOUR_OF_DAY)) < 10 ? ("0" + String.valueOf(hInicio.get(Calendar.HOUR_OF_DAY))) : String.valueOf(hInicio.get(Calendar.HOUR_OF_DAY));
            minuto = (hInicio.get(Calendar.MINUTE)) < 10 ? ("0" + String.valueOf(hInicio.get(Calendar.MINUTE))) : String.valueOf(hInicio.get(Calendar.MINUTE));
            //Adiciona o texto
            add.setText(hora + " : " + minuto);
            LL.addView(add);
            tvHorarios.add(add);
        }
        //Adiciona o onclick ao novo "último" TextView do Layout.
        tvHorarios.get(tvHorarios.size()-1).setOnClickListener(OnClinkRecalculaIntervalFinal(opcao));

    }

    //Função que retona um objeto do tipo OnclickListerner.
    //É Utilizada para adicionar um evento ao último textView quando o usuário seleciona "A cada h horas" que recalcula o intervalo de acordo com a hora final selecionada
    private View.OnClickListener OnClinkRecalculaIntervalFinal(int op){
        //Variavel criada para passar a opcao escolhida pelo usuário
        final int opcao = op;
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pega o horário final (último TextView) do intervalo para passar de referência ao TimerPicker para o usuário escolher
                String[] horario = tvHorarios.get(tvHorarios.size()-1).getText().toString().split(" : ");
                int horaAtual = Integer.parseInt(horario[0]);
                int minutoAtual = Integer.parseInt(horario[1]);
                TimePickerDialog time = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        recalculaIntervaloHorasFinal(opcao, hourOfDay, minute);
                    }
                }, horaAtual, minutoAtual, true);
                time.show();
            }
        };
        return click;
    }


    //Recebe como parametro intervaloHora. Corresponde ao intervalo de hora que o usuário escolheu no spinner, por exemplo "A cada 12 horas", então 'intervaloHora' possui o valor 12
    private View.OnClickListener OnClickIntervalTextView(int intervaloHora){
        //Variavel que passa a opcao escolhda pelo usuário
        final int opcao = intervaloHora;
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Crio um calendário para pegar o horário que está atualmente na view
                Calendar c = Calendar.getInstance();
                TextView horaTela = (TextView) v.findViewById(v.getId());
                String texto = horaTela.getText().toString();
                String [] resultado = texto.split(" : ");
                final View horaInterval = v;
                //Adiciona a hora ao calendário
                c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(resultado[0]));
                c.set(Calendar.MINUTE, Integer.parseInt(resultado[1]));
                //Crio o timepicker para o usuário selecionar a nova hora inicial do tratamento
                TimePickerDialog time = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Pego a referência do TextView para setar a novo hora Inicial
                        TextView horaInicioNova = (TextView) horaInterval.findViewById(horaInterval.getId());
                        String h = hourOfDay < 10 ? ("0" + String.valueOf(hourOfDay)) : String.valueOf(hourOfDay);
                        String m = minute < 10 ? ("0" + String.valueOf(minute)) : String.valueOf(minute);
                        //Escreve a nova hora inicial no TextView
                        horaInicioNova.setText(h + " : " + m);
                        //Função que recalcula os intevalos em relação a  nova hora inicial
                        recalculaIntervaloHoras(opcao, hourOfDay, minute);
                    }
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
                time.show();
            }
        };
        return click;
    }

    //Função para criar os intervalo "A cada h horas".
    private void criaIntervalosTextView(int opcao){
        Calendar c = Calendar.getInstance();
        c.set((Calendar.HOUR_OF_DAY), 8);
        c.set(Calendar.MINUTE, 0);
        String hora, minuto;

        //Pego a referência do elemento onde irei inserir novas views
        LinearLayout LL = (LinearLayout) getView().findViewById(R.id.posHorarioLL);
        //Remove todos os componetes já adicionados na view
        LL.removeAllViews();
        //Limpa o ArrayList contendo os TextViews. Isso é utilizado pois quando o usuário fica trocando de opção de intervalos, o ArrayList não ficar desatualizado.
        tvHorarios.clear();
        for(int i = 0; i < (24/opcao); i++){
            //Cria o TextView que receberá a hora
            TextView v  = new TextView(getView().getContext());
            //Adiciona a id ao objeto
            v.setId(20 + i);
            //Determina o tamanho do texto
            v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            //Seta o onclick listener ao objeto. Neste caso A funcao OnClickIntervalTextView determina o método.
            //v.setOnClickListener();
            //Objeto necessário para adicionar margin ao texto. (Usou-se Linear Layout pois é a view parent da TextView)
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            lp.setMargins(0, 10, 0, 0);
            //Adiciona a margin ao textView
            v.setLayoutParams(lp);
            //hora e minuto - Variaveis do tipo String que recebem o valor da hora e do  minuto respectivamento do horario de tratamento (c.Calendar)
            hora = (c.get(Calendar.HOUR_OF_DAY)) < 10 ? ("0" + String.valueOf(c.get(Calendar.HOUR_OF_DAY))) : String.valueOf(c.get(Calendar.HOUR_OF_DAY));
            minuto = (c.get(Calendar.MINUTE)) < 10 ? ("0" + String.valueOf(c.get(Calendar.MINUTE))) : String.valueOf(c.get(Calendar.MINUTE));
            //Adiciona o texto
            v.setText(hora + " : " + minuto);
            //Adiciona o TextView ao Arraylist tvHorarios
            tvHorarios.add(v);
            //Adiciona o TextView no LinearLayout
            LL.addView(v);
            c.add(Calendar.HOUR_OF_DAY, opcao);
        }
        //Adiciona o metodo OnclickListener no Horario inicial
        //TextView horarioInicial = (TextView) findViewById(20);
        //horarioInicial.setOnClickListener(OnClickIntervalTextView(opcao));
        tvHorarios.get(0).setOnClickListener(OnClickIntervalTextView(opcao));
        //Adiciona OnClickEvent no último TextView do ArrayList
        tvHorarios.get(tvHorarios.size()-1).setOnClickListener(OnClinkRecalculaIntervalFinal(opcao));

    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                criaTimeTextView(1);
                break;
            case 1:
                criaTimeTextView(2);
                break;
            case 2:
                criaTimeTextView(3);
                break;
            case 3:
                if (spinnerHourController) {
                    spinnerHourController = false;
                    spinnerControllerOptions(position);
                    break;
                } else if ((spinnerHourController == false)) {
                    criaTimeTextView(4);
                    break;
                }
            case 4:
                if (spinnerHourController) {
                    Toast.makeText(view.getContext(), "Intervalo", Toast.LENGTH_SHORT).show();
                    break;
                } else if ((spinnerHourController == false)) {
                    criaTimeTextView(5);
                    break;
                }
            case 5:
                if ((spinnerHourController == false)) {
                    criaTimeTextView(6);
                    break;
                } else if (spinnerHourController) {
                    criaIntervalosTextView(12);
                    Toast.makeText(view.getContext(), "A cada 12 horas", Toast.LENGTH_SHORT).show();
                    break;
                }
            case 6:
                if ((spinnerHourController == false)) {
                    criaTimeTextView(7);
                    break;
                } else if (spinnerHourController) {
                    criaIntervalosTextView(8);
                    Toast.makeText(view.getContext(), "A cada 8 horas", Toast.LENGTH_SHORT).show();
                    break;
                }
            case 7:
                if ((spinnerHourController == false)) {
                    criaTimeTextView(8);
                    break;
                } else if (spinnerHourController) {
                    criaIntervalosTextView(6);
                    Toast.makeText(view.getContext(), "A cada 6 horas", Toast.LENGTH_SHORT).show();
                    break;
                }
            case 8:
                if ((spinnerHourController == false)) {
                    criaTimeTextView(9);
                    break;
                } else if (spinnerIntervalController) {
                    spinnerIntervalController = false;
                    spinnerControllerOptions(position);
                    break;
                } else if (spinnerHourController && (spinnerIntervalController == false)) {
                    criaIntervalosTextView(4);
                    Toast.makeText(view.getContext(), "A cada 4 horas", Toast.LENGTH_SHORT).show();
                    break;
                }
            case 9:
                if ((spinnerHourController == false)) {
                    criaTimeTextView(10);
                    break;
                } else if (spinnerHourController && (spinnerIntervalController == false)) {
                    criaIntervalosTextView(3);
                    Toast.makeText(view.getContext(), "A cada 3 horas", Toast.LENGTH_SHORT).show();
                    break;
                }
            case 10:
                if ((spinnerHourController == false)) {
                    criaTimeTextView(11);
                    break;
                } else if (spinnerHourController && (spinnerIntervalController == false)) {
                    criaIntervalosTextView(2);
                    Toast.makeText(view.getContext(), "A cada 2 horas", Toast.LENGTH_SHORT).show();
                    break;
                }
            case 11:
                if ((spinnerHourController == false)) {
                    criaTimeTextView(12);
                    break;
                } else if (spinnerHourController && (spinnerIntervalController == false)) {
                    criaIntervalosTextView(1);
                    Toast.makeText(view.getContext(), "Por hora", Toast.LENGTH_SHORT).show();
                    break;
                }
            case 12:
                if (spinnerHourController == false) {
                    Toast.makeText(view.getContext(), "Intervalo 12", Toast.LENGTH_SHORT).show();
                    break;
                }
            case 13:
                criaIntervalosTextView(12);
                Toast.makeText(view.getContext(), " A cada 12 horas", Toast.LENGTH_SHORT).show();
                break;
            case 14:
                criaIntervalosTextView(8);
                Toast.makeText(view.getContext(), "A cada 8 horas", Toast.LENGTH_SHORT).show();
                break;
            case 15:
                criaIntervalosTextView(6);
                Toast.makeText(view.getContext(), "A cada 6", Toast.LENGTH_SHORT).show();
                break;
            case 16:
                if (spinnerIntervalController) {
                    spinnerIntervalController = false;
                    spinnerControllerOptions(position);
                    break;
                } else {
                    criaIntervalosTextView(4);
                    Toast.makeText(view.getContext(), "A cada 4 horas", Toast.LENGTH_SHORT).show();
                    break;
                }
            case 17:
                criaIntervalosTextView(3);
                Toast.makeText(view.getContext(), "A cada 3 horas", Toast.LENGTH_SHORT).show();
                break;
            case 18:
                criaIntervalosTextView(2);
                Toast.makeText(view.getContext(), "A cada 2 horas", Toast.LENGTH_SHORT).show();
                break;
            case 19:
                criaIntervalosTextView(1);
                Toast.makeText(view.getContext(), "Por hora", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
