package edu.ucsd.cse110.successorator.lib.domain;

public enum TaskContext {
    HOME('H'), WORK('W'), SCHOOL('S'), ERRAND('E');

    private final char symbol;
    TaskContext(char symbol) {
        this.symbol = symbol;
    }

    public char symbol() {
        return symbol;
    }

    public static TaskContext fetch(char symbol) {
        switch (symbol) {
            case 'H':
                return HOME;
            case 'W':
                return WORK;
            case 'S':
                return SCHOOL;
            case 'E':
                return ERRAND;
        }

        throw new IllegalArgumentException();
    }
}
