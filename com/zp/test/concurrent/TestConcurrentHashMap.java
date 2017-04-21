package com.zp.test.concurrent;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TestConcurrentHashMap {
	static Map<String, String> map = new Hashtable<String, String>();
	// static Map<String,String> map = new ConcurrentHashMap<String, String>();

	static AtomicInteger ai = new AtomicInteger(0);

	static void initMap() {
		for (int i = 0; i < 10000; i++) {
			map.put("" + i, "" + i + i);
		}
	}

	class DDMap extends Thread {

		// private Map<String,String> mapCopy = new HashMap<String, String>();
		//
		// private DDMap(Map<String,String> mapCopy){
		// this.mapCopy = mapCopy;
		// }

		@Override
		public void run() {
			synchronized (map) {
				Iterator<Entry<String, String>> ite = map.entrySet().iterator();

				while (ite.hasNext()) {
					try {
						Thread.sleep(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// map.put(""+(ite.next().getKey()+1000, value));
					System.out.println(ite.next().getKey());
					ai.set(ai.get() + 1);
				}
			}
		}
	}

	class RemoveMap extends Thread {

		@Override
		public void run() {
			synchronized (map) {
				for (int i = 1000; i < 9000; i++) {
					try {
						Thread.sleep(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					map.remove("" + i);
				}
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		initMap();
		// Map<String,String> mapCopy = new HashMap<String, String>();
		// mapCopy.putAll(map);

		Thread DDThread = new TestConcurrentHashMap().new DDMap();
		Thread RemoveThread = new TestConcurrentHashMap().new RemoveMap();
		DDThread.start();
		RemoveThread.start();

		RemoveThread.join();
		DDThread.join();

		System.out.println("-------ai-------------" + ai.get());
		System.out.println("----------map.size---------" + map.size());
	}
}
