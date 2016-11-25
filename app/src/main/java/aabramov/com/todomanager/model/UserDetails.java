package aabramov.com.todomanager.model;

/**
 * @author Andrii Abramov on 11/25/16.
 */
public class UserDetails {
    
    private String id;
    private String username;
    
    public UserDetails(String id, String username) {
        this.id = id;
        this.username = username;
    }
    
    public UserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    @Override
    public String toString() {
        return String.format("UserDetails{id='%s', username='%s'}", id, username);
    }
}
