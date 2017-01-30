/*
 * JTattoo SimplePanelTextureSample (c) 2012 by MH Software-Entwicklung
 *
 * This sample shows how to style a panel with a background texture
 */

package ui;
import ui.textures.ImageHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author Michael Hagen
 */
public class SimplePanelTexture extends BaseSampleFrame {
    
    public SimplePanelTexture() {
        super("SimplePanelTexture");
        
        // Setup the controls
        JPanel textAreaPanel = new JPanel(new BorderLayout());
        // Just put an icon as a client property to the panel. This icon is used by the panel to render the background.
        textAreaPanel.putClientProperty("backgroundTexture", ImageHelper.loadImage("BackgroundTexture.jpg"));
        // Setup the textArea
        JTextArea textArea = new JTextArea("What you see is a text area\nencapsulated in a panel and\nstyled with a background\ntexture.\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nfin...");
        // Set the foreground to black. If we won't do this it will look strange on dark look and feels like HiFi.
        textArea.setForeground(Color.black);
        // Use larger font.
        textArea.setFont(new Font("Dialog", Font.PLAIN, 14));
        // The textarea must be transparent to see the texture.
        textArea.setOpaque(false);
        // Some space arround the textarea looks better.
        textArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        // Add the textarea to the panel
        textAreaPanel.add(textArea, BorderLayout.CENTER);
        // Add the texture panel to a scrollpane
        JScrollPane textAreaScrollPane = new JScrollPane(textAreaPanel);
        // The scroll mode of the viewport must be simple. Otherwise the low level bit blit operations breaks the texture.
        textAreaScrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
        
        contentPanel.add(textAreaScrollPane, BorderLayout.CENTER);
        
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
                    SimplePanelTexture app = new SimplePanelTexture();
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
