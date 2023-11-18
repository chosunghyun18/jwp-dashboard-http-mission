package nextstep.jwp.spring.presantation;

public class MemberRegisterRequest {
    private final String account;
    private final String email;
    private final String password;
    public MemberRegisterRequest(String account, String email, String password) {
        this.account = account;
        this.email = email;
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
