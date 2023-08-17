package com.dh.userservice.entities;



import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Schema(description = "Complete parameters to create my user entity")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id of my user, usually generated by the database")
    private Long id;
    @Column
    @NotNull
    @Size(min = 1, max = 250)
    private String first_name ;
    @Column
    @NotNull
    @Size(min = 1, max = 250)
    private String last_name ;
    @Column
    @NotNull
    @Size(min = 1, max = 250)
    private String dni ;
    @Column
    @NotNull
    @Size(min = 1, max = 250)
    @Schema(description = "email of mi user cant be used in other accounts")
    private String email ;
    @Column
    @NotNull
    @Size(min = 1, max = 250)
    private String phone ;

    @Column
    @NotNull
    @Size(min = 1, max = 250)
    private String password;
    @JsonIgnore
    @Transient
    private Account account;
}