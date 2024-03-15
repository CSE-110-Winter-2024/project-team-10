package edu.ucsd.cse110.successorator.util;

import androidx.lifecycle.MutableLiveData;

import edu.ucsd.cse110.successorator.lib.util.MutableSubject;

public class MutableLiveDataSubject<T> extends LiveDataSubject<T> implements MutableSubject<T> {
    private final MutableLiveData<T> mutableAdapted;

    public MutableLiveDataSubject(MutableLiveData<T> adapted) {
        super(adapted);
        this.mutableAdapted = adapted;
    }

    @Override
    public void setValue(T value) {
        mutableAdapted.setValue(value);
    }
}
