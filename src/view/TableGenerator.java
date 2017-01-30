package view;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import model.SysGloableValue;

import controll.UserInterface;

public class TableGenerator extends
		SwingWorker<DefaultTableModel, UserInterface> {
	private UserInterface ui;
	private JTable resTable;
	public TableGenerator(JTable resTable, UserInterface ui) {
		this.ui = ui;
		this.resTable = resTable;
	}

	@Override
	protected DefaultTableModel doInBackground() throws Exception {
		// TODO Auto-generated method stub
		DefaultTableModel model = ui.collect();
		return model;
	}

	@Override
	protected void done() {
		try {
			DefaultTableModel mdl = get();
			if (mdl != null) {
				DefaultTableModel tableModel = (DefaultTableModel)resTable.getModel();
				tableModel.setRowCount(0);  
				resTable.setModel(mdl);
				resTable.setEnabled(true);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
