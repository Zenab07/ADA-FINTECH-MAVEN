package ci.ada.state;

public class LoggedInState implements SessionState {

    @Override
    public void login(SessionManager context, String login, String password, Scanner scanner) {
        System.out.println("⚠️ Vous êtes déjà connecté !");
    }

    @Override
    public void logout(SessionManager context) {
        System.out.println("✅ Déconnexion réussie !");
        context.setState(new LoggedOutState());
    }
}
