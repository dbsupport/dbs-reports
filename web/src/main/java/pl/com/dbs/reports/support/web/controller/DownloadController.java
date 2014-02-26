/**
 * 
 */
package pl.com.dbs.reports.support.web.controller;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Download common controller.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @coptyright (c) 2014
 */
public class DownloadController {

    public static String download(byte[] content, String filename, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		//response.setContentType("application/octet-stream");
		//response.setContentType("application/"+report.getFormat().getDefaultExt());
		//response.setContentType("application/force-download;charset=ISO-8859-1");
		response.setContentType("application/force-download;charset=UTF-8");
		response.setContentLength(content.length);
		//response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Disposition","attachment;filename="+filename);
	 
		ServletOutputStream out = null;
		try {
			out = response.getOutputStream();
			out.write(content);
			out.flush();
			out.close();
		} finally {
			out.close();
		}
		
		return null;
    }
}
