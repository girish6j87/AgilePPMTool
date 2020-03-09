package com.agileintelligence.ppmtool.web;

import com.agileintelligence.ppmtool.domain.Project;
import com.agileintelligence.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult  bindingResult) {
        if(bindingResult.hasErrors()){
            return new ResponseEntity<String>("Invalid project object",HttpStatus.BAD_REQUEST);
        }
        Project returnedObject = projectService.saveOrUpdateProject(project);
        return new ResponseEntity<Project>(returnedObject, HttpStatus.CREATED);
    }

}
