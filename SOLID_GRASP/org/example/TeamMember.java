package org.example; 

import java.util.ArrayList;
import java.util.List;

// L -- TeamMember is independent of other classes and doesn't need to know the
//      internals of Project or Task, it only interacts with its role and the
//      projects it's assigned to.
// D -- Roles are abstracted from the TeamMember class, making it easier to
//      assign different roles without changing the member class

public class TeamMember {
    private String name;
    private String email;
    private Role role;
    private List<Project> projects;

    public TeamMember(String name, String email, Role role) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.projects = new ArrayList<>();
    }

    public void joinProject(Project project) {
        if (!projects.contains(project)) {
            projects.add(project);
        }
    }

    public void leaveProject(Project project) {
        if (projects.contains(project)) {
            projects.remove(project);
        }
    }

    // ------------- Getter and setters -------------
    // getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // getter and setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // getter and setter for role
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // getter and setter for projects
    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
