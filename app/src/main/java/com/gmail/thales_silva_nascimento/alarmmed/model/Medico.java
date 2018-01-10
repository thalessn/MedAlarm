package com.gmail.thales_silva_nascimento.alarmmed.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Thales on 10/02/2017.
 */

public class Medico implements Parcelable {
    private long id;
    private String nome;
    private String endereco;
    private String telefone;
    private String observacao;
    private int idEspec;

    public Medico(String nome, String endereco, String telefone, String observacao, int idEspec){
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.observacao = observacao;
        this.idEspec = idEspec;
    }

    public Medico(long id, String nome, String endereco, String telefone, String observacao, int idEspec){
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.observacao = observacao;
        this.idEspec = idEspec;
    }

    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getObservacao() {
        return observacao;
    }

    public int getIdEspec() {
        return idEspec;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.nome);
        dest.writeString(this.endereco);
        dest.writeString(this.telefone);
        dest.writeString(this.observacao);
        dest.writeInt(this.idEspec);
    }

    protected Medico(Parcel in) {
        this.id = in.readLong();
        this.nome = in.readString();
        this.endereco = in.readString();
        this.telefone = in.readString();
        this.observacao = in.readString();
        this.idEspec = in.readInt();
    }

    public static final Parcelable.Creator<Medico> CREATOR = new Parcelable.Creator<Medico>() {
        @Override
        public Medico createFromParcel(Parcel source) {
            return new Medico(source);
        }

        @Override
        public Medico[] newArray(int size) {
            return new Medico[size];
        }
    };
}
