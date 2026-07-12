package com.ytk.ytkoj.global.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class CustomUserDetails implements UserDetails {
    private String userHandle;
    private String userUuid;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return this.userHandle;
    }

    public CustomUserDetails(String userHandle, String userUuid){
        this.userHandle = userHandle;
        this.userUuid = userUuid;
    }
}
