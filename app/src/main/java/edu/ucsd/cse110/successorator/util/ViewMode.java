package edu.ucsd.cse110.successorator.util;

import edu.ucsd.cse110.successorator.MainViewModel;

public enum ViewMode {
    TODAY, TOMORROW, PENDING, RECURRING;

    public static ViewMode fetch(int index) {
        switch (index) {
            case 0:
                return TODAY;
            case 1:
                return TOMORROW;
            case 2:
                return PENDING;
            case 3:
                return RECURRING;
            default:
                throw new IllegalArgumentException();
        }
    }
}
