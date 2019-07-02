package com.goldmooon.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoldMoonUtils
{

	private static final Logger		LOG				= LoggerFactory.getLogger(GoldMoonUtils.class);
	private static final String		PROPERTIES_FILE	= "context.properties";
	private static final Properties	PROPS			= new Properties();

	static
	{
		try
		{
			ClassLoader cl;
			cl = Thread.currentThread().getContextClassLoader();
			if ( cl == null )
			{
				cl = ClassLoader.getSystemClassLoader();
			}

			InputStream inputStream = cl.getResourceAsStream(PROPERTIES_FILE);
			PROPS.load(inputStream);
			LOG.info("PROPS.toString(): " + PROPS.toString());
			inputStream.close();
		}
		catch ( java.lang.Exception e )
		{
			LOG.error(e.getMessage(), e);
		}
	}

	public static class RootContext
	{
		public static final String ROOT_CONTEXT = PROPS.getProperty("root.context").trim();
	}





	public static String getClientIp(final HttpServletRequest request)
	{
		String ip = request.getHeader("X-Forwarded-For");
		if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) )
		{
			ip = request.getHeader("Proxy-Client-IP");
		}
		if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) )
		{
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) )
		{
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) )
		{
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if ( ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip) )
		{
			ip = request.getRemoteAddr();
		}
		return ip;
	}





	public static String convertHttpToHttps(final HttpServletRequest request) throws MalformedURLException
	{
		String defaultTargetUrl = "";

		URL aURL = new URL(request.getRequestURL().toString());

		LOG.info("=====> aURL.getPort(): " + aURL.getPort());
		defaultTargetUrl = "http://" + aURL.getAuthority();

		int httpsPort = Integer.parseInt(PROPS.getProperty("goldmooon.https.port").trim());

		if ( aURL.getPort() == httpsPort )
		{
			defaultTargetUrl = "https://" + aURL.getAuthority();
		}
		else
		{
			defaultTargetUrl = "http://" + aURL.getAuthority();
		}

		return defaultTargetUrl + request.getContextPath();
	}





	/**
	 * <pre>
	 * 1. 개요 : 패스워드 규칙.
	 * 1) 영어/숫자/특수문자"~\"`!@#$%^&*()_-+={}[]|;:\'<>,.?/\\"(대문자) 포함
	 *
	 * 3) 3가지 조합일땐 8자 이상
	 * 4) 꼭 영문자로 시작
	 * 5) 전화번호 포함 불가
	 * 6) 키보드 연속 배열 4개 이상 금지
	 * 2. 처리 내용 :
	 * </pre>
	 *
	 * @Method Name : passwordValidation
	 * @param pw
	 * @return
	 */

	public static boolean passwordValidation(final String pw)
	{

		int keyRepeatLimit = 4;

		List<Pattern> passwordCheck = new ArrayList<>(3);
		boolean returnValue = true;

		passwordCheck.add(Pattern.compile("^.*[0-9]{1,}.*$")); // 숫자 포함 ok
		passwordCheck.add(Pattern.compile("^.*[a-zA-Z]{1,}.*$")); // 영문자 포함 ok
		passwordCheck.add(Pattern.compile("^.*[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]{1,}.*$")); // 특수문자 포함 ok

		int maxCount = 0;
		int strLength = pw.length();

		for ( Pattern tempPattern : passwordCheck )
		{
			Matcher tempMatcher = tempPattern.matcher(pw);
			if ( tempMatcher.matches() )
			{
				maxCount++;
			}
		}

		boolean needKeyRepaet = keyRepeatValid(pw, keyRepeatLimit);

		if ( !(maxCount > 2 && strLength > 7) || !needKeyRepaet )
		{
			returnValue = false;
		}

		return returnValue;
	}





	public static boolean keyRepeatValid(final String value, final int limit)
	{
		if ( value == null || value.length() < limit )
		{
			return true;
		}

		for ( int i = 0; i <= value.length() - limit; i++ )
		{
			String token = value.substring(i, i + limit);
			boolean matchAll = true;
			for ( int j = 1; j < limit; j++ )
			{
				if ( token.charAt(0) != token.charAt(j) )
				{
					matchAll = false;
				}
			}
			if ( matchAll )
			{
				return false;
			}
		}

		return true;
	}





	public static int compareVersions(final String version1, final String version2)
	{
		String[] levels1 = version1.split("\\.");
		String[] levels2 = version2.split("\\.");

		int length = Math.max(levels1.length, levels2.length);
		for ( int i = 0; i < length; i++ )
		{
			Integer v1 = i < levels1.length ? Integer.parseInt(levels1[i]) : 0;
			Integer v2 = i < levels2.length ? Integer.parseInt(levels2[i]) : 0;
			int compare = v1.compareTo(v2);
			if ( compare != 0 )
			{
				return compare;
			}
		}

		return 0;
	}





	public static String toPrettyString(final String xml, final int indent)
	{
		try
		{
			// Turn xml string into a document
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

			// Remove whitespaces outside tags
			document.normalize();
			XPath xPath = XPathFactory.newInstance().newXPath();
			NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']", document, XPathConstants.NODESET);

			for ( int i = 0; i < nodeList.getLength(); ++i )
			{
				Node node = nodeList.item(i);
				node.getParentNode().removeChild(node);
			}

			// Setup pretty print options
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", indent);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// Return pretty print xml string
			StringWriter stringWriter = new StringWriter();
			transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
			return stringWriter.toString();
		}
		catch ( Exception e )
		{
			throw new RuntimeException(e);
		}
	}





	public static String getXmlPrettyString(final Document document, final int indent)
	{
		try
		{
			// Remove whitespaces outside tags
			document.normalize();
			XPath xPath = XPathFactory.newInstance().newXPath();
			NodeList nodeList = (NodeList) xPath.evaluate("//text()[normalize-space()='']", document, XPathConstants.NODESET);

			for ( int i = 0; i < nodeList.getLength(); ++i )
			{
				Node node = nodeList.item(i);
				node.getParentNode().removeChild(node);
			}

			// Setup pretty print options
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", indent);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");

			// Return pretty print xml string
			StringWriter stringWriter = new StringWriter();
			transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
			return stringWriter.toString();
		}
		catch ( Exception e )
		{
			throw new RuntimeException(e);
		}
	}





	public static HttpServletRequest getHttpRequest()
	{
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		// return ServletActionContext.getRequest();
	}





	public static HttpServletResponse getHttpResponse()
	{
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
		// return ServletActionContext.getResponse();
	}





	public static Object invokeGetter(final Object obj, final String param)
	{
		if ( param.contains(".") )
		{
			String[] params = param.split("\\.", 2);
			Object subObj = invokeGetter0(obj, params[0]);
			return invokeGetter(subObj, params[1]);
		}
		else
		{
			return invokeGetter0(obj, param);
		}
	}





	private static Object invokeGetter0(final Object obj, final String param)
	{
		Object result = null;
		try
		{
			if ( obj instanceof Map )
			{
				result = ((Map) obj).get(param);
			}
			else
			{
				String getterMethodName = "get" + Character.toUpperCase(param.charAt(0)) + param.substring(1);
				Method m = obj.getClass().getMethod(getterMethodName);
				if ( null != m )
				{
					result = m.invoke(obj);
				}
			}
		}
		catch ( NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e )
		{
			log.error(e.getMessage(), e);
		}
		return result;
	}

}
