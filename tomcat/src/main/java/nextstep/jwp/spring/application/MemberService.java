package nextstep.jwp.spring.application;


import java.util.Optional;
import nextstep.jwp.spring.domain.UserRepository;
import nextstep.jwp.spring.domain.model.User;
import nextstep.jwp.spring.infrastructure.repository.UserRepositoryImpl;

public class MemberService {
    private final UserRepository userRepository;

    public MemberService() {
        this.userRepository = new UserRepositoryImpl();
    }

    public Optional<User> getMemberLoginInfo(String account, String password) {
        User user = userRepository.findByAccount(account);
        if(user.checkPassword(password)) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public User saveMember(String account, String email,String password) {
        User user = new User(account,email,password);
        return userRepository.save(user);
    }
}
