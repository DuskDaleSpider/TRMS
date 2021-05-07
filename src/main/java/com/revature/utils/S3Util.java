package com.revature.utils;

import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import software.amazon.awssdk.core.SdkField;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

public class S3Util {
	public static final Region region = Region.US_EAST_2;
	public static final String BUCKET_NAME = "com.revature.trms";
	
	private static Logger log = LogManager.getLogger(S3Util.class);	
	private static S3Util instance = null;
	private S3Client s3 = null;
	
	private S3Util() {
		s3 = S3Client.builder().region(region).build();
	}
	
	public static synchronized S3Util getInstance() {
		if(instance == null) {
			instance = new S3Util();
		}
		return instance;
	}
	
	public void uploadToBucket(String key, RequestBody body) {
		log.trace("Uploading file as "+key);
		
		s3.putObject(PutObjectRequest.builder()
				.bucket(BUCKET_NAME).key(key)
				.acl(ObjectCannedACL.PUBLIC_READ)
                .build(),
                body);
		
		log.trace("Upload Complete");
	}
	
	public InputStream getObject(String key) {
		log.trace("Retrieving Data from S3: " + key);
		InputStream s = s3.getObject(GetObjectRequest.builder().bucket(BUCKET_NAME).key(key).build());
		return s;
	}
	
	public String getObjectUrl(String key) {
		log.trace("Generating object url");
		return "https://s3.us-east-2.amazonaws.com/"+BUCKET_NAME+"/"+key;
	}
	
	public void deleteObject(String key) {
		log.trace("Delete data from s3: " + key);
		
		s3.deleteObject(DeleteObjectRequest.builder().bucket(BUCKET_NAME).key(key).build());

	}
}