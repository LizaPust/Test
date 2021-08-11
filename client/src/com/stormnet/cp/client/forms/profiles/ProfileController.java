package com.stormnet.cp.client.forms.profiles;

import com.stormnet.cp.data.Account;
import com.stormnet.cp.data.Country;
import com.stormnet.cp.utils.SocketJsonStreams;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.JSONWriter;

import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class ProfileController implements Initializable {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox isEnabled;

    @FXML
    private DatePicker birthday;

    @FXML
    private ComboBox<Country> countries;

    @FXML
    private TextField newCountry;

    @FXML
    private TableView<Account> allAccountsGrig;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        countries.setCellFactory(new CountryComboFactory());

        reloadCountyCombobox();

        TableColumn<Account, CheckBox> isEnabledCheckColumn = new TableColumn<>("Is Enabled Check");
        isEnabledCheckColumn.setMinWidth(150);
        isEnabledCheckColumn.setCellValueFactory(new IsActiveCheckboxFactory());

        TableColumn<Account, GridPane> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setMinWidth(300);
        actionsColumn.setCellValueFactory(new ButtonsCellFactory());

        ObservableList<TableColumn<Account, ?>> columns = allAccountsGrig.getColumns();
        columns.add(3, isEnabledCheckColumn);
        columns.add(actionsColumn);

        reloadAccountsGrid();
    }

    public void addCountryBtnPressed() {
//        String country = newCountry.getText();
//        ObservableList<String> allCountries = countries.getItems();
//
//        allCountries.add(country);
    }

    public void editBtnPressed(Account editedAccount) throws Exception {

        InetAddress ip = InetAddress.getByName("127.0.0.1");
        Socket clientSocket = new Socket(ip, 8848);

        SocketJsonStreams streams = new SocketJsonStreams(clientSocket);
        JSONWriter jsonWriter = streams.getJsonWriter();

        jsonWriter.object();
        jsonWriter.key("request-header").object();
        jsonWriter.key("command-name").value("edit-account");
        jsonWriter.endObject();

        jsonWriter.key("request-data").object();
        jsonWriter.key("login").value(editedAccount.getLogin());
        jsonWriter.key("password").value(editedAccount.getPassword());
        jsonWriter.endObject();
        jsonWriter.endObject();

        streams.flushWriter();

        clearForm();
        reloadAccountsGrid();
    }

    public void deleteBtnPressed(Account deletedAccount) throws Exception {

        InetAddress ip = InetAddress.getByName("127.0.0.1");
        Socket clientSocket = new Socket(ip, 8848);

        SocketJsonStreams streams = new SocketJsonStreams(clientSocket);
        JSONWriter jsonWriter = streams.getJsonWriter();

        jsonWriter.object();
        jsonWriter.key("request-header").object();
        jsonWriter.key("command-name").value("delete-account");
        jsonWriter.endObject();

        jsonWriter.key("request-data").object();
        jsonWriter.key("login").value(deletedAccount.getLogin());
        jsonWriter.key("password").value(deletedAccount.getPassword());
        jsonWriter.key("enabled").value(deletedAccount.isEnabled());
        jsonWriter.key("birthday").value(deletedAccount.getBirthday());
        jsonWriter.key("country").value(deletedAccount.getCountry());
        jsonWriter.endObject();
        jsonWriter.endObject();

        streams.flushWriter();

        clearForm();
        reloadAccountsGrid();
    }

    public void saveBtnPressed() throws Exception {
        String login = loginField.getText();
        String password = passwordField.getText();
        boolean enabled = isEnabled.isSelected();
        String country = countries.getValue().getName();
        LocalDate birthdayDate = birthday.getValue();

        Account account = new Account();
        account.setLogin(login);
        account.setPassword(password);
        account.setEnabled(enabled);
        account.setCountry(country);
        account.setBirthday(birthdayDate);

        InetAddress ip = InetAddress.getByName("127.0.0.1");
        Socket clientSocket = new Socket(ip, 8848);

        SocketJsonStreams streams = new SocketJsonStreams(clientSocket);
        JSONWriter jsonWriter = streams.getJsonWriter();

        jsonWriter.object();
        jsonWriter.key("request-header").object();
        jsonWriter.key("command-name").value("save-account");
        jsonWriter.endObject();

        jsonWriter.key("request-data").object();
        jsonWriter.key("login").value(account.getLogin());
        jsonWriter.key("password").value(account.getPassword());
        jsonWriter.key("enabled").value(account.isEnabled());
        jsonWriter.key("birthday").value(account.getBirthday());
        jsonWriter.key("country").value(account.getCountry());
        jsonWriter.endObject();
        jsonWriter.endObject();

        streams.flushWriter();

        clearForm();
        reloadAccountsGrid();
    }

    private void clearForm() {
        loginField.setText("");
        passwordField.setText("");
        isEnabled.setSelected(false);
        birthday.setValue(null);
    }

    private void reloadAccountsGrid() {
        ObservableList<Account> allAccountsItems = allAccountsGrig.getItems();
        allAccountsItems.clear();

        try {
            InetAddress ip = InetAddress.getByName("127.0.0.1");
            Socket clientSocket = new Socket(ip, 8848);

            SocketJsonStreams streams = new SocketJsonStreams(clientSocket);
            JSONWriter jsonWriter = streams.getJsonWriter();

            jsonWriter.object();
            jsonWriter.key("request-header").object();
            jsonWriter.key("command-name").value("get-all-accounts");
            jsonWriter.endObject();
            jsonWriter.endObject();

            streams.flushWriter();

            JSONTokener jsonTokener = streams.getJsonTokener();
            JSONObject response = (JSONObject) jsonTokener.nextValue();

            JSONObject responseHeader = response.getJSONObject("response-header");
            JSONArray responseData = response.getJSONArray("response-data");

            for (int i = 0; i < responseData.length(); i++) {
                JSONObject accountJson = responseData.getJSONObject(i);

                String login = accountJson.getString("login");
                String password = accountJson.getString("password");
                boolean enabled = accountJson.getBoolean("enabled");
                String birthdayStr = accountJson.getString("birthday");
                LocalDate birthday = LocalDate.parse(birthdayStr);
                String country = accountJson.getString("country");

                Account account = new Account();
                account.setLogin(login);
                account.setPassword(password);
                account.setEnabled(enabled);
                account.setCountry(country);
                account.setBirthday(birthday);

                allAccountsItems.add(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reloadCountyCombobox() {
        try {
            InetAddress ip = InetAddress.getByName("127.0.0.1");
            Socket clientSocket = new Socket(ip, 8848);

            SocketJsonStreams streams = new SocketJsonStreams(clientSocket);
            JSONWriter jsonWriter = streams.getJsonWriter();

            jsonWriter.object();
            jsonWriter.key("request-header").object();
            jsonWriter.key("command-name").value("get-all-countries");
            jsonWriter.endObject();
            jsonWriter.endObject();

            streams.flushWriter();

            JSONTokener jsonTokener = streams.getJsonTokener();
            JSONObject response = (JSONObject) jsonTokener.nextValue();

            JSONObject responseHeader = response.getJSONObject("response-header");
            JSONArray responseData = response.getJSONArray("response-data");

            ObservableList<Country> countryItems = countries.getItems();
            countryItems.clear();

            for (int i = 0; i < responseData.length(); i++) {
                JSONObject countryJson = responseData.getJSONObject(i);

                Integer id = countryJson.getInt("id");
                String name = countryJson.getString("name");
                String code = countryJson.getString("code");

                Country country = new Country(id, name, code);

                countryItems.add(country);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class IsActiveCheckboxFactory implements Callback<TableColumn.CellDataFeatures<Account, CheckBox>, ObservableValue<CheckBox>> {

        @Override
        public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<Account, CheckBox> param) {
            Account account = param.getValue();
            boolean isEnabled = account.isEnabled();

            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().setValue(isEnabled);

            checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
                public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
                    account.setEnabled(newValue);
                }
            });

            return new SimpleObjectProperty<>(checkBox);
        }
    }

    public class ButtonsCellFactory implements Callback<TableColumn.CellDataFeatures<Account, GridPane>, ObservableValue<GridPane>> {


        @Override
        public ObservableValue<GridPane> call(TableColumn.CellDataFeatures<Account, GridPane> param) {
            Account person = param.getValue();

            Button editBtn = new Button("EDIT");
            editBtn.setOnAction(new EditAccountEvent(person));

            Button deleteBtn = new Button("DELETE");
            deleteBtn.setOnAction(new DeleteAccountEvent(person));

            GridPane panel = new GridPane();
            panel.setHgap(10);
            panel.setVgap(10);
            panel.setPadding(new Insets(25, 25, 25, 25));

            panel.add(editBtn, 0, 0);

            panel.add(deleteBtn, 1, 0);

            return new SimpleObjectProperty<>(panel);
        }
    }

    private class CountryComboFactory implements Callback<ListView<Country>, ListCell<Country>> {

        @Override
        public ListCell<Country> call(ListView<Country> param) {
            final ListCell<Country> cell = new ListCell<Country>(){

                @Override
                protected void updateItem(Country t, boolean bln) {
                    super.updateItem(t, bln);

                    if(t != null){
                        setText(t.getCode());
                    }else{
                        setText(null);
                    }
                }
            };

            return cell;
        }
    }


    private class EditAccountEvent implements EventHandler<ActionEvent> {

        private Account editedAccount;

        public EditAccountEvent(Account editedAccount) {
            this.editedAccount = editedAccount;
        }

        @Override
        public void handle(ActionEvent event) {
           // ObservableList<Account> items = allAccountsGrig.getItems();
            try {
                editBtnPressed(editedAccount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            loginField.setText(editedAccount.getLogin());
            passwordField.setText(editedAccount.getPassword());
        }
    }

    private class DeleteAccountEvent implements EventHandler<ActionEvent> {

       private Account deletedAccount;

        public DeleteAccountEvent(Account deletedAccount) {
            this.deletedAccount = deletedAccount;
       }

        @Override
        public void handle(ActionEvent event) {
           ObservableList<Account> items = allAccountsGrig.getItems();
            try {
                deleteBtnPressed(deletedAccount);
            } catch (Exception e) {
                e.printStackTrace();
            }
            items.remove(deletedAccount);
        }
    }
}