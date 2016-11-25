package aabramov.com.todomanager.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Andrii Abramov on 11/24/16.
 */

public class Todo implements Serializable {
    // TODO: 11/25/16 add status field
    private String title;
    private String body;

    private Date when;
    
    private Priority priority;
    
    private List<String> tags;
    
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
    
    @Override
    public String toString() {
        return String.format("Todo{title='%s', body='%s', when=%s, priority=%s, tags=%s}", title, body, when, priority, tags);
    }
}
