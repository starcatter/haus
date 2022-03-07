package pl.edu.pjwstk.pplebanski;

import org.jpl7.JPL;
import org.jpl7.fli.Prolog;
import pl.edu.pjwstk.pplebanski.haus.Haus;

import java.util.Arrays;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
//        System.load("C:/swipl/bin/libswipl.dll");
//        System.load("C:/swipl/bin/jpl.dll");
//
//        JPL.setNativeLibraryDir("C:/swipl/bin");
//        JPL.setNativeLibraryName("jpl");
//
//        Prolog.set_default_init_args(
//                new String[]{
//                        "C:/swipl/bin/jpl.dll",
//                        "-f", "none",
//                        "-g", "set_prolog_flag(debug_on_error,false)",
//                        "-q"
//                }
//        );

        if (System.getenv("SWI_HOME_DIR") != null ||
                System.getenv("SWI_EXEC_FILE") != null ||
                System.getenv("SWIPL_BOOT_FILE") != null) {
            String init_swi_config =
                    String.format("%s %s %s -g true -q --no-signals --no-packs",
                            System.getenv("SWI_EXEC_FILE") == null ? "swipl" :
                                    System.getenv("SWI_EXEC_FILE"),
                            System.getenv("SWIPL_BOOT_FILE") == null ? "" :
                                    String.format("-x %s", System.getenv("SWIPL_BOOT_FILE")),
                            System.getenv("SWI_HOME_DIR") == null ? "" :
                                    String.format("--home=%s", System.getenv("SWI_HOME_DIR")));
            System.out.printf("\nSWIPL initialized with: %s%n", init_swi_config);

            JPL.setDefaultInitArgs(init_swi_config.split("\\s+"));    // initialize SWIPL engine
        } else
            System.out.println("No explicit initialization done: no SWI_HOME_DIR, SWI_EXEC_FILE, or SWIPL_BOOT_FILE defined");

//        JPL.setTraditional(); // most often not used
        JPL.init();
        System.out.println("Prolog engine actual init args: " + Arrays.toString(Prolog.get_actual_init_args()));


        Haus h = new Haus();

        System.out.println("\n\nlistRooms...");
        try {
            h.listRooms();
        } catch (Exception e) {
            System.out.println("listRooms (outside) Error...");
        }

        System.out.println("\n\nlistPassages...");
        try {
            h.listPassages();
        } catch (Exception e) {
            System.out.println("listPassages (outside) Error...");
        }

        System.out.println("\n\nfindPath...");
        try {
            //{outside,anteRoom,kitchen,
            // 0		1		2
            // bathroom,livingroom,secondBedroom,
            // 3		4			5
            // masterBedroom,westBalcony,northEastBalcony,
            // 6				7			8
            // southEastBalcony}; <- 9
            h.findPath(Haus.locs[2], Haus.locs[6]);
            h.findPath(Haus.locs[0], Haus.locs[6]);
            h.findPath(Haus.locs[1], Haus.locs[8]);
            h.findPath(Haus.locs[7], Haus.locs[0]);
            h.findPath(Haus.locs[5], Haus.locs[6]);
        } catch (Exception e) {
            System.out.println("findPath (outside) Error...");
        }

        System.out.println("\n\nActivating the cat...");
        h.activateCat();
        System.out.println("\n\nActivating the old lady...");
        h.activateGramma();

        System.out.println("\nFIGHT!\n");

        for (int i = 1; i <= 200; i++) {
            //System.out.println( "\n\nActivating the cat..." );

            System.out.println("\nTurn " + i + ":");

            try {
                h.activateCat();
            } catch (Exception e) {
                System.out.println("cat Error...");
            }

            //System.out.println( "\n\nActivating the old lady..." );
            try {
                h.activateGramma();
            } catch (Exception e) {
                System.out.println("gramma Error...");
            }

            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
}
