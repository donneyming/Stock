/*
 * JTattoo SimpleThemeSample (c) 2012 by MH Software-Entwicklung
 *
 * This sample shows how to create a custom theme
 */
package ui;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Properties;
import javax.swing.BorderFactory;
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

public class SimpleThemeSample extends JFrame {

    public SimpleThemeSample() {
        super("SimpleThemeSample");

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
                if (fc.showOpenDialog(SimpleThemeSample.this) == JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(SimpleThemeSample.this, "Your selection: " + fc.getSelectedFile().getName());
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
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 4, 4, 4));
        JScrollPane treeScrollPane = new JScrollPane(new JTree());
        JScrollPane textAreaScrollPane = new JScrollPane(new JTextArea("Hello World"));
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, treeScrollPane, textAreaScrollPane);
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
            props.put("logoString", "my company");

            props.put("backgroundPattern", "off");

            props.put("windowTitleForegroundColor", "228 228 255");
            props.put("windowTitleBackgroundColor", "0 0 96");
            props.put("windowTitleColorLight", "0 0 96");
            props.put("windowTitleColorDark", "0 0 64");
            props.put("windowBorderColor", "96 96 64");

            props.put("windowInactiveTitleForegroundColor", "228 228 255");
            props.put("windowInactiveTitleBackgroundColor", "0 0 96");
            props.put("windowInactiveTitleColorLight", "0 0 96");
            props.put("windowInactiveTitleColorDark", "0 0 64");
            props.put("windowInactiveBorderColor", "32 32 128");

            props.put("menuForegroundColor", "228 228 255");
            props.put("menuBackgroundColor", "0 0 64");
            props.put("menuSelectionForegroundColor", "0 0 0");
            props.put("menuSelectionBackgroundColor", "255 192 48");
            props.put("menuColorLight", "32 32 128");
            props.put("menuColorDark", "16 16 96");

            props.put("toolbarColorLight", "32 32 128");
            props.put("toolbarColorDark", "16 16 96");

            props.put("controlForegroundColor", "228 228 255");
            props.put("controlBackgroundColor", "16 16 96");
            props.put("controlColorLight", "16 16 96");
            props.put("controlColorDark", "8 8 64");
            props.put("controlHighlightColor", "32 32 128");
            props.put("controlShadowColor", "16 16 64");
            props.put("controlDarkShadowColor", "8 8 32");

            props.put("buttonForegroundColor", "0 0 32");
            props.put("buttonBackgroundColor", "196 196 196");
            props.put("buttonColorLight", "196 196 240");
            props.put("buttonColorDark", "164 164 228");

            props.put("foregroundColor", "228 228 255");
            props.put("backgroundColor", "0 0 64");
            props.put("backgroundColorLight", "16 16 96");
            props.put("backgroundColorDark", "8 8 64");
            props.put("alterBackgroundColor", "255 0 0");
            props.put("frameColor", "96 96 64");
            props.put("gridColor", "96 96 64");
            props.put("focusCellColor", "240 0 0");

            props.put("disabledForegroundColor", "128 128 164");
            props.put("disabledBackgroundColor", "0 0 72");

            props.put("selectionForegroundColor", "0 0 0");
            props.put("selectionBackgroundColor", "196 148 16");

            props.put("inputForegroundColor", "228 228 255");
            props.put("inputBackgroundColor", "0 0 96");

            props.put("rolloverColor", "240 168 0");
            props.put("rolloverColorLight", "240 168 0");
            props.put("rolloverColorDark", "196 137 0");

            // Set your theme
            com.jtattoo.plaf.noire.NoireLookAndFeel.setCurrentTheme(props);

            // select the look and feel
            UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    // Start the application
                    SimpleThemeSample app = new SimpleThemeSample();
                    app.setSize(400, 300);
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