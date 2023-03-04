package fr.izy;

import fr.izy.database.dao.DataDAO;
import fr.izy.database.Database;
import fr.izy.database.dao.OpusDAO;
import fr.izy.database.dao.PlatformDAO;
import fr.izy.database.dao.StatsDAO;
import fr.izy.moon.ModernWarfare;
import io.github.izycorp.moonapi.components.*;
import io.github.izycorp.moonapi.query.RequestManager;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class Main {

    // Database properties
    private DataDAO dataDAO;
    private StatsDAO statsDAO;

    // MoonAPI properties

    private final RequestManager requestManager;

    private Platform targetedPlatform;
    private Opus targetedOpus;

    // Other properties
    /**
     * This value define from which page we start fetching data.
     */
    private int fetchedPagesAmount;
    /**
     * This value define how many thread are used by the program to send requests to distant server.
     */
    private int maxThreadAtRuntime = 2000;

    public static void main(String[] args) throws Exception {
        new Main();
    }

    public Main() throws Exception {
        boolean isOkay = false;

        System.out.println("[!] Connecting to database...");
        initializeDatabase();
        System.out.println("[!] Database initialized");

        requestManager = new RequestManager();
        System.out.println("[!] Starting...");
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("[?] Enter :\n| '1' = BO3 \n| '2' = INFINITE_WARFARE \n| '3' = WWII \n| '4' = 'BO4' \n| '5' = 'MW2019' \n| '6' = ColdWar \n| '7' = Vanguard \n| '8' = MW2");
            while(!isOkay) {
                int opus = scanner.nextInt();
                switch (opus) {
                    case 1:
                        targetedOpus = Opus.BO3;
                        isOkay = true;
                        break;
                    case 2:
                        targetedOpus = Opus.INFINITE_WARFARE;
                        isOkay = true;
                        break;
                    case 3:
                        targetedOpus = Opus.WWII;
                        isOkay = true;
                        break;
                    case 4:
                        targetedOpus = Opus.BO4;
                        isOkay = true;
                        break;
                    case 5:
                        targetedOpus = Opus.MW2019;
                        isOkay = true;
                        break;
                    case 6:
                        targetedOpus = Opus.COLD_WAR;
                        isOkay = true;
                        break;
                    case 7:
                        targetedOpus = Opus.VANGUARD;
                        isOkay = true;
                        break;
                    default:
                        System.out.println("[!] Invalid opus. please enter a valid one.");
                        break;
                }
            }

            isOkay = false;

            System.out.println("[?] Enter :\n| '1' = XBOX \n| '2' = PSN \n| '3' = BATTLE_NET \n| '4' = STEAM");
            System.out.println("/!\\ WARNING Compatible platforms with" + targetedOpus.name() + " : " + Arrays.toString(targetedOpus.getCompatiblePlatforms()));
            while (!isOkay) {
                int input = scanner.nextInt();
                switch (input) {
                    case 1:
                        targetedPlatform = Platform.XBOX;
                        break;
                    case 2:
                        targetedPlatform = Platform.PLAYSTATION;
                        break;
                    case 3:
                        targetedPlatform = Platform.BATTLE_NET;
                        break;
                    case 4:
                        targetedPlatform = Platform.STEAM;
                        break;
                    default:
                        System.out.println("[!] Invalid platform for this opus, please enter a valid one.");
                        break;
                }
                if(Arrays.asList(targetedOpus.getCompatiblePlatforms()).contains(targetedPlatform)) {
                    fetchedPagesAmount = dataDAO.getLatestPage(input) == 0 ? 1 : dataDAO.getLatestPage(input);
                    isOkay = true;
                } else System.out.println("[!] Invalid platform for this opus, please enter a valid one.");
            }

            System.out.println("[?] What are max thread amount (default 2000)");
            maxThreadAtRuntime = scanner.nextInt();
        } catch (Exception e) {
            System.out.println("[!] Error - While reading...");
            System.exit(1);
        }

        System.out.println("[?] From which page do you want to start fetching ? (default = " + fetchedPagesAmount + " -> this value has been calculated)");
        Scanner scanner = new Scanner(System.in);

        int input = scanner.nextInt();
        if(input <= 0) fetchedPagesAmount = 1;
        else fetchedPagesAmount = input;

        System.out.println("[!] Loading " + targetedPlatform.name() + " data...");

        otherGun(targetedPlatform);
    }

    private void initializeDatabase() {
        try {
            // We init our database connection
            Database database = new Database("localhost", 5432, "moon", "user", "password");
            System.out.println("[DATABASE] Successfully connected to database.");

            // Table: Opus
            OpusDAO opusDAO = new OpusDAO(database.getConnection());
            opusDAO.createTableOpus();
            System.out.println("[DATABASE] Successfully created table Opus.");

            // Table: Platform
            PlatformDAO platformDAO = new PlatformDAO(database.getConnection());
            platformDAO.createTablePlatform();
            System.out.println("[DATABASE] Successfully created table Platform.");

            // Table: Data
            dataDAO = new DataDAO(database.getConnection());
            dataDAO.createTableData();
            System.out.println("[DATABASE] Successfully created table Data.");

            // Table: Stats
            statsDAO = new StatsDAO(database.getConnection());
            statsDAO.createTableStats();
            statsDAO.insertProcedureChecker();
            System.out.println("[DATABASE] Successfully created table Stats.");

        } catch (Exception e) {
            System.out.println("[DATABASE] Error while creating tables... skipping (warn)");
            e.printStackTrace();
        }
    }

    public void otherGun(Platform targetPlatform) throws InterruptedException {
        // Creating Mw object
        ModernWarfare mw = new ModernWarfare(requestManager, targetedOpus);

        // For loop to get our 50 000 000 accounts KDA 2500000 - 5000000 for 1 000 000 accounts
        final int nbPageToFetch = 5000000;
        System.out.println("[!] Starting from page " + fetchedPagesAmount + " for " + targetPlatform.name() + " on " + nbPageToFetch + " pages | with " + maxThreadAtRuntime + " threads at runtime.");
        System.out.println("_____________________________________________________________");
        AtomicReference<Double> percentage = new AtomicReference<>((double) 0);

        // For loop to get our 50 000 000 accounts KDA 2500000 - 5000000 for 1 000 000 accounts
        for (int pageIndex = fetchedPagesAmount; pageIndex < nbPageToFetch; pageIndex++) {

            // Passing a variable to a thread
            int finalPageIndex = pageIndex;

            // while the number of threads is superior to 100, the thread will wait before creating a new one
            while (Thread.activeCount() >= maxThreadAtRuntime) {
                Thread.sleep(1);
            }

            // If the number of threads is inferior to 100 we sleep the thread for 1ms to avoid spamming the api
            if (Thread.activeCount() < maxThreadAtRuntime) {
                Thread.sleep(1);
            }

            // create a thread
            // TODO: Dégager cette merde et mettre des futures
            Thread currentThread = new Thread(() -> {
                JSONObject leaderboard = null;

                try {
                    leaderboard = mw.getLeaderboards(targetPlatform, TimeFrame.ALLTIME, Gamemode.CAREER, GameType.CORE, finalPageIndex);
                    if(leaderboard == null) {
                        System.out.println("[!] Error while fetching " + finalPageIndex + ".. retrying");
                        //Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    System.out.println("[!] Error while getting leaderboard (page " + finalPageIndex + ") retrying...");
                }

                // Looping through the leaderboard and our 20 players
                for (int userIndex = 0; userIndex < 19; userIndex++) {
                    // retrieving the users kda
                    BigDecimal kda;
                    //String username;
                    try {
                        kda = BigDecimal.valueOf(leaderboard.getJSONObject("data").getJSONArray("entries").getJSONObject(userIndex).getJSONObject("values").getDouble("kdRatio"));
                        //username = leaderboard.getJSONObject("data").getJSONArray("entries").getJSONObject(userIndex).getString("username");
                    } catch (Exception e) {
                        System.out.println("[!] Error while getting kda for user " + userIndex + " on page " + finalPageIndex + " maybe the end?");
                        break;
                    }

                    // Make it 2 decimals
                    kda = BigDecimal.valueOf(Math.round(kda.doubleValue() * 100.0) / 100.0);

                    try {
                        //dataDAO.insertData(new Data(kda, username, targetPlatform));
                        statsDAO.insertStats(kda, targetPlatform);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                percentage.set(Math.round(((double) finalPageIndex / (double) nbPageToFetch) * 10000.0) / 100.0);

                try {
                    System.out.println("Progress: " + finalPageIndex + "/" + nbPageToFetch + " (" + percentage + "%)| " + statsDAO.countNumberOfStats() + " different KDA");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                Thread.currentThread().interrupt();
            });

            currentThread.start();

            if(percentage.get() == 100.0) {
                System.out.println("Finished " + targetPlatform.name());
                System.out.println("_____________________________________________________________");
            }
        }
    }
}