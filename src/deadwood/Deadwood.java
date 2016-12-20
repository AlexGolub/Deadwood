/*
 * File: Deadwood.java 
 */
package deadwood;
import java.util.*;
import static processing.core.PConstants.CORNER;


/**
 *
 * @author Alex
 */


public class Deadwood {
    
     // Fields
    public int numOfPlayers; 
    public List<Actor> players; 
    public List<Sector> sectors;
    public int numOfDays;
    public int startRank;
    public int startCredit;
    
    // Sector names
    static public String trailer = "Trailer";
    static public String castingOff = "Casting Office";
    static public String hotel = "Hotel";
    static public String bank = "Bank";
    static public String church = "Church";
    static public String saloon = "Saloon";
    static public String mainStr = "Main Street";
    static public String jail = "Jail";
    static public String generalStore = "General Store";
    static public String ranch = "Ranch";
    static public String train = "Train Station";
    static public String secret = "Secret Hideout";
    
    // Structure for selling ranks
    private static class RankPrice{
        public RankPrice (int dol, int cred)
        {
            dollars = dol;
            credits = cred;
        }
        public int dollars;
        public int credits; 
    }
    
    static private Map<Integer, RankPrice> mapRankPrice;
    
    static private Scene[] scene = new Scene[40];
    
    // Constructor
    public Deadwood (int _numOfPlayers) 
    {
        numOfPlayers = _numOfPlayers;
       
       
        switch (numOfPlayers)
        {
            case 2: startRank=1; startCredit=0; numOfDays=3; break;
            case 3: startRank=1; startCredit=0; numOfDays=3; break;
            case 4: startRank=1; startCredit=0; numOfDays=4; break;
            case 5: startRank=1; startCredit=2; numOfDays=3; break;
            case 6: startRank=1; startCredit=4; numOfDays=3; break;
            case 7: startRank=2; startCredit=0; numOfDays=4; break;
            case 8: startRank=2; startCredit=0; numOfDays=4; break;
            default:
                System.out.println("Wrong number of players!");                
        }
        
        // Initialize sectors
        InitSectors();
        
        // Initialize players
        buildPlayers();
                
        // Initialize scenes
        InitScenes();
    }
    
    // Methods
    /////////
    // Assign a new scene
    private void assignScene (int currentDay)
    {
        int l = (currentDay ) * 10;
                
        for (int i = 0; i < sectors.size(); i++)
        {
            Sector sector = sectors.get(i);
            sector.resetShotCount();
            
            if (sector.name.equalsIgnoreCase(trailer) || sector.name.equalsIgnoreCase(castingOff))
                continue;
            Scene s = scene[l++];
            s.setBoundary(sector.getSceneBoundary());
            sector.addScene(s);
            
            List<Role> roles = s.getRoles();
      
            Boundary bnd = s.boundary;
            float gapX = (bnd.width/roles.size())/2;
            float gapY = (bnd.height/roles.size())/2;
            float x,y ;
                     
            for (int j = 0; j < roles.size(); j++)
            {
                Role role = roles.get(j);
            
                if(bnd.width >bnd.height)
                {
                    x = bnd.upperLeftX+ gapX*(j*2+1);
                    y = bnd.upperLeftY + bnd.height/2+20;
                }
                else
                {
                    x=bnd.upperLeftX+bnd.width/2;
                    y=bnd.upperLeftY+ gapY*(j*2+1);;
                }
                role.SetBoundary(x, y, 25, 25);
            }
        }   
    }
    
    // Get a new sector
    private Sector getSector(String sectorName)
    {
        Sector sector = null;
        for (int i = 0; i < sectors.size(); i++)
        {
            sector = sectors.get(i);
            if (sector.name.equalsIgnoreCase(sectorName))
                break;
        }
        return sector;
    }
    
    // Initialize sectors
    private void InitSectors()
    {
        float diceRadius = 25;
        int sh = 120;
        int sw = 210;

        sectors = new LinkedList();
        List<Boundary> shots = new LinkedList();
        sectors.add(new Sector(trailer, shots, new LinkedList<Role>(), new Boundary(986,237,1180, 440),new Boundary(0, 0, 0, 0))); 
        
        sectors.add(new Sector(castingOff, shots, new LinkedList<Role>(), new Boundary(0, 450, 226, 668), new Boundary(0, 0, 0, 0))); 
        
        List<Role> role = new LinkedList();
        
        role.add(new Role("", 2, true,  new Boundary((float)886, 293, diceRadius,diceRadius)));
        role.add(new Role("", 1, true,new Boundary((float)890, 367, diceRadius,diceRadius)));
        shots = new LinkedList();
        shots.add(new Boundary(647,238, diceRadius, diceRadius));
        shots.add(new Boundary(700, 235, diceRadius, diceRadius));
      
        int sx = 628;
        int sy =  275;
        sectors.add(new Sector(saloon, shots, role, new Boundary(600,200, 970,440), new Boundary(sx, sy, sx + sw, sy +sh))); 
        
        role = new LinkedList();
        role.add(new Role("", 3, true, new Boundary((float)998, 772, diceRadius,diceRadius)));
        role.add(new Role("", 2, true, new Boundary((float)1038, 836, diceRadius,diceRadius)));
        role.add(new Role("", 1, true, new Boundary((float)1081, 772, diceRadius,diceRadius)));
        role.add(new Role("", 1, true,new Boundary((float)1119, 836, diceRadius,diceRadius)));
        shots = new LinkedList();
        shots.add(new Boundary(1027, 712, diceRadius, diceRadius));
        shots.add(new Boundary(1081, 712, diceRadius, diceRadius));
        shots.add(new Boundary(1135, 712, diceRadius, diceRadius));
        sx = 1039;
        sy = 477;
        sectors.add(new Sector(hotel, shots, role, new Boundary(977, 440, 1180, 888), new Boundary(sx, sy, sx + sh, sy + sw)));  
        
        role = new LinkedList();
        role.add(new Role("", 4, true, new Boundary((float)737, 128, diceRadius,diceRadius)));
        role.add(new Role("", 2, true, new Boundary((float)658,127, diceRadius,diceRadius)));
        role.add(new Role("", 2, true, new Boundary((float)737, 53, diceRadius,diceRadius)));
        role.add(new Role("", 1, true, new Boundary((float)658, 53, diceRadius,diceRadius)));
        shots = new LinkedList();
        shots.add(new Boundary(818, 53, diceRadius, diceRadius));
        shots.add(new Boundary(870, 53, diceRadius, diceRadius));
        shots.add(new Boundary(920, 53, diceRadius, diceRadius));
        
        sx = 953;
        sy = 32;
        sectors.add(new Sector(mainStr, shots, role, new Boundary(600, 0, 1180,200), new Boundary(sx, sy, sx + sw, sy +sh)));          
        
        role = new LinkedList();
        role.add(new Role("", 3, true, new Boundary((float)921, 494, diceRadius,diceRadius)));
        role.add(new Role("", 2, true, new Boundary((float)921, 573, diceRadius,diceRadius)));
        shots = new LinkedList();
        shots.add(new Boundary(854, 572, diceRadius, diceRadius));
        
        sx = 625;
        sy = 470;
        sectors.add(new Sector(bank,  shots, role, new Boundary(600,440, 975, 640), new Boundary(sx, sy, sx + sw, sy +sh)));    
        
        role = new LinkedList();
        role.add(new Role("", 3, true, new Boundary((float)530, 134, diceRadius,diceRadius)));
        role.add(new Role("", 2, true, new Boundary((float)530, 53, diceRadius,diceRadius)));
        shots = new LinkedList();
        shots.add(new Boundary(455, 180, diceRadius, diceRadius));

        sx = 276;
        sy = 33;
        sectors.add(new Sector(jail,  shots, role, new Boundary(253,0, 600,247), new Boundary(sx, sy, sx + sw, sy +sh)));           
        
        role = new LinkedList();
        role.add(new Role("", 3, true, new Boundary((float)258, 364, diceRadius,diceRadius)));
        role.add(new Role("", 1, true, new Boundary((float)258, 296, diceRadius,diceRadius)));
        shots = new LinkedList();
        shots.add(new Boundary(333, 298, diceRadius, diceRadius));
        shots.add(new Boundary(333, 350, diceRadius, diceRadius));
        sx= 363;
        sy = 275;
        sectors.add(new Sector(generalStore,  shots, role, new Boundary(225, 235,600,440), new Boundary(sx, sy, sx + sw, sy +sh))); 
        
        role = new LinkedList();
        role.add(new Role("", 4, true, new Boundary((float)184, 128, diceRadius,diceRadius)));
        role.add(new Role("", 2, true, new Boundary((float)150,67, diceRadius,diceRadius)));
        role.add(new Role("", 1, true, new Boundary((float)60,67, diceRadius,diceRadius)));
        role.add(new Role("", 1, true, new Boundary((float)98,128, diceRadius,diceRadius)));
        shots = new LinkedList();
        shots.add(new Boundary(56, 185, diceRadius, diceRadius));
        shots.add(new Boundary(105, 185, diceRadius, diceRadius));
        shots.add(new Boundary(155, 185, diceRadius, diceRadius));
        sx = 34;
        sy = 215;
        sectors.add(new Sector(train,  shots, role, new Boundary(0,0,240,440), new Boundary(sx, sy, sx + sh, sy +sw))); 
        
        role = new LinkedList();
        role.add(new Role("", 3, true, new Boundary((float)500, 547, diceRadius,diceRadius)));
        role.add(new Role("", 2, true,new Boundary((float)500,625, diceRadius,diceRadius)));
        role.add(new Role("", 1, true,new Boundary((float)430, 625, diceRadius,diceRadius)));
        shots = new LinkedList();
        shots.add(new Boundary(486, 497, diceRadius, diceRadius));
        shots.add(new Boundary(537, 497, diceRadius, diceRadius));
        sx = 250;
        sy = 475;
        sectors.add(new Sector(ranch,  shots, role, new Boundary(224,440,600,690), new Boundary(sx, sy, sx + sw, sy +sh)));      
        
        role = new LinkedList();
        role.add(new Role("", 4, true,new Boundary((float)530, 818, diceRadius,diceRadius)));
        role.add(new Role("", 3, true,new Boundary((float)450, 818, diceRadius,diceRadius)));
        role.add(new Role("", 2, true,new Boundary((float)530,736, diceRadius,diceRadius)));
        role.add(new Role("", 1, true,new Boundary((float)450, 736, diceRadius,diceRadius)));
        shots = new LinkedList();
        shots.add(new Boundary(268, 766, diceRadius, diceRadius));
        shots.add(new Boundary(320, 766, diceRadius, diceRadius));
        shots.add(new Boundary(374, 766, diceRadius, diceRadius));
        sx = 37;
        sy = 723;
        sectors.add(new Sector(secret,  shots, role, new Boundary(0, 690,600, 888 ), new Boundary(sx, sy, sx + sw, sy +sh)));          
        
        role = new LinkedList();
        role.add(new Role("", 2, true, new Boundary((float)868, 821, diceRadius,diceRadius)));
        role.add(new Role("", 1, true, new Boundary((float)868,744, diceRadius,diceRadius)));
        shots = new LinkedList();
        shots.add(new Boundary(645, 693, diceRadius, diceRadius));
        shots.add(new Boundary(702, 693, diceRadius, diceRadius));
        sx = 618;
        sy = 724;
        sectors.add(new Sector(church,  shots, role, new Boundary(600,547,975,885), new Boundary(sx, sy, sx + sw, sy +sh)));          
        
        //  Init adjacentSectors
        getSector(trailer).addAdjacentSector(getSector(mainStr));
        getSector(trailer).addAdjacentSector(getSector(saloon));
        getSector(trailer).addAdjacentSector(getSector(hotel));
        
        getSector(castingOff).addAdjacentSector(getSector(secret));
        getSector(castingOff).addAdjacentSector(getSector(ranch));
        getSector(castingOff).addAdjacentSector(getSector(train));
        
        getSector(saloon).addAdjacentSector(getSector(trailer));
        getSector(saloon).addAdjacentSector(getSector(mainStr));
        getSector(saloon).addAdjacentSector(getSector(generalStore));        
        getSector(saloon).addAdjacentSector(getSector(bank));
        
        getSector(hotel).addAdjacentSector(getSector(trailer));
        getSector(hotel).addAdjacentSector(getSector(church));
        getSector(hotel).addAdjacentSector(getSector(bank));
        
        getSector(mainStr).addAdjacentSector(getSector(trailer));
        getSector(mainStr).addAdjacentSector(getSector(saloon));
        getSector(mainStr).addAdjacentSector(getSector(jail));
        
        
        getSector(bank).addAdjacentSector(getSector(hotel));
        getSector(bank).addAdjacentSector(getSector(church));
        getSector(bank).addAdjacentSector(getSector(ranch));        
        getSector(bank).addAdjacentSector(getSector(saloon));
        
        getSector(jail).addAdjacentSector(getSector(train));
        getSector(jail).addAdjacentSector(getSector(generalStore));        
        getSector(jail).addAdjacentSector(getSector(mainStr));
        
        getSector(generalStore).addAdjacentSector(getSector(jail));
        getSector(generalStore).addAdjacentSector(getSector(train));
        getSector(generalStore).addAdjacentSector(getSector(ranch));        
        getSector(generalStore).addAdjacentSector(getSector(saloon));
        
        getSector(train).addAdjacentSector(getSector(jail));
        getSector(train).addAdjacentSector(getSector(generalStore));
        getSector(train).addAdjacentSector(getSector(castingOff)); 
        
        getSector(ranch).addAdjacentSector(getSector(generalStore));
        getSector(ranch).addAdjacentSector(getSector(bank));
        getSector(ranch).addAdjacentSector(getSector(castingOff));        
        getSector(ranch).addAdjacentSector(getSector(secret));        
        
        getSector(secret).addAdjacentSector(getSector(ranch));
        getSector(secret).addAdjacentSector(getSector(church));
        getSector(secret).addAdjacentSector(getSector(castingOff));           
        
        getSector(church).addAdjacentSector(getSector(bank));
        getSector(church).addAdjacentSector(getSector(hotel));
        getSector(church).addAdjacentSector(getSector(secret));
    }
    
    // Initialize scenes
    private void InitScenes()
    {
        Random rand = new Random();
        
        for( int i = 0; i < scene.length; i++)
        {
            List<Role> roles = new LinkedList();
            int numberOfRoles = 1 + rand.nextInt(3);

            for(int j = 0; j < numberOfRoles; j++)
                roles.add(new Role("", 1 + rand.nextInt(6), false));
            
            scene[i] = new Scene(i, 2 + rand.nextInt(5), roles);
        }
        
        // Shuffle the scene cards
        Shuffle(scene);
        
        assignScene(1);
    }

    // Shuffle
    private void Shuffle(Scene[] scene)
    {
        Random rand = new Random();
        for (int i = 0; i < scene.length; i++)
        {
            int l = rand.nextInt(scene.length-1);
            int r = rand.nextInt(scene.length-1);
            Scene temp = scene[l];
            scene[l]=scene[r];
            scene[r]=temp;
        }
    }
    
    
    // Play a single day
    public void Play()
    {
        for (int day = 0; day < numOfDays; day++) 
        {
            setGameForDay(day);

            while (!isDayEnded()) 
            {
                for (int i = 0; i < numOfPlayers; i++) 
                {
                    Actor player = players.get(i);
                    player.play();
                }
            }
        }
        
        wrapGame();
    }
    
  // Set the game day  
  private void setGameForDay(int day)
  {
   	assignScene(day);
   	for (int i = 0; i < numOfPlayers; i++)
   	{
   		Actor player = players.get(i);
   		player.reset(getSector(trailer));          
   	}
  }  

    
    // Initialize all the players
    private void buildPlayers ()
    {
        //Scanner input = new Scanner( System.in );
        players = new LinkedList<Actor>();
        Sector sector = getSector(trailer);
        Boundary  bond = sector.getBoundary();
                
        float luX = 1200;
        float luY = 660;
        float width = 280;
        float height = 240;
                
        int diceSize = 50;
        int radius = diceSize/2;
        float gapX = (width - diceSize*3)/4;
        float gapY = (height - diceSize*2)/3;
        
           
        Random rand = new Random();
        for (int i = 0, clr = 0; i < numOfPlayers; i++, clr+=45)
        {
           // System.out.print("Enter the name of player " + (i+1) + ": ");
            if(i<3)
                players.add(new Actor("", startRank, startCredit, null, new Boundary((float)(luX + (i+1)*gapX+diceSize*(0.5+i)), luY + gapY+diceSize/2 , radius, radius), clr ));
            else
                players.add(new Actor("", startRank, startCredit, null, new Boundary((float)(luX + (i-2)*gapX+diceSize*(0.5+(i-3))), luY + 2*gapY+diceSize*3/2 , radius, radius), clr  ));
        }
        

    }
    
   // Check whether the day has ended 
	private boolean isDayEnded()
	{
		int count = 0;
		for (int i = 0; i < sectors.size(); i++)
		{
			if (!sectors.get(i).SceneComplete())
			{
				count++;
				if (count > 1) return false;
			}
		}

		return true;
	}

   // Show the end game results
   private void wrapGame()
	{   
        System.out.println("--------------Game result ----------------------");

        System.out.println();
        int winnerscore = 0;
        String winner = "";
		for (int i = 0; i < players.size(); i++)
		{

            int score = players.get(i).getScore();
            if (winnerscore < score)
            {
                winnerscore= score;
                winner = players.get(i).name;
            }
            System.out.println("The players : "+ players.get(i) + " has score - " + score );
            players.get(i).getScore();
		}
        System.out.println("The winner wit score " + winnerscore + " is - " + winner);
        System.out.println("CONGRATS! " + winner);
	}

            
} // end Deadwood