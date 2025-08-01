package ci.ada.adapter;

import ci.ada.models.UserAccount;
import ci.ada.models.enumeration.CompteType;
import org.jopendocument.dom.spreadsheet.SpreadSheet;
import org.jopendocument.dom.spreadsheet.Sheet;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OdsUserAccountAdapter implements DataReader<UserAccount> {

    private final String filePath;

    public OdsUserAccountAdapter(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<UserAccount> getData() {
        List<UserAccount> accounts = new ArrayList<>();
        try {
            Sheet sheet = SpreadSheet.createFromFile(new File(filePath)).getSheet(0);


            for (int row = 1; row < sheet.getRowCount(); row++) {
                String login = sheet.getCellAt(0, row).getTextValue();
                String password = sheet.getCellAt(1, row).getTextValue();
                String typeString = sheet.getCellAt(2, row).getTextValue().toUpperCase();

                CompteType type = CompteType.valueOf(typeString);

                UserAccount account = new UserAccount(login, password, type);
                accounts.add(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }
}
