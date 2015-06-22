package com.ctv.todos.controller;

import com.ctv.todos.model.Todo;
import com.ctv.todos.repository.TodoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by mpegram on 6/22/15.
 */
@RunWith(PowerMockRunner.class)
public class TodoControllerTest {

    @Mock
    private TodoRepository mockRepository;

    @Mock
    private Iterable<Todo> mockTodoIterable;

    private TodoController controller;

    /**
     * Create a TodoController and inject a Mock TodoRepository into
     * it so we can control the repository's behavior.
     *
     * This method is called before every test case so that you're
     * starting from a known good state.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        controller = new TodoController(mockRepository);
    }

    /**
     * This will test that when we call
     * @throws Exception
     */
    @Test
    public void testGetAll() throws Exception {

        // Define the behavior of the mock (what it should return) if called.
        when(mockRepository.findAll()).thenReturn(mockTodoIterable);

        // Run the test case.
        final ResponseEntity<Iterable<Todo>> result = controller.getAll();

        // Assertions to validate that what was returned was what was expected.
        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(result.getBody(), notNullValue());

        // You can also validate that the mock was used in the expected way.
        verify(mockRepository, times(1)).findAll();
    }

    /**
     * Basic validation for add.  Done is not set, so completed timestamp
     * is not expected to be set.   Make sure we save, and return a result
     * with the Todo class containing the expected values for created and
     * completed
     *
     * @throws Exception
     */
    @Test
    public void testAddNotDone() throws Exception {

        final Todo expectedTodo = new Todo();
        expectedTodo.setDone(false);

        // Define mock behavior.  Note: the "save" parameter is
        // saying "precisely this expectedTodo".
        // If a different "todo" instance were passed in, this
        // matcher would not fire the "when" defined beahvior.
        when(mockRepository.save(expectedTodo)).thenReturn(expectedTodo);

        // Run the test case.
        final ResponseEntity<Todo> result = controller.add(expectedTodo);

        // Assertions to validate that what was returned was what was expected.
        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));

        final Todo todoResult = result.getBody();
        assertThat(todoResult, notNullValue());
        // Make sure creation date was set.
        assertThat(todoResult.getCreatedTimestamp(), notNullValue());

        // Make sure completed timestamp is not set.
        assertThat(todoResult.getCompletedTimestamp(), nullValue());

        // You can also validate that the mock was used in the expected way.
        verify(mockRepository, times(1)).save(expectedTodo);
    }

    /**
     * Validation for add when the Todo is is "done" state.
     * Make sure we save, and return a result
     * with the Todo class containing the expected values for created and
     * completed
     *
     * @throws Exception
     */
    @Test
    public void testAddDone() throws Exception {

        final Todo expectedTodo = new Todo();

        // Define mock behavior.
        when(mockRepository.save(expectedTodo)).thenReturn(expectedTodo);

        // Run the test case.
        final ResponseEntity<Todo> result = controller.add(expectedTodo);

        // Assertions to validate that what was returned was what was expected.
        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));

        final Todo todoResult = result.getBody();
        assertThat(todoResult, notNullValue());
        // Make sure creation date was set.
        assertThat(todoResult.getCreatedTimestamp(), notNullValue());

        // Make sure completed timestamp is set (because it is done).
        assertThat(todoResult.getCompletedTimestamp(), notNullValue());

        // You can also validate that the mock was used in the expected way.
        verify(mockRepository, times(1)).save(expectedTodo);
    }

    /**
     * This is meant as an illustration of how unit tests can make you think
     * about your implementation.  <br/>
     * <br/>
     * Scenario:<br/>
     * - We pass in a Todo Item that is NOT Done <br/>
     * - But we've assigned a "completedTimestamp" to it.<br/>
     * - What should the outcome be?   Should Completed Timestamp be null or not?
     *
     * @throws Exception
     */
    @Test
    public void houstonWeHaveAProblem() throws Exception {

        final Todo expectedTodo = new Todo();
        expectedTodo.setDone(false);
        expectedTodo.setCompletedTimestamp(new Date());

        // Define mock behavior.
        when(mockRepository.save(expectedTodo)).thenReturn(expectedTodo);

        // Run the test case.
        final ResponseEntity<Todo> result = controller.add(expectedTodo);

        // Assertions to validate that what was returned was what was expected.
        assertThat(result, notNullValue());
        assertThat(result.getStatusCode(), equalTo(HttpStatus.OK));

        final Todo todoResult = result.getBody();
        assertThat(todoResult, notNullValue());
        // Make sure creation date was set.
        assertThat(todoResult.getCreatedTimestamp(), notNullValue());

        // Make sure completed timestamp is not set.
        assertThat(todoResult.getCompletedTimestamp(), nullValue());

        // You can also validate that the mock was used in the expected way.
        verify(mockRepository, times(1)).save(expectedTodo);
    }


    @Test
    public void testGetById() throws Exception {

    }

    @Test
    public void testUpdate() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }

    @Test
    public void testActive() throws Exception {

    }

    @Test
    public void testComplete() throws Exception {

    }

    @Test
    public void testGetActive() throws Exception {

    }

    @Test
    public void testGetCompleted() throws Exception {

    }
}