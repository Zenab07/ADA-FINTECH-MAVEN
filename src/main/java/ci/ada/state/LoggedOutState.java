package ci.ada.state;

import java.util.Scanner;

public class LoggedOutState implements SessionState {

    @Override
    public void login(SessionManager context, String login, String password, Scanner scanner) {
        boolean success = context.getLoginService().processLogin(login, password, scanner);
        if (success) {
            context.setState(new LoggedInState());
        } else {
            System.out.println("❌ Échec de la connexion !");
        }
    }

    @Override
    public void logout(SessionManager context) {
        System.out.println("⚠️ Aucun utilisateur n'est actuellement connecté.");
    }
}
