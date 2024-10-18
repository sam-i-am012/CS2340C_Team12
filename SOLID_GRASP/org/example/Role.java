package org.example; 

public class Role {
    private String roleName;
    private String responsibility;

    public Role(String roleName, String responsibility) {
        this.roleName = roleName;
        this.responsibility = responsibility;
    }

    // ------------- Getter and setters -------------
    // getter and setter for roleName
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    // getter and setter for responsibility
    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }
}
