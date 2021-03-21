package frontsnapk1ck.alloy.audio.lyric;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.connyscode.ctils.jTrack.Artist;
import com.github.connyscode.ctils.jTrack.Song;
import com.github.connyscode.ctils.jTrack.jTrackClient;
import com.github.connyscode.ctils.jTrack.backend.ArtistNotFoundException;
import com.github.connyscode.ctils.jTrack.backend.SongNotFoundException;
import com.github.connyscode.ctils.jTrack.backend.types.SongSearchResult;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class AlloyjLyricClient extends jTrackClient {

    private String token;

    public AlloyjLyricClient(String ACCESS_TOKEN) 
    {
        super(ACCESS_TOKEN);
        this.token = ACCESS_TOKEN;
    }

    /**
     * Perform a Search for Songs with parameters
     *
     * @param searchPhrase A Search Phrase
     * @see SongSearchResult
     */
    @Override
    public List<SongSearchResult> performSongSearch(String searchPhrase) 
    {
        return getSongSearch(Objects.requireNonNull(AlloyGeniusAPI.performSongSearch(searchPhrase, token)));
    }

    /**
     * Get a List of SearchResults
     *
     * @param jsonSearchResults jsonArray containing all search results
     * @return A List of SearchResults
     * @see SongSearchResult
     */
    protected List<SongSearchResult> getSongSearch(JSONArray jsonSearchResults) 
    {
        List<SongSearchResult> results = new ArrayList<>();
        for (Object jsonSearchResult : jsonSearchResults) 
        {
            if ((((JSONObject) jsonSearchResult).get("type")).equals("song")) 
            {
                JSONObject object = (JSONObject) ((JSONObject) jsonSearchResult).get("result");
                results.add(
                    new SongSearchResult(
                        object.get("full_title").toString(), 
                        object.get("url").toString(), 
                        object.get("song_art_image_url").toString(), 
                        Integer.parseInt(object.get("id").toString())
                    )
                );
            }
        }
        return results;
    }

    // protected Artist getArtistDetails(long artistID) throws ArtistNotFoundException 
    // {
    //     return AlloyGeniusAPI.parseArtist(artistID, token);
    // }

    /**
     * Get a Song out of a SearchResult
     *
     * @param result A single SearchResult
     * @return The Track that holds all Info like: Author, Title, ...
     * @see Song
     */
    @Override
    public Song getSong(SongSearchResult result) throws SongNotFoundException 
    {
        return AlloyGeniusAPI.parseSong(result.id, token);
    }

    /**
     * Get a Song out of a songID
     *
     * @param songID A song ID
     * @return The Track that holds all Info like: Author, Title, ...
     * @see Song
     */
    @Override
    public Song getSong(long songID) throws SongNotFoundException 
    {
        return AlloyGeniusAPI.parseSong(songID, token);
    }

    /**
     * Get an Artist out of an artistGID
     *
     * @param artistGID An artistGID
     * @return The Track that holds all Info like: Author, Title, ...
     * @see Song
     */
    @Override
    public Artist getArtist(long artistGID) throws ArtistNotFoundException 
    {
        return AlloyGeniusAPI.parseArtist(artistGID, token);
    }

}
