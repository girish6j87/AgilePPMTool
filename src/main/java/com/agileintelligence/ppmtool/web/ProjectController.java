package com.agileintelligence.ppmtool.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agileintelligence.ppmtool.domain.Project;
import com.agileintelligence.ppmtool.services.MapValidationErrorService;
import com.agileintelligence.ppmtool.services.ProjectService;
import com.agileintelligence.ppmtool.services.ProjectTaskService;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidationErrorService mapValidationErrorService;
    

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult  bindingResult) {
        ResponseEntity<?> errorMap = mapValidationErrorService.mapValidationService(bindingResult);
        if(errorMap != null) return errorMap;
        Project returnedObject = projectService.saveOrUpdateProject(project);
        return new ResponseEntity<Project>(returnedObject, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId) {
        Project project = projectService.findByProjectIdentifier(projectId);
        return new ResponseEntity<Project>(project,HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProjects() {
        return projectService.findAllProjects();
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId) {
        projectService.deleteProjectByIdentifier(projectId.toUpperCase());
        return new ResponseEntity<String>("Project with ID: '"+projectId.toUpperCase()+"' was deleted",HttpStatus.OK);
    }
}
