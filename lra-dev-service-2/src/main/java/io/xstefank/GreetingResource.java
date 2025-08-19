package io.xstefank;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.lra.annotation.AfterLRA;
import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @LRA(value = LRA.Type.REQUIRED, end = false)
    public String hello(@HeaderParam(LRA.LRA_HTTP_CONTEXT_HEADER) String lraId) {
        System.out.println("GreetingResource.hello | " + "lraId = " + lraId);
        return "Hello from Quarkus REST with LRA! lraId = " + lraId;
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
