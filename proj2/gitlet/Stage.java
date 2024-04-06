package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Stage implements Serializable {
    private Map<String, String> addition = new HashMap<>();//fileName -> fileHash
    private Set<String> removal = new HashSet<>();//fileName

    public static Stage load() {
        return Utils.readObject(Repository.STAGE_FILE, Stage.class);
    }

    public void save() {
        Utils.writeObject(Repository.STAGE_FILE, this);
    }

    public boolean existFileInAddition(String fileName) {
        return addition.containsKey(fileName);
    }

    public void removeFileInAddition(String fileName) {
        addition.remove(fileName);
    }

    public void addFileInAddition(String fileName, String fileHash){
        addition.put(fileName, fileHash);
    }

    public Map<String, String> getAddition() {
        return addition;
    }

    public String getHash(String fileName) {
        return addition.get(fileName);
    }

    public boolean exitFileInRemoval(String fileName) {
        return removal.contains(fileName);
    }

    public void removeFileInRemoval(String fileName) {
        removal.remove(fileName);
    }

    public void addFileInRemoval(String fileName) {
        removal.add(fileName);
    }

    public Set<String> getRemoval() {
        return removal;
    }

    public boolean isEmpty() {
        return addition.isEmpty() && removal.isEmpty();
    }

    public void clear() {
        addition.clear();
        removal.clear();
    }
}
