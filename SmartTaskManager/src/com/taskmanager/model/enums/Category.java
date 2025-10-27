package com.taskmanager.model.enums;

public enum Category {
    CODING,
    YOGA,
    GYM,
    SLEEP;

    @Override
    public String toString()
    {
        return switch (this)
        {
            case CODING -> "Coding";
            case YOGA -> "Yoga";
            case GYM -> "Gym";
            case SLEEP -> "Sleep";
            default -> super.toString();
        };
    }
}
