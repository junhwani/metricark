package com.nexcloud.api.cluster.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nexcloud.api.client.PrometheusClient;
import com.nexcloud.api.domain.ResponseData;
import com.nexcloud.util.Const;
import com.nexcloud.util.Util;

@Service
public class ClusterDiskService {
	static final Logger logger = LoggerFactory.getLogger(ClusterDiskService.class);
	
	@Autowired private PrometheusClient prometheusClient;

	/**
	 * Cluster Total Disk size
	 * @param model
	 * @throws Exception
	 */
	public ResponseEntity<ResponseData> getClusterDiskResource( String cluster_id, String start, String end )  throws Exception
	{
		String query								= null;
		ResponseEntity<ResponseData> response 		= null;
		ResponseData resData						= new ResponseData();
		ResponseEntity<String> entityData			= null;
		String param								= null;
		String sub_query							= "";
		 
		try{
			sub_query								= Util.makeSubQuery(start, end);
			param									= "{device=~'^/dev/[sv]d[a-z][1-9]',id='/'}";
			query									= "sum(container_fs_limit_bytes{param})";
			
			if( sub_query != null )
				entityData							= prometheusClient.getQueryRange(query+"&"+sub_query, param );
			else
				entityData							= prometheusClient.getQuery(query, param );
			
			JSONParser parser						= new JSONParser();
			//JSON데이터를 넣어 JSON Object 로 만들어 준다.
            JSONObject jsonObject 					= (JSONObject) parser.parse(entityData.getBody());
            resData.setData((JSONObject)jsonObject.get("data"));
            resData.setStatus((String)jsonObject.get("status"));
            
			//resData									= Util.JsonTobean(entityData.getBody(), ResponseData.class);
			resData.setResponse_code(entityData.getStatusCodeValue());
			resData.setMessage(Const.SUCCESS);

			response = new ResponseEntity<ResponseData>(resData, HttpStatus.OK);
		}catch(Exception e){
			e.printStackTrace();
			resData.setResponse_code(Const.INTERNAL_SERVER_ERROR);
			resData.setMessage(Const.FAIL);
			resData.setMessage(Util.makeStackTrace(e));
			response = new ResponseEntity<ResponseData>(resData, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
	
	
	/**
	 * Cluster Total Disk Usage (%)
	 * @param model
	 * @throws Exception
	 */
	public ResponseEntity<ResponseData> getClusterDiskUsageResource( String cluster_id, String start, String end )  throws Exception
	{
		String query								= null;
		ResponseEntity<ResponseData> response 		= null;
		ResponseData resData						= new ResponseData();
		ResponseEntity<String> entityData			= null;
		String param								= null;
		String sub_query							= "";
		 
		try{
			sub_query								= Util.makeSubQuery(start, end);
			param									= "{device=~'^/dev/[sv]d[a-z][1-9]',id='/'}";
			query									= "sum (container_fs_usage_bytes{param}) / sum (container_fs_limit_bytes{param}) * 100";
			
			if( sub_query != null )
				entityData							= prometheusClient.getQueryRange(query+"&"+sub_query, param, param );
			else
				entityData							= prometheusClient.getQuery(query, param, param );
			
			JSONParser parser						= new JSONParser();
			//JSON데이터를 넣어 JSON Object 로 만들어 준다.
            JSONObject jsonObject 					= (JSONObject) parser.parse(entityData.getBody());
            resData.setData((JSONObject)jsonObject.get("data"));
            resData.setStatus((String)jsonObject.get("status"));
            
			resData.setResponse_code(entityData.getStatusCodeValue());
			resData.setMessage(Const.SUCCESS);

			response = new ResponseEntity<ResponseData>(resData, HttpStatus.OK);
		}catch(Exception e){
			e.printStackTrace();
			resData.setResponse_code(Const.INTERNAL_SERVER_ERROR);
			resData.setMessage(Const.FAIL);
			resData.setMessage(Util.makeStackTrace(e));
			response = new ResponseEntity<ResponseData>(resData, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}

	/**
	 * Cluster Total Disk Used byte
	 * @param model
	 * @throws Exception
	 */
	public ResponseEntity<ResponseData> getClusterDiskUsedbyteResource( String cluster_id, String start, String end  )  throws Exception
	{
		String query								= null;
		ResponseEntity<ResponseData> response 		= null;
		ResponseData resData						= new ResponseData();
		String param								= null;
		String sub_query							= "";
		try{
			sub_query								= Util.makeSubQuery(start, end);
			param									= "{device=~'^/dev/[sv]d[a-z][1-9]',id='/'}";
			query									= "sum (container_fs_usage_bytes{param})";
			ResponseEntity<String> entityData		= null;
			
			if( sub_query != null )
				entityData							= prometheusClient.getQueryRange(query+"&"+sub_query, param );
			else
				entityData							= prometheusClient.getQuery(query, param );

			JSONParser parser						= new JSONParser();
			//JSON데이터를 넣어 JSON Object 로 만들어 준다.
            JSONObject jsonObject 					= (JSONObject) parser.parse(entityData.getBody());
            resData.setData((JSONObject)jsonObject.get("data"));
            resData.setStatus((String)jsonObject.get("status"));
            
			resData.setResponse_code(entityData.getStatusCodeValue());
			resData.setMessage(Const.SUCCESS);

			response = new ResponseEntity<ResponseData>(resData, HttpStatus.OK);
		}catch(Exception e){
			resData.setResponse_code(Const.INTERNAL_SERVER_ERROR);
			resData.setMessage(Const.FAIL);
			response = new ResponseEntity<ResponseData>(resData, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return response;
	}
}
