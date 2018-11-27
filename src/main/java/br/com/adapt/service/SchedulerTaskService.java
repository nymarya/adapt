package br.com.adapt.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.adapt.domain.Type;
import br.com.adapt.model.Resource;
import br.com.adapt.model.Task;

@Service
public class SchedulerTaskService extends SchedulerService<Task> {

	
	private TaskService taskService;
	
	
	@Override
	public void orderTemporaryTasksByPriority() {

    	Collections.sort(temporaryTasks, new Comparator<Task>() {
    		public int compare(Task t1, Task t2) {
    			return -(t1.getPriority().compareTo(t2.getPriority()));
    		}
    	});

		
	}


	@Override
	public void generateGroupsTask( ) {
		
		List<Task> temporary = taskService.findTemporaryNotDoneByUserAuthenticated();
		
		//System.out.println(temporary);
		/*
		List<Task> resources = taskService.findRoutineByUserAuthenticated();
		
		// percorre todas as tarefas
		for( Task resource : resources){
        	
        	// verifica se é rotina
        	if(Planning tasks dinamically ￼
Planning tasks dinamically ￼
 resource.getType() == Type.ROUTINE ){
        		
        		// verifica de qual dia da semana é e add tarefa na lista
	        	switch( resource.getDay() ) {
	        		case 0: routineTasks.get(0).add(resource); break;
	        		case 1: routineTasks.get(1).add(resource); break;
	        		case 2: routineTasks.get(2).add(resource); break;
	        		case 3: routineTasks.get(3).add(resource); break;
	        		case 4: routineTasks.get(4).add(resource); break;
	        		case 5: routineTasks.get(5).add(resource); break;
	        		case 6: routineTasks.get(6).add(resource); break;
	        		default: break;
        		} 
        		
        	} 
        	
        }*/
		
	}

	
	
}