package com.agileintelligence.ppmtool.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agileintelligence.ppmtool.domain.Backlog;
import com.agileintelligence.ppmtool.domain.Project;
import com.agileintelligence.ppmtool.domain.ProjectTask;
import com.agileintelligence.ppmtool.exceptions.ProjectNotFoundException;
import com.agileintelligence.ppmtool.repositories.BacklogRepository;
import com.agileintelligence.ppmtool.repositories.ProjectRepository;
import com.agileintelligence.ppmtool.repositories.ProjectTaskRepository;

@Service
public class ProjectTaskService {
	
	@Autowired
	private BacklogRepository backlogRepository;
	
	@Autowired
	private ProjectTaskRepository projectTaskRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {
		try {
			//Exception: Project not found
			//project task to be added to a specific project, project!=null, Backlog exists
			Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
			//set the backlog to the project task
			projectTask.setBacklog(backlog);
			//we want our project sequence to be like this: IDPRO-1 IDPRO-2 ... 100 101
			Integer backLogSequence = backlog.getPTSequence();
			//update the backlog sequence
			backLogSequence++;
			//add sequence to project task
			backlog.setPTSequence(backLogSequence);
			projectTask.setProjectSequence(projectIdentifier+"-"+backLogSequence);
			projectTask.setProjectIdentifier(projectIdentifier);
			//INITIAL priority when priority null
			if(projectTask.getPriority() == null) { // in future we need projectTask.getPriority() == 0 to handle the form
				projectTask.setPriority(3);
			}
			//INITIAL status when status is null
			if(projectTask.getStatus() == "" || projectTask.getStatus() == null) {
				projectTask.setStatus("TO_DO");
			}
			
			return projectTaskRepository.save(projectTask);
		} catch (Exception e) {
			throw new ProjectNotFoundException("Project not found");
		}
		
	}

	public Iterable<ProjectTask> findBacklogById(String id) {
		Project project = projectRepository.findByProjectIdentifier(id);
		if(project == null) {
			throw new ProjectNotFoundException("Project with ID: '"+id+"' does not exist");
		}
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
	}
	
	public ProjectTask findPTByProjectSequence(String backlog_id,String pt_id) {
		//make sure we are searching right backlog
		//make sure we are searching on an existing backlog
		Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
		if(backlog == null) {
			throw new ProjectNotFoundException("Project with ID: '"+backlog_id+"' does not exist");
		}
		//make sure that our task exists
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
		if(projectTask == null) {
			throw new ProjectNotFoundException("Project Task '"+pt_id+"' not found");
		}
		//make sure the backlog/project id in the path corresponds to the right project
		if(!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException("Project Task '"+pt_id+"' does not exist in project: '"+backlog_id);
		}
		return projectTask;
	}
}
