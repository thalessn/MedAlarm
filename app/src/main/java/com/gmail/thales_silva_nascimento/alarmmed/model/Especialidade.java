package com.gmail.thales_silva_nascimento.alarmmed.model;

/**
 * Created by Thales on 07/02/2017.
 */

public class Especialidade {
    private long id;
    private String nome;

    public Especialidade(){

    }

    public Especialidade(long id, String nome){
        this.id = id;
        this.nome = nome;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return nome;
    }
}
