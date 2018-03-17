package com.gmail.thales_silva_nascimento.alarmmed;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.thales_silva_nascimento.alarmmed.controller.HistoricoController;
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
                Toast.makeText(Email.this, "Enviar", Toast.LENGTH_SHORT).show();
                try {
                    createPdf();
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

    public void gerarRelatorio() {
        try {
            //Create file path for Pdf
            String fpath = "/sdcard/" + "thales" + ".pdf";
            File file = new File(fpath);
            if (!file.exists()) {
                file.createNewFile();
            }
            // To customise the text of the pdf
            // we can use FontFamily
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN,
                    12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN,
                    12);
            // create an instance of itext document
            Document document = new Document();
            PdfWriter.getInstance(document,
                    new FileOutputStream(file.getAbsoluteFile()));
            document.open();
            //using add method in document to insert a paragraph
            document.add(new Paragraph("My First Pdf !"));
            document.add(new Paragraph("Hello World"));
            // close document
            document.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);

        } catch (IOException e) {
            e.printStackTrace();

        } catch (DocumentException e) {
            e.printStackTrace();

        }




    }

    private void createPdf() throws FileNotFoundException, DocumentException {

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        if (!docsFolder.exists()) {
            docsFolder.mkdir();
            Log.v("TAG", "Created a new directory for PDF");
        }

        File pdfFile = new File(docsFolder.getAbsolutePath(),"HelloWorld.pdf");
        OutputStream output = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();

        /** Cabecalho */
        Font fontBold = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        Paragraph p = new Paragraph("Relatório - Status Medicamentos", fontBold);
        document.add(p);
        Paragraph pp = new Paragraph("Fevereiro 13, 2018 - Março 13, 2018");
        document.add(pp);
        //Adiciona linha em branco
        document.add(Chunk.NEWLINE);


        /** Cria a tabela principal*/
        PdfPTable table = new PdfPTable(1);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.setWidthPercentage(100);
        //Celula header contendo a data.
        PdfPCell cell = new PdfPCell(new Phrase("25/03/2018"));
        //Adiciona a celula na tabela
        table.addCell(cell);


        
        //Linha em branco
        PdfPCell linebranco = new PdfPCell(new Phrase(" "));
        linebranco.setColspan(4);
        linebranco.setBorder(Rectangle.NO_BORDER);
        linebranco.setFixedHeight(5f);

//        //Tabela contendo as informações do medicamento
//        float[] columnWidths = {1, 5, 5, 5};
//        PdfPTable nestedTable = new PdfPTable(columnWidths);

//        //Linha contendo espaco em branco no inicio da linha
//        PdfPCell cell1 = new PdfPCell(new Phrase(""));
//        //Define que a celula não terá borde no contorno.
//        cell1.setBorder(Rectangle.NO_BORDER);
//        //Nome do medicamento
//        PdfPCell cell2 = new PdfPCell(new Phrase("Nome do Medicamento"));
//        cell2.setBorder(Rectangle.NO_BORDER);
//        //Programado para
//        PdfPCell cell3 = new PdfPCell(new Phrase("Programado para 15:43"));
//        cell3.setBorder(Rectangle.NO_BORDER);
//        //Tomado ou Pulou as
//        PdfPCell cell4 = new PdfPCell(new Phrase("Tomado as 16:00"));
//        cell4.setBorder(Rectangle.NO_BORDER);
//        //Adicionando as celulas na tabele adaptada(Nested)
//        nestedTable.addCell(linebranco);
//        nestedTable.addCell(cell1);
//        nestedTable.addCell(cell2);
//        nestedTable.addCell(cell3);
//        nestedTable.addCell(cell4);

        List<ListItemHistorico>itens = histController.listarHistoricoPeriodo("01-01-2018", "15-03-2018");
        Log.v("Tamanho", String.valueOf(itens.size()));
//        //Adiciona a tabela aninhada com a principal.
//        PdfPCell cell5 = new PdfPCell(nestedTable);
//        cell5.setBorder(Rectangle.NO_BORDER);
//        table.addCell(cell5);
//        PdfPCell cell12 = new PdfPCell(new Phrase("24/03/2018"));
//        table.addCell(cell12);

        for(int i=0; i<itens.size(); i++){
            if(itens.get(i) instanceof ItemAlarmeHistorico){
                PdfPCell add = criaLinhaItemHistorico((ItemAlarmeHistorico) itens.get(i));
                table.addCell(add);
            }
        }
        //Adiciona a tabela no documento.
        document.add(table);
        document.close();

//        Intent email = new Intent(Intent.ACTION_SEND);
//        email.putExtra(Intent.EXTRA_SUBJECT,"Testando o iText");
//        email.putExtra(Intent.EXTRA_TEXT, "Segue em aenxo");
//        Uri uri = Uri.parse(pdfFile.getAbsolutePath());
//        email.putExtra(Intent.EXTRA_STREAM, uri);
//        email.setType("message/rfc822");
//        startActivity(email);
    }

    public PdfPCell criaLinhaItemHistorico(ItemAlarmeHistorico item){

        //Tabela contendo as informações do medicamento
        float[] columnWidths = {1, 5, 5, 5};
        PdfPTable nestedTable = new PdfPTable(columnWidths);

        //Linha contendo espaco em branco no inicio da linha
        PdfPCell cell1 = new PdfPCell(new Phrase(""));

        //Define que a celula não terá borde no contorno.
        cell1.setBorder(Rectangle.NO_BORDER);

        //Nome do medicamento
        PdfPCell cell2 = new PdfPCell(new Phrase(item.getMed().getNome()));
        cell2.setBorder(Rectangle.NO_BORDER);

        //Texto: Programado para 15:43
        String prog = "Programado para "+item.getHorario().getHorario();
        PdfPCell cell3 = new PdfPCell(new Phrase(prog));
        cell3.setBorder(Rectangle.NO_BORDER);

        //Cria o texto "Tomado às 12:00 ou Pulou às
        String texto = "";
        Font font = new Font(Font.FontFamily.TIMES_ROMAN, 12);
        if(item.getStatus().equals(ItemAlarmeHistorico.STATUS_TOMADO)){
            font.setColor(BaseColor.GREEN);
            String tomado = "Tomado às "+item.getHoraAdministrado();
            texto+=tomado;
        }else if(item.getStatus().equals(ItemAlarmeHistorico.STATUS_PULOU)){
            font.setColor(BaseColor.RED);
            String pulo = "Pulou às "+item.getHoraAdministrado();
            texto+=pulo;
        }

        PdfPCell cell4 = new PdfPCell(new Phrase(texto, font));
        cell4.setBorder(Rectangle.NO_BORDER);

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
}
