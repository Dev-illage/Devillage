package com.devillage.teamproject.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "CantBlockTwice", columnNames =
                        {"src_user_id", "dest_user_id"})
        }
)
public class Block extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "block_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "src_user_id")
    private User srcUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dest_user_id")
    private User destUser;
}
