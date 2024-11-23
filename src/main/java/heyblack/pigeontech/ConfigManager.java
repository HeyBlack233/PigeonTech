package heyblack.pigeontech;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Path;

@Environment(EnvType.SERVER)
public class ConfigManager {
    private static final Path PATH = ((MinecraftServer) FabricLoader.getInstance().getGameInstance()).getSavePath(WorldSavePath.ROOT).resolve("PigeonTech.txt");

    private static final int MAX_RETRY = 10;

    public static PTEvents initializePTE() {
        PTEvents pte = new PTEvents();
        int attempts = 0;

        while (attempts < MAX_RETRY) {
            if (PATH.toFile().exists()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(PATH.toFile()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] str = line.split(":");
                        if (str.length == 2) {
                            Field field = EventManager.getEventWithId(str[0]);
                            if (field != null)
                                field.setBoolean(pte, Boolean.parseBoolean(str[1]));
                            else PigeonTech.LOGGER.warn("Found invalid pigeon in config: " + str[0]);
                        } else {
                            PigeonTech.LOGGER.warn("Found invalid config line: " + line);
                        }
                    }

                    break;
                } catch (IOException e) {
                    attempts++;
                    PigeonTech.LOGGER.error("Failed to load config! (attempt " + attempts + ")");
                    e.printStackTrace();

                    if (!(attempts < MAX_RETRY)) {
                        throw new RuntimeException();
                    }
                } catch (IllegalAccessException e) {

                }
            } else {
                saveConfig(pte);
            }
        }

        return pte;
    }

    public static void saveConfig(PTEvents pte) {
        int attempts = 0;

        while (attempts < MAX_RETRY) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH.toFile()))) {
                for (Field field : pte.getClass().getDeclaredFields()) {
                    String value = Boolean.toString(field.getBoolean(pte));

                    writer.write(field.getName() + ":" + value);
                    writer.newLine();
                }

                break;
            } catch (IOException e) {
                attempts++;
                PigeonTech.LOGGER.error("Failed to save config! (attempt " + attempts + ")");
                e.printStackTrace();

                if (!(attempts < MAX_RETRY)) {
                    PigeonTech.LOGGER.error("Failed to save config after 10 attempts, closing without saving config!");

                    return;
                }
            } catch (IllegalAccessException e) {

            }
        }
    }
}
