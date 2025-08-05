package ci.ada.models;

public class BasicInfo {

    protected Long id;

    protected String lastName;

    protected String firstName;

    protected String phoneNumber;

    protected String email;

    protected UserAccount userAccount; //l'enfant reçoit le parent, dans notre projet User est l'enfant et UserAccount est le parent, où le bout de la flèche est c'est lui le parent(c'est une relation unidirectionnelle)

    //private Wallet wallet; // ici on a une relation bidirectionnele entre User et Wallet donc on créé cette ligne ici et on créé "private User user" dans la class User

    //public Wallet getWallet() {
    //return wallet;
    //}

    //public void setWallet(Wallet wallet) {
    //this.wallet = wallet;
    //}

    public BasicInfo(Builder<?> builder) {
        this.lastName = builder.lastName;
        this.firstName = builder.firstName;
        this.phoneNumber = builder.phoneNumber;
        this.email = builder.email;
        this.userAccount = builder.userAccount;
    }

    public BasicInfo() {

    }

    public String afficherInfo() {
        return "ID: " + getId() + " | " + getLastName() + " " + getFirstName() + " " +
                getPhoneNumber() + " " + getEmail();
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public abstract static class Builder <T extends Builder <T>>  {  // Builder

        protected Long id;

        protected String lastName;

        protected String firstName;

        protected String phoneNumber;

        protected String email;

        protected UserAccount userAccount;

        public abstract T self();

        public T id(Long id) {
            this.id = id;
            return self();
        }

        public T lastName(String lastName) {
            this.lastName = lastName;
            return self();
        }

        public T firstName(String firstName) {
            this.firstName = firstName;
            return self();
        }

        public T phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return self();
        }

        public T email(String email) {
            this.email = email;
            return self();
        }

        public T userAccount(UserAccount userAccount) {
            this.userAccount = userAccount;
            return self();
        }
    }

    public static class BasicInfoBuilder extends Builder<BasicInfoBuilder>{

        @Override
        public BasicInfoBuilder self() {
            return this;
        }
    }

}
