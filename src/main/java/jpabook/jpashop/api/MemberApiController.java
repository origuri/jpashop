package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    // entity의 정보가 전부 유출되기 때문에 사용 x
    public List<Member> membersV1(){
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result membersV2(){
         List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                // stream으로 푼 findMembers를 m 이라는 파라미터로 memberDto객체를 생성한다.
                .map(m -> new MemberDto(m.getId(),m.getName()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T>{
        // 유연성이 좋아짐.
        private int count;
        // memberDto에서 원하는 데이터들이 들어갈 객체
        private T data;
    }
    @Data
    @AllArgsConstructor
    static class MemberDto{
        private Long id;
        private String name;
    }

    @PostMapping("/api/v1/members")
    // @RequestBody는 json으로 온 데이터를 Member객체로 변환 해줌.
    // entity가 view와 1 대 1로 매핑되는 것은 매우 좋지 않음. 또한 파라미터를 사용하면 안됨.
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }


    @PostMapping("/api/v2/members")
    // entity의 필드 명이 바뀌어도 컴파일 과정에서 찾을 수 있음.
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request){

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid
                                               UpdateMemberRequest request){

        memberService.update(id, request.getName());
        // 업데이트 한거 따로 넘기는 정보 따로 구분해서 로직을 작성할 것.
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse{
        private Long id;
        private String name;
    }

    @Data
    static class UpdateMemberRequest{
        private String name;
    }

    @Data
    static class CreateMemberRequest{
        private String name;

    }

    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
