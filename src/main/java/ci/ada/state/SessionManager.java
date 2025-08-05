package ci.ada.state;

import ci.ada.services.LoginService;
import java.util.Scanner;

public class SessionManager {
    private SessionState state;
    private final LoginService loginService;

    public SessionManager(LoginService loginService) {
        this.loginService = loginService;
        this.state = new LoggedOutState(); // état par défaut
    }

    public void setState(SessionState state) {
        this.state = state;
    }

    public LoginService getLoginService() {
        return loginService;
    }

    public void login(String login, String password, Scanner scanner) {
        state.login(this, login, password, scanner);
    }

    public void logout() {
        state.logout(this);
    }
}
