package at.oculus.teamf.databaseconnectiontests.entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Norskan on 31.03.2015.
 */
@Entity
@Table(name = "user", schema = "", catalog = "oculus")
public class UserEntity {
    private int userId;
    private String userName;
    private String password;
    private String realName;
    private Timestamp createDate;
    private Timestamp idleDate;

    @Id
    @Column(name = "userId", nullable = false, insertable = true, updatable = true)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "userName", nullable = false, insertable = true, updatable = true, length = 30)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "password", nullable = false, insertable = true, updatable = true, length = 30)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "realName", nullable = false, insertable = true, updatable = true, length = 100)
    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Basic
    @Column(name = "createDate", nullable = false, insertable = true, updatable = true)
    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @Basic
    @Column(name = "idleDate", nullable = true, insertable = true, updatable = true)
    public Timestamp getIdleDate() {
        return idleDate;
    }

    public void setIdleDate(Timestamp idleDate) {
        this.idleDate = idleDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (userId != that.userId) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (realName != null ? !realName.equals(that.realName) : that.realName != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (idleDate != null ? !idleDate.equals(that.idleDate) : that.idleDate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userId;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (realName != null ? realName.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (idleDate != null ? idleDate.hashCode() : 0);
        return result;
    }
}
