package heyblack.pigeontech;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.SERVER)
public class SaveManager {
    private static final Path PATH = ((MinecraftServer) FabricLoader.getInstance().getGameInstance()).getSavePath(WorldSavePath.ROOT).resolve("PigeonTech.json");

    private static final int MAX_RETRY = 10;

    public static void savePTE(List<PTEGroup> list) {
        Gson gson = new Gson();
        int attempts = 0;

        while (true) {
            try (FileWriter writer = new FileWriter(PATH.toFile())) {
                gson.toJson(list, writer);

                break;
            } catch (IOException e) {
                attempts++;
                PigeonTech.LOGGER.error("Failed to save event groups! (attempt " + attempts + ")");
                e.printStackTrace();

                if (!(attempts < MAX_RETRY)) {
                    PigeonTech.LOGGER.error("Failed to save config after 10 attempts, closing without saving event groups!");

                    return;
                }
            }
        }

    }

    public static List<PTEGroup> loadPTE() {
        Gson gson = new Gson();
        int attempts = 0;

        List<PTEGroup> list = new ArrayList<>();

        if (!PATH.toFile().exists()) {
            return list;
        }

        while (true) {
            try (FileReader reader = new FileReader(PATH.toFile())) {
                list = gson.fromJson(reader, new TypeToken<List<PTEGroup>>() {}.getType());

                return list;
            } catch (IOException e) {
                attempts++;
                PigeonTech.LOGGER.error("Failed to load event groups! (attempt " + attempts + ")");
                e.printStackTrace();

                if (!(attempts < MAX_RETRY)) {
                    throw new RuntimeException();
                }
            }
        }
    }

    public static boolean saveExists() {
        return PATH.toFile().exists();
    }
}
