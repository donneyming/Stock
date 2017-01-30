/*
 * JTattoo MacStyleWindowDecorationSample (c) 2012 by MH Software-Entwicklung
 *
 * This sample shows how to set the mac style window decoration. In this case the window buttons
 * (closer, iconizer, minimizer) are painted on the left side of the titlebar and text will rendered
 * in the center of the titlebar.
 */

package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Properties;
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
public class MacStyleWindowDecorationSample extends BaseSampleFrame {
    
    public MacStyleWindowDecorationSample() {
        super("MacStyleWindowDecorationSample");
        JScrollPane treeScrollPane = new JScrollPane(new JTree());
        treeScrollPane.setMinimumSize(new Dimension(120, 80));
        JScrollPane textAreaScrollPane = new JScrollPane(new JTextArea("Hello World"));
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, treeScrollPane, textAreaScrollPane);
        splitPane.setDividerLocation(140);
        contentPanel.add(splitPane, BorderLayout.CENTER);
    }

    @Override
    public Properties getLAFProps() {
        Properties props = super.getLAFProps();
        // This property switches the layout style of the window titlebar.
        props.put("macStyleWindowDecoration", "on");
        return props;
    }
    
//------------------------------------------------------------------------------    
    public static void main(String[] args) {
//------------------------------------------------------------------------------    
        try {
            // Create a property object 
            Properties props = new Properties();

            // This property switches the layout style of the window titlebar.
            props.put("macStyleWindowDecoration", "on");
            
            // Set the theme
            com.jtattoo.plaf.acryl.AcrylLookAndFeel.setCurrentTheme(props);
            
            // Select the Look and Feel
            UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    // Start the application
                    MacStyleWindowDecorationSample app = new MacStyleWindowDecorationSample();
                    app.setSize(600, 400);
                    app.setLocationRelativeTo(null);
                    app.setVisible(true);
                }
            });
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // end main
    
}
