import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainMenuPanel extends Screen implements ActionListener
{
   public MainMenuPanel()
   {
      super();
      setLayout(new GridLayout(2,1));
      
      JLabel title=new JLabel("BLOXORZ",JLabel.CENTER);
      title.setBackground(getBackground());
      title.setForeground(Color.WHITE);
      title.setFont(new Font(Font.DIALOG_INPUT,Font.BOLD,200));
      
      add(title);
      
      JPanel buttons=new JPanel();
      buttons.setBackground(getBackground());
      buttons.setLayout(new FlowLayout(FlowLayout.CENTER));
      
      JButton play=new JButton("Play");
      play.addActionListener(this);
      play.setFont(new Font(Font.DIALOG_INPUT,Font.BOLD,30));
      buttons.add(play);
      
      JButton help=new JButton("Help");
      help.addActionListener(this);
      help.setFont(new Font(Font.DIALOG_INPUT,Font.BOLD,30));
      buttons.add(help);
      
      JButton quit=new JButton("Quit");
      quit.addActionListener(this);
      quit.setFont(new Font(Font.DIALOG_INPUT,Font.BOLD,30));
      buttons.add(quit);
      
      add(buttons);
   }
   
   @Override
   public void actionPerformed(ActionEvent e)
   {
      String id=e.getActionCommand();
      if(id.equals("Play"))
      {
         int seed;
         while(true)
         {
            try
            {
               String input=JOptionPane.showInputDialog("Enter Level Seed:");
               if(input==null)
                  return;
               else
                  seed = Integer.parseInt(input);
               break;
            }
            catch(Exception ex)
            {
               JOptionPane.showMessageDialog(null,"Enter an integer.","Input ERROR",JOptionPane.ERROR_MESSAGE);
            }
         }
         
         
         setNext(new BloxorzPanel(seed));
      }
      else if(id.equals("Help"))
         setNext(new HelpPanel());
      else if(id.equals("Quit"))
         setRunning(false);
   }
}