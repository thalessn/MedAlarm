package com.gmail.thales_silva_nascimento.alarmmed;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.controller.HistoricoController;
import com.gmail.thales_silva_nascimento.alarmmed.model.HeaderHistoricoRow;
import com.gmail.thales_silva_nascimento.alarmmed.model.ItemAlarmeHistorico;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPHeaderCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;

public class Email extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView tvDataIni, tvDataFinal;
    private TextInputEditText email;
    private HistoricoController histController;
    private TextInputLayout text;
    private TextInputEditText editEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        //Inicia a controladora
        histController = new HistoricoController(Email.this);

        toolbar = (Toolbar) findViewById(R.id.tbEmail);
        toolbar.setNavigationIcon(R.drawable.ic_menu_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Enviar Relatório de Tratamento");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvDataIni = (TextView) findViewById(R.id.dInicialHis);
        tvDataFinal = (TextView) findViewById(R.id.dFinalHis);

        //Calendário para preencher os textviews dos períodos
        Calendar cal = Calendar.getInstance();
        String dataFinal = Utils.formataDataBrasil(Utils.CalendarToStringData(cal));
        //Retira 30 dias do dia atual
        cal.add(Calendar.DATE, -30);
        String dataInicial = Utils.formataDataBrasil(Utils.CalendarToStringData(cal));

        tvDataIni.setText(dataInicial);
        tvDataFinal.setText(dataFinal);


        tvDataIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int dia = cal.get(Calendar.DAY_OF_WEEK);
                int mes = cal.get(Calendar.MONTH);
                int ano = cal.get(Calendar.YEAR);
                DatePickerDialog date = new DatePickerDialog(Email.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String novaData = Utils.formataDataBrasil(i2,i1,i);
                        tvDataIni.setText(novaData);
                    }
                }, ano, mes, dia);

                date.show();
            }
        });

        tvDataFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int dia = cal.get(Calendar.DAY_OF_WEEK);
                int mes = cal.get(Calendar.MONTH);
                int ano = cal.get(Calendar.YEAR);
                DatePickerDialog date = new DatePickerDialog(Email.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String novaData = Utils.formataDataBrasil(i2,i1,i);
                        tvDataIni.setText(novaData);
                    }
                }, ano, mes, dia);

                date.show();
            }
        });


        //TextInputLayout
        text = (TextInputLayout) findViewById(R.id.inputLayoutEmail);
        //TextInputEdit
        editEmail = (TextInputEditText) findViewById(R.id.email);

        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validaEmail(editable.toString());
            }
        });

        //Mantém o teclado escondido quando a activity inicia. Mantendo o foco no Editext
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Adiciona um menu a ToolBar(ActonBar)
        getMenuInflater().inflate(R.menu.toolbar_enviar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Metodo que gerencia os itens do menu da Toolbar.
        switch (item.getItemId()) {
            case R.id.iTEnviar:
                try {
                    if(validaEmail(editEmail.getText().toString())){
                        createPdf();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void createPdf() throws FileNotFoundException, DocumentException {

        //Busca no Banco o histórico de administração
        List<ListItemHistorico>itens = histController.listarHistoricoPeriodo(Utils.formataDataUTC(tvDataIni.getText().toString()), Utils.formataDataUTC(tvDataFinal.getText().toString()));
        Log.v("TamanhoHistorico", String.valueOf(itens.size()));

        if(itens.size() < 1){
            Toast.makeText(Email.this, "Não há status medicamento para esse período", Toast.LENGTH_SHORT).show();
            return;
        }

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/AlarmMed/");
        if (!docsFolder.exists()) {
            Log.v("TAG", "Created a new directory for PDF");
            docsFolder.mkdir();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String date = dateFormat.format(new Date());

        File pdfFile = new File(docsFolder.getAbsolutePath(),"relatorio"+date+".pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();

        /** Cabecalho */
        Font fontBold = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
        Paragraph p = new Paragraph("Relatório - Status Medicamentos", fontBold);
        document.add(p);

        //Insere texto abaixo de Status
        Calendar dInicial = Utils.DataDiaMesAnoToCalendar(tvDataIni.getText().toString());
        Calendar dFinal = Utils.DataDiaMesAnoToCalendar(tvDataFinal.getText().toString());

        SimpleDateFormat dia = new SimpleDateFormat("d");
        SimpleDateFormat mes = new SimpleDateFormat("MMM");
        SimpleDateFormat ano = new SimpleDateFormat("yyyy");
        String intervalo = mes.format(dInicial.getTime()).toUpperCase()+" "+dia.format(dInicial.getTime())+", "+ano.format(dInicial.getTime())
                +" - "+mes.format(dFinal.getTime()).toUpperCase()+" "+dia.format(dFinal.getTime())
                +", "+ano.format(dFinal.getTime());
        Paragraph pp = new Paragraph(intervalo);
        document.add(pp);
        //Adiciona linha em branco
        document.add(Chunk.NEWLINE);


        /** Cria a tabela principal*/
        PdfPTable table = new PdfPTable(1);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(100);


        //Preenche a Tabela
        for(int i=0; i<itens.size(); i++){
            if(itens.get(i) instanceof HeaderHistoricoRow){
                //Cast para o objeto HearHistoricoRow
                HeaderHistoricoRow header = (HeaderHistoricoRow) itens.get(i);
                //Cria a celular Header
                PdfPCell cell = new PdfPCell();
                //Retira as bordas
                cell.setBorder(Rectangle.NO_BORDER);
                //Define a cor do fundo da célula
                cell.setBackgroundColor(new BaseColor(176,224,230));
                //Seta uma altura mínima
                cell.setMinimumHeight(30);
                //Seta o alinhamento vertical centralizado
                cell.setVerticalAlignment(Element.ALIGN_CENTER);
                //Cria uma fonte para deixar o texto em negrito
                Font bold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
                //Cria o objeto paragrafo que será inserido na celula da tabela
                Paragraph data = new Paragraph(header.toString(), bold);
                //Seta o texto com alinhamento centralizado
                data.setAlignment(Element.ALIGN_CENTER);
                //Adiciona o paragrafo na célula
                cell.addElement(data);
                //Adiciona a celula na tabela
                table.addCell(cell);
            }else if(itens.get(i) instanceof ItemAlarmeHistorico){
                //Cria a celula contendo as informações do historico
                PdfPCell add = criaLinhaItemHistorico((ItemAlarmeHistorico) itens.get(i));
                table.addCell(add);
            }
        }

        //Adiciona a tabela no documento.
        document.add(table);
        document.close();

        Intent email = new Intent(Intent.ACTION_SEND);
        String [] destinatario = {editEmail.getText().toString()};
        email.setData(Uri.parse("mailto:")); // only email apps should handle this
        email.setType("text/plain");
        email.putExtra(Intent.EXTRA_SUBJECT,"Relatório - Status Tratamento");
        email.putExtra(Intent.EXTRA_EMAIL,destinatario);
        email.putExtra(Intent.EXTRA_TEXT, "Relatório de Status de Medicamentos\n"+intervalo+"\n\nSegue em anexo o relatório de Status do tratamento do(s) remédio(s).\n\n\n\nRelatório gerado por AlarmMed");
        Uri uri = Uri.fromFile(pdfFile);
        email.putExtra(Intent.EXTRA_STREAM, uri);

        if (email.resolveActivity(getPackageManager()) != null) {
            startActivity(email);
        }

    }

    public PdfPCell criaLinhaItemHistorico(ItemAlarmeHistorico item){

        //Tabela contendo as informações do medicamento
        float[] columnWidths = {1, 5, 5, 5};
        PdfPTable nestedTable = new PdfPTable(columnWidths);

        //Linha contendo espaco em branco no inicio da linha
        PdfPCell cell1 = new PdfPCell(new Phrase(""));
        //Define que a celula não terá borde no contorno.
        cell1.setBorder(Rectangle.NO_BORDER);
        cell1.setMinimumHeight(30);

        //Nome do medicamento
        PdfPCell cell2 = new PdfPCell();
        cell2.setBorder(Rectangle.NO_BORDER);
        cell2.setMinimumHeight(30);
        cell2.setVerticalAlignment(Element.ALIGN_CENTER);
        cell2.addElement(new Phrase(item.getMed().getNome()));

        //Texto: Programado para 15:43
        String prog = "Programado para "+item.getHorario().getHorario();
        PdfPCell cell3 = new PdfPCell();
        cell3.setBorder(Rectangle.NO_BORDER);
        cell3.setMinimumHeight(30);
        cell3.setVerticalAlignment(Element.ALIGN_CENTER);
        cell3.addElement(new Phrase(prog));

        //Cria o texto "Tomado às 12:00 ou Pulou às
        String texto = "";
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12);
        if(item.getStatus().equals(ItemAlarmeHistorico.STATUS_TOMADO)){
            font.setColor(new BaseColor(0,128,0));
            String tomado = "Tomado às "+item.getHoraAdministrado();
            texto+=tomado;
            Log.v("Entrou", "TOMOU");
        }else if(item.getStatus().equals(ItemAlarmeHistorico.STATUS_PULOU)){
            font.setColor(BaseColor.RED);
            String pulo = "Pulou às "+item.getHoraAdministrado();
            texto+=pulo;
            Log.v("Entrou", "Pulou");
        }

        PdfPCell cell4 = new PdfPCell();
        cell4.setBorder(Rectangle.NO_BORDER);
        cell4.setMinimumHeight(30);
        cell4.setVerticalAlignment(Element.ALIGN_CENTER);
        cell4.addElement(new Phrase(texto, font));

        //Adiciona as células na tabela
        nestedTable.addCell(cell1);
        nestedTable.addCell(cell2);
        nestedTable.addCell(cell3);
        nestedTable.addCell(cell4);


        //Adiciona a tabela aninhada com a principal. Adiciona a tabela dentro de uma celula para
        //não ahver bordas.
        PdfPCell cell5 = new PdfPCell(nestedTable);
        cell5.setBorder(Rectangle.NO_BORDER);

        return cell5;
    }

    public Boolean validaEmail(String email){
        if(!(email.isEmpty()) && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            text.setErrorEnabled(false);
            return true;
        }else {
            text.setErrorEnabled(true);
            text.setError("E-mail incorreto");
            return  false;
        }
    }

    /**
     * Finaliza a activity caso o usuário abra um aplicativo de email.
     * Volta para a activity Histórico
     */
    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
