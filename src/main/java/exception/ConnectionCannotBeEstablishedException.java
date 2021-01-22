package exception;

public class ConnectionCannotBeEstablishedException extends Exception{

    public ConnectionCannotBeEstablishedException(){
        super();
    }

    public ConnectionCannotBeEstablishedException(String message){
        super(message);
    }

    public ConnectionCannotBeEstablishedException(Throwable throwable){
        super(throwable);
    }
}
