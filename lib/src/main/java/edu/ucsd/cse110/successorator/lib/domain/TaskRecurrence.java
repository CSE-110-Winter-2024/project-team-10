package edu.ucsd.cse110.successorator.lib.domain;

public enum TaskRecurrence {
    // Assign numeric values so that storage/translation is easy
    ONE_TIME (0),
    DAILY    (1),
    WEEKLY   (2),
    MONTHLY  (3),
    YEARLY   (4);

    private final int value;

    TaskRecurrence(int value) {
        this.value = value;
    }

    public Integer value() {
        return value;
    }

    @Override
    public String toString() {
        switch (value) {
            case 0:
                return "[0nce]";
            case 1:
                return "[Daily]";
            case 2:
                return "[Weekly]";
            case 3:
                return "[Monthly]";
            case 4:
                return "[Yearly]";
        }

        throw new IllegalArgumentException();
    }

    public static TaskRecurrence fetch(int value) {
        switch (value) {
            case 0:
                return ONE_TIME;
            case 1:
                return DAILY;
            case 2:
                return WEEKLY;
            case 3:
                return MONTHLY;
            case 4:
                return YEARLY;
        }

        throw new IllegalArgumentException();
    }
}
