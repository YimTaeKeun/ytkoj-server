package com.ytk.ytkoj.global.token.blacklist;

import com.ytk.ytkoj.global.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class BlackListToken extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String jti;

    public BlackListToken(String jti) {
        this.jti = jti;
    }
}
