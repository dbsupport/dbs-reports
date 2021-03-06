/**
 * 
 */
package pl.com.dbs.reports.report.domain.builders;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import pl.com.dbs.reports.api.report.ReportParameter;
import pl.com.dbs.reports.api.report.pattern.PatternTransformate;
import pl.com.dbs.reports.report.domain.builders.inflaters.ReportTextBlockInflater;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.parser.RtfParser;

/**
 * Report blocks builder for rtf formats converted into pdf.
 *
 * @author Krzysztof Kaziura | krzysztof.kaziura@gmail.com | http://www.lazydevelopers.pl
 * @copyright (c) 2014
 */
@Slf4j
public final class ReportRtfPdfBlocksBuilder extends ReportTextBlocksBuilder {
	
	public ReportRtfPdfBlocksBuilder(final PatternTransformate transformate, ReportTextBlockInflater inflater, final List<ReportParameter> params) {
		super(transformate, inflater, params);
	}
	
	@Override
	public ReportTextBlocksBuilder build() throws ReportBlockException {
		super.build();
		convert();
        return this;
	}

	/**
	 * http://blog.rubypdf.com/2009/11/24/convert-rtf-to-pdf-with-open-source-library-itext/
	 * http://stackoverflow.com/questions/1876678/itext-5-0-0-where-did-rtf-and-html-go
	 * 
	 * http://itext-general.2136553.n4.nabble.com/importRtfDocument-Unknown-Source-td2151636.html
	 */
	protected void convert() {
		try {
			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, os);
			document.open();
			
			RtfParser parser = new RtfParser(null);
			parser.convertRtfDocument(new ByteArrayInputStream(content), document);
			document.close();
			log.debug("Pdf document closed");
			content = os.toByteArray();			
		 } catch (DocumentException e) {
			 log.error("Error converting rtf into pdf." + e.getMessage());
		 } catch (IOException e) {
			 log.error("Error converting rtf into pdf (content not found)." + e.getMessage());
		 }
	}
}
