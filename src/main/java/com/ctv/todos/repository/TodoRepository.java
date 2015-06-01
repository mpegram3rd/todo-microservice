package com.ctv.todos.repository;

import org.springframework.data.repository.CrudRepository;

import com.ctv.todos.model.Todo;

public interface TodoRepository extends CrudRepository<Todo, Long> {

	Iterable<Todo> findByDoneTrue();	
	Iterable<Todo> findByDoneFalse();
}
