package library.app;


public class LibraryApp {
    public static void main(String[] args) {
        final String appName = "Library project";
        System.out.println( appName );
        LibraryControl libraryControl = new LibraryControl();
        libraryControl.controlLoop();
    }
}
