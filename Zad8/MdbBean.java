import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.InitialContext;
import pl.jrj.mdb.IMdbManager;

/**
 *
 * @author Maciek Niechaj
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/MyQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class MdbBean implements MessageListener { 
    
    private IMdbManager mdbManager;
    private TopicConnectionFactory connectionFactory;
    private Topic myTopic;
    
    private final String ALBUM_NR = "124634";
    private State currentState = State.STOPPED;
    private int counter = 0;
    private int errorCounter = 0;
    private String sessionId = null;
    // auxiliary placeholder for incn and decn message
    private int n = 0;
    
    public MdbBean() {
        
    }
    
    private enum State {
        STOPPED, COUNTING
    }
    
    private enum Action {
        START, STOP, VAL, ERR, INC, INCN, DEC, DECN, UDFN
    }
    
    @Override
    public void onMessage(Message message) {
        
        if (message instanceof TextMessage) {
            try {
                InitialContext context = new InitialContext();
                connectionFactory = (TopicConnectionFactory) context.lookup("jms/ConnectionFactory");
                myTopic = (Topic) context.lookup("jms/MyTopic");
                mdbManager = (IMdbManager) context.lookup("java:global/mdb-project/MdbManager!pl.jrj.mdb.IMdbManager");
                registerBean();
                TextMessage textMessage = (TextMessage) message;
                Action action = getAction(textMessage.getText());
                if (isActionPossible(action)) {
                    executeAction(action);
                } else {
                    errorCounter++;
                }
            } catch (JMSException e) {
                System.out.println(e.getMessage());
            } catch (IllegalStateException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    private void registerBean() {
        if (sessionId == null) {
            sessionId = mdbManager.sessionId(ALBUM_NR);
            if (sessionId == null) {
                throw new IllegalStateException("Something went wrong "
                        + "during bean registration");
            }
        }
    }
    
    private Action getAction(String message) {
        Action action;
        switch (message) {
            case "start": action = Action.START;
                break;
            case "stop": action = Action.STOP;
                break;
            case "val": action = Action.VAL;
                break;
            case "err": action = Action.ERR;
                break;
            case "inc": action = Action.INC;
                break;
            case "dec": action = Action.DEC;
                break;
            default: action = checkDefaultAction(message);
        }
        
        return action;
    }
    
    private Action checkDefaultAction(String message) {
        Action action;
        try {
            if (message.startsWith("inc/")) {
                n = Integer.parseInt(message.substring(4));
                action = Action.INCN;
            } else if (message.startsWith("dec/")) {
                n = Integer.parseInt(message.substring(4));
                action = Action.DECN;
            } else {
                action = Action.UDFN;
            }
        } catch (NumberFormatException e) {
            action = Action.UDFN;
        }   
        
        return action;
    }
    
    private boolean isActionPossible(Action action) {
        boolean isPossible = false;
        if (!action.equals(Action.UDFN)) {
            if (currentState.equals(State.STOPPED)) {
                if (action.equals(Action.START) || action.equals(Action.ERR)
                        || action.equals(Action.VAL)) {
                    isPossible = true;
                }
            } else {
                if (!action.equals(Action.START)) {
                    isPossible = true;
                }
            }
        }
        
        return isPossible;
    }
    
    private void executeAction(Action action) {
        switch (action) {
            case START: currentState = State.COUNTING;
                break;
            case STOP: currentState = State.STOPPED;
                break;
            case VAL: returnVal();
                break;
            case ERR: returnErr();
                break;
            case INC: counter++;
                break;
            case INCN: counter += n;
                break;
            case DEC: counter--;
                break;
            case DECN: counter -= n;
                break;
            case UDFN: errorCounter++;
                break;
        }
    }
    
    private void sendMessageToTopic(int value) {
        try {
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession();
            MessageProducer messageProducer = session.createProducer(myTopic);
            TextMessage textMessage = session.createTextMessage(sessionId + "/" + value);
            messageProducer.send(textMessage);
        } catch (JMSException e) {
            System.out.println(e.getMessage());
        }
    }
    
    private void returnVal() {
        sendMessageToTopic(counter);
    }
    
    private void returnErr() {
        sendMessageToTopic(errorCounter);
    }
    
}

