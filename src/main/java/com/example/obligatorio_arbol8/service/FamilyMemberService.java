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

    // Crear un nuevo miembro familiar con múltiples padres
    @Transactional
    public FamilyMember createFamilyMember(String name, Integer degree, Set<Long> parentIds) {
        FamilyMember familyMember = FamilyMember.builder()
                .name(name)
                .degree(degree)
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
            // No es necesario guardar los padres explícitamente; JPA lo hará automáticamente
        }

        return familyMemberRepository.save(familyMember);
    }

    // Método para agregar padres a un miembro existente
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