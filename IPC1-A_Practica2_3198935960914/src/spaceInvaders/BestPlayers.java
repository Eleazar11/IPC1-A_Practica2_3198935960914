/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spaceInvaders;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
public class BestPlayers extends JFrame implements ActionListener {

    private JButton menuPrincipalButton;
    private java.util.List<JLabel> playerLabels;

    public BestPlayers() {
        initComponents();
        cargarMejoresJugadoresDesdeCSV("C:\\Users\\Usuario\\Desktop\\praticas\\IPC1-A_Practica2_3198935960914\\IPC1-A_Practica2_3198935960914\\src\\spaceInvaders\\registroPunteos.csv");
    }

    private void initComponents() {
        // Crear panel de fondo personalizado
        ImagenFondo backgroundPanel = new ImagenFondo();
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        // Título "MEJORES JUGADORES"
        JLabel titleLabel = new JLabel("MEJORES JUGADORES");
        titleLabel.setBounds(200, 20, 300, 30); // Ajusta la posición y el tamaño del título
        titleLabel.setForeground(Color.WHITE); // Establece el color del texto en blanco
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f)); // Establece el texto en negrita y un poco más grande
        backgroundPanel.add(titleLabel);

        // Botón de menú principal
        menuPrincipalButton = new JButton("Menú Principal");
        menuPrincipalButton.setBounds(50, 20, 150, 30); // Cambia la posición x a 50
        menuPrincipalButton.addActionListener(this);
        backgroundPanel.add(menuPrincipalButton);

        // Configuración de la ventana
        this.setTitle("Best Players");
        this.setSize(600, 400);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
    }

    private void cargarMejoresJugadoresDesdeCSV(String filePath) {
        playerLabels = new ArrayList<>();
        java.util.List<Jugador> jugadores = new ArrayList<>(); 
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    Jugador jugador = new Jugador(parts[0], Integer.parseInt(parts[1]));
                    jugadores.add(jugador);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al leer el archivo CSV", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ordenar jugadores por puntuación (de mayor a menor)
        Collections.sort(jugadores, Collections.reverseOrder());

        // Mostrar los mejores jugadores
        int contador = 0;
        for (int i = 0; i < jugadores.size() && contador < 5; i++) {
            Jugador jugador = jugadores.get(i);
            int j = i;
            while (j < jugadores.size() && jugadores.get(j).getPuntuacion() == jugador.getPuntuacion()) {
                JLabel playerLabel = new JLabel((contador + 1) + "º lugar: " + jugadores.get(j).getNombre() + " - " + jugadores.get(j).getPuntuacion() + " puntos");
                playerLabel.setBounds(50, 150 + contador * 50, 500, 30); // Ajusta la posición y el tamaño del texto
                playerLabel.setForeground(Color.WHITE); // Establece el color del texto en blanco
                playerLabel.setFont(playerLabel.getFont().deriveFont(Font.BOLD, 16f)); // Establece el texto en negrita y un poco más grande
                this.getContentPane().add(playerLabel);
                playerLabels.add(playerLabel);
                contador++;
                j++;
            }
            i = j - 1;
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == menuPrincipalButton) {
            // Volver al menú principal
            this.dispose();
            new MenuUsuario();
        }
    }

    class ImagenFondo extends JPanel {
        private Image imagen;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Cargar y dibujar la imagen de fondo
            imagen = new ImageIcon(getClass().getResource("backgroundBestPlayers.gif")).getImage();
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
        }
    }

    class Jugador implements Comparable<Jugador> {
        private String nombre;
        private int puntuacion;

        public Jugador(String nombre, int puntuacion) {
            this.nombre = nombre;
            this.puntuacion = puntuacion;
        }

        public String getNombre() {
            return nombre;
        }

        public int getPuntuacion() {
            return puntuacion;
        }

        @Override
        public int compareTo(Jugador otroJugador) {
            return Integer.compare(this.puntuacion, otroJugador.getPuntuacion());
        }
    }
}
