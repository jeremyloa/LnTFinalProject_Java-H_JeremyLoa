package main;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class Main extends JFrame implements ActionListener{
	Connect con;
	ResultSet res;
	Vector<Vector<Object>> selected;
	JPanel top, centerMain, centerForm, centerInputPanel, centerButtonPanel, centerTablePanel, bottom;
	JLabel title;
	JButton menuInsert, menuUpdate, menuDelete, menuClear;
	JLabel lblKode, lblNama, lblHarga, lblStok;
	JTextField txtKode, txtNama, txtHarga, txtStok;
	DefaultTableModel model;
	JTable table;
	JScrollPane tableScroll;
	SpringLayout formSpring;
	public Main() {
		con = new Connect();
		selected = new Vector<Vector<Object>>();
		initGUI();
	}
	
	public void initGUI() {
		setTitle("PT Pudding");
		setSize(800,600);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		top = new JPanel();
		title = new JLabel("PT Pudding", SwingConstants.CENTER);
		title.setFont(new Font("Sans Serif", Font.BOLD, 32));
		top.add(title);
		top.setBorder(new EmptyBorder(10,20,10,20));
		
		centerMain = new JPanel();
		initCenterForm();
		initCenterView();
		centerMain.add(centerForm);
		centerMain.add(centerTablePanel);
		centerMain.setLayout(new BoxLayout(centerMain, BoxLayout.PAGE_AXIS));
		centerMain.setBorder(new EmptyBorder(0, 20, 0, 20));
		bottom = new JPanel();
		
		setLayout(new BorderLayout());
		add(top, BorderLayout.NORTH);
		add(centerMain, BorderLayout.CENTER);
		add(bottom, BorderLayout.SOUTH);
		
		setVisible(true);
	}

	public void view() {
		try {
			res = con.select();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			while(res.next()) {
				String[] data = {res.getString(1), res.getString(2), res.getString(3), res.getString(4)};
				model.addRow(data);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void initCenterForm() {
		centerForm = new JPanel();
		centerInputPanel = new JPanel();
		centerButtonPanel = new JPanel();
		initFormFields();
		initFormButtons();
		centerForm.add(centerInputPanel);
		centerForm.add(centerButtonPanel);
		centerForm.setLayout(new BoxLayout(centerForm, BoxLayout.PAGE_AXIS));
		
	}
	public void initCenterView() {
		centerTablePanel = new JPanel();
		String [] col = {"Kode Menu", "Nama Menu", "Harga Menu", "Stok Menu"};
		model = new DefaultTableModel(col, 0);
		view();
		table = new JTable(model);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				txtKode.setText(model.getValueAt(table.getSelectedRow(), 0).toString());
				txtNama.setText(model.getValueAt(table.getSelectedRow(), 1).toString());
				txtHarga.setText(model.getValueAt(table.getSelectedRow(), 2).toString());
				txtStok.setText(model.getValueAt(table.getSelectedRow(), 3).toString());
				System.out.println(table.getSelectedRow());
			}
		});
		tableScroll = new JScrollPane(table);
		centerTablePanel.add(tableScroll);
	}
	
	public void initFormFields() {
		lblKode = new JLabel("Kode Menu: ");
		lblNama = new JLabel("Nama Menu: ");
		lblHarga = new JLabel("Harga Menu: ");
		lblStok = new JLabel("Stok Menu: ");
		txtKode = new JTextField("", 10); 
		txtNama = new JTextField("", 10); 
		txtHarga = new JTextField("", 10); 
		txtStok = new JTextField("", 10);
		centerInputPanel.add(lblKode);
		centerInputPanel.add(txtKode);		
		centerInputPanel.add(lblNama);
		centerInputPanel.add(txtNama);		
		centerInputPanel.add(lblHarga);
		centerInputPanel.add(txtHarga);		
		centerInputPanel.add(lblStok);
		centerInputPanel.add(txtStok);	
		centerInputPanel.setLayout(new BoxLayout(centerInputPanel, BoxLayout.LINE_AXIS));
	}
	public void initFormButtons() {
		menuInsert = new JButton("Insert");
		menuUpdate = new JButton("Update");
		menuDelete = new JButton("Delete");
		menuClear = new JButton("Clear");
		
		menuInsert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(cekKosong(txtKode.getText(), txtNama.getText(), txtHarga.getText(), txtStok.getText())==false) {
					if (cekKode(txtKode.getText())==false) {
						con.insert(txtKode.getText(), txtNama.getText(), Integer.parseInt(txtHarga.getText()), Integer.parseInt(txtStok.getText()));
						model.addRow(new String[] {txtKode.getText(), txtNama.getText(), txtHarga.getText(), txtStok.getText()});
						JOptionPane.showMessageDialog(null, "Insert success.");
					} else JOptionPane.showMessageDialog(null, "Insert failed. 'Kode Menu' must be unique, starts with 'PD-', and must be 6 characters. [example: PD-001].");
				} else JOptionPane.showMessageDialog(null, "Insert failed. Field must not be empty");
			}
		});
		
		menuUpdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(cekKosong(txtKode.getText(), txtNama.getText(), txtHarga.getText(), txtStok.getText())==false) {
					if (table.getSelectedRow()>=0) {
						con.update(txtKode.getText(), Integer.parseInt(txtHarga.getText()), Integer.parseInt(txtStok.getText()));
						model.setValueAt(Integer.parseInt(txtHarga.getText()), table.getSelectedRow(), 2);
						model.setValueAt(Integer.parseInt(txtStok.getText()), table.getSelectedRow(), 3);
						JOptionPane.showMessageDialog(null, "Update success. Price and stock changed.");
					} else JOptionPane.showMessageDialog(null, "Update failed. Please select a valid row.");
				} else JOptionPane.showMessageDialog(null, "Update failed. Field must not be empty");
			}			
		});
		
		menuDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (table.getSelectedRow()>=0) {
//				System.out.println(model.getValueAt(table.getSelectedRow(), 0));
					con.delete(model.getValueAt(table.getSelectedRow(), 0).toString());
					model.removeRow(table.getSelectedRow());
					JOptionPane.showMessageDialog(null, "Delete success.");
				} else JOptionPane.showMessageDialog(null, "Delete failed. Please select a valid row.");
			}			
		});
		
		menuClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				txtKode.setText("");
				txtNama.setText("");
				txtHarga.setText("");
				txtStok.setText("");
			}		 	
		}); 
		
		centerButtonPanel.add(menuInsert);
		centerButtonPanel.add(menuUpdate);
		centerButtonPanel.add(menuDelete);
		centerButtonPanel.add(menuClear);
	}
	
	public boolean cekKosong(String kodeMenu, String namaMenu, String hargaMenu, String stokMenu) {
		if (kodeMenu.isEmpty() || kodeMenu.equals("")) return true;
		if (namaMenu.isEmpty() || namaMenu.equals("")) return true;
		if (hargaMenu.isEmpty() || hargaMenu.equals("")) return true;
		if (stokMenu.isEmpty() || stokMenu.equals("")) return true;
		return false;
	}
	public boolean cekKode(String kodeMenu) {
		if (!kodeMenu.startsWith("PD-")) return true;
		if (kodeMenu.length()!=6) return true;
		if (model.getRowCount()>=1) {
			for (int i = 0; i<model.getRowCount(); i++) {
				if (model.getValueAt(i, 0).toString().equals(kodeMenu)) return true;
			}
		}
		return false;
	}
	public static void main(String[] args) {new Main();}
	@Override
	public void actionPerformed(ActionEvent arg0) {}

}
