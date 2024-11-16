package com.example.obligatorio_arbol8.service;

import com.example.obligatorio_arbol8.entity.FamilyMember;
import com.example.obligatorio_arbol8.entity.RelationshipType;
import com.example.obligatorio_arbol8.repository.FamilyMemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class FamilyMemberService {

    @Autowired
    private FamilyMemberRepository familyMemberRepository;

    @Transactional
    public FamilyMember createFamilyMember(String name, Integer degree, Long parentId) {
        FamilyMember familyMember = FamilyMember.builder()
                .name(name)
                .degree(degree)
                .build();

        if (parentId != null) {
            Optional<FamilyMember> parentOpt = familyMemberRepository.findById(parentId);
            if (parentOpt.isPresent()) {
                FamilyMember parent = parentOpt.get();
                familyMember.getParents().add(parent);

            } else {
                throw new RuntimeException("Parent not found with ID: " + parentId);
            }
        }

        return familyMemberRepository.save(familyMember);
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