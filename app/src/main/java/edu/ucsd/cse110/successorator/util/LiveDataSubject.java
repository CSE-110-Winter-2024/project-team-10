package edu.ucsd.cse110.successorator.util;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import edu.ucsd.cse110.successorator.lib.util.Observer;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class LiveDataSubject<T> implements Subject<T> {
        private final LiveData<T> adapted;

        public LiveDataSubject(LiveData<T> adapted) {
            this.adapted = adapted;
        }

        @Nullable
        public T getValue() {
            return adapted.getValue();
        }

        @Override
        public void observe(Observer<T> observer) {
            adapted.observeForever(observer::onChanged);
        }

        @Override
        public void removeObserver(Observer<T> observer) {
            adapted.removeObserver(observer::onChanged);
        }
}
