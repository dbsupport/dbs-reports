/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import pl.com.dbs.reports.api.report.ReportParameter;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.report.domain.builders.inflaters.ReportTextBlockInflater;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Report blocks builder for text formats converted into pdf.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2013
 */
@Slf4j
public final class ReportTextPdfBlocksBuilder extends ReportTextBlocksBuilder {
	
	public ReportTextPdfBlocksBuilder(final PatternTransformate transformate, ReportTextBlockInflater inflater, final List<ReportParameter> params) {
		super(transformate, inflater, params);
	}
	
	@Override
	public ReportTextBlocksBuilder build() throws ReportBlockException {
		super.build();
		convert();
		return this;
	}

	/**
	 * http://stackoverflow.com/questions/20545458/conversion-of-pdf-file-to-word-document-using-itext-in-java
	 * http://thejavayard.blogspot.com/2007/02/convert-text-file-into-pdf-format-in.html
	 */
	protected void convert() {
		try {
			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, os);
			document.open();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content)));
			
			while (br.readLine()!=null) {
				document.add(new Paragraph(br.readLine()));
			}
			document.close();
			log.debug("Pdf document closed");
			content = os.toByteArray();
		} catch (Exception e) {
			log.error("Error converting into pdf." + e.getMessage());
		}
	}
}
