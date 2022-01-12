package org.javaloong.kongmink.open.data.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
public class ClientEntity extends AbstractEntity<String> {

    private String name;
    private LocalDateTime createdDate;
    private UserEntity user;

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public void setId(String s) {
        super.setId(s);
    }

    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "created_date", nullable = false)
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @ManyToOne(optional=false)
    @JoinColumn(name="user_id", nullable=false)
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
