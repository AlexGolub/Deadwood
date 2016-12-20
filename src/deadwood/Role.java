/*
 * File: Role.java 
 */
package deadwood;

/**
 *
 * @author Alex
 */
public class Role {

   // Fields
    private String name;
    private int rank;
    public boolean isExtras;
    public boolean IsFree;
    private Boundary boundary;
   
    // Constructor
    public Role (String _name, int _rank, boolean _isExtras)
    {
        name = _name;
        rank = _rank;
        isExtras = _isExtras;
	IsFree = true;
    }

     public Role (String _name, int _rank, boolean _isExtras, Boundary bnd)
    {
        this(_name,  _rank, _isExtras);
        boundary = bnd;
    }
    // Methods
    ///////
    // Show info about a role
     public void printInfo()
    {
        System.out.println();
        System.out.println("Role info: ");
        System.out.println("------------");
        System.out.println("Rank: " + rank);
        String roleType = isExtras? "Extras": "Starring";
        System.out.println("Role type: "+ roleType);   
    }
     
     // Get rank
     public int getRank ()
     {
         return rank;
     }
      
     // Get boundary
     public Boundary getBoundary ()
     {
         return boundary;
     }
     
     // Check wheather the role is extra
     public boolean isExtrasType ()
     {
         return isExtras;
     }     
     
     public void SetBoundary(int x1, int y1, int x2, int y2)
     {
        boundary = new Boundary(x1, y1, x2, y2);
     }
    	
    public void SetBoundary(float x, float y, float radX, float radY)
    {
        boundary = new Boundary(x, y, radX, radY);
    }

} // end Role
