//Dipesh Manandhar 5/3/18

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Renderer extends BloxorzPanel
{
   public Renderer()
   {
      super(-1);
      //do nothing- constructor should never be used
   }
   //Static variables
   private static BufferedImage[] images=new BufferedImage[14];
   
   //Static Constants
   private static final double SCALE=1;
   
   private static final int TILE_HEIGHT=(int)(29*SCALE),
                            TILE_WIDTH=(int)(53*SCALE),
                            TILE_HEIGHT_R=(int)(22*SCALE),
                            TILE_HEIGHT_C=(int)(7*SCALE),
                            TILE_WIDTH_R=(int)(11*SCALE),
                            TILE_WIDTH_C=(int)(42*SCALE);
   protected static final int EMPTY_TILE=0,    //image indicies
                              END_TILE=1,
                              FRAGILE_TILE=2,
                              SOFT_BUTTON_TILE=3,
                              HARD_BUTTON_TILE=4,
                              SPLIT_BUTTON_TILE=5,
                              BRIDGE_TILE=6,
                              POINTER=7,
                              PLAYER_LEFT=8,
                              PLAYER_UP=9,
                              PLAYER_RIGHT=10,
                              PLAYER_DOWN=11,
                              PLAYER_ATOP=12,
                              PLAYER=13;
   
   
   //Mutators
   public static void loadImages()
   {
      images[EMPTY_TILE]=scaleImage(bufferImage("pics/Empty Tile.png"),SCALE,SCALE);
      images[END_TILE]=scaleImage(bufferImage("pics/End Tile.png"),SCALE,SCALE);
      images[FRAGILE_TILE]=scaleImage(bufferImage("pics/Fragile Tile.png"),SCALE,SCALE);
      images[SOFT_BUTTON_TILE]=scaleImage(bufferImage("pics/Soft Button Tile.png"),SCALE,SCALE);
      images[HARD_BUTTON_TILE]=scaleImage(bufferImage("pics/Hard Button Tile.png"),SCALE,SCALE);
      images[SPLIT_BUTTON_TILE]=scaleImage(bufferImage("pics/Split Button Tile.png"),SCALE,SCALE);
      images[BRIDGE_TILE]=scaleImage(bufferImage("pics/Bridge Tile.png"),SCALE,SCALE);
      
      images[POINTER]=scaleImage(bufferImage("pics/Pointer.png"),SCALE,SCALE);
      
      images[PLAYER]=scaleImage(bufferImage("pics/Block 0.png"),SCALE,SCALE);
      
      for(int i=0;i<5;i++)
         images[PLAYER_LEFT+i]=scaleImage(bufferImage("pics/Block "+(i+1)+".png"),SCALE,SCALE);
   }
   
   public static void drawBoard(Graphics g)
   {
      //draw all tiles
      for(int r=0;r<board.length;r++)
      {
         for(int c=board[0].length-1;c>=0;c--)
         {
            if(board[r][c]<0)
               continue;
            BufferedImage curr=images[board[r][c]%images.length];
            g.drawImage(curr,BOARD_ORIGIN_X+boardToX(r,c),BOARD_ORIGIN_Y+boardToY(r,c),null);
         }
      }
      
      
   }
   
   public static void drawPlayer(Graphics g,Player p)
   {
      BufferedImage curr=images[PLAYER];
      
      if(p.isSplit())
      {
         if(p.getRow()>p.getRow2() || p.getCol()<p.getCol2())
         {
            //draw block at playerR2,playerC2
            g.drawImage(curr,BOARD_ORIGIN_X+boardToX(p.getRow2(),p.getCol2()),BOARD_ORIGIN_Y+boardToY(p.getRow2(),p.getCol2())-(curr.getHeight()-TILE_HEIGHT),null);
            //draw block at playerR,playerC
            g.drawImage(curr,BOARD_ORIGIN_X+boardToX(p.getRow(),p.getCol()),BOARD_ORIGIN_Y+boardToY(p.getRow(),p.getCol())-(curr.getHeight()-TILE_HEIGHT),null);
            
            //draw pointer
            curr=images[POINTER];
            if(p.isControllingFirst())
               g.drawImage(curr,BOARD_ORIGIN_X+boardToX(p.getRow(),p.getCol()),BOARD_ORIGIN_Y+boardToY(p.getRow(),p.getCol())-(curr.getHeight()-TILE_HEIGHT),null);
            else
               g.drawImage(curr,BOARD_ORIGIN_X+boardToX(p.getRow2(),p.getCol2()),BOARD_ORIGIN_Y+boardToY(p.getRow2(),p.getCol2())-(curr.getHeight()-TILE_HEIGHT),null);
         }
         else
         {
            //draw block at playerR,playerC
            g.drawImage(curr,BOARD_ORIGIN_X+boardToX(p.getRow(),p.getCol()),BOARD_ORIGIN_Y+boardToY(p.getRow(),p.getCol())-(curr.getHeight()-TILE_HEIGHT),null);
            //draw block at playerR2,playerC2
            g.drawImage(curr,BOARD_ORIGIN_X+boardToX(p.getRow2(),p.getCol2()),BOARD_ORIGIN_Y+boardToY(p.getRow2(),p.getCol2())-(curr.getHeight()-TILE_HEIGHT),null);
            
            //draw pointer
            curr=images[POINTER];
            if(p.isControllingFirst())
               g.drawImage(curr,BOARD_ORIGIN_X+boardToX(p.getRow(),p.getCol()),BOARD_ORIGIN_Y+boardToY(p.getRow(),p.getCol())-(curr.getHeight()-TILE_HEIGHT),null);
            else
               g.drawImage(curr,BOARD_ORIGIN_X+boardToX(p.getRow2(),p.getCol2()),BOARD_ORIGIN_Y+boardToY(p.getRow2(),p.getCol2())-(curr.getHeight()-TILE_HEIGHT),null);
         }
      }
      else
      {
         //draw block at playerR,playerC
         g.drawImage(curr,BOARD_ORIGIN_X+boardToX(p.getRow(),p.getCol()),BOARD_ORIGIN_Y+boardToY(p.getRow(),p.getCol())-(curr.getHeight()-TILE_HEIGHT),null);
         
         //draw block at player
         if(p.isHorizontal())
         {
            curr=images[PLAYER_UP+p.getDc()];
            g.drawImage(curr,BOARD_ORIGIN_X+boardToX(p.getRow2(),p.getCol2()),BOARD_ORIGIN_Y+boardToY(p.getRow2(),p.getCol2())-(curr.getHeight()-TILE_HEIGHT),null);
         }
         else if(p.isVertical())
         {
            curr=images[PLAYER_RIGHT+p.getDr()];
            g.drawImage(curr,BOARD_ORIGIN_X+boardToX(p.getRow2(),p.getCol2()),BOARD_ORIGIN_Y+boardToY(p.getRow2(),p.getCol2())-(curr.getHeight()-TILE_HEIGHT),null);
         }
         else// if(p.isStanding())
         {
            curr=images[PLAYER_ATOP];
            g.drawImage(curr,BOARD_ORIGIN_X+boardToX(p.getRow(),p.getCol()),BOARD_ORIGIN_Y+boardToY(p.getRow(),p.getCol())-2*(curr.getHeight()-TILE_HEIGHT),null);
         }
      }
   }
   
   //Helpers
   private static BufferedImage bufferImage(String filename)
   {
      try
      {
         return ImageIO.read(new File(filename));
      }
      catch(Exception e)
      {
         e.printStackTrace();
         return null;
      }
   }
   private static BufferedImage scaleImage(BufferedImage old,double xScale,double yScale)
   {
      BufferedImage scaled=new BufferedImage((int)(xScale*old.getWidth()),(int)(yScale*old.getHeight()),BufferedImage.TYPE_INT_ARGB);
      Graphics g = scaled.getGraphics();
      g.drawImage(old,0,0,scaled.getWidth(),scaled.getHeight(),null);
      g.dispose();
      return scaled;
   }
   //post: returns (r,c)'s top left point in (x,y) from the origin of the board
   private static int boardToX(int r,int c)
   {
      return TILE_WIDTH_C*c+TILE_WIDTH_R*r;
   }
   private static int boardToY(int r,int c)
   {
      return TILE_HEIGHT_R*r-TILE_HEIGHT_C*(c+1);
   }
}