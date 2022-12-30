package org.javaloong.kongmink.open.data.domain.support;

import org.javaloong.kongmink.open.data.domain.AbstractEntityAuditable;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public class AuditListener {

    @PrePersist
    public void setCreateAt(AbstractEntityAuditable<?> audit) {
        audit.setCreatedAt(LocalDateTime.now());
    }

    @PreUpdate
    public void setUpdateAt(AbstractEntityAuditable<?> audit) {
        audit.setUpdatedAt(LocalDateTime.now());
    }
}
