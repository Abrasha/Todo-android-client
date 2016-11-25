package aabramov.com.todomanager.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Andrii Abramov on 11/24/16.
 */
public class User implements Serializable {

    private String id;

    private String username;

    private List<Todo> todos;

    public String getId() {
        return id;
    }

    public List<Todo> getTodos() {
        return todos;
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return String.format("User{id='%s', todos=%s, username='%s'}", id, todos, username);
    }
}
