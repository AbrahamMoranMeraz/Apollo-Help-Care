package com.mim.apollo.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GalleryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<Integer> counter;

    public GalleryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");

        counter= new MutableLiveData<>();
        counter.setValue(6);
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<Integer> getCounter() {
        return counter;
    }

    public void setCounter(int val){
        counter.setValue(val);
    }

}