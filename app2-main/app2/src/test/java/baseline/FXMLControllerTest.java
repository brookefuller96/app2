/*
 *  UCF COP3330 Fall 2021 Application Assignment 2 Solution
 *  Copyright 2021 Benjamin Shin
 */
package baseline;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Iterator;
import java.util.Random;

class FXMLControllerTest {

    @Test
    void addtolist() {
        ObservableList<itemgettersetter>testlist= FXCollections.observableArrayList();
        testlist.add(new itemgettersetter("A-abc-123-a1b","thing",500));
        Assertions.assertEquals("A-abc-123-a1b",testlist.get(0).getSerialNumber());
        Assertions.assertEquals("thing",testlist.get(0).getName());
        Assertions.assertEquals(500.0,testlist.get(0).getValue());
    }

    @Test
    void randomCharacter() {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String alphanumeric="123456789abcdefghijklmnopqrstuvwxyz";

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
        String serialnumber = randomStringA + "-" + randomString + "-" + randomString1 + "-" + randomString2;
        Boolean actual=serialnumber.matches("[A-Z]-[0-9a-z]{3}-[0-9a-z]{3}-[0-9a-z]{3}");
        Assertions.assertEquals(true,actual);
    }

    @Test
    void jsonload() {
        JsonParser jsonParser = new JsonParser();
        ObservableList<itemgettersetter>testlist1= FXCollections.observableArrayList();

        try (FileReader reader = new FileReader("data/jsontest.json"))
        {
            Object obj = JsonParser.parseReader(new FileReader("data/jsontest.json"));
            JsonObject jsonObject = (JsonObject)obj;
            //Reading products array from  the file
            JsonArray totalproducts = (JsonArray)jsonObject.get("items");

            Iterator<JsonElement> iterator = totalproducts.iterator();
            //Loop through
            while (iterator.hasNext()) {
                JsonObject json = (JsonObject) iterator.next();
                testlist1.add(new itemgettersetter(json.get("serial_number").toString().replaceAll("\"",""),json.get("Name").toString().replaceAll("\"",""),json.get("Value").getAsDouble()));
            }
            Assertions.assertEquals("M-bht-xzy-era",testlist1.get(0).getSerialNumber());
            Assertions.assertEquals("testitem",testlist1.get(0).getName());
            Assertions.assertEquals(500.0,testlist1.get(0).getValue());



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void loadhtml() {
        try {
            ObservableList<itemgettersetter>testlist2= FXCollections.observableArrayList();
            File input = new File("data/htmltest.html");
            Document doc = Jsoup.parse(input, null);
            Elements rows = doc.getElementsByTag("tr");

            for(Element row : rows) {
                String Test = row.getElementsByTag("td").text();
                String[] Data=Test.split("\\s+");
                if(Data.length>1){
                    //some reason the first iteration is length of 1 probably due to it parsing blank data
                    testlist2.add(new itemgettersetter(Data[0],Data[1],Double.parseDouble(Data[2])));
                }
            }
            Assertions.assertEquals("M-bht-xzy-era",testlist2.get(0).getSerialNumber());
            Assertions.assertEquals("testitem",testlist2.get(0).getName());
            Assertions.assertEquals(500.0,testlist2.get(0).getValue());

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Test
    void loadtsv() throws IOException {
        ObservableList<itemgettersetter>testlist3= FXCollections.observableArrayList();
        BufferedReader br = new BufferedReader(new FileReader(new File("data/tsv test.tsv")));
        String line;
        String[] array;

        while ((line = br.readLine()) != null){
            array = line.split("\t");
            testlist3.add(new itemgettersetter((array[0]), (array[1]), Double.parseDouble(array[2])));
        }
        Assertions.assertEquals("M-bht-xzy-era",testlist3.get(0).getSerialNumber());
        Assertions.assertEquals("testitem",testlist3.get(0).getName());
        Assertions.assertEquals(500.0,testlist3.get(0).getValue());

    }
}