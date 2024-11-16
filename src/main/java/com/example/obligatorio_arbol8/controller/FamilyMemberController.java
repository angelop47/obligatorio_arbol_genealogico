package com.example.obligatorio_arbol8.controller;

import com.example.obligatorio_arbol8.dto.FamilyMemberDTO;
import com.example.obligatorio_arbol8.entity.FamilyMember;
import com.example.obligatorio_arbol8.service.FamilyMemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/family")
public class FamilyMemberController {

    @Autowired
    private FamilyMemberService familyMemberService;

    // Crear un nuevo miembro
    @PostMapping
    public ResponseEntity<FamilyMemberDTO> createFamilyMember(@Valid @RequestBody FamilyMemberDTO dto) {
        FamilyMember created = familyMemberService.createFamilyMember(
                dto.getName(),
                dto.getDegree(),
                dto.getParentId()
        );
        FamilyMemberDTO responseDto = convertToDTO(created);
        return ResponseEntity.ok(responseDto);
    }

    // Obtener un miembro por ID
    @GetMapping("/{id}")
    public ResponseEntity<FamilyMemberDTO> getFamilyMember(@PathVariable Long id) {
        return familyMemberService.getFamilyMember(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener todos los miembros
    @GetMapping
    public ResponseEntity<Set<FamilyMemberDTO>> getAllFamilyMembers() {
        Set<FamilyMemberDTO> dtos = familyMemberService.getAllFamilyMembers()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(dtos);
    }

    // Método para convertir entidad a DTO
    private FamilyMemberDTO convertToDTO(FamilyMember member) {
        FamilyMemberDTO dto = new FamilyMemberDTO();
        dto.setId(member.getId());
        dto.setName(member.getName());
        dto.setDegree(member.getDegree());
        if (!member.getParents().isEmpty()) {
            dto.setParentId(member.getParents().iterator().next().getId()); // Asumiendo un único padre
        }
        // Opcional: Mapear hijos
        dto.setChildIds(member.getChildren().stream()
                .map(FamilyMember::getId)
                .collect(Collectors.toSet()));
        return dto;
    }
}