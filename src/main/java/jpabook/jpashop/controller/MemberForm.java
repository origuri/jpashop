package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {
    // 필수값으로 해줌.
    // implementation 'org.springframework.boot:spring-boot-starter-validation' 임포트해야함.
    @NotEmpty(message = "회원 이름은 필수 입니다. 이게 나오는거임? ")
    private String name;

    private String city;
    private String street;
    private String zipcode;

}
