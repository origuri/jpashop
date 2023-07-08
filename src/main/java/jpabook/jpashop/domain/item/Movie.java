package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity @Getter
@Setter
@DiscriminatorValue("M") // DB에서 쓰는 구분자.
public class Movie extends Item{

    private String director;
    private String actor;
}
