package homework2.storage;

import homework2.model.InMemoryRepository;
import homework2.model.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * @author Dmitriy Baidin on 9/21/2016.
 */
public class RepositoryReader {

    public Repository read(InputStream inputStream) {
        try (ObjectInputStream in = new ObjectInputStream(inputStream)) {
            return (Repository) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
