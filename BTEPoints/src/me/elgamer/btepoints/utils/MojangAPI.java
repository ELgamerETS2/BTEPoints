package me.elgamer.btepoints.utils;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

public class MojangAPI {

	public static String getName(String uuid) {

		String host = "https://api.mojang.com/user/profiles/" + uuid.replace("-","") + "/names";
		
		try {
			@SuppressWarnings("deprecation")
			String nameJson = IOUtils.toString(new URL(host));           
			JSONArray nameValue = (JSONArray) JSONValue.parseWithException(nameJson);
			String playerSlot = nameValue.get(nameValue.size()-1).toString();
			JSONObject nameObject = (JSONObject) JSONValue.parseWithException(playerSlot);
			return nameObject.get("name").toString();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String getUuid(String name) {
		
		String host = "https://api.mojang.com/users/profiles/minecraft/"+name;
       
		try {
            @SuppressWarnings("deprecation")
            String UUIDJson = IOUtils.toString(new URL(host));           
            if(UUIDJson.isEmpty()) return null;                       
            JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);
            return UUIDObject.get("id").toString();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }

	}

}
