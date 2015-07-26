package com.microstep.android.onclick.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microstep.android.onclick.MainActivity;
import com.microstep.android.onclick.model.Category;
import com.microstep.android.onclick.model.DeviceClass;
import com.microstep.android.onclick.model.Organization;
import com.microstep.android.onclick.model.OrganizationProductType;
import com.microstep.android.onclick.model.Product;
import com.microstep.android.onclick.model.Shop;
import com.microstep.android.onclick.rest.model.DataXml;
import com.microstep.android.onclick.rest.model.IdentityXml;
import com.microstep.android.onclick.rest.model.Login;
import com.microstep.android.onclick.rest.model.NoteXml;
import com.microstep.android.onclick.rest.model.OnClickXml;
import com.microstep.android.onclick.rest.model.ProductXml;
import com.microstep.android.onclick.rest.model.StatusXml;
import com.microstep.android.onclick.security.SecurityContextHolder;

public class NoteServiceImpl implements NoteService {

	private XmlBeanFactory xmlBeanFactory = new XmlBeanFactory();
	
	@Override
	public Login registerDevice(String deviceId) {		
		
		HttpClient httpclient = new DefaultHttpClient();          		

        try { 
        	List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    		qparams.add(new BasicNameValuePair("deviceId", deviceId));
    		DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
    		qparams.add(new BasicNameValuePair("dt", dc.getType()));
    		qparams.add(new BasicNameValuePair("did", dc.getId()));		
    		qparams.add(new BasicNameValuePair("ln", dc.getLanguage()));
    		if(dc.getVersion() != null){
    			qparams.add(new BasicNameValuePair("av", dc.getVersion()));
    		}
    		HttpGet request = new HttpGet(getUri("security/json/registerDevice", qparams));        	
        	ResponseHandler<String> handler = new BasicResponseHandler();
        	String result = httpclient.execute(request, handler);        
        	System.out.println(result);
            Gson gson = new GsonBuilder().create();
            OnClickXml onclick = gson.fromJson(result, OnClickXml.class);    	        	    
    	    return onclick.getDataXml().getLogin().get(0);    	    
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (URISyntaxException e) {
			e.printStackTrace();
		}finally{
        	httpclient.getConnectionManager().shutdown(); 
        }
	    
        Login login = new Login();
        login.setUserId("-1");
        return login;
	}

	@Override
	public Login loginDevice(String deviceId) {
		HttpClient httpclient = new DefaultHttpClient();          		

        try { 
        	List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    		qparams.add(new BasicNameValuePair("deviceId", deviceId));
    		DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
    		qparams.add(new BasicNameValuePair("dt", dc.getType()));
    		qparams.add(new BasicNameValuePair("did", dc.getId()));	
    		qparams.add(new BasicNameValuePair("ln", dc.getLanguage()));
    		if(dc.getVersion() != null){
    			qparams.add(new BasicNameValuePair("av", dc.getVersion()));
    		}
    		HttpGet request = new HttpGet(getUri("security/json/loginDevice", qparams));        	
        	ResponseHandler<String> handler = new BasicResponseHandler();
        	String result = httpclient.execute(request, handler);        
        	System.out.println(result);
            Gson gson = new GsonBuilder().create();
            OnClickXml onclick = gson.fromJson(result, OnClickXml.class);    	        	    
    	    return onclick.getDataXml().getLogin().get(0);    	    
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (URISyntaxException e) {
			e.printStackTrace();
		}finally{
        	httpclient.getConnectionManager().shutdown(); 
        }
	    
        Login login = new Login();
        login.setUserId("-1");
        return login;
	}

	@Override
	public StatusXml addNote(String userId, Shop shop, String authToken, String language) {
		
		HttpClient httpclient = new DefaultHttpClient();          		

        try { 
        	List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    		qparams.add(new BasicNameValuePair("id", userId));
    		qparams.add(new BasicNameValuePair("authToken", authToken));
    		qparams.add(new BasicNameValuePair("ln", language));
    		DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
    		qparams.add(new BasicNameValuePair("dt", dc.getType()));
    		qparams.add(new BasicNameValuePair("did", dc.getId()));		
    		HttpPost request = new HttpPost(getUri("note/json/add", qparams));
    		
    		NoteXml noteXml = xmlBeanFactory.getNoteXmlFromShop(userId, shop);
    		Gson gson = new GsonBuilder().create();
    		String json = new String(gson.toJson(noteXml).getBytes("UTF-8"));    		
            System.out.println(json);
    		StringEntity se = new StringEntity(json, HTTP.UTF_8);  
            se.setContentType("application/json;charset=UTF-8");
            request.setHeader("Content-Type","application/json;charset=UTF-8");
            request.setEntity(se);
            
        	ResponseHandler<String> handler = new BasicResponseHandler();
        	String result = httpclient.execute(request, handler);              
            System.out.println(result);
            
            OnClickXml onclick = gson.fromJson(result, OnClickXml.class);    	        	    
    	    return onclick.getStatusXml();
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (URISyntaxException e) {
			e.printStackTrace();
		}finally{
        	httpclient.getConnectionManager().shutdown(); 
        }

		StatusXml statusXml = new StatusXml();
		statusXml.setCode(100);
		statusXml.setMessage("Problem with web service response");
        return statusXml;
	}
	
	public void setXmlBeanFactory(XmlBeanFactory xmlBeanFactory) {
		this.xmlBeanFactory = xmlBeanFactory;
	}
	
	public XmlBeanFactory getXmlBeanFactory() {
		return xmlBeanFactory;
	}
	
	private URI getUri(String path, List<NameValuePair> qparams) throws URISyntaxException{
		URI uri = URIUtils.createURI("http", MainActivity.SERVER_HOST_REST_API, MainActivity.SERVER_PORT_REST_API, 
				MainActivity.SERVER_PATH_REST_API + path, URLEncodedUtils.format(qparams, "UTF-8"), null);
		System.out.println(uri.toString());
		return uri;
	}

	@Override
	public OnClickXml loginDeviceUpdateCategory(String deviceId,
			Date lastUpdate, String language) {
		HttpClient httpclient = new DefaultHttpClient();          		

        try { 
        	List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    		qparams.add(new BasicNameValuePair("deviceId", deviceId));
    		DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
    		qparams.add(new BasicNameValuePair("dt", dc.getType()));
    		qparams.add(new BasicNameValuePair("did", dc.getId()));
    		qparams.add(new BasicNameValuePair("ln", language));
    		if(dc.getVersion() != null){
    			qparams.add(new BasicNameValuePair("av", dc.getVersion()));
    		}
    		if(lastUpdate != null){
    			qparams.add(new BasicNameValuePair("lastUpdate", lastUpdate.toGMTString()));
    		}
    		HttpGet request = new HttpGet(getUri("security/json/loginDeviceUpdateCategory", qparams));        	
        	ResponseHandler<String> handler = new BasicResponseHandler();
        	String result = httpclient.execute(request, handler);        
        	System.out.println(result);
            Gson gson = new GsonBuilder().create();
            OnClickXml onclick = gson.fromJson(result, OnClickXml.class);    	        	    
    	    return onclick;
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (URISyntaxException e) {
			e.printStackTrace();
		}finally{
        	httpclient.getConnectionManager().shutdown(); 
        }
	    
        OnClickXml onclickXml = new OnClickXml();
        DataXml dataXml = new DataXml();
        Login login = new Login();
        login.setUserId("-1");
        dataXml.getLogin().add(login);
        StatusXml statusXml = new StatusXml();
        statusXml.setCode(100);
        onclickXml.setStatusXml(statusXml);
        onclickXml.setDataXml(dataXml);
        return onclickXml;
	}

	@Override
	public StatusXml addDeviceToAccount(String deviceId, String email, String language, String authToken) {
		HttpClient httpclient = new DefaultHttpClient();          		

        try { 
        	List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    		qparams.add(new BasicNameValuePair("deviceId", deviceId));
    		qparams.add(new BasicNameValuePair("email", email));
    		DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
    		qparams.add(new BasicNameValuePair("dt", dc.getType()));
    		qparams.add(new BasicNameValuePair("did", dc.getId()));
    		qparams.add(new BasicNameValuePair("ln", language));
    		qparams.add(new BasicNameValuePair("authToken", authToken));
    		if(dc.getVersion() != null){
    			qparams.add(new BasicNameValuePair("av", dc.getVersion()));
    		}
    		HttpGet request = new HttpGet(getUri("identity/json/addDeviceToAccount", qparams));        	
        	ResponseHandler<String> handler = new BasicResponseHandler();
        	String result = httpclient.execute(request, handler);        
        	System.out.println(result);
            Gson gson = new GsonBuilder().create();
            StatusXml statusXml = gson.fromJson(result, StatusXml.class);    	        	    
    	    return statusXml;
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (URISyntaxException e) {
			e.printStackTrace();
		}finally{
        	httpclient.getConnectionManager().shutdown(); 
        }
	    
        StatusXml statusXml = new StatusXml();
        statusXml.setCode(100);
        statusXml.setMessage("Error");
        return statusXml;
	}

	@Override
	public IdentityXml getAccount(String accountId, String authToken) {
		HttpClient httpclient = new DefaultHttpClient();          		

        try { 
        	List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    		qparams.add(new BasicNameValuePair("id", accountId));
    		qparams.add(new BasicNameValuePair("authToken", authToken));    		
    		HttpGet request = new HttpGet(getUri("identity/json/get", qparams));        	
        	ResponseHandler<String> handler = new BasicResponseHandler();
        	String result = httpclient.execute(request, handler);        
        	System.out.println(result);
            Gson gson = new GsonBuilder().create();
            OnClickXml onClickXml = gson.fromJson(result, OnClickXml.class);    	 
            DataXml dataXml = onClickXml.getDataXml();
    	    return dataXml.getIdentityXml().get(0);
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (URISyntaxException e) {
			e.printStackTrace();
		}finally{
        	httpclient.getConnectionManager().shutdown(); 
        }

        return null;
	}

	@Override
	public OnClickXml registerAccount(String email, String password, String currency) {
		HttpClient httpclient = new DefaultHttpClient();          		

        try { 
        	List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    		qparams.add(new BasicNameValuePair("identifier", email));
    		qparams.add(new BasicNameValuePair("password", password));
    		DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
    		qparams.add(new BasicNameValuePair("language", dc.getLanguage()));
    		qparams.add(new BasicNameValuePair("currency", currency));
    		HttpGet request = new HttpGet(getUri("identity/json/register", qparams));        	
        	ResponseHandler<String> handler = new BasicResponseHandler();
        	String result = httpclient.execute(request, handler);        
        	System.out.println(result);
            Gson gson = new GsonBuilder().create();
            OnClickXml onClickXml = gson.fromJson(result, OnClickXml.class);    	 
            return onClickXml;
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (URISyntaxException e) {
			e.printStackTrace();
		}finally{
        	httpclient.getConnectionManager().shutdown(); 
        }

        return null;
	}

	@Override
	public StatusXml removeDeviceFromAccount(String deviceId, String accountId,
			String authToken) {
		HttpClient httpclient = new DefaultHttpClient();          		

        try { 
        	List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    		qparams.add(new BasicNameValuePair("deviceId", deviceId));
    		qparams.add(new BasicNameValuePair("accountId", accountId));
    		qparams.add(new BasicNameValuePair("authToken", authToken));

    		HttpGet request = new HttpGet(getUri("identity/json/removeDeviceFromAccount", qparams));        	
        	ResponseHandler<String> handler = new BasicResponseHandler();
        	String result = httpclient.execute(request, handler);        
        	System.out.println(result);
            Gson gson = new GsonBuilder().create();
            StatusXml statusXml = gson.fromJson(result, StatusXml.class);    	        	    
    	    return statusXml;
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (URISyntaxException e) {
			e.printStackTrace();
		}finally{
        	httpclient.getConnectionManager().shutdown(); 
        }
	    
        StatusXml statusXml = new StatusXml();
        statusXml.setCode(100);
        statusXml.setMessage("Error");
        return statusXml;
	}

	@Override
	public List<ProductXml> getProductName(String name, String categoryId) {
		HttpClient httpclient = new DefaultHttpClient();          		

        try { 
        	List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    		
    		DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
    		Login login = SecurityContextHolder.getSecurityContext().getLogin();
    		
    		if(!"-1".equals(login.getUserId())){
	        	qparams.add(new BasicNameValuePair("id", login.getUserId()));
	    		qparams.add(new BasicNameValuePair("authToken", login.getAuthToken()));
	    		qparams.add(new BasicNameValuePair("ln", dc.getLanguage()));
	    		qparams.add(new BasicNameValuePair("dt", dc.getType()));
	    		qparams.add(new BasicNameValuePair("did", dc.getId()));		
	    		qparams.add(new BasicNameValuePair("name", name));
	    		
	    		HttpGet request = new HttpGet(getUri("product/json/search", qparams));        	
	        	ResponseHandler<String> handler = new BasicResponseHandler();
	        	String result = httpclient.execute(request, handler);        
	        	System.out.println(result);
	            Gson gson = new GsonBuilder().create();
	            OnClickXml onClickXml = gson.fromJson(result, OnClickXml.class);    	        	    
	    	    return onClickXml.getDataXml().getProductXml();
    		}
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (URISyntaxException e) {
			e.printStackTrace();
		}finally{
        	httpclient.getConnectionManager().shutdown(); 
        }

        return new ArrayList<ProductXml>();
        
	}

	@Override
	public List<Organization> getOrganizationsNearMe(Double latitude,
			Double longitude) {
		HttpClient httpclient = new DefaultHttpClient();          		

        try { 
        	List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    		
    		DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
    		Login login = SecurityContextHolder.getSecurityContext().getLogin();
    		
    		if(!"-1".equals(login.getUserId())){
	        	qparams.add(new BasicNameValuePair("id", login.getUserId()));
	    		qparams.add(new BasicNameValuePair("authToken", login.getAuthToken()));
	    		qparams.add(new BasicNameValuePair("ln", dc.getLanguage()));
	    		qparams.add(new BasicNameValuePair("dt", dc.getType()));
	    		qparams.add(new BasicNameValuePair("did", dc.getId()));		
	    		qparams.add(new BasicNameValuePair("lat", latitude.toString()));
	    		qparams.add(new BasicNameValuePair("lng", longitude.toString()));
	    		
	    		HttpGet request = new HttpGet(getUri("organization/json/getOrganizationsNearMe", qparams));        	
	        	ResponseHandler<String> handler = new BasicResponseHandler();
	        	String result = httpclient.execute(request, handler);        
	        	System.out.println(result);
	            Gson gson = new GsonBuilder().create();
	            OnClickXml onClickXml = gson.fromJson(result, OnClickXml.class);    	        	    
	    	    return xmlBeanFactory.getOrganizationListFromOrganizationXmlList(
	    	    		onClickXml.getDataXml().getOrganizationXml());
    		}
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (URISyntaxException e) {
			e.printStackTrace();
		}finally{
        	httpclient.getConnectionManager().shutdown(); 
        }

        return new ArrayList<Organization>();
	}

	@Override
	public List<Category> getOrganizationCategoryById(Long oid, Long cid) {
		HttpClient httpclient = new DefaultHttpClient();          		

        try { 
        	List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    		
    		DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
    		Login login = SecurityContextHolder.getSecurityContext().getLogin();
    		
    		if(!"-1".equals(login.getUserId())){
	        	qparams.add(new BasicNameValuePair("id", login.getUserId()));
	    		qparams.add(new BasicNameValuePair("authToken", login.getAuthToken()));
	    		qparams.add(new BasicNameValuePair("ln", dc.getLanguage()));
	    		qparams.add(new BasicNameValuePair("dt", dc.getType()));
	    		qparams.add(new BasicNameValuePair("did", dc.getId()));		
	    		qparams.add(new BasicNameValuePair("oid", oid.toString()));
	    		if(cid != null){
	    			qparams.add(new BasicNameValuePair("cid", cid.toString()));
	    		}
	    		
	    		HttpGet request = new HttpGet(getUri("organization/json/getOrganizationCategoryById", qparams));        	
	        	ResponseHandler<String> handler = new BasicResponseHandler();
	        	String result = httpclient.execute(request, handler);        
	        	System.out.println(result);
	            Gson gson = new GsonBuilder().create();
	            OnClickXml onClickXml = gson.fromJson(result, OnClickXml.class);    	        	    
	    	    return xmlBeanFactory.getCategoryListFromCategoryXmlList(
	    	    		onClickXml.getDataXml().getCategoryXml());
    		}
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (URISyntaxException e) {
			e.printStackTrace();
		}finally{
        	httpclient.getConnectionManager().shutdown(); 
        }

        return new ArrayList<Category>();
	}

	@Override
	public List<Category> getOrganizationCategories(Long oid, String type) {
		HttpClient httpclient = new DefaultHttpClient();          		

        try { 
        	List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    		
    		DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
    		Login login = SecurityContextHolder.getSecurityContext().getLogin();
    		
    		if(!"-1".equals(login.getUserId())){
	        	qparams.add(new BasicNameValuePair("id", login.getUserId()));
	    		qparams.add(new BasicNameValuePair("authToken", login.getAuthToken()));
	    		qparams.add(new BasicNameValuePair("ln", dc.getLanguage()));
	    		qparams.add(new BasicNameValuePair("dt", dc.getType()));
	    		qparams.add(new BasicNameValuePair("did", dc.getId()));		
	    		qparams.add(new BasicNameValuePair("oid", oid.toString()));
	    		qparams.add(new BasicNameValuePair("type", type));
	    		
	    		HttpGet request = new HttpGet(getUri("organization/json/getOrganizationCategories", qparams));        	
	        	ResponseHandler<String> handler = new BasicResponseHandler();
	        	String result = httpclient.execute(request, handler);        
	        	System.out.println(result);
	            Gson gson = new GsonBuilder().create();
	            OnClickXml onClickXml = gson.fromJson(result, OnClickXml.class);    	        	    
	    	    return xmlBeanFactory.getCategoryListFromCategoryXmlList(
	    	    		onClickXml.getDataXml().getCategoryXml());
    		}
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (URISyntaxException e) {
			e.printStackTrace();
		}finally{
        	httpclient.getConnectionManager().shutdown(); 
        }

        return new ArrayList<Category>();
	}

	@Override
	public List<Product> getOrganizationProductsByCategoryId(Long oid, Long cid, OrganizationProductType pType) {
		HttpClient httpclient = new DefaultHttpClient();          		

        try { 
        	List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    		
    		DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
    		Login login = SecurityContextHolder.getSecurityContext().getLogin();
    		
    		if(!"-1".equals(login.getUserId())){
	        	qparams.add(new BasicNameValuePair("id", login.getUserId()));
	    		qparams.add(new BasicNameValuePair("authToken", login.getAuthToken()));
	    		qparams.add(new BasicNameValuePair("ln", dc.getLanguage()));
	    		qparams.add(new BasicNameValuePair("dt", dc.getType()));
	    		qparams.add(new BasicNameValuePair("did", dc.getId()));		
	    		qparams.add(new BasicNameValuePair("oid", oid.toString()));
	    		qparams.add(new BasicNameValuePair("cid", cid.toString()));
	    		if(pType != null){
	    			qparams.add(new BasicNameValuePair("ptype", pType.toString()));
	    		}	    		
	    		
	    		HttpGet request = new HttpGet(getUri("organization/json/getOrganizationProductsByCategoryId", qparams));        	
	        	ResponseHandler<String> handler = new BasicResponseHandler();
	        	String result = httpclient.execute(request, handler);        
	        	System.out.println(result);
	            Gson gson = new GsonBuilder().create();
	            OnClickXml onClickXml = gson.fromJson(result, OnClickXml.class);    	        	    
	    	    return xmlBeanFactory.getProductListFromProductXmlList(
	    	    		onClickXml.getDataXml().getProductXml());
    		}
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (URISyntaxException e) {
			e.printStackTrace();
		}finally{
        	httpclient.getConnectionManager().shutdown(); 
        }

        return new ArrayList<Product>();
	}

	@Override
	public List<Product> getOrganizationProductsByName(Long oid, Long cid,
			String name, OrganizationProductType pType) {
		HttpClient httpclient = new DefaultHttpClient();          		

        try { 
        	List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    		
    		DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
    		Login login = SecurityContextHolder.getSecurityContext().getLogin();
    		
    		if(!"-1".equals(login.getUserId())){
	        	qparams.add(new BasicNameValuePair("id", login.getUserId()));
	    		qparams.add(new BasicNameValuePair("authToken", login.getAuthToken()));
	    		qparams.add(new BasicNameValuePair("ln", dc.getLanguage()));
	    		qparams.add(new BasicNameValuePair("dt", dc.getType()));
	    		qparams.add(new BasicNameValuePair("did", dc.getId()));		
	    		qparams.add(new BasicNameValuePair("oid", oid.toString()));
	    		if(cid != null){
	    			qparams.add(new BasicNameValuePair("cid", cid.toString()));
	    		}
	    		if(pType != null){
	    			qparams.add(new BasicNameValuePair("ptype", pType.toString()));
	    		}
	    		qparams.add(new BasicNameValuePair("name", name));

	    		HttpGet request = new HttpGet(getUri("organization/json/searchOrganizationProducts", qparams));        	
	        	ResponseHandler<String> handler = new BasicResponseHandler();
	        	String result = httpclient.execute(request, handler);        
	        	System.out.println(result);
	            Gson gson = new GsonBuilder().create();
	            OnClickXml onClickXml = gson.fromJson(result, OnClickXml.class);    	        	    
	    	    return xmlBeanFactory.getProductListFromProductXmlList(
	    	    		onClickXml.getDataXml().getProductXml());
    		}
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (URISyntaxException e) {
			e.printStackTrace();
		}finally{
        	httpclient.getConnectionManager().shutdown(); 
        }

        return new ArrayList<Product>();
	}

	@Override
	public List<Product> getOrganizationProductsByIdentifier(Long oid,
			String identifier) {
		HttpClient httpclient = new DefaultHttpClient();          		

        try { 
        	List<NameValuePair> qparams = new ArrayList<NameValuePair>();
    		
    		DeviceClass dc = SecurityContextHolder.getSecurityContext().getDeviceClass();
    		Login login = SecurityContextHolder.getSecurityContext().getLogin();
    		
    		if(!"-1".equals(login.getUserId())){
	        	qparams.add(new BasicNameValuePair("id", login.getUserId()));
	    		qparams.add(new BasicNameValuePair("authToken", login.getAuthToken()));
	    		qparams.add(new BasicNameValuePair("ln", dc.getLanguage()));
	    		qparams.add(new BasicNameValuePair("dt", dc.getType()));
	    		qparams.add(new BasicNameValuePair("did", dc.getId()));		
	    		qparams.add(new BasicNameValuePair("oid", oid.toString()));
	    		qparams.add(new BasicNameValuePair("identifier", identifier));

	    		HttpGet request = new HttpGet(getUri("organization/json/getOrganizationProductsByIdentifier", qparams));        	
	        	ResponseHandler<String> handler = new BasicResponseHandler();
	        	String result = httpclient.execute(request, handler);        
	        	System.out.println(result);
	            Gson gson = new GsonBuilder().create();
	            OnClickXml onClickXml = gson.fromJson(result, OnClickXml.class);    	        	    
	    	    return xmlBeanFactory.getProductListFromProductXmlList(
	    	    		onClickXml.getDataXml().getProductXml());
    		}
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (URISyntaxException e) {
			e.printStackTrace();
		}finally{
        	httpclient.getConnectionManager().shutdown(); 
        }

        return new ArrayList<Product>();
	}

}
