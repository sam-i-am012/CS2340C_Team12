package org.example; 

import java.time.LocalDate;

// I -- Each tasks can have its own implementation of execute() without forcing
//      unrelated tasks to implement methods they don't need

public class UrgentTask extends Task {
    public UrgentTask(String title, String description, LocalDate dueDate,
                String status, String priority) {
        super(title, description, dueDate, status, priority);
    }

    public void execute() {
        // Handles execution for urgent tasks
    }
}
