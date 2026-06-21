package com.ytk.ytkoj.usr.infra;

import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.infra.UserRepository;
import com.ytk.ytkoj.domain.usr.infra.UserRepositoryImpl;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaEntity;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserRepositoryTest {

    @Mock
    private UserJpaRepository userJpaRepository;


    private User testUser;

    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        String userUuid = "testUuid";
        String serviceUqId = "testServiceUq";
        String handle = "UqHandle";
        String registerService = "testService";

        testUser = User.builder()
                .userUuid(userUuid)
                .serviceUqId(serviceUqId)
                .handle(handle)
                .registerService(registerService)
                .build();

        userRepository = new UserRepositoryImpl(userJpaRepository);
    }

    @Test
    public void 유저를_성공적으로_만들어낼_수_있다(){
        // given
        String userUuid = "testUuid";
        String serviceUqId = "testServiceUq";
        String handle = "UqHandle";
        String registerService = "testService";
        UserJpaEntity entity = UserJpaEntity.fromPojo(testUser);
        when(userJpaRepository.save(any(UserJpaEntity.class)))
                .thenReturn(entity);

        // when
        User user = userRepository.save(testUser);

        // then
        assertThat(user.getUserUuid()).isEqualTo(userUuid);
        assertThat(user.getServiceUqId()).isEqualTo(serviceUqId);
        assertThat(user.getHandle()).isEqualTo(handle);
        assertThat(user.getRegisterService()).isEqualTo(registerService);

    }
}
