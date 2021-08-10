package com.in28minutes.microservices;

import java.util.function.Function;

import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.Buildable;
import org.springframework.cloud.gateway.route.builder.PredicateSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

	@Bean
	public RouteLocator gateWayRouter(RouteLocatorBuilder builder) {
		
		return builder
				.routes() 
				//route-1
				.route(p-> p.path("/get")
				.filters(f -> 
					f.addRequestHeader("MyHeader", "KolluHeader")
						.addRequestParameter("Myparam", "KolluParams")
						)
				.uri("http://httpbin.org:80"))
				//route-2
				.route(f->f.path("/currency-exchange/**")
						.uri("lb://currency-exchange-service")
						)
				//route-2
				.route(f->f.path("/currency-converter/**")
						.uri("lb://currency-conversion-service")
						)
				//route-3
				.route(f->f.path("/currency-converter-feign/**")
						.uri("lb://currency-conversion-service")
						)
				//route-4 urirewrite
				.route(f->f.path("/currency-converter-new/**")
						.filters(p ->p.rewritePath("/currency-converter-new/(?<segment>.*)", 
								"/currency-converter-feign/${segment}")
								)
						.uri("lb://currency-conversion-service")
						)
				.build();
	}
}
