package com.orchid.orchidbe.domain.role;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Schema(description = "User roles")
    public enum RoleName {
        @Schema(description = "Staff role")
        STAFF,

        @Schema(description = "Normal user")
        USER,

        @Schema(description = "Manager role")
        MANAGER,

        @Schema(description = "Admin role")
        ADMIN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @JsonProperty("id")
    private Long id;

    @Enumerated(EnumType.STRING)
    public RoleName name;

    public Role(RoleName name) {
        this.name = name;
    }

}
