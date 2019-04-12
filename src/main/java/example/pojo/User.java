package example.pojo;

import com.alibaba.fastjson.annotation.JSONField;

import java.sql.Timestamp;

public class User {
    private long id;
    private String name;
    private String password;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createDate;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateDate;
    private String nickname;
    private String roleNames;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }
}
