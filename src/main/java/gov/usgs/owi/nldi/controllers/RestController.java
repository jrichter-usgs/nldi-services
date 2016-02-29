package gov.usgs.owi.nldi.controllers;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.ResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gov.usgs.owi.nldi.dao.StreamingDao;
import gov.usgs.owi.nldi.dao.StreamingResultHandler;
import gov.usgs.owi.nldi.transform.MapToJsonTransformer;
import gov.usgs.owi.nldi.transform.Transformer;

@Controller
public class RestController {
	private static final Logger LOG = LoggerFactory.getLogger(RestController.class);

	protected final StreamingDao streamingDao;

	@Autowired
	public RestController(StreamingDao inStreamingDao) {
		streamingDao = inStreamingDao;
	}
	
	@RequestMapping(value="/navigation", method=RequestMethod.GET)
    public void navigation(HttpServletRequest request, HttpServletResponse response) {
		OutputStream responseStream = null;

		try {
			responseStream = new BufferedOutputStream(response.getOutputStream());
			
			Transformer transformer = new MapToJsonTransformer(responseStream, null);
			
			streamingDao.navigate("navigate", null);
			
			ResultHandler<?> handler = new StreamingResultHandler(transformer);
			streamingDao.stream("navigate", null, handler);
	
			transformer.end();

		} catch (Exception e) {
			LOG.error("Handle me better" + e.getLocalizedMessage(), e);
		} finally {
			if (null != responseStream) {
				try {
					responseStream.flush();
				} catch (IOException e) {
					//Just log, cause we obviously can't tell the client
					LOG.error("Error flushing response stream", e);
				}
			}
		}
    }

	@RequestMapping(value="/pointindexing", method=RequestMethod.GET)
    public String pointIndexing(HttpServletRequest request, HttpServletResponse response) {
		OutputStream responseStream = null;

//		try {
			
			streamingDao.navigate("pointIndexing", null);
			
//			ResultHandler<?> handler = new StreamingResultHandler(transformer);
//			streamingDao.stream("navigate", null, handler);
//	
//			transformer.end();
//
//		} catch (Exception e) {
//			LOG.error("Handle me better" + e.getLocalizedMessage(), e);
//		} finally {
//			if (null != responseStream) {
//				try {
//					responseStream.flush();
//				} catch (IOException e) {
//					//Just log, cause we obviously can't tell the client
//					LOG.error("Error flushing response stream", e);
//				}
//			}
//		}
			return "hi";
	}
}