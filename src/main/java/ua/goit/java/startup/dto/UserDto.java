package ua.goit.java.startup.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.goit.java.startup.bom.UserRole;

import javax.persistence.*;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/*
A (Data Transfer Object) class for some entity(Admin, Developer or Investor)
that will be connect the entities(BOMs) and the database
 */
@Entity
@Table(name = "users")
public class UserDto extends ModelDTO implements UserDetails {

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "contacts")
    private String contacts;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "paidcost")
    private long paidCost;

    @Column(name = "locked")
    private boolean isLocked;

    @Column(name = "image")
    private byte[] image;

    @ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinTable(name = "users_startups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "startup_id"))
    private Set<StartupDto> startupDto = new HashSet<>();

    public UserDto() {
    }

    public UserDto(String username, String password, String contacts, UserRole role, long paidCost) {
        this();
        setUsername(username);
        setPassword(password);
        setContacts(contacts);
        setRole(role);
        setPaidCost(paidCost);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = isNotBlank(username) ? username : "";
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = isNotBlank(password) ? password : "";
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = isNotBlank(email) ? email : "";
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = isNotBlank(contacts) ? contacts : "";
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = (role != null) ? role : UserRole.INVESTOR;
    }

    public long getPaidCost() {
        return paidCost;
    }

    public void setPaidCost(long paidCost) {
        this.paidCost = paidCost > 0 ? paidCost : 0;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Set<StartupDto> getStartupDto() {
        return startupDto;
    }

    public void setStartupDto(Set<StartupDto> startupDto) {
        this.startupDto = startupDto != null ? startupDto : new HashSet<>();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_" + getRole().name());
        grantedAuthorities.add(simpleGrantedAuthority);
        return grantedAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !isLocked;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !isLocked;
    }

    @Override
    public boolean isEnabled() {
        return !isLocked;
    }
}