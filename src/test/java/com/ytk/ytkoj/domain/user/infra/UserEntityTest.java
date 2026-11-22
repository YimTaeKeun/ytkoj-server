package com.ytk.ytkoj.domain.user.infra;

import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.domain.UserRole;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEntityTest {

    @Test
    public void 유저_도메인이_JpaEntity로_매핑이_된다(){
        // given
        String userUuid = "testUuid";
        String registerService = "reg";
        String handle = "handle";
        String socialUQId = "Uq";
        UserRole userRole = UserRole.CLIENT;
        User user = User.builder()
                .userUuid(userUuid)
                .registerService(registerService)
                .handle(handle)
                .socialUQId(socialUQId)
                .userRole(userRole).build();

        // when
        UserJpaEntity entity = UserJpaEntity.toEntity(user);

        // then
        assertThat(entity.getUserUuid()).isEqualTo(userUuid);
        assertThat(entity.getRegisterService()).isEqualTo(registerService);
        assertThat(entity.getHandle()).isEqualTo(handle);
        assertThat(entity.getUserRole()).isEqualTo(userRole);
        assertThat(entity.getServiceUniqueId()).isEqualTo(socialUQId);

    }

    @Test
    public void JpaEntity가_유저_도메인으로_매핑이_된다(){
        // given
        String userUuid = "testUuid";
        String registerService = "reg";
        String handle = "handle";
        String socialUQId = "Uq";
        UserRole userRole = UserRole.CLIENT;
        UserJpaEntity entity = UserJpaEntity.builder()
                .handle(handle)
                .userUuid(userUuid)
                .registerService(registerService)
                .serviceUniqueId(socialUQId)
                .userRole(userRole)
                .build();

        // when
        User user = entity.toDomain();

        // then
        assertThat(user.getUserUuid()).isEqualTo(userUuid);
        assertThat(user.getRegisterService()).isEqualTo(registerService);
        assertThat(user.getHandle()).isEqualTo(handle);
        assertThat(user.getUserRole()).isEqualTo(userRole);
        assertThat(user.getSocialUQId()).isEqualTo(socialUQId);
    }

    @Test
    public void JpaEntity가_업데이트된다(){
        // given
        String userUuid = "testUuid";
        String registerService = "reg";
        String handle = "handle";
        String socialUQId = "Uq";
        UserRole userRole = UserRole.CLIENT;
        UserJpaEntity entity = UserJpaEntity.builder()
                .handle("h")
                .userUuid("u")
                .registerService("r")
                .serviceUniqueId("s")
                .userRole(UserRole.CLIENT)
                .build();
        User user = User.builder()
                .userUuid(userUuid)
                .registerService(registerService)
                .handle(handle)
                .socialUQId(socialUQId)
                .userRole(userRole).build();

        // when
        entity.applyChange(user);

        // then
        assertThat(entity.getUserUuid()).isEqualTo(userUuid);
        assertThat(entity.getRegisterService()).isEqualTo(registerService);
        assertThat(entity.getHandle()).isEqualTo(handle);
        assertThat(entity.getUserRole()).isEqualTo(userRole);
        assertThat(entity.getServiceUniqueId()).isEqualTo(socialUQId);
    }
}
