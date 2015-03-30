package at.oculus.teamf.domain.entity;

import javax.persistence.*;

/**
 * Created by Norskan on 30.03.2015.
 */
@Entity
@Table(name = "receptionist", schema = "", catalog = "oculus")
public class ReceptionistEntity {
    private int receptionistId;
    private UserEntity userId;

    @Id
    @Column(name = "receptionistId", nullable = false, insertable = true, updatable = true)
    public int getReceptionistId() {
        return receptionistId;
    }

    public void setReceptionistId(int receptionistId) {
        this.receptionistId = receptionistId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReceptionistEntity that = (ReceptionistEntity) o;

        if (receptionistId != that.receptionistId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return receptionistId;
    }

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    public UserEntity getUserId() {
        return userId;
    }

    public void setUserId(UserEntity userId) {
        this.userId = userId;
    }
}
