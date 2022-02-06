package com.poscoict.jblog.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.poscoict.jblog.exception.FileUploadException;

@Service
public class FileUploadService {

	private static String SAVE_PATH = "/Users/wonseok/upload-images";
	private static String URL_BASE = "/images";

	public String restore(MultipartFile multipartFile) {
		String url = null;

		try {
			if (multipartFile.isEmpty()) {
				return url;
			}

			// 사용자가 올린 파일 = getOriginalFilename
			String originFileName = multipartFile.getOriginalFilename();
			String extName = originFileName.substring(originFileName.lastIndexOf('.') + 1);
			String saveFileName = generateSaveFilename(extName);
			long fileSize = multipartFile.getSize();

			System.out.println("#########################" + originFileName);
			System.out.println("#########################" + saveFileName);
			System.out.println("#########################" + fileSize);

			byte[] data = multipartFile.getBytes();
			// dir 존재하지않으면 프로그램이 만들어주게끔
			OutputStream os = new FileOutputStream(SAVE_PATH + "/" + saveFileName);
			os.write(data);
			os.close();

			url = URL_BASE + "/" + saveFileName;
		} catch (IOException ex) {
			throw new FileUploadException("file upload error:" + ex);
		}
		return url;
	}

	private String generateSaveFilename(String extName) {
		Calendar calendar = Calendar.getInstance();
		String fileName = "";
		fileName += calendar.get(Calendar.YEAR);
		fileName += calendar.get(Calendar.MONTH);
		fileName += calendar.get(Calendar.DATE);
		fileName += calendar.get(Calendar.HOUR);
		fileName += calendar.get(Calendar.MINUTE);
		fileName += calendar.get(Calendar.SECOND);
		fileName += calendar.get(Calendar.MILLISECOND);
		fileName += ("." + extName);
		return fileName;
	}

}
