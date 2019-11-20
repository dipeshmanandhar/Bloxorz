//Dipesh Manandhar 5/8/18

import java.awt.Graphics;

public class Player
{
   //Instance Variables
   private int r,c,dr,dc;  //at any given time, (dr!=0 ^ dc!=0) || (dr==0 && dc==0)
   private boolean isSplit=false;
   private int r2,c2;   //for when block is split in half
   private boolean controllingFirst=true;
   
   //Constructors
   public Player(int r0,int c0)
   {
      this(r0,c0,0,0);
   }
   
   public Player(int r0,int c0,int dr0,int dc0)
   {
      this(r0,c0,dr0,dc0,false,-1,-1,true);
   }
   private Player(int r0,int c0,int dr0,int dc0,boolean iS,int r20,int c20,boolean cF)
   {
      r=r0;
      c=c0;
      dr=dr0;
      dc=dc0;
      isSplit=iS;
      r2=r20;
      c2=c20;
      controllingFirst=cF;
   }
   
   //Helpers
   private void combine()
   {
      int rDiff=Math.abs(r2-r);
      int cDiff=Math.abs(c2-c);
      if((rDiff==0 && cDiff==1) || (rDiff==1 && cDiff==0))
      {
         //combine halves
         isSplit=false;
         dr=r2-r;
         dc=c2-c;
      }
   }
   
   //Mutators
   
   //pre: ( (dr!=0 ^ dc!=0) || (dr==0 && dc==0) ) && dir<4
   //post: moves and rotates player in appropriate direction dir (up -> dir=0)
   //                                                            (right -> dir=1)
   //                                                            (down -> dir=2)
   //                                                            (left -> dir=3)
   public void move(int dir)
   {
      if(!isSplit)
      {
         if(dir==0)        //up
         {
            r--;
            if(dc!=0)
            {
            //do nothing
            }
            else if(dr!=0)
            {
               if(dr<0)
                  r--;
               dr=0;
            }
            else //if(dr==0 && dc==0)
               dr=-1;
         }
         else if(dir==1)   //right
         {
            c++;
            if(dr!=0)
            {
            //do nothing
            }
            else if(dc!=0)
            {
               if(dc>0)
                  c++;
               dc=0;
            }
            else //if(dr==0 && dc==0)
               dc=1;
         }
         else if(dir==2)   //down
         {
            r++;
            if(dc!=0)
            {
            //do nothing
            }
            else if(dr!=0)
            {
               if(dr>0)
                  r++;
               dr=0;
            }
            else //if(dr==0 && dc==0)
               dr=1;
         }
         else if(dir==3)   //left
         {
            c--;
            if(dr!=0)
            {
            //do nothing
            }
            else if(dc!=0)
            {
               if(dc<0)
                  c--;
               dc=0;
            }
            else //if(dr==0 && dc==0)
               dc=-1;
         }
      }
      else
      {
         if(controllingFirst)
         {
            if(dir%2==0)
               r+=dir-1;
            else
               c-=dir-2;
            
         }
         else
         {
            if(dir%2==0)
               r2+=dir-1;
            else
               c2-=dir-2;
         }
         
         combine();
      }
   }
   //pre: (dr!=0 ^ dc!=0) || (dr==0 && dc==0)
   public void draw(Graphics g)
   {
      Renderer.drawPlayer(g,this);
   }
   
   //pre: isSplit==false
   //post: stands up in one move, returns the direction moved to stanfd up (0~4) see move(int dir) above
   public int standUp()
   {
      int dir;
      
      if(isStanding())
         return -1;
      else if(dr!=0)
      {
         if(Math.min(r,r+dr)>0)
         {
            move(0); //up
            dir=0;
         }
         else
         {
            move(2); //down
            dir=2;
         }
      }
      else //if(dc!=0)
      {
         if(Math.min(c,c+dc)>0)
         {
            move(3); //left
            dir=3;
         }
         else
         {
            move(1); //right
            dir=1;
         }
      }
      return dir;
   }
   public void split(int row1,int col1,int row2,int col2)
   {
      r=row1;
      c=col1;
      r2=row2;
      c2=col2;
      dr=0;
      dc=0;
      isSplit=true;
      combine();
   }
   public void switchControl()
   {
      if(isSplit)
         controllingFirst=!controllingFirst;
   }
   
   //Getters
   //post: returns whether or not player is standing up
   public boolean isStanding()
   {
      return !isSplit && dr==0 && dc==0;
   }
   //post: returns whether or not player is horizontally oriented on board
   public boolean isHorizontal()
   {
      return !isSplit && dc!=0;
   }
   //post: returns whether or not player is vertically oriented on board
   public boolean isVertical()
   {
      return !isSplit && dr!=0;
   }
   
   public int getRow()
   {
      return r;
   }
   public int getCol()
   {
      return c;
   }
   
   public int getRow2()
   {
      if(isSplit)
         return r2;
      else
         return r+dr;
   }
   public int getCol2()
   {
      if(isSplit)
         return c2;
      else
         return c+dc;
   }
   
   public int getDr()
   {
      return dr;
   }
   public int getDc()
   {
      return dc;
   }
   
   //pre: ( (dr!=0 ^ dc!=0) || (dr==0 && dc==0) ) && dir<4
   //post: returns whether a movement in dir is valid, this is unchanged
   public boolean validMove(int dir)
   {
      Player temp=this.clone();
      temp.move(dir);
      return temp.isOnBoard();
   }
   private boolean isOnBoard()
   {
      if(isSplit)
         return r>=0 && r<BloxorzPanel.board.length && c>=0 && c<BloxorzPanel.board[0].length && r2>=0 && r2<BloxorzPanel.board.length && c2>=0 && c2<BloxorzPanel.board[0].length;
      else
         return !(r<0 || r>=BloxorzPanel.board.length || c<0 || c>=BloxorzPanel.board[0].length || r+dr<0 || r+dr>=BloxorzPanel.board.length || c+dc<0 || c+dc>=BloxorzPanel.board[0].length);
   }
   
   //pre: (dr!=0 ^ dc!=0) || (dr==0 && dc==0)
   public boolean isAlive()
   {
      if(!isOnBoard())
         return false;
      if(isStanding() && BloxorzPanel.board[r][c]%(Renderer.PLAYER+1)==Renderer.FRAGILE_TILE)
         return false;
      
      if(isSplit)
         return !(BloxorzPanel.board[r][c]==-1 || BloxorzPanel.board[r2][c2]==-1);
      else
         return !(BloxorzPanel.board[r][c]==-1 || BloxorzPanel.board[r+dr][c+dc]==-1);
   }
   //pre: ( (dr!=0 ^ dc!=0) || (dr==0 && dc==0) ) && (r,c) are valid indicies of BloxorzPanel.board
   public boolean hasWon()
   {
      return !isSplit && BloxorzPanel.board[r][c]%(Renderer.PLAYER+1)==Renderer.END_TILE && dr==0 && dc==0;
   }
   
   public boolean isSplit()
   {
      return isSplit;
   }
   public boolean isControllingFirst()
   {
      return controllingFirst;
   }
   //pre: isSplit==true
   public int getControllingRow()
   {
      if(controllingFirst)
         return r;
      else
         return r2;
   }
   //pre: isSplit==true
   public int getControllingCol()
   {
      if(controllingFirst)
         return c;
      else
         return c2;
   }
   @Override
   public String toString()
   {
      return "("+r+","+c+")"+(dr==0 && dc==0 ? "":" ("+(r+dr)+","+(c+dc)+")");
   }
   
   public Player clone()
   {
      return new Player(r,c,dr,dc,isSplit,r2,c2,controllingFirst);
   }
}