package com.devillage.teamproject.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Tag extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name="tag_id")
    private Long id;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String name;

    @OneToMany(mappedBy = "tag")
    private List<PostTag> postTag;
}
