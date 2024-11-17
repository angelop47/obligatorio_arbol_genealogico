package com.example.obligatorio_arbol8.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class FamilyMemberDTO {
    private Long id;

    private String name;

    private Integer generation; // Campo 'generation'

    private Set<FamilyMemberSimpleDTO> parents; // Detalles de los padres

    private Set<FamilyMemberSimpleDTO> children; // Detalles de los hijos
}