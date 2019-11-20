//Dipesh Manandhar 5/3/18

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;

import java.util.Stack;
import java.util.Random;

public class BloxorzPanel extends Screen
{
   //Static Constants
   protected static final int BOARD_ORIGIN_X=100,BOARD_ORIGIN_Y=200;
   private static final String MOVE_UP="move up",
                               MOVE_DOWN="move down",
                               MOVE_LEFT="move left",
                               MOVE_RIGHT="move right",
                               STOP_VERTICAL="stop vertical",
                               STOP_HORIZONTAL="stop horizontal",
                               SWITCH_CONTROL="switch control",
                               RESET_LEVEL="reset level",
                               BACK_TO_MAIN_MENU="main menu",
                               EXIT="exit";
   
   //Static Variables
   protected static int[][] board=new int[20][20];
   //protected static int playerR=0,playerC=0;
   
   //Instance Variables
   private boolean won=false,lost=false;
   private int numMoves=0,totalMoves=0;
   private Player player;
   private int level,levelsCompleted;
   
   
   //Constructors
   public BloxorzPanel(int seed)
   {
      super();
      
      
      level=seed;
      createLevel(level);
      
      setKeyBindings();
   }
   
   //Helpers
   private void gimp(String key,String name)
   {
      getInputMap().put(KeyStroke.getKeyStroke(key),name);
   }
   private void gamp(String name,AbstractAction action)
   {
      getActionMap().put(name,action);
   }
   private void setKeyBindings()
   {
      gimp("UP",MOVE_UP);
      gimp("W",MOVE_UP);
      gimp("DOWN",MOVE_DOWN);
      gimp("S",MOVE_DOWN);
      gimp("LEFT",MOVE_LEFT);
      gimp("A",MOVE_LEFT);
      gimp("RIGHT",MOVE_RIGHT);
      gimp("D",MOVE_RIGHT);
      gimp("ESCAPE",EXIT);
      gimp("SPACE",SWITCH_CONTROL);
      gimp("R",RESET_LEVEL);
      gimp("M",BACK_TO_MAIN_MENU);
      
      /*
      gimp("released UP",STOP_VERTICAL);
      gimp("released W",STOP_VERTICAL);
      gimp("released DOWN",STOP_VERTICAL);
      gimp("released S",STOP_VERTICAL);
      gimp("released LEFT",STOP_HORIZONTAL);
      gimp("released A",STOP_HORIZONTAL);
      gimp("released RIGHT",STOP_HORIZONTAL);
      gimp("released D",STOP_HORIZONTAL);
      */
      
      gamp(MOVE_UP,new MoveAction(0));
      gamp(MOVE_RIGHT,new MoveAction(1));
      gamp(MOVE_DOWN,new MoveAction(2));
      gamp(MOVE_LEFT,new MoveAction(3));
      gamp(SWITCH_CONTROL,new MoveAction(4));
      gamp(RESET_LEVEL,new MoveAction(5));
      gamp(BACK_TO_MAIN_MENU,new MoveAction(6));
      gamp(EXIT,new MoveAction(-1));
      
      /*
      gamp(STOP_VERTICAL,new MoveAction(4));
      gamp(STOP_HORIZONTAL,new MoveAction(5));
      */
   }
   private int positionX(Graphics g,String text,double mult)
   {
      FontMetrics fm = g.getFontMetrics();
      return (int)(getWidth()*mult - fm.stringWidth(text)*0.5);
      //int centerY = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
   }
   
   
   private void nextLevel()
   {
      if(level==Integer.MAX_VALUE)
         level=-1;
      else if(level==Integer.MIN_VALUE)
         level=0;
      else if(level>=0)
         level++;
      else
         level--;
      createLevel(level);
      //paused=false;
      won=false;
      //lost=false;
      //running=true;
      numMoves=0;
   }
   
   private void resetLevel()
   {
      createLevel(level);
      //paused=false;
      won=false;
      lost=false;
      //running=true;
      totalMoves-=numMoves;
      numMoves=0;
   }
   
   //uses random walk
   //pre: board.length>2, board[0].length>2
   //post: creates map and initializes player
   private void createLevel(int seed)
   {
      Random rng= new Random(seed);
      
      //first, clear board of all tiles
      for(int r=0;r<board.length;r++)
         for(int c=0;c<board[0].length;c++)
            board[r][c]=-1;
      
      //then, chose a random valid start point
      int r0=(int)(rng.nextDouble()*board.length);
      int c0=(int)(rng.nextDouble()*board[0].length);
      
      //create a Player for map generation
      player=new Player(r0,c0);
      
      Stack<Integer> moves=new Stack<Integer>();
      
      int walks=board.length*3;
      int maxButtons=(int)(rng.nextDouble()*(board.length/5+1)*Math.min(Math.abs(seed)/30.0,1));
      int currButtons=0;
      Stack<Integer> buttons=new Stack<Integer>();
      
      int maxSplits=(int)(rng.nextDouble()*(2+1)*Math.min(Math.abs(seed)/30.0,1));
      int currSplits=0;
      Stack<Integer> splits=new Stack<Integer>();
      
      for(int i=0;i<walks;i++)
      {
         int dir=(int)(rng.nextDouble()*4);
         while(!player.validMove(dir))
            dir=(dir+1)%4;
         
         player.move(dir);
         moves.push(dir);
         
         if(player.isStanding())
         {
            if(player.getRow()==r0 && player.getCol()==c0)
            {
               //Do nothing
            }
            else if(board[player.getRow()][player.getCol()]==-1 || board[player.getRow()][player.getCol()]==Renderer.FRAGILE_TILE || board[player.getRow()][player.getCol()]==Renderer.EMPTY_TILE)
            {
               double random=rng.nextDouble();
               if(random>=0.9 && currButtons<maxButtons && i<walks-Math.max(walks/(Math.max(currButtons,1)*2),1))
               {
                  board[player.getRow()][player.getCol()]=Renderer.HARD_BUTTON_TILE;
                  currButtons++;
                  buttons.push(player.getRow()*board[0].length+player.getCol());
               }
               else if(random>=0.7 && currSplits<maxSplits && i!=walks-1)
               {
                  board[player.getRow()][player.getCol()]=Renderer.SPLIT_BUTTON_TILE;
                  currSplits++;
                  //TODO - need to add 2 pointers of where to teleport 2 pieces to
                  splits.push(player.getRow()*board[0].length+player.getCol());
               }
               else
                  board[player.getRow()][player.getCol()]=Renderer.EMPTY_TILE;
                  
            }
         }
         else if((player.getRow()==r0 && player.getCol()==c0) || (player.getRow2()==r0 && player.getCol2()==c0))
         {
            if(board[player.getRow()][player.getCol()]==-1)
               board[player.getRow()][player.getCol()]=Renderer.EMPTY_TILE;
            if(board[player.getRow2()][player.getCol2()]==-1)
               board[player.getRow2()][player.getCol2()]=Renderer.EMPTY_TILE;
         }
         else
         {
            double random=rng.nextDouble();
            if(random<0.3)
            {
               if(board[player.getRow()][player.getCol()]==-1)
                  board[player.getRow()][player.getCol()]=Renderer.FRAGILE_TILE;
               if(board[player.getRow2()][player.getCol2()]==-1)
                  board[player.getRow2()][player.getCol2()]=Renderer.FRAGILE_TILE;
            }
            else
            {
               if(board[player.getRow()][player.getCol()]==-1 || board[player.getRow()][player.getCol()]==Renderer.FRAGILE_TILE)
                  board[player.getRow()][player.getCol()]=Renderer.EMPTY_TILE;
               if(board[player.getRow2()][player.getCol2()]==-1 || board[player.getRow2()][player.getCol2()]==Renderer.FRAGILE_TILE)
                  board[player.getRow2()][player.getCol2()]=Renderer.EMPTY_TILE;
            }
            if((board[player.getRow2()][player.getCol2()]==Renderer.EMPTY_TILE || board[player.getRow2()][player.getCol2()]==Renderer.FRAGILE_TILE) && 
               random>=0.9 && currButtons<maxButtons && i<walks-Math.max(walks/(Math.max(currButtons,1)*2),1))
            {
               //System.out.println(board[player.getRow2()][player.getCol2()]);
               board[player.getRow2()][player.getCol2()]=Renderer.SOFT_BUTTON_TILE;
               currButtons++;
               buttons.push(player.getRow2()*board[0].length+player.getCol2());
            }
         }
      }
      //System.out.println("Num Buttons: "+currButtons);
      //System.out.println("Num Splits: "+currSplits);
      
      int dir=player.standUp();
      if(dir>=0)
         moves.push(dir);
      
      board[player.getRow()][player.getCol()]=Renderer.END_TILE;
      
      
      
      //end tile is start tile, need to make end tile somwhere in middle of path walked
      if(board[r0][c0]==Renderer.END_TILE)
      {
         //System.out.println("End tile=start tile");
         //board[player.getRow()][player.getCol()]=1;
         for(int i=0;i<walks/2;i++)
         {
            dir=(moves.pop()+2)%4;
            player.move(dir);
         }
         
         dir=player.standUp();
         if(dir>=0)
            moves.push(dir);
         board[player.getRow()][player.getCol()]=Renderer.END_TILE;
         
         //if middle of path also is the start, retry from beginning, with a different seed
         if(r0==player.getRow() && c0==player.getCol())
         {
            createLevel(rng.nextInt());
            return;
         }
      }
      
      
      //now ensure tiles surrounding end tile are not empty
      for(int r=-1;r<=1;r++)
      {
         if(r+player.getRow()<0)
            continue;
         else if(r+player.getRow()>=board.length)
            break;
         for(int c=-1;c<=1;c++)
         {
            if(c+player.getCol()<0)
               continue;
            else if(c+player.getCol()>=board[0].length)
               break;
            else if(board[r+player.getRow()][c+player.getCol()]<0)
               board[r+player.getRow()][c+player.getCol()]=Renderer.EMPTY_TILE;
         }
      }
      
      
      //System.out.println(player);
      
      //at this point, at least one end tile is fixed in board
      
      //need to connect all buttons to a tile in the path, between the button and the end tile
      if(currButtons>0)
      {
         Stack<Integer> movesCopy=(Stack<Integer>)moves.clone();
         Player playerCopy=player.clone();
         
         //System.out.println(currButtons);
         int backsteps=Math.max(movesCopy.size()/(currButtons*2),1);  //for button bridge pointers
         Stack<Integer> pointers=new Stack<Integer>();
         
         for(int j=0;j<currButtons;j++)
         {
            for(int i=0;i<backsteps;i++)
            {
               playerCopy.move((movesCopy.pop()+2)%4);
               //System.out.println(playerCopy);
            }
            
            while(!(board[playerCopy.getRow()][playerCopy.getCol()]==Renderer.EMPTY_TILE || board[playerCopy.getRow()][playerCopy.getCol()]==Renderer.FRAGILE_TILE))
            {
               playerCopy.move((movesCopy.pop()+2)%4);
               //System.out.println(playerCopy);
            }
            
            int button=buttons.pop();
            int buttonR=button/board[0].length;
            int buttonC=button%board[0].length;
            
            //add pointer to button location on board
            
            if(board[buttonR][buttonC]!=Renderer.END_TILE)
            {
               pointers.push(playerCopy.getRow()*board[0].length+playerCopy.getCol());
               
               board[buttonR][buttonC]+=(Renderer.PLAYER+1)*pointers.peek();
            }
         }
         
         //deactivate pointed to tiles
         while(!pointers.isEmpty())
         {
            int pointer=pointers.pop();
            int r=pointer/board[0].length;
            int c=pointer%board[0].length;
            board[r][c]=-1;
         }
      }
      
      
      //need to connect all split buttons to 2 tiles in the path, anywhere (except on other special tiles, including on bridges, or on same spot or already combined)
      if(currSplits>0)
      {
         Stack<Integer> movesCopy=(Stack<Integer>)moves.clone();
         Player playerCopy=player.clone();
         
         while(!splits.isEmpty())
         {
            int backsteps=(int)(rng.nextDouble()*movesCopy.size()/(3*currSplits));
         
            for(int i=0;i<backsteps;i++)
            {
               playerCopy.move((movesCopy.pop()+2)%4);
            //System.out.println(playerCopy);
            }
         
            while(!(board[playerCopy.getRow()][playerCopy.getCol()]==Renderer.EMPTY_TILE || board[playerCopy.getRow()][playerCopy.getCol()]==Renderer.FRAGILE_TILE))
            {
               playerCopy.move((movesCopy.pop()+2)%4);
            //System.out.println(playerCopy);
            }
         
         
            int pointer1=playerCopy.getRow()*board[0].length+playerCopy.getCol();
            int p1R=playerCopy.getRow(),p1C=playerCopy.getCol();
            
            board[p1R][p1C]+=Renderer.PLAYER+1;
         
            backsteps=(int)(rng.nextDouble()*movesCopy.size()/(2*currSplits));
         
            for(int i=0;i<backsteps;i++)
            {
               playerCopy.move((movesCopy.pop()+2)%4);
            //System.out.println(playerCopy);
            }
            
            {
               int rDiff=Math.abs(playerCopy.getRow()-p1R);
               int cDiff=Math.abs(playerCopy.getCol()-p1C);
               while(!(board[playerCopy.getRow()][playerCopy.getCol()]==Renderer.EMPTY_TILE || board[playerCopy.getRow()][playerCopy.getCol()]==Renderer.FRAGILE_TILE) || 
                  ((rDiff==0 && cDiff==1) || (rDiff==1 && cDiff==0)))
               {
                  playerCopy.move((movesCopy.pop()+2)%4);
                  rDiff=Math.abs(playerCopy.getRow()-p1R);
                  cDiff=Math.abs(playerCopy.getCol()-p1C);
               //System.out.println(playerCopy);
               }
            }
            
            int pointer2=playerCopy.getRow()*board[0].length+playerCopy.getCol();
         
            int split=splits.pop();
            int splitR=split/board[0].length;
            int splitC=split%board[0].length;
            //System.out.println("Split Button: ("+splitR+","+splitC+")");
         
            board[splitR][splitC]+=(Renderer.PLAYER+1)*(pointer1);
            board[p1R][p1C]-=Renderer.PLAYER+1;
            board[p1R][p1C]+=(Renderer.PLAYER+1)*(pointer2);
         }
      }
      
      while(!moves.isEmpty())
      {
         player.move((moves.pop()+2)%4);
         //System.out.println(player);
      }
      
      
      board[r0][c0]=Renderer.EMPTY_TILE;
      
      player=new Player(r0,c0);
   }
   
   //Mutators
   @Override
   public void drawScene(Graphics g)
   {
      super.drawScene(g);
      Renderer.drawBoard(g);
      player.draw(g);
   }
   @Override
   public void drawUI(Graphics g)
   {
      super.drawUI(g);
      
      //draw info on top of everything
      g.setFont(new Font(Font.DIALOG_INPUT,Font.BOLD,75));
      if(won)
      {
         g.setColor(Color.GREEN.darker());
         String text="YOU WIN";
         g.drawString(text,positionX(g,text,0.5),85);
         
         g.setFont(new Font(Font.DIALOG_INPUT,Font.BOLD,30));
         text="(Press spacebar to continue)";
         g.drawString(text,positionX(g,text,0.5),85+40);
      }
      else if(lost)
      {
         g.setColor(Color.BLUE.darker());
         String text="YOU LOSE";
         g.drawString(text,positionX(g,text,0.5),85);
         
         g.setFont(new Font(Font.DIALOG_INPUT,Font.BOLD,30));
         text="(Press spacebar or 'R' to try again)";
         g.drawString(text,positionX(g,text,0.5),85+40);
      }
      
      g.setColor(Color.WHITE);
      g.setFont(new Font(Font.DIALOG_INPUT,Font.BOLD,20));
      
      //top left
      g.drawString("Level: "+level,50,50);
      g.drawString("Levels Completed: "+levelsCompleted,50,100);
      
      //top right
      g.drawString("Moves: "+numMoves,getWidth()*3/4,50);
      g.drawString("Total Moves: "+totalMoves,getWidth()*3/4,100);
      
      //middle right
      g.drawString("Controls:",getWidth()*3/4,200);
      g.drawString("  -WASD or arrow keys to move",getWidth()*3/4,250);
      g.drawString("  -R to restart level",getWidth()*3/4,300);
      g.drawString("  -Spacebar to switch control",getWidth()*3/4,350);
      g.drawString("   of split blocks",getWidth()*3/4,400);
      g.drawString("  -M to return to Main Menu",getWidth()*3/4,450);
      g.drawString("  -Escape to quit game",getWidth()*3/4,500);
   }
   
   //Keyboard events
   private class MoveAction extends AbstractAction
   {
      private int direction;
      private MoveAction(int dir)
      {
         direction=dir;
      }
      @Override
      public void actionPerformed(ActionEvent e)
      {
         if(won)
         {
            if(direction<0)
               setRunning(false);
            else if(direction==4)   //space bar - next level
               nextLevel();
            else if(direction==5)   //'R' - reset level
            {
               resetLevel();
               levelsCompleted--;
            }
            else if(direction==6)   //'M' - main menu
               setNext(new MainMenuPanel());
         }
         else if(lost)
         {
            if(direction<0)
               setRunning(false);
            else if(direction==4 || direction==5)   //space bar or 'R' - reset level
               resetLevel();
            else if(direction==6)   //'M' - main menu
               setNext(new MainMenuPanel());
         }
         else
         {
            if(direction<0)
               setRunning(false);
            else if(direction<4)
            {
               player.move(direction);
            
            //System.out.println(board[player.getRow()][player.getCol()]);
            
               if(!won && !lost)
               {
                  numMoves++;
                  totalMoves++;
                  if(player.isAlive())
                     Utilities.activateCurrTile(player);
               }
            
               if(!player.isAlive())
               {
                  lost=true;
                  won=false;
               }
               else if(player.hasWon() && !lost)
               {
                  won=true;
                  levelsCompleted++;
               }
            }
            else if(direction==4)
               player.switchControl();
            else if(direction==5)   //'R' - reset level
               resetLevel();
            else if(direction==6)   //'M' - main menu
               setNext(new MainMenuPanel());
         }
      }
   }
}