package holdfast;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author HoldFast
 */
public class MovieAPI {

    private static String api_url = "https://api.themoviedb.org/3/";
    public static String img_url = "https://image.tmdb.org/t/p/w300";
    private static String api_key = "d272326e467344029e68e3c4ff0b4059";
    private static String lang = "ru-RU";
    private static final HashMap genres = new HashMap();

    static {
        genres.put(28, "боевик");
        genres.put(12, "приключения");
        genres.put(16, "мультфильм");
        genres.put(35, "комедия");
        genres.put(80, "криминал");
        genres.put(99, "документальный");
        genres.put(18, "драма");
        genres.put(10751, "семейный");
        genres.put(14, "фэнтези");
        genres.put(36, "история");
        genres.put(27, "ужасы");
        genres.put(10402, "музыка");
        genres.put(9648, "детектив");
        genres.put(10749, "мелодрама");
        genres.put(878, "фантастика");
        genres.put(10770, "tелевизионный фильм");
        genres.put(53, "триллер");
        genres.put(10752, "военный");
        genres.put(37, "вестерн");
    }

    public int getID(String name) {
        try {
            int i = 0;
            boolean find = false;
            name = ucwords(name);
            JSONObject result = new JSONObject(query("search/movie", "query=" + URLEncoder.encode(name, "UTF-8") + "&page=1&include_adult=true"));
            if (result.getInt("total_results") > 0) {
                JSONArray results = result.getJSONArray("results");
                for (i = 0; i < results.length(); i++) {
                    if (name.equals(ucwords(results.getJSONObject(i).getString("title")))) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    i = 0;
                }
                return results.getJSONObject(i).getInt("id");
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return -1;
        }
        return -1;
    }

    public HashMap searchByName(String name) {
        HashMap film_map = new HashMap();
        try {
            int i = 0;
            boolean find = false;
            name = ucwords(name);
            JSONObject result = new JSONObject(query("search/movie", "query=" + URLEncoder.encode(name, "UTF-8") + "&page=1&include_adult=true"));
            if (result.getInt("total_results") > 0) {
                JSONArray results = result.getJSONArray("results");
                for (i = 0; i < results.length(); i++) {
                    if (name.equals(ucwords(results.getJSONObject(i).getString("title")))) {
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    i = 0;
                }
                JSONObject film = results.getJSONObject(i);
                JSONArray film_genres = null;
                if (film.has("genre_ids")) {
                    film_genres = film.getJSONArray("genre_ids");
                }
                film_map.put("id", film.getInt("id"));
                film_map.put("title", film.getString("title"));
                film_map.put("release", formatDate(film.getString("release_date")));
                film_map.put("overview", film.getString("overview"));
                if (film.has("poster_path")) {
                    film_map.put("poster", film.getString("poster_path"));
                } else if (film.has("backdrop_path")) {
                    film_map.put("poster", film.getString("backdrop_path"));
                }
                String _genres = "";
                if (film_genres != null) {
                    for (int ii = 0; ii < film_genres.length(); ii++) {
                        _genres += genres.get(film_genres.getInt(ii)) + ((ii != film_genres.length() - 1) ? ", " : "");
                    }
                }
                film_map.put("genres", _genres);
                return film_map;
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            return null;
        }
        return null;
    }

    private String query(String method, String params) {
        String request = IOUtil.get(api_url + method + "?api_key=" + api_key + "&language=" + lang + "&" + params);
        return request;
    }

    private String formatDate(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        DateFormat df = new SimpleDateFormat("yyyy-dd-MM");
        Date res = df.parse(date);
        return formatter.format(res);
    }

    private static String ucwords(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        str = str.trim();
        if (str.contains(" ")) {
            final StringBuilder sb = new StringBuilder();
            while (str.contains(" ")) {
                String tmp = str.substring(0, str.indexOf(" "));
                if (!tmp.trim().isEmpty()) {
                    sb.append(Character.toUpperCase(tmp.charAt(0))).append(tmp.substring(1, tmp.length()).toLowerCase()).append(' ');
                }
                str = str.substring(str.indexOf(" ") + 1, str.length());
            }
            sb.append(Character.toUpperCase(str.charAt(0))).append(str.substring(1, str.length()).toLowerCase());
            str = sb.toString().trim();
        } else {
            str = Character.toUpperCase(str.charAt(0)) + str.substring(1, str.length()).toLowerCase();
        }
        return str;
    }
}
