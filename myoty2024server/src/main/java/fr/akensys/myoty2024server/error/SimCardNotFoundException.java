package fr.akensys.myoty2024server.error;

public class SimCardNotFoundException extends RuntimeException {

    public SimCardNotFoundException(String message) {
        super(message);
    }
}
