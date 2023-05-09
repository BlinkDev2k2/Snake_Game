import javax.swing.*;

public class Game_Frame extends JFrame {
    public Game_Frame() {
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(3);
        this.setResizable(false);
        this.add(new Game_Panel());
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
