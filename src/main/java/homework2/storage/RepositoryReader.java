package homework2.storage;

import homework2.model.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * Read repository info from input stream
 */
public class RepositoryReader {

    /**
     * Read repository info from input stream
     */
    public Repository read(InputStream inputStream) {
        try (ObjectInputStream in = new ObjectInputStream(inputStream)) {
            return (Repository) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
