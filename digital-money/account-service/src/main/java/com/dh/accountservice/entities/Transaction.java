package com.dh.accountservice.entities;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Schema(description = "id of my transaction, usually generated by the database")
    private Long id;
    @NotNull
    @Size(min = 1, max = 250)
    private Long account_id;
    @NotNull
    @Size(min = 1, max = 250)
    private LocalDateTime date;
    @NotNull
    @Size(min = 1, max = 250)
    private String description;
    @Column
    @NotNull
    private Double amount;
    @NotNull
    @Size(min = 22, max = 22)
    @Schema(description = "destination cvu has to be numeric and has to have 22 characters")
    private String destination_cvu;
    @NotNull
    @Size(min = 22, max = 22)
    @Schema(description = "origin cvu has to be numeric and has to have 22 characters")
    private String origin_cvu;
    @NotNull
    @Size(min = 1, max = 250)
    private String type;
}