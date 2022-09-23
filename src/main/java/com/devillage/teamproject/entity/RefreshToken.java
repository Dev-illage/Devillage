package com.devillage.teamproject.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class RefreshToken extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name="refresh_token_id")
    private Long id;

    @ToString.Include
    @EqualsAndHashCode.Include
    private String tokenValue;

    public RefreshToken(String tokenValue) {
        this.tokenValue = tokenValue;
    }
}
