/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spaceInvaders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuUsuario extends JFrame implements ActionListener {

    private JButton nuevoJuegoButton;
    private JButton cargarJuegoButton;
    private JButton puntuacionMaximaButton;
    private JButton salirButton;

    public MenuUsuario() {
        initComponents();
    }

    private void initComponents() {
        // Crear panel de fondo personalizado
        ImagenFondo backgroundPanel = new ImagenFondo();
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        // Cargar la imagen del título y redimensionarla
        ImageIcon titleIcon = new ImageIcon(getClass().getResource("../imgs/title.png"));
        Image titleImage = titleIcon.getImage();
        Image scaledTitleImage = titleImage.getScaledInstance(300, 100, Image.SCALE_SMOOTH);
        ImageIcon scaledTitleIcon = new ImageIcon(scaledTitleImage);

        JLabel titleLabel = new JLabel(scaledTitleIcon);
        titleLabel.setBounds(100, 20, scaledTitleIcon.getIconWidth(), scaledTitleIcon.getIconHeight());
        backgroundPanel.add(titleLabel);

        // Botones
        nuevoJuegoButton = new JButton("Nuevo Juego");
        nuevoJuegoButton.setBounds(150, 150, 200, 30);
        nuevoJuegoButton.addActionListener(this);
        backgroundPanel.add(nuevoJuegoButton);

        cargarJuegoButton = new JButton("Cargar Juego");
        cargarJuegoButton.setBounds(150, 200, 200, 30);
        cargarJuegoButton.addActionListener(this);
        backgroundPanel.add(cargarJuegoButton);

        puntuacionMaximaButton = new JButton("Puntuación Máxima");
        puntuacionMaximaButton.setBounds(150, 250, 200, 30);
        puntuacionMaximaButton.addActionListener(this);
        backgroundPanel.add(puntuacionMaximaButton);

        salirButton = new JButton("Salir");
        salirButton.setBounds(150, 300, 200, 30);
        salirButton.addActionListener(this);
        backgroundPanel.add(salirButton);

        // Configuración de la ventana
        this.setTitle("Menu de Usuario");
        this.setSize(500, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == nuevoJuegoButton) {
            // Iniciar un nuevo juego
            JOptionPane.showMessageDialog(this, "Iniciar un nuevo juego", "Nuevo Juego", JOptionPane.INFORMATION_MESSAGE);
        
        } else if (ae.getSource() == cargarJuegoButton) {
            // Cargar un juego existente
            JOptionPane.showMessageDialog(this, "Cargar un juego existente", "Cargar Juego", JOptionPane.INFORMATION_MESSAGE);
        } else if (ae.getSource() == puntuacionMaximaButton) {
            // Mostrar la puntuación máxima
            JOptionPane.showMessageDialog(this, "Mostrar la puntuación máxima", "Puntuación Máxima", JOptionPane.INFORMATION_MESSAGE);
        } else if (ae.getSource() == salirButton) {
            // Salir del juego
            System.exit(0);
        }
    }

    class ImagenFondo extends JPanel {
        private Image imagen;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Cargar y dibujar la imagen de fondo
            imagen = new ImageIcon(getClass().getResource("../imgs/backgroundPrincipal.gif")).getImage();
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }


}
