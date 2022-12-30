package org.javaloong.kongmink.open.data.domain;

import org.javaloong.kongmink.open.data.converter.HashMapConverter;
import org.javaloong.kongmink.open.data.domain.support.AuditListener;

import javax.persistence.*;
import java.util.Map;

@Entity
@Table(name = "users")
@EntityListeners(AuditListener.class)
public class UserEntity extends AbstractEntityAuditable<String> {

    private String username;
    private Map<String, Object> configData;

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

    @Version
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
