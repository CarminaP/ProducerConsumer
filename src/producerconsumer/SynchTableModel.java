/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package producerconsumer;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author martell
 */
public class SynchTableModel extends DefaultTableModel{

    public synchronized void removeRow(int row) {
        super.removeRow(row); //To change body of generated methods, choose Tools | Templates.
    }

    public synchronized void addRow(Object[] rowData) {
        super.addRow(rowData); //To change body of generated methods, choose Tools | Templates.
    }
}
