package com.ctv.todos.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ctv.todos.model.Todo;
import com.ctv.todos.repository.TodoRepository;

/**
 * This is the receiver of our REST calls.   
 * 
 * It ties together the logic and persistence mechanisms for our service.
 * @author Macon Pegram
 */
@RestController
@RequestMapping("/v1/todos")
public class TodoController {

	private final TodoRepository todoRepository;
	
	/** 
	 * Dependency injection point for the Todo Repository
	 * 
	 * @param repo 
	 * 		Todo Repository implementation.
	 */
	@Autowired
	public TodoController(final TodoRepository repo) {
		this.todoRepository = repo;
	}

}
