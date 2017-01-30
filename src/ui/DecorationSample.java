/*
 * JTattoo DecorationSample (c) 2012 by MH Software-Entwicklung
 *
 * This sample figures out how to customize the window decoration.
 * You can pass following arguments to the application:
 *
 * decoration:default - Application starts with JTattoo default window decorations
 * decoration:system - Application starts with system window decorations
 * decoration:none - Application does not have any decoration (no title and no border)
 *
 * If no argument is specified the application starts with default decoration.
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

public class DecorationSample extends JFrame {

    // Constants for application arguments
    private static final String DECORATION_ARG = "decoration:";
    private static final String DEFAULT_DECORATION = "default"; // if you wish the JTattoo window decorations
    private static final String SYSTEM_DECORATION = "system"; // if you wish the system window decorations
    private static final String NO_DECORATION = "none"; // if you do not want any window decorations

    private static String decorationStyle = DEFAULT_DECORATION;

    public DecorationSample() {
        super("DecorationSample");

        // If the decorationStyle is "none" we have to set the frame to undecorated
        if (NO_DECORATION.equals(decorationStyle)) {
            setUndecorated(true);
        }

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
                if (fc.showOpenDialog(DecorationSample.this) == JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(DecorationSample.this, "Your selection: " + fc.getSelectedFile().getName());
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
            for (String arg : args) {
                if (arg.startsWith(DECORATION_ARG)) {
                    String style = arg.substring(DECORATION_ARG.length());
                    if (DEFAULT_DECORATION.equals(style)) {
                        decorationStyle = style;
                    } else if (SYSTEM_DECORATION.equals(style)) {
                        decorationStyle = style;
                    } else if (NO_DECORATION.equals(style)) {
                        decorationStyle = style;
                    }
                }
            }

            // Setup the look and feel properties
            Properties props = new Properties();
            props.put("logoString", "my company");

            // If the decorationStyle is not "default" we have to switch off the windowDecoration property
            if (!DEFAULT_DECORATION.equals(decorationStyle)) {
                props.put("windowDecoration", "off");
            }

            // Set the theme
            com.jtattoo.plaf.hifi.HiFiLookAndFeel.setCurrentTheme(props);

            // Select the Look and Feel
            UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");

            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    // Start the application
                    DecorationSample app = new DecorationSample();
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