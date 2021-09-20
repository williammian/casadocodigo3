package br.com.casadocodigo3.loja.infra.prod;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
public class FileSaver {

	@Autowired
	private AmazonS3 amazonS3;
	
	@Autowired
	private HttpServletRequest request;
	

	@Profile("prod")
	public String writeProd(MultipartFile file) {
		try {
			String bucket=System.getProperty("BUCKET");
			amazonS3.putObject(new PutObjectRequest(bucket, 
					file.getOriginalFilename(), file.getInputStream(),null)
					.withCannedAcl(CannedAccessControlList.PublicRead));
				
			return "http://s3.amazonaws.com/"+bucket+"/"+file.getOriginalFilename();
			
		} catch (IllegalStateException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Profile("dev")
	public String writeDev(MultipartFile file) {
		try {
			File arquivo = new File(request.getServletContext().getRealPath(
					"/resources/imagens"), file.getOriginalFilename());
			file.transferTo(arquivo);
			
			return file.getOriginalFilename();

		} catch (IllegalStateException | IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}









