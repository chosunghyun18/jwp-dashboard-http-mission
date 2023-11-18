package nextstep.jwp.spring.domain;

import nextstep.jwp.spring.domain.model.User;

public interface UserRepository {
    User save(User user);
    User findByAccount(String account);
}
