//Dipesh Manandhar 5/3/18

//Rules:
//http://www.papou.byethost9.com/games/bloxorz.html?i=1
//https://www.wikihow.com/Play-Bloxorz

//Check it out:
//http://users.isc.tuc.gr/~agrammenos/web/


/*
 * Check List: (DONE!!!)
 * 
 * 5 points- DONE
    *    Map is drawn in an axonometric projection (isometric, dimetric, or trimetric) view; movement of player is aligned with axonometric axes
    * 5 points- DONE
    *    Player accurately rotates on every move
    * 5 points- DONE
    *    Player loses if falls off edge
    * 5 points- DONE
    *    Keeps track of number of moves made to solve each puzzle
    * 5 points- DONE
    *    Checks if player won by standing on exit hole- must be standing vertically
    * 10 points- DONE
    *    “Soft” O-Shaped button functionality works (to open/close a bridge)
    * 10 points- DONE
    *    “Heavy” X-Shaped button functionality works (to open/close a bridge)
    * 5 points- DONE
    *    “Fragile” Orange Tiles functionality works (breaks when standing vertically)
    * 10 points- DONE
    *    “Split” tile splits & teleports block
    * 5 points- DONE
    *    Spacebar functionality to switch which split block to control works
    * 5 points- DONE
    *    Random (solvable) map generation- meaning infinite levels
    * 
 * 
 * 
 * 
 * 
 */



import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.WindowEvent;

public class Driver extends JFrame
{
   private static final long NANOSECONDS_IN_A_SECOND=1000000000;
   
   private Screen panel=new MainMenuPanel();
   
   public Driver()
   {
      super("BLOXORZ");
      
      
      setContentPane(panel);
      pack();
      setSize(1000,1000);
      setLocationRelativeTo(null);
      setResizable(false);
      
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      //setExtendedState(MAXIMIZED_BOTH);
      setVisible(true);
      
      Renderer.loadImages();
      
      
      runGameLoop();
   }
   private void runGameLoop()
   {
      JFrame temp=this;
      Thread game=
         new Thread()
         {
            @Override
            public void run()
            {
               gameLoop();
               dispose();
               
               JOptionPane.showMessageDialog(new JOptionPane(),
                                             "On my honor as a member of the Woodson HS Community,\n"+
                                             "I, Dipesh Manandhar, certify that I have neither given \n"+
                                             "nor received unauthorized aid on this assignment, \n"+
                                             "that I have cited my sources for authorized aid, and \n"+
                                             "that this project was started on or after April 18, 2018.",
                                             "Honor Code",
                                             JOptionPane.INFORMATION_MESSAGE);
               
               dispatchEvent(new WindowEvent(temp,WindowEvent.WINDOW_CLOSING));
            }
         };
      game.start();
   }
   private void gameLoop()
   {
      final int TARGET_UPS=60;
      final long TARGET_UPDATE_TIME=NANOSECONDS_IN_A_SECOND/TARGET_UPS;
      final int TARGET_FPS=60;
      final long TARGET_FRAME_TIME=NANOSECONDS_IN_A_SECOND/TARGET_FPS;
      long prevFrameTime=System.nanoTime();
      long accumulator=0;
      long prevUpdateTime=prevFrameTime;
      
      long prevFPSTime=prevFrameTime,prevUPSTime=prevFrameTime;
      int frames=0,updates=0;
      
      while(panel.isRunning())
      {
         if(panel.getNext()!=null)
         {
            boolean wasBloxorzPanel=panel instanceof BloxorzPanel;
            
            getContentPane().remove(panel);
            getContentPane().invalidate();
            panel=panel.getNext();
            setContentPane(panel);
            getContentPane().revalidate();
            panel.requestFocus();
            
            if(panel instanceof BloxorzPanel)
               setExtendedState(MAXIMIZED_BOTH);
            else if(wasBloxorzPanel)
            {
               setVisible(false);
               pack();
               setSize(1000,1000);
               setLocationRelativeTo(null);
               //setResizable(false);
               setVisible(true);
            }
         }
         
         
         double interpolation=0;
         long now=System.nanoTime();
         accumulator+=now-prevFrameTime;
         while(accumulator>TARGET_UPDATE_TIME)
         {
            prevUpdateTime=System.nanoTime();
            if(!panel.isPaused())
               panel.update();
            updates++;
            
            long timeDiff=prevUpdateTime-prevUPSTime;
            if(timeDiff>=NANOSECONDS_IN_A_SECOND)
            {
               //panel.setUps((int)(updates*LightingPanel.NANOSECONDS_IN_A_SECOND/timeDiff));
               panel.setUps(updates);
               updates=0;
               prevUPSTime=prevUpdateTime;
            }
            
            accumulator-=TARGET_UPDATE_TIME;
         }
         now=System.nanoTime();
         interpolation=(double)(now-prevFrameTime)/TARGET_FRAME_TIME;
         //System.out.println(interpolation);
         if(panel.isPaused())
            interpolation=0;
         
         
         panel.render(interpolation);
         now=System.nanoTime();
         prevFrameTime=now;
         frames++;
         
         long timeDiff=prevFrameTime-prevFPSTime;
         if(timeDiff>=NANOSECONDS_IN_A_SECOND)
         {
            //panel.setFps((int)(frames*LightingPanel.NANOSECONDS_IN_A_SECOND/timeDiff));
            panel.setFps(frames);
            frames=0;
            prevFPSTime=prevFrameTime;
         }
         
         
         while(now-prevFrameTime<TARGET_FRAME_TIME && now-prevFrameTime<TARGET_UPDATE_TIME)
         {
            Thread.yield();
            now=System.nanoTime();
         }
         /*
         if(tempPanel!=null)
         {
            if(panel.isPaused())
            {
               panel.pause();
               TanksSound.play();
            }
         }
         */
      }
   }
   public static void main(String[] arg)
   {
      new Driver();
   }
}