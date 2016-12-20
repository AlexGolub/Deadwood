/*
 * File: Scene.java 
 */
package deadwood;
import java.util.*;

/**
 *
 * @author Alex
 */
public class Scene {

    // Fields
    public int players;
    public String sceneRoles;
    public int sceneNum;
    private String name;
    private String description;
    private int budget;
    private List<Role> roles;
    public Boundary boundary;

    
    // Constructor
    public Scene(int _sceneNum, int _budget, List<Role> _roles )
    {
        sceneNum = _sceneNum;
        budget = _budget;
        roles = _roles;
        
    }
    
     public Scene(int _sceneNum, int _budget, List<Role> _roles, Boundary bnd )
    {
        this(_sceneNum,  _budget, _roles);
        boundary = bnd;
    }
    
    // Methods
    //////
    // Show information about a scene
    public void printInfo()
    { 
        printRoles();
    }
    
    // Get scene name
    public int getSceneName ()
    {
        return sceneNum;
    }
    
    // Print roles
    public void printRoles()
    {
        for(int i = 0; i < roles.size(); i++)
            roles.get(i).printInfo();
    }
    
    // Return the budget for the scene
    public int getBudget ()
    {
        return budget;
    }
    
    // Return Roles
    public List<Role> getRoles ()
    {
        return roles;
    }
    
    
    public void setBoundary(int x1, int y1, int x2, int y2)
    {
        boundary = new Boundary(x1, y1, x2, y2);
    }
    	
    public void setBoundary(float x, float y, float radX, float radY)
    {
        boundary = new Boundary(x, y, radX, radY);
    }
    
    public void setBoundary(Boundary bond)
    {
        boundary = bond;
    }
            

} // end Scene

