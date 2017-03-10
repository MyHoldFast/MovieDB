package holdfast;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HoldFast
 */
public class Main {

    public static void main(String[] args) {
        
        MovieAPI api = new MovieAPI();      
        String films = "asdfgsdfg,гладиатор, достучаться до небес, я начало, леон, пила, семь";
        for(String film:films.split(",")){
            System.out.println((api.getID(film.trim())));
        }
                
        HashMap film = api.searchByName("гладиатор");       
        if (film != null) {
            System.out.println(film.get("title"));
            System.out.println("Дата: " + film.get("release"));
            System.out.println("Жанр: " + film.get("genres"));
            System.out.println("Описание: " + film.get("overview"));
            System.out.println("Постер: " + MovieAPI.img_url + film.get("poster"));
        }
    }
}
