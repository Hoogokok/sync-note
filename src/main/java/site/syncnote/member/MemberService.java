package site.syncnote.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
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

    public void updateMemberInfo(String email, String name, String password) {
        Member member = findMember(email);
        member.updateMemberInfo(name, password);
        memberRepository.save(member);
    }

    private Member findMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(IllegalArgumentException::new);
    }

    private void verifyEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException();
        }
    }
}
