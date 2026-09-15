import javax.swing.*;
import java.awt.*;

public class ConnectFour extends JFrame {

    private final int ROWS = 6;
    private final int COLS = 7;

    private JButton[] columnButtons;
    private JPanel boardPanel;
    private JLabel statusLabel;

    private int[][] board = new int[ROWS][COLS];

    private int currentPlayer = 1;
    private boolean gameOver = false;

    public ConnectFour() {

        setTitle("Connect Four");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("CONNECT FOUR", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        add(title, BorderLayout.NORTH);

        // Main board
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(ROWS, COLS, 5, 5));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        createBoard();

        add(boardPanel, BorderLayout.CENTER);

        // Bottom section
        JPanel bottomPanel = new JPanel(new BorderLayout());

        statusLabel = new JLabel(
                "Player 1's Turn 🔴",
                SwingConstants.CENTER
        );

        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JButton restartButton = new JButton("New Game");
        restartButton.setFont(new Font("Arial", Font.BOLD, 16));

        restartButton.addActionListener(e -> resetGame());

        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        bottomPanel.add(restartButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void createBoard() {

        columnButtons = new JButton[COLS];

        for (int col = 0; col < COLS; col++) {

            final int selectedColumn = col;

            columnButtons[col] = new JButton("↓");
            columnButtons[col].setFont(new Font("Arial", Font.BOLD, 18));

            columnButtons[col].addActionListener(
                    e -> dropPiece(selectedColumn)
            );

            addColumnButton(columnButtons[col]);
        }

        updateBoard();
    }

    private void addColumnButton(JButton button) {

        JPanel wrapper = new JPanel(new BorderLayout());

        wrapper.add(button, BorderLayout.NORTH);

        boardPanel.add(wrapper);
    }

    private void dropPiece(int col) {

        if (gameOver) {
            return;
        }

        int row = getAvailableRow(col);

        if (row == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "This column is full!",
                    "Invalid Move",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        board[row][col] = currentPlayer;

        updateBoard();

        if (checkWin(row, col)) {

            gameOver = true;

            String winner = currentPlayer == 1
                    ? "🔴 Player 1 Wins!"
                    : "🟡 Player 2 Wins!";

            statusLabel.setText(winner);

            JOptionPane.showMessageDialog(
                    this,
                    winner,
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        if (isBoardFull()) {

            gameOver = true;
            statusLabel.setText("It's a Draw!");

            JOptionPane.showMessageDialog(
                    this,
                    "The game is a draw!",
                    "Game Over",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        currentPlayer = currentPlayer == 1 ? 2 : 1;

        updateStatus();
    }

    private int getAvailableRow(int col) {

        for (int row = ROWS - 1; row >= 0; row--) {

            if (board[row][col] == 0) {
                return row;
            }
        }

        return -1;
    }

    private boolean checkWin(int row, int col) {

        int player = board[row][col];

        return count(row, col, 0, 1, player)
                + count(row, col, 0, -1, player) - 1 >= 4

                || count(row, col, 1, 0, player)
                + count(row, col, -1, 0, player) - 1 >= 4

                || count(row, col, 1, 1, player)
                + count(row, col, -1, -1, player) - 1 >= 4

                || count(row, col, 1, -1, player)
                + count(row, col, -1, 1, player) - 1 >= 4;
    }

    private int count(
            int row,
            int col,
            int rowDirection,
            int colDirection,
            int player) {

        int count = 0;

        while (
                row >= 0 &&
                row < ROWS &&
                col >= 0 &&
                col < COLS &&
                board[row][col] == player
        ) {

            count++;

            row += rowDirection;
            col += colDirection;
        }

        return count;
    }

    private boolean isBoardFull() {

        for (int col = 0; col < COLS; col++) {

            if (board[0][col] == 0) {
                return false;
            }
        }

        return true;
    }

    private void updateBoard() {

        boardPanel.removeAll();

        for (int row = 0; row < ROWS; row++) {

            for (int col = 0; col < COLS; col++) {

                JPanel cell = new JPanel();
                cell.setBorder(
                        BorderFactory.createLineBorder(Color.GRAY)
                );

                if (board[row][col] == 1) {

                    cell.setBackground(Color.RED);

                } else if (board[row][col] == 2) {

                    cell.setBackground(Color.YELLOW);

                } else {

                    cell.setBackground(Color.WHITE);
                }

                boardPanel.add(cell);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private void updateStatus() {

        if (currentPlayer == 1) {

            statusLabel.setText("Player 1's Turn 🔴");

        } else {

            statusLabel.setText("Player 2's Turn 🟡");
        }
    }

    private void resetGame() {

        board = new int[ROWS][COLS];

        currentPlayer = 1;
        gameOver = false;

        updateBoard();
        updateStatus();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(
                ConnectFour::new
        );
    }
}
