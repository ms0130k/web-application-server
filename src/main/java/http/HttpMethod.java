package http;

public enum HttpMethod {
    GET, POST;
    
    public boolean isGet() {
        return this == GET;
    }
    
    public boolean isNotGet() {
        return this != GET;
    }
}
