package com.ytk.ytkoj.global.token.refresh;

import com.ytk.ytkoj.global.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Lob
    private String token;

    private String jti;

    public RefreshToken(String token, String jti) {
        this.token = token;
        this.jti = jti;
    }
}
