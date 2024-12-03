package heyblack.pigeontech;

public class PTEvents {
    @Pigeon(
            displayName = "Example",
            desc = "",
            notifyPlayers = false
    )
    boolean exampleEvent = false;

    @Pigeon(
            displayName = "Nether Invasion",
            desc = "Any nether mob can spawn in overworld portals",
            notifyPlayers = true
    )
    boolean netherInvasion = false;

    @Pigeon(
            displayName = "Mojang Does Not Play Dice",
            desc = "Disables random ticks",
            notifyPlayers = true
    )
    boolean noRandomTicks = false;
}
