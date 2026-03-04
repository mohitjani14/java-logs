package com.logdownloader.controller;

import com.logdownloader.model.Module;
import com.logdownloader.repository.ModuleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    private final ModuleRepository moduleRepository;

    public ModuleController(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @GetMapping
    public List<Module> getModulesByProject(@RequestParam Long projectId) {
        return moduleRepository.findByProjectId(projectId);
    }

}