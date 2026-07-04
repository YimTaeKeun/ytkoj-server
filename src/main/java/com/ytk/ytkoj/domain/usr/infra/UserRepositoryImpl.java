package com.ytk.ytkoj.domain.usr.infra;

import com.ytk.ytkoj.domain.usr.domain.User;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaEntity;
import com.ytk.ytkoj.domain.usr.infra.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.resource.NoResourceFoundException;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository{

    private final UserJpaRepository userJpaRepository;

    @Override
    public User findByHandle(String handle) {
        UserJpaEntity entity = userJpaRepository.findByHandle(handle)
                .orElseThrow(() -> new NoResourceFoundException("유저를 찾을 수 없습니다."));
        return entity.toDomain();
    }

    @Override
    public User findBySocialUqId(String socialUqId) {
        UserJpaEntity entity = userJpaRepository.findByServiceUniqueId(socialUqId)
                .orElseThrow(() -> new NoResourceFoundException("유저를 찾을 수 없습니다."));
        return entity.toDomain();
    }

    @Override
    public User findByUserUuid(String uuid) {
        UserJpaEntity entity = userJpaRepository.findByUserUuid(uuid)
                .orElseThrow(() -> new NoResourceFoundException("유저를 찾을 수 없습니다."));
        return entity.toDomain();
    }

    @Override
    @Transactional
    public User registerNewUser(User user) {
        UserJpaEntity entity = UserJpaEntity.toEntity(user);
        UserJpaEntity save = userJpaRepository.save(entity);
        return save.toDomain();
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        // 유저 도메인은 db의 pk를 모르기 때문에 엔티티 자체를 수정하는 방향으로 갑니다.
        String userUuid = user.getUserUuid();
        UserJpaEntity entity = userJpaRepository.findByUserUuid(userUuid)
                .orElseThrow(() -> new NoResourceFoundException("유저를 찾을 수 없습니다."));
        entity = entity.applyChange(user);
        UserJpaEntity save = userJpaRepository.save(entity);
        return save.toDomain();
    }

}
