package com.gmail.thales_silva_nascimento.alarmmed;

/**
 * Created by Thales on 25/02/2018.
 */

public abstract class ListItemHistoricoInterface {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_GENERAL = 1;

    abstract public int getType();
}
