package ci.ada.adapter;

import ci.ada.dao.UserAccountDao;
import ci.ada.models.Admin;
import ci.ada.models.UserAccount;
import ci.ada.models.enumeration.Privileges;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OdsAdminAdapter implements DataReader<Admin> {

    private final String filePath;
    private final UserAccountDao userAccountDao;

    public OdsAdminAdapter(String filePath, UserAccountDao userAccountDao) {
        this.filePath = filePath;
        this.userAccountDao = userAccountDao;
    }

    @Override
    public List<Admin> getData() {
        List<Admin> admins = new ArrayList<>();
        try {
            Sheet sheet = SpreadSheet.createFromFile(new File(filePath)).getSheet(0);

            for (int row = 1; row < sheet.getRowCount(); row++) {
                try {
                    // Lecture des colonnes
                    String idStr = sheet.getCellAt(0, row).getTextValue();
                    String lastname = sheet.getCellAt(1, row).getTextValue();
                    String firstname = sheet.getCellAt(2, row).getTextValue();
                    String phonenumber = sheet.getCellAt(3, row).getTextValue();
                    String email = sheet.getCellAt(4, row).getTextValue();
                    String privilegesStr = sheet.getCellAt(5, row).getTextValue();
                    String idUserAccountStr = sheet.getCellAt(6, row).getTextValue();

                    // Conversion en Long
                    Long id = Long.parseLong(idStr);
                    Long idUserAccount = Long.parseLong(idUserAccountStr);

                    // Récupération UserAccount par ID
                    UserAccount account = userAccountDao.findById(idUserAccount);

                    if (account != null) {
                        // Conversion String -> List<Privileges>
                        List<Privileges> privilegesList = new ArrayList<>();
                        if (privilegesStr != null && !privilegesStr.isEmpty()) {
                            privilegesList = Arrays.stream(privilegesStr.split(","))
                                    .map(String::trim)
                                    .map(Privileges::valueOf)
                                    .collect(Collectors.toList());
                        }

                        // Création de l'Admin
                        Admin admin = new Admin();
                        admin.setId(id);
                        admin.setLastName(lastname);
                        admin.setFirstName(firstname);
                        admin.setPhoneNumber(phonenumber);
                        admin.setEmail(email);
                        admin.setPrivileges(privilegesList);
                        admin.setUserAccount(account);

                        admins.add(admin);
                    } else {
                        System.err.println("⚠️ UserAccount introuvable pour l'id : " + idUserAccount);
                    }

                } catch (NumberFormatException e) {
                    System.err.println("⚠️ Erreur de conversion ID à la ligne " + (row + 1));
                } catch (Exception e) {
                    System.err.println("⚠️ Erreur lors du traitement de la ligne " + (row + 1) + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return admins;
    }
}
