import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        String s = "<dwml version=\"1.0\" xsi:noNamespaceSchemaLocation=\"https://graphical.weather.gov/xml/DWMLgen/schema/DWML.xsd\">\n" +
                "<head>\n" +
                "<product srsName=\"WGS 1984\" concise-name=\"time-series\" operational-mode=\"official\">\n" +
                "<title>NOAA's National Weather Service Forecast Data</title>\n" +
                "<field>meteorological</field>\n" +
                "<category>forecast</category>\n" +
                "<creation-date refresh-frequency=\"PT1H\">2019-11-17T15:07:07Z</creation-date>\n" +
                "</product>\n" +
                "<source>\n" +
                "<more-information>https://graphical.weather.gov/xml/</more-information>\n" +
                "<production-center>\n" +
                "Meteorological Development Laboratory\n" +
                "<sub-center>Product Generation Branch</sub-center>\n" +
                "</production-center>\n" +
                "<disclaimer>http://www.nws.noaa.gov/disclaimer.html</disclaimer>\n" +
                "<credit>https://www.weather.gov/</credit>\n" +
                "<credit-logo>https://www.weather.gov/logorequest</credit-logo>\n" +
                "<feedback>https://www.weather.gov/contact</feedback>\n" +
                "</source>\n" +
                "</head>\n" +
                "<data>\n" +
                "<location>\n" +
                "<location-key>point1</location-key>\n" +
                "<point latitude=\"38.99\" longitude=\"-77.01\"/>\n" +
                "</location>\n" +
                "<moreWeatherInformation applicable-location=\"point1\">https://forecast-v3.weather.gov/point/38.99,-77.01</moreWeatherInformation>\n" +
                "<time-layout time-coordinate=\"local\" summarization=\"none\">\n" +
                "<layout-key>k-p24h-n7-1</layout-key>\n" +
                "<start-valid-time>2019-11-17T07:00:00-05:00</start-valid-time>\n" +
                "<end-valid-time>2019-11-17T19:00:00-05:00</end-valid-time>\n" +
                "<start-valid-time>2019-11-18T07:00:00-05:00</start-valid-time>\n" +
                "<end-valid-time>2019-11-18T19:00:00-05:00</end-valid-time>\n" +
                "<start-valid-time>2019-11-19T07:00:00-05:00</start-valid-time>\n" +
                "<end-valid-time>2019-11-19T19:00:00-05:00</end-valid-time>\n" +
                "<start-valid-time>2019-11-20T07:00:00-05:00</start-valid-time>\n" +
                "<end-valid-time>2019-11-20T19:00:00-05:00</end-valid-time>\n" +
                "<start-valid-time>2019-11-21T07:00:00-05:00</start-valid-time>\n" +
                "<end-valid-time>2019-11-21T19:00:00-05:00</end-valid-time>\n" +
                "<start-valid-time>2019-11-22T07:00:00-05:00</start-valid-time>\n" +
                "<end-valid-time>2019-11-22T19:00:00-05:00</end-valid-time>\n" +
                "<start-valid-time>2019-11-23T07:00:00-05:00</start-valid-time>\n" +
                "<end-valid-time>2019-11-23T19:00:00-05:00</end-valid-time>\n" +
                "</time-layout>\n" +
                "<time-layout time-coordinate=\"local\" summarization=\"none\">\n" +
                "<layout-key>k-p24h-n6-2</layout-key>\n" +
                "<start-valid-time>2019-11-17T19:00:00-05:00</start-valid-time>\n" +
                "<end-valid-time>2019-11-18T08:00:00-05:00</end-valid-time>\n" +
                "<start-valid-time>2019-11-18T19:00:00-05:00</start-valid-time>\n" +
                "<end-valid-time>2019-11-19T08:00:00-05:00</end-valid-time>\n" +
                "<start-valid-time>2019-11-19T19:00:00-05:00</start-valid-time>\n" +
                "<end-valid-time>2019-11-20T08:00:00-05:00</end-valid-time>\n" +
                "<start-valid-time>2019-11-20T19:00:00-05:00</start-valid-time>\n" +
                "<end-valid-time>2019-11-21T08:00:00-05:00</end-valid-time>\n" +
                "<start-valid-time>2019-11-21T19:00:00-05:00</start-valid-time>\n" +
                "<end-valid-time>2019-11-22T08:00:00-05:00</end-valid-time>\n" +
                "<start-valid-time>2019-11-22T19:00:00-05:00</start-valid-time>\n" +
                "<end-valid-time>2019-11-23T08:00:00-05:00</end-valid-time>\n" +
                "</time-layout>\n" +
                "<time-layout time-coordinate=\"local\" summarization=\"none\">\n" +
                "<layout-key>k-p3h-n36-3</layout-key>\n" +
                "<start-valid-time>2019-11-17T10:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-17T13:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-17T16:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-17T19:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-17T22:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-18T01:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-18T04:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-18T07:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-18T10:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-18T13:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-18T16:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-18T19:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-18T22:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-19T01:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-19T04:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-19T07:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-19T10:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-19T13:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-19T16:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-19T19:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-20T01:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-20T07:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-20T13:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-20T19:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-21T01:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-21T07:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-21T13:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-21T19:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-22T01:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-22T07:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-22T13:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-22T19:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-23T01:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-23T07:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-23T13:00:00-05:00</start-valid-time>\n" +
                "<start-valid-time>2019-11-23T19:00:00-05:00</start-valid-time>\n" +
                "</time-layout>\n" +
                "<parameters applicable-location=\"point1\">\n" +
                "<temperature type=\"maximum\" units=\"Fahrenheit\" time-layout=\"k-p24h-n7-1\">\n" +
                "<name>Daily Maximum Temperature</name>\n" +
                "<value>45</value>\n" +
                "<value>48</value>\n" +
                "<value>52</value>\n" +
                "<value>54</value>\n" +
                "<value>54</value>\n" +
                "<value>60</value>\n" +
                "<value>52</value>\n" +
                "</temperature>\n" +
                "<temperature type=\"minimum\" units=\"Fahrenheit\" time-layout=\"k-p24h-n6-2\">\n" +
                "<name>Daily Minimum Temperature</name>\n" +
                "<value>36</value>\n" +
                "<value>36</value>\n" +
                "<value>36</value>\n" +
                "<value>36</value>\n" +
                "<value>45</value>\n" +
                "<value>38</value>\n" +
                "</temperature>\n" +
                "<temperature type=\"dew point\" units=\"Fahrenheit\" time-layout=\"k-p3h-n36-3\">\n" +
                "<name>Dew Point Temperature</name>\n" +
                "<value>24</value>\n" +
                "<value>30</value>\n" +
                "<value>32</value>\n" +
                "<value>32</value>\n" +
                "<value>33</value>\n" +
                "<value>33</value>\n" +
                "<value>34</value>\n" +
                "<value>34</value>\n" +
                "<value>35</value>\n" +
                "<value>37</value>\n" +
                "<value>38</value>\n" +
                "<value>37</value>\n" +
                "<value>37</value>\n" +
                "<value>37</value>\n" +
                "<value>35</value>\n" +
                "<value>35</value>\n" +
                "<value>35</value>\n" +
                "<value>35</value>\n" +
                "<value>35</value>\n" +
                "<value>36</value>\n" +
                "<value>34</value>\n" +
                "<value>34</value>\n" +
                "<value>35</value>\n" +
                "<value>35</value>\n" +
                "<value>34</value>\n" +
                "<value>34</value>\n" +
                "<value>37</value>\n" +
                "<value>41</value>\n" +
                "<value>44</value>\n" +
                "<value>46</value>\n" +
                "<value>40</value>\n" +
                "<value>36</value>\n" +
                "<value>32</value>\n" +
                "<value>32</value>\n" +
                "<value>30</value>\n" +
                "<value>33</value>\n" +
                "</temperature>\n" +
                "<wind-speed type=\"sustained\" units=\"knots\" time-layout=\"k-p3h-n36-3\">\n" +
                "<name>Wind Speed</name>\n" +
                "<value>9</value>\n" +
                "<value>9</value>\n" +
                "<value>9</value>\n" +
                "<value>9</value>\n" +
                "<value>8</value>\n" +
                "<value>6</value>\n" +
                "<value>7</value>\n" +
                "<value>6</value>\n" +
                "<value>6</value>\n" +
                "<value>5</value>\n" +
                "<value>5</value>\n" +
                "<value>3</value>\n" +
                "<value>4</value>\n" +
                "<value>2</value>\n" +
                "<value>5</value>\n" +
                "<value>3</value>\n" +
                "<value>5</value>\n" +
                "<value>4</value>\n" +
                "<value>5</value>\n" +
                "<value>3</value>\n" +
                "<value>4</value>\n" +
                "<value>4</value>\n" +
                "<value>9</value>\n" +
                "<value>5</value>\n" +
                "<value>3</value>\n" +
                "<value>2</value>\n" +
                "<value>5</value>\n" +
                "<value>6</value>\n" +
                "<value>7</value>\n" +
                "<value>6</value>\n" +
                "<value>10</value>\n" +
                "<value>8</value>\n" +
                "<value>7</value>\n" +
                "<value>5</value>\n" +
                "<value>5</value>\n" +
                "<value>3</value>\n" +
                "</wind-speed>\n" +
                "</parameters>\n" +
                "</data>\n" +
                "</dwml>";


        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource src = new InputSource();
        src.setCharacterStream(new StringReader(s));

        Document doc = builder.parse(src);
        String tMax = doc.getElementsByTagName("temperature").item(0).getTextContent().split(System.lineSeparator())[2];
        String tMin = doc.getElementsByTagName("temperature").item(1).getTextContent().split(System.lineSeparator())[2];
        String tDew = doc.getElementsByTagName("temperature").item(2).getTextContent().split(System.lineSeparator())[2];
        String wind = doc.getElementsByTagName("wind-speed").item(0).getTextContent().split(System.lineSeparator())[2];

        System.out.println(tMax);
        System.out.println(tMin);
        System.out.println(tDew);
        System.out.println(wind);

    }
}
