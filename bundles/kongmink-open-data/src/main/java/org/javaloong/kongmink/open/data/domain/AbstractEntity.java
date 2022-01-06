package org.javaloong.kongmink.open.data.domain;

import java.io.Serializable;

public abstract class AbstractEntity<ID extends Serializable> implements Serializable {

    public static final String ID_FIELD_KEY = "id";

    protected ID id;

    /**
     * Returns the id of the entity.
     *
     * @return the id
     */
    public ID getId() {
        return id;
    }

    /**
     * Sets the id of the entity.
     *
     * @param id the id to set
     */
    public void setId(ID id) {
        this.id = id;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (this.id == null || obj == null || !(this.getClass().equals(obj.getClass()))) {
            return false;
        }

        AbstractEntity<?> that = (AbstractEntity<?>) obj;

        return this.id.equals(that.getId());
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }
}
