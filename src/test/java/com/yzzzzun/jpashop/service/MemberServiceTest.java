package com.yzzzzun.jpashop.service;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.yzzzzun.jpashop.domain.Member;
import com.yzzzzun.jpashop.repository.MemberRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberRepository memberRepository;

	@DisplayName("회원가입을 테스트")
	@Test
	void testJoin() {
		//given
		Member member = new Member("kim", null, null);
		//when
		Long memberId = memberService.join(member);
		//then
		assertThat(memberId).isEqualTo(member.getId());
	}

	@DisplayName("회원 중복 테스트")
	@Test
	void testMemberDuplicate() {
		//given
		Member member1 = new Member("kim", null, null);
		Member member2 = new Member("kim", null, null);
		//when
		memberService.join(member1);
		//then
		Assertions.assertThatThrownBy(() -> {
			memberService.join(member2);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("이미 존재하는 회원입니다.");

	}

}