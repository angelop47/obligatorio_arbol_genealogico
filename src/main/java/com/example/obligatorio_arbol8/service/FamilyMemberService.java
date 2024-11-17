package com.example.obligatorio_arbol8.service;

import com.example.obligatorio_arbol8.entity.FamilyMember;
import com.example.obligatorio_arbol8.entity.RelationshipType;
import com.example.obligatorio_arbol8.exception.ParentNotFoundException;
import com.example.obligatorio_arbol8.repository.FamilyMemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FamilyMemberService {

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    // Crear un nuevo miembro familiar con generación automática
    @Transactional
    public FamilyMember createFamilyMember(String name, Set<Long> parentIds) {
        FamilyMember familyMember = FamilyMember.builder()
                .name(name)
                .build();

        if (parentIds != null && !parentIds.isEmpty()) {
            Set<FamilyMember> parents = familyMemberRepository.findAllById(parentIds)
                    .stream()
                    .collect(Collectors.toSet());

            if (parents.size() != parentIds.size()) {
                throw new ParentNotFoundException("Uno o más padres no fueron encontrados");
            }

            familyMember.getParents().addAll(parents);
            parents.forEach(parent -> parent.getChildren().add(familyMember));

            // Calcular la generación automáticamente
            Optional<Integer> maxParentGeneration = parents.stream()
                    .map(FamilyMember::getGeneration)
                    .max(Integer::compareTo);
            familyMember.setGeneration(maxParentGeneration.orElse(0) + 1);
        } else {
            // Si no tiene padres, se asume la generación 1
            familyMember.setGeneration(1);
        }

        return familyMemberRepository.save(familyMember);
    }

    // Método para agregar padres a un miembro existente con generación automática
    @Transactional
    public FamilyMember addParents(Long memberId, Set<Long> parentIds) {
        Optional<FamilyMember> memberOpt = familyMemberRepository.findById(memberId);
        if (!memberOpt.isPresent()) {
            throw new RuntimeException("Miembro no encontrado con ID: " + memberId);
        }

        FamilyMember member = memberOpt.get();

        Set<FamilyMember> parentsToAdd = familyMemberRepository.findAllById(parentIds)
                .stream()
                .collect(Collectors.toSet());

        if (parentsToAdd.size() != parentIds.size()) {
            throw new ParentNotFoundException("Uno o más padres no fueron encontrados");
        }

        member.getParents().addAll(parentsToAdd);
        parentsToAdd.forEach(parent -> parent.getChildren().add(member));

        // Actualizar la generación basada en los nuevos padres
        Optional<Integer> maxParentGeneration = parentsToAdd.stream()
                .map(FamilyMember::getGeneration)
                .max(Integer::compareTo);
        int newGeneration = maxParentGeneration.orElse(0) + 1;
        if (newGeneration > member.getGeneration()) {
            member.setGeneration(newGeneration);
        }

        return familyMemberRepository.save(member);
    }

    // Obtener un miembro por ID
    public Optional<FamilyMember> getFamilyMember(Long id) {
        return familyMemberRepository.findById(id);
    }

    // Obtener todos los miembros
    public Set<FamilyMember> getAllFamilyMembers() {
        return Set.copyOf(familyMemberRepository.findAll());
    }
}