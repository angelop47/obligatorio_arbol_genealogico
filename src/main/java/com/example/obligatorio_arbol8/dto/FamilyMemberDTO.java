package com.example.obligatorio_arbol8.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class FamilyMemberDTO {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotNull(message = "El grado es obligatorio")
    @Min(value = 1, message = "El grado debe ser al menos 1")
    private Integer degree;

    private Set<Long> parentIds; // IDs de los padres o antecesores

    private Set<Long> childIds; // Opcional: IDs de los hijos
}