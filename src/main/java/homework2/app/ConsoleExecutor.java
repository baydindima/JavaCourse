package homework2.app;

import homework2.command.Command;
import homework2.command.CommandFactory;
import homework2.model.Repository;
import homework2.utils.RepositoryUtils;

import java.util.Arrays;

/**
 * @author Dmitriy Baidin on 9/24/2016.
 */
public class ConsoleExecutor {

    public void run(String[] args) {
        if (args.length == 0) {
            System.out.println("No command specified");
        } else {
            try {
                Repository repository = RepositoryUtils.getRepository();
                Command commandByName = CommandFactory.getCommandByName(args[0]);

                System.out.println(
                        commandByName.execute(repository, Arrays.copyOfRange(args, 1, args.length))
                );

                RepositoryUtils.saveRepository(repository);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

    }

}
