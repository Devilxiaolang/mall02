package com.company.Commons;

public class Const {
    public static final String CURRENTUSER="currentUser";
    public interface Role{
        int ROLE_ADMIN=1;//管理员
        int ROLE_USER=0;//普通
    }
    public interface VaildType{
        String EMAIL="email";
        String USERNAME="username";
    }
}
