import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MqManager {
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
//    private static String url = "tcp://localhost:8161?wireFormat.maxInactivityDuration=0";
    private static Connection connection = null;
    private static Session session = null;
    private static Destination quIn = null;
    private static Destination quOut = null;
    private static MessageProducer messageProducer = null;
    private static MessageConsumer messageConsumer = null;

    public MqManager() {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        try {
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void createQuIn(String name) {
        try {
            quIn = session.createQueue(name);
            messageConsumer = session.createConsumer(quIn);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void createQuOut(String name) {
        try {
            quOut = session.createQueue(name);
            messageProducer = session.createProducer(quOut);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public String consumer() {
        try {
            Message message = messageConsumer.receive();
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                return textMessage.getText();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void producer(String message) {
        try {
            TextMessage textMessage = session.createTextMessage(message);
            messageProducer.send(textMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
