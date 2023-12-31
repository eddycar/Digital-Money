package com.dh.userservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "description of how is the model of my account which is associated to its respective user")
public class Account {
    @JsonIgnore
    @Schema(description = "id of my userAccount, usually generated by the database")
    private Long id;
    @Schema(description = "alias of my userAccount, usually generated by the Account-service")
    private String alias;
    @NotNull
    @Size(min = 22, max = 22)
    private String cvu;
    @JsonIgnore
    @NotNull
    private Double balance;

   @JsonIgnore
    private Long user_id;
}
