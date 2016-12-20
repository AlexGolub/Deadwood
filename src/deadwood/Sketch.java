/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deadwood;
import processing.core.*;
import java.util.*;
/**
 *
 * @author Alex
 */
public class Sketch extends PApplet {
    
    PImage img;  
    PFont font;
    float bx;
    float by;
    int boxSize = 35;
    boolean overBox = false;
    boolean locked = false;
    float xOffset = (float) 0.0; 
    float yOffset = (float) 0.0; 
    Deadwood dw =null;
    int numPlayers = 0;
    Actor currentPlayer = null;
    Actor clickPlayer = null;
    Boundary act =null;
    Boundary rehearse =null;
    
    private void initDeadwood()
    {
        if(dw != null) return;
        rectMode(CENTER);
        fill(50);
        rect(width/2, height/2, 500, 100);
        fill(250);     
        text("Enter number of players (2 - 6): " + key, width/2 -150, height/2 );
        
        if(numPlayers<2 || numPlayers>6) return;
        dw=new Deadwood(numPlayers);
        
    }
    
   
    
    public void settings() 
    {
        size(1500, 893);
        
    }
    
    public void setup ()
    {
        img = loadImage("Board.jpg");
        //image(img, 0, 0);
        
        bx = (float) (width/2.0);
        by = (float) (height/2.0);
        rectMode(RADIUS); 
        font = createFont("Arial", 24);
        textFont(font);

        act = new Boundary(1220, 40, 1220 + 140, 40 +(int)(140/4));
        rehearse = new Boundary(1220, 100, 1220 + 140, 100 + (int)(140/4));
        
    }
    
    public void draw()
    {
         background(50);
         image(img, 0, 0);
         
         drawDashboardDice(2);
         drawDashboardInfo();
                           
         initDeadwood();
         locateObject();
         drawScenes();
         drawDashboardPlayers();
         drawCompleteView();
         
    }
    
    
    private void locateObject()
    {     
        if(dw == null) return;
        if(clickPlayer != null) return;
        
        List<Actor> players = dw.players;
        for(int i = 0; i < players.size(); i++)
        {
            Actor player = players.get(i);
            if (player.boundary.isInBoundary(mouseX, mouseY))
            {
                clickPlayer = player;
                xOffset = 0;
                yOffset = 0;
                return;
            }
        }
    }
    

    public void mousePressed() 
    {
    
        if(clickPlayer != null)
        {
            locked = true;
            //fill(255, 255, 255);
            xOffset = mouseX - clickPlayer.boundary.centerX;
            yOffset = mouseY - clickPlayer.boundary.centerY;
            currentPlayer = clickPlayer;
        } else {
            locked = false;
        }

        if(act.isInBoundary(mouseX,mouseY)) 
        {
                rollDice();
        }
        if(rehearse.isInBoundary(mouseX,mouseY)) 
        {
            doRehearse();
        }
    }

    public void mouseDragged() 
    {

        if(locked && clickPlayer != null)
        {
           bx = mouseX - xOffset;
           by = mouseY - yOffset;
           clickPlayer.boundary.centerX = bx;
           clickPlayer.boundary.centerY = by;

        }
        else if (locked) {
            xOffset=0;
            yOffset=0;
        }
        
    }
    
    
    public void mouseClicked() 
    {
        if(currentPlayer == null || currentPlayer.getSector() == null ||
                !currentPlayer.getSector().getName().equalsIgnoreCase(Deadwood.castingOff))
            return;
        
        currentPlayer.doUpgarde(mouseX, mouseY);
    }

    public void mouseReleased() 
    {
        if(dw==null) return;
       locked = false;
       Sector cursector = getSector(mouseX, mouseY);
       if(clickPlayer != null && cursector != null && !currentPlayer.isWorking())
       {
          clickPlayer.move(cursector);
           Role role = IsOverStarringRole(cursector, mouseX, mouseY);
           if(role == null)
               role = IsOverExtrasRole(cursector, mouseX, mouseY);
           if (role != null && role.getRank() <= clickPlayer.rank && role.IsFree)
               clickPlayer.setRole(role);
        }
        clickPlayer = null;
    }
    
    private void drawDashboardDice(int side)
    {
        float luX = 1200;
        float luY = 20;
        float width = 280;
        float height = 140;
        fill(255);
        font = createFont("Arial", 24);
        textFont(font);

        fill(150);
        rectMode(CORNER);
        rect(luX, luY, width, height, 30);
        if(currentPlayer != null && currentPlayer.isWorking())
            fill(0, 140, 224);
        else
             fill(140);

        rect(act.upperLeftX, act.upperLeftY, act.width, act.height, 30);
        rect(rehearse.upperLeftX, rehearse.upperLeftY, rehearse.width, rehearse.height, 30);
        if(currentPlayer != null && currentPlayer.isWorking())
            fill(255);
        else
             fill(190);
        text( "Act", luX + 60, luY + 45);
        text( "Reherse", luX + 35, luY + 105);
        if(currentPlayer != null )
            drawDice(luX + width- 50, luY + height/2 , currentPlayer.dice, 245, 245, 200);
        else
            drawDice(luX + width- 50, luY + height/2 , side, 245, 245, 200);
    }
    
    
    private void drawDashboardInfo()
    {
        float luX = 1200;
        float luY = 200;
        float width = 280;
        float height = 400;
        font = createFont("Arial", 28);
        textFont(font);

        fill(200);
        rectMode(CORNER);
        rect(luX, luY, width, height, 30);
        
        fill(0);
        text( "Player info", luX + 30, luY + 30);
        font = createFont("Arial", 18);
        if(currentPlayer == null)    return;
        
        textFont(font);
      
        text( "Player:   " , luX + 35, luY + 30 * 2);
        text( "Money:  " + currentPlayer.money, luX + 35, luY + 30 * 3);
        text( "Credit:  " + currentPlayer.credits, luX + 35, luY + 30 * 4);
        text( "Rank:  " + currentPlayer.rank, luX + 35, luY + 30 * 5);
        text( "Reaherse bonus:  " + currentPlayer.diceBonus, luX + 35, luY + 30 * 6);
        
        Actor player = currentPlayer;
        Boundary bnd = player.boundary;
        drawDice(luX + 200 , luY + 50 , player.rank, 255, player.color, 100);
      
        text( "X= " + mouseX + "   Y = " + mouseY, luX + 10, luY + height - 10);
    }
    
    private void drawDashboardPlayers()
    {
        float luX = 1200;
        float luY = 640;
        float width = 280;
        float height = 240;
        font = createFont("Arial", 24);
        textFont(font);

        fill(150);
        rectMode(CORNER);
        rect(luX, luY, width, height, 30);
        fill(255);

        text( "Players", luX + 20, luY + 20);
        
        if(dw == null) return;
        
        List<Actor> players = dw.players;
           
        drawPlayers();
    }
    
    private void drawPlayers()
    {
        if(dw == null) return;
        List<Actor> players = dw.players;
        for(int i=0; i<players.size(); i++)
        {
            Actor player = players.get(i);
            Boundary bnd = player.boundary;
            drawDice(bnd.centerX, bnd.centerY, player.rank, 255, player.color, 100);
        }
        //currentPlayer = clickPlayer;
    }
    private void drawDice(float x, float y,int side, int red, int green , int blu)
    {
         int diceSize = 50;
        
        //dice
          noStroke();

          fill(color(red, green, blu));

          rectMode(CENTER);
          float centerX = x ;
          float centerY = y ;
          rect(centerX, centerY, diceSize, diceSize, diceSize/5);

          //dots
          fill(50);

          if (side == 1 || side == 3 || side == 5)
            ellipse(centerX, centerY, diceSize/5, diceSize/5);
          if (side == 2 || side == 3 || side == 4 || side == 5 || side == 6) {
            ellipse(centerX - diceSize/4, centerY - diceSize/4, diceSize/5, diceSize/5);
            ellipse(centerX + diceSize/4, centerY + diceSize/4, diceSize/5, diceSize/5);
          }
          if (side == 4 || side == 5 || side == 6) {
            ellipse(centerX - diceSize/4, centerY + diceSize/4, diceSize/5, diceSize/5);
            ellipse(centerX + diceSize/4, centerY - diceSize/4, diceSize/5, diceSize/5);
          }
          if (side == 6) {
            ellipse(centerX, centerY - diceSize/4, diceSize/5, diceSize/5);
            ellipse(centerX, centerY + diceSize/4, diceSize/5, diceSize/5);
          }
    }
    
     public void keyPressed()
    {
        if(key >='2' && key<='6')
            numPlayers = (int)(key-'0');
    }
     
     private void rollDice()
     {
         if(currentPlayer != null && currentPlayer.isWorking())
         {
             if(currentPlayer.getSector() != null && !currentPlayer.getSector().SceneComplete())
             {
                Random rand = new Random();
                int dice = rand.nextInt((6 - 1) + 1) + 1; 
                boolean success = currentPlayer.act(dice);
                if (success)
                {
                    currentPlayer.getPay(success);
                    currentPlayer.getSector().removeShot();
                }
                
             }
        }
     }
     
     private void doRehearse()
     {
         if(currentPlayer != null && currentPlayer.isWorking())
         {
           currentPlayer.rehearse();
        }
     }
     
     
     private void drawScenes()
     {
         if(dw==null)   return;
         
         List<Sector> sectors = dw.sectors;
         for(int i =0; i<sectors.size(); i++)
         {
             drawScene(sectors.get(i).getScene());
         }
     }
     
     private void drawScene(Scene scene)
     {
         if(scene == null) return;
         
         Boundary bnd = scene.boundary;
         List<Role> roles = scene.getRoles();
         int budget = scene.getBudget();
         
         rectMode(CORNER);
         fill(180);
         rect((int)bnd.upperLeftX, (int)bnd.upperLeftY, (int)bnd.width, (int)bnd.height);
         fill(255,0,0);
         font = createFont("Arial", 20);
         textFont(font);
         text("Budget: $ " + budget, bnd.upperLeftX + 10, bnd.upperLeftY + 20);
                  
         float x,y ;
         
        for (int i = 0; i < roles.size(); i++)
        {
            Role role = roles.get(i);
            Boundary roleBnd = role.getBoundary();
            x = roleBnd.centerX;
            y = roleBnd.centerY;
                
            drawDice(x ,y , role.getRank(), 255, 250, 250);
        }
     }
     
    private Sector getSector(int mouseX, int mouseY)
    { 
        if(dw == null) return null;
        for(int i = 0 ; i < dw.sectors.size(); i++)
            if(IsOverSector(dw.sectors.get(i), mouseX,	mouseY))
                return dw.sectors.get(i);
        return null;
    }

    private boolean IsOverSector(Sector	s, int mouseX,	int mouseY)	
    {
        return	s.getBoundary().isInBoundary(mouseX, mouseY);
    }
    
    Role IsOverStarringRole(Sector s, int mouseX, int mouseY) 
    {
        if( s == null  || s.getScene() == null) return null;
        List<Role> roles =  s.getScene().getRoles();
        for (int i = 0; i <roles.size(); i++)
        {
            Boundary bond = roles.get(i).getBoundary();
            if (bond != null && bond.isInBoundary(mouseX, mouseY)) 
            {
                return roles.get(i);
            }
        }
        return null;
    }

    Role IsOverExtrasRole(Sector s, int mouseX, int mouseY)	
    {
        if( s == null ) return null;
        List<Role> roles =  s.getRoles();
        for (int i = 0; i < roles.size(); i++)
        {
           if (roles.get(i).getBoundary().isInBoundary(mouseX, mouseY)) 
           {
               return roles.get(i);
           }
        }
        return null;
    }


    boolean isOutAllowedRegion(Boundary gameboard, int mouseX, int mouseY)	
    {
        return	gameboard.isInBoundary(mouseX, mouseY);
    }
    void drawCompleteView()
    { 
        if(dw == null) return;
        List<Sector> sectors = dw.sectors;
        for( int i = 0 ; i < sectors.size(); i++)
        {
                 if(sectors.get(i).getScene() != null && sectors.get(i).SceneComplete())
                 {
                     drawCompleteView(sectors.get(i).getScene().boundary);
                 }
                 for( int j= sectors.get(i).shotCount; j< sectors.get(i).numberOfShots; j++)
                        drawCompleteView(sectors.get(i).shots.get(j)); 
        }
    }
    
    void drawCompleteView(Boundary bond)
    {
        stroke(255, 0, 0);
        strokeWeight(7); 
        line(bond.upperLeftX, bond.upperLeftY, bond.lowerRightX, bond.lowerRightY);
        line(bond.upperLeftX, bond.lowerRightY, bond.lowerRightX, bond.upperLeftY);
        strokeWeight(1);
        stroke(53);
    }
}
