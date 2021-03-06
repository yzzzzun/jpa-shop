package com.yzzzzun.jpashop.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.yzzzzun.jpashop.domain.Member;
import com.yzzzzun.jpashop.service.MemberService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
public class MemberApiController {

	private final MemberService memberService;

	public MemberApiController(MemberService memberService) {
		this.memberService = memberService;
	}

	@PostMapping("/api/v1/members")
	public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}

	@PostMapping("/api/v2/members")
	public CreateMemberResponse saveMemberV1(@RequestBody @Valid CreateMemberRequest request) {
		Member member = new Member(request.getName());

		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}

	@PutMapping("/api/v2/members/{memberId}")
	public UpdateMemberResponse updateMember(@PathVariable("memberId") Long memberId,
		@RequestBody @Valid UpdateMemberRequest request) {
		memberService.update(memberId, request.getName());
		Member findMember = memberService.findOne(memberId);
		return new UpdateMemberResponse(memberId, findMember.getName());
	}

	@GetMapping("/api/v1/members")
	public List<Member> membersV1() {
		return memberService.findMembers();
	}

	@GetMapping("/api/v2/members")
	public Result membersV2() {
		List<MemberDto> memberDtos = memberService.findMembers().stream()
			.map(member -> new MemberDto(member.getName()))
			.collect(Collectors.toList());
		return new Result(memberDtos.size(), memberDtos);
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class Result<T> {
		private int count;
		private T data;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	static class MemberDto {
		String name;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor

	static class UpdateMemberResponse {
		private Long id;
		private String name;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	static class UpdateMemberRequest {
		private String name;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	static class CreateMemberResponse {
		private Long id;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	static class CreateMemberRequest {
		private String name;
	}
}
