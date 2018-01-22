package com.gmail.thales_silva_nascimento.alarmmed;

/**
 * Created by Thales on 21/01/2018.
 */

public class ItemGridView {

    private int imgResource;
    private String texto;

    public ItemGridView(int imgResource, String texto){
        this.imgResource = imgResource;
        this.texto = texto;
    }

    public int getImgResource() {
        return imgResource;
    }

    public String getTexto() {
        return texto;
    }
}
