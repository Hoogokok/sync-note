package site.syncnote.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member join(String email, String name, String password) {
        verifyEmail(email);
        Member member = Member.builder()
            .email(email)
            .name(name)
            .password(password)
            .build();
        return memberRepository.save(member);
    }

    private void verifyEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException();
        }
    }
}
