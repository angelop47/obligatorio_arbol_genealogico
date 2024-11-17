package com.example.obligatorio_arbol8.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class FamilyMemberCreateDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    private Set<Long> parentIds; // IDs de los padres o antecesores
}