//Dipesh Manandhar 5/18/18


public class Utilities extends BloxorzPanel
{
   public Utilities()
   {
      super(-1);
      //do nothing- constructor should never be used
   }
   
   //activates the tile player is currently standing on, if there is a button there
   public static void activateCurrTile(Player p)
   {
      if(p.isStanding())
      {
         int type=board[p.getRow()][p.getCol()]%(Renderer.PLAYER+1);
         int pointer1=board[p.getRow()][p.getCol()]/(Renderer.PLAYER+1);
         
         if(type==Renderer.SOFT_BUTTON_TILE)
            activateBridge(pointer1);
         else if(type==Renderer.HARD_BUTTON_TILE)
            activateBridge(pointer1);
         else if(type==Renderer.SPLIT_BUTTON_TILE)
         {
            int p1R=pointer1/board[0].length;
            int p1C=pointer1%board[0].length;
            
            int pointer2=board[p1R][p1C]/(Renderer.PLAYER+1);
            
            int p2R=pointer2/board[0].length;
            int p2C=pointer2%board[0].length;
            
            p.split(p1R,p1C,p2R,p2C);
         }
      }
      else
      {
         if(p.isSplit())
         {
            if(board[p.getControllingRow()][p.getControllingCol()]%(Renderer.PLAYER+1)==Renderer.SOFT_BUTTON_TILE)
               activateBridge(board[p.getControllingRow()][p.getControllingCol()]/(Renderer.PLAYER+1));
         }
         else
         {
            if(board[p.getRow()][p.getCol()]%(Renderer.PLAYER+1)==Renderer.SOFT_BUTTON_TILE)
               activateBridge(board[p.getRow()][p.getCol()]/(Renderer.PLAYER+1));
            if(board[p.getRow2()][p.getCol2()]%(Renderer.PLAYER+1)==Renderer.SOFT_BUTTON_TILE)
               activateBridge(board[p.getRow2()][p.getCol2()]/(Renderer.PLAYER+1));
         }
      }
      
   }
   private static void activateBridge(int pointer)
   {
      int pointerR=pointer/board[0].length;
      int pointerC=pointer%board[0].length;
            
      if(board[pointerR][pointerC]==-1)
         board[pointerR][pointerC]=Renderer.BRIDGE_TILE;
      else //if(board[pointerR][pointerC]==Renderer.EMPTY_TILE)
         board[pointerR][pointerC]=-1;
   }
}