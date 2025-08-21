package io.xstefank;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.lra.annotation.AfterLRA;
import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;

import java.util.Optional;

@Path("/hello")
public class GreetingResource {

    @ConfigProperty(name = "narayana.lra.base-uri")
    Optional<String> baseUri;

    @ConfigProperty(name = "quarkus.lra.base-uri")
    Optional<String> quarkusBaseUri;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @LRA
    public String lra(@HeaderParam(LRA.LRA_HTTP_CONTEXT_HEADER) String lraId) {
        System.out.println("GreetingResource.lra | " + "lraId = " + lraId);
        System.out.println("XXXXXXXXXXXXXXXx baseUri = " + (baseUri.isPresent() ? baseUri.get() : "not set"));
        System.out.println("XXXXXXXXXXXXXXXx quarkusBaseUri = " + (quarkusBaseUri.isPresent() ? quarkusBaseUri.get() : "not set"));
        callService2(lraId);
        return "Hello from Quarkus REST";
    }

    private static void callService2(String lraId) {
        Client client = ClientBuilder.newClient();
        client.target("http://localhost:8082/hello")
            .request()
            .header(LRA.LRA_HTTP_CONTEXT_HEADER, lraId)
            .get();
    }

    @PUT
    @Path("/complete")
    @Complete
    public Response complete(@HeaderParam(LRA.LRA_HTTP_CONTEXT_HEADER) String lraId,
                             @HeaderParam(LRA.LRA_HTTP_RECOVERY_HEADER) String recoveryId) {
        System.out.println("GreetingResource.complete | " + "lraId = " + lraId + ", recoveryId = " + recoveryId);
        return Response.ok().build();
    }

    @PUT
    @Path("/compensate")
    @Compensate
    public Response compensate(@HeaderParam(LRA.LRA_HTTP_CONTEXT_HEADER) String lraId,
                               @HeaderParam(LRA.LRA_HTTP_RECOVERY_HEADER) String recoveryId) {
        System.out.println("GreetingResource.compensate | " + "lraId = " + lraId + ", recoveryId = " + recoveryId);
        return Response.ok().build();
    }

    @PUT
    @Path("/after")
    @AfterLRA
    public Response after(@HeaderParam(LRA.LRA_HTTP_ENDED_CONTEXT_HEADER) String endedLraId) {
        System.out.println("GreetingResource.after | " + "endedLraId = " + endedLraId);
        return Response.ok().build();
    }
}

