import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.HashMap;
import java.util.Map;

public class Gestion_Etudiant extends JFrame{

    private JTextField nomField, noteField;
    private JButton submitBtn, UpdateBtn, DeleteBtn;
    private JList<String> EtudiantList;
    private DefaultListModel<String> listModel;
    private HashMap<Integer, String> EtudiantMap = new HashMap<>();
    private int selectedId = -1;


    public Gestion_Etudiant(){
        //inialisation de la fenetre
        setTitle("Gestion des etudiants");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panneau d'ajouter
        JPanel AddPanel = new JPanel();
        AddPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // les elements des panneau d'ajouter
        JLabel nomLab = new JLabel("Nom de l'étudiant:");
        nomField = new JTextField(20);
        JLabel noteLab = new JLabel("Note: ");
        noteField = new JTextField(10);
        submitBtn = new JButton("Ajouter");
        AddPanel.add(nomLab);
        AddPanel.add(nomField);
        AddPanel.add(noteLab);
        AddPanel.add(noteField);
        AddPanel.add(submitBtn);  

        // ajouter a le fenetre
        add(AddPanel, BorderLayout.NORTH);
        
        // panneau des button
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new GridBagLayout());
        
        // les elements des panneau
        UpdateBtn = new JButton("Modifier");
        DeleteBtn = new JButton("Supprimer");
        btnPanel.add(UpdateBtn);
        btnPanel.add(DeleteBtn);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ajouter a le fenetre
        add(btnPanel, BorderLayout.WEST);

        

        //la list d'affichage
        listModel = new DefaultListModel<>();
        EtudiantList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(EtudiantList);
        scrollPane.setPreferredSize(new Dimension(150, 100));
        
        // ajouter a le fenetre
        add(scrollPane, BorderLayout.CENTER);


        // inialistaion des class d'Etudiant
        Etudiant etudiant = new Etudiant();

        // Lire
        etudiant.ReadEtudiant(listModel, EtudiantMap);

        // Ajouter
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = nomField.getText();
                String note = noteField.getText();
                try {
                    double parsedNote = Double.parseDouble(note);
                    if (!nom.isEmpty() && !note.isEmpty()) {
                        if (parsedNote >= 0 && parsedNote <= 20) {
                            if (selectedId == -1) {
                                etudiant.CreateEtudiant(nom, parsedNote);
                                System.out.println("etudiant ajouter: " + nom + " : " + note);
                            } else {
                                etudiant.UpdateEtudiant(selectedId, nom, parsedNote);
                                System.out.println("etudiant modifier: " + nom + " : " + note);
                                selectedId = -1;
                            }
                            nomField.setText("");
                            noteField.setText("");
                            etudiant.ReadEtudiant(listModel, EtudiantMap);
                        } else {
                            JOptionPane.showMessageDialog(null, "La note doit être entre 0 et 20.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Tous les champs sont requis.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "La note doit être un nombre valide.");
                }
            }
        });
        
        
        // Supprimer
        DeleteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedId != -1) {
                    etudiant.DeleteEtudiant(selectedId);

                    String selectedValue = EtudiantList.getSelectedValue();
                    listModel.removeElement(selectedValue);

                    EtudiantList.clearSelection();
                    selectedId = -1;
                    System.out.println("etudiant supprimer.");
                } else {
                    System.out.println("Error: etudiant n'est pas selectionner");
                }
            }
        });
            
        // Modefier
        UpdateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = EtudiantList.getSelectedIndex();
                
                if (selectedIndex != -1) {
                    String selectedValue = EtudiantList.getSelectedValue();
                    String[] splitValue = selectedValue.split(" : ");
                    nomField.setText(splitValue[0]);
                    noteField.setText(splitValue[1]);
        
                    System.out.println("modifier etudiant: " + selectedValue);
                } else {
                    JOptionPane.showMessageDialog(null, "selectionner un etudiant a modifier");
                }
            }
        });
                            
            
        // Selection
        EtudiantList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedValue = EtudiantList.getSelectedValue();
                    if (selectedValue != null) {
                        for (Map.Entry<Integer, String> entry : EtudiantMap.entrySet()) {
                            if (entry.getValue().equals(selectedValue)) {
                                selectedId = entry.getKey();
                                System.out.println("selected id: " + selectedId);
                                break;
                            }
                        }
                    } else {
                        selectedId = -1;
                        System.out.println("Error: no etudiant selectioner");
                    }
                }
            }
        });

}
        
    public static void main(String[] args) {
        Gestion_Etudiant form = new Gestion_Etudiant();
        form.setVisible(true);
    }
}
