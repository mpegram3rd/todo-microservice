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
 * It ties together the logic an persistence mechanisms for our service.
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


	/**
	 * Retrieves the full list of TODO items.
	 * 
	 * @return A complete list of Todos
	 */
	@RequestMapping(method= RequestMethod.GET)
	public ResponseEntity<Iterable<Todo>> getAll() {
		final Iterable<Todo> result = todoRepository.findAll();
		return new ResponseEntity<Iterable<Todo>>(result, HttpStatus.OK);
	}

	/**
	 * Adds a new TOOD item.
	 * 
	 * @param todo 
	 * 		The Todo item to add
	 * 
	 * @return the updated Todo item with the item ID populated so it can be referenced on the front end
	 */
	@RequestMapping(method= RequestMethod.POST, consumes="application/json")
	public ResponseEntity<Todo> add(@RequestBody final Todo todo) {
		
		todo.setCreatedTimestamp(new Date());
		if (todo.isDone())
			todo.setCompletedTimestamp(new Date());
		
		// Get updated Todo instance with ID so we can return that back.
		final Todo result = todoRepository.save(todo);
		
		return new ResponseEntity<Todo>(result, HttpStatus.OK);
	}

	/**
	 * Retrieves a specific Todo item.
	 * 
	 * @param id 
	 * 		The ID of the item the user wishes to retrieve.
	 * 
	 * @return A Todo item that matches the ID or an HTTP 404 Status NOT FOUND 
	 * 		if the resource couldn't be found.
	 */
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<Todo> getById(@PathVariable long id) {
		
		final Todo result = todoRepository.findOne(id);
		
		return new ResponseEntity<Todo>(result, (result == null ? HttpStatus.NOT_FOUND 
				: HttpStatus.OK));
	}
	
	/**
	 * Updates an existing Todo item if the item can be found using the provided ID.
	 * 
	 * @param id
	 * 		ID of the todo item to update.
	 * @param todoIn
	 * 		Updated values (only Title and Completion status are modifiable).
	 * 
	 * @return The modified Todo item or an HTTP 404 Status NOT FOUND 
	 * 		if the resource couldn't be found.
	 */
	@RequestMapping(value="/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Todo> update(@PathVariable long id, @RequestBody final Todo todoIn) {
		
		// Don't trust the incoming data. Make sure the item exists.  
		Todo result = todoRepository.findOne(id);
		
		if (result != null) {
			// See if the done status changed?  If so we need to update the completed timestamp
			if (result.isDone() != todoIn.isDone()) {
				result.setDone(todoIn.isDone());
				// Set timestamp to current time if incoming value is true, null if incoming value is false
				result.setCompletedTimestamp(todoIn.isDone() ? new Date() : null);
			}
			
			result.setTitle(todoIn.getTitle());
			
			result = todoRepository.save(result);
		}
		return new ResponseEntity<Todo>(result, (result == null ? HttpStatus.NOT_FOUND 
								: HttpStatus.OK));
	}

	/**
	 * Deletes a Todo item
	 * 
	 * @param id
	 * 		The ID of the Todo item to be deleted.
	 * 
	 * @return An HTTP 200 Status OK if the deletion completed successfully, 
	 * 		or an HTTP 404 Status NOT FOUND if the resource couldn't be found.
	 */
	@RequestMapping(value="/{id}", method = RequestMethod.DELETE)
	public HttpStatus delete(@PathVariable long id) {
		
		// Don't trust the incoming data. Make sure the item exists.  
		Todo result = todoRepository.findOne(id);
		
		if (result != null) {
			todoRepository.delete(id);
		}
		return (result == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
	}
	
	/**
	 * Changes an existing Todo item to "active" (not done)
	 * 
	 * @param id
	 * 		ID of the todo item to update.
	 * 
	 * @return The modified Todo item or an HTTP 404 Status NOT FOUND 
	 * 		if the resource couldn't be found.
	 */
	@RequestMapping(value="/{id}/active", method = RequestMethod.PUT)
	public ResponseEntity<Todo> active(@PathVariable long id) {
		
		Todo result = todoRepository.findOne(id);
		if (result != null) {
			result.setCompletedTimestamp(null);
			result.setDone(false);
		
			result = todoRepository.save(result);
		}
		return new ResponseEntity<Todo>(result, (result == null ? HttpStatus.NOT_FOUND 
				: HttpStatus.OK));
	}

	/**
	 * Changes an existing Todo item to "completed" 
	 * 
	 * @param id
	 * 		ID of the todo item to update.
	 * 
	 * @return The modified Todo item or an HTTP 404 Status NOT FOUND 
	 * 		if the resource couldn't be found.
	 */
	@RequestMapping(value="/{id}/completed", method = RequestMethod.PUT)
	public ResponseEntity<Todo> complete(@PathVariable long id) {
		
		Todo result = todoRepository.findOne(id);
		if (result != null) {
			result.setCompletedTimestamp(new Date());
			result.setDone(true);
		
			result = todoRepository.save(result);
		}
		return new ResponseEntity<Todo>(result, (result == null ? HttpStatus.NOT_FOUND 
				: HttpStatus.OK));
	}

	/**
	 * Finds all active (not done) Todo items
	 * 
	 * @return A list of active todo items
	 */
	@RequestMapping(value="/active", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Todo>> getActive() {
		final Iterable<Todo> result = todoRepository.findByDoneFalse();
		return new ResponseEntity<Iterable<Todo>>(result, HttpStatus.OK);
	}

	/**
	 * Finds all completed Todo items
	 * 
	 * @return A list of completed todo items.
	 */
	@RequestMapping(value="/completed", method = RequestMethod.GET)
	public ResponseEntity<Iterable<Todo>> getCompleted() {
		final Iterable<Todo> result = todoRepository.findByDoneTrue();
		return new ResponseEntity<Iterable<Todo>>(result, HttpStatus.OK);
	}
	
}
