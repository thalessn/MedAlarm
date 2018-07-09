package com.gmail.thales_silva_nascimento.alarmmed;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Thales on 08/07/2018.
 */

public class RetroMedicamento {

    @SerializedName("nome")
    @Expose
    private String nome;
    @SerializedName("nomeGen")
    @Expose
    private String nomeGen;
    @SerializedName("forma")
    @Expose
    private String forma;
    @SerializedName("concentracao")
    @Expose
    private String concentracao;

    public RetroMedicamento(String nome, String forma, String concentracao){
        this.nome = nome;
        this.forma = forma;
        this.concentracao = concentracao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getForma() {
        return forma;
    }

    public void setForma(String forma) {
        this.forma = forma;
    }

    public String getNomeGen() {
        return nomeGen;
    }

    public void setNomeGen(String nomeGen) {
        this.nomeGen = nomeGen;
    }

    public String getConcentracao() {

        return concentracao;
    }

    public void setConcentracao(String concentracao) {
        this.concentracao = concentracao;
    }
}
