/*
 * JTattoo BrandingSample (c) 2013 by MH Software-Entwicklung
 *
 * This sample shows what is to be done for branding your app. This means to replace the text
 * which is showing on the left side of pop-up menus.
 */

package ui;

import java.awt.BorderLayout;
import java.util.Properties;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author Michael Hagen
 */
public class BrandingSample extends BaseSampleFrame {
    
    public BrandingSample() {
        super("BrandingSample");
        JTextArea textArea = new JTextArea("Open the file menu and look at the text\nwich is rendered in the box on the left\nside of the pop-up");
        JScrollPane textAreaScrollPane = new JScrollPane(textArea);
        contentPanel.add(textAreaScrollPane, BorderLayout.CENTER);
    }
    
    /**
     * This method is called from BaseSampleFrame.setLookAndFeel to prepare
     * the look and feel properties.
     * 
     * @return the look and feel properties
     */
    @Override
    public Properties getLAFProps() {
        Properties props = super.getLAFProps();
        
        // This property sets the text which will be rendered on the left side of pop-up menus.
        props.put("logoString", "brand new");

        // To remove the bar painted in pop-up menus you have to put an empty string for the logo string
        // props.put("logoString", "");

        // To paint an empty bar just set a non breakeable space for logo string
        // props.put("logoString", "\u00A0");
        
        return props;
    }
    
//------------------------------------------------------------------------------    
    public static void main(String[] args) {
//------------------------------------------------------------------------------    
        try {
            // Create a property object 
            Properties props = new Properties();

            // This property sets the text which will be rendered on the left side of pop-up menus.
            props.put("logoString", "brand new");

            // To remove the bar painted in pop-up menus you have to put an empty string for the logo string
            // props.put("logoString", "");

            // To paint an empty bar just set a non breakeable space for logo string
            // props.put("logoString", "\u00A0");
            
            // Set the theme
            com.jtattoo.plaf.acryl.AcrylLookAndFeel.setCurrentTheme(props);

            // Select the Look and Feel
            UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    // Start the application
                    BrandingSample app = new BrandingSample();
                    app.setSize(400, 300);
                    app.setLocationRelativeTo(null);
                    app.setVisible(true);
                }
            });
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    } // end main
}
