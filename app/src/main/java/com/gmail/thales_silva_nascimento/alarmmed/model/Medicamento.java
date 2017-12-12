package com.gmail.thales_silva_nascimento.alarmmed.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thales on 06/09/17.
 */

public class Medicamento implements Parcelable {

    private long id;
    private String nome;
    private int dosagem;
    private String tipoDosagem;
    private boolean uso_continuo;
    private String observacao;
    private String foto;


    public Medicamento(String nome, int dosagem, String tipoDosagem, boolean uso_continuo, String observacao, String foto){
        this.nome = nome;
        this.dosagem = dosagem;
        this.tipoDosagem = tipoDosagem;
        this.uso_continuo = uso_continuo;
        this.observacao = observacao;
        this.foto = foto;
    }

    public Medicamento(long id, String nome, int dosagem, String tipoDosagem, boolean uso_continuo, String observacao, String foto){
        this.id = id;
        this.nome = nome;
        this.dosagem = dosagem;
        this.uso_continuo = uso_continuo;
        this.observacao = observacao;
        this.foto = foto;
    }

    public void setId(long id){
        this.id = id;
    }

    public long getId(){
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getDosagem() {
        return dosagem;
    }

    public void setDosagem(int dosagem) {
        this.dosagem = dosagem;
    }

    public boolean isUso_continuo() {
        return uso_continuo;
    }

    public void setUso_continuo(boolean uso_continuo) {
        this.uso_continuo = uso_continuo;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getTipoDosagem(){
        return tipoDosagem;
    }

    public void setTipoDosagem(String tipoDosagem) {
        this.tipoDosagem = tipoDosagem;
    }

    @Override
    public String toString() {
        return "Nome: " +nome + " Dosagem: "+String.valueOf(dosagem)+ " - TipoDosagem: "+ tipoDosagem +
                " UsoContinuo: "+String.valueOf(uso_continuo)+ " Observacao: "+observacao+ " Foto: "+foto;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.nome);
        dest.writeInt(this.dosagem);
        dest.writeString(this.tipoDosagem);
        dest.writeInt((this.uso_continuo ? 1 : 0));
        dest.writeString(this.observacao);
        dest.writeString(this.foto);
    }

    protected Medicamento(Parcel in) {
        this.id = in.readLong();
        this.nome = in.readString();
        this.dosagem = in.readInt();
        this.tipoDosagem = in.readString();
        this.uso_continuo = (in.readInt() == 1 ? true : false);
        this.observacao = in.readString();
        this.foto = in.readString();
    }

    public static final Parcelable.Creator<Medicamento> CREATOR = new Parcelable.Creator<Medicamento>() {
        @Override
        public Medicamento createFromParcel(Parcel source) {
            return new Medicamento(source);
        }

        @Override
        public Medicamento[] newArray(int size) {
            return new Medicamento[size];
        }
    };
}
