/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.dbproject;

import TableStrcuture.Journals;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import my.control.ButtonEvents;

/**
 *
 * @author chintanpanchamia
 */
public class JournalSelectionForm extends javax.swing.JFrame {

    /**
     * Creates new form PublicationSelectionForm
     */
    public JournalSelectionForm() {
        initComponents();
    }
    public void populate(ArrayList <Journals> jo)
    {
        String[] schema = {"ISSN", "Year of Publication", "Authors", "Title", "Library", "e-copy", "Hard Copy"};
        DefaultTableModel d = new DefaultTableModel(schema,0);
        System.out.println("Here1"+jo.size());
        for(int i = 0;i < jo.size(); i++)
        {
            System.out.println("Here2");
            String issn = jo.get(i).getIssn_no();
            System.out.println(issn);
            int year = jo.get(i).getYear_of_publication();
            String author = "";
            ArrayList <String> al = new ArrayList<>();
            al = jo.get(i).getAuthor_list();
            for(int j = 0;j < al.size(); j++)
            {
                author += al.get(j)+"";
            }
            System.out.println("Here");
            String title = jo.get(i).getTitle();
            String e_copy = jo.get(i).getE_copy();
            int hunt_total = jo.get(i).getHunt_total_no();
            int hunt_avail = jo.get(i).getHunt_avail_no();
            int hill_total = jo.get(i).getHill_total_no();
            int hill_avail = jo.get(i).getHill_avail_no();
            String library = "", hard_copy = "";
            if(e_copy.equalsIgnoreCase("Y"))
            {
                library = "-"; hard_copy = "-";
                Object[] o = {issn, year, author, title, library, e_copy, hard_copy};
                d.addRow(o);
                
            }
            if(hunt_total > 0)
            {
                library = "HUNT"; 
                hard_copy = "" + hunt_avail;
                Object[] o = {issn, year, author, title, library, e_copy, hard_copy};
                d.addRow(o);
            }
            if(hill_total > 0)
            {
                library = "HILL";
                hard_copy = "" + hill_avail;
                Object[] o = {issn, year, author, title, library, e_copy, hard_copy};
                d.addRow(o);
            }
        }
        jTable1.setModel(d);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Select a Publication"));

        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Reserve");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 649, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(78, 78, 78))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addGap(0, 58, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.setVisible(false);
        ResourceForm.init();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int select = jTable1.getSelectedRow();
        String issn = (String) jTable1.getValueAt(select, 0);
        Journals j = new Journals();
        j.setIssn_no(issn);
        String library = (String) jTable1.getValueAt(select, 4);
        try {
            ButtonEvents.checkout_journal(j, library);
        } catch (SQLException ex) {
            Logger.getLogger(JournalSelectionForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setVisible(false);
        MenuForm.init();
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void init(ArrayList <Journals> jo) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JournalSelectionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JournalSelectionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JournalSelectionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JournalSelectionForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        final ArrayList <Journals> jo1 = jo;
        System.out.println(jo1.size());
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JournalSelectionForm j = new JournalSelectionForm();
                j.populate(jo1);
                j.setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
