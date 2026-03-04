package com.logdownloader.service;

import com.logdownloader.model.Module;
import com.logdownloader.repository.ModuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {

    private final ModuleRepository moduleRepository;

    public ModuleService(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    public Module createModule(Module module) {
        return moduleRepository.save(module);
    }

    public List<Module> getModules() {
        return moduleRepository.findAll();
    }
}