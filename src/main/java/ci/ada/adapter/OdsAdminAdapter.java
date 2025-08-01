package ci.ada.adapter;

import ci.ada.dao.AdminDao;
import ci.ada.dao.UserAccountDao;
import ci.ada.models.Admin;
import ci.ada.models.UserAccount;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
                String idStr = sheet.getCellAt(0, row).getTextValue();
                String lastname = sheet.getCellAt(1, row).getTextValue();
                String firstname = sheet.getCellAt(2, row).getTextValue();
                String phonenumber = sheet.getCellAt(3, row).getTextValue();
                String email = sheet.getCellAt(4, row).getTextValue();
                String privileges = sheet.getCellAt(5, row).getTextValue();
                String idUserAccountStr = sheet.getCellAt(6, row).getTextValue();

                Long id = null;
                Long idUserAccount = null;
                try {
                    id = Long.parseLong(idStr);
                    idUserAccount = Long.parseLong(idUserAccountStr);
                } catch (NumberFormatException e) {
                    System.err.println("Format d'id incorrect à la ligne " + (row + 1));
                    continue; // on passe à la ligne suivante
                }

                // Recherche UserAccount par ID (méthode à créer dans UserAccountDao)
                UserAccount account = userAccountDao.findById(idUserAccount);

                if (account != null) {
                    Admin admin = new Admin();
                    admin.setId(id);
                    admin.setLastName(lastname);
                    admin.setFirstName(firstname);
                    admin.setPhoneNumber(phonenumber);
                    admin.setEmail(email);
                    admin.setPrivileges(privileges); // adapte selon ton modèle Admin
                    admin.setUserAccount(account);

                    admins.add(admin);
                } else {
                    System.err.println("UserAccount introuvable pour id : " + idUserAccount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return admins;
    }
}
