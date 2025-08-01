package ci.ada.services;

import ci.ada.dao.*;
import ci.ada.models.*;
import ci.ada.models.enumeration.CompteType;
import ci.ada.models.enumeration.Gender;
import ci.ada.models.enumeration.Privileges;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Scanner;

public class RegistrationService {

    private final UserAccountDao userAccountDao = new UserAccountDao();
    private final WalletDao walletDao = new WalletDao();
    private final AdminDao adminDao = new AdminDao();
    private final CustomerDao customerDao = new CustomerDao();
    private final MerchantDao merchantDao = new MerchantDao();

    public void registerUser(Scanner saisie) {
        try {
            System.out.println("\n\t##########################################");
            System.out.println("\t#              MENU INSCRIPTION          #");
            System.out.println("\t##########################################");
            System.out.println("\t# 1. Admin                               #");
            System.out.println("\t# 2. Client                              #");
            System.out.println("\t# 3. Marchand                            #");
            System.out.println("\t# 0. Retour                              #");
            System.out.println("\t##########################################");
            System.out.print("Votre choix : ");
            int typeCompte = saisie.nextInt();
            saisie.nextLine();

            if (typeCompte == 0) return;

            //  SAISIE INFOS PERSONNELLES
            System.out.print("Nom : ");
            String lastName = saisie.nextLine().trim();
            System.out.print("Prénom : ");
            String firstName = saisie.nextLine().trim();
            System.out.print("Téléphone : ");
            String phone = saisie.nextLine().trim();
            System.out.print("Email : ");
            String email = saisie.nextLine().trim();

            if (lastName.isEmpty() || firstName.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                System.out.println("❌ Tous les champs doivent être remplis !");
                return;
            }

            List<Privileges> privileges = null;
            Gender gender = null;
            String location = "";

            if (typeCompte == 1) {
                System.out.print("Privilèges (MANAGE_USERS, VIEW_REPORTS) séparés par des virgules : ");
                String privilegeInput = saisie.nextLine().trim();
                try {
                    privileges = Arrays.asList(privilegeInput.split(","))
                            .stream()
                            .map(s -> Privileges.valueOf(s.trim().toUpperCase()))
                            .collect(Collectors.toList());
                } catch (IllegalArgumentException e) {
                    System.out.println("❌ Privileges invalides. Valeurs possibles : MANAGE_USERS, VIEW_REPORTS");
                    return;
                }
            } else if (typeCompte == 2) {
                System.out.print("Genre (MAN/WOMAN): ");
                try {
                    gender = Gender.valueOf(saisie.nextLine().trim().toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("❌ Genre invalide. Valeurs possibles : MAN ou WOMAN");
                    return;
                }
            } else if (typeCompte == 3) {
                System.out.print("Localisation : ");
                location = saisie.nextLine().trim();
                if (location.isEmpty()) {
                    System.out.println("❌ La localisation ne peut pas être vide !");
                    return;
                }
            }

            // LOGIN / PASSWORD
            System.out.print("Login : ");
            String login = saisie.nextLine().trim();
            System.out.print("Mot de passe : ");
            String password = saisie.nextLine().trim();

            if (login.isEmpty() || password.isEmpty()) {
                System.out.println("❌ Login et mot de passe obligatoires !");
                return;
            }

            // Vérifier si le login existe déjà
            if (userAccountDao.existsLogin(login)) {
                System.out.println("❌ Ce login est déjà utilisé. Veuillez en choisir un autre.");
                return;
            }

            // Création du compte utilisateur
            CompteType compteType = null;
            switch (typeCompte) {
                case 1:
                    compteType = CompteType.ADMIN;
                    break;
                case 2:
                    compteType = CompteType.CUSTOMER;
                    break;
                case 3:
                    compteType = CompteType.MERCHANT;
                    break;
                default:
                    System.out.println("❌ Type de compte invalide");
                    return;
            }

            UserAccount userAccount = new UserAccount(login, password, compteType);
            userAccountDao.createUser(userAccount);

            // ============================
            // INSERTION SELON LE TYPE DE COMPTE
            // ============================
            switch (typeCompte) {
                case 1:
                    if (userAccount.getId() == null) {
                        System.out.println("❌ Erreur : l'ID du UserAccount est introuvable.");
                        return;
                    }
                    Admin admin = new Admin(lastName, firstName, phone, email, userAccount, privileges);
                    adminDao.createAdmin(admin);
                    System.out.println("✅ Inscription Admin réussie !");
                    break;

                case 2:
                    Wallet wallet = new Wallet();
                    wallet.setBalance(BigDecimal.ZERO);
                    wallet.setUserAccount(userAccount);
                    walletDao.createWallet(wallet);

                    Customer customer = new Customer(lastName, firstName, phone, email, gender, userAccount, wallet, "");
                    customerDao.createCustomer(customer);
                    System.out.println("✅ Inscription Client réussie !");
                    break;

                case 3:
                    Wallet wallet2 = new Wallet();
                    wallet2.setBalance(BigDecimal.ZERO);
                    wallet2.setUserAccount(userAccount);
                    walletDao.createWallet(wallet2);

                    Merchant merchant = new Merchant(lastName, firstName, phone, email, location, userAccount, wallet2, "");
                    merchantDao.createMerchant(merchant);
                    System.out.println("✅ Inscription Marchand réussie !");
                    break;

                default:
                    System.out.println("❌ Type de compte inconnu.");
                    return;
            }
        } catch (Exception e) {
            System.out.println("❌ Erreur lors de l'inscription : " + e.getMessage());
        }
    }
}
