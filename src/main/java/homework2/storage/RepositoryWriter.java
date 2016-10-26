package homework2.storage;

import homework2.model.Repository;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Write repository to output stream
 */
public class RepositoryWriter {

    /**
     * Write repository to output stream
     */
    public void write(Repository repository, OutputStream outputStream) {
        try (ObjectOutputStream out = new ObjectOutputStream(outputStream)) {
            out.writeObject(repository);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
