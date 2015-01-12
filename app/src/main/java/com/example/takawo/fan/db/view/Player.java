package com.example.takawo.fan.db.view;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Takawo on 2014/12/29.
 */
public class Player {

    private static final String VIEW_NAME = "players";
    private static final String BY_ID_VIEW_NAME = "players_by_id";
    private static final String DOC_TYPE = "player";

    public static Query getQuery(Database database) {
        View view = database.getView(VIEW_NAME);
        if (view.getMap() == null) {
            Mapper mapper = new Mapper() {
                public void map(Map<String, Object> document, Emitter emitter) {
                    String type = (String)document.get("type");
                    if (DOC_TYPE.equals(type)) {
                        emitter.emit(document.get("title"), document);
                    }
                }
            };
            view.setMap(mapper, "1");
        }

        Query query = view.createQuery();
        return query;
    }

    public static Query getQueryById(Database database, String playerId) {
        View view = database.getView(BY_ID_VIEW_NAME);
        if (view.getMap() == null) {
            Mapper map = new Mapper() {
                @Override
                public void map(Map<String, Object> document, Emitter emitter) {
                    if (DOC_TYPE.equals(document.get("type"))) {
                        emitter.emit(document.get("playerId"), document);
                    }
                }
            };
            view.setMap(map, "1");
        }

        Query query = view.createQuery();
        java.util.List<Object> keys = new ArrayList<Object>();
        keys.add(playerId);
        query.setKeys(keys);

        return query;
    }

    public static Document getPlayerProfileById(Database database, String playerId) {
        Document player = null;
        try {
            QueryEnumerator enumerator = Player.getQueryById(database, playerId).run();
            player = enumerator != null && enumerator.getCount() > 0 ?
                    enumerator.getRow(0).getDocument() : null;
        } catch (CouchbaseLiteException e) { }

        return player;
    }

    public static Document createPlayer(Database database, String playerId, String playerName, String gameEvent,
                                        String category_id, String category_period, String category_name, String resultType,
                                        String color, String comment)
            throws CouchbaseLiteException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Calendar calendar = GregorianCalendar.getInstance();
        String currentTimeString = dateFormatter.format(calendar.getTime());

        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("type", DOC_TYPE);
        properties.put("player_id", playerId);
        properties.put("player_name", playerName);
        properties.put("game_event", gameEvent);
//        Map<String, Object> category = new HashMap<String, Object>();
//        category.put("category_id", category_id);
//        category.put("period", category_period);
//        category.put("category_name", category_name);
//        properties.put("categories", category);
        properties.put("result_type", resultType);
        properties.put("color", color);
        properties.put("comment", comment);

        Document document = database.getDocument("player:" + playerId);
        document.putProperties(properties);

        return document;
    }

}
