package nextstep.jwp.spring.presantation;

import nextstep.jwp.spring.application.MemberService;

public class MemberController {
    public MemberController() {
        this.memberService = new MemberService();
    }

    private final MemberService memberService;
    public  String getMemberLoginInfo(MemberLoginRequest request) {
        return memberService.getMemberLoginInfo(request.getAccount(),request.getPassword());
    }

    public  String saveMemberInfo(MemberRegisterRequest request) {
        return memberService.saveMember(request.getAccount(),request.getEmail(),request.getPassword());
    }
}
