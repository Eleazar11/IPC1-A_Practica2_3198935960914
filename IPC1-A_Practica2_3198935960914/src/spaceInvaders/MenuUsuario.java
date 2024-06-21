/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spaceInvaders;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author 3198935960914 - Eleazar Colop
 */
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
        ImageIcon titleIcon = new ImageIcon(getClass().getResource("title.png"));
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
            String playerName = JOptionPane.showInputDialog(null, "Introduce tu nombre:");
            if (playerName == null || playerName.trim().isEmpty()) {
                playerName = "Jugador";
            }

            JFrame frame = new JFrame("Space Invaders");
            SpaceInvaders game = new SpaceInvaders(playerName);
            frame.add(game);
            frame.setSize(1200, 650);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setVisible(true);
            this.dispose();
        } else if (ae.getSource() == cargarJuegoButton) {
            // Cargar un juego existente
            new CargarJuego();
            // Cerrar el menú de usuario
             this.dispose();
            
        } else if (ae.getSource() == puntuacionMaximaButton) {
            // Mostrar la puntuación máxima
            new BestPlayers();
            this.dispose();
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
            imagen = new ImageIcon(getClass().getResource("backgroundPrincipal.gif")).getImage();
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }


}
