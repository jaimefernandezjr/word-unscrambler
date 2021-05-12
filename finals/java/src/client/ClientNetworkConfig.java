public class ClientNetworkConfig {
    String rootNamingContext;
    String objectReferenceName;

    public ClientNetworkConfig(String rootNamingContext, String objectReferenceName) {
        this.rootNamingContext = rootNamingContext;
        this.objectReferenceName = objectReferenceName;
    }

    public String getRootNamingContext() {
        return rootNamingContext;
    }

    public void setRootNamingContext(String rootNamingContext) {
        this.rootNamingContext = rootNamingContext;
    }

    public String getObjectReferenceName() {
        return objectReferenceName;
    }

    public void setObjectReferenceName(String objectReferenceName) {
        this.objectReferenceName = objectReferenceName;
    }
}
