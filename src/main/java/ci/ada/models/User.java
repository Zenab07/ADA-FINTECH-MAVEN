package ci.ada.models;

import java.util.concurrent.BlockingQueue;

public class User extends BasicInfo{

    private String profilePage;

    private Wallet wallet;


    public User(Builder<?> builder) {
        super(builder);
        this.profilePage = builder.profilePage;
        this.wallet = builder.wallet;
    }

    public User() {
        super();
    }

    public String getProfilePage() {
        return profilePage;
    }

    public void setProfilePage(String profilePage) {
        this.profilePage = profilePage;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }


    public abstract static class Builder <T extends Builder<T>> extends BasicInfo.Builder<T>{
        protected String profilePage;
        private Wallet wallet;

        public abstract T self();

        public T profilePage(String profilePage) {
            this.profilePage = profilePage;
            return self();
        }

        public T wallet(Wallet wallet){
            this.wallet = wallet;
            return self();
        }
    }

    public static class UserBuilder extends Builder<UserBuilder>{


        @Override
        public UserBuilder self() {
            return this;
        }
    }
}
