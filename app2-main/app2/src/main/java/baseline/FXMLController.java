
package baseline;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.*;


public class FXMLController implements Initializable {
    @FXML
    private AnchorPane Anchor_pane;

    @FXML
    private Button add_item_button;

    @FXML
    private Button delete_all_button;

    @FXML
    private MenuButton drop_down_save_load;

    @FXML
    private MenuItem load_saved;

    @FXML
    private MenuItem save_as;

    @FXML
    private Text search_text_text;

    @FXML
    private SplitPane split_pane;

    @FXML
    private Button delete_item_button;

    @FXML
    private TextField name_text_field;

    @FXML
    private TextField price_text_field;

    @FXML
    private TextField search_bar_text_field;

    @FXML
    private TextField ser;

    @FXML
    private TextField ser1;

    @FXML
    private TextField ser2;

    @FXML
    private TextField ser3;

    @FXML
    private TableColumn<itemgettersetter, String> name = new TableColumn<>("Name");

    @FXML
    private TableColumn<itemgettersetter, String> serial_number = new TableColumn<>("SerialNumber");

    @FXML
    private TableColumn<itemgettersetter, Double> value = new TableColumn<>("Value");


    @FXML
    private TableView<itemgettersetter> table_view = new TableView<>();

    ObservableList<itemgettersetter> list = FXCollections.observableArrayList();

    @FXML
    public void addtolist() {
        //get the text from the user input and diplay it on the tableview
        //add to database
        //serial number builder
        String serialnumber = ser.getText() + "-" + ser1.getText() + "-" + ser2.getText() + "-" + ser3.getText();

        Boolean serialstatus=true;
        Boolean namestatus=true;
        for(itemgettersetter d : list){
            if(d.getSerialNumber() != null && d.getSerialNumber().matches(serialnumber)){
                serialstatus=false;
            }
        }
        for(itemgettersetter d : list){
            if(d.getName()!=null&&d.getName().matches(name_text_field.getText())){
                namestatus=false;
            }
        }
        if(!namestatus){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("Name already exists!");
            alert.showAndWait();
        } else if(!serialstatus){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setContentText("serial number already exists!");
            alert.showAndWait();
        } else if (name_text_field.getText().length()>2
                &&name_text_field.getText().length()<256
                &&!name_text_field.getText().isEmpty()
                && !price_text_field.getText().isEmpty()
                &&serialnumber.matches("[A-Z]-[0-9a-z]{3}-[0-9a-z]{3}-[0-9a-z]{3}")
                &&Double.parseDouble(price_text_field.getText())>=0) {
            try {
                list.add(new itemgettersetter(serialnumber, name_text_field.getText(), Double.parseDouble(price_text_field.getText())));
            }catch (NumberFormatException e){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ERROR");
                alert.setContentText("Please input a number!");
                alert.showAndWait();
            }
        } else{
            //error message if input is empty
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input fields empty or invalid");
            alert.setContentText("Please fill all fields");

            alert.showAndWait();
        }

        //clear the textfields
        ser.clear();
        ser1.clear();
        ser2.clear();
        ser3.clear();
        name_text_field.clear();
        price_text_field.clear();


        table_view.setEditable(true);
        table_view.setItems(list);


    }

    @FXML
    public void removefromlist() {
        //user can click on an item an it will remove the item from the list
        delete_item_button.setOnAction(e -> {
            itemgettersetter selectedItem = table_view.getSelectionModel().getSelectedItem();
            table_view.getItems().remove(selectedItem);
            list.remove(selectedItem);
        });
        table_view.refresh();
    }

    @FXML
    public void randomCharacter() {

        //string of all characters
        //added random serial number generator
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String alphanumeric="1234567890abcdefghijklmnopqrstuvwxyz";

        StringBuilder Alpha = new StringBuilder();
        StringBuilder sb = new StringBuilder();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        Random random = new Random();

        // specify length of random string
        int length = 3;
        for(int i = 0; i < 1; i++) {
            int indexA = random.nextInt(alphabet.length());
            char randomCharA = alphabet.charAt(indexA);
            Alpha.append(randomCharA);
        }

        for(int i = 0; i < length; i++) {
            int index = random.nextInt(alphanumeric.length());
            char randomChar = alphanumeric.charAt(index);
            sb.append(randomChar);
        }
        for(int i = 0; i < length; i++) {
            int index1 = random.nextInt(alphanumeric.length());
            char randomChar1 = alphanumeric.charAt(index1);
            sb1.append(randomChar1);
        }
        for(int i = 0; i < length; i++) {
            int index2 = random.nextInt(alphanumeric.length());
            char randomChar2 = alphanumeric.charAt(index2);
            sb2.append(randomChar2);
        }
        String randomStringA = Alpha.toString();
        String randomString = sb.toString();
        String randomString1 = sb1.toString();
        String randomString2 = sb2.toString();
        ser.setText(randomStringA);
        ser1.setText(randomString);
        ser2.setText(randomString1);
        ser3.setText(randomString2);

    }
    @FXML
    public void save_file() throws IOException {
        //based on what file type the user chooses this will write to that file and save on local
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        //Set extension filter
        fileChooser.setInitialDirectory(new File("C://"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"),
                new FileChooser.ExtensionFilter("TSV", "*.txt"),
                new FileChooser.ExtensionFilter("HTML Files", "*.html")
        );

        File savedFile= fileChooser.showSaveDialog(stage);
        if(savedFile.toString().contains(".json")){
            //json format save
            Jsonsave(savedFile);
        }else if(savedFile.toString().contains(".txt")){
            //save to tsv format
            tsvsave(savedFile);
        }
        else if (savedFile != null) {

            try {
                //regular format for now
                savehtml(savedFile);

            }
            catch(IOException e) {

                e.printStackTrace();
            }
        }
    }
    @FXML
    //get the file location from filechooser and then create a new txt file with that new location
    public void savehtml(File fileName) throws IOException {
        try{
            // Create file
            FileWriter fstream = new FileWriter(fileName);
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(new StringBuilder().append("<html>\n").append("<style>\n").append("table, th, td {\n").append("  border:1px solid black;\n").append("}\n").append("</style>\n").append("<body>\n").append("<table style=\"width:100%\">").toString());
            out.write(new StringBuilder().append("<tr>\n").append("    <th>Serial Number</th>\n").append("    <th>Name</th>\n").append("    <th>Value</th>\n").append("  </tr>\n").toString());
            for(int i=0;i<list.size();i++){
                out.write("<tr>\n");
                out.write("<td>"+list.get(i).getSerialNumber()+"</td>\n");
                out.write("<td>"+list.get(i).getName().replaceAll(" ","-")+"</td>\n");
                out.write("<td>"+list.get(i).getValue()+"</td>\n");
                //out.write(list.get(i).getSerialNumber()+","+list.get(i).getName()+","+list.get(i).getValue()+"\n");
                out.write("</tr>\n");
            }
            out.write(new StringBuilder().append("</table>\n").append("\n").append("</body>\n").append("</html>").toString());

            //Close the output stream
            out.close();
        }catch (Exception e){//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void tsvsave(File filesave) throws IOException {
        //tab separated values
        FileWriter fstream = new FileWriter(filesave);
        BufferedWriter out = new BufferedWriter(fstream);
        for(int i=0;i<list.size();i++){
            out.write(list.get(i).getSerialNumber()+"\t"+list.get(i).getName()+"\t"+list.get(i).getValue()+"\n");
        }
        //Close the output stream
        out.close();
    }

    public void Jsonsave(File filesave) throws IOException {
        JsonArray itemObject = new JsonArray();
        JsonObject itemlist = new JsonObject();

        FileWriter fstream = new FileWriter(filesave);
        BufferedWriter out = new BufferedWriter(fstream);
        //creating valid json format
         for(int i=0;i<list.size();i++) {
             JsonObject itemdetails = new JsonObject();
             itemdetails.addProperty("serial_number", list.get(i).getSerialNumber());
             itemdetails.addProperty("Name", list.get(i).getName());
             itemdetails.addProperty("Value", list.get(i).getValue());
             //Put the above JSON Object in another JSON object.
             itemObject.add(itemdetails);
         }
        itemlist.add("items",itemObject);
        out.write(itemlist.toString());
         out.close();
        //Write above object to JSONArray
        System.out.println(itemlist);
    }
    public void jsonload(File fileload){

        //JSON parser object to parse read file
        JsonParser jsonParser = new JsonParser();

        try (FileReader reader = new FileReader(fileload))
        {
            list.removeAll();
            table_view.getItems().clear();
            Object obj = JsonParser.parseReader(new FileReader(fileload));
            JsonObject jsonObject = (JsonObject)obj;
            //Reading products array from  the file
            JsonArray totalproducts = (JsonArray)jsonObject.get("items");

            Iterator<JsonElement> iterator = totalproducts.iterator();
            //Loop through
            while (iterator.hasNext()) {
                JsonObject json = (JsonObject) iterator.next();
                list.add(new itemgettersetter(json.get("serial_number").toString().replaceAll("\"",""),json.get("Name").toString().replaceAll("\"",""),json.get("Value").getAsDouble()));
            }
            table_view.setItems(list);
            table_view.setEditable(true);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void deleteall() {
        table_view.getItems().clear();
        list.removeAll();
    }

    @FXML
    public void loadsaved() throws IOException {
        //will load and update the tableview with the loaded list
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load");
        //Set extension filter
        fileChooser.setInitialDirectory(new File("C://"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"),
                new FileChooser.ExtensionFilter("TSV", "*.txt"),
                new FileChooser.ExtensionFilter("HTML Files", "*.html")
        );
        File loadfile= fileChooser.showOpenDialog(stage);
        if(loadfile.toString().contains(".json")){
            jsonload(loadfile);

        } else if(loadfile.toString().contains(".txt")){
            loadtsv(loadfile);

        } else if (loadfile != null) {
            loadhtml(loadfile);
        }
        }

        @FXML
       public void loadhtml(File loadfile){
        //loading a html file
            try {
                list.removeAll();
                table_view.getItems().clear();
                table_view.refresh();

                ArrayList<String> downServers = new ArrayList<>();
                File input = new File(loadfile.toString());
                Document doc = Jsoup.parse(input, null);
                Elements rows = doc.getElementsByTag("tr");

                for(Element row : rows) {
                    String Test = row.getElementsByTag("td").text();
                    String[] Data=Test.split("\\s+");
                    if(Data.length>1){
                        //some reason the first iteration is length of 1 probably due to it parsing blank data
                        list.add(new itemgettersetter(Data[0],Data[1].replaceAll("-"," "),Double.parseDouble(Data[2])));
                    }
                }
                table_view.setItems(list);
                table_view.setEditable(true);

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        public void loadtsv(File loadfile) throws IOException {
            //loading a tsv file
            BufferedReader br = new BufferedReader(new FileReader(new File(loadfile.toString())));
            String line;
            String[] array;
            list.removeAll();
            table_view.getItems().clear();
            table_view.refresh();

            while ((line = br.readLine()) != null){
            array = line.split("\t");
            list.add(new itemgettersetter((array[0]), (array[1]), Double.parseDouble(array[2])));
             }
            table_view.setItems(list);
            table_view.setEditable(true);
        }

    //search method for filtering items in the array list
    private boolean searchFindsOrder(itemgettersetter order, String searchText){
        return (order.getSerialNumber().toLowerCase().contains(searchText.toLowerCase())) ||
                (order.getName().toLowerCase().contains(searchText.toLowerCase()));
    }
    private ObservableList<itemgettersetter> filterList(List<itemgettersetter> list, String searchText){
        List<itemgettersetter> filteredList = new ArrayList<>();
        for (itemgettersetter order : list){
            if(searchFindsOrder(order, searchText)) filteredList.add(order);
        }
        return FXCollections.observableList(filteredList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        serial_number.setCellValueFactory(new PropertyValueFactory<>("SerialNumber"));
        serial_number.setCellFactory(TextFieldTableCell.forTableColumn());

        name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        name.setCellFactory(TextFieldTableCell.forTableColumn());


        value.setCellValueFactory(new PropertyValueFactory<>("Value"));
        value.setCellFactory(TextFieldTableCell.<itemgettersetter, Double>forTableColumn(new DoubleStringConverter() {
            @Override
            public Double fromString(String value) {
                try {
                    return super.fromString(value);
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("ERROR");
                    alert.setContentText("Please input a number!");
                    alert.showAndWait();
                    return null;
                }
            }
        }));


        serial_number.setOnEditCommit(
                (TableColumn.CellEditEvent<itemgettersetter, String> t) -> {
                    Boolean serialstatus=true;
                    for(itemgettersetter d : list){
                        if(d.getSerialNumber() != null && d.getSerialNumber().matches(t.getNewValue())){
                            serialstatus=false;
                        }
                    }
                    if(!serialstatus){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("ERROR");
                        alert.setContentText("serial number already exists!");
                        alert.showAndWait();
                        t.getOldValue();
                    }
                    if (t.getNewValue().matches("[A-Z]-[0-9a-z]{3}-[0-9a-z]{3}-[0-9a-z]{3}")&&serialstatus) {
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setSerialNumber(t.getNewValue());
                    } else if(serialstatus){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("ERROR");
                        alert.setContentText("Please input a valid serial number!");
                        alert.showAndWait();
                    }
                    table_view.refresh();
                }
        );


        name.setOnEditCommit(
                (TableColumn.CellEditEvent<itemgettersetter, String> t) -> {
                    Boolean status=true;
                    for(itemgettersetter d : list){
                        if(d.getName() != null && d.getName().matches(t.getNewValue())){
                            status=false;
                        }
                    }
                    if(!status){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("ERROR");
                        alert.setContentText("Name already exists!");
                        alert.showAndWait();
                        t.getOldValue();
                        table_view.refresh();
                    }else if (t.getNewValue().length() < 2 || t.getNewValue().length() > 256) {

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("ERROR");
                        alert.setContentText("Please input a valid name!");
                        alert.showAndWait();

                        t.getOldValue();
                        table_view.refresh();
                    } else {
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                    }
                }
        );
        value.setOnEditCommit(
                (TableColumn.CellEditEvent<itemgettersetter, Double> t) -> {
                    if (t.getNewValue() == null||t.getNewValue()<0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("ERROR");
                        alert.setContentText("Invalid Value!");
                        alert.showAndWait();
                        t.getRowValue().setValue(t.getOldValue());
                    } else {
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setValue(t.getNewValue());
                    }
                    table_view.refresh();
                }
        );

        //dynamic search bar for searching the name and serial number
        search_bar_text_field.textProperty().addListener((observable, oldValue, newValue) ->
                table_view.setItems(filterList(list, newValue))
        );

    }
}

