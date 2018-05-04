package com.axis2.api.wsdlparse.action;

import java.util.List;

import com.axis2.api.wsdlparse.bean.ParameterInfo;
import com.axis2.api.wsdlparse.bean.WebServiceMethod;
import com.axis2.api.wsdlparse.service.BmWsTestBoImpl;
import net.sf.json.JSONArray;

/**
 * @author zhengtian
 * 
 * @date 2011-8-4 下午10:48:35
 */
@SuppressWarnings("all")
public class WstestDwrFacade {
	/**
	 * 根据webservice Url地址查询该url对应的所有的方法
	 * 
	 * @param webserviceUrl
	 * @return
	 * @throws Exception
	 */
	public String getAllMethod(String webserviceUrl) throws Exception {
		List<WebServiceMethod> list = new BmWsTestBoImpl().getAllMethodByServiceUrl(webserviceUrl);
		return JSONArray.fromObject(list).toString();
	}

	/**
	 * 根据方法名称和webserviceUrl得到参数
	 * 
	 * @param methodName
	 * @param webserviceUrl
	 * @return
	 * @throws Exception
	 */
	public String getParamByMethodNameAndWsUrl(String methodName, String webserviceUrl) throws Exception {
		List<ParameterInfo> paramList = new BmWsTestBoImpl().getParamByMethodNameAndWsUrl(webserviceUrl, methodName);
		String result = JSONArray.fromObject(paramList).toString();
		return result;
	}

	/**
	 * 远程调用方法并返回结果
	 * 
	 * @param paramStr
	 * @return
	 * @throws Exception
	 */
	public String executionMethod(String webserviceUrl, String methodName, String paramStr) throws Exception {
		try {
			String message = new BmWsTestBoImpl().executionMethod(webserviceUrl, methodName, paramStr);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}
}
