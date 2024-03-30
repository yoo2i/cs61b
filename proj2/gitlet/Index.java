package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Index implements Serializable {
    private Map<String, String> blobs = new HashMap<>();//fileName -> fileHash

    public boolean existTheFile(String fileName) {
        return blobs.containsKey(fileName);
    }

    public void removeTheFile(String fileName) {
        blobs.remove(fileName);
    }

    public void addFile(String fileName, String fileHash){
        blobs.put(fileName, fileHash);
    }

    public String getHash(String fileName) {
        return blobs.get(fileName);
    }
}
