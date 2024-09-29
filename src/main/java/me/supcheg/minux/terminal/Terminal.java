package me.supcheg.minux.terminal;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public final class Terminal implements Closeable {
    private final Process process;
    private final BufferedWriter writer;
    private final BufferedReader reader;
    private final List<CompletableFuture<?>> messageSubscriptions;

    public Terminal(@NotNull String shellCommand) throws IOException {
        this.process = new ProcessBuilder(shellCommand)
                .redirectErrorStream(true)
                .start();

        this.writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        this.reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        this.messageSubscriptions = new LinkedList<>();
    }

    public void sendCommand(@NotNull String command) throws IOException {
        writer.write(command);
        writer.write("\n");
        writer.flush();
    }

    public void addOutputSubscription(@NotNull Consumer<String> consumer) {
        CompletableFuture<Void> subscription = CompletableFuture.runAsync(() -> {
            try {
                String line;
                while (process.isAlive() && (line = reader.readLine()) != null) {
                    consumer.accept(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        messageSubscriptions.add(subscription);
    }

    @Override
    public void close() {
        process.destroy();
        messageSubscriptions.forEach(CompletableFuture::join);
    }
}
