package aabramov.com.todomanager.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Andrii Abramov on 11/24/16.
 */
public class Todo implements Serializable {

    private String id;

    private String title;
    private String body;

    private Date when;

    private Priority priority;
    private Status status;

    private List<String> tags;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Todo todo = (Todo) o;
        return Objects.equals(id, todo.id) &&
                Objects.equals(title, todo.title) &&
                Objects.equals(body, todo.body) &&
                Objects.equals(when, todo.when) &&
                priority == todo.priority &&
                status == todo.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, body, when, priority, status);
    }

    @Override
    public String toString() {
        return String.format("Todo{id='%s', title='%s', body='%s', when=%s, priority=%s, status=%s, tags=%s}", id, title, body, when, priority, status, tags);
    }
}
