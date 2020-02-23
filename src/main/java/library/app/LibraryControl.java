package library.app;

import library.exception.*;
import library.io.ConsolePrinter;
import library.io.DataReader;
import library.io.file.FileManager;
import library.io.file.FileManagerBuilder;
import library.model.Book;
import library.model.Library;
import library.model.LibraryUser;
import library.model.Magazine;
import library.model.comparator.AlphabeticalTitleComparator;

import java.util.InputMismatchException;

class LibraryControl {
    private ConsolePrinter printer = new ConsolePrinter();
    private DataReader dataReader = new DataReader( printer );
    private FileManager fileManager;

    private Library library;

    LibraryControl() {
        fileManager = new FileManagerBuilder( printer, dataReader ).build();
        try {
            library = fileManager.importData();
            printer.printLine( "Imported data from a file " );
        } catch (DataImportException | InvalidDataException e) {
            printer.printLine( e.getMessage() );
            printer.printLine( "A new base has been initiated. " );
            library = new Library();
        }
    }

    void controlLoop() {
        Option option;

        do {
            printOptions();
            option = getOption();
            switch (option) {
                case ADD_BOOK:
                    addBook();
                    break;
                case ADD_MAGAZINE:
                    addMagazine();
                    break;
                case PRINT_BOOKS:
                    printBooks();
                    break;
                case PRINT_MAGAZINES:
                    printMagazines();
                    break;
                case DELETE_BOOK:
                    deleteBook();
                    break;
                case DELETE_MAGAZINE:
                    deleteMagazine();
                    break;
                case ADD_USER:
                    addUser();
                    break;
                case PRINT_USERS:
                    printUsers();
                    break;
                case EXIT:
                    exit();
                    break;
                default:
                    printer.printLine( "There is no such option, please enter again: " );
            }
        } while (option != Option.EXIT);
    }

    private Option getOption() {
        boolean optionOk = false;
        Option option = null;
        while (!optionOk) {
            try {
                option = Option.createFromInt( dataReader.getInt() );
                optionOk = true;
            } catch (NoSuchOptionException e) {
                printer.printLine( e.getMessage() + ", provide again: " );
            } catch (InputMismatchException ignored) {
                printer.printLine( "You entered a value that is not a number, please enter again: " );
            }
        }

        return option;
    }

    private void printOptions() {
        printer.printLine( "Choose an option: " );
        for (Option option : Option.values()) {
            printer.printLine( option.toString() );
        }
    }

    private void addBook() {
        try {
            Book book = dataReader.readAndCreateBook();
            library.addPublication( book );
        } catch (InputMismatchException e) {
            printer.printLine( "Failed to create book, invalid data. " );
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine( "Capacity limit reached, no more book can be added. " );
        }
    }

    private void addMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            library.addPublication( magazine );
        } catch (InputMismatchException e) {
            printer.printLine( "Failed to create storage, invalid data. " );
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.printLine( "Capacity limit reached, no more storage can be added. " );
        }
    }


    private void addUser() {
        LibraryUser libraryUser = dataReader.createLibraryUser();
        try {
            library.addUser( libraryUser );
        } catch (UserAlreadyExistsException e) {
            printer.printLine( e.getMessage() );
        }
    }


    private void printBooks() {
        printer.printBooks( library.getSortedPublications( new AlphabeticalTitleComparator() ) );
    }

    private void printMagazines() {
        printer.printMagazines( library.getSortedPublications( new AlphabeticalTitleComparator() ) );
    }

    private void printUsers() {
        printer.printUsers( library.getSortedUsers( (p1, p2) -> p1.getLastName().compareToIgnoreCase( p2.getLastName() ) ) );
    }

    private void deleteMagazine() {
        try {
            Magazine magazine = dataReader.readAndCreateMagazine();
            if (library.removePublication( magazine ))
                printer.printLine( "Warehouse deleted. " );
            else
                printer.printLine( "No warehouse indicated. " );
        } catch (InputMismatchException e) {
            printer.printLine( "Failed to create storage, invalid data. " );
        }
    }

    private void deleteBook() {
        try {
            Book book = dataReader.readAndCreateBook();
            if (library.removePublication( book ))
                printer.printLine( "Book has been deleted. " );
            else
                printer.printLine( "No book specified. " );
        } catch (InputMismatchException e) {
            printer.printLine( "Failed to create book, invalid data. " );
        }
    }

    private void exit() {
        try {
            fileManager.exportData( library );
            printer.printLine( "Data export to file successful. " );
        } catch (DataExportException e) {
            printer.printLine( e.getMessage() );
        }
        dataReader.close();
        printer.printLine( "End of program. " );
    }

    private enum Option {
        EXIT( 0, "Exit the program " ),
        ADD_BOOK( 1, "Book addition " ),
        ADD_MAGAZINE( 2, "Addition of a magazine / newspaper " ),
        PRINT_BOOKS( 3, "Display available books " ),
        PRINT_MAGAZINES( 4, "Display of available magazines / newspapers " ),
        DELETE_BOOK( 5, "Delete book " ),
        DELETE_MAGAZINE( 6, "Delete the magazine " ),
        ADD_USER( 7, "Add a reader " ),
        PRINT_USERS( 8, "Display readers " );

        private int value;
        private String description;

        Option(int value, String desc) {
            this.value = value;
            this.description = desc;
        }

        @Override
        public String toString() {
            return value + " - " + description;
        }

        static Option createFromInt(int option) throws NoSuchOptionException {
            try {
                return Option.values()[option];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new NoSuchOptionException( "No option with id " + option );
            }
        }
    }
}