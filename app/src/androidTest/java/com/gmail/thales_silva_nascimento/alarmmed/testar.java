package com.gmail.thales_silva_nascimento.alarmmed;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.internal.util.AndroidRunnerParams;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.gmail.thales_silva_nascimento.alarmmed.dao.EspecialidadeDAO;
import com.gmail.thales_silva_nascimento.alarmmed.model.Especialidade;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.model.InitializationError;

import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Thales on 09/02/2017.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class testar  {

    public boolean Maior(int x, int y){
        return x > y ? true : false;
    }

    @org.junit.Test
    public void CRUD(){
        Context appContext = InstrumentationRegistry.getTargetContext();

        EspecialidadeDAO especDAO = EspecialidadeDAO.getInstance(appContext);

        List<Especialidade> especBase = especDAO.recuperaTodas();
        assertFalse(especBase.isEmpty());
        assertTrue(Maior(especBase.size(), 10));

       // Especialidade especE = especDAO.recuperaTodas().get(0);
       // assertSame(especR.getId(), especE.getId());





    }
}
