package org.example; 

import java.time.LocalDate;

// O -- Task class is open for extension by introducing child classes such as
//      RecurringTask and UrgentTask to represent different types of tasks,
//      But is closed for modification.
// G -- Task is a general, abstract class for all types of tasks where specific
//      types of tasks inherit from it.
// L -- Since we're using the abstract class Task for all types of tasks,
//      specific tasks can be substituted without changing functionality

public abstract class Task {
    private String title;
    private String description;
    private LocalDate dueDate;
    private String status;
    private String priority;

    protected Task(String title, String description, LocalDate dueDate,
                String status, String priority) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.status = status; 
        this.priority = priority;
    }

    public abstract void execute();
}


