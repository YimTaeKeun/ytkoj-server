package com.ytk.ytkoj.domain.user.domain;

import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.domain.UserRole;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {


    @Test
    public void 도메인_객체의_필수_요소에_null이_들어가면_오류가_발생합니다(){
        // given
        String nullUserUuid = null;
        UserRole nullUserRole = null;
        String notNullUserUuid = "testUuid";
        UserRole notNullUserRole = UserRole.CLIENT;

        // when
        User.UserBuilder base = User.builder()
                .userUuid(notNullUserUuid)
                .userRole(notNullUserRole);

        // then
        assertThatThrownBy(
                () -> base.userUuid(nullUserUuid).build()
        ).isInstanceOf(NullPointerException.class);

        assertThatThrownBy(
                () -> base.userRole(nullUserRole).build()
        ).isInstanceOf(NullPointerException.class);
    }
}
