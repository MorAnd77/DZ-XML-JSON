import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Employee> list = parseXML("data.xml");
        String json = listToJson(list);
        writeString(json);
    }

    private static List<Employee> parseXML(String s) {
        List<Employee> list = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(s));
            Node root = document.getDocumentElement();
            NodeList nodeList = root.getChildNodes();
            long id = 0l;
            String firstName = null;
            String lastName = null;
            String country = null;
            int age = 0;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node_ = nodeList.item(i);
                if (Node.ELEMENT_NODE == node_.getNodeType()) {
                    Element element = (Element) node_;
                    id = Integer.valueOf(element.getElementsByTagName("id").item(0).getTextContent());
                    firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                    lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                    country = element.getElementsByTagName("country").item(0).getTextContent();
                    age = Integer.valueOf(element.getElementsByTagName("age").item(0).getTextContent());
                }
            }
            Employee employee = new Employee(id, firstName, lastName, country, age);
            list.add(employee);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return list;
    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);
        return json;
    }

    private static void writeString(String json) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try (FileWriter file = new FileWriter("data.json")) {
            file.write(gson.toJson(json));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
