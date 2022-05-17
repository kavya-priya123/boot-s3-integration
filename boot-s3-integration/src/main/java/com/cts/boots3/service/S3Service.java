package com.cts.boots3.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

@Service
public class S3Service {
	@Value("${application.bucket.name}")
	private String bucketName;
	@Autowired
	private AmazonS3 s3Client;  //AmazonS3-it like repository,to put file to s3bucket
    
	
	  public String uploadFile(MultipartFile file)throws FileNotFoundException {
	  
	  System.out.println(file.getSize()+">>>>>>>>>>>>");
	  File fileObj=convertMultipartFileToFile(file); //user defined method
	  String fileName=System.currentTimeMillis()+"_"+file.getOriginalFilename();
	  ObjectMetadata metadata=new ObjectMetadata();
	  metadata.setContentLength(fileObj.length());
	  FileInputStream fis=new FileInputStream(fileObj);
	  s3Client.putObject(new PutObjectRequest(bucketName,fileName,fis,metadata));
	  fileObj.delete(); //deleting from local cache memory
	  return "File uploaded: "+fileName;
	  }
	  
	  public byte[] downloadFile(String fileName)
	  {
		  S3Object s3Object = s3Client.getObject(bucketName,fileName);
		  S3ObjectInputStream inputStream = s3Object.getObjectContent();
		  try
		  {
			  byte[] content = IOUtils.toByteArray(inputStream);
			  return content;
		  }
		  catch(IOException e)
		  {
			  e.printStackTrace();
		  }
		  return null;
	  }
	 public String deleteFile(String fileName)
	 {
		 s3Client.deleteObject(bucketName, fileName);
		 return fileName + "removed....";
	 }
			
private File convertMultipartFileToFile(MultipartFile file)
{
	File convertedFile=new File(file.getOriginalFilename());
		try(FileOutputStream fos=new FileOutputStream(convertedFile))
		{
			fos.write(file.getBytes());
		}
		catch(IOException e) 
		{
			//Log.error("error converting multipart to file",e);
		e.printStackTrace();
		}
		return convertedFile;
}
}
