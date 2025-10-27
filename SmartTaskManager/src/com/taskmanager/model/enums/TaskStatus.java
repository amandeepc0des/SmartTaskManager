package com.taskmanager.model.enums;

public enum TaskStatus {
    PENDING,
    COMPLETED,
    FORECASTED;

    @Override
    public String toString()
    {
        return switch (this) {
            case PENDING -> "Pending";
            case COMPLETED -> "Completed";
            case FORECASTED -> "Forecasted";
            default -> super.toString();
        };
    }
}
