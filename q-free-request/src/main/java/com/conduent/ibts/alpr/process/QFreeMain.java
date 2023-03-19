package com.conduent.ibts.alpr.process;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


/*
The BeanDefinitionOverrideException in Spring Boot

Bean Overriding
Spring beans are identified by their names within an ApplicationContext.

Therefore, bean overriding is a default behavior that happens when we define a bean within an ApplicationContext that has the 
same name as another bean. It works by simply replacing the former bean in case of a name conflict.

https://www.baeldung.com/spring-boot-bean-definition-override-exception
*/

//by default transaction management in spring boot is AUTO_COMMIT



/*
if there are many implementation of MasterCacheDao for example MasterCacheDaoImpl MasterCacheDaoImpl2 
and someone declares   MasterCacheDao masterCacheDao;   
then MasterCacheDaoImpl2 will be the definition 
to use because the implementation has declare to be @Primary ... however you can declare directly which
implementation to use for example
	
	@Qualifier("masterCacheDaoImpl2") //	//this tell you which implementation to use 
	@Resource
	MasterCacheDao masterCacheDao;
*/



@EnableAutoConfiguration(exclude = { ErrorMvcAutoConfiguration.class, BatchAutoConfiguration.class  })
@SpringBootApplication
//@EnableScheduling
public class QFreeMain  {

	private static final Logger logger = LoggerFactory.getLogger(QFreeMain.class);
	
	/*
	 * @Autowired QFreeQueueServiceImpl qFreeService;
	 */
	
	public static void main(String args[]) {
		
		logger.info("<<<<<<<< in QFreeMain.main() >>>>>>>>");
		
		//String cacertsLocation = "/home/app/WSDL_Files/cacerts";
		String cacertsLocation = "C:/workspacesSTD/springBootBatch/q-free-request/src/main/resources/dev_cert_config/cacerts";
		String cacertsPassword = "changeit";
		
		logger.info("Adding cacert location :{}",cacertsLocation );
		System.setProperty("javax.net.ssl.trustStore", cacertsLocation);// cacerts location
		System.setProperty("javax.net.ssl.trustStorePassword", cacertsPassword);// changeit
		System.setProperty("javax.net.ssl.keyStore", cacertsLocation);// cacerts location
		System.setProperty("javax.net.ssl.keyStorePassword", cacertsPassword);// changeit
		logger.info("Added cacert successfully");
		
		SpringApplication.run(QFreeMain.class, args);
		
		logger.info("<<<<<<<< exiting QFreeMain.main() >>>>>>>>\n\n");
		logger.info("\n");
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException
	{
		logger.info("<<<<<<<< in QFreeMain.restTemplate() >>>>>>>>");
		
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();

		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

		CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
	
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		
		requestFactory.setHttpClient(httpClient);
		requestFactory.setConnectTimeout(60000);
		requestFactory.setReadTimeout(60000);
		
		logger.info("<<<<<<<< exiting QFreeMain.restTemplate() >>>>>>>>");
		
		return new RestTemplate(requestFactory);
	}
	
	/*
	 * @Bean public TaskExecutor threadPoolTaskExecutor() { ThreadPoolTaskExecutor
	 * executor = new ThreadPoolTaskExecutor(); executor.setCorePoolSize(4);
	 * executor.setMaxPoolSize(10);
	 * executor.setThreadNamePrefix("qfree-process-thread"); executor.initialize();
	 * return executor; }
	 * 
	 * @Override public void run(String... args) throws Exception {
	 * logger.info("Started q-free job"); TaskExecutor threadPoolTaskExecutor =
	 * threadPoolTaskExecutor(); threadPoolTaskExecutor.execute(qFreeService); }
	 */
}