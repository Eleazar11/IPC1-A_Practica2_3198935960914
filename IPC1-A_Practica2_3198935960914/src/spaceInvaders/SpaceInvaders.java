/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package spaceInvaders;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author 3198935960914 - Eleazar Colop
 */
public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {
    Timer timer;
    Timer countdownTimer;
    int countdown = 90;
    List<List<Enemy>> monsterColumns = new ArrayList<>();
    List<Rectangle> mShoots = new ArrayList<>();
    List<Rectangle> pShoots = new ArrayList<>();
    Image playerImage;
    Image column1Image;
    Image column2And3Image;
    Image column4And5Image;
    Image explosionImage;
    int playerX = 50;
    int playerY = 225;
    int playerWidth = 41;
    int playerHeight = 41;
    int playerSpeed = 8;
    int monsterSpeed = 2;
    boolean toDown = true;
    int numPoints = 0;
    int numLives = 10;
    JLabel lives;
    JLabel points;
    JLabel timeLabel;
    Random random = new Random();
    Thread enemyMovementThread;
    String playerName;

    public SpaceInvaders(String playerName) {
        this.playerName = playerName;

        setLayout(null);
        setBackground(Color.BLACK);

        playerImage = new ImageIcon(getClass().getResource("nave.png")).getImage();
        column1Image = new ImageIcon(getClass().getResource("enemigo1.png")).getImage();
        column2And3Image = new ImageIcon(getClass().getResource("enemigo2.png")).getImage();
        column4And5Image = new ImageIcon(getClass().getResource("enemigo3.png")).getImage();
        explosionImage = new ImageIcon(getClass().getResource("explosion.gif")).getImage();

        lives = new JLabel("Lives: 10");
        lives.setForeground(Color.WHITE);
        lives.setBounds(20, 10, 100, 30);
        add(lives);

        points = new JLabel("Points: 0");
        points.setForeground(Color.WHITE);
        points.setBounds(550, 10, 100, 30);
        add(points);

        timeLabel = new JLabel("Tiempo (s): 90");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setBounds(1000, 10, 200, 30);
        add(timeLabel);

        addMonsters();

        timer = new Timer(10, this);
        timer.start();

        countdownTimer = new Timer(1000, e -> {
            countdown--;
            timeLabel.setText("Tiempo (s): " + countdown);
            if (countdown <= 0) {
                countdownTimer.stop();
                timer.stop();
                savePlayerData(playerName, numPoints);
                JOptionPane.showMessageDialog(this, playerName + ", el tiempo ha terminado. Tu puntaje es: " + numPoints);
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                frame.dispose(); // Cerrar la ventana del juego
                new MenuUsuario();
            }
        });
        countdownTimer.start();

        setFocusable(true);
        addKeyListener(this);

        startEnemyMovement();
    }

    public void addMonsters() {
        for (int j = 0, x = 1000; j < 5; j++, x += 50) {
            List<Enemy> column = new ArrayList<>();
            for (int i = 0, y = 40; i < 8; i++, y += 65) {
                int health = (j == 0) ? 2 : (j == 1 || j == 2) ? 3 : 4;
                int points = (j == 0) ? 10 : (j == 1 || j == 2) ? 20 : 30;
                column.add(new Enemy(x, y, 35, 30, health, points));
            }
            monsterColumns.add(column);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image backgroundImage = new ImageIcon(getClass().getResource("background.gif")).getImage();
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        g.drawImage(playerImage, playerX, playerY, playerWidth, playerHeight, this);

        for (int i = 0; i < monsterColumns.size(); i++) {
            List<Enemy> column = monsterColumns.get(i);
            for (Enemy monster : column) {
                if (monster.explosionTime > 0) {
                    g.drawImage(explosionImage, monster.x, monster.y, monster.width, monster.height, this);
                } else {
                    if (i == 0) {
                        g.drawImage(column1Image, monster.x, monster.y, monster.width, monster.height, this);
                    } else if (i >= 1 && i <= 2) {
                        g.drawImage(column2And3Image, monster.x, monster.y, monster.width, monster.height, this);
                    } else {
                        g.drawImage(column4And5Image, monster.x, monster.y, monster.width, monster.height, this);
                    }
                }
            }
        }

        g.setColor(Color.YELLOW);
        for (Rectangle shoot : pShoots) {
            g.fillOval(shoot.x, shoot.y, shoot.width, shoot.height);
        }

        g.setColor(Color.MAGENTA);
        for (Rectangle shoot : mShoots) {
            g.fillOval(shoot.x, shoot.y, shoot.width, shoot.height);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gameUpdate();
        repaint();
    }

    public void gameUpdate() {
        if (Math.random() < 0.01) {
            monstersShoot();
        }

        updateShoots(mShoots, -2);
        updateShoots(pShoots, playerSpeed);
        updateExplosions();
        isPlayerDestroyed();
        isMonsterDestroyed();
        isWin();
        isLost();
    }

    public void monstersShoot() {
        int getShootingMonsterIndex = rand(0, monsterColumns.size() - 1);
        List<Enemy> column = monsterColumns.get(getShootingMonsterIndex);
        if (!column.isEmpty()) {
            Enemy monster = column.get(random.nextInt(column.size()));
            mShoots.add(new Rectangle(monster.x + 15, monster.y + 15, 20, 15));
        }
    }

    public void updateShoots(List<Rectangle> shoots, int speed) {
        for (int i = 0; i < shoots.size(); i++) {
            Rectangle shoot = shoots.get(i);
            shoot.x += speed;
            if (shoot.x < 0 || shoot.x > getWidth()) {
                shoots.remove(i);
                i--;
            }
        }
    }

    public void updateExplosions() {
        for (List<Enemy> column : monsterColumns) {
            for (int j = 0; j < column.size(); j++) {
                Enemy monster = column.get(j);
                if (monster.explosionTime > 0) {
                    monster.explosionTime--;
                    if (monster.explosionTime <= 0) {
                        column.remove(j);
                        j--;
                    }
                }
            }
        }
    }

    public void startEnemyMovement() {
        enemyMovementThread = new Thread(() -> {
            while (numLives > 0) {
                monstersMove();
                repaint();
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        enemyMovementThread.start();
    }

    public void monstersMove() {
        int speed = toDown ? monsterSpeed : -monsterSpeed;

        boolean reachedBorder = false;
        for (List<Enemy> column : monsterColumns) {
            for (Enemy monster : column) {
                if (monster.y + speed >= getHeight() - monster.height || monster.y + speed <= 0) {
                    reachedBorder = true;
                    break;
                }
            }
            if (reachedBorder) break;
        }

        if (reachedBorder) {
            toDown = !toDown;
            for (List<Enemy> column : monsterColumns) {
                for (Enemy monster : column) {
                    monster.x -= 10;
                }
            }
        } else {
            for (List<Enemy> column : monsterColumns) {
                for (Enemy monster : column) {
                    monster.y += speed;
                }
            }
        }
    }

    public void isPlayerDestroyed() {
        for (int i = 0; i < mShoots.size(); i++) {
            Rectangle shoot = mShoots.get(i);
            if (shoot.intersects(playerX, playerY, playerWidth, playerHeight)) {
                playerX = 50;
                playerY = 225;
                numLives -= 1;
                lives.setText("Lives: " + numLives);
                mShoots.remove(i);
                i--;
            }
        }
    }

    public void isMonsterDestroyed() {
        for (int i = 0; i < pShoots.size(); i++) {
            Rectangle shoot = pShoots.get(i);
            for (int rowIndex = 0; rowIndex < 8; rowIndex++) {
                for (int colIndex = 0; colIndex < monsterColumns.size(); colIndex++) {
                    List<Enemy> column = monsterColumns.get(colIndex);
                    if (column.isEmpty()) {
                        continue;
                    }

                    boolean canShootNextColumn = colIndex == 0 || isRowClearedInPreviousColumns(rowIndex, colIndex);

                    if (canShootNextColumn) {
                        for (int j = 0; j < column.size(); j++) {
                            Enemy monster = column.get(j);
                            if (monster.y / 65 == rowIndex) {
                                Rectangle hitbox = new Rectangle(monster.x + 5, monster.y + 5, monster.width - 10, monster.height - 10);
                                if (shoot.intersects(hitbox)) {
                                    monster.health--;
                                    pShoots.remove(i);
                                    i--;

                                    if (monster.health <= 0 && monster.explosionTime <= 0) {
                                        monster.explosionTime = 50; // 0.5 seconds at 10 ms per update
                                        numPoints += monster.points;
                                        points.setText("Points: " + numPoints);
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    private boolean isRowClearedInPreviousColumns(int rowIndex, int colIndex) {
        for (int prevColIndex = 0; prevColIndex < colIndex; prevColIndex++) {
            List<Enemy> prevColumn = monsterColumns.get(prevColIndex);
            boolean rowCleared = true;
            for (Enemy prevMonster : prevColumn) {
                if (prevMonster.y / 65 == rowIndex) {
                    rowCleared = false;
                    break;
                }
            }
            if (!rowCleared) {
                return false;
            }
        }
        return true;
    }

    public void isWin() {
        boolean allColumnsCleared = true;
        for (List<Enemy> column : monsterColumns) {
            if (!column.isEmpty()) {
                allColumnsCleared = false;
                break;
            }
        }

        if (allColumnsCleared) {
            timer.stop();
            countdownTimer.stop();
            savePlayerData(playerName, numPoints);
            JOptionPane.showMessageDialog(this, playerName + ", tu puntaje ha sido de " + numPoints);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose(); // Cerrar la ventana del juego
            new MenuUsuario(); // Volver al menú de usuario
        }
    }

    public void isLost() {
        if (numLives <= 0) {
            timer.stop();
            countdownTimer.stop();
            savePlayerData(playerName, numPoints);
            JOptionPane.showMessageDialog(this, playerName + ", tu puntaje ha sido de " + numPoints);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose(); // Cerrar la ventana del juego
            new MenuUsuario(); // Volver al menú de usuario
        }
    }

    private void savePlayerData(String playerName, int score) {
        try (FileWriter writer = new FileWriter("C:\\Users\\Usuario\\Desktop\\praticas\\IPC1-A_Practica2_3198935960914\\IPC1-A_Practica2_3198935960914\\src\\spaceInvaders\\registroPunteos.csv", true)) {
            String timestamp = new SimpleDateFormat("HH_mm_dd_MM_yyyy").format(new Date());
            writer.append(playerName).append(",").append(String.valueOf(score)).append(",").append(timestamp).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (playerY - playerSpeed >= 0) {
                playerY -= playerSpeed;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (playerY + playerSpeed + playerHeight <= getHeight()) {
                playerY += playerSpeed;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            pShoots.add(new Rectangle(playerX + playerWidth, playerY + playerHeight / 2 - 10, 30, 20)); // Dispara normalmente
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose();
            new MenuUsuario(); // Instancia de la clase MenuUsuario
            // Aquí deberías mostrar la ventana del menú de usuario
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            JOptionPane.showMessageDialog(
        null, "Serializando en la carpeta -Juegos-...", "SERIALIZANDO", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public int rand(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    class Enemy {
        int x, y, width, height, health, points;
        int explosionTime = 0;

        Enemy(int x, int y, int width, int height, int health, int points) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.health = health;
            this.points = points;
        }
    }
}
