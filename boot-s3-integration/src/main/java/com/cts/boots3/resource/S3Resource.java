package com.cts.boots3.resource;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cts.boots3.service.S3Service;

@RestController
@RequestMapping("/file")
public class S3Resource 
{
	@Autowired
	private S3Service service;
	
	@PostMapping("/upload")
	//String
	//ModelAndView
	//void ? is used for POST Methods
	//A RestController API Method/RequestMapping 
	public ResponseEntity<String> uploadFile(@RequestParam(value="file") MultipartFile file) throws FileNotFoundException
	{
		return new ResponseEntity<>(service.uploadFile(file), HttpStatus.OK);
	}
	@GetMapping("/download/{fileName}")
	public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable String fileName)
	{
		byte[] data = service.downloadFile(fileName);
		ByteArrayResource resource = new ByteArrayResource(data);
		return ResponseEntity
					.ok()
					.contentLength(data.length)
					.header("Content-type", "application/octet-stream")
					.header("Content-disposition", "attachment; filename=\"" + fileName +"\"")
					.body(resource);
	 }
	@DeleteMapping("/delete/{fileName}")
	public ResponseEntity<String> deleteFile(@PathVariable(value="fileName") String fileName)
	{
		return new ResponseEntity<String>(service.deleteFile(fileName), HttpStatus.OK);
	}
}
