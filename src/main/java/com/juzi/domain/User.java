package com.juzi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A user.
 */
@Document(collection = "JHI_USER")
public class User extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Pattern(regexp = "^[a-z0-9]*$|(anonymousUser)")
    @Size(min = 1, max = 50)
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60) 
    private String password;

    @Size(max = 50)
    @Field("first_name")
    private String firstName;

    @Size(max = 50)
    @Field("last_name")
    private String lastName;

    @NotNull
    @Email
    @Size(max = 100)
    private String email;

    private boolean activated = false;

    @Size(min = 2, max = 5)
    @Field("lang_key")
    private String langKey;

    @Size(max = 20)
    @Field("activation_key")
    @JsonIgnore
    private String activationKey;

    @Field("partner_code")
    private String partnerCode;

    @Field("secret_key")
    private String secretKey;

    @Size(max = 20)
    @Field("reset_key")
    private String resetKey;

    private Long balance;

    @Field("donate_balance")
    private Long donateBalance;

    //判断账户是否欠费 0:未欠费 1：欠费
    private String amount;

    @Field("reset_date")
    private ZonedDateTime resetDate = null;
//
//    @JsonIgnore
//    private Set<Authority> authorities = new HashSet<>();

    @Field("mgr_industry")
    private  String mgrIndustry;

    private String delete;

    // 是否配置了风控引擎 0:false 1:true
    @Field("is_configured_risk_engine")
    private String isConfiguredRiskEngine;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public ZonedDateTime getResetDate() {
       return resetDate;
    }

    public void setResetDate(ZonedDateTime resetDate) {
       this.resetDate = resetDate;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

//    public Set<Authority> getAuthorities() {
//        return authorities;
//    }
//
//    public void setAuthorities(Set<Authority> authorities) {
//        this.authorities = authorities;
//    }

    public String getPartnerCode() {
        return partnerCode;
    }

    public void setPartnerCode(String partnerCode) {
        this.partnerCode = partnerCode;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getDonateBalance() {
        return donateBalance;
    }

    public void setDonateBalance(Long donateBalance) {
        this.donateBalance = donateBalance;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMgrIndustry() {
        return mgrIndustry;
    }

    public void setMgrIndustry(String mgrIndustry) {
        this.mgrIndustry = mgrIndustry;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getIsConfiguredRiskEngine() {
        return isConfiguredRiskEngine;
    }

    public void setIsConfiguredRiskEngine(String isConfiguredRiskEngine) {
        this.isConfiguredRiskEngine = isConfiguredRiskEngine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        if (!login.equals(user.login)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", activated=" + activated +
                ", langKey='" + langKey + '\'' +
                ", activationKey='" + activationKey + '\'' +
                ", partnerCode='" + partnerCode + '\'' +
                ", secretKey='" + secretKey + '\'' +
                ", resetKey='" + resetKey + '\'' +
                ", balance=" + balance +
                ", donateBalance=" + donateBalance +
                ", amount='" + amount + '\'' +
                ", resetDate=" + resetDate +
                ", mgrIndustry='" + mgrIndustry + '\'' +
                ", delete='" + delete + '\'' +
                ", isConfiguredRiskEngine='" + isConfiguredRiskEngine + '\'' +
                '}';
    }
}
