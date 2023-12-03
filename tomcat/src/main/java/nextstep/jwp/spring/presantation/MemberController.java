package nextstep.jwp.spring.presantation;

import nextstep.jwp.spring.application.MemberService;
import nextstep.jwp.spring.domain.model.User;

public class MemberController {
    public MemberController() {
        this.memberService = new MemberService();
    }

    private final MemberService memberService;

    public User getMemberLoginInfo(MemberLoginRequest request) {
        return memberService.getMemberLoginInfo(request.getAccount(),request.getPassword()).orElseThrow(()->new IllegalStateException("[Error] WrongPassWord"));
    }

    public  User saveMemberInfo(MemberRegisterRequest request) {
        return memberService.saveMember(request.getAccount(),request.getEmail(),request.getPassword());
    }
}
