package com.ctv.todos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ctv.todos.model.Todo;
import com.ctv.todos.repository.TodoRepository;

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

	
	@RequestMapping(method= RequestMethod.GET)
	public ResponseEntity<Iterable<Todo>> getTodos() {
		final Iterable<Todo> result = todoRepository.findAll();
		return new ResponseEntity(result, HttpStatus.OK);
	}

	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<Todo> getById(@PathVariable long id) {
		final Todo result = todoRepository.findOne(id);
		return new ResponseEntity<Todo>(result, (result == null ? HttpStatus.NOT_FOUND 
								: HttpStatus.OK));
	}

	@RequestMapping(value="/active", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Todo>> getActive() {
		final Iterable<Todo> result = todoRepository.findByDoneFalse();
		return new ResponseEntity(result, HttpStatus.OK);
	}

	@RequestMapping(value="/done", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Todo>> getDone() {
		final Iterable<Todo> result = todoRepository.findByDoneTrue();
		return new ResponseEntity(result, HttpStatus.OK);
	}
}
