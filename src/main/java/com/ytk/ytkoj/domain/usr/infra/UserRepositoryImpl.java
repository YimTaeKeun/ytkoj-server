package com.ytk.ytkoj.domain.usr.infra;

import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaEntity;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaRepository;
import com.ytk.ytkoj.global.exception.NoResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User findByUserUuid(String userUuid) {
        return userJpaRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new NoResourceException("유저를 찾을 수 없습니다."))
                .toPojo();
    }

    @Override
    public User findByServiceUniqueId(String serviceUniqueId) {
        return userJpaRepository.findByServiceUniqueId(serviceUniqueId)
                .orElseThrow(() -> new NoResourceException("유저를 찾을 수 없습니다."))
                .toPojo();
    }

    @Override
    public User findByHandle(String handle) {
        return userJpaRepository.findByHandle(handle)
                .orElseThrow(() -> new NoResourceException("유저를 찾을 수 없습니다."))
                .toPojo();
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = UserJpaEntity.fromPojo(user);
        UserJpaEntity saved = userJpaRepository.save(entity);

        // 저장된 객체를 확인 차 다시 변환하여 돌려줍니다.
        return saved.toPojo();
    }
}
