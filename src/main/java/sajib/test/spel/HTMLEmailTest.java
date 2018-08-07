package sajib.test.spel;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.config.BeanExpressionContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.expression.BeanExpressionContextAccessor;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class HTMLEmailTest {

	public String parseEmailContent(StringWriter emailTemplateContent) {	
		// populate spel context
		ConfigurableApplicationContext appContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		StandardEvaluationContext spelContext = new StandardEvaluationContext();
		spelContext.setBeanResolver(new BeanFactoryResolver(appContext.getBeanFactory()));
		spelContext.addPropertyAccessor(new BeanExpressionContextAccessor());
		BeanExpressionContext rootObject = new BeanExpressionContext(appContext.getBeanFactory(), null);
		// create spel expression parser instance
		ExpressionParser parser = new SpelExpressionParser();
		
		// search the fileContent string and find spel expressions, 
		// then evaluate the expressions
		Integer start = 0, braketStart = 0;
		StringBuffer sb = emailTemplateContent.getBuffer();
		while ((braketStart = sb.indexOf("#{", start)) > -1) {
			Integer braketClose = sb.indexOf("}", start);
			String expressionStr = sb.substring(braketStart + 2, braketClose);
			Expression expression = parser.parseExpression(expressionStr);
			String evaluatedValue = expression.getValue(spelContext, rootObject, String.class);
			sb.replace(braketStart, braketClose + 1, evaluatedValue);
			start = braketClose + evaluatedValue.length();
		}
		System.out.println(sb.toString());				
		return sb.toString();
	}
	
	public Properties getSmtpProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.mail.yahoo.com");
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", "465");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.fallback", "false");
		return props;
	}

	public Session getMailSession(Properties props) {
		Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("XXXXXX@yahoo.com", "xxxxxxxxxxx");
			}
		});
		mailSession.setDebug(true);
		return mailSession;
	}

	public void populateAndSendEmail(Session mailSession, String emailBody) {
		try {
			Message msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress("XXXXXX@yahoo.com"));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("YYYYYY@gmail.com"));
			msg.setSentDate(new Date());
			msg.setSubject("HTML Email with SPEL expression");
			msg.setContent(emailBody, "text/html");
			// Send the email using Transport
			Transport.send(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			HTMLEmailTest htmlEmailTest = new HTMLEmailTest();
			String emailTemplate = "emailTemplate.html";
			ClassLoader classLoader = htmlEmailTest.getClass().getClassLoader();
			File file = new File(classLoader.getResource(emailTemplate).getFile());
			StringWriter emailTemplateContent = new StringWriter();
			IOUtils.copy(new FileInputStream(new File(file.getAbsolutePath())), emailTemplateContent);
			
			// replace the SPEL expressions of email template with evaluated values
			String emailBody = htmlEmailTest.parseEmailContent(emailTemplateContent);			
			// populate SMTP server properties 
			Properties props = htmlEmailTest.getSmtpProperties();
			// generate email session
			Session mailSession = htmlEmailTest.getMailSession(props);
			// generate email message and send email
			htmlEmailTest.populateAndSendEmail(mailSession, emailBody);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
