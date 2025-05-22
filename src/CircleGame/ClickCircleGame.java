package CircleGame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
public class ClickCircleGame extends JFrame {
    private ImageIcon image=new ImageIcon(ClickCircleGame.class.getResource("CircleGameLogo.jpg"));
    static int score = 0;
    final int Board_Height=500;
    final int Board_Weight=500;
    private int highScore = 0;
    private int timeLeft = 30;
    private Timer gameTimer, moveTimer;
    private int circleX, circleY;
    public static final int circleSize = 50;
    private Random rand = new Random();
    private JLabel scoreLabel, timeLabel, highScoreLabel;
    private JPanel gamePanel;
    private int moveDelay = 1000; // Default = Medium
    public void setTimeLeft(int timeLefT){
        timeLeft=timeLefT;
        return;

    }
    public int getTimeLeft(){
        return timeLeft;
    }

    public ClickCircleGame() {
        setTitle("Click the Circle!");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(image.getImage());
        setLayout(new BorderLayout());



        // Top Panel
        JPanel topPanel = new JPanel();
        scoreLabel = new JLabel("Score: 0");
        highScoreLabel = new JLabel("High Score: 0");
        timeLabel = new JLabel("Time: 30");
        topPanel.add(scoreLabel);
        topPanel.add(highScoreLabel);
        topPanel.add(timeLabel);
        add(topPanel, BorderLayout.SOUTH);

        // Game Panel
        gamePanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillOval(circleX, circleY, circleSize, circleSize);

            }
        };
        gamePanel.setBackground(Color.CYAN);
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mx = e.getX();
                int my = e.getY();
                double dx = mx - (circleX + circleSize / 2.0);
                double dy = my - (circleY + circleSize / 2.0);
                if (Math.sqrt(dx * dx + dy * dy) <= circleSize / 2.0) {
                    score++;
                    scoreLabel.setText("Score: " + score);
                    playClickSound();
                    moveCircle();
                }
            }
        });
        add(gamePanel, BorderLayout.CENTER);

        // Difficulty Panel
        JPanel bottomPanel = new JPanel();
        String[] levels = {"Easy", "Medium", "Hard"};
        JComboBox<String> difficultyBox = new JComboBox<>(levels);
        difficultyBox.setSelectedIndex(0); // Default Medium
        difficultyBox.addActionListener(e -> {

            String level = (String) difficultyBox.getSelectedItem();
            gameOver();
            switch (level) {
                case "Easy": moveDelay = 1200; break;
                case "Medium": moveDelay = 1000; break;
                case "Hard": moveDelay = 800; break;
            }

            moveTimer.setDelay(moveDelay);
        });
        bottomPanel.add(new JLabel("Level"));
        bottomPanel.add(difficultyBox);
        add(bottomPanel, BorderLayout.NORTH);

        // Timers
        moveTimer = new Timer(moveDelay, e -> {
            moveCircle();
            gamePanel.repaint();
        });

        gameTimer = new Timer(1000, e -> {
            timeLeft--;
            timeLabel.setText("Time: " + timeLeft);
            if (timeLeft <= 0) {
                gameOver();
            }
        });

        moveCircle();
        moveTimer.start();
        gameTimer.start();

        setVisible(true);
    }

    private void gameOver() {
        gameTimer.stop();
        moveTimer.stop();
        if (score > highScore) {
            highScore = score;
            highScoreLabel.setText("High Score: " + highScore);
        }
       int option= JOptionPane.showOptionDialog(this, "Time's up! Final Score: " + score,"Game Over",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE,null,new String[]{"Restart","Exit"},"Restart");
        if(option==JOptionPane.YES_OPTION) {
            resetGame();
        }
        else
        {
            System.exit(0);
        }

    }

    private void resetGame() {
        score = 0;
        timeLeft = 30;
        scoreLabel.setText("Score: 0");
        timeLabel.setText("Time: 30");
        moveTimer.start();
        gameTimer.start();
    }

    private void moveCircle() {
        circleX = rand.nextInt(Math.max(1, gamePanel.getWidth() - circleSize));
        circleY = rand.nextInt(Math.max(1, gamePanel.getHeight() - circleSize));
        gamePanel.repaint();
    }

    private void playClickSound() {
        try {
            Toolkit.getDefaultToolkit().beep();

        } catch (Exception e) {
            System.out.println("Sound error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClickCircleGame::new);
}
}
