package Gepnaplo2;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Fabian Tamas 1.0
 */
public class Gepnaplo2 extends javax.swing.JFrame {

    Properties tulajdonsagok = new Properties();
    String t_ip, t_user, t_pass;
            
    public Gepnaplo2() {
        initComponents();
        cbxIdo.requestFocus();
        // tulajdonságok beolvasása
        try (FileInputStream be = new FileInputStream("config.properties")) {
            tulajdonsagok.load(be);
            t_ip = tulajdonsagok.getProperty("ip");
            t_user = tulajdonsagok.getProperty("user");
            t_pass = tulajdonsagok.getProperty("pass");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            System.exit(0);
        }
    }
    
    private String lekerdez() {
        String q = "";
        if (!txtGepszuro.getText().isEmpty()) {
            q = q + " felhasznalo LIKE '" + txtGepszuro.getText() + "' AND ";
        }
        switch (cbxIdo.getSelectedIndex()) {
            case 0: // ezen az órán
                q = q + " TIMEDIFF(NOW(),ido)<'00:45' AND";
                break;
            case 1: // ma
                q = q + " DATE(ido)=DATE(NOW()) AND";
                break;
            case 2: // 7 napja
                q = q + " DATEDIFF(NOW(),ido)<=7 AND";
                break;
            case 3: // 30 napja
                q = q + " DATEDIFF(NOW(),ido)<=30 AND";
                break;
        }
        
         if (!txtNevszuro.getText().isEmpty()) {
            q = q + " nev LIKE '" + txtNevszuro.getText() + "' AND ";
         }
         
         if (chkProb.isSelected()) {
             q = q + " allapot NOT LIKE 'Rendben%'";
         } else {
             q = q + " allapot LIKE '%'";
         }
         
         return "SELECT felhasznalo,iskola,osztaly,nev,ido,allapot "
                 + "FROM gepek WHERE" + q + " ORDER BY ido DESC;";
    }
    
    // Adatbázisból beolvasás
    private void beolvas() {
        final String dbUrl = "jdbc:mysql://" + t_ip + ":3306/gepnaplo"
                + "?useUnicode=true&characterEncoding=UTF-8";
        final String user = t_user;
        final String pass = "tanar" + t_pass;
        DefaultTableModel tm = (DefaultTableModel)tblGepek.getModel();
        
        try (Connection kapcs = DriverManager.getConnection(dbUrl, user, pass);
                PreparedStatement parancs = kapcs.prepareStatement(lekerdez());
                ResultSet eredmeny = parancs.executeQuery()) {
            tm.setRowCount(0); //sorok törlése
            while (eredmeny.next()) {
                Object sor[] = {
                    eredmeny.getString("felhasznalo"),
                    eredmeny.getString("ido"),
                    eredmeny.getString("nev"),
                    eredmeny.getString("allapot"),
                    eredmeny.getString("osztaly"),
                    eredmeny.getString("iskola")
                };
                tm.addRow(sor);
                }
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            System.exit(0);
        }
    }
    
    private void torol() {
        final String dbUrl = "jdbc:mysql://" + t_ip + ":3306/gepnaplo"
                + "?useUnicode=true&characterEncoding=UTF-8";
        final String user = t_user;
        final String pass = "tanar" + t_pass;
        final String s = "DELETE FROM gepek WHERE DATEDIFF(NOW(),ido)>30";
        try (Connection kapcs = DriverManager.getConnection(dbUrl, user, pass);
                PreparedStatement parancs = kapcs.prepareStatement(s)) {
            parancs.executeUpdate(s);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            System.exit(0);
        }
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtGepszuro = new javax.swing.JTextField();
        cbxIdo = new javax.swing.JComboBox<>();
        txtNevszuro = new javax.swing.JTextField();
        chkProb = new javax.swing.JCheckBox();
        btnFrissit = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGepek = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gépnapló ellenőrzés");

        txtGepszuro.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtGepszuro.setText("%");
        txtGepszuro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGepszuroActionPerformed(evt);
            }
        });

        cbxIdo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        cbxIdo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ezen az órán", "Ma", "7 napja", "30 napja" }));
        cbxIdo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxIdoItemStateChanged(evt);
            }
        });
        cbxIdo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxIdoActionPerformed(evt);
            }
        });

        txtNevszuro.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        txtNevszuro.setText("%");
        txtNevszuro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNevszuroActionPerformed(evt);
            }
        });

        chkProb.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        chkProb.setMnemonic('P');
        chkProb.setText("Csak a problémások");
        chkProb.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkProbItemStateChanged(evt);
            }
        });

        btnFrissit.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        btnFrissit.setMnemonic('F');
        btnFrissit.setText("Frissítés");
        btnFrissit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFrissitActionPerformed(evt);
            }
        });

        tblGepek.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        tblGepek.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Gép", "Idő", "Név", "Állapot", "Osztály", "Iskola"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblGepek);
        if (tblGepek.getColumnModel().getColumnCount() > 0) {
            tblGepek.getColumnModel().getColumn(0).setPreferredWidth(100);
            tblGepek.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblGepek.getColumnModel().getColumn(2).setPreferredWidth(150);
            tblGepek.getColumnModel().getColumn(3).setPreferredWidth(150);
            tblGepek.getColumnModel().getColumn(4).setPreferredWidth(80);
            tblGepek.getColumnModel().getColumn(5).setPreferredWidth(170);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtGepszuro, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(cbxIdo, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtNevszuro, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(chkProb)
                        .addGap(18, 18, 18)
                        .addComponent(btnFrissit, javax.swing.GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtGepszuro)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbxIdo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtNevszuro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(chkProb)
                        .addComponent(btnFrissit)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void cbxIdoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxIdoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxIdoActionPerformed

    private void txtGepszuroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGepszuroActionPerformed
        beolvas();
    }//GEN-LAST:event_txtGepszuroActionPerformed

    private void btnFrissitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFrissitActionPerformed
        beolvas();
    }//GEN-LAST:event_btnFrissitActionPerformed

    private void cbxIdoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxIdoItemStateChanged
        beolvas();
    }//GEN-LAST:event_cbxIdoItemStateChanged

    private void chkProbItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkProbItemStateChanged
        beolvas();
    }//GEN-LAST:event_chkProbItemStateChanged

    private void txtNevszuroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNevszuroActionPerformed
        beolvas();
    }//GEN-LAST:event_txtNevszuroActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(Gepnaplo2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Gepnaplo2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Gepnaplo2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Gepnaplo2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Gepnaplo2().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFrissit;
    private javax.swing.JComboBox<String> cbxIdo;
    private javax.swing.JCheckBox chkProb;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblGepek;
    private javax.swing.JTextField txtGepszuro;
    private javax.swing.JTextField txtNevszuro;
    // End of variables declaration//GEN-END:variables
}
