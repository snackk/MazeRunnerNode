package com.mazerunner.node.mss;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.UpdateTableRequest;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

public class MSSManagerNode {
	
	private static final String METRICS_TABLE_NAME = "MazerunnerMetrics";
	private static MSSManagerNode mssmanager = null;
	private static AmazonDynamoDB dynamoDB;

	private enum paramsType {x0, y0, x1, y1, v, s, m, bbl_count}

	private MSSManagerNode() {
		initDB();
	}
	
	public static MSSManagerNode getInstance() {
      		if(mssmanager == null) {
	        	mssmanager = new MSSManagerNode();
      		}
      		return mssmanager;
	}
	
		
	private void initDB() {
		ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
		try {
		    credentialsProvider.getCredentials();
		} catch (Exception e) {
		    throw new AmazonClientException(
			    "Cannot load the credentials from the credential profiles file. " +
			    "Please make sure that your credentials file is at the correct " +
			    "location (~/.aws/credentials), and is in valid format.",
			    e);
		}
		dynamoDB = AmazonDynamoDBClientBuilder.standard()
		    .withCredentials(credentialsProvider)
		    .withRegion("us-east-1a")
		    .build();
	}
	
	public void putMetrics(long threadId) {

	        Map<String, AttributeValue> item = parseMetricsFile(threadId);
			
	        try {
	            System.out.println("Adding a new item...");
			
	            PutItemRequest putItemRequest = new PutItemRequest(METRICS_TABLE_NAME, item);
	            PutItemResult outcome = dynamoDB.putItem(putItemRequest);

	            System.out.println("PutItem succeeded:" + outcome.toString());

	        }
	        catch (Exception e) {
	            System.err.println("Unable to add item: " + item);
	            System.err.println(e.getMessage());
	        }
	}

	private Map<String, AttributeValue> parseMetricsFile(long threadId) {
		
		Map<String, AttributeValue> map = new HashMap<String, AttributeValue>();
		
		try{
			String filename = "metrics-" + threadId + ".txt";
			String metricFilesPath = "/home/ec2-user/metricfiles/";
			Path path =Paths.get(metricFilesPath, filename);
			String line = Files.readAllLines(path,StandardCharsets.UTF_8 ).get(0);


			String[] parameters = line.split("&");
			for (String p : parameters) {
				String[] split = p.split("=");
				map.put(split[0], new AttributeValue(split[1]));
			}
			String date = new SimpleDateFormat("yyyMMddHHmmssSS").format(new Date());
			map.put("id", new AttributeValue(date.toString()));
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		return map;
	}
}
