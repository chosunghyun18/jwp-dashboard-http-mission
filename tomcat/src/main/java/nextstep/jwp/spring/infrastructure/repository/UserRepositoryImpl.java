package nextstep.jwp.spring.infrastructure.repository;

import nextstep.jwp.spring.domain.UserRepository;
import nextstep.jwp.spring.domain.model.User;
import nextstep.jwp.spring.infrastructure.db.InMemoryUserRepository;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public User save(User user) {
        InMemoryUserRepository.save(user);
        return user;
    }

    @Override
    public User findByAccount(String account) {
        return InMemoryUserRepository.findByAccount(account).orElseThrow(IllegalStateException::new);
    }
}
