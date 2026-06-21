package com.ytk.ytkoj.usr.domain;

import com.ytk.ytkoj.domain.usr.domain.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserTest {

    @Test
    public void 유저가_정상적으로_생성되는지_확인한다(){
        // given
        String registerService = "kakao";
        String handle = "hello";
        String serviceUqId = null;
        String userUuid = UUID.randomUUID().toString();

        // when
        User user = User.builder()
                .registerService(registerService)
                .handle(handle)
                .serviceUqId(serviceUqId)
                .userUuid(userUuid)
                .build();

        // then
        assertThat(user.getUserUuid()).isEqualTo(userUuid);
        assertThat(user.getRegisterService()).isEqualTo(registerService);
        assertThat(user.getHandle()).isEqualTo(handle);
        assertThat(user.getServiceUqId()).isEqualTo(serviceUqId);
        assertThat(user.getUserUuid()).isEqualTo(userUuid);
    }

    @Test
    public void 유저의_uuid가_null이면_오류가_발생한다(){
        // given
        String userUuid = null;

        // when
        assertThatThrownBy(
                () -> User.builder()
                        .userUuid(userUuid)
                        .build()
        ).isInstanceOf(NullPointerException.class);

    }
}
