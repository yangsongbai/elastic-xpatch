package com.dirk.authorization.domain;

/**
 * @ClassName UserInof
 * @Description 请描述类的业务用途
 * @Author yangsongbai
 * @Date 2021/6/6 下午5:01
 * @email yangsongbaivat@163.com
 * @Version 1.0
 **/
public class UserInfo {
    private String  user;
    private String  password;
    private String  token;
    private int     version;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", version=" + version +
                '}';
    }
}
