package homework2.app;

import homework2.command.Command;
import homework2.command.CommandFactory;

import java.util.Arrays;

/**
 * @author Dmitriy Baidin on 9/24/2016.
 */
public class ConsoleExecutor {

    public void run(String[] args, Backend backend) {
        if (args.length == 0) {
            System.out.println("No command specified");
        } else {
            try {
                Command commandByName = CommandFactory.getCommandByName(args[0]);
                String result = commandByName.execute(backend, Arrays.copyOfRange(args, 1, args.length));

                System.out.println(result);

                backend.getRepositoryUtils().saveRepository(backend.getRepository());
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }

    }

}
