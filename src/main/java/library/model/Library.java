package library.model;

import library.exception.PublicationAlreadyExistsException;
import library.exception.UserAlreadyExistsException;

import java.io.Serializable;
import java.util.*;

public class Library implements Serializable {


    private Map<String, Publication> publications = new HashMap<>();

    public Collection<Publication> getSortedPublications(Comparator<Publication> comparator) {
        List<Publication> list = new ArrayList<>( this.publications.values() );
        list.sort( comparator );
        return list;
    }

    private Map<String, LibraryUser> users = new HashMap<>();

    public Collection<LibraryUser> getSortedUsers(Comparator<LibraryUser> comparator) {
        List<LibraryUser> list = new ArrayList<>( this.users.values() );
        list.sort( comparator );
        return list;
    }


    public Map<String, Publication> getPublications() {
        return publications;
    }


    public Map<String, LibraryUser> getUsers() {
        return users;
    }


    public void addUser(LibraryUser user) {
        if (users.containsKey( user.getPesel() ))
            throw new UserAlreadyExistsException(
                    "A user with the specified username already exists " + user.getPesel()
            );
        users.put( user.getPesel(), user );
    }


    public void addPublication(Publication publication) {
        if (publications.containsKey( publication.getTitle() ))
            throw new PublicationAlreadyExistsException(
                    "A publication with this title already exists " + publication.getTitle()
            );
        publications.put( publication.getTitle(), publication );
    }


    public boolean removePublication(Publication publication) {
        if (publications.containsValue( publication )) {
            publications.remove( publication.getTitle() );
            return true;
        } else {
            return false;
        }
    }
}