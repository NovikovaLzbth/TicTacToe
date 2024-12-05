import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Сервер ждет клиента...");

        try (Socket clientSocket = serverSocket.accept();
             InputStream inputStream = clientSocket.getInputStream();
             OutputStream outputStream = clientSocket.getOutputStream()) {

            System.out.println("Новое соединение: " + clientSocket.getInetAddress().toString());
            char[][] board = new char[3][3];
            int request;
            while ((request = inputStream.read()) != -1) {
                updateBoard(board, request, 'O'); // 'O' для сервера
                if (checkWin(board, 'O')) {
                    System.out.println("Сервер выиграл!");
                    break;
                }
                // Логика для сервера, чтобы сделать ход
                int serverMove = makeServerMove(board);
                outputStream.write(serverMove);
                updateBoard(board, serverMove, 'X'); // 'X' для клиента
                if (checkWin(board, 'X')) {
                    System.out.println("Клиент выиграл!");
                    break;
                }
            }
            System.out.println("Клиент отключился");
        }
    }

    private static void updateBoard(char[][] board, int move, char symbol) {
        int row = move / 3;
        int col = move % 3;
        board[row][col] = symbol;
    }

    private static int makeServerMove(char[][] board) {
        // Простой алгоритм для сервера, чтобы сделать ход
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    return i * 3 + j; // Возвращает позицию
                }
            }
        }
        return -1; // Если нет доступных ходов
    }

    private static boolean checkWin(char[][] board, char symbol) {
        // Проверка строк, столбцов и диагоналей
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
