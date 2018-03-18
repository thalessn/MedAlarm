package com.gmail.thales_silva_nascimento.alarmmed.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.gmail.thales_silva_nascimento.alarmmed.ListItemHistorico;

/**
 * Created by thales on 09/09/17.
 */

public class ItemAlarmeHistorico extends ListItemHistorico implements Parcelable {

    private long id;
    Medicamento med;
    String dataProgramada;
    Horario horario;
    String dataAdministrado;
    String horaAdministrado;
    long idAlarme;
    String status;
    public final static String STATUS_TOMADO = "Tomado";
    public final static String STATUS_PULOU = "Pulou";
    public final static String STATUS_ADIOU = "Adiou";

    public ItemAlarmeHistorico(Medicamento med, String dataProgramada, Horario horario, long idAlarme){
        this.med = med;
        this.dataProgramada = dataProgramada;
        this.horario = horario;
        this.idAlarme = idAlarme;
    }

    public ItemAlarmeHistorico(Medicamento med, String dataProgramada, Horario horario, long idAlarme, String dataAdministrado, String horaAdministrado){
        this.med = med;
        this.dataProgramada = dataProgramada;
        this.horario = horario;
        this.idAlarme = idAlarme;
        this.dataAdministrado = dataAdministrado;
        this.horaAdministrado = horaAdministrado;
    }

    public ItemAlarmeHistorico(long id, Medicamento med, String dataProgramada, Horario horario, String dataAdministrado, String horaAdministrado){
        this.id = id;
        this.med = med;
        this.idAlarme = -1;
        this.dataProgramada = dataProgramada;
        this.horario = horario;
        this.dataAdministrado = dataAdministrado;
        this.horaAdministrado = horaAdministrado;
    }

    public Medicamento getMed() {
        return med;
    }

    public Horario getHorario() {
        return horario;
    }

    public String getDataProgramada() {
        return dataProgramada;
    }

    public String getDataAdministrado() {
        return dataAdministrado;
    }

    public String getHoraAdministrado() {
        return horaAdministrado;
    }

    public long getIdAlarme() {
        return idAlarme;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDataAdministrado(String dataAdministrado) {
        this.dataAdministrado = dataAdministrado;
    }

    public void setHoraAdministrado(String horaAdministrado) {
        this.horaAdministrado = horaAdministrado;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeParcelable(this.med, flags);
        dest.writeString(this.dataProgramada);
        dest.writeParcelable(this.horario, flags);
        dest.writeString(this.dataAdministrado);
        dest.writeString(this.horaAdministrado);
        dest.writeLong(this.idAlarme);
        dest.writeString(this.status);
    }

    protected ItemAlarmeHistorico(Parcel in) {
        this.id = in.readLong();
        this.med = in.readParcelable(Medicamento.class.getClassLoader());
        this.dataProgramada = in.readString();
        this.horario = in.readParcelable(Horario.class.getClassLoader());
        this.dataAdministrado = in.readString();
        this.horaAdministrado = in.readString();
        this.idAlarme = in.readLong();
        this.status = in.readString();
    }

    public static final Parcelable.Creator<ItemAlarmeHistorico> CREATOR = new Parcelable.Creator<ItemAlarmeHistorico>() {
        @Override
        public ItemAlarmeHistorico createFromParcel(Parcel source) {
            return new ItemAlarmeHistorico(source);
        }

        @Override
        public ItemAlarmeHistorico[] newArray(int size) {
            return new ItemAlarmeHistorico[size];
        }
    };

    @Override
    public String toString() {
        return "Medicamento: "+med.getNome()+" DataProgramada: "+dataProgramada+"DataAdministrado: "+dataAdministrado;
    }
}

