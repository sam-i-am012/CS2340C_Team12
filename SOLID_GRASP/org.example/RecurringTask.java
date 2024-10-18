package org.example; 

import jxava.time.LocalDate;

// I -- Each tasks can have its own implementation of execute() without forcing
//      unrelated tasks to implement methods they don't need

public class RecurringTask extends Task {
    private int interval;

    public RecurringTask(String title, String description, LocalDate dueDate,
                String status, String priority, int interval) {
        super(title, description, dueDate, status, priority);
        this.interval = interval;
    }

    public void execute() {
        // Handles execution for recurring tasks
    }
}
