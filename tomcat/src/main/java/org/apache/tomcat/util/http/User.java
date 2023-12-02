package org.apache.tomcat.util.http;

public class User {
    private final UserType userType;

    public User(String userType) {
        this.userType = UserType.get(userType);
    }

    public User() {
        this.userType = UserType.GUEST;
    }

    public boolean isNew() {
        return userType.equals(UserType.GUEST);
    }
}
