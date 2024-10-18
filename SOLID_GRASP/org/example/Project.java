package org.example; 

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// S -- Each class is focused on 1 responsibility i.e. Project only manages
//      tasks and team members

public class Project {
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Task> tasks;
    private List<TeamMember> teamMembers;

    public Project(String name, String description, LocalDate startDate,
                   LocalDate endDate) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tasks = new ArrayList<>();
        this.teamMembers = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
    }

    public void addTeamMember(TeamMember teamMember) {
        teamMembers.add(teamMember);
    }

    public void removeTeamMember(TeamMember teamMember) {
        teamMembers.remove(teamMember);
    }

    // ------------- Getter and setters -------------
    // getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // getter and setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // getter and setter for startDate
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    // getter and setter for endDate
    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    // getter and setter for tasks
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    // getter and setter for teamMembers
    public List<TeamMember> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<TeamMember> teamMembers) {
        this.teamMembers = teamMembers;
    }

}
