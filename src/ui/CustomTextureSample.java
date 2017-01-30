/*
 * JTattoo CustomTextureSample (c) 2012 by MH Software-Entwicklung
 *
 * This sample shows how to modify the background textures for the
 * TextureLookAndFeel.
 */

package ui;

import ui.textures.ImageHelper;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class CustomTextureSample extends JFrame {

    public CustomTextureSample() {
        super("CustomTextureSample");

        // Setup menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        menu.setMnemonic('F');
        JMenuItem menuItem = new JMenuItem("New");
        menuItem.setMnemonic('N');
        menuItem.setEnabled(false);
        menu.add(menuItem);
        menuItem = new JMenuItem("Open...");
        menuItem.setMnemonic('O');
        menuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(CustomTextureSample.this) == JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(CustomTextureSample.this, "Your selection: " + fc.getSelectedFile().getName());
                }
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Save");
        menuItem.setMnemonic('S');
        menuItem.setEnabled(false);
        menu.add(menuItem);
        menuItem = new JMenuItem("Save as");
        menuItem.setMnemonic('a');
        menuItem.setEnabled(false);
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Exit");
        menuItem.setMnemonic('x');
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_MASK));
        menuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        menu.add(menuItem);
        menu.add(menuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // Setup widgets
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
        JScrollPane westPanel = new JScrollPane(new JTree());
        JTextArea textArea = new JTextArea("Hello World");
        JScrollPane eastPanel = new JScrollPane(textArea);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, westPanel, eastPanel);
        splitPane.setDividerLocation(148);
        contentPanel.add(splitPane, BorderLayout.CENTER);
        setContentPane(contentPanel);

        // Add listeners
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

    } // end CTor


//------------------------------------------------------------------------------    
    public static void main(String[] args) {
//------------------------------------------------------------------------------    
        try {
            // Setup the look and feel properties
            Properties props = new Properties();

            // Setup textures
            props.put("textureSet", "Custom");
            Icon texture = ImageHelper.loadImage("WindowTexture.jpg");
            if (texture != null) {
                props.put("windowTexture", texture);
            }
            texture = ImageHelper.loadImage("BackgroundTexture.jpg");
            if (texture != null) {
                props.put("backgroundTexture", texture);
            }
            texture = ImageHelper.loadImage("AlterBackgroundTexture.jpg");
            if (texture != null) {
                props.put("alterBackgroundTexture", texture);
            }
            texture = ImageHelper.loadImage("RolloverTexture.gif");
            if (texture != null) {
                props.put("rolloverTexture", texture);
            }
            texture = ImageHelper.loadImage("RolloverTexture.gif");
            if (texture != null) {
                props.put("selectedTexture", texture);
            }
            texture = ImageHelper.loadImage("AlterBackgroundTexture.jpg");
            if (texture != null) {
                props.put("pressedTexture", texture);
            }
            texture = ImageHelper.loadImage("BackgroundTexture.jpg");
            if (texture != null) {
                props.put("disabledTexture", texture);
            }
            texture = ImageHelper.loadImage("MenubarTexture.jpg");
            if (texture != null) {
                props.put("menubarTexture", texture);
            }

            props.setProperty("backgroundColor", "240 240 240");
            props.setProperty("backgroundColorLight", "220 220 220");
            props.setProperty("backgroundColorDark", "200 200 200");
            props.setProperty("alterBackgroundColor", "180 180 180");

            props.setProperty("frameColor", "164 164 164");
            props.setProperty("gridColor", "196 196 196");

            props.setProperty("disabledForegroundColor", "96 96 96");
            props.setProperty("disabledBackgroundColor", "240 240 240");

            props.setProperty("rolloverColor", "160 160 160");
            props.setProperty("rolloverColorLight", "230 230 230");
            props.setProperty("rolloverColorDark", "210 210 210");

            props.setProperty("controlBackgroundColor", "248 248 248");
            props.setProperty("controlShadowColor", "160 160 160");
            props.setProperty("controlDarkShadowColor", "110 110 110");
            props.setProperty("controlColorLight", "248 248 248");
            props.setProperty("controlColorDark", " 210 210 210");

            props.setProperty("buttonColorLight", "255 255 255");
            props.setProperty("buttonColorDark", "230 230 230");

            props.setProperty("menuBackgroundColor", "64 64 64");
            props.setProperty("menuColorLight", "96 96 96");
            props.setProperty("menuColorDark", "48 48 48");
            props.setProperty("menuSelectionBackgroundColor", "48 48 48");
            props.setProperty("toolbarBackgroundColor", "64 64 64");
            props.setProperty("toolbarColorLight", "96 96 96");
            props.setProperty("toolbarColorDark", "48 48 48");
            props.setProperty("desktopColor", "220 220 220");

            // Set your theme
            com.jtattoo.plaf.texture.TextureLookAndFeel.setCurrentTheme(props);

            // Select the Look and Feel
            UIManager.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    // Start the application
                    CustomTextureSample app = new CustomTextureSample();
                    app.setSize(600, 400);
                    app.setLocationRelativeTo(null);
                    app.setVisible(true);
                }
            });
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    } // end main

}