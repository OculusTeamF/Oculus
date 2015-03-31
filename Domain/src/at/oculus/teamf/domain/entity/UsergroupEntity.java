package at.oculus.teamf.domain.entity;

import javax.persistence.*;

/**
 * Created by Norskan on 30.03.2015.
 */
@Entity
@Table(name = "usergroup", schema = "", catalog = "oculus")
public class UsergroupEntity {
    private int userGroupId;
    private String userGroupName;
    private String description;

    @Id
    @Column(name = "userGroupId", nullable = false, insertable = true, updatable = true)
    public int getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(int userGroupId) {
        this.userGroupId = userGroupId;
    }

    @Basic
    @Column(name = "userGroupName", nullable = false, insertable = true, updatable = true, length = 50)
    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    @Basic
    @Column(name = "description", nullable = true, insertable = true, updatable = true, length = 65535)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsergroupEntity that = (UsergroupEntity) o;

        if (userGroupId != that.userGroupId) return false;
        if (userGroupName != null ? !userGroupName.equals(that.userGroupName) : that.userGroupName != null)
            return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userGroupId;
        result = 31 * result + (userGroupName != null ? userGroupName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
