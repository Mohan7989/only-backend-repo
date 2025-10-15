package com.mohan.studentresources.service;

import com.mohan.studentresources.model.Material;
import com.mohan.studentresources.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {
    
    @Autowired
    private MaterialRepository materialRepository;

    public List<Material> getApprovedMaterials(String semester, String subject, String group, String year, String type) {
        List<Material> all = materialRepository.findByApprovedTrueOrderByIdDesc();

        return all.stream()
                .filter(m -> semester == null || semester.isEmpty() || 
                            (m.getSemester() != null && m.getSemester().equalsIgnoreCase(semester)))
                .filter(m -> subject == null || subject.isEmpty() || 
                            (m.getSubject() != null && m.getSubject().equalsIgnoreCase(subject)))
                .filter(m -> group == null || group.isEmpty() || 
                            (m.getGroupName() != null && m.getGroupName().equalsIgnoreCase(group)))
                .filter(m -> year == null || year.isEmpty() || 
                            (m.getUploadYear() != null && m.getUploadYear().equalsIgnoreCase(year)))
                .filter(m -> type == null || type.isEmpty() || type.equalsIgnoreCase("all") || 
                            (m.getType() != null && m.getType().equalsIgnoreCase(type)))
                .toList();
    }
}