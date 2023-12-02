package org.apache.tomcat.util.http;

public enum UserType {
    GUEST,USER;

    public static UserType get(String userType) {
        if(userType.equals("user")) return USER;
        if(userType.equals("User")) return USER;
        if(userType.equals("USER")) return USER;
        return GUEST;
    }
}
