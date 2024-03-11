package edu.ucsd.cse110.successorator.lib.domain;

public enum TaskMode {
    // Assign numeric values so that storage/translation is easy
    PENDING (0),
    TODAY    (1),
    TOMORROW   (2);

    private final int value;

    TaskMode(int value) {
        this.value = value;
    }

    public Integer value() {
        return value;
    }

    @Override
    public String toString() {
        switch (value) {
            case 0:
                return "[Pending]";
            case 1:
                return "[Today]";
            case 2:
                return "[Tomorrow]";
        }

        throw new IllegalArgumentException();
    }

    public static TaskMode fetch(int value) {
        switch (value) {
            case 0:
                return PENDING;
            case 1:
                return TODAY;
            case 2:
                return TOMORROW;
        }

        throw new IllegalArgumentException();
    }
}
