package com.devillage.teamproject.entity;

import com.devillage.teamproject.entity.enums.CategoryType;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Category extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name="category_id")
    private Long id;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Enumerated(EnumType.STRING)
    private CategoryType categoryType;

    @OneToMany(mappedBy = "category")
    private List<Post> posts = new ArrayList<>();

    public void addPost(Post post) {
        posts.add(post);
    }
}
