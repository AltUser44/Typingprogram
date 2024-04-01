import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

class WordLibrary {
    private static final String[] WORDS = {
            "apple", "banana", "cherry", "date", "fig", "grape", "kiwi",
            "lemon", "mango", "nectarine", "orange", "peach", "quince",
            "raspberry", "strawberry", "tangerine", "watermelon"
    };

    private Random random = new Random();

    public String generateSentence(int wordCount) {
        StringBuilder sentence = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            sentence.append(WORDS[random.nextInt(WORDS.length)]);
            if (i < wordCount - 1) {
                sentence.append(" ");
            }
        }
        sentence.append(".");
        return sentence.toString().substring(0, 1).toUpperCase() + sentence.substring(1);
    }
}

public class TypingScreen extends JFrame implements KeyListener {
    private static final int HIGH_WPM_THRESHOLD = 100;  // Define what you consider a high WPM
    private static final String[] CONGRATULATIONS_MESSAGES = {
            "You're Awesome!", "You Rock!", "CONGRATS!", "WOW!!", "UNBELIEVABLE!"
    };

    private static final int SCREEN_WIDTH = 1000;
    private static final int SCREEN_HEIGHT = 1000;
    private static final int FONT_SIZE = 40;
    private static final int TEXT_X = 40;
    private static final int TEXT_Y = 140;
    private static final int WPM_Y = 80;
    private static final Color CORRECT_COLOR = new Color(0, 128, 0);
    private static final Color INCORRECT_COLOR = new Color(128, 0, 0);

    private JLabel textLabel;
    private JTextField textField;
    private JLabel wpmLabel;
    private JLabel congratsLabel;

    private WordLibrary wordLibrary = new WordLibrary();
    private String currentSentence;
    private long startTime;
    private int wordsPerMinute;

    public TypingScreen() {
        super("Typing Screen");
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        currentSentence = wordLibrary.generateSentence(7);  // Generate a 5-word sentence.

        textLabel = new JLabel(currentSentence);
        textLabel.setFont(new Font("Arial", Font.PLAIN, FONT_SIZE));
        textLabel.setBounds(TEXT_X, TEXT_Y, SCREEN_WIDTH, FONT_SIZE);
        add(textLabel);

        textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, FONT_SIZE));
        textField.setBounds(TEXT_X, TEXT_Y + FONT_SIZE + 10, SCREEN_WIDTH - TEXT_X * 2, FONT_SIZE);
        textField.addKeyListener(this);
        add(textField);

        wpmLabel = new JLabel("WPM: 0");
        wpmLabel.setFont(new Font("Arial", Font.PLAIN, FONT_SIZE));
        wpmLabel.setBounds(TEXT_X, WPM_Y, SCREEN_WIDTH, FONT_SIZE);
        add(wpmLabel);

        congratsLabel = new JLabel("");
        congratsLabel.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        congratsLabel.setBounds(TEXT_X, WPM_Y + FONT_SIZE + 20, SCREEN_WIDTH, FONT_SIZE);
        congratsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(congratsLabel);

        setVisible(true);
        startTime = System.currentTimeMillis();
    }

    private void updateWPM() {
        int numChars = textField.getText().length();
        long elapsedTime = System.currentTimeMillis() - startTime;
        double minutes = elapsedTime / 60000.0;
        wordsPerMinute = (int) ((numChars / 5) / minutes);
        wpmLabel.setText(String.format("WPM: %d", wordsPerMinute));
    }

    private void showCongratulations() {
        if (wordsPerMinute >= HIGH_WPM_THRESHOLD) {
            Random rand = new Random();
            String message = CONGRATULATIONS_MESSAGES[rand.nextInt(CONGRATULATIONS_MESSAGES.length)];
            congratsLabel.setText(message);
        } else {
            congratsLabel.setText("");
        }
    }

    private void resetTypingTest() {
        currentSentence = wordLibrary.generateSentence(5);
        textLabel.setText(currentSentence);
        textField.setText("");
        textField.setForeground(Color.BLACK);
        textField.setEnabled(true);
        startTime = System.currentTimeMillis();
        showCongratulations();  // Show congratulations if the user achieved a high WPM.
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        updateWPM();
        if (textField.getText().length() == currentSentence.length()) {
            resetTypingTest();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        String text = textField.getText();
        int pos = text.length();
        if (pos < currentSentence.length() && e.getKeyChar() == currentSentence.charAt(pos)) {
            textField.setForeground(CORRECT_COLOR);
        } else {
            textField.setForeground(INCORRECT_COLOR);
        }
    }

    public static void main(String[] args) {
        new TypingScreen();
    }
}
