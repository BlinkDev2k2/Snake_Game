import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Game_Panel extends JPanel implements ActionListener {
    private static final short SCREEN_WIDTH = 600, SCREEN_HEIGHT = 600;
    private static final short UNIT_SIZE = 25;
    private static final short DELAY = 105;
    private int[] x = new int[UNIT_SIZE * UNIT_SIZE];
    private int[] y = new int[UNIT_SIZE * UNIT_SIZE];
    private short bodySnake = 5;
    private short Point_Eaten = 0;
    private short PointX, PointY;
    private char towards = 'R';
    private boolean running = false;
    private Timer timer;
    private Random random;

    Game_Panel(){
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.setDoubleBuffered(true);
        this.addKeyListener(new checkKeyBoard());
        this.random = new Random();
        this.startGame();
    }

    public class checkKeyBoard extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(towards != 'R')
                        towards = 'L';
                    break;
                case KeyEvent.VK_RIGHT:
                    if(towards != 'L')
                        towards = 'R';
                    break;
                case KeyEvent.VK_UP:
                    if(towards != 'D')
                        towards = 'U';
                    break;
                case KeyEvent.VK_DOWN:
                    if(towards != 'U')
                        towards = 'D';
            }
        }
    }

    public void startGame(){
        this.createPoint();
        this.running = true;
        this.timer = new Timer(DELAY, this);
        this.timer.start();
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        this.draw(g);
    }

    public void draw(Graphics g){
        if(this.running){
            /*for(int i = 1; i < SCREEN_HEIGHT/UNIT_SIZE; ++i){
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }*/
            g.setColor(Color.RED);
            g.fillOval(PointX, PointY, UNIT_SIZE, UNIT_SIZE);

            for(int i = 0; i < this.bodySnake; ++i){
                if(i == 0){
                    g.setColor(Color.GREEN);
                    g.fillRect(this.x[i], this.y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(50,170,0));
                    g.fillRect(this.x[i], this.y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.RED);
            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + this.Point_Eaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + this.Point_Eaten))/2, g.getFont().getSize());
        }
        else{
            this.gameOver(g);
        }
    }

    public void move(){
        for(int i = this.bodySnake; i > 0; --i){
            this.x[i] = this.x[i-1];
            this.y[i] = this.y[i-1];
        }

        switch (this.towards){
            case 'R':
                this.x[0] = this.x[0] + UNIT_SIZE;
                break;
            case 'L':
                this.x[0] = this.x[0] - UNIT_SIZE;
                break;
            case 'U':
                this.y[0] = this.y[0] - UNIT_SIZE;
                break;
            case 'D':
                this.y[0] = this.y[0] + UNIT_SIZE;
        }
    }

    public void createPoint(){
        PointX = (short) (this.random.nextInt(SCREEN_WIDTH/UNIT_SIZE) * UNIT_SIZE);
        PointY = (short) (this.random.nextInt(SCREEN_HEIGHT/UNIT_SIZE) * UNIT_SIZE);
    }

    public void checkPoint(){
        if((this.x[0] == this.PointX) && (this.y[0] == this.PointY)){
            ++bodySnake;
            ++Point_Eaten;
            this.createPoint();
        }
    }

    public void checkCollision(){
        for(int i = this.bodySnake; i > 0; --i){
            if((x[0] == x[i]) && (y[0] == y[i])){
                this.running = false;
            }
        }
        if(x[0] < 0 || y[0] < 0) this.running = false;
        if(x[0] > SCREEN_WIDTH || y[0] > SCREEN_HEIGHT) this.running = false;
        if(!this.running) this.timer.stop();
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 50));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: " + this.Point_Eaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + this.Point_Eaten)) / 2, (SCREEN_HEIGHT - metrics2.stringWidth("Score: " + this.Point_Eaten)) / 3);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int exitGame = JOptionPane.showConfirmDialog(null, "Do you want play game again?", "Exit game?", JOptionPane.INFORMATION_MESSAGE);
                switch (exitGame) {
                    case JOptionPane.YES_OPTION:
                        int size = UNIT_SIZE * UNIT_SIZE;
                        x = null;
                        x = new int[size];
                        y = null;
                        y = new int[size];
                        bodySnake = 5;
                        Point_Eaten = 0;
                        towards = 'R';
                        startGame();
                        break;
                    case JOptionPane.NO_OPTION:
                        System.exit(0);
                }
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(this.running){
            this.move();
            this.checkPoint();
            this.checkCollision();
            repaint();
        }
    }
}
