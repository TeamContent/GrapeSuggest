package model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import apps.appsProxy;
import authority.userDBHelper;
import database.db;
import esayhelper.JSONHelper;
import esayhelper.formHelper;
import esayhelper.formHelper.formdef;
import nlogger.nlogger;
import rpc.execRequest;

public class SuggestModel {
	private static userDBHelper suggest;
	private static formHelper form;
	
	static{
		suggest = new userDBHelper("suggest", (String) execRequest.getChannelValue("sid"));
		nlogger.logout((String) execRequest.getChannelValue("sid"));
		form = suggest.getChecker();
	}
	public SuggestModel() {
		form.putRule("content", formdef.notNull);
	}
	public JSONObject check(String info,HashMap<String, Object> map) {
		JSONObject object = AddMap(map, info);
		return !form.checkRuleEx(object) ? null : object;
	}
	public db getdb() {
		return suggest.bind(String.valueOf(appsProxy.appid()));
	}
	/**
	 * 将map添加至JSONObject中
	 * 
	 * @param map
	 * @param object
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject AddMap(HashMap<String, Object> map, String Info) {
		JSONObject object = JSONHelper.string2json(Info);
		if (object != null) {
			if (map.entrySet() != null) {
				Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String, Object> entry = (Map.Entry<String, Object>) iterator.next();
					if (!object.containsKey(entry.getKey())) {
						object.put(entry.getKey(), entry.getValue());
					}
				}
			}
		}
		return object;
	}
}