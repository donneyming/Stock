/*
 * JTattoo CustomTextureSample (c) 2012 by MH Software-Entwicklung
 *
 * This sample shows how to modify the background textures for the
 * TextureLookAndFeel.
 */

package view;

import ui.TitleBorder;
import ui.textures.ImageHelper;
import util.Configure;
import util.DateChooser;
import util.Fund;
import util.GZQH;
import util.MongoDBUtil;
import util.StockCode;
import util.StockFund;

import impl.CreateJJJZFromSina;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTabbedPane;
import javax.swing.Timer;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import model.SysGloableValue;

import controll.GetGZQH;
import controll.UserInterface;


//cms  20160304
public class ProcessPanel extends JFrame implements ActionListener {
	private JButton searchBtn;
	private JComboBox modeCombo;
	private JLabel tagPath;
	private JLabel tagModeLbl;
	private JLabel previewLbl;
	private JTable resTable;
	private JTextField tagTxt;
	protected JPanel contentPanel = null;
	protected JSplitPane contentSplitPane = null;
	protected JPanel contentLayoutPanel = null;
	protected JScrollPane tablePane = null;
	protected JPanel controlPanel = null;
	protected JPanel showPanel = null;
	private Timer timer;
	protected JList lafList = null;
	protected SysGloableValue sysGV;
	protected Container contentPane;
	private ButtonGroup buttonGroup;
	private int choose = 1;
	protected UserInterface ui;
	protected JTabbedPane tab;
	private JButton configBtn;
	private JButton jjjzBtn;
	private JButton jslrBtn;

	private boolean DEBUG = true;

	protected List<StockCode> selectedList = new ArrayList();

	public ProcessPanel() {
		super("股指期货基金软件");

		contentPane = getContentPane();

		tab = new JTabbedPane(JTabbedPane.TOP);
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
				if (fc.showOpenDialog(ProcessPanel.this) == JFileChooser.APPROVE_OPTION) {
					JOptionPane
							.showMessageDialog(ProcessPanel.this,
									"Your selection: "
											+ fc.getSelectedFile().getName());
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
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
				KeyEvent.ALT_MASK));
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		menu.add(menuItem);
		menu.add(menuItem);
		menuBar.add(menu);
		JMenu menu2 = new JMenu("Config");
		menu.setMnemonic('C');
		JMenuItem menuItem2 = new JMenuItem("New");
		menuItem.setMnemonic('N');
		menuItem.setEnabled(false);
		menu2.add(menuItem);

		menuItem2 = new JMenuItem("Exit");
		menuItem.setMnemonic('x');
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
				KeyEvent.ALT_MASK));
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		menu2.add(menuItem);

		JMenu findOptionsMenu = new JMenu("Options");
		Icon atIcon = new ImageIcon("at.gif");
		findOptionsMenu.setIcon(atIcon);
		findOptionsMenu.setMnemonic(KeyEvent.VK_O);
		ButtonGroup directionGroup = new ButtonGroup();

		JRadioButtonMenuItem forwardMenuItem = new JRadioButtonMenuItem("新浪",
				true);
		forwardMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Configure.ConfirureNet = 1;
			}
		});
		forwardMenuItem.setMnemonic(KeyEvent.VK_X);
		findOptionsMenu.add(forwardMenuItem);
		directionGroup.add(forwardMenuItem);

		JRadioButtonMenuItem backMenuItem = new JRadioButtonMenuItem("集思录");
		// backMenuItem.addActionListener(menuListener);
		backMenuItem.setMnemonic(KeyEvent.VK_J);
		findOptionsMenu.add(backMenuItem);
		directionGroup.add(backMenuItem);
		backMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Configure.ConfirureNet = 2;
			}
		});

		findOptionsMenu.addSeparator();
		JRadioButtonMenuItem caseMenuItem = new JRadioButtonMenuItem("东方财富");
		// caseMenuItem.addActionListener(menuListener);
		caseMenuItem.setMnemonic(KeyEvent.VK_D);
		findOptionsMenu.add(caseMenuItem);
		directionGroup.add(caseMenuItem);
		caseMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Configure.ConfirureNet = 3;
			}
		});

		JRadioButtonMenuItem case2MenuItem = new JRadioButtonMenuItem("和讯");
		// caseMenuItem.addActionListener(menuListener);
		case2MenuItem.setMnemonic(KeyEvent.VK_H);
		findOptionsMenu.add(case2MenuItem);
		directionGroup.add(case2MenuItem);
		case2MenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Configure.ConfirureNet = 4;
			}
		});

		menu2.add(findOptionsMenu);
		menuBar.add(menu2);

		setJMenuBar(menuBar);

		sysGV = SysGloableValue.getGloableValue();
		ui = new UserInterface(sysGV);
		// panel分成上下两部分：
		// 上面是控件；下面是表格
		controlPanel = new JPanel(new FlowLayout());
		JLabel label1 = new JLabel("请输入采集时间");
		JTextArea textArea2 = new JTextArea("14:45,14:50,14:55");
		JLabel label2 = new JLabel("(多个时间以；隔开)");
		JButton jb1, jb2, jb3;
		jb1 = new JButton("启动");
		jb1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				ui.configure(Configure.ConfirureNet);

				ui.initTaskList();

				// TableGenerator tg = new TableGenerator(resTable, ui);

				// tg.execute();
			}
		});
		// jb1.addActionListener(this);
		jb2 = new JButton("停止");
		// jb2.addActionListener(this);
		jb3 = new JButton("立刻采集");
		jb3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ui.configure(Configure.ConfirureNet);
				TableGenerator tg = new TableGenerator(resTable, ui);
				tg.execute();
			}
		});
		controlPanel.add(label1);
		controlPanel.add(textArea2);
		controlPanel.add(label2);

		controlPanel.add(jb1);
		controlPanel.add(jb2);
		controlPanel.add(jb3);

		contentPane.setLayout(new BorderLayout());
		contentPane.add(controlPanel, BorderLayout.NORTH); // 北边

		resTable = new JTable(10, 3);
		tablePane = new JScrollPane(resTable);
		contentPane.add(tablePane, BorderLayout.CENTER); // 北边

		contentPane.add(tablePane, BorderLayout.CENTER); // 北边

		showPanel = new JPanel(new FlowLayout());
		tagPath = new JLabel(Configure.writeFilePath + ";"
				+ Configure.writeFilePath2);
		showPanel.add(tagPath);
		contentPane.add(showPanel, BorderLayout.SOUTH); // 南边

		// /////////////////////////////////////////////////
		final JPanel p2 = new JPanel(new BorderLayout());// 基础配置
		configBtn = new JButton("选择监控的股票代码，写入数据库");
		configBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 写入数据库
				MongoDBUtil dbUtil = MongoDBUtil.instance;
				dbUtil.dropCollection(Configure.MoniterDateTable);
				for (int i = 0; i < selectedList.size(); i++) {
					dbUtil.insert(Configure.MoniterDateTable,
							selectedList.get(i).StockFund2DbObject());
				}
			}
		});
		p2.add(configBtn, BorderLayout.NORTH);

		jjjzBtn = new JButton("获取基金净值");
		jjjzBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 写入数据库
				MongoDBUtil dbUtil = MongoDBUtil.instance;
				for (int i = 0; i < sysGV.fundList.size(); i++) {
					CreateJJJZFromSina cfs = new CreateJJJZFromSina();
					StockFund sf = sysGV.fundList.get(i);
					Fund f = cfs.getFundFromNet(sf.motherfund.getCode());
					if (f != null)
						dbUtil.insert(Configure.DailyDateJZTable,
								f.Fund2DBObj());
				}
			}
		});
		p2.add(jjjzBtn, BorderLayout.SOUTH);
		MyTableModel myModel = new MyTableModel();// myModel存放表格的数据
		JCheckBox jc1 = new JCheckBox();

		JTable baseDataTable = new JTable(myModel);
		baseDataTable.getColumnModel().getColumn(5)
				.setCellEditor(new DefaultCellEditor(jc1));
		JScrollPane baseDataTablePane = new JScrollPane(baseDataTable);
		p2.add(baseDataTablePane);

		// /////////////////////////////////////////////////

		// /////////////////////////////////////////////////
		final JPanel p3 = new JPanel(new BorderLayout());// 计算利润
	DefaultTableModel profitModel;// = (DefaultTableModel) ui.cmptProfit();

		final JTable profitTable  = new JTable();
		jslrBtn = new JButton("实时计算利润");
		jslrBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel mdl = (DefaultTableModel) ui.cmptProfit();
				profitTable.setModel(mdl);
			}
		});

		p3.add(jslrBtn, BorderLayout.NORTH);

		p3.add(profitTable);

		// /////////////////////////////////////////////////
		// /////////////////////////////////////////////////
		final JPanel p4 = new JPanel(new BorderLayout());// 统计分析
		JPanel contentLayoutPanel2 = new JPanel(new FlowLayout());
		JLabel startlbl = new JLabel("选择日期");
		final JTextField showDate1 = new JTextField("单击选择开始日期");
		//JLabel endlbl = new JLabel("结束日期");
		//final JTextField showDate2 = new JTextField("单击选择结束日期");
		JButton  jsearchBtn = new  JButton("统计分析");
		
		JButton  excelBtn = new  JButton("导出excel");
		//DefaultTableModel mdl = (DefaultTableModel) ui.summary(null,null);
		//final JTable tongjiModelTable = new JTable(mdl);
		jsearchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ui.summary2(showDate1.getText());
			//	DefaultTableModel mdl = (DefaultTableModel) ui.summary(showDate1.getText(),showDate1.getText());

				//if(mdl !=null)
				//tongjiModelTable.setModel(mdl);
			}
		});
		excelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
           //导出excel
			}
		});
		DateChooser dateChooser1 = DateChooser.getInstance("yyyy-MM-dd");
	//	DateChooser dateChooser2 = DateChooser.getInstance("yyyy-MM-dd");

		dateChooser1.register(showDate1);
//		dateChooser2.register(showDate2);

		contentLayoutPanel2.add(startlbl);
		contentLayoutPanel2.add(showDate1);
	//	contentLayoutPanel2.add(endlbl);
	//	contentLayoutPanel2.add(showDate2);
		contentLayoutPanel2.add(jsearchBtn);
		contentLayoutPanel2.add(excelBtn);

		//contentLayoutPanel2.add(tongjiModelTable);

		p4.add(contentLayoutPanel2, BorderLayout.NORTH);

		JPanel contentLayoutPanel3 = new JPanel(new FlowLayout());
		//contentLayoutPanel3.add(tongjiModelTable);
		
		p4.add(contentLayoutPanel3,BorderLayout.CENTER);
		// /////////////////////////////////////////////////

		tab.add(p2, "基础配置");

		tab.add(contentPane, "系统监控");
		tab.add(p3, "计算利润");
		tab.add(p4, "统计分析");

		String[] listData = new String[] { "IF1603:3047.6", "IC1603:5465.0",
				"IH1603:2085.4" };

		timer = new Timer(5000, this);
		timer.start();

		lafList = new JList(listData);
		JScrollPane lafScrollPane = new JScrollPane(lafList);
		lafScrollPane.setBorder(new TitleBorder("股指期货"));
		lafScrollPane.setMinimumSize(new Dimension(120, 80));

		contentSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
				lafScrollPane, tab);
		contentSplitPane.setDividerLocation(140);

		contentLayoutPanel = new JPanel(new BorderLayout());
		contentLayoutPanel.setBorder(BorderFactory
				.createEmptyBorder(4, 4, 4, 4));
		contentLayoutPanel.add(contentSplitPane, BorderLayout.CENTER);

		setContentPane(contentLayoutPanel);

		// Add listeners
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// 增加配置文件
		Configure.getgzqhzsListFilePath();
		Configure.getStockFundListFilePath();

	} // end CTor

	class MyTableModel extends AbstractTableModel {
		// 表格中第一行所要显示的内容存放在字符串数组columnNames中
		final String[] columnNames = { "基金代码", "基金名称", "A代码", "B代码", "比例", "选择" };
		Object[][] data = new Object[143][6];

		public MyTableModel() {
			String[][] tmp = ui.readFromDB();
			// 表格中各行的内容保存在二维数组data中
			for (int i = 0; i < tmp.length; i++) {
				int size = tmp.length;
				int size2 = tmp[i].length;
				for (int j = 0; j < tmp[i].length; j++) {
					data[i][j] = tmp[i][j];
				}

				data[i][5] = new Boolean(false);
			}
		}

		// 获得列的数目
		public int getColumnCount() {
			return columnNames.length;
		}

		// 获得行的数目
		public int getRowCount() {
			return data.length;
		}

		// 获得某列的名字，而目前各列的名字保存在字符串数组columnNames中
		public String getColumnName(int col) {
			return columnNames[col];
		}

		// 获得某行某列的数据，而数据保存在对象数组data中
		public Object getValueAt(int row, int col) {
			return data[row][col];
		}

		// 判断每个单元格的类型
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}

		// 将表格声明为可编辑的
		public boolean isCellEditable(int row, int col) {

			if (col < 2) {
				return false;
			} else {
				return true;
			}
		}

		// 改变某个数据的值
		public void setValueAt(Object value, int row, int col) {
			if (DEBUG) {
				System.err.println("Setting value at " + row + "," + col
						+ " to " + value + " (an instance of "
						+ value.getClass() + ")");
			}

			if (data[0][col] instanceof Integer && !(value instanceof Integer)) {
				try {
					data[row][col] = new Integer(value.toString());
					fireTableCellUpdated(row, col);
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(ProcessPanel.this, "The \""
							+ getColumnName(col)
							+ "\" column accepts only integer values.");
				}
			} else {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}

			try

			{
				if (data[row][col].toString() == "true") {
					// 添加到数据列表
					StockCode sc = new StockCode();
					sc.setMotherCode(data[row][0].toString());
					sc.setCodeName(data[row][1].toString());
					sc.setACode(data[row][2].toString());
					sc.setBCode(data[row][3].toString());
					sc.setPortion(data[row][4].toString());

					selectedList.add(sc);
				} else {
					for (int i = 0; i < selectedList.size(); i++) {
						if (selectedList.get(i).getMotherCode() == data[row][0]
								.toString())
							selectedList.remove(i);
					}
				}

			} catch (Exception ex) {

			}
			if (DEBUG) {
				System.out.println("New value of data:");
				printDebugData();
			}

		}

		private void printDebugData() {
			int numRows = getRowCount();
			int numCols = getColumnCount();

			for (int i = 0; i < numRows; i++) {
				System.out.print(" row " + i + ":");
				for (int j = 0; j < numCols; j++) {
					System.out.print(" " + data[i][j]);
				}
				System.out.println();
			}
			System.out.println("--------------------------");
		}
	}

	// ------------------------------------------------------------------------------
	public static void main(String[] args) {
		// ------------------------------------------------------------------------------
		try {
			// Setup the look and feel properties

			// Select the Look and Feel
			UIManager
					.setLookAndFeel("com.jtattoo.plaf.texture.TextureLookAndFeel");

			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					// Start the application
					ProcessPanel app = new ProcessPanel();
					app.setSize(800, 600);
					app.setLocationRelativeTo(null);
					app.setVisible(true);
				}
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	} // end main

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
		  GetGZQH get = new GetGZQH(); String path =
		  Configure.getgzqhzsListFilePath(); DefaultListModel defaultListModel
		  = new DefaultListModel();
		 String []result  =  get.getQHZS(path);
		  defaultListModel.addElement(result[0]);
		  defaultListModel.addElement(result[1]);
		  defaultListModel.addElement(result[2]);
		 lafList.setModel(defaultListModel);
		 
		 GZQH gz = new GZQH();
		 Double  []result2  = get.getQHZS2(path,gz.code);
		 gz.ifzs =result2[0];
		 gz.iczs =result2[1];
		 gz.ihzs =result2[2];

		 MongoDBUtil dbUtil = MongoDBUtil.instance;

		 
		 dbUtil.insert(Configure.GZQHDateTable, gz.GZQH2DbObject());
		// 判断当前时间，启动采集线程；

		// ui.collect();

	}

}