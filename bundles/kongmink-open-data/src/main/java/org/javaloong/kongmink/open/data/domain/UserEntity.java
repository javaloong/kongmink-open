package org.javaloong.kongmink.open.data.domain;

import org.javaloong.kongmink.open.data.converter.HashMapConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "users")
public class UserEntity extends AbstractEntity<String> {

    private String username;
    private Map<String, Object> configData;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private long version;

    @Id
    @Column(name = "id", length = 36)
    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public void setId(String s) {
        super.setId(s);
    }

    @Column(name = "username", nullable = false, unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "config_data", length = 4000)
    @Convert(converter = HashMapConverter.class)
    public Map<String, Object> getConfigData() {
        return configData;
    }

    public void setConfigData(Map<String, Object> configData) {
        this.configData = configData;
    }

    @Column(name = "created_at", nullable = false)
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Column(name = "updated_at")
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Version
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
