//Instructions taken from https://www.wikihow.com/Play-Bloxorz

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class HelpPanel extends Screen implements ActionListener
{
   private JPanel[] pages=new JPanel[8];
   private int pageIndex=0;
   private JButton left,right;
   
   public HelpPanel()
   {
      setLayout(new BorderLayout());
      
      JButton back=new JButton("Back to Menu");
      back.addActionListener(this);
      back.setFont(new Font(Font.DIALOG_INPUT,Font.BOLD,30));
      add(back,BorderLayout.NORTH);
      
      left=new JButton("Previous");
      left.addActionListener(this);
      left.setFont(new Font(Font.DIALOG_INPUT,Font.BOLD,30));
      add(left,BorderLayout.WEST);
      
      right=new JButton("Next");
      right.addActionListener(this);
      right.setFont(new Font(Font.DIALOG_INPUT,Font.BOLD,30));
      add(right,BorderLayout.EAST);
      
      
      String[] texts=new String[pages.length];
                                //.   
      texts[0]="The aim of the game\n"+
               "is to get the block\n"+
               "to fall into the\n"+
               "square hole at the\n"+
               "end of each stage.\n"+
               "There are an infite\n"+
               "number of stages to\n"+
               "complete, each one\n"+
               "randomly generated\n"+
               "from an integer\n"+
               "seed.";
                                //. 
      texts[1]="To move the block\n"+
               "around the world,\n"+
               "use the left,\n"+
               "right, up and down\n"+
               "arrow keys or W, A,\n"+
               "S, and D. Be\n"+
               "careful not to fall\n"+
               "off the edges. The\n"+
               "level will be\n"+
               "restarted if this\n"+
               "happens.";
                                //.
      texts[2]="Bridges and\n"+
               "switches are\n"+
               "located in many\n"+
               "levels. The\n"+
               "switches are\n"+
               "activated when they\n"+
               "are pressed down by\n"+
               "the block. You do\n"+
               "not need to stay\n"+
               "resting on the\n"+
               "switch to keep\n"+
               "bridges closed.";
                                //.
      texts[3]="There are two types\n"+
               "of switches:\n"+
               "'Heavy' X-shaped\n"+
               "switches and 'soft'\n"+
               "octagon switches.\n"+
               "Soft switches\n"+
               "(octagons) are\n"+
               "activated when any\n"+
               "part of your block\n"+
               "presses it. Hard\n"+
               "switches (x's)\n"+
               "require much more\n"+
               "pressure, so your\n"+
               "block must be\n"+
               "standing on its end\n"+
               "to activate them.";
                                //.
      texts[4]="When activated,\n"+
               "each switch may\n"+
               "behave differently.\n"+
               "The 'hard' and\n"+
               "'soft' buttons will\n"+
               "swap bridges from\n"+
               "open to closed to\n"+
               "open each time it\n"+
               "is used.";
                                //.
      texts[5]="Orange tiles are\n"+
               "more fragile than\n"+
               "the rest of the\n"+
               "land. If your block\n"+
               "stands up\n"+
               "vertically on an\n"+
               "orange tile, the\n"+
               "tile will give way\n"+
               "and your block will\n"+
               "fall through.";
                                //.
      texts[6]="Finally, there is a\n"+
               "third type of\n"+
               "switch shaped like\n"+
               "this: ( ) It\n"+
               "teleports your\n"+
               "block to different\n"+
               "locations,\n"+
               "splitting it into\n"+
               "two smaller blocks\n"+
               "at the same time.\n"+
               "These can be\n"+
               "controlled\n"+
               "individually and\n"+
               "will rejoin into a\n"+
               "normal block when\n"+
               "both are placed\n"+
               "next to each other.";
                                //.
      texts[7]="You can select\n"+
               "which small block\n"+
               "to use at any time\n"+
               "by pressing the\n"+
               "spacebar. Small\n"+
               "blocks can still\n"+
               "operate soft\n"+
               "switches, but they\n"+
               "aren't big enough\n"+
               "to activate heavy\n"+
               "switches. Also,\n"+
               "small blocks cannot\n"+
               "go through the exit\n"+
               "hole - only a\n"+
               "complete block can\n"+
               "finish the stage.";
                                //.
      
      for(int i=0;i<pages.length;i++)
      {
         pages[i]=new JPanel();
         pages[i].setLayout(new GridLayout(1,2));
         
         JTextArea info=new JTextArea("Page "+(i+1)+" of "+pages.length+"\n\n"+texts[i]);
         info.setLineWrap(true);
         info.setFont(new Font(Font.DIALOG_INPUT,Font.BOLD,30));
         info.setBackground(getBackground());
         info.setForeground(Color.WHITE);
      
         pages[i].add(info);
         
         JLabel image=new JLabel(new ImageIcon("pics/Info "+(i+1)+".png"));
         image.setOpaque(true);
         image.setBackground(Color.BLACK);
         
         pages[i].add(image);
      }
      
      
      add(pages[pageIndex]);
      left.setEnabled(false);
   }
   
   @Override
   public void actionPerformed(ActionEvent e)
   {
      String id=e.getActionCommand();
      if(id.equals("Back to Menu"))
         setNext(new MainMenuPanel());
      else if(id.equals("Next"))
      {
         if(pageIndex<pages.length-1)
         {
            remove(pages[pageIndex]);
            invalidate();
            pageIndex++;
            add(pages[pageIndex]);
            revalidate();
            
            left.setEnabled(true);
            if(pageIndex==pages.length-1)
               right.setEnabled(false);
         }
      }
      else if(id.equals("Previous"))
      {
         if(pageIndex>0)
         {
            remove(pages[pageIndex]);
            invalidate();
            pageIndex--;
            add(pages[pageIndex]);
            revalidate();
            
            right.setEnabled(true);
            if(pageIndex==0)
               left.setEnabled(false);
         }
      }
   }
}