package server_cli;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import net.zephyrizing.http_server.HttpServer;
import net.zephyrizing.http_server.handlers.CobSpecHandler;
import net.zephyrizing.http_server.handlers.Handler;

import static java.util.Arrays.asList;

public class StartServer {

    private static int DEFAULT_THREADS = 24;

    public static void main(String[] args) throws InterruptedException {
        String defaultPublicRoot = String.format("%s/src/cob_spec/public/",
                                                 System.getenv("HOME"));

        OptionParser parser = new OptionParser();
        OptionSpec<Integer> portOpt = parser.acceptsAll(asList("p", "port"))
            .withRequiredArg().ofType(Integer.class).defaultsTo(5000);
        OptionSpec<String>  rootOpt = parser.acceptsAll(asList("d", "directory"))
            .withRequiredArg().ofType(String.class).defaultsTo(defaultPublicRoot);
        OptionSpec<Integer> threadsOpt = parser.acceptsAll(asList("t", "threads"))
            .withRequiredArg().ofType(Integer.class).defaultsTo(DEFAULT_THREADS);

        OptionSet options = parser.parse(args);

        int portNumber = options.valueOf(portOpt);
        String public_root_path = options.valueOf(rootOpt);
        int threadPoolSize = options.valueOf(threadsOpt);

        Path public_root = Paths.get(public_root_path);

        if (!Files.isDirectory(public_root)) {
            System.err.format("The directory '%s' must exist!\n", public_root_path);
            return;
        }

        System.err.format("Starting server on port %d...", portNumber);

        Handler handler = new CobSpecHandler(public_root);
        try (HttpServer server = HttpServer.createServer(portNumber, handler)) {

            server.listen();
            server.serve();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
