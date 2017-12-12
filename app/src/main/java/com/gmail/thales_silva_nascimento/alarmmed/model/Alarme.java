package com.gmail.thales_silva_nascimento.alarmmed.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gmail.thales_silva_nascimento.alarmmed.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by thales on 06/09/17.
 */

public class Alarme implements Parcelable {

    /**
     *  Inicia o Alarme com uma id inválida enquanto não foi salvo no banco
     */
    public static final long ID_INVALIDA = -1;

    /**
     * Variáveis para o tipo de repetição
     */
    public static final int REP_DIASDASEMANA = 1;
    public static final int REP_DIASINTERVALOS = 2;
    public static final int REP_TODO_DIA = 3;


    private long id;
    private Calendar dataInicio;
    private Calendar dataFim;
    private int periodo;
    private int tipoRepeticao;
    private int intervalo;
    private long idMedicamento;
    private boolean status;
    private String freqHorario;
    private String freqDias;
    private List<AlarmeInfo> alarmeInfo;

    public Alarme(Calendar dataInicio, Calendar dataFim, int periodo, int tipoRepetição, int intervalo, boolean status, long idMedicamento, String freqHorario, String freqDias){
        this.id = ID_INVALIDA;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.periodo = periodo;
        this.tipoRepeticao = tipoRepetição;
        this.intervalo = intervalo;
        this.status = status;
        this.idMedicamento = idMedicamento;
        this.freqHorario = freqHorario;
        this.freqDias = freqDias;
        this.alarmeInfo = new ArrayList<>();
    }

    public Alarme(long id, Calendar dataInicio, Calendar dataFim, int periodo, int tipoRepetição, int intervalo,  boolean status, long idMedicamento, String freqHorario, String freqDias){
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.periodo = periodo;
        this.tipoRepeticao = tipoRepetição;
        this.intervalo = intervalo;
        this.status = status;
        this.idMedicamento = idMedicamento;
        this.freqHorario = freqHorario;
        this.freqDias = freqDias;
        this.alarmeInfo = new ArrayList<>();
    }

    public void setId(long Id){
        this.id = Id;
    }

    public long getId(){
        return id;
    }

    public void setDataInicio(Calendar data){
        this.dataInicio = data;
    }

    public Calendar getDataInicio(){

        return dataInicio;
    }


    public Calendar getDataFim(){
        return dataFim;
    }

    public int getPeriodo(){
        return periodo;
    }

    public long getIdMedicamento(){
        return idMedicamento;
    }

    public int getTipoRepeticao(){
        return tipoRepeticao;
    }

    public int getIntervaloRepeticao(){
        return intervalo;
    }

    public String getFreqDias(){
        return this.freqDias;
    }

    public boolean isStatus() {
        return status;
    }

    public List<AlarmeInfo> getAlarmeInfo() {
        return alarmeInfo;
    }

    public void setAlarmeInfo(List<AlarmeInfo> alarmeInfo) {
        this.alarmeInfo = alarmeInfo;
    }

    @Override
    public String toString() {
        return "ID = " +String.valueOf(id)+ " Periodo = " + String.valueOf(periodo) +" TipoRepeticao = " + String.valueOf(tipoRepeticao)+
                " - DataInicio:  " + Utils.CalendarToStringData(dataInicio) + " - DataFim: " + Utils.CalendarToStringData(dataFim)+
                " - Intervalo: " +String.valueOf(intervalo) +" - idMedicamento: " +String.valueOf(idMedicamento) +" - Status: "+ String.valueOf(status)+
                " - FreqHorario: " + freqHorario +", AlarmeInfo: " +alarmeInfo.toString();
    }

    public void setStatus(boolean status){
        this.status = status;
    }

    public String getFreqHorario(){
        return freqHorario;
    }

    public void setIdMedicamento(long idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public void setDataFim(Calendar dataFim) {
        this.dataFim = dataFim;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public void setTipoRepeticao(int tipoRepeticao) {
        this.tipoRepeticao = tipoRepeticao;
    }

    public void setIntervalo(int intervalo) {
        this.intervalo = intervalo;
    }

    public void setFreqHorario(String freqHorario) {
        this.freqHorario = freqHorario;
    }

    public void setFreqDias(String freqDias) {
        this.freqDias = freqDias;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeSerializable(this.dataInicio);
        dest.writeSerializable(this.dataFim);
        dest.writeInt(this.periodo);
        dest.writeInt(this.tipoRepeticao);
        dest.writeInt(this.intervalo);
        dest.writeLong(this.idMedicamento);
        dest.writeByte(this.status ? (byte) 1 : (byte) 0);
        dest.writeString(this.freqHorario);
        dest.writeString(this.freqDias);
        dest.writeTypedList(this.alarmeInfo);
    }

    protected Alarme(Parcel in) {
        this.id = in.readLong();
        this.dataInicio = (Calendar) in.readSerializable();
        this.dataFim = (Calendar) in.readSerializable();
        this.periodo = in.readInt();
        this.tipoRepeticao = in.readInt();
        this.intervalo = in.readInt();
        this.idMedicamento = in.readLong();
        this.status = in.readByte() != 0;
        this.freqHorario = in.readString();
        this.freqDias = in.readString();
        this.alarmeInfo = in.createTypedArrayList(AlarmeInfo.CREATOR);
    }

    public static final Creator<Alarme> CREATOR = new Creator<Alarme>() {
        @Override
        public Alarme createFromParcel(Parcel source) {
            return new Alarme(source);
        }

        @Override
        public Alarme[] newArray(int size) {
            return new Alarme[size];
        }
    };
}
