/*
 * File: Sector.java 
 */
package deadwood;
import java.util.*;

/**
 *
 * @author Alex
 */
public class Sector {

   // Fields
    public String name;
    private List<Sector> adjacentSectors;
    public int numberOfShots;
    public int shotCount;
    private List<Role> roles;
    private List<Actor> actors;
    private Scene scene = null;
    private Boundary boundary;
    private Boundary sceneBoundary;
    public List<Boundary> shots;
    
    
    // Constructor
    public Sector(String _name, List<Boundary> _shots, List<Role> _roles)
    {
        name = _name;
        numberOfShots = _shots.size();
        adjacentSectors = new LinkedList<>();
        actors = new LinkedList<>();
        shots = _shots;
        roles = _roles;
        shotCount = numberOfShots;
        
    }
    
     public Sector(String _name, List<Boundary> _shots, List<Role> _roles, Boundary bnd, Boundary scnBnd)
    {
        this(_name,  _shots,  _roles);
        boundary = bnd;
        sceneBoundary = scnBnd;
    }
    // Methods
    //////
    // Show info about a sector
    public void printInfo()
    {
        System.out.println();
        System.out.println("Sector info: ");
        System.out.println("------------");
        System.out.println("Name: " + name);
        System.out.println("# Of shots: " + numberOfShots);
        if (scene == null)
        {
            printAdjacentSectors();
            return;
        }
        System.out.println("Active scene ID: " + scene.sceneNum);
        
        printRoles();
        if (scene != null) scene.printInfo();
        
        printAdjacentSectors();                
    }
    
    // Print roles
    public void printRoles()
    {
        for(int i = 0; i < roles.size(); i++)
            roles.get(i).printInfo();
    }
    
    // Get Roles
    public List<Role> getRoles ()
    {
        return roles;
    }
    
    public void printAdjacentSectors()
    {
        System.out.println();
        System.out.println("Adjacent sectors: ");
        for(int i = 0; i < adjacentSectors.size(); i++)
            System.out.println(i +". " + adjacentSectors.get(i).name + ". ");
    }
    
        public void SetBoundary(int x1, int y1, int x2, int y2)
    {
        boundary = new Boundary(x1, y1, x2, y2);
    }
    	
    public void SetBoundary(float x, float y, float radX, float radY)
    {
        boundary = new Boundary(x, y, radX, radY);
    }
 
    public Boundary getBoundary()
    {
        return boundary;
    }
    
    public Boundary getSceneBoundary()
    {
        return sceneBoundary;
    }
        
    // Get adjacent Sectors
    public List<Sector> getAdjacentSectors ()
    {
        return adjacentSectors;
    }
    
    // Add an adjacent sector
    public void addAdjacentSector(Sector sector)
    {
        adjacentSectors.add(sector);
    }
    
    // Reset the shots
    public void resetShotCount()
    {
        shotCount = numberOfShots;
    }
    
        // Reset the shots
    public Boundary getCurrentShot()
    {
        return shots.get(shotCount);
    }
    // Return available roles
    public List<Role> getAvailableRoles (int rank)
    {
        List<Role> availRoles = new LinkedList();
        if(scene == null || roles == null || roles.size() == 0) 
        {
            return availRoles;
        }
        
        //sector roles
        for (int i = 0; i < roles.size(); i++)
        {
           Role role = roles.get(i);
            if (rank >= role.getRank())
                availRoles.add(role);
        }
        // Scene roles
        
        for (int i = 0; i < scene.getRoles().size(); i++)
        {
            Role role = scene.getRoles().get(i);
            if (rank >= role.getRank())
                availRoles.add(role);
        }
        return availRoles;
    }
    
    // Remove one shot
    public void removeShot ()
    {
        shotCount--;
        if(shotCount == 0 && !name.equals("Trailer") && !name.equals("Casting Office") )
        {
            wrapScene();
        }
    }
   
    // Add a new player
    public void addActor (Actor actor)
    {
        actors.add(actor);
    } 

    // Add a shotr
    public void addActor (Boundary shot)
    {
        shots.add(shot);
    }
    
    // Remove actor
    public void removeActor (Actor actor)
    {
        actors.remove(actor);
    } 
    // Return wheather the scene is complete
    public boolean SceneComplete ()
    {       
        return shotCount == 0;
    }
    
    // Return the name of the sector
    public String getName ()
    {
        return name;
    }
    
    // Add a scene
    public void addScene(Scene scene)
    {
        this.scene = scene;
    }
    
    // Get a scene
    public Scene getScene ()
    {
        return scene;
    }
    
    // Remove a scene
    public void removeScene()
    {
        scene = null;
    }
    
   // Get available roles 
   public Role getRoleByRankType(int rrank, boolean isExtras) 
   {
       List<Role> availRoles = getAvailableRoles(rrank);
       
        for(int i = 0; i < availRoles.size(); i++)
        {
           Role  role = availRoles.get(i);
           if (role.getRank() == rrank && role.IsFree && role.isExtrasType() == isExtras)
           return availRoles.get(i);
        }
        return null;
   }
    
    // Wrap the scene
    private void  wrapScene()
    {
	Actor starringActor = getStarringActor();
		
	if(starringActor != null )
	{
	    Dictionary<Integer, Integer> bonusMap = new Hashtable();
        bonusMap = getBonusDestribution(starringActor.DiceWrap());

	    for (int i = 0; i < actors.size(); i++)
	    {
	       actors.get(i).getBonusPay(bonusMap);

            }
        }
        for (int i = 0; i < actors.size(); i++)
        {
            actors.get(i).reset(actors.get(i).getSector());
        }
        //removeScene();
    }
    
   // Get a working actor
   private Actor getStarringActor()
	{

		for (int i = 0; i < actors.size(); i++)
		{
         if (!actors.get(i).role.isExtras)
			   return actors.get(i);
		}

		return null;
	}

   // Calculate the bonuses for each player
	private Dictionary<Integer, Integer> getBonusDestribution( List<Integer> wrapDices)
	{
		int currentroleId = 0;
		Dictionary<Integer, Integer> bonusMap = new Hashtable();
		for(int i = 0; i < scene.getRoles().size(); i++)
		{
			bonusMap.put(scene.getRoles().get(i).getRank(), 0);
		}

		for(int i = 0; i < wrapDices.size(); i++)
		{
         int rank=scene.getRoles().get(currentroleId).getRank();
         bonusMap.put(rank,bonusMap.get(rank)+wrapDices.get(i));
         if(++currentroleId == scene.getRoles().size())
			   currentroleId = 0;
		}
		return bonusMap;
	}
   
} // end Sector

