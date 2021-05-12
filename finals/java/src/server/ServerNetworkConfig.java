public class ServerNetworkConfig {
    String rootpoaName;
    String rootNamingContext;
    String objectReferenceName;

    public ServerNetworkConfig(String rootNamingContext, String objectReferenceName, String rootpoaName) {
        this.rootpoaName = rootpoaName;
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

    public String getRootpoaName() {
        return rootpoaName;
    }

    public void setRootpoaName(String rootpoaName) {
        this.rootpoaName = rootpoaName;
    }
}
