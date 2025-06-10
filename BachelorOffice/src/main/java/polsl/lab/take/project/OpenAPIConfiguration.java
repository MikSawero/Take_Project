package polsl.lab.take.project;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfiguration {

   @Bean
   public OpenAPI defineOpenApi() {
       Server server = new Server();
       server.setUrl("http://localhost:8080");
       server.setDescription("Project Development");

       Contact myContact = new Contact();
       myContact.setName("Mikołaj Sawicz");
       myContact.setEmail("ms306698@student.polsl.pl");

       Info information = new Info()
               .title("Dean's Office API")
               .version("Beta")
               .description("This API exposes endpoints to manage a Dean's Office.")
               .contact(myContact);
       return new OpenAPI().info(information).servers(List.of(server));
   }
}
