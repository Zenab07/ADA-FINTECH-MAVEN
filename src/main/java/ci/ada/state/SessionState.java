package ci.ada.state;

import java.util.Scanner;

public interface SessionState {
    void login(SessionManager context, String login, String password, Scanner scanner);
    void logout(SessionManager context);
}
