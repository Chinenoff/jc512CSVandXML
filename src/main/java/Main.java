import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Long.parseLong;

public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        //Задание №1 csv-->json
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, "data.json");
        System.out.println(json);

        //Задание №2 xml-->json
        List<Employee> listXml = parseXML("data.xml");
        String jsonXml = listToJson(listXml);
        writeString(jsonXml, "data2.json");
        System.out.println(jsonXml);

        //Задание №3 json-->java
        //String json = readString("new_data.json");
    }

   /* public static String readString(String fName){
        Gson gson = new Gson();
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("data.json"));
            JSONObject jsonObject = (JSONObject) obj;
            System.out.println(jsonObject);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        *//*File jsonFile = new File(fName);
        JSONParser parser = new JSONParser();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(jsonFile));
            // convert the json string back to object
            Employee obj = gson.fromJson(br, Employee.class);
            System.out.println(obj);
        } catch (IOException e)
        {
            e.printStackTrace();
        }*//*

        return strJson;
    }*/

    /*public static List<Employee> jsonToList(String json) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(list, listType);
    }*/


    public static List<Employee> parseXML(String fName) throws IOException, SAXException, ParserConfigurationException {
        List<Employee> listXML = new ArrayList<>();
        //чтение xml файла
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fName));

        //получаем корневой узел node
        Node root = doc.getDocumentElement();

        //получаем список узлов корневого узла NodeList
        NodeList nodeList = root.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            //System.out.println("Teкyщий элeмeнт: " + node_.getNodeName());

            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                Element employee = (Element) node_;
                Employee newEmployee = new Employee();

                //заполняем ID
                String sID = employee.getElementsByTagName("id").item(0).getTextContent();
                try {
                    newEmployee.id = parseLong(sID);
                } catch (NumberFormatException nfe) {
                    System.out.println("NumberFormatException: " + nfe.getMessage());
                }

                //заполняем firstName
                newEmployee.firstName = employee.getElementsByTagName("firstName").item(0).getTextContent();

                //заполняем lastName
                newEmployee.lastName = employee.getElementsByTagName("lastName").item(0).getTextContent();

                //заполняем country
                newEmployee.country = employee.getElementsByTagName("country").item(0).getTextContent();

                //заполняем age
                String sAGE = employee.getElementsByTagName("age").item(0).getTextContent();
                newEmployee.age = Integer.parseInt(sAGE);

                //добавляем сотрудника в список
                listXML.add(newEmployee);
            }
        }
        return listXML;
    }

    public static List<Employee> parseCSV(String[] colMapping, String fName) {
        List<Employee> list = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(fName))) {
            ColumnPositionMappingStrategy<Employee> strategy =
                    new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(colMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();

            list = csv.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String listToJson(List list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(list, listType);
    }

    public static void writeString(String stringJson, String fName) {
        try (FileWriter file = new
                FileWriter(fName)) {
            file.write(stringJson);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
