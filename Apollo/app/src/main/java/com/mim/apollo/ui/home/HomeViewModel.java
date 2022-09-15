package com.mim.apollo.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<Integer> tipoAlerta;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");

        tipoAlerta= new MutableLiveData<>();
        tipoAlerta.setValue(0);
    }

    public LiveData<String> getText() {
        return mText;
    }
    public LiveData<Integer> getTipoAlerta() {
        return tipoAlerta;
    }

    public void setTipoAlerta(int alerta){
        tipoAlerta.postValue(alerta);
    }
}