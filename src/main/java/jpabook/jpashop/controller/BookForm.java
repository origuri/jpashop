package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {
    // item 엔티티 상속 필드 값
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    // book 엔티티만 갖고있는 것
    private String author;
    private String isbn;
}
