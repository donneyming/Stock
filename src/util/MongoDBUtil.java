package util;

import java.beans.BeanInfo;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;
import java.lang.reflect.Field;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.beanutils.BeanUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.util.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * MongoDB工具类 Mongo实例代表了一个数据库连接池，即使在多线程的环境中，一个Mongo实例对我们来说已经足够了<br>
 * 注意Mongo已经实现了连接池，并且是线程安全的。 <br>
 * 设计为单例模式， 因 MongoDB的Java驱动是线程安全的，对于一般的应用，只要一个Mongo实例即可，<br>
 * Mongo有个内置的连接池（默认为10个） 对于有大量写和读的环境中，为了确保在一个Session中使用同一个DB时，<br>
 * DB和DBCollection是绝对线程安全的<br>
 * 
 * @author zhoulingfei
 * @date 2015-5-29 上午11:49:49
 * @version 0.0.0
 * @Copyright (c)1997-2015 NavInfo Co.Ltd. All Rights Reserved.
 */
public enum MongoDBUtil {

	/**
	 * 定义一个枚举的元素，它代表此类的一个实例
	 */
	instance;
	private Mongo mongo;
	private MongoClient mongoClient;
	private static final String MONGO_DB_ADDRESS = "localhost";
	private static final int MONGO_DB_PORT = 27017;
	private static final String MONGO_DB_USERNAME = "root";
	private static final String MONGO_DB_PASSWORD = "root";
	private static final String MONGO_DB_DBNAME = "JJDB";
	private static final String MONGO_DB_RESOURCE_FILE = "mongodb.properties";
	private DB db;
	static Log log = Log.getLogger();
	static {
		System.out
				.println("===============MongoDBUtil初始化========================");
		log.logger
				.info("===============MongoDBUtil初始化========================");
		String path = MongoDBUtil.class.getResource("/").getPath();
		String fileName = path + MONGO_DB_RESOURCE_FILE;

		try {
			File file = new File(fileName);
			if (!file.exists()) {
				PropertiesUtil.updateProperties("mongo.db.address",
						MONGO_DB_ADDRESS, fileName);
				PropertiesUtil.updateProperties("mongo.db.port",
						String.valueOf(MONGO_DB_PORT), fileName);
				PropertiesUtil.updateProperties("mongo.db.username",
						MONGO_DB_USERNAME, fileName);
				PropertiesUtil.updateProperties("mongo.db.password",
						MONGO_DB_PASSWORD, fileName);
				PropertiesUtil.updateProperties("mongo.db.dbname",
						MONGO_DB_DBNAME, fileName);
			}

		} catch (Exception e) {
			e.printStackTrace();
			log.logger.error("MongoDBUtil->MongoDBUtil" + e.getMessage());
		}
		try {
			// 从配置文件中获取属性值
			String ip = PropertiesUtil.getProperties(fileName)
					.get("mongo.db.address").toString();
			int port = Integer.parseInt(PropertiesUtil.getProperties(fileName)
					.get("mongo.db.port").toString());
			instance.mongoClient = new MongoClient(ip, port);
			instance.mongo = new Mongo(ip, port);
			instance.db = instance.mongo.getDB(MONGO_DB_DBNAME);
			// or, to connect to a replica set, with auto-discovery of the
			// primary, supply a seed list of members
			// List<ServerAddress> listHost = Arrays.asList(new
			// ServerAddress("localhost", 27017),new ServerAddress("localhost",
			// 27018));
			// instance.mongoClient = new MongoClient(listHost);

			// 大部分用户使用mongodb都在安全内网下，但如果将mongodb设为安全验证模式，就需要在客户端提供用户名和密码：
			// boolean auth = db.authenticate(myUserName, myPassword);
			Builder options = new MongoClientOptions.Builder();
			// options.autoConnectRetry(true);// 自动重连true
			// options.maxAutoConnectRetryTime(10); // the maximum auto connect
			// retry time
			options.connectionsPerHost(300);// 连接池设置为300个连接,默认为100
			options.connectTimeout(15000);// 连接超时，推荐>3000毫秒
			options.maxWaitTime(5000); //
			options.socketTimeout(0);// 套接字超时时间，0无限制
			options.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出“Out
																		// of
																		// semaphores
																		// to
																		// get
																		// db”错误。
			options.writeConcern(WriteConcern.SAFE);//
			options.build();
		} catch (Exception ex) {

			log.logger.error("MongoDBUtil->MongoDBUtil" + ex.getMessage());
		}
	}

	// ------------------------------------共用方法---------------------------------------------------
	/**
	 * 获取DB实例 - 指定DB
	 * 
	 * @param dbName
	 * @return
	 */

	public DB getDb(String dbName) {

		return this.db;
	}

	/**
	 * 获取collection对象 - 指定Collection
	 * 
	 * @param collName
	 * @return
	 */
	public DBCollection getCollection(String collName) {
		if (null == collName || "".equals(collName)) {
			return null;
		}
		DBCollection collection = null;
		try {
			collection = db.getCollection(collName);
			if (collection == null) {
				collection = db.createCollection(collName, null);
			}
			return collection;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		return collection;
	}

	/**
	 * 查询DB下的所有表名
	 */
	public List<String> getAllCollections(String dbName) {

		return null;
	}

	/**
	 * 获取所有数据库名称列表
	 * 
	 * @return
	 */
	public MongoIterable<String> getAllDBNames() {
		MongoIterable<String> s = mongoClient.listDatabaseNames();
		return s;
	}

	public DBObject map2Obj(Map<String, Object> map) {
		DBObject obj = new BasicDBObject();
		if (map.containsKey("class") && map.get("class") instanceof Class)
			map.remove("class");
		obj.putAll(map);
		return obj;
	}

	public DBObject insert(String collName, DBObject obj) {
		getCollection(collName).insert(obj);
		return obj;
	}

	public void insertBatch(String collName, List<DBObject> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		List<DBObject> listDB = new ArrayList<DBObject>();
		for (int i = 0; i < list.size(); i++) {
			listDB.add(list.get(i));
		}
		getCollection(collName).insert(listDB);
	}

	public void delete(String collName, DBObject obj) {
		getCollection(collName).remove(obj);
	}

	public void deleteBatch(String collName, List<DBObject> list) {
		if (list == null || list.isEmpty()) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			getCollection(collName).remove(list.get(i));
		}
	}

	public long getCollectionCount(String collName) {
		return getCollection(collName).getCount();
	}

	public long getCount(String collName, DBObject obj) {
		if (obj != null)
			return getCollection(collName).getCount(obj);
		return getCollectionCount(collName);
	}

	public List<DBObject> find(String collName, DBObject obj) {
		DBCursor cur = getCollection(collName).find(obj);
		return DBCursor2list(cur);
	}

	public List<DBObject> find(String collName, DBObject query, DBObject sort) {
		DBCursor cur;
		if (query != null) {
			cur = getCollection(collName).find(query);
		} else {
			cur = getCollection(collName).find();
		}
		if (sort != null) {
			cur.sort(sort);
		}
		return DBCursor2list(cur);
	}

	public List<DBObject> find(String collName, DBObject query, DBObject sort,
			int start, int limit) {
		DBCursor cur;
		if (query != null) {
			cur = getCollection(collName).find(query);
		} else {
			cur = getCollection(collName).find();
		}
		if (sort != null) {
			cur.sort(sort);
		}
		if (start == 0) {
			cur.batchSize(limit);
		} else {
			cur.skip(start).limit(limit);
		}
		return DBCursor2list(cur);
	}

	private List<DBObject> DBCursor2list(DBCursor cur) {
		List<DBObject> list = new ArrayList<DBObject>();
		if (cur != null) {
			list = cur.toArray();
		}
		return list;
	}

	public void update(String collName, DBObject setFields, DBObject whereFields) {
		getCollection(collName).updateMulti(setFields, whereFields);
	}

	public List<DBObject> findAll(String collName) {
		DBCursor cur = getCollection(collName).find();
		List<DBObject> list = new ArrayList<DBObject>();
		if (cur != null) {
			list = cur.toArray();
		}
		return list;
	}

	public DBObject getById(String collName, String id) {
		DBObject obj = new BasicDBObject();
		obj.put("_id", new ObjectId(id));
		DBObject result = getCollection(collName).findOne(obj);
		return result;
	}

	/**
	 * 查找对象 - 根据主键_id
	 * 
	 * @param collection
	 * @param id
	 * @return
	 */
	public Document findById(MongoCollection<Document> coll, String id) {
		ObjectId _idobj = null;
		try {
			_idobj = new ObjectId(id);
		} catch (Exception e) {
			return null;
		}
		Document myDoc = coll.find(Filters.eq("_id", _idobj)).first();
		return myDoc;
	}

	/** 统计数 */
	public int getCount(MongoCollection<Document> coll) {
		int count = (int) coll.count();
		return count;
	}

	/** 条件查询 */
	public MongoCursor<Document> find(MongoCollection<Document> coll,
			Bson filter) {
		return coll.find(filter).iterator();
	}

	/** 分页查询 */
	public MongoCursor<Document> findByPage(MongoCollection<Document> coll,
			Bson filter, int pageNo, int pageSize) {
		Bson orderBy = new BasicDBObject("_id", 1);
		return coll.find(filter).sort(orderBy).skip((pageNo - 1) * pageSize)
				.limit(pageSize).iterator();
	}

	/**
	 * 通过ID删除
	 * 
	 * @param coll
	 * @param id
	 * @return
	 */
	public int deleteById(MongoCollection<Document> coll, String id) {
		int count = 0;
		ObjectId _id = null;
		try {
			_id = new ObjectId(id);
		} catch (Exception e) {
			return 0;
		}
		Bson filter = Filters.eq("_id", _id);
		DeleteResult deleteResult = coll.deleteOne(filter);
		count = (int) deleteResult.getDeletedCount();
		return count;
	}

	/**
	 * FIXME
	 * 
	 * @param coll
	 * @param id
	 * @param newdoc
	 * @return
	 */
	public Document updateById(MongoCollection<Document> coll, String id,
			Document newdoc) {
		ObjectId _idobj = null;
		try {
			_idobj = new ObjectId(id);
		} catch (Exception e) {
			return null;
		}
		Bson filter = Filters.eq("_id", _idobj);
		// coll.replaceOne(filter, newdoc); // 完全替代
		coll.updateOne(filter, new Document("$set", newdoc));
		return newdoc;
	}

	public void dropCollection(String collName) {
		getCollection(collName).drop();
	}

	/**
	 * 关闭Mongodb
	 */
	public void close() {
		if (mongoClient != null) {
			mongoClient.close();
			mongoClient = null;
		}
	}

	/**
	 * 把实体bean对象转换成DBObject
	 * 
	 * @param bean
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static <T> DBObject bean2DBObject(T bean)
			throws IllegalArgumentException, IllegalAccessException {
		if (bean == null) {
			return null;
		}
		DBObject dbObject = new BasicDBObject();
		// 获取对象对应类中的所有属性域
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields) {
			// 获取属性名
			String varName = field.getName();
			// 修改访问控制权限
			boolean accessFlag = field.isAccessible();
			if (!accessFlag) {
				field.setAccessible(true);
			}
			Object param = field.get(bean);
			if (param == null) {
				continue;
			} else if (param instanceof Integer) {// 判断变量的类型
				int value = ((Integer) param).intValue();
				dbObject.put(varName, value);
			} else if (param instanceof String) {
				String value = (String) param;
				dbObject.put(varName, value);
			} else if (param instanceof Double) {
				double value = ((Double) param).doubleValue();
				dbObject.put(varName, value);
			} else if (param instanceof Float) {
				float value = ((Float) param).floatValue();
				dbObject.put(varName, value);
			} else if (param instanceof Long) {
				long value = ((Long) param).longValue();
				dbObject.put(varName, value);
			} else if (param instanceof Boolean) {
				boolean value = ((Boolean) param).booleanValue();
				dbObject.put(varName, value);
			} else if (param instanceof Date) {
				Date value = (Date) param;
				dbObject.put(varName, value);
			}
			// 恢复访问控制权限
			field.setAccessible(accessFlag);
		}
		return dbObject;
	}

	/**
	 * 把DBObject转换成bean对象
	 * 
	 * @param dbObject
	 * @param bean
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public static <T> T dbObject2Bean(DBObject dbObject, T bean) {
		if (bean == null) {
			return null;
		}
		Map map = new HashMap();
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields) {
			String varName = field.getName();
			try {
				Object object = dbObject.get(varName);
				if (object != null) {
					BeanUtils.setProperty(bean, varName, object);
					// map.put(varName, object);
				}
			} catch (Exception ex) {
				System.out.print(ex);
			}

		}
		return bean;

	}

	// json字符串转成JavaBean
	// 测试已通过
	@SuppressWarnings("unchecked")
	public static <T> T json2Bean(String jsonString, Class<T> beanCalss) {
		JSONObject jsonObject = JSONObject.fromObject(jsonString);
		T bean = (T) JSONObject.toBean(jsonObject, beanCalss);

		return bean;
	}

	// JavaBean转成json字符串
	// 测试已通过
	
	public static DBObject bean2DBobject2(Object object)
	{
	
		String strJson = object2json(object); 
		strJson=strJson.replaceAll("\'","\""); 
		DBObject db = (BasicDBObject) JSON.parse(strJson);
		return db;
	}
	public static String object2json(Object obj) {
        StringBuilder json = new StringBuilder();
        if (obj == null) {
            json.append("\"\"");
        } else if (obj instanceof String || obj instanceof Integer || obj instanceof Float || obj instanceof Boolean
                || obj instanceof Short || obj instanceof Double || obj instanceof Long || obj instanceof BigDecimal
                || obj instanceof BigInteger || obj instanceof Byte) {
            json.append("\"").append(string2json(obj.toString())).append("\"");
        } else if (obj instanceof Object[]) {
            json.append(array2json((Object[]) obj));
        } else if (obj instanceof List) {
            json.append(list2json((List<?>) obj));
        } else if (obj instanceof Map) {
            json.append(map2json((Map<?, ?>) obj));
        } else if (obj instanceof Set) {
            json.append(set2json((Set<?>) obj));
        } else {
            json.append(bean2json(obj));
        }
        return json.toString();
    }
   
    public static String bean2json(Object bean) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        PropertyDescriptor[] props = null;
        try {
            props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
        } catch (IntrospectionException e) {
        }
        if (props != null) {
            for (int i = 0; i < props.length; i++) {
                try {
                    String name = object2json(props[i].getName());
                    String value = object2json(props[i].getReadMethod().invoke(bean));
                    json.append(name);
                    json.append(":");
                    json.append(value);
                    json.append(",");
                } catch (Exception e) {
                }
            }
            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }
        return json.toString();
    }
   
    public static String list2json(List<?> list) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                json.append(object2json(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }
   
    public static String array2json(Object[] array) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (array != null && array.length > 0) {
            for (Object obj : array) {
                json.append(object2json(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }
   
    public static String map2json(Map<?, ?> map) {
        StringBuilder json = new StringBuilder();
        json.append("{");
        if (map != null && map.size() > 0) {
            for (Object key : map.keySet()) {
                json.append(object2json(key));
                json.append(":");
                json.append(object2json(map.get(key)));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, '}');
        } else {
            json.append("}");
        }
        return json.toString();
    }
   
    public static String set2json(Set<?> set) {
        StringBuilder json = new StringBuilder();
        json.append("[");
        if (set != null && set.size() > 0) {
            for (Object obj : set) {
                json.append(object2json(obj));
                json.append(",");
            }
            json.setCharAt(json.length() - 1, ']');
        } else {
            json.append("]");
        }
        return json.toString();
    }
   
    public static String string2json(String s) {
        if (s == null)
            return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
            case '"':
                sb.append("\\\"");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '/':
                sb.append("\\/");
                break;
            default:
                if (ch >= '\u0000' && ch <= '\u001F') {
                    String ss = Integer.toHexString(ch);
                    sb.append("\\u");
                    for (int k = 0; k < 4 - ss.length(); k++) {
                        sb.append('0');
                    }
                    sb.append(ss.toUpperCase());
                } else {
                    sb.append(ch);
                }
            }
        }
        return sb.toString();
    }

	/**
	 * 测试入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		String collName = "COMMUNITY_BJ";
		MongoDBUtil dbUtil = MongoDBUtil.instance;

		// 插入多条
		for (int i = 1; i <= 4; i++) {
			BasicDBObject doc = new BasicDBObject();
			doc.put("name", "zhoulf");
			doc.put("school", "NEFU" + i);
			Document interests = new Document();
			interests.put("game", "game" + i);
			interests.put("ball", "ball" + i);
			doc.put("interests", interests);
			dbUtil.insert(collName, doc);
		}

		// // 根据ID查询

		// /查询多个
		// MongoCursor<Person> cursor2 = coll.find(Person.class).iterator();

		// 删除数据库
		// MongoDBUtil2.instance.dropDB("testdb");

		// 删除表
		// MongoDBUtil2.instance.dropCollection(dbName, collName);

		// 修改数据
		// String id = "556949504711371c60601b5a";
		// Document newdoc = new Document();
		// newdoc.put("name", "时候");
		// MongoDBUtil.instance.updateById(coll, id, newdoc);

		// 统计表
		// System.out.println(MongoDBUtil.instance.getCount(coll));

		// 查询所有
		dbUtil.close();
	}

}