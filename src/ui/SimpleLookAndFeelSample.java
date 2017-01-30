package ui;
/*
 * JTattoo SimpleLookAndFeelSample (c) 2012 by MH Software-Entwicklung
 *
 * This sample shows how to select a look and feel.
 */



import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


/**
 *
 * @author Michael Hagen
 */
public class SimpleLookAndFeelSample extends BaseSampleFrame {
    
    public SimpleLookAndFeelSample() {
        super("SimpleLookAndFeelSample");
        JScrollPane treeScrollPane = new JScrollPane(new JTree());
        treeScrollPane.setMinimumSize(new Dimension(120, 80));
        JScrollPane textAreaScrollPane = new JScrollPane(new JTextArea("Hello World"));
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, treeScrollPane, textAreaScrollPane);
        splitPane.setDividerLocation(140);
        contentPanel.add(splitPane, BorderLayout.CENTER);
    }

//------------------------------------------------------------------------------    
    public static void main(String[] args) {
//------------------------------------------------------------------------------    
        try {
            
            // Select the Look and Feel
            UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    // Start the application
                    final SimpleLookAndFeelSample app = new SimpleLookAndFeelSample();
                    app.setSize(600, 400);
                    app.setLocationRelativeTo(null);
                    
                    //app.setBounds(2000, 40, 800, 600);
                    //app.setExtendedState(MAXIMIZED_BOTH);
                    
                    app.setVisible(true);
                            
                }
            });
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // end main
    
}
