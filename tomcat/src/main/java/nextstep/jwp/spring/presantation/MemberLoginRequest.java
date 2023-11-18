package nextstep.jwp.spring.presantation;

public class MemberLoginRequest {
    private final String account;
    private final String password;

    public MemberLoginRequest(String account, String password) {
        this.account = account;
        this.password = password;
    }
    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }
}
