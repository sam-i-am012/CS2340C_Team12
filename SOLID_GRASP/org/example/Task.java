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

    // ------------- Getter and setters -------------
    // getter and setter for title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // getter and setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //getter and setter for dueDate
    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // getter and setter for status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // getter and setter for priority
    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

}


