/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.Utilities;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.table.TableModel;

/**
 *
 * @author Nkanabo
 */
public class ExcelFormat {
      //JTable Header
  String[] columns = new String[] {
        "Id",
        "Name", 
        "Address", 
        "Hourly rate", 
  };
  
    //data for JTable in a 2D table
  Object[][] data = new Object[][] {
        {1, "Thomas", "Paris", 20.0 },
        {2, "Jean", "Marseille", 50.0 },
        {3, "Yohan", "Lyon", 30.0 },
        {4, "Emily", "Toulouse", 60.0 },
        {5, "Alex", "Nice", 10.0 },
        {6, "Nicolas", "Lille", 11.5 },
  };
  
  //create a JTable with data
  JTable table = new JTable(data, columns);
  JPanel panel = new JPanel(new BorderLayout());
  JButton btn = new JButton("Export");
  
  
   /* public ExcelFormat(){
    setSize(450,200);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setTitle("Export JTable to Excel");
    panel.add(btn, BorderLayout.SOUTH);
    panel.add(new JScrollPane(table), BorderLayout.NORTH);
    add(panel);
    setVisible(true);
    btn.addActionListener(new MyListener());
  }*/
   
   
    public void export(JTable table, File file){
    try
    {
      TableModel m = table.getModel();
      FileWriter fw = new FileWriter(file);
      for(int i = 0; i < m.getColumnCount(); i++){
        fw.write(m.getColumnName(i) + "\t");
      }
      fw.write("\n");
      for(int i=0; i < m.getRowCount(); i++) {
        for(int j=0; j < m.getColumnCount(); j++) {
          fw.write(m.getValueAt(i,j).toString()+"\t");
        }
        fw.write("\n");
      }
      fw.close();
    }
    catch(IOException e){ System.out.println(e); }
  }
//  public static void main(String[] args){
//        new JTableToExcel();
//  }
//  class MyListener implements ActionListener{
     /* public void actionPerformed(ActionEvent e){
         if(e.getSource() == btn){
           JFileChooser fchoose = new JFileChooser();
           int option = fchoose.showSaveDialog(JTableToExcel.this);
           if(option == JFileChooser.APPROVE_OPTION){
             String name = fchoose.getSelectedFile().getName(); 
             String path = fchoose.getSelectedFile().getParentFile().getPath();
             String file = path + "\\" + name + ".xls"; 
             export(table, new File(file));
           }
         }
      }
  } */
}
