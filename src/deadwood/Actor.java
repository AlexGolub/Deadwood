/* File: Actor.java
 * 
 */
package deadwood;
import java.util.*;
/**
 *
 * @author Alex
 */
public class Actor {

    // Fields
    public int money;
    public int credits;
    public int rank;
    public int dice;
    public int diceBonus;
    private String occupation;
    public String name;
    public Role role;
    private Sector sector;
    static private Map<Integer, RankPrice> mapRankPrice;
    public Boundary boundary;
    public int color;

	
	// Structure for selling ranks
    private static class RankPrice
    {
        public RankPrice (int dol, int cred, Boundary bnd)
        {
            dollars = dol;
            credits = cred;            
            this.bnd = bnd;
        }
        public int dollars;
        public int credits;
        public Boundary bnd;
    }
    // Constructor
    public Actor (String _name, int _rank, int _credits, Sector _sector)
    {
        name = _name;
        rank = _rank;
        credits = _credits;
        money = 0;
        diceBonus = 0;
        sector = _sector;
        occupation = "Idle";
        dice = 2;
        
        
        // Initialize rank upgrade price
        int upX = 98;
        int upY = 535;
        int stepX = 80;
        int stepY = 21;
        mapRankPrice = new HashMap<>();
        mapRankPrice.put(2, new RankPrice(4,5, new Boundary(upX,upY,upX+stepX, upY + stepY )));
        mapRankPrice.put(3, new RankPrice(10,10, new Boundary(upX,upY+stepY,upX+stepX, upY + 2*stepY )));
        mapRankPrice.put(4, new RankPrice(18,15, new Boundary(upX,upY+2*stepY,upX+stepX, upY + 3*stepY )));
        mapRankPrice.put(5, new RankPrice(28,20, new Boundary(upX,upY+3*stepY,upX+stepX, upY + 4*stepY )));
        mapRankPrice.put(6, new RankPrice(40,25, new Boundary(upX,upY+4*stepY,upX+stepX, upY + 5*stepY )));
    }
    
     public Actor (String _name, int _rank, int _credits, Sector _sector, Boundary bnd, int _color)
    {
        this( _name, _rank, _credits, _sector);
        boundary = bnd;
        color = _color;
    }

    // Methods
    //////
    // Move to an ajdacent sector
    public void move (Sector moveTo)
    {
      if(this.sector != null) this.sector.removeActor(this);
        this.sector = moveTo;
	    occupation = "Idle";
        this.sector.addActor(this);
    }
    
    // Show info about a player
    public void printInfo ()
    {
        System.out.println("-PLAYER STATS-");
        System.out.println("Rank: " + rank);
        System.out.println("Money: " + money);
        System.out.println("Credits: " + credits);
        System.out.println("DiceBonus: " + diceBonus);        
        System.out.println("Current position: " + sector.getName());
        System.out.println("Current occupation: " + occupation);
        if(role != null)
            role.printInfo();
        else
            System.out.println("No role");
            
        System.out.println();
    }
    
    // Get the player's sector
    public Sector getSector ()
    {
        return sector;
    }    

    public boolean act ()
    {
        Random rand = new Random();
        this.dice = rand.nextInt((6 - 1) + 1) + 1;
        //System.out.println("You have rolled a " + dice);
       return this.dice + diceBonus >= sector.getScene().getBudget() ? true : false;
    }
    
    public boolean act (int dice)
    {
        this.dice = dice;
        //System.out.println("You have rolled a " + dice);
        if(sector != null && sector.getScene() != null)
         return this.dice + diceBonus >= sector.getScene().getBudget() ? true : false;
        return false;
    }
    // Get bonus to dice roll for reahearsing
    public void rehearse ()
    {
        this.diceBonus++;
    }
    
    public void SetBoundary(int x1, int y1, int x2, int y2)
    {
        boundary = new Boundary(x1, y1, x2, y2);
    }
    	
    public void SetBoundary(float x, float y, float radX, float radY)
    {
        boundary = new Boundary(x, y, radX, radY);
    }
    	
    // Wrap the scene
    public List<Integer> DiceWrap ()
    {
        int diceNumber = sector.getScene().getBudget();
        ArrayList<Integer> dices = new ArrayList<Integer>();
        Random rand = new Random();

        for (int i = 0; i < diceNumber; i++) 
        {
            dices.add(1 + rand.nextInt(5));
        }
        Collections.sort(dices, Collections.reverseOrder());
        return dices;
    }

    // Get the bonus payment
    public void getBonusPay (Dictionary<Integer,Integer> bonusMap)
    {
        if (role == null)
            return;
        int roleRank = role.getRank();

        if(role.isExtrasType())
            money = money + roleRank;
        else
            money = money + bonusMap.get(roleRank);
    }

    // Display the available action to the player
    public List<Integer> getAvailableAction()
    {
         List<Integer> actions = new LinkedList<>();
        switch (sector.getName()) {
            case "Trailer":
                actions.add(0);
                break;
            case "Casting Office":
                if("Idle".equals(occupation))
                {
                    actions.add(0);
                    actions.add(3);
                }
                else
                    actions.add(3);
                break;
            default:
                if ("Idle".equals(occupation) || role == null)
                {
                    actions.add(0);
                }   
                else if (sector.getAvailableRoles(rank).size() > 0 &&  sector.getScene() != null) {
                    actions.add(1);
                    actions.add(2);
            }   break;
        }
        return actions;
    }

   // Player selections
   public void play()
   {
        int choice = getActionChoice();
       switch (choice) {
           case 0: {
               boolean done = doMove();
               if (!done) {
                    doTakeRole();
               }
               break;
           }
           case 1:
               doAct();
               break;
           case 2:
               doReherse();
               break;
           case 3:
               doUpgarde();
               break;
           default:
       }

       System.out.println("Your turn has ended.");
   }

    // Check whether the player can move
    public boolean doMove()
    {
        Scanner input = new Scanner (System.in);
        sector.printAdjacentSectors();
        System.out.print("Where would you like to move: ");
        int moveTo = input.nextInt();

        while (sector.getAdjacentSectors().size() <= moveTo || moveTo < 0)
        {
            System.out.println("You can only move to an adjacent sector. Please enter #");
            moveTo = input.nextInt();
        }
        move(sector.getAdjacentSectors().get(moveTo));

        System.out.println("You are now at: " + sector.getName());

        sector.printInfo();

        if (sector.getAvailableRoles(rank).size() == 0)
        {
            return true;
        }
        else
            return false;
    }

    // Take a roll
    public void doTakeRole()
    {
        System.out.println("You are now at: " + sector.getName());

        if (sector.getAvailableRoles(rank).size() > 0)
        {
            Scanner input = new Scanner (System.in);
            System.out.print("Would you like to work on a role (y/n): ");
            String ans = input.next();
            while (!ans.equals("y") && !ans.equals("n"))
            {
                System.out.println("Answer y or n:  ");
                ans = input.next();
            }

            if (ans.equals("y"))
            {
                System.out.println("What role would you like to take. Available roles:");
                List<Role> availRoles =  sector.getAvailableRoles(rank);
                for(int i= 0; i< availRoles.size(); i++)
                {
                   System.out.println(i +". " +"Rank "+ availRoles.get(i).getRank()  + "-" + 
                           (availRoles.get(i).isExtrasType()? "Extras": "Starring"));
                }

                int roleId = input.nextInt();
                role = availRoles.get(roleId);
                if(role != null)
                {
                    occupation = "Working";
                    role.IsFree = false;
                }
            }
        }
        else
            System.out.println("Your rank is insufficient to work on a role");
        
    }
   // Check whether the player's acting was a success
	public void doAct()
	{
            boolean success = act();
            if(success)
            {
                System.out.println("Congrats! Your acting was successfull!");
                sector.removeShot();                        
            }
            else
                System.out.println("Sorry! Your acting was unsuccessfull!");
            getPay(success);	
	}
	
   // Get payment for the player
	public void getPay(boolean succsess)
	{
            if (role == null)
                return;
		if(role.isExtrasType() && succsess)
		{
                    money = money + 1;
                    credits = credits +1;
		}
		else if (!role.isExtrasType() && succsess)
		{
                    credits = credits + 2;
		}
		if(role.isExtrasType() && !succsess)
		{
                    money = money + 1;
		}
	}

   // Do the reaherse option
	public void doReherse()
	{
		rehearse();
	}

       // Do the reaherse option
	public void setRole(Role _role)
	{
            role = _role;
            occupation = "Working";
            role.IsFree = false;
	}
        
   // Player upgrade
	public void doUpgarde()
	{
		int availableRank = rank;

		for(int key : mapRankPrice.keySet())
		{
			if ((money >= mapRankPrice.get(key).dollars) || credits >= mapRankPrice.get(key).credits)
			{
				System.out.println("you can upgarde to:" +  key + "for dollars = " + mapRankPrice.get(key).dollars + "or credit" + mapRankPrice.get(key).credits );
				availableRank = key;
			}
		}

		if (rank == availableRank)
		{
			System.out.println("not enaough money to upgarde.");
			return;
		}

		System.out.println("What rank you want to upgarde to?");
      Scanner input = new Scanner (System.in);
		int ans = input.nextInt();
		while(ans > availableRank || ans < rank)
		{
			System.out.println("Invalid rank - you can be upgraded between" + rank + "and " + availableRank +". Try again...");
			ans = input.nextInt();
		}

		System.out.println("What you want to pay with?");
		System.out.println("1. money.");
		System.out.println("2. credit");
		int payMethod = input.nextInt();
		if (money >= mapRankPrice.get(ans).dollars || credits >= mapRankPrice.get(ans).credits)
		{
			rank = ans;
			if(payMethod == 1)
				money = money - mapRankPrice.get(rank).dollars;
			else
				credits = credits - mapRankPrice.get(rank).credits;

			System.out.println("CONGRATS!!!. You upgrade your rank to " + rank);
		}
	}
        
        public void doUpgarde(int mouseX, int mouseY)
	{
		int availableRank = rank;
                boolean ifMoney = false;
                for(int key : mapRankPrice.keySet())
		{
			
                    if(mapRankPrice.get(key).bnd.isInBoundary(mouseX, mouseY))
                    {
                            availableRank = key;	
                    }
                }
               
                if(availableRank <= rank)
                {
                    System.out.println("Rank must be less.");
                    return;
                }

                Boundary bnd = mapRankPrice.get(availableRank).bnd;
                
                ifMoney = mouseX < (bnd.upperLeftX + bnd.width/2);
                
                if(ifMoney)
                {
                    if (money < mapRankPrice.get(availableRank).dollars)
                    {
                        System.out.println("not enaough money to upgarde.");
			return;
                    }
                    money = money - mapRankPrice.get(availableRank).dollars;                  
                }
                else
                {
                     if (credits < mapRankPrice.get(availableRank).credits)
                    {
                        System.out.println("not enaough credit to upgarde.");
			return;
                    }
                    credits = credits - mapRankPrice.get(availableRank).credits;                
                }
                rank = availableRank;
	}

   // Reset the players positions to the default location
	public void reset(Sector _sector)
	{
		occupation= "Idle";
                move(_sector);
		role = null;
		diceBonus = 0;	
	}
   
   // Reset the dice bonus
	public void resetDiceBonus()
	{
		diceBonus = 0;
	}

   // Remove the role
	public void removeRole()
	{
		role = null;
	}

   // The choices that are available to the players
	public static String GetActionName(int id) 
   {
       switch (id) {
        case 0:
         return "Move";
        case 1:
         return "Act";
        case 2:
         return "Rehearse";
        case 3:
         return "Upgrade";
        default:
         return "Unknown action";
      }
   }
   
   // Check whether the	player's choice is valid
	private boolean IsvalidChoise(int choise, List<Integer> allowedActions)
	{
		return allowedActions.contains(choise);
	}

   // Get the choices in the form of integers
	public int getActionChoice()
	{
            Scanner input = new Scanner (System.in);
            List<Integer> allowedActions = getAvailableAction();
            int choice = 6;
            System.out.println();
            System.out.println(name + "'s " + "turn");
            printInfo();
            while (!IsvalidChoise(choice, allowedActions)) {
                for (int i = 0; i < allowedActions.size(); i++) {
                    System.out.println(allowedActions.get(i) + ". " + GetActionName(allowedActions.get(i)));
                }

                System.out.println();
                System.out.print("What will you do next: ");
                choice = input.nextInt();
            }
            return choice;

    }
    // Get the final score
    public int getScore()
    {
        return  money + credits + rank * 5;
    }
    
    public boolean isWorking()
    {
        return occupation != "Idle";
    }
    
} // end Actor
