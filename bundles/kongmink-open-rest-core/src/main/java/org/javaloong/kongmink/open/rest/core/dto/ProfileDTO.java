package org.javaloong.kongmink.open.rest.core.dto;

import org.javaloong.kongmink.open.common.model.User;
import org.javaloong.kongmink.open.common.model.UserProfile;

public class ProfileDTO {

    private String companyName;
    private String companyProvince;
    private String companyCity;
    private String companyAddress;
    private String contactName;
    private String contactPhone;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyProvince() {
        return companyProvince;
    }

    public void setCompanyProvince(String companyProvince) {
        this.companyProvince = companyProvince;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public UserProfile toUserProfile(User user) {
        UserProfile userProfile = new UserProfile();
        userProfile.setCompanyName(companyName);
        userProfile.setCompanyProvince(companyProvince);
        userProfile.setCompanyCity(companyCity);
        userProfile.setCompanyAddress(companyAddress);
        userProfile.setContactName(contactName);
        userProfile.setContactPhone(contactPhone);
        return userProfile;
    }
}
