package homework2.storage;

import homework2.model.Repository;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * @author Baidin Dima
 */
public class RepositoryWriter {

    public void write(Repository repository, OutputStream outputStream) {
        try (ObjectOutputStream out = new ObjectOutputStream(outputStream)) {
            out.writeObject(repository);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
