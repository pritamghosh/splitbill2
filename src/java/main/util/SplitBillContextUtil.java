package util;

import java.util.HashMap;
import java.util.Map;

public class SplitBillContextUtil {
private static final Map<String,Object> applicationContext = new HashMap<String,Object>();

public static synchronized Map<String, Object> getApplicationcontext() {
	return applicationContext;
}
}
