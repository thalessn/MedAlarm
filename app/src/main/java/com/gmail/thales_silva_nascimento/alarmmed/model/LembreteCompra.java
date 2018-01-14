package com.gmail.thales_silva_nascimento.alarmmed.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thales on 10/01/18.
 */

public class LembreteCompra implements Parcelable {
    private long id;
    private long idMedicamento;
    private int qtd_alerta;
    private String horarioAlerta;

    public LembreteCompra(long idMedicamento, int qtd_alerta, String horarioAlerta){
        this.idMedicamento = idMedicamento;
        this.qtd_alerta = qtd_alerta;
        this.horarioAlerta = horarioAlerta;
    }

    public LembreteCompra(long id, long idMedicamento, int qtd_alerta, String horarioAlerta){
        this.id = id;
        this.idMedicamento = idMedicamento;
        this.qtd_alerta = qtd_alerta;
        this.horarioAlerta = horarioAlerta;
    }

    public long getId() {
        return id;
    }

    public long getIdMedicamento() {
        return idMedicamento;
    }

    public int getQtd_alerta() {
        return qtd_alerta;
    }

    public String getHorarioAlerta() {
        return horarioAlerta;
    }

    public void setIdMedicamento(long idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public void setHorarioAlerta(String horarioAlerta) {
        this.horarioAlerta = horarioAlerta;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.idMedicamento);
        dest.writeInt(this.qtd_alerta);
        dest.writeString(this.horarioAlerta);
    }

    protected LembreteCompra(Parcel in) {
        this.id = in.readLong();
        this.idMedicamento = in.readLong();
        this.qtd_alerta = in.readInt();
        this.horarioAlerta = in.readString();
    }

    public static final Parcelable.Creator<LembreteCompra> CREATOR = new Parcelable.Creator<LembreteCompra>() {
        @Override
        public LembreteCompra createFromParcel(Parcel source) {
            return new LembreteCompra(source);
        }

        @Override
        public LembreteCompra[] newArray(int size) {
            return new LembreteCompra[size];
        }
    };
}
