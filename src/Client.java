import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080);
             InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream();
             Scanner scanner = new Scanner(System.in)) {

            char[][] board = new char[3][3];
            int move;
            while (true) {
                printBoard(board);
                System.out.print("Введите Вашу позицию (0-8): ");
                move = scanner.nextInt();
                outputStream.write(move);

                int serverMove = inputStream.read();
                if (serverMove == -1) break; // если сервер отключился
                updateBoard(board, move, 'X'); // 'X' для клиента
                updateBoard(board, serverMove, 'O'); // 'O' для сервера

                // проверка на победу или ничью
                if (checkWin(board, 'X') || checkWin(board, 'O')) {
                    printBoard(board);
                    System.out.println("Игра окончена!");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printBoard(char[][] board) {
        for (char[] row : board) {
            for (char cell : row) {
                System.out.print((cell == 0 ? '.' : cell) + " ");
            }
            System.out.println();
        }
    }

    private static void updateBoard(char[][] board, int move, char symbol) {
        int row = move / 3;
        int col = move % 3;
        board[row][col] = symbol;
    }

    private static boolean checkWin(char[][] board, char symbol) {
        // проверка строк, столбцов и диагоналей
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) ||
                    (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol)) {
                return true;
            }
        }
        return (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
                (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol);
    }
}
